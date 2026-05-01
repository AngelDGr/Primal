package org.primal.entity.ai.behavior.cassowary;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.entity.animal.CassowaryEntity;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

import java.util.List;

public class CassowaryPickFruit extends Behavior<CassowaryEntity> {

    public CassowaryPickFruit() {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.HURT_RECENTLY.get(),
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryStatus.VALUE_PRESENT), 38
        );
    }

    @Nullable
    public ItemEntity pendingPickup;

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary) {
        Vec3 look = cassowary.getLookAngle().normalize();

        double forward = 0.4;
        double width = 0.35;
        double height = 0.35;

        AABB pickupBox = cassowary.getBoundingBox()
                .move(look.x * forward, 0.0, look.z * forward)
                .inflate(width, height, width);

        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, pickupBox)) {
            if (!item.isRemoved()
                    && !item.getItem().isEmpty()
                    && cassowary.wantsToPickUp(item.getItem())
                    && !Primal_Util.isMoving(cassowary, 0.015f)) {

                pendingPickup = item;
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary, long gameTime) {
        return pendingPickup != null
                && !pendingPickup.isRemoved()
                && !pendingPickup.getItem().isEmpty()
                && cassowary.getBrain().getMemory(Primal_MemoryModuleTypes.HURT_RECENTLY.get()).isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary, long gameTime) {
        cassowary.triggerPickFruit();
        timer++;

        cassowary.getNavigation().stop();
        cassowary.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);

        if (pendingPickup != null)
            cassowary.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(pendingPickup, true));
    }

    private int timer=0;

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary, long gameTime) {
        cassowary.getNavigation().stop();
        cassowary.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        timer++;

        if (pendingPickup != null){
            cassowary.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(pendingPickup, true));
            if(timer==30){
                if (cassowary.level() instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(
                            new ItemParticleOption(ParticleTypes.ITEM, pendingPickup.getItem()),
                            pendingPickup.getX(), pendingPickup.getY(), pendingPickup.getZ(),
                            8, 0.15, 0.1, 0.15, 0.02
                    );

                //Drops the seed
                if (pendingPickup.getItem().is(Primal_Tags.Item.PROCESSES_CASSOWARY)) {
                    boolean first = true;
                    //Set loot table and plays sound on the first one
                    for (ItemStack seedStack : cassowary.getDroppedSeeds()) {
                        if (seedStack.isEmpty()) continue;

                        BehaviorUtils.throwItem(cassowary, seedStack, cassowary.position().add(0, 1, 0));

                        if (first) {
                            cassowary.playSound(cassowary.getPlopSound());
                            first = false;
                        }
                    }

                    //Triggers advancement
                    List<ServerPlayer> playersList= cassowary.level().getEntitiesOfClass(ServerPlayer.class, cassowary.getBoundingBox().inflate(24));

                    if(!playersList.isEmpty()){
                        playersList.forEach(Primal_Advancements.FEED_PETRIFIED::trigger);
                    }
                }

                ItemStack stack = pendingPickup.getItem();
                stack.shrink(1);
                if (stack.isEmpty()) pendingPickup.discard();

                if(cassowary.playEatingSound())
                    cassowary.heal(2);
                cassowary.getBrain().eraseMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
            }
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary, long gameTime) {
        if (pendingPickup == null) return;


        if(!pendingPickup.getItem().isEmpty()) cassowary.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, pendingPickup);
        // force behavior termination next tick
        pendingPickup = null;
        cassowary.getBrain().setMemoryWithExpiry(MemoryModuleType.PACIFIED, true, 200);
        timer=0;
    }

    public static CassowaryPickFruit create(){
        return new CassowaryPickFruit();
    }
}
