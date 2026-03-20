package org.primal.entity.ai.behavior.snake;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;
import org.primal.util.block_types.AnimalEgg;

import java.util.List;

public class SnakeEatEgg extends Behavior<SnakeEntity> {

    public SnakeEatEgg() {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.HURT_RECENTLY.get(),
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryStatus.VALUE_PRESENT), 5
        );
    }

    @Nullable
    public ItemEntity pendingPickup;

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SnakeEntity snake) {
        Vec3 look = snake.getLookAngle().normalize();

        double forward = 0.4;
        double width = 0.45;
        double height = 0.45;

        AABB pickupBox = snake.getBoundingBox()
                .move(look.x * forward, 0.0, look.z * forward)
                .inflate(width, height, width);

        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, pickupBox)) {
            if (!item.isRemoved()
                    && !item.getItem().isEmpty()
                    && snake.wantsToPickUp(item.getItem())
                    && !Primal_Util.isMoving(snake, 0.0015f)) {

                pendingPickup = item;
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SnakeEntity cassowary, long gameTime) {
        return pendingPickup != null
                && !pendingPickup.isRemoved()
                && !pendingPickup.getItem().isEmpty()
                && cassowary.getBrain().getMemory(Primal_MemoryModuleTypes.HURT_RECENTLY.get()).isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull SnakeEntity cassowary, long gameTime) {
        cassowary.getNavigation().stop();
        cassowary.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);

        if (pendingPickup != null) cassowary.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(pendingPickup, true));
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull SnakeEntity snake, long gameTime) {
        snake.getNavigation().stop();
        snake.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);

        if (pendingPickup != null)
            snake.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(pendingPickup, true));
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull SnakeEntity snake, long gameTime) {
        if (pendingPickup == null || pendingPickup.isRemoved()) return;

        if (snake.level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, pendingPickup.getItem()),
                    pendingPickup.getX(), pendingPickup.getY(), pendingPickup.getZ(),
                    8, 0.15, 0.1, 0.15, 0.02
            );

        ItemStack stack = pendingPickup.getItem();
        stack.shrink(1);
        if (stack.isEmpty()) pendingPickup.discard();

        if(snake.playEatingSound()){

            //This makes animals of the same egg type mad if the snake eat eggs
            if(pendingPickup.getItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AnimalEgg animalEgg){
                //Get all entities nearby, then filters them by the same type as the egg
                List<Entity> allEntitiesList = level.getEntitiesOfClass(
                                Entity.class,
                                new AABB(snake.blockPosition()).inflate(20))
                        .stream().filter(
                                entity -> entity.getType() == animalEgg.getAnimal().get())
                        .toList();

                for (Entity entityNearby: allEntitiesList){
                    if(entityNearby instanceof LivingEntity animal && !animal.isBaby()){
                        Brain<?> brain = animal.getBrain();
                        brain.eraseMemory(MemoryModuleType.PACIFIED);
                        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                        brain.setMemory(MemoryModuleType.ATTACK_TARGET, snake);
                    }
                }
            }

            snake.setIsShedding(true);
        }
        snake.getBrain().eraseMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);


        if(!pendingPickup.getItem().isEmpty()) snake.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, pendingPickup);
        // force behavior termination next tick
        pendingPickup = null;
    }

    public static SnakeEatEgg create(){
        return new SnakeEatEgg();
    }
}
