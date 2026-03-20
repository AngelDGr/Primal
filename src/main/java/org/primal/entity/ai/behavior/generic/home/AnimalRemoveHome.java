package org.primal.entity.ai.behavior.generic.home;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Tags;

import java.util.function.Predicate;

public class AnimalRemoveHome<T extends Animal> extends Behavior<T> {
    @Nullable
    private final Block block;
    @Nullable
    private final TagKey<Block> blockTagKey;

    private final Predicate<T> removesHomeIf;

    public AnimalRemoveHome(TagKey<Block> blockTagKey, Predicate<T> removesHomeIf) {
        this(null, blockTagKey, removesHomeIf);
    }

    public AnimalRemoveHome(Block block, Predicate<T> removesHomeIf) {
        this(block, null, removesHomeIf);
    }

    public AnimalRemoveHome(TagKey<Block> blockTagKey) {
        this(null, blockTagKey, a->true);
    }

    public AnimalRemoveHome(Block block) {
        this(block, null, a->true);
    }

    public AnimalRemoveHome(@Nullable Block block, @Nullable TagKey<Block> blockTagKey, Predicate<T> removesHomeIf) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.HOME,
                MemoryStatus.VALUE_PRESENT
        ));
        this.block=block;
        this.blockTagKey=blockTagKey;
        this.removesHomeIf=removesHomeIf;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T animal) {

        var hasHome = animal.getBrain().getMemory(MemoryModuleType.HOME).isPresent();
        var isNotBlock = hasHome && this.block!=null && !level.getBlockState(animal.getBrain().getMemory(MemoryModuleType.HOME).get().pos()).is(block);
        var isNotTag = hasHome && this.blockTagKey!=null && !level.getBlockState(animal.getBrain().getMemory(MemoryModuleType.HOME).get().pos()).is(blockTagKey);
        var removesHome = this.removesHomeIf.test(animal);

        return isNotBlock || isNotTag || removesHome;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull T animal, long gameTime) {
        animal.getBrain().eraseMemory(MemoryModuleType.HOME);
        level.broadcastEntityEvent(animal, (byte)13);
    }

    public static<T extends Animal> boolean isFar(T animal, int distance){
        //Or if it is 150 blocks away while a baby
        return animal.getBrain().getMemory(MemoryModuleType.HOME).isPresent()
                && animal.getOnPos().distSqr(animal.getBrain().getMemory(MemoryModuleType.HOME).get().pos())>(distance*distance);
    }

    public static<T extends Animal> boolean isBabyFar(T animal){
        //Or if it is 150 blocks away while a baby
        return isBabyFar(animal,150);
    }

    public static<T extends Animal> boolean isBabyFar(T animal, int distance){
        //Or if it is 150 blocks away while a baby
        return isFar(animal, distance) && animal.isBaby();
    }

    public static<T extends Animal> AnimalRemoveHome<T> basicNest(int distanceToRemoveWhenBaby){
        return new AnimalRemoveHome<>(Primal_Blocks.NEST_BLOCK.get(), animal-> isBabyFar(animal, distanceToRemoveWhenBaby));
    }

    public static<T extends Animal> AnimalRemoveHome<T> basicHollowLog(int distanceToRemoveWhenBaby){
        return new AnimalRemoveHome<>(Primal_Tags.Block.HOLLOW_LOGS, animal-> isBabyFar(animal, distanceToRemoveWhenBaby));
    }
}
