package org.primal.entity.ai.behavior.generic.home;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AnimalGoesToBlock<T extends Animal> extends Behavior<T> {
    @Nullable
    private final Block block;
    @Nullable
    private final TagKey<Block> blockTagKey;
    private final Predicate<T> canUse;

    public AnimalGoesToBlock(Block block, Predicate<T> canUse) {
        this(block, null, canUse);
    }

    public AnimalGoesToBlock(TagKey<Block> blockTagKey, Predicate<T> canUse) {
        this(null, blockTagKey, canUse);
    }

    private AnimalGoesToBlock(@Nullable Block block, @Nullable TagKey<Block> blockTagKey, Predicate<T> canUse) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(), MemoryStatus.VALUE_PRESENT
        ));
        this.block = block;
        this.blockTagKey = blockTagKey;
        this.canUse=canUse;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, T animal) {
        var memory = animal.getBrain()
                .getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());

        if(canUse.test(animal)) {
            if (memory.isEmpty())
                return false;

            var state = level.getBlockState(memory.get());

            if (this.block != null) return state.is(this.block);

            if (this.blockTagKey != null) return state.is(this.blockTagKey);
        }

        return false;
    }

    @Override
    protected void start(@NotNull ServerLevel level, T animal, long gameTime) {
        var memory = animal.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());

        if (memory.isEmpty()) return;

        animal.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        animal.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(memory.get(), 1.0f, 0));
    }

    public static<T extends Animal> AnimalGoesToBlock<T> toNest(){
        return new AnimalGoesToBlock<>(Primal_Blocks.NEST_BLOCK.get(), m-> true);
    }

    public static<T extends Animal> AnimalGoesToBlock<T> toHollowLog(){
        return new AnimalGoesToBlock<>(Primal_Tags.Block.HOLLOW_LOGS, m-> true);
    }

    public static<T extends Animal> AnimalGoesToBlock<T> toNest(Predicate<T> canUse){
        return new AnimalGoesToBlock<>(Primal_Blocks.NEST_BLOCK.get(), canUse);
    }

    public static<T extends Animal> AnimalGoesToBlock<T> toHollowLog(Predicate<T> canUse){
        return new AnimalGoesToBlock<>(Primal_Tags.Block.HOLLOW_LOGS, canUse);
    }
}
