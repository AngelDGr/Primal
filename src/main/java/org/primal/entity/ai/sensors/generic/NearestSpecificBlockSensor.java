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

public class NearestSpecificBlockSensor extends Sensor<LivingEntity> {

    @Nullable
    public final TagKey<Block> blockTag;
    @Nullable
    public final Block block;
    public final int widthDetection;
    public final int heightDetection;
    public NearestSpecificBlockSensor(@Nullable TagKey<Block> blockTag, @Nullable Block block, int widthDetection, int heightDetection){
        this.blockTag = blockTag;
        this.block=block;
        this.widthDetection=widthDetection;
        this.heightDetection=heightDetection;
    }

    public NearestSpecificBlockSensor(Block block, int widthDetection, int heightDetection){
        this(null, block, widthDetection, heightDetection);
    }

    public NearestSpecificBlockSensor(TagKey<Block> blockTag, int widthDetection, int heightDetection){
        this(blockTag, null, widthDetection, heightDetection);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());
    }

    protected void doTick(@NotNull ServerLevel level, LivingEntity entity) {
        entity.getBrain()
                .setMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(), findNearestBlock(level, entity));
    }

    public Optional<BlockPos> findNearestBlock(ServerLevel level, LivingEntity mob) {
        if(this.blockTag!=null)
            return BlockPos.findClosestMatch(mob.blockPosition(), this.widthDetection, this.heightDetection, pos -> level.getBlockState(pos).is(this.blockTag));

        if(this.block!=null)
            return BlockPos.findClosestMatch(mob.blockPosition(), this.widthDetection, this.heightDetection, pos -> level.getBlockState(pos).is(this.block));

        return Optional.empty();
    }
}
