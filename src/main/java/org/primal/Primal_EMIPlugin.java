package org.primal;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.registry.EmiTags;
import dev.emi.emi.runtime.EmiReloadLog;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.primal.item.HelmetDecoration;
import org.primal.item.component.HelmetDecorationComponent;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@EmiEntrypoint
public class Primal_EMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        Set<Item> hiddenItems = Stream.concat(
                EmiUtil.values(TagKey.create(EmiPort.getItemRegistry().key(), EmiTags.HIDDEN_FROM_RECIPE_VIEWERS)).map(Holder::value),
                EmiPort.getDisabledItems()
        ).collect(Collectors.toSet());

        for (Item i : EmiPort.getItemRegistry()) {
            if (hiddenItems.contains(i)) {
                continue;
            }

            if(i instanceof ArmorItem armorItem && armorItem.getType().equals(ArmorItem.Type.HELMET)){
                for (var helmetDecorationEntry : Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getEntries()){
                    if(!helmetDecorationEntry.getValue().getAsItem().equals(ItemStack.EMPTY.getItem())){
                        addRecipeSafe(registry, () -> createHelmetDecorationRecipe(registry, i, helmetDecorationEntry.getValue(), true));
                        addRecipeSafe(registry, () -> createHelmetDecorationRecipe(registry, i, helmetDecorationEntry.getValue(), false));
                    }
                }
            }
        }
    }

    private static EmiHelmetDecorationRecipe createHelmetDecorationRecipe(EmiRegistry registry, Item helmet, HelmetDecoration<?> helmetDecoration, boolean left){
        var decorKey= Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getKey(helmetDecoration);
        var helmetKey= BuiltInRegistries.ITEM.getKey(helmet);

        assert decorKey != null;
        var recipeId = synthetic(
                decorKey.getNamespace(),
                "anvil/helmet_decoration",
                decorKey.getPath()+"/" + (left? "left/": "right/") +  helmetKey.getNamespace() +"/"+ helmetKey.getPath());

        return new EmiHelmetDecorationRecipe(helmet, helmetDecoration,
                left,
                recipeId);
    }

    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
        try {
            registry.addRecipe(supplier.get());
        } catch (Throwable e) {
            EmiReloadLog.warn("Exception thrown when parsing EMI recipe (no ID available)", e);
        }
    }

    private static ResourceLocation synthetic(String modid, String type, String name) {
        return ResourceLocation.fromNamespaceAndPath(modid, "/" + type + "/" + name);
    }

    public static class EmiHelmetDecorationRecipe implements EmiRecipe {
        private final Item baseHelmet;
        private final HelmetDecoration<?> helmetDecoration;
        private final ResourceLocation id;
        private final boolean left;

        public EmiHelmetDecorationRecipe(Item baseHelmet, HelmetDecoration<?> helmetDecoration, boolean left, ResourceLocation id) {
            this.baseHelmet = baseHelmet;
            this.helmetDecoration = helmetDecoration;
            this.left = left;
            this.id = id;
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return VanillaEmiRecipeCategories.ANVIL_REPAIRING;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(EmiStack.of(baseHelmet), EmiStack.of(helmetDecoration.getAsItem()));
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(EmiStack.of(baseHelmet));
        }

        @Override
        public boolean supportsRecipeTree() {
            return false;
        }

        @Override
        public int getDisplayWidth() {
            return 125;
        }

        @Override
        public int getDisplayHeight() {
            return 18;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.addTexture(EmiTexture.PLUS, 27, 3);
            widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
            widgets.addSlot(left? EmiStack.of(helmetDecoration.getAsItem()): EmiStack.of(baseHelmet), 0, 0);
            widgets.addSlot(left? EmiStack.of(baseHelmet): EmiStack.of(helmetDecoration.getAsItem()), 49, 0);
            widgets.addSlot(EmiStack.of(getModifiedHelmet()), 107, 0).recipeContext(this);
        }

        private ItemStack getModifiedHelmet() {
            var helmetModified = baseHelmet.getDefaultInstance().copy();
            HelmetDecorationComponent.of(helmetModified, helmetDecoration.getAsItem().getDefaultInstance(), left);

            return helmetModified;
        }
    }
}