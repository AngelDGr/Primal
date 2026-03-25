package org.primal.entity.ai.behavior.lion;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.advancements.criterion.Primal_CustomCriterion;
import org.primal.util.mob_types.PrimalTamable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class AnimalRelaxOnSleepingOwner<T extends TamableAnimal & PrimalTamable> extends Behavior<T> {

    private final Predicate<T> canUse;
    private final Function<LivingEntity, Float> speedModifier;
    private @Nullable BlockPos goalPos;
    private final @Nullable ResourceLocation giftLootTable;
    private final float giftProbability;
    @Nullable
    private final Primal_CustomCriterion advancement;

    public static<T extends TamableAnimal & PrimalTamable> AnimalRelaxOnSleepingOwner<T> create(float speedModifier, @Nullable ResourceLocation giftLootTable, float giftProbability, Primal_CustomCriterion advancement) {
        return create(mob -> speedModifier, giftLootTable, giftProbability, advancement);
    }
    public static<T extends TamableAnimal & PrimalTamable> AnimalRelaxOnSleepingOwner<T> create(Function<LivingEntity, Float> speedModifier, @Nullable ResourceLocation giftLootTable, float giftProbability, Primal_CustomCriterion advancement) {
        return new AnimalRelaxOnSleepingOwner<>(m->true, speedModifier, giftLootTable, giftProbability, advancement);
    }

    public AnimalRelaxOnSleepingOwner(Predicate<T> canUse, Function<LivingEntity, Float> speedModifier, @Nullable ResourceLocation giftLootTable, float giftProbability, @Nullable Primal_CustomCriterion advancement) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT
        ), Integer.MAX_VALUE);
        this.canUse=canUse;
        this.speedModifier=speedModifier;
        this.giftLootTable=giftLootTable;
        this.giftProbability=giftProbability;
        this.advancement=advancement;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T mob) {
        if(canUse.test(mob)){
            if(mob.isTame()) {

                LivingEntity owner = mob.getOwner();
                if (owner instanceof Player ownerPlayer) {

                    if (!owner.isSleeping()) {
                        return false;
                    }

                    if (mob.distanceToSqr(ownerPlayer) > 100.0) {
                        return false;
                    }

                    BlockPos blockpos = ownerPlayer.blockPosition();
                    BlockState blockstate = mob.level().getBlockState(blockpos);
                    goalPos = blockstate.getOptionalValue(BedBlock.FACING)
                            .map(direction -> blockpos.relative(direction.getOpposite()))
                            .orElseGet(() -> new BlockPos(blockpos));

                    return blockstate.is(BlockTags.BEDS) && !spaceIsOccupied(mob, goalPos);

                }

                return false;
            }
        }

        return false;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        return checkExtraStartConditions(level, entity) && hasRequiredMemories(entity);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {}

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        LivingEntity owner = mob.getOwner();

        if (owner instanceof Player ownerPlayer) {
            if(goalPos!=null){
                // Middle of the bed block
//                Vec3 center = Vec3.atCenterOf(goalPos);
                mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(ownerPlayer, true));
                mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(goalPos, speedModifier.apply(mob), 0));
                mob.setIsLaying(true);
            }
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        LivingEntity owner = mob.getOwner();
        if (owner instanceof Player ownerPlayer) {
            float f = mob.level().getTimeOfDay(1.0F);
            if (ownerPlayer.getSleepTimer() >= 100 && (double)f > 0.77 && (double)f < 0.8 && mob.getRandom().nextFloat() < this.giftProbability) {
                giveMorningGift(mob);
            }

            if(advancement!=null){
                var players = level.getEntitiesOfClass(
                        Player.class,
                        new AABB(
                                mob.blockPosition().getX()-1.5,
                                mob.blockPosition().getY()-1.5,
                                mob.blockPosition().getZ()-1.5,

                                mob.blockPosition().getX()+1.5,
                                mob.blockPosition().getY()+1.5,
                                mob.blockPosition().getZ()+1.5
                        )
                ).stream().filter(player1 -> player1.equals(ownerPlayer)).toList();

                if(!players.isEmpty() && players.get(0) instanceof ServerPlayer serverPlayer){
                    advancement.trigger(serverPlayer);
                }
            }
        }
        mob.setIsLaying(false);
    }

    private static<T extends TamableAnimal & PrimalTamable> boolean spaceIsOccupied(T mob, BlockPos goalPos) {
        for (TamableAnimal pet : mob.level().getEntitiesOfClass(TamableAnimal.class, new AABB(goalPos).inflate(2.0))) {

            if (pet != mob && ((pet instanceof PrimalTamable primalTamable && primalTamable.isLaying()))) {
                return true;
            }
        }

        return false;
    }

    public List<ItemStack> getGifts(T mob) {
        if (giftLootTable == null || !(mob.level() instanceof ServerLevel serverLevel))
            return List.of();

        LootTable lootTable = serverLevel.getServer()
                .getLootData()
                .getLootTable(this.giftLootTable);

        return lootTable.getRandomItems(new LootParams.Builder((ServerLevel)mob.level())
                .withParameter(LootContextParams.THIS_ENTITY, mob)
                .withParameter(LootContextParams.ORIGIN, mob.position())
                .create(LootContextParamSets.GIFT));
    }

    private void giveMorningGift(T mob) {
        RandomSource randomsource = mob.getRandom();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        blockpos$mutableblockpos.set(mob.isLeashed() ? mob.getLeashHolder().blockPosition() : mob.blockPosition());
        mob
                .randomTeleport(
                        blockpos$mutableblockpos.getX() + randomsource.nextInt(11) - 5,
                        blockpos$mutableblockpos.getY() + randomsource.nextInt(5) - 2,
                        blockpos$mutableblockpos.getZ() + randomsource.nextInt(11) - 5,
                        false
                );
        blockpos$mutableblockpos.set(mob.blockPosition());

        for (ItemStack itemstack : getGifts(mob)) {
            mob
                    .level()
                    .addFreshEntity(
                            new ItemEntity(
                                    mob.level(),
                                    (double)blockpos$mutableblockpos.getX() - (double) Mth.sin(mob.yBodyRot * (float) (Math.PI / 180.0)),
                                    blockpos$mutableblockpos.getY(),
                                    (double)blockpos$mutableblockpos.getZ() + (double)Mth.cos(mob.yBodyRot * (float) (Math.PI / 180.0)),
                                    itemstack
                            )
                    );
        }
    }
}

