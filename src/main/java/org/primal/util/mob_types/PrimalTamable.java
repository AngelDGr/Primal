package org.primal.util.mob_types;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;
import org.primal.util.Primal_Util;

public interface PrimalTamable {

    int WANDERING_STATE = 0;
    int FOLLOWING_STATE = 1;
    int SITTING_STATE = 2;

    private TamableAnimal self() {
        return (TamableAnimal) this;
    }

    int getFollowerState();

    void setFollowerState(int state);

    default boolean isSitting() {
        return this.getFollowerState() == SITTING_STATE;
    }

    default boolean isFollowing() {
        return this.getFollowerState() == FOLLOWING_STATE;
    }

    default boolean isWandering() {
        return this.getFollowerState() == WANDERING_STATE;
    }

    boolean handleEating(@NotNull Player player, @NotNull ItemStack stack);

    default boolean hasCollar() {
        return true;
    }

    default void setHasCollar(boolean hasCollar){}

    default boolean hasRemovableCollar() {
        return false;
    }

    DyeColor getCollarColor();

    void setCollarColor(DyeColor collarColor);

    default InteractionResult changeFollowState(Player player, InteractionHand hand){
        if(player!=self().getOwner()){
            if (self().level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }

        if(!self().level().isClientSide && hand.equals(InteractionHand.MAIN_HAND)){
            this.setFollowerState(this.isWandering()? FOLLOWING_STATE: this.isFollowing()? SITTING_STATE: WANDERING_STATE);
            self().setOrderedToSit(this.isSitting());
            self().setInSittingPose(this.isSitting());

            player.displayClientMessage(Component.translatable(
                    this.isFollowing()? "primal.gui.animal_following":
                            this.isSitting()? "primal.gui.animal_sitting":
                                    "primal.gui.animal_wandering",
                    self().getName()), true);
        }

        return InteractionResult.sidedSuccess(self().level().isClientSide());
    }


    default boolean dyeCollar(Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);

        //To handle dye collar logic
        if (stackInHand.getItem() instanceof DyeItem dyeitem && self().isOwnedBy(player)) {
            DyeColor dyecolor = dyeitem.getDyeColor();
            if (dyecolor != this.getCollarColor() || !hasCollar()) {
                this.setCollarColor(dyecolor);
                Primal_Util.OneTwentyEquivalent.consumeStack(1, player, stackInHand);
                return true;
            }
        }

        return false;
    }

    default boolean canBabyAttack(@NotNull Entity target){
        return false;
    }

    default void sprintWhenFollowing(int startDistance, int stopDistance, double speedThreshold){
        //Make sprint when too far
        if(self().getOwner()!=null && this.isFollowing()){
            //Start
            if(self().distanceToSqr(self().getOwner())>(startDistance*startDistance) && self().getDeltaMovement().length()>speedThreshold)
                self().setSprinting(true);

            //Stop
            if(self().distanceToSqr(self().getOwner())<(stopDistance*stopDistance) || self().getDeltaMovement().length()<speedThreshold)
                self().setSprinting(false);
        }
    }


    default void addAdditionalSaveDataTamable(@NotNull CompoundTag compound) {
        if(self().getOwnerUUID()!=null)
            compound.putInt("FollowerState", this.getFollowerState());

        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());

        if(this.canLayOnBed())
            compound.putBoolean("IsLaying", this.isLaying());

        if(this.hasRemovableCollar())
            compound.putBoolean("HasCollar", this.hasCollar());
    }

    default void readAdditionalSaveDataTamable(@NotNull CompoundTag compound) {
        if (compound.hasUUID("Owner"))
            this.setFollowerState(compound.getBoolean("Sitting")? SITTING_STATE: compound.getInt("FollowerState"));

        if (compound.contains("CollarColor", 99))
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));

        if(compound.contains("IsLaying"))
            this.setIsLaying(compound.getBoolean("IsLaying"));

        if(compound.contains("HasCollar"))
            this.setHasCollar(compound.getBoolean("HasCollar"));
    }

    default boolean isLaying() {
        return false;
    }

    default void setIsLaying(boolean isLaying) {}

    default boolean canLayOnBed(){
        return false;
    }

    /** A couple of helping methods that only exist on Minecraft 1.21 */

    default boolean shouldTryTeleportToOwner() {
        LivingEntity livingentity = self().getOwner();
        return livingentity != null && self().distanceToSqr(self().getOwner()) >= (double)144.0F;
    }

    default void tryToTeleportToOwner() {
        LivingEntity livingentity = self().getOwner();
        if (livingentity != null) {
            this.teleportToAroundBlockPos(livingentity.blockPosition());
        }

    }

    default void teleportToAroundBlockPos(BlockPos arg) {
        for(int i = 0; i < 10; ++i) {
            int j = self().getRandom().nextIntBetweenInclusive(-3, 3);
            int k = self().getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = self().getRandom().nextIntBetweenInclusive(-1, 1);
                if (this.maybeTeleportTo(arg.getX() + j, arg.getY() + l, arg.getZ() + k)) {
                    return;
                }
            }
        }

    }

    default boolean maybeTeleportTo(int i, int j, int k) {
        if (!this.canTeleportTo(new BlockPos(i, j, k))) {
            return false;
        } else {
            self().moveTo((double)i + (double)0.5F, j, (double)k + (double)0.5F, self().getYRot(), self().getXRot());
            self().getNavigation().stop();
            return true;
        }
    }

    default boolean canTeleportTo(BlockPos arg) {
        BlockPathTypes pathtype = WalkNodeEvaluator.getBlockPathTypeStatic(self().level(), arg.mutable());
        if (pathtype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = self().level().getBlockState(arg.below());
            if (!this.canFlyToOwner() && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = arg.subtract(self().blockPosition());
                return self().level().noCollision(self(), self().getBoundingBox().move(blockpos));
            }
        }
    }

    default boolean canFlyToOwner() {
        return false;
    }

    default boolean unableToMoveToOwner() {
        return self().isOrderedToSit() || self().isPassenger() || self().isLeashed();
    }
}
