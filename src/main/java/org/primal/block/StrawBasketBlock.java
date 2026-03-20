package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.block_entity.StrawBasketBlockEntity;
import org.primal.registry.Primal_BlockEntities;

import java.util.Optional;
import java.util.function.Supplier;

public class StrawBasketBlock extends ChestBlock {
    public StrawBasketBlock(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new StrawBasketBlockEntity(Primal_BlockEntities.STRAW_BASKET.get(), pos, state);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return this.combine(state, level, pos, false).apply(MENU_PROVIDER_COMBINER).orElse(null);
    }

    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<>() {
        public @NotNull Optional<MenuProvider> acceptDouble(final @NotNull ChestBlockEntity first, final @NotNull ChestBlockEntity second) {
            final Container container = new CompoundContainer(first, second);
            return Optional.of(new MenuProvider() {
                @javax.annotation.Nullable
                @Override
                public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
                    if (first.canOpen(player) && second.canOpen(player)) {
                        first.unpackLootTable(inventory.player);
                        second.unpackLootTable(inventory.player);
                        return ChestMenu.sixRows(containerId, inventory, container);
                    } else {
                        return null;
                    }
                }

                @Override
                public @NotNull Component getDisplayName() {
                    if (first.hasCustomName()) {
                        return first.getDisplayName();
                    } else {
                        return second.hasCustomName() ? second.getDisplayName() : Component.translatable("container.primal.strawBasketDouble");
                    }
                }
            });
        }

        public @NotNull Optional<MenuProvider> acceptSingle(@NotNull ChestBlockEntity chestBlockEntity) {
            return Optional.of(chestBlockEntity);
        }

        public @NotNull Optional<MenuProvider> acceptNone() {
            return Optional.empty();
        }
    };
}
