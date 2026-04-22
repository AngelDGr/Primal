package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.entity.ai.WalrusAi;
import org.primal.entity.ai.controls.look.WaterOrLandLookControl;
import org.primal.entity.ai.controls.move.WaterOrLandMoveControl;
import org.primal.entity.ai.controls.navigation.WalrusPathNavigation;
import org.primal.networking.packets.WalrusJumpPacket;
import org.primal.registry.*;
import org.primal.sounds.WalrusSong;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.MobWithTransitionablePoseAnimations;
import org.primal.util.mob_types.SemiAquaticAnimal;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;

//xTODO
//You can put a saddle on the walrus.
// Walruses can also be mounted for a beastly steed, Slow but powerful on land and fast yet not as powerful underwater.
// With the walrus possessing a body slam ability that you charge via the stamina bar.
// Unlike Nautilus, Walrus cannot provide water breathing.
//When on land the attack does some heavy aerial damage and breaks near ice
//When danger is nearby and attacks a walrus, the rest flee into the ocean, while the victim fights back.
//Yet in water the attack transforms into a fast tusk jab with a small lunge forewards
//Walrus lay sometimes
//These large pinnipeds gather around Frozen Oceans, stony shores and Snowy Beaches.
//Walruses live in groups of 4-5 and watch out for danger.
//Sun Burned variant spawn in any non cold biome
//Breed with a bucket of Cod
// If you give a walrus a Conch shell, it’ll play it and play a song!
// Some of the songs will be meme references
public class WalrusEntity extends AbstractHorse implements VariantHolder<WalrusEntity.Variant>, MobWithTransitionablePoseAnimations, NeutralMob, SemiAquaticAnimal {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        BROWN(0, "brown"),
        SUNBURNT(1, "sunburnt");

        public static final Codec<WalrusEntity.Variant> CODEC = StringRepresentable.fromEnum(WalrusEntity.Variant::values);

