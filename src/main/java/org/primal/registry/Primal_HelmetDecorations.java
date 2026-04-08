package org.primal.registry;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Registries;
import org.primal.item.HelmetDecoration;

import java.util.function.Supplier;

public class Primal_HelmetDecorations {

    public static RegistryObject<HelmetDecoration<?>> NONE;

    public static RegistryObject<HelmetDecoration<?>> FALLOW_DEER;
    public static RegistryObject<HelmetDecoration<?>> REINDEER;
    public static RegistryObject<HelmetDecoration<?>> WHITETAIL;
    public static RegistryObject<HelmetDecoration<?>> GOAT;

    /**
     * @see org.primal.datagen.providers.Primal_HelmetDecorationModelGenerator
     */
    public static void init(){
        NONE =register("none", ()-> new HelmetDecoration<>(ItemStack.EMPTY::getItem, Style.EMPTY));

        FALLOW_DEER = register("fallow_antler", () ->
                new HelmetDecoration<>(
                        Primal_Items.FALLOW_DEER_ANTLER,
                        Style.EMPTY.withColor(0xa5857d)));

        REINDEER = register("reindeer_antler", () ->
                new HelmetDecoration<>(
                        Primal_Items.REINDEER_ANTLER,
                        Style.EMPTY.withColor(0xd2dbee)));

        WHITETAIL = register("whitetail_antler", () ->
                new HelmetDecoration<>(
                        Primal_Items.WHITETAIL_DEER_ANTLER,
                        Style.EMPTY.withColor(0x986e47)));

        GOAT = register("goat_horn", () ->
                new HelmetDecoration<>(
                        ()-> Items.GOAT_HORN,
                        Style.EMPTY.withColor(0x575757)));
    }

    public static RegistryObject<HelmetDecoration<?>> register(final String name, final Supplier<HelmetDecoration<?>> helmetDecoration) {
        return Primal_Registries.HELMET_DECORATIONS.register(name, helmetDecoration);
    }
}
