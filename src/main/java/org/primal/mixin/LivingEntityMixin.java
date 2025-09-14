package org.primal.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.primal.entity.animal.EagleEntity;
import org.primal.injection.IsEagleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension, IsEagleTarget {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    LivingEntity primal$THIS = (LivingEntity)(Object)this;

    @Unique
    private static final EntityDataAccessor<Optional<UUID>> primal$eagleAttacking = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.OPTIONAL_UUID);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectToxicityDataTracker(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(primal$eagleAttacking, Optional.empty());
    }

    @Override
    public Optional<UUID> primal$eagleAttacking() {
        return this.entityData.get(primal$eagleAttacking);
    }

    @Override
    public void primal$setEagleAttacking(@Nullable UUID eagleUUID) {
        if(eagleUUID==null){
            this.entityData.set(primal$eagleAttacking, Optional.empty());
        } else {
            this.entityData.set(primal$eagleAttacking, Optional.of(eagleUUID));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void primal$test(CallbackInfo ci) {
        if(primal$THIS.primal$eagleAttacking().isPresent()
                && !this.level().isClientSide
                //Erase the eagle value if its null or the eagle is dead
                && (((ServerLevel)this.level()).getEntity(primal$THIS.primal$eagleAttacking().get())==null
                || ((ServerLevel)this.level()).getEntity(primal$THIS.primal$eagleAttacking().get()) instanceof LivingEntity eagle && eagle.isDeadOrDying())
        ){
            primal$THIS.primal$setEagleAttacking(null);
        }
    }
}
