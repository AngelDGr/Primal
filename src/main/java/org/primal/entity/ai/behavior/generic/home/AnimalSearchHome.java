package org.primal.entity.ai.behavior.generic.home;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;

public class AnimalSearchHome extends Behavior<Animal> {

    @Nullable
    private final Block block;
    @Nullable
    private final TagKey<Block> blockTagKey;

    public AnimalSearchHome(TagKey<Block> blockTagKey) {
        this(null, blockTagKey);
    }

    public AnimalSearchHome(Block block) {
        this(block, null);
    }

    public AnimalSearchHome(@Nullable Block block, @Nullable TagKey<Block> blockTagKey) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.HOME,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_PRESENT
        ));
        this.block=block;
        this.blockTagKey=blockTagKey;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull Animal eagle) {

        var hasBlock = eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isPresent();
        var isBlock = hasBlock && this.block!=null && level.getBlockState(eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).is(block);
        var isTag = hasBlock && this.blockTagKey!=null && level.getBlockState(eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).is(blockTagKey);

        return isBlock || isTag;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull Animal eagle, long gameTime) {
        if(!checkExtraStartConditions(level, eagle) || eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isEmpty()){
            stop(level, eagle, gameTime);
            return;
        }

        GlobalPos pos = GlobalPos.of(level.dimension(), eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get());

        eagle.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        eagle.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(pos.pos(), 1.0f, 3));

        eagle.getBrain().setMemory(MemoryModuleType.HOME, pos);

        level.broadcastEntityEvent(eagle, (byte)14);
    }

    public static AnimalSearchHome fromNest(){
        return new AnimalSearchHome(Primal_Blocks.NEST_BLOCK.get());
    }

    public static AnimalSearchHome fromHollowLog(){
        return new AnimalSearchHome(Primal_Tags.Block.HOLLOW_LOGS);
    }
}
