package org.primal.mixin.replaced;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.primal.entity.replaced.DolphinReplaced;
import org.primal.registry.Primal_Tags;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dolphin.class)
public abstract class DolphinMixin extends WaterAnimal implements ReplacedEntityNewVariantHolder<DolphinReplaced.Variant> {
    protected DolphinMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static final EntityDataAccessor<Integer> PRIMAL$DATA_VARIANT_ID = SynchedEntityData.defineId(DolphinMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void primal$dolphinVariantSynchedData(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(PRIMAL$DATA_VARIANT_ID, DolphinReplaced.Variant.LUKEWARM.getId());
    }

    @Unique
    Dolphin p$THIS = (Dolphin)(Object)this;

    @SuppressWarnings("unchecked")
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void primal$dolphinFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir){
        this.primal$setVariantFromBiome((ReplacedEntityNewVariantHolder<DolphinReplaced.Variant>) p$THIS, level.getBiome(this.blockPosition()));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void primal$dolphinAddAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("PrimalVariant", this.primal$getVariant().getId());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void primal$dolphinReadAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.primal$setVariant(DolphinReplaced.Variant.byId(compound.getInt("PrimalVariant")));
    }

    @Override
    public <M extends ReplacedEntityNewVariantHolder<DolphinReplaced.Variant>> void primal$setVariantFromBiome(M animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_DOLPHIN_WARM))
            animal.primal$setVariant(DolphinReplaced.Variant.WARM);
        else if(holder.is(Primal_Tags.Biome.SPAWNS_DOLPHIN_COLD))
            animal.primal$setVariant(DolphinReplaced.Variant.COLD);
        else
            animal.primal$setVariant(DolphinReplaced.Variant.LUKEWARM);
    }

    @Override
    public void primal$setVariant(DolphinReplaced.Variant variant) {
        this.entityData.set(PRIMAL$DATA_VARIANT_ID, variant.getId());
    }

    @Override
    public DolphinReplaced.Variant primal$getVariant() {
        return DolphinReplaced.Variant.byId(this.entityData.get(PRIMAL$DATA_VARIANT_ID));
    }
}