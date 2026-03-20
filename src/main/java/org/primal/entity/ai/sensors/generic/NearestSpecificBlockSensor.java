package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class NearestSpecificBlockSensor<T extends LivingEntity> extends Sensor<T> {

    @Nullable
    public final TagKey<Block> blockTag;
    @Nullable
    public final Block block;
    @Nullable
    public final BiPredicate<T, BlockPos> blockPredicate;
    public final int widthDetection;
    public final int heightDetection;
    public final Predicate<T> canUse;
    public NearestSpecificBlockSensor(Predicate<T> canUse, @Nullable TagKey<Block> blockTag, @Nullable Block block, @Nullable BiPredicate<T, BlockPos> blockPredicate, int widthDetection, int heightDetection){
        super(40);
        this.canUse=canUse;
        this.blockTag = blockTag;
        this.block=block;
        this.blockPredicate=blockPredicate;
        this.widthDetection=widthDetection;
        this.heightDetection=heightDetection;
    }

    public NearestSpecificBlockSensor(Block block, int widthDetection, int heightDetection){
        this(l->true, null, block, null, widthDetection, heightDetection);
    }

    public NearestSpecificBlockSensor(TagKey<Block> blockTag, int widthDetection, int heightDetection){
        this(l->true, blockTag, null, null, widthDetection, heightDetection);
    }

    public NearestSpecificBlockSensor(BiPredicate<T, BlockPos> blockPredicate, int widthDetection, int heightDetection){
        this(l->true, null, null, blockPredicate, widthDetection, heightDetection);
    }

    public NearestSpecificBlockSensor(Predicate<T> entityPredicate, Block block, int widthDetection, int heightDetection) {
        this(entityPredicate, null, block, null, widthDetection, heightDetection);
    }

    public NearestSpecificBlockSensor(Predicate<T> entityPredicate, TagKey<Block> blockTag, int widthDetection, int heightDetection) {
        this(entityPredicate, blockTag, null, null, widthDetection, heightDetection);
    }

    public NearestSpecificBlockSensor(Predicate<T> entityPredicate, BiPredicate<T, BlockPos> blockPredicate, int widthDetection, int heightDetection) {
        this(entityPredicate, null, null, blockPredicate, widthDetection, heightDetection);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull T entity) {
        if(canUse.test(entity))
            entity.getBrain()
                    .setMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(), findNearestBlock(level, entity));
    }

    public Optional<BlockPos> findNearestBlock(ServerLevel level, T mob) {
        if(this.blockTag!=null)
            return BlockPos.findClosestMatch(mob.blockPosition(), this.widthDetection, this.heightDetection, pos -> level.getBlockState(pos).is(this.blockTag));

        if(this.block!=null)
            return BlockPos.findClosestMatch(mob.blockPosition(), this.widthDetection, this.heightDetection, pos -> level.getBlockState(pos).is(this.block));

        if(this.blockPredicate!=null)
            return BlockPos.findClosestMatch(mob.blockPosition(), this.widthDetection, this.heightDetection, b-> blockPredicate.test(mob, b));

        return Optional.empty();
    }
}
