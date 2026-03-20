package org.primal.entity.ai.behavior.shark;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SharkEntity;

public class SharkJumpOutWater extends Behavior<SharkEntity> {
    public SharkJumpOutWater() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_PRESENT),
                60);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SharkEntity shark) {
        var target= shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        
        return target.isPresent() && !target.get().isInWater() && shark.isInWater();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SharkEntity entity, long gameTime) {
        return true;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull SharkEntity shark, long gameTime) {
        var target= shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if(target.isEmpty())
            return;

        shark.getNavigation().stop();
        BehaviorUtils.lookAtEntity(shark, target.get());
    }
    private boolean breached;
    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull SharkEntity shark, long gameTime) {
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull SharkEntity shark, long gameTime) {
        boolean flag = this.breached;
        if (!flag) {
            FluidState fluidstate = shark.level().getFluidState(shark.blockPosition());
            this.breached = fluidstate.is(FluidTags.WATER);
        }

        Direction direction = shark.getMotionDirection();
        shark.setDeltaMovement(shark.getDeltaMovement().add((double)direction.getStepX() * 0.3, 0.4, (double)direction.getStepZ() * 0.3));
//        if (this.breached && !flag) {
//            shark.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
//        }

        Vec3 vec3 = shark.getDeltaMovement();
        if (vec3.y * vec3.y < 0.03F && shark.getXRot() != 0.0F) {
            shark.setXRot(Mth.rotLerp(0.2F, shark.getXRot(), 0.0F));
        } else if (vec3.length() > 1.0E-5F) {
            double d0 = vec3.horizontalDistance();
            double d1 = Math.atan2(-vec3.y, d0) * 180.0F / (float)Math.PI;
            shark.setXRot((float)d1);
        }
    }

    private boolean waterIsClear(@NotNull SharkEntity shark, BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return shark.level().getFluidState(blockpos).is(FluidTags.WATER) && !shark.level().getBlockState(blockpos).blocksMotion();
    }

    private boolean surfaceIsClear(@NotNull SharkEntity shark,BlockPos pos, int dx, int dz, int scale) {
        return shark.level().getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir()
                && shark.level().getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
    }
}
