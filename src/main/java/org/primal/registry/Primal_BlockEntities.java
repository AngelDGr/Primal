package org.primal.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;
import org.primal.block_entity.NestBlockEntity;

public class Primal_BlockEntities {

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<NestBlockEntity>> NEST_BLOCK_ENTITY;

    public static void init() {

        NEST_BLOCK_ENTITY = Primal_Registries.BLOCK_ENTITIES.register(
                "nest",
                ()-> BlockEntityType.Builder.of(
                        (pos, state) -> new NestBlockEntity(NEST_BLOCK_ENTITY.get(), pos, state),
                        Primal_Blocks.NEST_BLOCK.get()).build(null));
    }

}
