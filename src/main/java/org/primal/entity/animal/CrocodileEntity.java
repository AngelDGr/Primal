package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.entity.CrocodileAnimations;
import org.primal.entity.ai.CrocodileAi;
import org.primal.entity.ai.controls.*;
import org.primal.registry.*;
import org.primal.util.HostileMount;
import org.primal.util.MiscUtil;
import org.primal.util.VariantHolderPrimal;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;

//xTODO
// x Fix movement and set basic behaviors
// x Egg block
// x Create the lay egg behavior
// x Hide beneath in river reeds
// x Attack behavior
//        x Attack specific prey mobs each x ticks, like sheeps, pigs, chickens and so
//        x Attack any mob that gets too close unless sneaking
//        x Attack any mob that gets too close to their eggs or children
//        x Attack any mob that attack their children
// x Store mob drops, it can be recovered using a feather
// x Specific Items eaten
//      x Compass goes to destination
//      x Clock makes it do tick-tock sounds
//      ? Eating a disc make it create a new swampy disc
// x Become chill after giving them breeding food
// ? They need music to breed
// x Baby crocodiles passive
// x Crocodile scutes dropped when growing up or when brushed (2-3 pieces)
// x Scaly blocks from scutes
// x Scaly blocks blast resistance but instamined
// x Heavyweight effect, from shark tooth, lmao
//      x Make density enchantment better
//      x Entity falls faster
//      x Sink faster in water
//      x Inflicted from Ominous Trial Spawner’s projectile throw.
public class CrocodileEntity extends Animal implements VariantHolder<CrocodileEntity.Variant>, GeoEntity, ContainerListener, NeutralMob, HostileMount, VariantHolderPrimal<CrocodileEntity.Variant, CrocodileEntity> {

    protected SimpleContainer inventory=new SimpleContainer(54);
    @Override
    public void containerChanged(@NotNull Container container) {}

    //Attributes and Variants
    public enum Variant implements StringRepresentable {
        GREEN(0, "green"),
        BROWN(1, "brown"),
        BLACK(2, "black"),
        ALBINO(3,"albino");

        public static final Codec<CrocodileEntity.Variant> CODEC = StringRepresentable.fromEnum(CrocodileEntity.Variant::values);

        private static final IntFunction<CrocodileEntity.Variant> BY_ID = ByIdMap.continuous(CrocodileEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static CrocodileEntity.Variant byId(int id) {
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40f)
                .add(Attributes.MOVEMENT_SPEED, 0.20f)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4f)
                .add(Attributes.STEP_HEIGHT, 1.5f);
    }