        private static final IntFunction<WalrusEntity.Variant> BY_ID = ByIdMap.continuous(WalrusEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static WalrusEntity.Variant byId(int id) {
            return BY_ID.apply(id);
        }

        final int id;

        private final String name;

        Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    @Override
    public void setVariant(@NotNull WalrusEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull WalrusEntity.Variant getVariant() {
        return WalrusEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariantFromBiome(WalrusEntity animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_SUNBURNT_WALRUS)) {
            animal.setVariant(WalrusEntity.Variant.SUNBURNT);
        } else {
            animal.setVariant(WalrusEntity.Variant.BROWN);
        }
    }

    //──────────────────────────────────── Init ────────────────────────────────────
    public WalrusEntity(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
        // Avoid leaves
        this.setPathfindingMalus(PathType.LEAVES, 2.0F);

        // Prefer water / shore
        this.setPathfindingMalus(PathType.WATER, -2.0F);
        this.moveControl = new WaterOrLandMoveControl<>(this, 85, 50, 0.58f, 0.01f, false);
        this.lookControl = new WaterOrLandLookControl<>(this, 10);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40f)
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                .add(Attributes.ATTACK_DAMAGE, 3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.4f)
                .add(Attributes.STEP_HEIGHT, 1.5f)
                .add(Attributes.JUMP_STRENGTH, 0.5F);
    }

    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK_LAYING = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.LONG);
    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK_SWIM_IDLE = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WHIRLWIND_DURATION = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SLAM_DURATION = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> JUMP_COOLDOWN = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> JUMP_SCALE = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> SONG_ID = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_INSTRUMENT = SynchedEntityData.defineId(WalrusEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LAST_POSE_CHANGE_TICK_LAYING, 0L);
        builder.define(LAST_POSE_CHANGE_TICK_SWIM_IDLE, 0L);
        builder.define(DATA_VARIANT_ID, WalrusEntity.Variant.BROWN.id);
        builder.define(WHIRLWIND_DURATION, 0);
        builder.define(SLAM_DURATION, 0);
        builder.define(JUMP_COOLDOWN, 0);
        builder.define(JUMP_SCALE, 0f);
        builder.define(SONG_ID, WalrusSong.OIIA_OIIA.id);
        builder.define(HAS_INSTRUMENT, false);
    }

    public void setJumpScale(float jumpScale) {
        this.entityData.set(JUMP_SCALE, jumpScale);
    }

    public float getJumpScale() {
        return this.entityData.get(JUMP_SCALE);
    }

    public void setWhirlwindDuration(int whirlwindDuration) {
        this.entityData.set(WHIRLWIND_DURATION, whirlwindDuration);
    }

    public int getWhirlwindDuration() {
        return this.entityData.get(WHIRLWIND_DURATION);
    }

    public void setSlamDuration(int slamDuration) {
        this.entityData.set(SLAM_DURATION, slamDuration);
    }

    public int getSlamDuration() {
        return this.entityData.get(SLAM_DURATION);
    }

    public void setJumpCooldown(int jumpCooldown) {
        this.entityData.set(JUMP_COOLDOWN, jumpCooldown);
    }

    public int getSongId() {
        return this.entityData.get(SONG_ID);
    }

    public void setSongId(int songId) {
        this.entityData.set(SONG_ID, songId);
    }

    public boolean hasInstrument() {
        return this.entityData.get(HAS_INSTRUMENT);
    }

    public void setHasInstrument(boolean hasInstrument) {
        this.entityData.set(HAS_INSTRUMENT, hasInstrument);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null)
            spawnGroupData = new AgeableMob.AgeableMobGroupData(0.4f);

        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addAdditionalSaveDataTransitionablePoseAnimations(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putInt("WhirlwindDuration", this.getWhirlwindDuration());
        compound.putInt("SlamDuration", this.getSlamDuration());
        compound.putInt("JumpCooldown", this.getJumpCooldown());
        compound.putFloat("JumpScale", this.getJumpScale());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readAdditionalSaveDataTransitionablePoseAnimations(compound);
        this.setVariant(WalrusEntity.Variant.byId(compound.getInt("Variant")));
        this.setWhirlwindDuration(compound.getInt("WhirlwindDuration"));
        this.setSlamDuration(compound.getInt("SlamDuration"));
        this.setJumpCooldown(compound.getInt("JumpCooldown"));
        this.setJumpScale(compound.getInt("JumpScale"));
    }

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<WalrusEntity> brainProvider() {
        return WalrusAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return WalrusAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<WalrusEntity> getBrain() {
        return (Brain<WalrusEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        //Starts the brain
        Brain<WalrusEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);
        WalrusAi.updateActivity(this);

        //Drops the item if player leaves before ending the behavior
        if(!brain.isActive(Primal_Activities.PLAY.get()) && !this.getMainHandItem().isEmpty()){
            BehaviorUtils.throwItem(this, this.getMainHandItem().copy(), this.position().add(0, 1, 0));
            this.getMainHandItem().shrink(1);
        }

        //Makes it sprint when attacking or running on land
        if(!this.isInWater())
            this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() || brain.getMemory(MemoryModuleType.AVOID_TARGET).isPresent());
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

        //Removes the mount
        if(!this.isTamed() && this.getFirstPassenger()!=null) this.becomesMadOrTame();

        //Bubble spiral
        if (this.hasPose(Pose.SPIN_ATTACK) && this.level() instanceof ServerLevel serverLevel) {
            float yawRad = this.yBodyRot * Mth.DEG_TO_RAD;

            // Direction vector (behind the mob)
            double backX = -Mth.sin(yawRad);
            double backZ =  Mth.cos(yawRad);

            int points = 12;
            double radius = 0.8;
            double heightStep = 0.06;
            double backDistance = -2.6;

            for (int i = 0; i < points; i++) {
                double angle = (i * 0.6) + (this.tickCount * 0.3);
                double spiralX = Math.cos(angle) * radius;
                double spiralZ = Math.sin(angle) * radius;

                double x = this.getX()
                        + backX * backDistance
                        + spiralX;
                double y = this.getY() + 0.3 + (i * heightStep);
                double z = this.getZ()
                        + backZ * backDistance
                        + spiralZ;

                serverLevel.sendParticles(
                        ParticleTypes.BUBBLE,
                        x, y, z,
                        1,
                        0, 0, 0,
                        0.0
                );
            }
        }

        this.setHasInstrument(this.isInstrument(this.getMainHandItem()));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(!this.level().isClientSide){
            //Stop the whirlwind
            boolean isWhirlwind=false;
            //Stops when hitting a wall or the attack must stop
            if((this.horizontalCollision || this.autoSpinAttackTicks<=0) && this.getWhirlwindDuration()>0){
                this.setWhirlwindDuration(0);
                this.stopWhirlwind();
            }

            if(this.getWhirlwindDuration()>0){
                this.setWhirlwindDuration(this.getWhirlwindDuration() - 1);
                isWhirlwind=true;
            }
            if(getWhirlwindDuration()==0 && isWhirlwind)
                this.stopWhirlwind();

            //Does the slam
            boolean isSlam=false;
            if(this.getSlamDuration()>0){
                this.setSlamDuration(this.getSlamDuration() - 1);
                isSlam=true;
            }
            if(getSlamDuration()==0 && isSlam){
                this.doSlamAttackDamage(this.getJumpScale());
            }
        }

        if (this.getJumpCooldown() > 0) {
            this.setJumpCooldown(this.getJumpCooldown()-1);
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WalrusPathNavigation(this, level);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if ((this.isEffectiveAi() || this.getWhirlwindDuration()>0) && this.isInWater()) {
            this.moveRelative(this.hasControllingPassenger()? this.getJumpScale()*0.35f:  this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        else super.travel(travelVector);
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.3f : super.getSpeed();
    }

    public void stopMoving(){
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getNavigation().stop();
        this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    public boolean refuseToMove() {
        return hasInstrument() || this.isOnPoseTransition();
    }

    public boolean isSeeingTarget(LivingEntity target) {
        Vec3 vec3d = target.position();

        Vec3 vec3d2 = this.calculateViewVector(0.0f, this.getYHeadRot());
        Vec3 vec3d3 = vec3d.vectorTo(this.position());
        vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();

        return vec3d3.dot(vec3d2) < -0.92;
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        //To not attack Hucha, lol
        if(target instanceof Player player
                && (player.getGameProfile().getName().equals("Hucha") || player.getGameProfile().getName().equals("hucha")))
            return false;

        //To not attack owner
        if(this.getOwner()!=null && this.getOwner().equals(target)) return false;

        //To stop attacking
        if(target.getType().equals(this.getType()) && target.getHealth()<=target.getMaxHealth()*0.30)
            return false;

        return (this.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || this.getBrain().isMemoryValue(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), target))
                && super.canAttack(target);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if(hurt){
            this.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 40);

            if (source.getEntity() instanceof LivingEntity target && !(target instanceof Player player && player.isCreative()))
                WalrusEntity.wasHurtBy(this, target);
        }

        return hurt;
    }

    public static <T extends Mob> void wasHurtBy(T self, LivingEntity target) {
        var brain = self.getBrain();

        brain.eraseMemory(MemoryModuleType.PACIFIED);
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
        brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.HURT_RECENTLY.get(), true, 200);

        //If baby, escapes, otherwise attacks
        if (self.isBaby())
            Primal_Util.Ai.retreatFromNearestTarget(self, target);
        else
            brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);

        //Alerts all nearby walrus
        alertWalruses(self, target);
    }

    public static <T extends Mob> void alertWalruses(T self, LivingEntity target){
        for (WalrusEntity nearbyWalrus : getNearestWalruses(self)) {
            Primal_Util.Ai.retreatFromNearestTarget(nearbyWalrus, target);
        }
    }

    private static <T extends Mob> List<WalrusEntity> getNearestWalruses(T self) {
        return self.level()
                .getEntitiesOfClass(
                        WalrusEntity.class,
                        self.getBoundingBox().inflate(30, 5, 30)
                )
                .stream()
                .filter(walrus -> walrus!=self && !walrus.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && !walrus.isTamed())
                .toList();
    }

    public void doSlamAttackDamage(float playerJumpPendingScale){
        float damage= 4 + (playerJumpPendingScale*2);
        double lateralExpansion = 2.0 + (playerJumpPendingScale*1);
        double yExpansion = 2;
        double knockbackStrength = 1.4 + (playerJumpPendingScale*2);
        DamageSource damageSource = this.damageSources().mobAttack(this);

        final List<Entity> listMobs= this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(lateralExpansion,yExpansion,lateralExpansion),
                entity -> {
                    if(entity instanceof WalrusEntity || this.hasPassenger(entity)) return false;

                    return entity != this;
                }
        );
        for (final Entity entity : listMobs){
            final double d = this.getX() - entity.getX();
            final double e = this.getZ() - entity.getZ();
            if(entity instanceof final LivingEntity livingEntity) {

                livingEntity.knockback(knockbackStrength, d, e);
                //Push the player
                if (entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()) {
                    ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
                }
                //Removes the shield
                if (livingEntity.isBlocking() && entity instanceof Player) {
                    ((Player) entity).disableShield();
                }
                //Checks if the entity it's blocking, to block the damage
                else if (!livingEntity.isBlocking()) {
                    entity.hurt(damageSource, damage);
                }

                //Destroys other no-living entities
            } else if(entity instanceof VehicleEntity || entity instanceof EndCrystal || entity instanceof HangingEntity) {
                entity.hurt(damageSource, 50.0f);
            } else {
                return;
            }
        }

        this.playSound(Primal_Sounds.WALRUS_SLAM.get(), 1.0f, 1.0f);

        // --- Break ice blocks below the slam ---
        if (!this.level().isClientSide()) {
            ServerLevel level = (ServerLevel) this.level();

            BlockPos center = this.blockPosition();
            int radius = 2;   // horizontal radius
            int depth = 1;    // blocks below the feet

            int radiusSq = radius * radius;

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {

                    // circle check (XZ plane)
                    if (dx * dx + dz * dz > radiusSq) {
                        continue;
                    }

                    for (int dy = 0; dy <= depth; dy++) {
                        BlockPos pos = center.offset(dx, -dy, dz);
                        BlockState state = level.getBlockState(pos);

                        if (state.is(Primal_Tags.Block.WALRUS_SLAM_CAN_BREAK)) {
                            level.destroyBlock(pos, false, this);
                            level.levelEvent(2001, pos, Block.getId(state));
                        }
                    }
                }
            }
        }
    }

    public AABB getSlamAttackBoundingBox() {
        AABB aabb = super.getAttackBoundingBox();
        return aabb.inflate(0.55, 0.0, 0.55);
    }

    public void doWhirlwind(float playerJumpPendingScale){
        //Do push
        float f = playerJumpPendingScale==-1? 1.5f: playerJumpPendingScale;

        float f7 = this.getYRot();
        float f1 = this.getXRot();
        float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
        float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
        float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
        float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
        f2 *= f / f5;
        f3 *= f / f5;
        f4 *= f / f5;
        Vec3 current = this.getDeltaMovement();
        Vec3 impulse = new Vec3(f2, f3, f4);
        this.setDeltaMovement(current.add(impulse));
        this.hasImpulse = true;

        //Start
        this.autoSpinAttackTicks = 20;
        this.autoSpinAttackDmg = 4.0F + (playerJumpPendingScale*2);
        this.invulnerableTime= 20;
        this.playSound(this.getWhirlwindSound(), 1f, 1f);

        //Set pose
        this.setPose(Pose.SPIN_ATTACK);
    }

    public void stopWhirlwind(){
        //Removes invulnerableTime
        if(invulnerableTime>0) this.invulnerableTime=0;
        this.setPose(Pose.STANDING);
        this.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(), true, 20);
    }

    public int getAutoSpinAttackTicks(){
        return autoSpinAttackTicks;
    }

    @Override
    protected void doAutoAttackOnTouch(@NotNull LivingEntity target) {
        target.hurt(this.damageSources().mobAttack(this), autoSpinAttackDmg);
    }

    @Override
    protected void checkAutoSpinAttack(@NotNull AABB boundingBoxBeforeSpin, @NotNull AABB boundingBoxAfterSpin) {
        AABB aabb = boundingBoxBeforeSpin.minmax(boundingBoxAfterSpin);

        //To not select the rider
        List<Entity> list = this.level().getEntities(this, aabb).stream().filter(select-> !this.hasPassenger(select)).toList();
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof LivingEntity) {
                    this.doAutoAttackOnTouch((LivingEntity)entity);
                    this.autoSpinAttackTicks = 0;
                    this.setDeltaMovement(this.getDeltaMovement().scale(-0.2));
                    break;
                }
            }
        } else if (this.horizontalCollision) {
            this.autoSpinAttackTicks = 0;
        }

        if (!this.level().isClientSide && this.autoSpinAttackTicks <= 0) {
            this.setLivingEntityFlag(4, false);
            this.autoSpinAttackDmg = 0.0F;
            this.autoSpinAttackItemStack = null;
        }
    }

    //──────────────────────────────────── Feeding & Interaction ────────────────────────────────────
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        //Default
        if (this.isVehicle()) {
            return super.mobInteract(player, hand);
        }
        else if(this.isInstrument(stackInHand) && !this.isBaby()){

            int size = WalrusSong.values().length;
            int previous = this.getSongId();

            int next = this.level().random.nextIntBetweenInclusive(0, size - 2);
            if (next >= previous) next++;

            this.setSongId(next);

            WalrusSong song = WalrusSong.byId(this.getSongId());

            if(song==null)
                return InteractionResult.FAIL;

            this.setItemInHand(InteractionHand.MAIN_HAND, stackInHand.copy());
            stackInHand.shrink(1);

            //Triggers advancement
            if(player instanceof ServerPlayer serverPlayer)
                Primal_Advancements.WALRUS_PLAYS.get().trigger(serverPlayer);
            this.brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.HAS_INSTRUMENT.get(), true, song.lengthInSeconds);

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        //To handle food logic
        else if(this.isFood(stackInHand)){
            boolean wasFeed = this.handleEating(player, stackInHand);
            if (wasFeed) {
                this.usePlayerItem(player, hand, stackInHand);
                return InteractionResult.SUCCESS;
            }
        }
        //To open the inventory if tamed
        else if (this.isTamed() && player.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        //To ride the walrus
        else {
            if (!stackInHand.isEmpty()) {
                InteractionResult interactionResult = stackInHand.interactLivingEntity(player, this, hand);
                if (interactionResult.consumesAction()) return interactionResult;
            }

            if(!this.isBaby()){
                this.doPlayerRide(player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    protected boolean handleEating(@NotNull Player player, @NotNull ItemStack stack) {
        if (this.isFood(stack)) {
            //To try to mate
            if(isMatingFood(stack)){
                //Breeding
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);
                    return playEatingSound();
                }
                //AgeUp
                else if(this.isBaby()){
                    int i = this.getAge();
                    this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                    return playEatingSound();
                }
            }
            //Only when tamed
            else if (this.isTamed() && !this.level().isClientSide) {
                //To heal the walrus
                if(isHealFood(stack)){
                    if (this.getHealth() < this.getMaxHealth()) {
                        FoodProperties foodProperties = stack.getFoodProperties(this);
                        float nutrition=0;
                        if(foodProperties!=null)
                            nutrition = foodProperties.nutrition()/2f;

                        this.heal(1f + nutrition);

                        return playEatingSound();
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected void usePlayerItem(@NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack) {
        if (isMatingFood(stack)) player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
        else super.usePlayerItem(player, hand, stack);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return isMatingFood(stack) || isHealFood(stack);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return stack.is(ItemTags.FISHES);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.COD_BUCKET);
    }

    public boolean isInstrument(@NotNull ItemStack stack) {
        return stack.is(Primal_Tags.Item.WALRUS_INSTRUMENT);
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return otherAnimal != this && otherAnimal instanceof WalrusEntity walrus && this.canParent() && walrus.canParent();
    }

    @Override
    protected boolean canParent() {
        return !this.isVehicle() && !this.isPassenger() && !this.isBaby() && this.isInLove();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof WalrusEntity otherParentCasted)
            return Primal_Util.createFromParents(Primal_Entities.WALRUS.get(),
                    this,
                    otherParentCasted, c-> WalrusAi.initMemories(c, level.getRandom()));

        return null;
    }

    //──────────────────────────────────── Mount Behavior ────────────────────────────────────
    private void becomesMadOrTame() {
        final float TAME_CHANCE = 0.1f;   // 10%
        final float MAD_CHANCE  = 0.2f;   // 20%
        // remaining 70% = do nothing

        if (this.getFirstPassenger() == null || this.tickCount % 20 != 0 || !(this.getFirstPassenger() instanceof LivingEntity passenger)) return;

        float roll = this.getRandom().nextFloat();

        if (roll < TAME_CHANCE && passenger instanceof Player player) {
            // Tame
            this.level().broadcastEntityEvent(this, (byte) 7);
            this.tameWithName(player);
            this.brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
        }
        else if (roll < TAME_CHANCE + MAD_CHANCE) {
            // Mad
            passenger.stopRiding();
            this.level().broadcastEntityEvent(this, (byte) 6);

            if (this.getAngrySound() != null)
                this.playSound(this.getAngrySound(), 1.0f, 1.0f);

            //Runs from the passenger
            this.brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, passenger, 200);
            alertWalruses(this, passenger);
        }
        // else → do nothing
    }

    @Override
    protected boolean canPerformRearing() {
        return false;
    }

    @Override
    public boolean canEatGrass() {
        return false;
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yBodyRot = this.yHeadRot = this.getYRot();

        //Avoid walrus control when whirlwind
        if(this.getWhirlwindDuration()>0) return;

        controlWalrus(travelVector);
    }

    protected void controlWalrus(Vec3 travelVector) {
        if (this.getControllingPassenger() instanceof Player rider) {
            //If moves
            if(travelVector.z > 0.0){
                float pitch = rider.getXRot();
                float yaw = rider.getYRot();

                float fYaw = -Mth.sin(yaw * Mth.DEG_TO_RAD);
                float fPitchY = -Mth.sin(pitch * Mth.DEG_TO_RAD);
                float fYawCos = Mth.cos(yaw * Mth.DEG_TO_RAD);
                float fPitchCos = Mth.cos(pitch * Mth.DEG_TO_RAD);

                double speed = this.getRiddenSpeed(rider);

                Vec3 direction = new Vec3(fYaw * fPitchCos, fPitchY, fYawCos * fPitchCos).normalize().scale(speed);

                this.setDeltaMovement(direction);
            }

            if (this.onGround() || this.isInWater()) {
                this.setIsJumping(false);
                if (this.playerJumpPendingScale > 0.0F && !this.isJumping())
                    this.executeRidersJump(this.playerJumpPendingScale, travelVector);

                this.playerJumpPendingScale = 0.0F;
            }
        }
    }

    @Override
    public @NotNull Vec3 getFluidFallingAdjustedMovement(double gravity, boolean isFalling, @NotNull Vec3 deltaMovement) {
        return deltaMovement.scale(0.94);
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if (this.isSaddled() && (this.onGround() || this.isInWater())) {
            super.onPlayerJump(jumpPower);
        }
    }

    @Override
    public int getJumpCooldown() {
        return this.entityData.get(JUMP_COOLDOWN);
    }

    @Override
    protected void executeRidersJump(float playerJumpPendingScale, @NotNull Vec3 travelVector) {
        net.neoforged.neoforge.common.CommonHooks.onLivingJump(this);
        PacketDistributor.sendToServer(new WalrusJumpPacket(this.getId(), playerJumpPendingScale));
    }

    @Override
    protected @NotNull Vec3 getPassengerAttachmentPoint(@NotNull Entity entity, @NotNull EntityDimensions dimensions, float partialTick) {
        //TODO: Synch better the ground pound animation with the offset.
        // The animation last 0.5s (500L accumulated time)
        // - Tenebris
        float maxSlam = 10.0f;
        float peakTime = 0.25f;

        float slam = Mth.clamp(this.getSlamDuration() - partialTick, 0.0f, maxSlam);
        float t = 1.0f - (slam / maxSlam); // 0 → 1

        float peak;
        if (t < peakTime) {
            peak = t / peakTime;
        } else {
            peak = (1.0f - t) / (1.0f - peakTime);
        }
        peak = Mth.clamp(peak, 0.0f, 1.0f);

        float baseBack = -0.6f;
        float extraBack = -0.55f * peak;

        float back = baseBack + extraBack;

        Vec3 offset = new Vec3(0.0, -0.85, back)
                .yRot(-this.getYRot() * Mth.DEG_TO_RAD);

        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick)
                .add(offset);
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player player, @NotNull Vec3 travelVector) {
        //Just has the normal travel vector
        if(this.getWhirlwindDuration()>0) return travelVector;

        //Stop
        if(this.getSlamDuration()>0) return Vec3.ZERO;

        if(this.isInWater() || this.onGround()){
            float f = player.xxa * 0.5F;
            float g = player.zza;
            if (g <= 0.0F) {
                g *= 0.25F;
            }

            return new Vec3(f, 0.0, g);
        }
        else {
            return Vec3.ZERO;
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player player) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isInWater()? 1.5f: 0.4f);
    }

    //──────────────────────────────────── Visuals ────────────────────────────────────
    public final AnimationState idleAnimationState = new AnimationState();

    public final AnimationState startLayingAnimationState = new AnimationState();
    public final AnimationState layingAnimationState = new AnimationState();
    public final AnimationState stopLayingAnimationState = new AnimationState();

    public final AnimationState startSwimIdleAnimationState = new AnimationState();
    public final AnimationState swimIdleAnimationState = new AnimationState();
    public final AnimationState stopSwimIdleAnimationState = new AnimationState();

    public final AnimationState playingAnimationState = new AnimationState();

    public final AnimationState swimAttackAnimationState = new AnimationState();

    public final AnimationState groundPoundAnimationState = new AnimationState();

    private int swimIdleTimer=0;
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        }

        if(this.level().isClientSide()) return;

        double speed = this.getDeltaMovement().length();
        if((speed<0.08) && !this.hasPose(Pose.SPIN_ATTACK) && !this.isAggressive() && this.isInWaterOrBubble() && !this.isVehicle()){
            //Needs to remain still for at least 1s
            swimIdleTimer++;
            if(!this.isSwimIdling() && swimIdleTimer>20)
                this.startAnimation("SwimIdle");
        }
        else if(this.isSwimIdling() && !this.isAnimationInProgress()) {
            this.stopAnimation("SwimIdle");
            swimIdleTimer=0;
        }
    }

    public static final byte SWIM_ATTACK=4;
    public static final byte GROUND_POUND=5;
    @Override
    public void handleEntityEvent(byte id) {
        if (id == GROUND_POUND) {
            this.groundPoundAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void setupAnimationStates() {
        this.transitionablePoseAnimationsSetupAnimationStates();
        this.playingAnimationState.animateWhen(this.hasInstrument(), this.tickCount);
        this.swimAttackAnimationState.animateWhen(this.hasPose(Pose.SPIN_ATTACK), this.tickCount);
        this.idleAnimationState.animateWhen(!this.swimAttackAnimationState.isStarted() && !this.layingAnimationState.isStarted(), this.tickCount);
        if(this.groundPoundAnimationState.getAccumulatedTime()>=375L && this.groundPoundAnimationState.getAccumulatedTime()<=425L){
            BlockPos groundPos = this.getOnPos();
            BlockState groundState = this.level().getBlockState(groundPos);
            if (groundState.isAir() || groundState.liquid()) return;

            spawnGroundPoundParticles(this.level(), this, groundState);
        }
    }

    @Override
    public Map<String, TransitionablePoseAnimation> transitionableAnimations() {
        return Map.of(
                "Laying",
                new MobWithTransitionablePoseAnimations.TransitionablePoseAnimation(this.isLaying(),
                        Pose.CROAKING, LAST_POSE_CHANGE_TICK_LAYING,
                        startLayingAnimationState, layingAnimationState, stopLayingAnimationState, 20),
                "SwimIdle",
                new MobWithTransitionablePoseAnimations.TransitionablePoseAnimation(this.isSwimIdling(),
                        Pose.SNIFFING, LAST_POSE_CHANGE_TICK_SWIM_IDLE,
                        startSwimIdleAnimationState, swimIdleAnimationState, stopSwimIdleAnimationState, 5));
    }

    private boolean isLaying() {
        return this.hasPose(Pose.CROAKING);
    }

    private static void spawnGroundPoundParticles(Level level, WalrusEntity walrus, BlockState groundState) {
        Vec3 center = walrus.position();

        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, groundState);

        int points = 80;
        double radius = 1.5;
        double y = 0.05;

        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 * i) / points;

            double px = center.x + Math.cos(angle) * radius;
            double pz = center.z + Math.sin(angle) * radius;

            level.addParticle(
                    particle,
                    px,
                    center.y + y,
                    pz,
                    0.0,
                    0.15,
                    0.0
            );
        }
    }

    private boolean isSwimIdling(){
        return this.hasPose(Pose.SNIFFING);
    }

    public boolean isAnimationInProgress(){
        return this.startSwimIdleAnimationState.isStarted() || this.stopSwimIdleAnimationState.isStarted();
    }

    //──────────────────────────────────── Spawning ────────────────────────────────────
    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    public static boolean checkWalrusSpawnRules(
            EntityType<WalrusEntity> walrus, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        Holder<Biome> holder = level.getBiome(pos);
        return (MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))
                && level.getBlockState(pos.below()).is(Primal_Tags.Block.WALRUS_SPAWN_ON);
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    @Override
    public boolean canBeLeashed() {
        return !isAggressive();
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.5f, this.getBbWidth() * 0.2F);
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        return level.getBlockState(pos.below()).is(Primal_Tags.Block.WALRUS_SPAWN_ON) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isInWater()? 1: 70;
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInWater()? 1: 5;
    }

    @Override
    public int getMaxAirSupply() {
        return 12000;
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        return Math.min(currentAir + 40, this.getMaxAirSupply());
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!this.level().isClientSide && this.isTamed()) {
            player.openHorseInventory(this, this.inventory);
        }
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        int i = this.calculateFallDamage(fallDistance, multiplier);
        if (i <= 0) {
            return false;
        } else {
            this.hurt(source, (float)i);
            if (this.isVehicle()) {
                for (Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(source, (float)i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        //To not do sounds when playing music
        if(this.hasInstrument())
            return null;

        if(this.random.nextIntBetweenInclusive(0, 10)==0)
            return Primal_Sounds.WALRUS_WHISTLE.get();

        if(this.random.nextIntBetweenInclusive(0, 50)==0)
            return Primal_Sounds.WALRUS_WHISTLE_RARE.get();

        return Primal_Sounds.WALRUS_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.WALRUS_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.WALRUS_DEATH.get();
    }

    @Override
    public SoundEvent getEatingSound() {
        return Primal_Sounds.WALRUS_EAT.get();
    }

    @Override
    protected @Nullable SoundEvent getAngrySound() {
        return null;
    }

    public SoundEvent getWhirlwindSound() {
        return Primal_Sounds.WALRUS_WHIRLWIND.get();
    }

    public boolean playEatingSound(){
        //Play the sound
        if (!this.isSilent()) {
            SoundEvent soundEvent = this.getEatingSound();
            if (soundEvent != null) {
                this.level()
                        .playSound(
                                null, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
                        );
            }
        }

        return true;
    }

    @Override
    protected void playJumpSound() {
//        super.playJumpSound();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        if (!block.liquid()) {
            BlockState blockstate = this.level().getBlockState(pos.above());
            SoundType soundtype = block.getSoundType(level(), pos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level(), pos, this);

                this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
            } else {
                this.playSound(Primal_Sounds.WALRUS_STEP.get(), 0.15F, 1.0f);
            }
        }
    }

    //──────────────────────────────────── Neutral Behavior ────────────────────────────────────
    @Override
    public int getRemainingPersistentAngerTime() {return 0;}

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {}

    @Override
    public @Nullable UUID getPersistentAngerTarget() {return null;}

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {}

    @Override
    public void startPersistentAngerTimer() {}
}