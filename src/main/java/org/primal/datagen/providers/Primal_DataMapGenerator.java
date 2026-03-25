package org.primal.datagen.providers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.primal.registry.Primal_Items;

public class Primal_DataMapGenerator {

    public static void setSimilarToDataMaps() {
        var compostables= ComposterBlock.COMPOSTABLES;

        compostables.put(Primal_Items.WARM_SEASHELLS.get(), 0.3f);
        compostables.put(Primal_Items.COLD_SEASHELLS.get(), 0.3f);
        compostables.put(Primal_Items.TEMPERATE_SEASHELLS.get(), 0.3f);
        compostables.put(Primal_Items.SHORT_RIVER_REEDS.get(), 0.3f);
        compostables.put(Primal_Items.RIVER_REEDS.get(), 0.5f);
        compostables.put(Primal_Items.CATTAILS.get(), 0.5f);
        compostables.put(Primal_Items.THORNY_ACACIA_LEAVES.get(), 0.3f);
        compostables.put(Primal_Items.EXPLOSEED.get(), 0.65f);
        compostables.put(Primal_Items.STRAW_BLOCK.get(), 0.85f);
        compostables.put(Primal_Items.DRIED_STRAW_BLOCK.get(), 0.85f);
        compostables.put(Primal_Items.WEAVED_STRAW.get(), 0.85f);
        compostables.put(Primal_Items.WEAVED_STRAW_STAIRS.get(), 0.35f);
        compostables.put(Primal_Items.WEAVED_STRAW_SLAB.get(), 0.35f);
        compostables.put(Primal_Items.STRAW_BASKET.get(), 0.85f);

        MinecraftForge.EVENT_BUS.addListener((FurnaceFuelBurnTimeEvent event) -> {
            ItemStack stack = event.getItemStack();

            if (stack.is(Primal_Items.NEST.get())) event.setBurnTime(100);
            if (stack.is(Primal_Items.STRAW_BLOCK.get())) event.setBurnTime(800);
            if (stack.is(Primal_Items.DRIED_STRAW_BLOCK.get())) event.setBurnTime(1600);
            if (stack.is(Primal_Items.WEAVED_STRAW.get())) event.setBurnTime(600);
            if (stack.is(Primal_Items.WEAVED_STRAW_STAIRS.get())) event.setBurnTime(300);
            if (stack.is(Primal_Items.WEAVED_STRAW_SLAB.get())) event.setBurnTime(300);
            if (stack.is(Primal_Items.STRAW_BASKET.get())) event.setBurnTime(600);
        });
    }
}
