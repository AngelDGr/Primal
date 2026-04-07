package org.primal;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.library.plugins.vanilla.anvil.AnvilRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.primal.item.HelmetDecoration;
import org.primal.item.component.HelmetDecorationComponent;
import org.primal.registry.Primal_HelmetDecorations;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JeiPlugin
public class Primal_JEIPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "jei-primal");
    }

    @Override
    public void registerRecipes(final @NotNull IRecipeRegistration registration) {
        final IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();
        IIngredientManager ingredientManager = registration.getIngredientManager();

        registration.addRecipes(RecipeTypes.ANVIL, HelmetDecorationsRecipeMaker.getHelmetDecorationRecipes(vanillaRecipeFactory, ingredientManager));
    }

    private static class HelmetDecorationsRecipeMaker {
        public static List<IJeiAnvilRecipe> getHelmetDecorationRecipes(IVanillaRecipeFactory vanillaRecipeFactory,IIngredientManager ingredientManager){
            var decorations = new HelmetDecorationsData(ingredientManager);
            return decorations.getAllRecipes();
        }

        private static final class HelmetDecorationsData {
            private final List<HelmetDecoration<?>> helmetDecorations = new ArrayList<>();
            private final List<ItemStack> allHelmets = new ArrayList<>();

            private HelmetDecorationsData(IIngredientManager ingredientManager) {
                for(var entry: Primal_Registries.HELMET_DECORATIONS_REGISTRY.entrySet()){
                    if(entry.getValue()!= Primal_HelmetDecorations.NONE.get())
                        helmetDecorations.add(entry.getValue());
                }

                allHelmets.addAll(
                        ingredientManager.getAllItemStacks()
                        .stream()
                        .filter(stack -> stack.is(ItemTags.HEAD_ARMOR))
                        .toList());
            }

            public List<IJeiAnvilRecipe> getAllRecipes() {
                List<IJeiAnvilRecipe> recipes = new ArrayList<>();

                allHelmets.forEach(stack ->
                        helmetDecorations.forEach(type -> {
                            recipes.add(apply(stack, type, false));
                            recipes.add(apply(stack, type, true));
                        })
                );

                return recipes;
            }

            private AnvilRecipe apply(ItemStack helmet, HelmetDecoration<?> helmetDecoration, boolean left) {
                var decorKey= Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(helmetDecoration);

                var recipeId = ResourceLocation.fromNamespaceAndPath(
                        decorKey.getNamespace(),
                        decorKey.getPath()+"_attach_" + (left? "left": "right"));

                List<ItemStack> toAttachList = new ArrayList<>();

                //To put all goat horns, regardless of their instrument
                if(helmetDecoration.getAsItem().equals(Items.GOAT_HORN)) {
                    toAttachList = new ArrayList<>();

                    HolderSet<Instrument> holderset = BuiltInRegistries.INSTRUMENT.getOrCreateTag(InstrumentTags.GOAT_HORNS);

                    var defaultInstance = helmetDecoration.getAsItem().getDefaultInstance();
                    toAttachList.add(defaultInstance);

                    for(Holder<Instrument> instrument: holderset){
                        var specificInstrument = defaultInstance.copy();
                        specificInstrument.set(DataComponents.INSTRUMENT, instrument);
                        toAttachList.add(specificInstrument);
                    }
                }
                //Default
                else {
                    toAttachList.add(helmetDecoration.getAsItem().getDefaultInstance());
                }

                var helmetModified = helmet.copy();
                HelmetDecorationComponent.of(helmetModified, toAttachList.getFirst(), left);

                return new AnvilRecipe(
                        left ?  toAttachList   : List.of(helmet),
                        left ?  List.of(helmet): toAttachList,
                        List.of(helmetModified),
                        recipeId
                );
            }
        }
    }
}