package org.primal.entity.ai.controls.move;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Activities;

public class EagleMoveControl extends MoveControl {
    private final EagleEntity eagle;

    //Normal Speed
    private static final double THRUST = 0.10;     // acceleration
    private static final double MAX_SPEED = 0.30;   // max

    //Attacking Speed
    private static final double ATTACK_THRUST = 0.15;     // acceleration
    private static final double MAX_ATTACK_SPEED = 0.40;   // max

    //Ascending Speed
    private static final double ASCENDING_THRUST = 0.35;     // acceleration
    private static final double MAX_ASCENDING_SPEED = 0.50;   // max

    //Swooping Speed
    private static final double SWOOP_THRUST = 0.65;     // acceleration
    private static final double MAX_SWOOP_SPEED = 0.90;   // max

    //Follow with Elytra Speed
    private static final double FOLLOW_WITH_ELYTRA_THRUST = 0.70;     // acceleration
    private static final double MAX_FOLLOW_WITH_ELYTRA_SPEED = 0.90;   // max

    private static final double DAMPING = 0.98;    // air drag

    public EagleMoveControl(EagleEntity mob) {
        super(mob);
        this.eagle=mob;
    }

    @Override
    public void tick() {
        //Ground move control for babies
        if(mob.isBaby()){
            super.tick();
        }
        //Flying movement for adults
        else {

            if (this.operation == MoveControl.Operation.JUMPING)
                this.operation = MoveControl.Operation.WAIT;

            if (this.operation == Operation.MOVE_TO) {
                this.mob.setNoGravity(true);

                this.operation = Operation.WAIT;

                Vec3 pos = this.mob.position();
                Vec3 target = new Vec3(this.wantedX, this.wantedY, this.wantedZ);

                Vec3 dir = target.subtract(pos);
                double dist = dir.length();

                if (dist < 0.1) {
                    return;
                }

                var xRotWrapped= Mth.wrapDegrees(this.mob.getXRot());

                boolean swoop = xRotWrapped>50 && !this.eagle.isVehicle() && this.eagle.isAggressive();
                boolean ascending = xRotWrapped<-20;
                boolean attacking = this.mob.getTarget()!=null;
                boolean followWithElytra = this.eagle.getOwner()!=null
                        && this.eagle.getOwner().isFallFlying()
                        && this.eagle.getBrain().isActive(Primal_Activities.FOLLOW.get());
                dir = dir.normalize();

                // Apply thrust (real acceleration)
                Vec3 velocity = this.mob.getDeltaMovement();
                if(swoop)
                    velocity = velocity.add(dir.scale(SWOOP_THRUST));
                else if(ascending)
                    velocity = velocity.add(dir.scale(ASCENDING_THRUST));
                else if(attacking)
                    velocity = velocity.add(dir.scale(ATTACK_THRUST));
                else if(followWithElytra)
                    velocity = velocity.add(dir.scale(FOLLOW_WITH_ELYTRA_THRUST));
                else
                    velocity = velocity.add(dir.scale(THRUST));

                // Clamp max speed
                //Swoop
                if(swoop && velocity.length() > MAX_SWOOP_SPEED) velocity = velocity.normalize().scale(MAX_SWOOP_SPEED);
                //Ascending
                else if(ascending && velocity.length() > MAX_ASCENDING_SPEED) velocity = velocity.normalize().scale(MAX_ASCENDING_SPEED);
                //Attack
                else if(attacking && velocity.length() > MAX_ATTACK_SPEED) velocity = velocity.normalize().scale(MAX_ATTACK_SPEED);
                //Follow with elytra
                else if(followWithElytra && velocity.length() > MAX_FOLLOW_WITH_ELYTRA_SPEED) velocity = velocity.normalize().scale(MAX_FOLLOW_WITH_ELYTRA_SPEED);
                //Normal Speed
                else if (velocity.length() > MAX_SPEED) velocity = velocity.normalize().scale(MAX_SPEED);

                this.mob.setDeltaMovement(velocity);

                // Rotate toward movement direction
                float yaw = (float)(Mth.atan2(velocity.z, velocity.x) * (180F / Math.PI)) - 90F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yaw, swoop? 5: 90));

                float pitch = (float)(-Mth.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z)) * (180F / Math.PI));
                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), pitch, 90));

            } else {
                // Light damping when idle
                Vec3 v = this.mob.getDeltaMovement().scale(DAMPING);
                this.mob.setDeltaMovement(v);
                Vec3 vec3 = this.mob.getDeltaMovement();
                if (!this.mob.onGround() && vec3.y < 0.0) {
                    this.mob.setDeltaMovement(vec3.multiply(1.0, 0.8, 1.0));
                }

                Vec3 velocity = this.mob.getDeltaMovement();
                if (this.eagle.isFlying() && velocity.lengthSqr() > 1.0E-4) {
                    Vec3 dir = velocity.normalize();

                    dir = new Vec3(dir.x, dir.y * 0.1, dir.z).normalize();

                    Vec3 lookTarget = this.mob.position().add(dir.scale(4.0));
                    this.mob.getLookControl().setLookAt( lookTarget);
                }

                this.mob.setNoGravity(false);
            }
        }
    }
}
