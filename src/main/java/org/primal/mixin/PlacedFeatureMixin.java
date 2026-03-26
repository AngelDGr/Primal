package org.primal.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;
import org.primal.registry.Primal_WorldGen;
import org.primal.util.Primal_Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureMixin {

    @Shadow public abstract Holder<ConfiguredFeature<?, ?>> feature();

    @Unique
    PlacedFeature p$THIS = (PlacedFeature)(Object)this;

    //To cache the instances instead of doing it everytime
//    @Unique
//    private PlacedFeature primal$acaciaTreesInstance=null;
//    @Unique
//    private PlacedFeature primal$thornyAcaciaTreesInstance=null;

//    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
//    private void primal$replacePlacedFeaturesDirectly(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
//        //This replaces vanilla features to a variation from Primal
//        MinecraftServer server = level.getServer();
//
//        if(server==null) return;
//
//        //Thorny Acacia
//        primal$acaciaTreesInstance = primal$setIfNull(server, primal$acaciaTreesInstance, TreePlacements.ACACIA_CHECKED);
//        primal$thornyAcaciaTreesInstance = primal$setIfNull(server, primal$thornyAcaciaTreesInstance, Primal_WorldGen.PlacedFeatures.THORNY_ACACIA);
//        if(primal$featureReplacingByBiome(level, generator, random, pos,
//                Primal_Main.COMMON_CONFIG.thornyAcaciaSpawnInWorld.get(),
//                Primal_Main.COMMON_CONFIG.thornyAcaciaProbabilityOfReplacing.get(),
//                Primal_Main.COMMON_CONFIG.thornyAcaciaExtraBiomes.get().stream().map(Object::toString).toList(),
//                Primal_Tags.Biome.SPAWNS_THORNY_ACACIA,
//                primal$acaciaTreesInstance, primal$thornyAcaciaTreesInstance))
//            cir.setReturnValue(true);
//    }

    @Unique
    private boolean primal$featureReplacingByBiome(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos,
                                                   boolean isEnabled,
                                                   double probability,
                                                   List<String> extraBiomes,
                                                   TagKey<Biome> biomeTag,
                                                   PlacedFeature featureToReplace,
                                                   PlacedFeature replacingFeature){
        if (isEnabled) {
            var biome = level.getBiome(pos);

            boolean matchesExtra = Primal_Util.Generation.biomeMatchesWithConfigList(extraBiomes, biome);

            if ((biome.is(biomeTag) || matchesExtra) && featureToReplace.equals(p$THIS) && level.getRandom().nextFloat() < probability) {
                return replacingFeature.place(level, generator, random, pos);
            }
        }

        return false;
    }

    @Unique
    private @NotNull PlacedFeature primal$setIfNull(@NotNull MinecraftServer server, @Nullable PlacedFeature cachedFeature, ResourceKey<PlacedFeature> featureKey){
        if(cachedFeature==null)
            return server.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolderOrThrow(featureKey).value();

        return cachedFeature;
    }
}