    //Init
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public CrocodileEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.setPathfindingMalus(PathType.WATER, 1.0F);
        this.moveControl = new WaterOrLandMoveControl(this, 85, 10, 0.18f, 0.01f, false);
        this.lookControl = new WaterOrLandLookControl(this, 10);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData) {
        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void setVariantFromBiome(CrocodileEntity crocodile, Holder<Biome> holder){
        if (holder.is(Primal_Tags.SPAWNS_BLACK_CROCODILE)) {
            crocodile.setVariant( Variant.BLACK);
        } else if(holder.is(Primal_Tags.SPAWNS_BROWN_CROCODILE)){
            crocodile.setVariant(Variant.BROWN);
        } else {
            crocodile.setVariant(Variant.GREEN);
        }
    }

    @Override
    public CrocodileEntity.Variant getRareVariant(CrocodileEntity crocodile) {
        return Variant.ALBINO;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                CrocodileAnimations.mainController(this)
                        .receiveTriggeredAnimations()
                        .triggerableAnim("vomits", CrocodileAnimations.VOMITS)
                        .triggerableAnim("basking_start", CrocodileAnimations.BASKING_START)
                        .triggerableAnim("basking_end", CrocodileAnimations.BASKING_END),

                new AnimationController<>(this, "attack", state -> PlayState.STOP)
                        .triggerableAnim("attack", CrocodileAnimations.ATTACK)
                        .triggerableAnim("attack_water", CrocodileAnimations.ATTACK_UNDERWATER));
    }

    @Override
    public int getMaxHeadXRot() {
        return 25;
    }

    @Override
    public int getMaxHeadYRot() {
        return 10;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void swing(@NotNull InteractionHand hand) {
        this.triggerAnim("attack", this.isInWater()?  "attack_water": "attack");
        super.swing(hand);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        return super.doHurtTarget(entity);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasClock() && this.tickCount % 20 == 0) {
            // Alternate pitch each tick
            float pitch = (this.tickCount / 20) % 2 == 0 ? 0.6f : 0.5f;

            this.playSound(Primal_Sounds.CROCODILE_CLOCK.get(), 1.0f, pitch);

//            this.level().addParticle(
//                    ParticleTypes.NOTE,
//                    this.getX(),
//                    this.getY() + 1.5,
//                    this.getZ(),
//                    0, 0.0, 0.0
//            );
        }

    }

    //SynchedData
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HEALTH_WHEN_START_RIDING = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HEALTH_WHEN_START_RIDING, 0f);
        builder.define(DATA_VARIANT_ID, CrocodileEntity.Variant.GREEN.id);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);

        ListTag listtag = new ListTag();
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)(i));
                listtag.add(itemstack.save(this.registryAccess(), compoundtag));
            }
        }
        compound.put("Items", listtag);

        compound.putFloat("HealthWhenStarted", this.getHealthWhenStartRiding());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(CrocodileEntity.Variant.byId(compound.getInt("Variant")));

        ListTag listtag = compound.getList("Items", 10);
        for (int i = 0; i < listtag.size(); i++) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.parse(this.registryAccess(), compoundtag).orElse(ItemStack.EMPTY));
            }
        }

        this.setHealthWhenStartRiding(compound.getFloat("HealthWhenStarted"));
    }

    public float getHealthWhenStartRiding() {
        return this.entityData.get(HEALTH_WHEN_START_RIDING);
    }

    public void setHealthWhenStartRiding(float healthWhenStartRiding) {
        this.entityData.set(HEALTH_WHEN_START_RIDING, healthWhenStartRiding);
    }

    @Override
    public void setVariant(@NotNull Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull Variant getVariant() {
        return CrocodileEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    //Breeding
    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal mate) {
        this.finalizeSpawnChildFromBreeding(level, mate, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        CrocodileEntity crocodile = Primal_Entities.CROCODILE.get().create(level);
        if (crocodile != null) {
            CrocodileAi.initMemories(crocodile, level.getRandom());
        }

        return crocodile;
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.spawnAtLocation(new ItemStack(Primal_Items.CROCODILE_SCUTE.get(), this.getRandom().nextIntBetweenInclusive(3, 5)), 1);
        }
    }

    public boolean brushOffScute() {
        if (this.isBaby()) {
            return false;
        } else {
            this.spawnAtLocation(new ItemStack(Primal_Items.CROCODILE_SCUTE.get()), 1);
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            this.playSound(SoundEvents.ARMADILLO_BRUSH);
            return true;
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return isMatingFood(stack);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.CHICKEN);
    }

    //Movement
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new CrocodilePathNavigation(this, level);
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        Brain<CrocodileEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);

        CrocodileAi.updateActivity(this);

        if(!this.isInWater())
            this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent());
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

        var thrashing= this.getBrain().getMemory(Primal_MemoryModuleTypes.IS_THRASHING.get());

        //Fallback to reset, just in case
        if(thrashing.isPresent() && thrashing.get() && this.getPassengers().isEmpty()){
            this.getBrain().eraseMemory(Primal_MemoryModuleTypes.IS_THRASHING.get());
            this.setPose(Pose.STANDING);
        }

        if(thrashing.isEmpty() && !this.getPassengers().isEmpty())
            this.ejectPassengers();

        if(!this.getPassengers().isEmpty())
            this.stopMoving();
    }

    @Override
    protected @NotNull Vec3 collide(@NotNull Vec3 vec) {
        AABB aabb = this.getBoundingBox();
        List<VoxelShape> list = this.level().getEntityCollisions(this, aabb.expandTowards(vec));
        Vec3 vec3 = vec.lengthSqr() == 0.0 ? vec : collideBoundingBox(this, vec, aabb, this.level(), list);
        boolean flag = vec.x != vec3.x;
        boolean flag1 = vec.y != vec3.y;
        boolean flag2 = vec.z != vec3.z;
        boolean flag3 = flag1 && vec.y < 0.0;

        if (this.maxUpStep() > 0.0F && (flag3 || this.onGround() || (this.isInWater())) && (flag || flag2)) {
            AABB aabb1 = flag3 ? aabb.move(0.0, vec3.y, 0.0) : aabb;
            AABB aabb2 = aabb1.expandTowards(vec.x, this.maxUpStep(), vec.z);
            if (!flag3) {
                aabb2 = aabb2.expandTowards(0.0, -1.0E-5F, 0.0);
            }

            List<VoxelShape> list1 = collectColliders(this, this.level(), list, aabb2);
            float f = (float)vec3.y;
            float[] afloat = collectCandidateStepUpHeights(aabb1, list1, this.maxUpStep(), f);

            for (float f1 : afloat) {
                Vec3 vec31 = collideWithShapes(new Vec3(vec.x, f1, vec.z), aabb1, list1);
                if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                    double d0 = aabb.minY - aabb1.minY;
                    return vec31.add(0.0, -d0, 0.0);
                }
            }
        }

        return vec3;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));

            //Avoid drowning + keep the crocodile just slightly above water
            if(this.isUnderWater()){
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.0005, 0.0));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @SuppressWarnings("all")
    //Damage necessary to being released
    private final float healthLossToBeReleased =2.0f;

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if(hurt){
            //If receives more than the threshold of damage that when first got its pray, it dismounts it and gets confused for a few seconds
            if(this.isVehicle() && this.getHealth() < (this.getHealthWhenStartRiding() - healthLossToBeReleased)){
                this.ejectPassengers();

                this.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.IS_STUNNED.get(), true, 60L);
            }

            if(source.getEntity()!=null && source.getEntity() instanceof ServerPlayer player && player.getMainHandItem().isEmpty()){
                Primal_Advancements.PUNCH_CROCODILE.get().trigger(player);
            }

            if (source.getEntity() instanceof LivingEntity target && !(target instanceof Player player && player.isCreative())) {
                CrocodileAi.wasHurtBy(this, target);
            }
        }

        return hurt;
    }

    //Thrash
    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        return new Vec3(passenger.getX(), passenger.getY()+0.1, passenger.getZ());
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (this.hasPassenger(passenger)) {

            // Vertical position
            double y = this.getY();

            // Offset forward and right relative to the body rotation
            float bodyYawRad = this.yBodyRot * Mth.DEG_TO_RAD;
            double forwardOffset = -1.2; // Forward (Z direction)
            double sideOffset = -0.0;    // Right (X direction)

            double x = this.getX() + (Mth.sin(bodyYawRad) * forwardOffset) + (Mth.cos(bodyYawRad) * sideOffset);
            double z = this.getZ() - (Mth.cos(bodyYawRad) * forwardOffset) + (Mth.sin(bodyYawRad) * sideOffset);

            callback.accept(passenger, x, y, z);
        }
    }

    public static boolean canPickUpEntity(@NotNull Entity target, @NotNull CrocodileEntity crocodile){
        return target.getBoundingBox().getSize()<crocodile.getBoundingBox().getSize()
                && !crocodile.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.IS_STUNNED.get());
    }

    public void stopMoving(){
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getNavigation().stop();
        this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    public @NotNull Vec3 getFluidFallingAdjustedMovement(double gravity, boolean isFalling, @NotNull Vec3 deltaMovement) {
        if (gravity != 0.0 && !this.isSprinting()) {
            double d0;
            if (isFalling && Math.abs(deltaMovement.y - 0.005) >= 0.003 && Math.abs(deltaMovement.y - gravity / 16.0) < 0.003) {
                d0 = -0.0001;
            } else {
                d0 = deltaMovement.y - gravity / 16.0;
            }

            return new Vec3(deltaMovement.x, d0, deltaMovement.z);
        } else {
            return deltaMovement;
        }
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.375f : super.getSpeed();
    }

    //AI
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<CrocodileEntity> brainProvider() {
        return CrocodileAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return CrocodileAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<CrocodileEntity> getBrain() {
        return (Brain<CrocodileEntity>) super.getBrain();
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        if(this.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) && super.canAttack(target))
            return true;

        return super.canAttack(target) &&
                //Hunts regularly
                ((target.getType().is(Primal_Tags.CROCODILE_HUNTABLE) && !this.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN))
                //Attacks if it has a nearby baby
                || (this.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get()) && !(target instanceof CrocodileEntity) && !target.isShiftKeyDown())
                //To attack the last one that hurt it
                || this.getLastHurtByMob()==target
                //Attacks if it has a nearby egg
                || (this.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get())
                    && this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isPresent()
                    && this.level().getBlockState(this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).is(Primal_Blocks.CROCODILE_EGG.get())
                    && !(target instanceof CrocodileEntity) && !target.isShiftKeyDown())
                //Attacks if it's too close
                || (target.distanceTo(this)<8 && !(target instanceof CrocodileEntity) && !target.isShiftKeyDown()))
                && !this.isPacified()
                && MiscUtil.isNotNeverAttack(target)
                && !target.getType().is(Primal_Tags.CROCODILE_NEVER_ATTACK);
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        //Put the cooldown to attack prey each 600 ticks (30s)
        if(killed.getType().is(Primal_Tags.CROCODILE_HUNTABLE)){
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 600L);
        }

        return super.killedEntity(level, killed);
    }

    //Eating
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        //Drops and eats items
        if(hand==InteractionHand.MAIN_HAND && !this.isAggressive() && !this.isBaby()){
            if (itemstack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.BRUSH_BRUSH) && this.brushOffScute()) {
                itemstack.hurtAndBreak(16, player, getSlotForHand(hand));
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if(itemstack.is(Tags.Items.FEATHERS) && !this.inventory.isEmpty()){

                // Vertical position
                double y = this.getY();
                // Offset forward and right relative to the body rotation
                float bodyYawRad = this.yBodyRot * Mth.DEG_TO_RAD;
                double forwardOffset = -1.3; // Forward (Z direction)
                double sideOffset = -0.0;    // Right (X direction)
                double x = this.getX() + (Mth.sin(bodyYawRad) * forwardOffset) + (Mth.cos(bodyYawRad) * sideOffset);
                double z = this.getZ() - (Mth.cos(bodyYawRad) * forwardOffset) + (Mth.sin(bodyYawRad) * sideOffset);


                NonNullList<ItemStack> items = this.inventory.getItems();

                int maxIndex=0;
                for(ItemStack stack: items){
                    if(!stack.isEmpty())
                        maxIndex++;
                }
                if(maxIndex!=0)
                    maxIndex=maxIndex-1;

                ItemStack stackToDrop= items.get(maxIndex);
                this.inventory.setItem(maxIndex, ItemStack.EMPTY);

                ItemEntity itementity = new ItemEntity(this.level(), x, y, z, stackToDrop);
                itementity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itementity);
                if(!this.level().isClientSide)
                    this.triggerAnim("base_controller", "vomits");
                if(this.getVomitSound()!=null)
                    this.playSound(this.getVomitSound(), 1.0f, 1.0f);


                if(player instanceof ServerPlayer serverPlayer)
                    Primal_Advancements.TICKLE_CROC.get().trigger(serverPlayer);

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else if (!isFood(itemstack) && this.canEatItem(itemstack)){

                if(itemstack.is(Items.CLOCK) && player instanceof ServerPlayer serverPlayer)
                    Primal_Advancements.CLOCK_CROC.get().trigger(serverPlayer);

                Collection<ItemStack> stack=List.of(itemstack);

                this.addItemsToInventory(stack);

                itemstack.shrink(itemstack.getCount());

                if(!this.level().isClientSide)
                    this.triggerAnim("base_controller", "vomits");

                if(this.getEatSound()!=null)
                    this.playSound(this.getEatSound(), 1.0f, 1.0f);

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }



        }


        //Mating and pacifying
        if (this.isFood(itemstack) && !this.isAggressive()) {
            int i = this.getAge();

            if (!this.level().isClientSide && i == 0) {
                this.usePlayerItem(player, hand, itemstack);

                if(this.canFallInLove())
                    this.setInLove(player);

                this.getBrain().setMemoryWithExpiry(MemoryModuleType.PACIFIED, true, 1200);
                this.level().broadcastEntityEvent(this, (byte)38);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.usePlayerItem(player, hand, itemstack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 38) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void addParticlesAroundSelf(ParticleOptions particleOption) {
        for (int i = 0; i < 7; i++) {
            double d0 = this.random.nextGaussian() * 0.01;
            double d1 = this.random.nextGaussian() * 0.01;
            double d2 = this.random.nextGaussian() * 0.01;
            this.level().addParticle(particleOption, this.getRandomX(1.0), this.getRandomY() + 0.2, this.getRandomZ(1.0), d0, d1, d2);
        }
    }

    public boolean isPacified(){
        return this.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
    }

    public boolean canEatItem(ItemStack drop){
        return this.inventory.canAddItem(drop) && !drop.is(Primal_Tags.CROCODILE_CANT_EAT) && !drop.isEmpty();
    }

    public void addItemsToInventory(Collection<ItemStack> drops){
        for(ItemStack stack: drops){
            if(this.inventory.canAddItem(stack))
                this.inventory.addItem(stack);
        }
    }

    public Optional<GlobalPos> getCompassPos(){
        Optional<GlobalPos> compassPos=Optional.empty();

        for(ItemStack stack: this.inventory.getItems()){
            if(stack.has(DataComponents.LODESTONE_TRACKER) && stack.get(DataComponents.LODESTONE_TRACKER)!=null){
                compassPos= stack.get(DataComponents.LODESTONE_TRACKER).target();
            }

        }

        return compassPos;
    }

    public boolean hasClock(){
        return this.inventory.hasAnyMatching(stack -> stack.is(Items.CLOCK));
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        if(!this.inventory.isEmpty()){
            for(ItemStack stack : this.inventory.getItems()){
                this.spawnAtLocation(stack);
            }
        }
    }

    //Sounds
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Primal_Sounds.CROCODILE_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.CROCODILE_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.CROCODILE_HURT.get();
    }

    @Override
    protected void playAttackSound() {
        this.playSound(Primal_Sounds.CROCODILE_ATTACK.get(), 1.0F, 1.0F);
    }

    @Nullable
    protected SoundEvent getVomitSound(){
        return Primal_Sounds.CROCODILE_VOMIT.get();
    }

    @Nullable
    protected SoundEvent getEatSound(){
        return Primal_Sounds.CROCODILE_EAT.get();
    }

    //Misc
    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    //This ensures the crocodile only spawn near the water
    public static boolean checkCrocodileSpawnRules(EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() >= level.getSeaLevel() - 10
//                && (MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))
                && isNearWater(level, pos, 5, 2);
    }


    @SuppressWarnings("all")
    protected static boolean isNearWater(LevelAccessor level, BlockPos pos, int lateralDistance, int verticalDistance) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();

        for (int dx = -lateralDistance; dx <= lateralDistance; dx++) {
            for (int dz = -lateralDistance; dz <= lateralDistance; dz++) {
                for (int dy = -verticalDistance; dy <= verticalDistance; dy++) {
                    check.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);

                    // Only check if the chunk is actually loaded
                    if (level.isAreaLoaded(check, 1)) {
                        if (level.getFluidState(check).is(FluidTags.WATER)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    //Just to being classified as neutral
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
