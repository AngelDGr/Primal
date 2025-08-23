package org.primal.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MiscUtil {

    /**
     * Works just like the GeckoLib {@link software.bernie.geckolib.animation.AnimationState#isMoving()} but on server side
     */
    public static boolean isMoving(LivingEntity entity, float motionThreshold){
        float limbSwingAmount = 0;


        if (entity.isAlive()) {
            limbSwingAmount = entity.walkAnimation.speed(entity.tickCount);


            if (limbSwingAmount > 1f)
                limbSwingAmount = 1f;
        }

        Vec3 velocity = entity.getDeltaMovement();
        float avgVelocity = (float)((Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f);

        return avgVelocity >= motionThreshold && limbSwingAmount != 0;
    }

    /**
     * Detects if an entity is directly facing other
     * @param target The entity to check
     * @param watcher The entity that is watching
     * @param angle The value of how much angle it needs to return true, 0.1 to 1.0 detects from the back, -0.1 to -1.0 detects from the front
     */
    public static boolean isSeeingTarget(Entity target, Entity watcher, float angle) {
        Vec3 vec3d = target.position();

        Vec3 vec3d2 = watcher.calculateViewVector(0.0f, watcher.getYHeadRot());
        Vec3 vec3d3 = vec3d.vectorTo(watcher.position());
        vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();

        return vec3d3.dot(vec3d2) < angle;
    }
}
