package org.primal.entity.ai.behavior.generic.bird;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;
import java.util.function.Predicate;

public class BirdDescending {

    public static <T extends PathfinderMob> OneShot<T> create(
            float speedModifier,
            int xzRange,
            Predicate<T> canDescend,
            UniformInt restRecovered
    ) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.absent(MemoryModuleType.WALK_TARGET),
                instance.absent(Primal_MemoryModuleTypes.RESTED_TIME.get()),
                instance.registered(Primal_MemoryModuleTypes.REST_NEEDED.get()),
                instance.registered(Primal_MemoryModuleTypes.LANDING_POS.get())
        ).apply(instance, (walkTargetMemoryAccessor,
                           restedTimeMemoryAccessor,
                           restNeededMemoryAccessor,
                           landingPosMemoryAccessor)
                -> (serverLevel, mob, gameTime) -> {
            if (!canDescend.test(mob)) return false;

            Optional<Integer> restedNeeded = instance.tryGet(restNeededMemoryAccessor);

            //Reduces timer
            if(mob.onGround() || mob.isInWater()){

                if(restedNeeded.isPresent()){
                    //If above 0, decreases the rested needed time
                    if(restedNeeded.get()>0)
                        restNeededMemoryAccessor.setOrErase(Optional.of(restedNeeded.get() - 1));

                    //Auto resets if touches water, to avoid weird swimming
                    if(restedNeeded.get()==0 || mob.isInWater()){
                        mob.getBrain().setMemory(
                                Primal_MemoryModuleTypes.RESTED_TIME.get(),
                                mob.getRandom().nextIntBetweenInclusive(restRecovered.getMinValue(), restRecovered.getMaxValue()));
                        restNeededMemoryAccessor.erase();
                    }
                }
                else {
                    mob.getBrain().setMemory(
                            Primal_MemoryModuleTypes.RESTED_TIME.get(),
                            mob.getRandom().nextIntBetweenInclusive(restRecovered.getMinValue(), restRecovered.getMaxValue()));
                }
            }

            Optional<BlockPos> landingPos = instance.tryGet(landingPosMemoryAccessor);

            if(landingPos.isPresent()){

                walkTargetMemoryAccessor.set(new WalkTarget(landingPos.get(), speedModifier, 0));

            } else {

                int offsetX = mob.getRandom().nextIntBetweenInclusive(-xzRange, xzRange);
                int offsetZ = mob.getRandom().nextIntBetweenInclusive(-xzRange, xzRange);
                BlockPos startPos = mob.blockPosition();

                BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(
                        startPos.getX() + offsetX,
                        startPos.getY() - 1,
                        startPos.getZ() + offsetZ
                );

                // Search downward for the first solid block
                while (cursor.getY() > serverLevel.getMinBuildHeight()) {
                    BlockState state = serverLevel.getBlockState(cursor);

                    if (state.isFaceSturdy(serverLevel, cursor, Direction.UP) && state.getFluidState().isEmpty()) {
                        // Target one block above the solid surface
                        Vec3 landingPosVec = Vec3.atCenterOf(cursor.above());
                        walkTargetMemoryAccessor.set(
                                new WalkTarget(landingPosVec, speedModifier, 0)
                        );

                        //Per 5s
                        landingPosMemoryAccessor.setWithExpiry(BlockPos.containing(landingPosVec), 100);
                        return true;
                    }

                    cursor.move(Direction.DOWN);
                }
            }

            return true;
        }));
    }
}
