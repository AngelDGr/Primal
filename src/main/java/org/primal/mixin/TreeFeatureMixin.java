package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Main;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Sounds;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;
import org.primal.worldgen.feature.AlterLeavesDecorator;
import org.primal.worldgen.feature.AlterTrunkDecorator;
import org.primal.worldgen.feature.HollowLogTreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Mixin(TreeFeature.class)
public class TreeFeatureMixin {

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 0))
    private void primal$replaceAcaciaWithThornyAcacia(FeaturePlaceContext<TreeConfiguration> context, CallbackInfoReturnable<Boolean> cir,
                                                      @Local TreeConfiguration treeconfiguration,
                                                      @Local WorldGenLevel worldgenlevel,
                                                      @Local(ordinal = 2) BiConsumer<BlockPos, BlockState> biConsumer2,
                                                      @Local RandomSource randomsource,
                                                      @Local(ordinal = 1) Set<BlockPos> set1,
                                                      @Local(ordinal = 2) Set<BlockPos> set2,
                                                      @Local(ordinal = 0) Set<BlockPos> set){

        if(Primal_Main.COMMON_CONFIG.thornyAcaciaSpawnInWorld.get() && context.level().getRandom().nextFloat() < Primal_Main.COMMON_CONFIG.thornyAcaciaProbabilityOfReplacing.get()){

            Holder<Biome> biome = context.level().getBiome(context.origin());

            boolean matchesExtra =
                    Primal_Util.Generation.biomeMatchesWithConfigList(Primal_Main.COMMON_CONFIG.thornyAcaciaExtraBiomes.get().stream().map(Object::toString).toList(), biome);

            if(biome.is(Primal_Tags.Biome.SPAWNS_THORNY_ACACIA) || matchesExtra){
                TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biConsumer2, randomsource, set1, set2, set);

                boolean wasTree = false;

                //Leaves
                if (treeconfiguration.foliageProvider instanceof SimpleStateProvider simpleStateProvider) {
                    BlockState leavesState = simpleStateProvider.getState(context.random(), context.origin());
                    Block leaves = leavesState.getBlock();

                    if(leaves.equals(Blocks.ACACIA_LEAVES)){
                        new AlterLeavesDecorator(
                                1,
                                BlockStateProvider.simple(Primal_Blocks.THORNY_ACACIA_LEAVES.get()),
                                false,
                                Integer.MAX_VALUE)
                                .place(treedecorator$context);
                    }
                }

                //Trunk
                if (treeconfiguration.trunkProvider instanceof SimpleStateProvider simpleStateProvider) {
                    BlockState logState = simpleStateProvider.getState(context.random(), context.origin());
                    Block log = logState.getBlock();

                    if(log.equals(Blocks.ACACIA_LOG)){
                        new AlterTrunkDecorator(
                                1,
                                BlockStateProvider.simple(Primal_Blocks.THORNY_ACACIA_LOG.get().defaultBlockState()),
                                false,
                                BlockStateProvider.simple(Blocks.ACACIA_LOG),
                                Integer.MAX_VALUE, true)
                                .place(treedecorator$context);
                        wasTree=true;
                    }

                    if(log.equals(Blocks.ACACIA_WOOD)){
                        new AlterTrunkDecorator(
                                1,
                                BlockStateProvider.simple(Primal_Blocks.THORNY_ACACIA_WOOD.get()),
                                false,
                                BlockStateProvider.simple(Blocks.ACACIA_WOOD),
                                Integer.MAX_VALUE)
                                .place(treedecorator$context);
                        wasTree=true;
                    }

                    //Dirt
                    if(wasTree)
                        new AlterGroundDecorator(BlockStateProvider.simple(Blocks.COARSE_DIRT))
                                .place(treedecorator$context);
                }
            }
        }
    }


    @Unique
    private static Map<Block, RegistryObject<Block>> PRIMAL$MAPPED_LOGS;
    @Unique
    private static Map<Block, Boolean> PRIMAL$MAPPED_LOG_SPAWN;

    //Empty
    @Unique
    private static Map<Block, Double> PRIMAL$MAPPED_LOG_EMPTY_PROBABILITY;
    @Unique
    private static final Map<Block, UniformInt> PRIMAL$MAPPED_LOG_EMPTY_AMOUNT =
            Map.of(
                    Blocks.OAK_LOG, UniformInt.of(1, 1),
                    Blocks.SPRUCE_LOG, UniformInt.of(1, 2),
                    Blocks.BIRCH_LOG, UniformInt.of(1, 1),
                    Blocks.JUNGLE_LOG, UniformInt.of(1, 4),
                    Blocks.ACACIA_LOG, UniformInt.of(1, 1),
                    Blocks.DARK_OAK_LOG, UniformInt.of(1, 4),
                    Blocks.MANGROVE_LOG, UniformInt.of(1, 2)
            );
    @Unique
    private static final Map<Block, Float> PRIMAL$MAPPED_LOG_SUBSEQUENT_PROBABILITY =
            Map.of(
                    Blocks.OAK_LOG, 0.0f,
                    Blocks.SPRUCE_LOG, 0.1f,
                    Blocks.BIRCH_LOG, 0.0f,
                    Blocks.JUNGLE_LOG, 0.3f,
                    Blocks.ACACIA_LOG, 0.0f,
                    Blocks.DARK_OAK_LOG, 0.4f,
                    Blocks.MANGROVE_LOG, 0.25f
            );

    //Snake
    @Unique
    private static Map<Block, Double> PRIMAL$MAPPED_LOG_SNAKE_PROBABILITY;
    @Unique
    private static final Map<Block, UniformInt> PRIMAL$MAPPED_LOG_SNAKE_AMOUNT =
            Map.of(
                    Blocks.OAK_LOG, UniformInt.of(1, 2),
                    Blocks.SPRUCE_LOG, UniformInt.of(1, 2),
                    Blocks.BIRCH_LOG, UniformInt.of(1, 2),
                    Blocks.JUNGLE_LOG, UniformInt.of(1, 1),
                    Blocks.ACACIA_LOG, UniformInt.of(1, 1),
                    Blocks.DARK_OAK_LOG, UniformInt.of(1, 1),
                    Blocks.MANGROVE_LOG, UniformInt.of(1, 2)
            );

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 0))
    private void primal$replaceWithLogsWithHollowLogs(FeaturePlaceContext<TreeConfiguration> context, CallbackInfoReturnable<Boolean> cir,
                                                      @Local TreeConfiguration treeconfiguration,
                                                      @Local WorldGenLevel worldgenlevel,
                                                      @Local(ordinal = 2) BiConsumer<BlockPos, BlockState> biConsumer2,
                                                      @Local RandomSource randomsource,
                                                      @Local(ordinal = 1) Set<BlockPos> set1,
                                                      @Local(ordinal = 2) Set<BlockPos> set2,
                                                      @Local(ordinal = 0) Set<BlockPos> set){
        Holder<Biome> biome = context.level().getBiome(context.origin());
        boolean matchesExtra =
                Primal_Util.Generation.biomeMatchesWithConfigList(Primal_Main.COMMON_CONFIG.hollowTreesExtraBiomes.get().stream().map(Object::toString).toList(), biome);

        if (treeconfiguration.trunkProvider instanceof SimpleStateProvider simpleStateProvider) {
            Block log = simpleStateProvider.getState(context.random(), context.origin()).getBlock();

            RegistryObject<Block> hollowLog = primal$getMappedLogs().get(log);
            Boolean logSpawnInWorld = primal$getMappedLogSpawn().get(log);

            if(hollowLog != null && logSpawnInWorld!=null && logSpawnInWorld && (biome.is(Primal_Tags.Biome.SPAWNS_HOLLOW_TREE) || matchesExtra)){
                TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biConsumer2, randomsource, set1, set2, set);
                boolean hollowGenerated=false;

                //Empty
                if(context.random().nextFloat()<=primal$getMappedEmptyProbabilities().get(log)){
                    new HollowLogTreeDecorator(
                            ResourceLocation.withDefaultNamespace(""),
                            ResourceLocation.withDefaultNamespace(""),
                            hollowLog.get(),
                            Map.of(),
                            UniformInt.of(0,0),
                            PRIMAL$MAPPED_LOG_SUBSEQUENT_PROBABILITY.get(log),
                            randomsource.nextIntBetweenInclusive(PRIMAL$MAPPED_LOG_EMPTY_AMOUNT.get(log).getMinValue(), PRIMAL$MAPPED_LOG_EMPTY_AMOUNT.get(log).getMaxValue()),
                            0,
                            Integer.MAX_VALUE)
                            .place(treedecorator$context);

                    hollowGenerated=true;
                }

                //Snake
                if (context.random().nextFloat()<=primal$getMappedSnakeProbabilities().get(log) && !hollowGenerated) {
                    new HollowLogTreeDecorator(
                            Primal_Entities.SNAKE.getId(),
                            Primal_Sounds.SNAKE_SNORING.getId(),
                            hollowLog.get(),
                            SnakeEntity.Variant.VARIANT_IDS_FOR_LOG,
                            PRIMAL$MAPPED_LOG_SNAKE_AMOUNT.get(log),
                            0,
                            1,
                            0,
                            1)
                            .place(treedecorator$context);

                    hollowGenerated=true;
                }
            }
        }
    }

    //Lazy initialization
    @Unique
    private static Map<Block, RegistryObject<Block>> primal$getMappedLogs() {
        if (PRIMAL$MAPPED_LOGS == null) {
            PRIMAL$MAPPED_LOGS = Map.of(
                    Blocks.OAK_LOG, Primal_Blocks.HOLLOW_OAK_LOG,
                    Blocks.SPRUCE_LOG, Primal_Blocks.HOLLOW_SPRUCE_LOG,
                    Blocks.BIRCH_LOG, Primal_Blocks.HOLLOW_BIRCH_LOG,
                    Blocks.JUNGLE_LOG, Primal_Blocks.HOLLOW_JUNGLE_LOG,
                    Blocks.ACACIA_LOG, Primal_Blocks.HOLLOW_ACACIA_LOG,
                    Blocks.DARK_OAK_LOG, Primal_Blocks.HOLLOW_DARK_OAK_LOG,
                    Blocks.MANGROVE_LOG, Primal_Blocks.HOLLOW_MANGROVE_LOG
            );
        }

        return PRIMAL$MAPPED_LOGS;
    }

    @Unique
    private static Map<Block, Boolean> primal$getMappedLogSpawn() {
        if (PRIMAL$MAPPED_LOG_SPAWN == null) {
            PRIMAL$MAPPED_LOG_SPAWN = Map.of(
                    Blocks.OAK_LOG, Primal_Main.COMMON_CONFIG.hollowOakLogSpawnInWorld.get(),
                    Blocks.SPRUCE_LOG, Primal_Main.COMMON_CONFIG.hollowSpruceLogSpawnInWorld.get(),
                    Blocks.BIRCH_LOG, Primal_Main.COMMON_CONFIG.hollowBirchLogSpawnInWorld.get(),
                    Blocks.JUNGLE_LOG, Primal_Main.COMMON_CONFIG.hollowJungleLogSpawnInWorld.get(),
                    Blocks.ACACIA_LOG, Primal_Main.COMMON_CONFIG.hollowAcaciaLogSpawnInWorld.get(),
                    Blocks.DARK_OAK_LOG, Primal_Main.COMMON_CONFIG.hollowDarkOakLogSpawnInWorld.get(),
                    Blocks.MANGROVE_LOG, Primal_Main.COMMON_CONFIG.hollowMangroveLogSpawnInWorld.get()
            );
        }

        return PRIMAL$MAPPED_LOG_SPAWN;
    }

    @Unique
    private static Map<Block, Double> primal$getMappedEmptyProbabilities() {
        if (PRIMAL$MAPPED_LOG_EMPTY_PROBABILITY == null) {
            List<? extends Double> values = Primal_Main.COMMON_CONFIG.hollowLogEmptyProbability.get();

            PRIMAL$MAPPED_LOG_EMPTY_PROBABILITY = Map.of(
                    Blocks.OAK_LOG, primal$getProbability(values, 0),
                    Blocks.SPRUCE_LOG, primal$getProbability(values, 1),
                    Blocks.BIRCH_LOG, primal$getProbability(values, 2),
                    Blocks.JUNGLE_LOG, primal$getProbability(values, 3),
                    Blocks.ACACIA_LOG, primal$getProbability(values, 4),
                    Blocks.DARK_OAK_LOG, primal$getProbability(values, 5),
                    Blocks.MANGROVE_LOG, primal$getProbability(values, 6)
            );
        }

        return PRIMAL$MAPPED_LOG_EMPTY_PROBABILITY;
    }

    @Unique
    private static Map<Block, Double> primal$getMappedSnakeProbabilities() {
        if (PRIMAL$MAPPED_LOG_SNAKE_PROBABILITY == null) {
            List<? extends Double> values = Primal_Main.COMMON_CONFIG.hollowLogSnakeProbability.get();

            PRIMAL$MAPPED_LOG_SNAKE_PROBABILITY = Map.of(
                    Blocks.OAK_LOG, primal$getProbability(values, 0),
                    Blocks.SPRUCE_LOG, primal$getProbability(values, 1),
                    Blocks.BIRCH_LOG, primal$getProbability(values, 2),
                    Blocks.JUNGLE_LOG, primal$getProbability(values, 3),
                    Blocks.ACACIA_LOG, primal$getProbability(values, 4),
                    Blocks.DARK_OAK_LOG, primal$getProbability(values, 5),
                    Blocks.MANGROVE_LOG, primal$getProbability(values, 6)
            );
        }

        return PRIMAL$MAPPED_LOG_SNAKE_PROBABILITY;
    }

    @Unique
    private static double primal$getProbability(List<? extends Double> list, int index) {
        if (list != null && index >= 0 && index < list.size() && list.get(index) != null) {
            return list.get(index);
        }
        return 0.0D;
    }
}
