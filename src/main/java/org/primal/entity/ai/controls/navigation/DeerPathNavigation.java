package org.primal.entity.ai.controls.navigation;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DeerPathNavigation extends GroundPathNavigation {
    public DeerPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new DeerNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanOpenDoors(false);
        this.nodeEvaluator.setCanFloat(true);
        this.nodeEvaluator.setCanWalkOverFences(true);

        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !this.level.getBlockState(pos.below()).isAir();
    }

    public static class DeerNodeEvaluator extends WalkNodeEvaluator {

        private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap<>();

        @Override
        public void done() {
            super.done();
            this.collisionCache.clear();
        }

        @Override
        @Nullable
        protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, @NotNull Direction direction, @NotNull PathType pathType) {
            Node node = null;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            double d0 = this.getFloorLevel(mutableBlockPos.set(x, y, z));
            if (d0 - nodeFloorLevel > this.getMobJumpHeight()) {
                return null;
            } else {

                PathType pathtype = this.getCachedPathType(x, y, z);
                float f = this.mob.getPathfindingMalus(pathtype);
                if (f >= 0.0F) {
                    node = this.getNodeAndUpdateCostToMax(x, y, z, pathtype, f);
                }

                if (doesBlockHavePartialCollision(pathType) && node != null && node.costMalus >= 0.0F && !this.canReachWithoutCollision(node)) {
                    node = null;
                }

                if (pathtype != PathType.WALKABLE && (!this.isAmphibious() || pathtype != PathType.WATER)) {
                    if ((node == null || node.costMalus < 0.0F)
                            && verticalDeltaLimit > 0
                            && (pathtype != PathType.FENCE || this.canWalkOverFences())
                            && pathtype != PathType.UNPASSABLE_RAIL
                            && pathtype != PathType.TRAPDOOR
                            && pathtype != PathType.POWDER_SNOW) {
                        node = this.tryJumpOn(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType, mutableBlockPos);
                    } else if (!this.isAmphibious() && pathtype == PathType.WATER && !this.canFloat()) {
                        node = this.tryFindFirstNonWaterBelow(x, y, z, node);
                    } else if (pathtype == PathType.OPEN) {
                        node = this.tryFindFirstGroundNodeBelow(x, y, z);
                    } else if (doesBlockHavePartialCollision(pathtype) && node == null) {
                        node = this.getClosedNode(x, y, z, pathtype);
                    }
                }


                return node;
            }
        }

        private double getMobJumpHeight() {
            return this.mob.isSprinting()? 2.125: Math.max(1.125, this.mob.maxUpStep());
        }

        private Node getNodeAndUpdateCostToMax(int x, int y, int z, PathType pathType, float malus) {
            Node node = this.getNode(x, y, z);
            node.type = pathType;
            node.costMalus = Math.max(node.costMalus, malus);
            return node;
        }

        private Node getBlockedNode(int x, int y, int z) {
            Node node = this.getNode(x, y, z);
            node.type = PathType.BLOCKED;
            node.costMalus = -1.0F;
            return node;
        }

        private Node getClosedNode(int x, int y, int z, PathType pathType) {
            Node node = this.getNode(x, y, z);
            node.closed = true;
            node.type = pathType;
            node.costMalus = pathType.getMalus();
            return node;
        }

        private static boolean doesBlockHavePartialCollision(PathType pathType) {
            return pathType == PathType.FENCE || pathType == PathType.DOOR_WOOD_CLOSED || pathType == PathType.DOOR_IRON_CLOSED;
        }

        @Nullable
        private Node tryJumpOn(
                int x,
                int y,
                int z,
                int verticalDeltaLimit,
                double nodeFloorLevel,
                Direction direction,
                PathType pathType,
                BlockPos.MutableBlockPos pos
        ) {
            Node node = this.findAcceptedNode(x, y + 1, z, verticalDeltaLimit - 1, nodeFloorLevel, direction, pathType);
            if (node == null) {
                return null;
            } else if (this.mob.getBbWidth() >= 1.0F) {
                return node;
            } else if (node.type != PathType.OPEN && node.type != PathType.WALKABLE) {
                return node;
            } else {
                double d0 = (double)(x - direction.getStepX()) + 0.5;
                double d1 = (double)(z - direction.getStepZ()) + 0.5;
                double d2 = (double)this.mob.getBbWidth() / 2.0;
                AABB aabb = new AABB(
                        d0 - d2,
                        this.getFloorLevel(pos.set(d0, y + 1, d1)) + 0.001,
                        d1 - d2,
                        d0 + d2,
                        (double)this.mob.getBbHeight() + this.getFloorLevel(pos.set(node.x, node.y, (double)node.z)) - 0.002,
                        d1 + d2
                );
                return this.hasCollisions(aabb) ? null : node;
            }
        }

        private boolean hasCollisions(AABB boundingBox) {
            return this.collisionCache.computeIfAbsent(boundingBox, p_330163_ -> !this.currentContext.level().noCollision(this.mob, boundingBox));
        }

        private Node tryFindFirstGroundNodeBelow(int x, int y, int z) {
            for (int i = y - 1; i >= this.mob.level().getMinBuildHeight(); i--) {
                if (y - i > this.mob.getMaxFallDistance()) {
                    return this.getBlockedNode(x, i, z);
                }

                PathType pathtype = this.getCachedPathType(x, i, z);
                float f = this.mob.getPathfindingMalus(pathtype);
                if (pathtype != PathType.OPEN) {
                    if (f >= 0.0F) {
                        return this.getNodeAndUpdateCostToMax(x, i, z, pathtype, f);
                    }

                    return this.getBlockedNode(x, i, z);
                }
            }

            return this.getBlockedNode(x, y, z);
        }

        @Nullable
        private Node tryFindFirstNonWaterBelow(int x, int y, int z, @Nullable Node node) {
            y--;

            while (y > this.mob.level().getMinBuildHeight()) {
                PathType pathtype = this.getCachedPathType(x, y, z);
                if (pathtype != PathType.WATER) {
                    return node;
                }

                node = this.getNodeAndUpdateCostToMax(x, y, z, pathtype, this.mob.getPathfindingMalus(pathtype));
                y--;
            }

            return node;
        }

        private boolean canReachWithoutCollision(Node node) {
            AABB aabb = this.mob.getBoundingBox();
            Vec3 vec3 = new Vec3(
                    (double)node.x - this.mob.getX() + aabb.getXsize() / 2.0,
                    (double)node.y - this.mob.getY() + aabb.getYsize() / 2.0,
                    (double)node.z - this.mob.getZ() + aabb.getZsize() / 2.0
            );
            int i = Mth.ceil(vec3.length() / aabb.getSize());
            vec3 = vec3.scale(1.0F / (float)i);

            for (int j = 1; j <= i; j++) {
                aabb = aabb.move(vec3);
                if (this.hasCollisions(aabb)) {
                    return false;
                }
            }

            return true;
        }
    }
}
