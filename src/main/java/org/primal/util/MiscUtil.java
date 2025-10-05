package org.primal.util;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.primal.Primal_Main;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;

import java.util.List;

public class MiscUtil {

    public static final IntegerProperty EGGS_2 = IntegerProperty.create("eggs", 1, 2);
    public static final IntegerProperty EGGS_3 = IntegerProperty.create("eggs", 1, 3);
    public static final IntegerProperty EGGS_4 = IntegerProperty.create("eggs", 1, 4);

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

    /**
     Makes an angle move slowly to the desired angle
     @param current The rotation that you want to move
     @param target The desired rotation
     @param maxChange The maximum rotation that it moves every tick
     @return The new rotation
     */
    public static float smoothAngle(float current, float target, float maxChange) {
        float delta = Mth.wrapDegrees(target - current);
        delta = Mth.clamp(delta, -maxChange, maxChange);

        return current + delta;
    }

    /**
     Detects if an entity is riding an enemy that implements {@link HostileMount}, used for mobs like the Crocodile
     @param entity The rider
     */
    public static boolean isRidingUnfriendly(LivingEntity entity) {
        return entity.isPassenger() && entity.getVehicle() instanceof HostileMount;
    }

    /**
     Extends a loot pool
     */
    public static void extendLootPool(LootPool pool, List<LootPoolEntryContainer> newEntries) {
        var entriesBuilder = ImmutableList.<LootPoolEntryContainer>builder();
        entriesBuilder.addAll(pool.entries);
        entriesBuilder.addAll(newEntries);
        pool.entries = entriesBuilder.build();
    }

    /**
        A simple check to see if the entity is from the tag {@link  Primal_Tags#NEVER_ATTACK}
     */
    public static boolean isNotNeverAttack(LivingEntity entity) {
        return !entity.getType().is(Primal_Tags.NEVER_ATTACK);
    }

    /**
     Simple addition logic, used to count attacks
     */
    public static void addToAttackCount(LivingEntity entity) {
        if(entity.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get())){
            entity.getBrain().setMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(),
                    entity.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).get()+1);
        } else {
            entity.getBrain().setMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(), 1);
        }
    }

    public static boolean isSameEagleAttacking(LivingEntity target, LivingEntity eagle) {
        if(target.primal$eagleAttacking().isPresent()){
            //So babies can have priority over other eagles
            if(eagle.isBaby())
                return true;
            else
                return target.primal$eagleAttacking().get() == eagle.getUUID();
        } else {
            return true;
        }
    }

    /**
     Convenient method to add items to a CreativeTab in order after one item
     */
    public static void insertItemsAfter(BuildCreativeModeTabContentsEvent event, ItemStack after, ItemStack... itemStacks){

        for (int i=0; i<itemStacks.length; i++){
            if(i==0){
                event.insertAfter(after, itemStacks[i], CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            else {
                event.insertAfter(itemStacks[i-1], itemStacks[i], CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

        }
    }


    /**
     * Create a biome modifier with config values, for mobs
     **/
    public static void createBiomeModifier(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder, TagKey<Biome> biomeTagSpawn,
                                           List<String> extraBiomes,
                                           boolean enabled,
                                           int spawnWeight,
                                           int minGroupSize,
                                           int maxGroupSize,
                                           EntityType<?> entityType) {
        if(enabled){
            // Check if biome is in extra biomes
            boolean matchesExtra = extraBiomes.stream()
                    .anyMatch(b -> {
                        ResourceLocation loc = ResourceLocation.tryParse(b);
                        return loc != null && biome.unwrapKey().map(key -> key.location().equals(loc)).orElse(false);
                    });

            if ((biome.is(biomeTagSpawn) || matchesExtra) && spawnWeight > 0 && minGroupSize > 0 && maxGroupSize > 0) {

                builder.getMobSpawnSettings().getSpawner(entityType.getCategory())
                        .add(new MobSpawnSettings.SpawnerData(
                                entityType,
                                spawnWeight,
                                minGroupSize,
                                maxGroupSize));
            }
        }
    }

    /**
     * Create a biome modifier with config values, for features
     **/
    public static<FC extends FeatureConfiguration, F extends Feature<FC>> void createBiomeModifier(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder, TagKey<Biome> biomeTagSpawn,
                                                                                                   List<String> extraBiomes,
                                                                                                   GenerationStep.Decoration step,
                                                                                                   boolean enabled,
                                                                                                   F feature,
                                                                                                   FC config,
                                                                                                   PlacementModifier heightmap,
                                                                                                   int rarity) {
        if(enabled){
            // Check if biome is in extra biomes
            boolean matchesExtra = extraBiomes.stream()
                    .anyMatch(b -> {
                        ResourceLocation loc = ResourceLocation.tryParse(b);
                        return loc != null && biome.unwrapKey().map(key -> key.location().equals(loc)).orElse(false);
                    });

            if (biome.is(biomeTagSpawn) || matchesExtra) {

                // Configured feature
                var configured = new ConfiguredFeature<>(
                        feature,
                        config
                );

                // Build placement modifiers dynamically
                List<PlacementModifier> modifiers = List.of(
                        RarityFilter.onAverageOnceEvery(rarity),
                        InSquarePlacement.spread(),
                        heightmap,
                        BiomeFilter.biome()
                );

                // Wrap into placed feature
                Holder<PlacedFeature> placedFeature = Holder.direct(
                        new PlacedFeature(Holder.direct(configured), modifiers)
                );

                // Add to biome
                builder.getGenerationSettings().addFeature(
                        step,
                        placedFeature
                );
            }
        }
    }

    /**
     * Create a biome modifier serializer, for creation of biome modifiers
     *
     * @param name The name of the biome modifier
     * @return
     */
    public static DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<? extends BiomeModifier>> createBiomeModifierSerializer(String name){
        return DeferredHolder.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    public static ResourceLocation nomanslandLoc(String name){
        return ResourceLocation.fromNamespaceAndPath("nomansland", name);
    }
}
