package org.primal.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Registries;
import org.primal.block_entity.*;

public class Primal_BlockEntities {

    public static RegistryObject<BlockEntityType<NestBlockEntity>> NEST_BLOCK_ENTITY;

    public static RegistryObject<BlockEntityType<Rotation16BlockEntity>> ROTATION_16_BLOCK_ENTITY;

    public static RegistryObject<BlockEntityType<ChompTrapBlockEntity>> CHOMP_TRAP;

    public static RegistryObject<BlockEntityType<HollowLogBlockEntity>> HOLLOW_LOG;

    public static RegistryObject<BlockEntityType<DreamcatcherBlockEntity>> DREAMCATCHER;

    public static RegistryObject<BlockEntityType<StrawBasketBlockEntity>> STRAW_BASKET;

    @SuppressWarnings("all")
    public static void init() {

        NEST_BLOCK_ENTITY = Primal_Registries.BLOCK_ENTITIES.register(
                "nest",
                ()-> BlockEntityType.Builder.of(
                        (pos, state) -> new NestBlockEntity(NEST_BLOCK_ENTITY.get(), pos, state),
                        Primal_Blocks.NEST_BLOCK.get()).build(null));

        ROTATION_16_BLOCK_ENTITY = Primal_Registries.BLOCK_ENTITIES.register(
                "rotation_16_block",
                ()-> BlockEntityType.Builder.of(
                        (pos, state) -> new Rotation16BlockEntity(ROTATION_16_BLOCK_ENTITY.get(), pos, state),
                        Primal_Blocks.PETRIFIED_FRUIT.get()).build(null));

        CHOMP_TRAP = Primal_Registries.BLOCK_ENTITIES.register(
                "chomp_trap",
                ()-> BlockEntityType.Builder.of(
                        (pos, state) -> new ChompTrapBlockEntity(CHOMP_TRAP.get(), pos, state),
                        Primal_Blocks.CHOMP_TRAP_GREEN.get(),
                        Primal_Blocks.CHOMP_TRAP_ARID.get(),
                        Primal_Blocks.CHOMP_TRAP_HUMID.get()).build(null));

        HOLLOW_LOG = Primal_Registries.BLOCK_ENTITIES.register(
                "hollow_log",
                () -> BlockEntityType.Builder.of(
                        (pos, state) -> new HollowLogBlockEntity(HOLLOW_LOG.get(), pos, state),
                        Primal_Blocks.HOLLOW_OAK_LOG.get(),
                        Primal_Blocks.HOLLOW_SPRUCE_LOG.get(),
                        Primal_Blocks.HOLLOW_BIRCH_LOG.get(),
                        Primal_Blocks.HOLLOW_JUNGLE_LOG.get(),
                        Primal_Blocks.HOLLOW_ACACIA_LOG.get(),
                        Primal_Blocks.HOLLOW_DARK_OAK_LOG.get(),
                        Primal_Blocks.HOLLOW_MANGROVE_LOG.get()).build(null));

        DREAMCATCHER = Primal_Registries.BLOCK_ENTITIES.register(
                "dreamcatcher",
                ()-> BlockEntityType.Builder.of(
                        (pos, state) -> new DreamcatcherBlockEntity(DREAMCATCHER.get(), pos, state),
                        Primal_Blocks.DREAMCATCHER.get()).build(null));

        STRAW_BASKET = Primal_Registries.BLOCK_ENTITIES.register(
                "straw_basket",
                ()-> BlockEntityType.Builder.of(
                        (pos, state) -> new StrawBasketBlockEntity(STRAW_BASKET.get(), pos, state),
                        Primal_Blocks.STRAW_BASKET.get()).build(null));
    }

}