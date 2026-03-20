package org.primal.util;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.primal.Primal_Main;
import org.primal.block.NestBlock;
import org.primal.injection.IsEagleTarget;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;
import org.primal.util.mob_types.PrimalTamable;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Primal_Util {

    public static final IntegerProperty EGGS_2 = IntegerProperty.create("eggs", 1, 2);
    public static final IntegerProperty EGGS_3 = IntegerProperty.create("eggs", 1, 3);
    public static final IntegerProperty EGGS_4 = IntegerProperty.create("eggs", 1, 4);


    /**
     * Set a custom name for the specified variants
     */
    @SafeVarargs
    public static<T extends StringRepresentable, E extends Entity & VariantHolder<T>> Component getCustomVariantName(E entity,
                                                                                                                     Component defaultName,
                                                                                                                     T... variants){
        if(entity.hasCustomName()) return defaultName;

        var location = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        for(T variant : variants){
            if(entity.getVariant().equals(variant)){
                //entity.primal.bear.[variant]
                return Component.translatable("entity."+location.getNamespace()+ "."+ location.getPath() +"."+variant.getSerializedName());
            }
        }

        return defaultName;
    }
    public static int toTicks(int seconds){
        return seconds*20;
    }

    public static long getDaytime(Level level){
        return level.getDayTime() % 24000;
    }

    /**
     * Works just like the GeckoLib {@link software.bernie.geckolib.animation.AnimationState#isMoving()} but on server side. With 0.015 motion threshold
     */
    public static boolean isMoving(LivingEntity entity){
        return isMoving(entity, 0.015f);
    }

    /**
     * Works just like the GeckoLib {@link software.bernie.geckolib.animation.AnimationState#isMoving()} but on server side
     */
    public static boolean isMoving(LivingEntity entity, float motionThreshold){
        float limbSwingAmount = 0;


        if (entity.isAlive()) {
            limbSwingAmount = entity.walkAnimation.speed(entity.tickCount);


            if (limbSwingAmount > 1f)
                limbSwingAmount = 1f;
        }

        Vec3 velocity = entity.getDeltaMovement();
        float avgVelocity = (float)((Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f);

        return avgVelocity >= motionThreshold && limbSwingAmount != 0;
    }

    /**
     * Detects if an entity is directly facing other
     * @param target The entity to check
     * @param watcher The entity that is watching
     * @param angle The value of how much angle it needs to return true, 0.1 to 1.0 detects from the back, -0.1 to -1.0 detects from the front
     */
    public static boolean isSeeingTarget(Entity target, Entity watcher, float angle) {
        Vec3 vec3d = target.position();

        Vec3 vec3d2 = watcher.calculateViewVector(0.0f, watcher.getYHeadRot());
        Vec3 vec3d3 = vec3d.vectorTo(watcher.position());
        vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();

        return vec3d3.dot(vec3d2) < angle;
    }

    /**
     Makes an angle move slowly to the desired angle
     @param current The rotation that you want to move
     @param target The desired rotation
     @param maxChange The maximum rotation that it moves every tick
     @return The new rotation
     */
    public static float smoothAngle(float current, float target, float maxChange) {
        float delta = Mth.wrapDegrees(target - current);
        delta = Mth.clamp(delta, -maxChange, maxChange);

        return current + delta;
    }

    public static<M extends AgeableMob & VariantHolder<V>, V> M createFromParents(EntityType<M> type, M parent, M otherParent, Consumer<M> init){
        M offspring = type.create(parent.level());

        if (offspring != null) {
            init.accept(offspring);

            offspring.setVariant(
                    offspring.getRandom().nextBoolean()
                            ? parent.getVariant()
                            : otherParent.getVariant()
            );
        }

        return offspring;
    }

    public static void useShearsOnEntityAndDamage(LivingEntity entity, Player player, ItemStack stackInHand, InteractionHand hand){
        //Emit event, damages if possible and plays sound
        entity.gameEvent(GameEvent.SHEAR, player);
        if(stackInHand.isDamageableItem()){
            stackInHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        entity.level().playSound(null, entity, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
    }


    /**
     Extends a loot pool
     */
    public static void extendLootPool(LootPool pool, List<LootPoolEntryContainer> newEntries) {
        var entriesBuilder = ImmutableList.<LootPoolEntryContainer>builder();
        entriesBuilder.addAll(pool.entries);
        entriesBuilder.addAll(newEntries);
        pool.entries = entriesBuilder.build();
    }

    public static boolean isLootTable(LootTableLoadEvent event, ResourceKey<LootTable> lootTable) {
        return event.getName().equals(lootTable.location());
    }

    /**
        A simple check to see if the entity is from the tag {@link  Primal_Tags.Entity#NEVER_ATTACK}
     */
    public static boolean isNotNeverAttack(LivingEntity entity) {
        return !entity.getType().is(Primal_Tags.Entity.NEVER_ATTACK);
    }

    /**
     Simple addition logic, used to count attacks
     */
    public static void addToAttackCount(LivingEntity entity) {
        if(entity.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).isPresent()){
            entity.getBrain().setMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(),
                    entity.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).get()+1);
        } else {
            entity.getBrain().setMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(), 1);
        }
    }

    public static boolean isSameEagleAttacking(LivingEntity target, LivingEntity eagle) {
        if(((IsEagleTarget) target).primal$eagleAttacking().isPresent()){
            //So babies can have priority over other eagles
            if(eagle.isBaby())
                return true;
            else
                return ((IsEagleTarget) target).primal$eagleAttacking().get() == eagle.getUUID();
        } else {
            return true;
        }
    }

    /**
     Convenient method to add items to a CreativeTab in order after one item
     */
    public static void insertItemsAfter(BuildCreativeModeTabContentsEvent event, ItemStack after, ItemStack... itemStacks){

        for (int i=0; i<itemStacks.length; i++){
            if(i==0){
                event.insertAfter(after, itemStacks[i], CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            else {
                event.insertAfter(itemStacks[i-1], itemStacks[i], CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

        }
    }


    public static ResourceLocation nomanslandLoc(String name){
        return ResourceLocation.fromNamespaceAndPath("nomansland", name);
    }

    /**
     Damages the equipment of an entity
     @param entity The entity that uses the armor
     @param source The DamageSource
     @param amount The amount of damage
     @param slots The slots to damage
     */
    public static void damageEquipment(final LivingEntity entity, final DamageSource source, final float amount, final EquipmentSlot... slots) {
        if (!(amount <= 0.0F)) {
            final int i = (int)Math.max(1.0F, amount / 4.0F);

            for (final EquipmentSlot equipmentSlot : slots) {
                final ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
                if (itemStack.getItem() instanceof ArmorItem && itemStack.canBeHurtBy(source)) {
                    itemStack.hurtAndBreak(i, entity, equipmentSlot);
                }
            }
        }
    }

    public static @Nullable Direction getFaceTouchedByEntity(AABB entityBox, AABB blockBox) {
        double dxWest   = entityBox.maxX - blockBox.minX;
        double dxEast   = blockBox.maxX - entityBox.minX;
        double dzNorth  = entityBox.maxZ - blockBox.minZ;
        double dzSouth  = blockBox.maxZ - entityBox.minZ;
        double dyDown   = entityBox.maxY - blockBox.minY;
        double dyUp     = blockBox.maxY - entityBox.minY;

        // Find nearest face
        double min = Double.MAX_VALUE;
        Direction face = null;

        if (dxWest >= 0 && dxWest < min) { min = dxWest; face = Direction.WEST; }
        if (dxEast >= 0 && dxEast < min) { min = dxEast; face = Direction.EAST; }
        if (dzNorth >= 0 && dzNorth < min) { min = dzNorth; face = Direction.NORTH; }
        if (dzSouth >= 0 && dzSouth < min) { min = dzSouth; face = Direction.SOUTH; }
        if (dyDown >= 0 && dyDown < min) { min = dyDown; face = Direction.DOWN; }
        if (dyUp >= 0 && dyUp < min) { min = dyUp; face = Direction.UP; }
        return face;
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static class Ai {

        public static final int MIN_LOW_AIR_DEFAULT=40;
        public static final int MIN_LOW_AIR_SLOW   =60;
        private static final int[][] SWIM_XY_DISTANCE_TIERS = new int[][]{{4, 4}, {5, 5}, {6, 5}, {7, 7}, {10, 7}};

        public static void setNearestAttackableOnSensor(LivingEntity mob){
            setNearestAttackableOnSensor(mob, target -> Sensor.isEntityAttackable(mob, target) && mob.canAttack(target));
        }

        public static void setNearestAttackableOnSensor(LivingEntity mob,
                                                        Predicate<LivingEntity> filter){
            mob.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                    .filter(filter)
                    .findFirst()
                    .ifPresentOrElse(ent -> mob.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                            () -> mob.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
        }

        public static void setMemoryFromVisibleEntity(LivingEntity mob,
                                                         NearestVisibleLivingEntities nearbyEntities,
                                                         Predicate<LivingEntity> filter,
                                                         MemoryModuleType<LivingEntity> memory){
            Optional<LivingEntity> optional = nearbyEntities.findClosest(filter);

            optional.ifPresentOrElse(
                    o -> mob.getBrain().setMemory(memory, o),
                    ()-> mob.getBrain().eraseMemory(memory)
            );
        }

        public static void setMemoryFromVisibleEntity(LivingEntity mob,
                                                              Predicate<LivingEntity> filter,
                                                              MemoryModuleType<LivingEntity> memory){

            var visibles= mob.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

            visibles.ifPresentOrElse(nearestVisibleLivingEntities ->
                    Primal_Util.Ai.setMemoryFromVisibleEntity(mob, nearestVisibleLivingEntities, filter, memory),
                    () -> mob.getBrain().eraseMemory(memory));
        }

        public static void setMemoryFromNearEntities(LivingEntity mob,
                                                      Predicate<LivingEntity> filter,
                                                      MemoryModuleType<LivingEntity> memory){

            var visibles= mob.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);

            visibles.stream().flatMap(Collection::stream)
                    .filter(filter)
                    .findFirst()
                    .ifPresentOrElse(ent -> mob.getBrain().setMemory(memory, ent),
                            () -> mob.getBrain().eraseMemory(memory));
        }

        public static boolean lessThanMinAir(Mob mob){
            return mob.getAirSupply()< MIN_LOW_AIR_DEFAULT;
        }

        public static boolean lessThanMinAirSlow(Mob mob){
            return mob.getAirSupply()< MIN_LOW_AIR_SLOW;
        }

        public static boolean lessThanMinAir(Mob mob, int air){
            return mob.getAirSupply()<air;
        }

        public static <T extends Mob> boolean wasHurtRecently(T self) {
            var brain = self.getBrain();

            return brain.getMemory(Primal_MemoryModuleTypes.HURT_RECENTLY.get()).isPresent() && brain.getMemory(Primal_MemoryModuleTypes.HURT_RECENTLY.get()).get();
        }

        public static <T extends Mob> void wasHurtByAndAvoids(
                T self,
                LivingEntity target,
                Class<T> entityClass,
                boolean adultAlertOtherAdults,
                boolean alertBabies
        ) {
            wasHurtByAndAvoids(self, target, entityClass, adultAlertOtherAdults, alertBabies, DEFAULT_RETREAT_DURATION);
        }

        public static <T extends Mob> void wasHurtByAndAvoids(
                T self,
                LivingEntity target,
                Class<T> entityClass,
                boolean adultAlertOtherAdults,
                boolean alertBabies,
                UniformInt duration
        ) {
            var brain = self.getBrain();

            //Avoid triggering for pets and owners
            if(self instanceof TamableAnimal pet && pet.getOwner()!=null && pet.getOwner().equals(target)) return;

            brain.eraseMemory(MemoryModuleType.PACIFIED);
            brain.eraseMemory(MemoryModuleType.BREED_TARGET);
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
            brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.HURT_RECENTLY.get(), true, 200);

            if(self.isBaby())
                retreatFromNearestTarget(self, target, duration);
            else
                retreatFromNearestTarget(self, target, duration);

            if(self.isBaby() || adultAlertOtherAdults)
                for (T nearbyAdult : getNearestAdults(self, entityClass))
                    retreatFromNearestTarget(nearbyAdult, target, duration);

            if(alertBabies)
                for (T nearbyBaby : getNearestBabies(self, entityClass))
                    retreatFromNearestTarget(nearbyBaby, target, duration);
        }

        public static <T extends Mob> void wasHurtByAndAttacks(
                T self,
                LivingEntity target,
                Class<T> entityClass,
                boolean adultAlertOtherAdults,
                boolean alertBabies,
                boolean babyRuns
        ) {
            var brain = self.getBrain();

            //Avoid triggering for pets and owners
            if(self instanceof TamableAnimal pet && pet.getOwner()!=null && pet.getOwner().equals(target)) return;

            brain.eraseMemory(MemoryModuleType.PACIFIED);
            brain.eraseMemory(MemoryModuleType.BREED_TARGET);
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
            brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.HURT_RECENTLY.get(), true, 200);

            if(self.isBaby() && babyRuns)
                retreatFromNearestTarget(self, target);
            else
                brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);

            if(self.isBaby() || adultAlertOtherAdults)
                for (T nearbyAdult : getNearestAdults(self, entityClass))
                    nearbyAdult.getBrain()
                            .setMemory(MemoryModuleType.ATTACK_TARGET, target);

            if(alertBabies)
                for (T nearbyBaby : getNearestBabies(self, entityClass))
                    retreatFromNearestTarget(nearbyBaby, target);
        }

        public static <T extends LivingEntity> List<T> getNearestMobs(
                T self,
                Class<T> entityClass,
                Predicate<T> filter
        ) {
            return getNearestMobs(self, entityClass, filter, 30, 5);
        }

        public static <T extends LivingEntity> List<T> getNearestMobs(
                T self,
                Class<T> entityClass,
                Predicate<T> filter,
                int width,
                int height
        ) {
            return self.level()
                    .getEntitiesOfClass(
                            entityClass,
                            self.getBoundingBox().inflate(width, height, width)
                    )
                    .stream()
                    .filter(filter)
                    .toList();
        }

        public static <T extends LivingEntity> List<T> getNearestBabies(T self, Class<T> entityClass) {
            return getNearestMobs(self, entityClass, e -> e.isBaby() && e!=self && !(self instanceof PrimalTamable tamable && !tamable.isWandering()), 30, 5);
        }

        public static <T extends LivingEntity> List<T> getNearestAdults(T self, Class<T> entityClass) {
            return getNearestMobs(self, entityClass, e -> !e.isBaby() && e!=self && !(self instanceof PrimalTamable tamable && !tamable.isWandering()), 30, 5);
        }

        public static <T extends Mob> void retreatFromNearestTarget(
                T self,
                LivingEntity target
        ) {
            retreatFromNearestTarget(self, target, DEFAULT_RETREAT_DURATION);
        }

        public static <T extends Mob> void retreatFromNearestTarget(
                T self,
                LivingEntity target,
                UniformInt duration
        ) {
            var brain = self.getBrain();

            LivingEntity avoidTarget =
                    BehaviorUtils.getNearestTarget(
                            self,
                            brain.getMemory(MemoryModuleType.AVOID_TARGET),
                            target
                    );

            avoidTarget =
                    BehaviorUtils.getNearestTarget(
                            self,
                            brain.getMemory(MemoryModuleType.ATTACK_TARGET),
                            avoidTarget
                    );

            setAvoidTarget(self, avoidTarget, duration);
        }

        private static final UniformInt DEFAULT_RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);

        private static <T extends Mob> void setAvoidTarget(
                T self,
                LivingEntity target
        ) {
            setAvoidTarget(self, target, DEFAULT_RETREAT_DURATION);
        }

        private static <T extends Mob> void setAvoidTarget(
                T self,
                LivingEntity target,
                UniformInt duration
        ) {
            var brain = self.getBrain();

            brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);

            brain.setMemoryWithExpiry(
                    MemoryModuleType.AVOID_TARGET,
                    target,
                    duration.sample(self.level().random)
            );
        }

        public static BehaviorControl<PathfinderMob> swimButMobIsReallyFuckingFat(float speedModifier) {
            return RandomStroll.strollFlyOrSwim(speedModifier, Ai::getTargetSwimPos, Entity::isInWaterOrBubble);
        }

        @Nullable
        private static Vec3 getTargetSwimPos(PathfinderMob mob) {
            Vec3 vec3 = null;
            Vec3 vec31 = null;

            for (int[] aint : SWIM_XY_DISTANCE_TIERS) {
                if (vec3 == null) {
                    vec31 = BehaviorUtils.getRandomSwimmablePos(mob, aint[0], aint[1]);
                } else {
                    vec31 = mob.position().add(mob.position().vectorTo(vec3).normalize().multiply(aint[0], aint[1], aint[0]));
                }

                if (vec31 == null || mob.level().getFluidState(BlockPos.containing(vec31)).isEmpty()) {
                    return vec3;
                }

                vec3 = vec31;
            }

            return vec31;
        }

        public static TargetingConditions getTargetConditions(double distance, boolean ignoreInvisibility){
            if(ignoreInvisibility)
                return TargetingConditions.forNonCombat().range(distance).ignoreInvisibilityTesting();

            return TargetingConditions.forNonCombat().range(distance);
        }

        public static OneShot<PathfinderMob> fly(float speedModifier, int xzRange, int yRange) {
            return RandomStroll.strollFlyOrSwim(speedModifier, mob -> getTargetFlyPos(mob, xzRange, yRange), mob -> true);
        }

        @Nullable
        public static Vec3 getTargetFlyPos(PathfinderMob mob, int maxDistance, int yRange) {
            Vec3 vec3 = mob.getViewVector(0.0F);
            return AirAndWaterRandomPos.getPos(mob, maxDistance, yRange, -2, vec3.x, vec3.z, (float) (Math.PI / 2));
        }

        public static boolean isMovingOrNoInGround(Mob mob){
            return !mob.getNavigation().isDone()
                            || mob.getDeltaMovement().horizontalDistanceSqr() > 0.0025F
                            || !mob.onGround()
                            || mob.isInWater();
        }

        public static<T extends AgeableMob> boolean isBabyWithoutNest(T e){
            return e.isBaby() && e.getBrain().getMemory(MemoryModuleType.HOME).isEmpty();
        }

        public static boolean canReachPos(Mob mob, BlockPos desiredPos){
            Path path = mob.getNavigation().createPath(desiredPos, 0);
            return path != null && path.canReach();
        }
    }

    public static class Generation {

        /**
         * Updates a nest block connections during world generation
         **/
        public static BlockState updateNest(WorldGenLevel level, BlockPos nestPosition, BlockState nestState){

            //The nest to update
            nestState =
                    nestState
                            .setValue(NestBlock.PROPERTY_BY_DIRECTION.get(Direction.NORTH), NestBlock.canHaveDirection(nestPosition, level, Direction.NORTH))
                            .setValue(NestBlock.PROPERTY_BY_DIRECTION.get(Direction.EAST), NestBlock.canHaveDirection(nestPosition, level, Direction.EAST))
                            .setValue(NestBlock.PROPERTY_BY_DIRECTION.get(Direction.SOUTH), NestBlock.canHaveDirection(nestPosition, level, Direction.SOUTH))
                            .setValue(NestBlock.PROPERTY_BY_DIRECTION.get(Direction.WEST), NestBlock.canHaveDirection(nestPosition, level, Direction.WEST));

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos neighbourNestPos = nestPosition.relative(direction);

                //Updates nest around
                if (level.getBlockState(neighbourNestPos).is(Primal_Blocks.NEST_BLOCK)) {
                    level.setBlock(
                            neighbourNestPos,
                            level.getBlockState(neighbourNestPos).setValue(NestBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()), false),
                            Block.UPDATE_CLIENTS
                    );
                }
            }

            return nestState;
        }

        /**
         * Create a biome modifier serializer, for creation of biome modifiers
         *
         * @param name The variant of the biome modifier
         * @return The proper serializer
         */
        public static DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<? extends BiomeModifier>> createBiomeModifierSerializer(String name){
            return DeferredHolder.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
        }

        /**
         * Create a biome modifier with config values, for features
         **/
        public static<FC extends FeatureConfiguration, F extends Feature<FC>> void createBiomeModifier(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder, TagKey<Biome> biomeTagSpawn,
                                                                                                       List<String> extraBiomes,
                                                                                                       GenerationStep.Decoration step,
                                                                                                       boolean enabled,
                                                                                                       F feature,
                                                                                                       FC config,
                                                                                                       PlacementModifier heightmap,
                                                                                                       int rarity) {
            if(enabled){
                boolean matchesExtra = biomeMatchesWithConfigList(extraBiomes, biome);

                if (biome.is(biomeTagSpawn) || matchesExtra) {

                    // Configured feature
                    var configured = new ConfiguredFeature<>(
                            feature,
                            config
                    );

                    // Build placement modifiers dynamically
                    List<PlacementModifier> modifiers = List.of(
                            RarityFilter.onAverageOnceEvery(rarity),
                            InSquarePlacement.spread(),
                            heightmap,
                            BiomeFilter.biome()
                    );

                    // Wrap into placed feature
                    Holder<PlacedFeature> placedFeature = Holder.direct(
                            new PlacedFeature(Holder.direct(configured), modifiers)
                    );

                    // Add to biome
                    builder.getGenerationSettings().addFeature(
                            step,
                            placedFeature
                    );
                }
            }
        }

        /**
         * Create a biome modifier with config values, for features with special placementModifiers
         **/
        public static<FC extends FeatureConfiguration, F extends Feature<FC>> void createBiomeModifier(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder, TagKey<Biome> biomeTagSpawn,
                                                                                                       List<String> extraBiomes,
                                                                                                       GenerationStep.Decoration step,
                                                                                                       boolean enabled,
                                                                                                       F feature,
                                                                                                       FC config,
                                                                                                       List<PlacementModifier> modifiers) {
            if(enabled){
                boolean matchesExtra = biomeMatchesWithConfigList(extraBiomes, biome);

                if (biome.is(biomeTagSpawn) || matchesExtra) {

                    // Configured feature
                    var configured = new ConfiguredFeature<>(
                            feature,
                            config
                    );

                    // Wrap into placed feature
                    Holder<PlacedFeature> placedFeature = Holder.direct(
                            new PlacedFeature(Holder.direct(configured), modifiers)
                    );

                    // Add to biome
                    builder.getGenerationSettings().addFeature(
                            step,
                            placedFeature
                    );
                }
            }
        }

        /**
         * Create a biome modifier with config values, for mobs
         **/
        public static void createBiomeModifier(Holder<Biome> biome,
                                               ModifiableBiomeInfo.BiomeInfo.Builder builder, TagKey<Biome> biomeTagSpawn,
                                               List<String> extraBiomes,
                                               boolean enabled,
                                               int spawnWeight,
                                               int minGroupSize,
                                               int maxGroupSize,
                                               EntityType<?> entityType) {
            if(enabled){
                boolean matchesExtra = biomeMatchesWithConfigList(extraBiomes, biome);

                if ((biome.is(biomeTagSpawn) || matchesExtra) && spawnWeight > 0 && minGroupSize > 0 && maxGroupSize > 0) {

                    builder.getMobSpawnSettings().getSpawner(entityType.getCategory())
                            .add(new MobSpawnSettings.SpawnerData(
                                    entityType,
                                    spawnWeight,
                                    minGroupSize,
                                    maxGroupSize));
                }
            }
        }

        @Unique
        public static boolean biomeMatchesWithConfigList(List<String> extraBiomes, Holder<Biome> biome){
            // Check if biome is in extra biomes
            return extraBiomes.stream()
                    .anyMatch(b -> {
                        ResourceLocation loc = ResourceLocation.tryParse(b);

                        if (b.contains("#")) {
                            //Splits the tag, getting the # alone and the id like ( minecraft:is_forest )
                            String[] stringArray = b.split("#");

                            //If it is actually that long
                            if (stringArray.length >= 1) {

                                String biomeTag = stringArray[1];

                                ResourceLocation tagLoc = ResourceLocation.tryParse(biomeTag);

                                //Detects biome tags
                                return tagLoc != null && biome.is(TagKey.create(Registries.BIOME, tagLoc));
                            }
                        }

                        return loc != null && biome.unwrapKey().map(key -> key.location().equals(loc)).orElse(false);
                    });
        }
    }

    public static class Visuals {

        public static @Nullable ResourceLocation getNomanslandVariant(CompoundTag mainTag, String mobType, String suffix, String... supported){
            CompoundTag neoforgeAttachments= mainTag.getCompound("neoforge:attachments");
            var variantTag = neoforgeAttachments.get("mixed_litter:variants");

            //If it has nomansland variation, it set the location differently, so it can use nomansland textures
            if(variantTag!=null && variantTag.getAsString().contains("nomansland")){
                String raw = variantTag.getAsString();

                // Defensive check: only parse if format looks correct
                if (raw.contains("/")) {
                    String[] variantSplit = raw.split("/");
                    if (variantSplit.length > 1) {
                        String[] quoteSplit = variantSplit[1].split("\"");
                        if (quoteSplit.length > 0 && !quoteSplit[0].isEmpty()) {
                            String variant = quoteSplit[0];
                            //Support only for cream and forest, to avoid missing textures
                            if(Arrays.stream(supported).toList().contains(variant))
                                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                                        "textures/entity/"+mobType+"/nomansland/"+ variant +(suffix)+ ".png");
                        }
                    }
                }
            }
            return null;
        }

        public static  <T extends LivingEntity> ResourceLocation getHelmetDecorationTexture(String name){
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                    "textures/helmet_decoration/models/"+ name +".png");
        }

        public static void emitAnimation(@Nullable Pair<String, String> animation, Entity mob){
            if(animation !=null && mob instanceof GeoEntity geoAnimatable)
                geoAnimatable.triggerAnim(animation.getFirst(), animation.getSecond());
        }

        public static void stopAnimation(@Nullable Pair<String, String> animation, Entity mob){
            if(animation !=null && mob instanceof GeoEntity geoAnimatable)
                geoAnimatable.stopTriggeredAnim(animation.getFirst(), animation.getSecond());
        }

        public static void bodyFullRotations(LivingEntity animatable, float partialTick, PoseStack poseStack){
            float headPitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());

            float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
            float lerpHeadRot = 0;
            float netHeadYaw = lerpHeadRot - lerpBodyRot;
            headPitch = -1*headPitch;
            netHeadYaw= -1*netHeadYaw;

            // Move pivot to center of entity
            float halfHeight = animatable.getBbHeight() * 0.5F;
            poseStack.translate(0.0D, halfHeight, 0.0D);

            // Apply rotations
            poseStack.mulPose(Axis.YP.rotationDegrees(180 - netHeadYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));

            // Move back
            poseStack.translate(0.0D, -halfHeight, 0.0D);
        }

        public static <M extends Mob, T extends GeoAnimatable> void headRotationsSwimming(AnimationProcessor<T> animationProcessor, M mob, AnimationState<T> animationState){
            GeoBone head = animationProcessor.getBone("head");

            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

                float rotX = entityData.headPitch() * Mth.DEG_TO_RAD;
                float rotY = entityData.netHeadYaw() * Mth.DEG_TO_RAD;

                head.setRotX(mob.isInWater()? Mth.clamp(rotX, -mob.getMaxHeadXRot() * Mth.DEG_TO_RAD, mob.getMaxHeadXRot() * Mth.DEG_TO_RAD): rotX);
                head.setRotY(mob.isInWater()? Mth.clamp(rotY, -mob.getMaxHeadYRot() * Mth.DEG_TO_RAD, mob.getMaxHeadYRot() * Mth.DEG_TO_RAD): rotY);
            }
        }

        public static void addParticleAboveSelf(LivingEntity mob, ParticleOptions particleOption, int amount) {
            addParticleAboveSelf(mob, particleOption, 0.02, amount, 1);
        }

        public static void addParticlesAroundSelf(LivingEntity mob, ParticleOptions particleOption, int amount) {
            addParticlesAroundSelf(mob, particleOption, 0.02, amount, 1, 1, 1);
        }

        public static void addParticleAboveSelf(LivingEntity mob, ParticleOptions particleOption, double modifier, int amount, double height) {
            for (int i = 0; i < amount; i++) {
                double d0 = mob.getRandom().nextGaussian() * modifier;
                double d1 = mob.getRandom().nextGaussian() * modifier;
                double d2 = mob.getRandom().nextGaussian() * modifier;
                mob.level().addParticle(particleOption, mob.getX(), mob.getRandomY() + height, mob.getZ(), d0, d1, d2);
            }
        }

        public static void addParticlesAroundSelf(LivingEntity mob, ParticleOptions particleOption, double modifier, int amount, double height, double randomX, double randomZ) {
            for (int i = 0; i < amount; i++) {
                double d0 = mob.getRandom().nextGaussian() * modifier;
                double d1 = mob.getRandom().nextGaussian() * modifier;
                double d2 = mob.getRandom().nextGaussian() * modifier;
                mob.level().addParticle(particleOption, mob.getRandomX(randomX), mob.getRandomY() + height, mob.getRandomZ(randomZ), d0, d1, d2);
            }
        }

        public static<M extends Entity & GeoEntity> void resetControllerAfterAttack(M animatable, AnimationState<M> state, RawAnimation... animations){
            resetControllerAfterAttack(animatable, state, "attack", animations);
        }

        public static<M extends Entity & GeoEntity> void resetControllerAfterAttack(M animatable, AnimationState<M> state, String controllerName, RawAnimation... animations){
            //Resets animation after triggering an attack animation
            var manager = animatable.getAnimatableInstanceCache().getManagerForId(animatable.getId());
            var attackController = manager.getAnimationControllers().get(controllerName);

            if(attackController.isPlayingTriggeredAnimation() && attackController.getTriggeredAnimation() !=null &&
                    Arrays.stream(animations).anyMatch(r-> r.equals(attackController.getTriggeredAnimation()))){
                state.getController().forceAnimationReset();
            }
        }
    }
}