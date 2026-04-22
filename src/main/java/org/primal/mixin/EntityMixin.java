package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.animal.WolfVariants;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.animal.SnakeEntity;
import org.primal.entity.replaced.DolphinReplaced;
import org.primal.registry.Primal_Items;
import org.primal.server_data.ConchShellsData;
import org.primal.util.mob_types.HostileMount;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;
import org.primal.util.mob_types.SemiAquaticAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    Entity p$THIS = (Entity)(Object)this;

    @Shadow public abstract @Nullable Entity getVehicle();

    @Shadow public abstract EntityType<?> getType();

    @Shadow public abstract RegistryAccess registryAccess();

    @ModifyReturnValue(method = "isShiftKeyDown", at = @At("RETURN"))
    private boolean primal$cantShiftIfHostileMount(boolean original) {
        if(this.getVehicle()!=null && this.getVehicle() instanceof HostileMount && !(p$THIS instanceof Player player && player.isCreative()))
            return false;

        return original;
    }

    @ModifyReturnValue(method = "canRide", at = @At("RETURN"))
    private boolean primal$shiftCantPreventRidingIfHostileMount(boolean original, @Local(argsOnly = true) Entity vehicle) {
        if(vehicle instanceof HostileMount)
            return true;

        return original;
    }

    @ModifyReturnValue(method = "getMaxAirSupply", at = @At("RETURN"))
    public int primal$improvePolarBearRespiration(int original) {
        //2 min
        if(p$THIS.getType()!=null && p$THIS.getType().equals(EntityType.POLAR_BEAR))
            return 2400;

        return original;
    }

    @ModifyExpressionValue(method = "collide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;onGround()Z"))
    public boolean primal$improveStepUpFromWaterToGround(boolean original) {
        if(p$THIS.getType()!=null && p$THIS.getType().equals(EntityType.POLAR_BEAR) || p$THIS instanceof SemiAquaticAnimal)
            return original || p$THIS.isInWater();

        return original;
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addDuringTeleport(Lnet/minecraft/world/entity/Entity;)V"))
    private void primal$petInConchShellTeleports(ServerLevel serverLevel,
                                               double x, double y, double z,
                                               Set<RelativeMovement> relativeMovements,
                                               float yRot, float xRot,
                                               CallbackInfoReturnable<Boolean> cir, @Local Entity newEntity) {

        var data = ConchShellsData.get(serverLevel);
        if(p$THIS.getServer() == null) return;

        if(data.hasPet(p$THIS)){
            data.removePet(p$THIS);
            data.addPet(newEntity);
        }
    }

    @Inject(method = "changeDimension", at = @At(value = "RETURN", ordinal = 1))
    private void primal$petInConchShellChangesDimension(DimensionTransition transition, CallbackInfoReturnable<Entity> cir, @Local Entity newEntity) {
        if(p$THIS.level() instanceof ServerLevel serverLevel){
            var data = ConchShellsData.get(serverLevel);
            if(p$THIS.getServer() == null) return;

            if(data.hasPet(p$THIS)){
                data.removePet(p$THIS);
                data.addPet(newEntity);
            }
        }
    }

    @Inject(method = "setPosRaw", at = @At("TAIL"))
    private void primal$setSnakePartsPositions(double x, double y, double z, CallbackInfo ci) {
        if (p$THIS instanceof SnakeEntity snake && snake.parts!=null){
            snake.parts.positionParts();
        }
    }

    @Inject(method = "thunderHit", at = @At(value = "HEAD"), cancellable = true)
    private void primal$addDiscLightingImmunity(ServerLevel level, LightningBolt lightning, CallbackInfo ci) {
        if(p$THIS instanceof ItemEntity item && item.getItem().is(Primal_Items.MUSIC_DISC_OH_DEER)){
            ci.cancel();
        }
    }


    @ModifyReturnValue(method = "getTypeName", at = @At("TAIL"))
    private Component primal$setCustomVariantName(Component original) {
        if(p$THIS instanceof Dolphin && Primal_Main.COMMON_CONFIG.dolphinModelChange.get()){
             return primal$getCustomVariantNameV(
                     (Dolphin & VariantHolder<DolphinReplaced.Variant>) p$THIS,
                     original,
                     DolphinReplaced.Variant.COLD);
        }

        if(p$THIS instanceof Wolf wolf && Primal_Main.COMMON_CONFIG.wolfModelChange.get()){
            return primal$getCustomVariantNameForWolf(
                    wolf,
                    original,
                    WolfVariants.SPOTTED,
                    WolfVariants.RUSTY);
        }

        return original;
    }

    @Unique
    @SafeVarargs
    private static<T extends StringRepresentable, E extends Entity & VariantHolder<T>> Component primal$getCustomVariantNameV(E entity,
                                                                                                                              Component defaultName,
                                                                                                                              T... variants){
        if(entity.hasCustomName()) return defaultName;

        var location = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        for(T variant : variants){
            if(entity.getVariant().equals(variant)){
                //entity.primal.bear.[variant]
                return Component.translatable("entity."+location.getNamespace()+ "."+ location.getPath() +"."+variant.getSerializedName());
            }
        }

        return defaultName;
    }

    /**
     * Set a custom name for the specified new primal variants
     */
    @Unique
    @SafeVarargs
    private static<T extends StringRepresentable, E extends Entity & ReplacedEntityNewVariantHolder<T>> Component primal$getCustomVariantName(E entity,
                                                                                                                                          Component defaultName,
                                                                                                                                          T... variants){
        if(entity.hasCustomName()) return defaultName;

        var location = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        for(T variant : variants){
            if(entity.primal$getVariant().equals(variant)){
                //entity.primal.bear.[variant]
                return Component.translatable("entity."+location.getNamespace()+ "."+ location.getPath() +"."+variant.getSerializedName());
            }
        }

        return defaultName;
    }

    /**
     * Set a custom name for the specified wolf variants
     */
    @Unique
    @SafeVarargs
    private static Component primal$getCustomVariantNameForWolf(Wolf entity,
                                                                Component defaultName,
                                                                ResourceKey<WolfVariant>... variantKeys){
        if(entity.hasCustomName()) return defaultName;

        var registry = entity.registryAccess().registryOrThrow(Registries.WOLF_VARIANT);
        var location = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        for (ResourceKey<WolfVariant> key : variantKeys) {
            Optional<Holder.Reference<WolfVariant>> holder = registry.getHolder(key);

            if (holder.isPresent() && entity.getVariant().equals(holder.get())) {
                ResourceLocation nameRegistered = ResourceLocation.tryParse(holder.get().getRegisteredName());
                if(nameRegistered!=null){
                    return Component.translatable(
                            "entity." + location.getNamespace() + "." + location.getPath() + "." + nameRegistered.getPath()
                    );
                }
            }
        }

        return defaultName;
    }
}
