package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_DamageTypes;
import org.primal.util.Primal_Util;

public class ThornyAcaciaLog extends RotatedPillarBlock {
    private final boolean allFacesDoDamage;
    public ThornyAcaciaLog(Properties properties, boolean allFacesDoDamage) {
        super(properties);
        this.allFacesDoDamage = allFacesDoDamage;
    }

    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0, 1.0, 1.0, 15.0, 15.0, 15.0);
    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return COLLISION_SHAPE;
    }

    protected static final VoxelShape OUTLINE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (level.isClientSide) return;

        if(allFacesDoDamage){
            entity.hurt(Primal_DamageTypes.thornyAcacia(level), 2.0F);
            return;
        }

        // Block AABB (full cube)
        AABB blockBox = new AABB(pos);

        // Entity AABB
        AABB entityBox = entity.getBoundingBox();

        if (!blockBox.intersects(entityBox)) return; // not touching

        // Distances from faces
        Direction face = Primal_Util.getFaceTouchedByEntity(entityBox, blockBox);

        if (face != null) {

            switch (state.getValue(AXIS)){
                case Y:
                    if(face!=Direction.UP && face!=Direction.DOWN)
                        entity.hurt(Primal_DamageTypes.thornyAcacia(level), 2.0F);
                    break;
                case X:
                    if(face!=Direction.EAST && face!=Direction.WEST)
                        entity.hurt(Primal_DamageTypes.thornyAcacia(level), 2.0F);
                    break;
                case Z:
                    if(face!=Direction.NORTH && face!=Direction.SOUTH)
                        entity.hurt(Primal_DamageTypes.thornyAcacia(level), 2.0F);
                    break;
            }
        }
    }

    @Override
    public @Nullable PathType getBlockPathType(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Mob mob) {
//        return super.getBlockPathType(state, level, pos, mob);
        return PathType.DAMAGE_OTHER;
    }

    @Override
    public @Nullable PathType getAdjacentBlockPathType(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Mob mob, @NotNull PathType originalType) {
        return PathType.DANGER_OTHER;
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }
}
