package org.primal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;
import org.primal.client.item.ConchShellClientExtension;
import org.primal.client.item.StrawBasketExtension;
import org.primal.client.layer.HelmetDecorationLayer;
import org.primal.client.model.helmet_decoration.DeerAntlersModel;
import org.primal.client.model.helmet_decoration.GoatHornsModel;
import org.primal.client.renderer.EntityOnNeckLayer;
import org.primal.client.renderer.block_entity.*;
import org.primal.client.renderer.entity.*;
import org.primal.client.renderer.replaced.*;
import org.primal.compat.PetCemeteryCompat;
import org.primal.entity.animal.SnakeEntity;
import org.primal.item.ConchShellItem;
import org.primal.item.SnakeItem;
import org.primal.particle.DreamcatcherAshParticle;
import org.primal.particle.SnakeSkinFlakeParticle;
import org.primal.registry.*;

@EventBusSubscriber(modid = Primal_Main.MOD_ID, value = Dist.CLIENT)
@Mod(value = Primal_Main.MOD_ID, dist = Dist.CLIENT)
public class Primal_Client {

    public static ModelLayerLocation DEER_ANTLERS_ON_HELMET_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "helmet_decoration_antlers"),
            "helmet_decoration_antlers");

    public static ModelLayerLocation GOAT_HORNS_ON_HELMET_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "helmet_decoration_goat_horns"),
            "helmet_decoration_goat_horns");

    @SubscribeEvent
    public static void registerClientEvent(final FMLClientSetupEvent event){
        registerBlockRenderers();
        registerItemProperties();

        if (Primal_Main.COMMON_CONFIG.polarBearModelChange.get())
            EntityRenderers.register(EntityType.POLAR_BEAR, PolarBearRenderer::new);

        if (Primal_Main.COMMON_CONFIG.foxModelChange.get())
            EntityRenderers.register(EntityType.FOX, FoxRenderer::new);

        if (Primal_Main.COMMON_CONFIG.rabbitModelChange.get())
            EntityRenderers.register(EntityType.RABBIT, RabbitRenderer::new);

        if (Primal_Main.COMMON_CONFIG.wolfModelChange.get()){
            EntityRenderers.register(EntityType.WOLF, WolfRenderer::new);

            if(FMLLoader.getLoadingModList().getModFileById("pet_cemetery")!=null) PetCemeteryCompat.registerMobsRenderer();
        }

        if (Primal_Main.COMMON_CONFIG.dolphinModelChange.get())
            EntityRenderers.register(EntityType.DOLPHIN, DolphinRenderer::new);
    }

    @SubscribeEvent
    public static void registerBuiltinResourcePack(final AddPackFindersEvent event){
        if (event.getPackType() == PackType.CLIENT_RESOURCES){
            addBuiltinResourcePack(event, "modern_banner_patterns");
            addBuiltinResourcePack(event, "modern_spawn_eggs");
        }
    }

    public static void addBuiltinResourcePack(AddPackFindersEvent event, String id){
        String[] parts = id.split("_");
        StringBuilder nameBuilder = new StringBuilder();

        for (String part : parts){
            if (part.isEmpty()) continue;
            nameBuilder.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }

        String name = nameBuilder.toString().trim();

        Component primalStyled = Component.literal("Primal")
                .withStyle(style -> style
                        .withColor(0xd1854b)
                        .withItalic(false)
                        .withBold(true)
                );

        Component subtitleStyle = Component.literal("")
                .withStyle(style -> style
                        .withColor(0x92502e)
                        .withItalic(false)
                        .withBold(false)
                );

        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "resourcepacks/" + id),
                PackType.CLIENT_RESOURCES,
                primalStyled.copy()
                        .append(Component.literal(" - " + name)
                                .withStyle(subtitleStyle.getStyle())),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void registerStandaloneModels(final ModelEvent.RegisterAdditional event){
        //Adds all helmet decorations automatically
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        manager.listResources("models/helmet_decoration", path -> path.getPath().endsWith(".json"))
                .keySet()
                .forEach(location -> {

                    String path = location.getPath()
                            .replace("models/", "")
                            .replace(".json", "");

                    ResourceLocation modelId = ResourceLocation.fromNamespaceAndPath(
                            location.getNamespace(),
                            path
                    );

                    event.register(ModelResourceLocation.standalone(modelId));
                });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DEER_ANTLERS_ON_HELMET_LAYER, DeerAntlersModel::createLayer);
        event.registerLayerDefinition(GOAT_HORNS_ON_HELMET_LAYER, GoatHornsModel::createLayer);
    }

    @SubscribeEvent
    public static void registerEntityLayers(final EntityRenderersEvent.AddLayers event) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance()
                .getEntityRenderDispatcher();

        EntityOnNeckLayer.registerOnPlayer(dispatcher, event.getContext());

        HelmetDecorationLayer.registerOnAll(dispatcher, event.getContext());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Primal_Entities.BEAR.get(),      BearRenderer::new);
        event.registerEntityRenderer(Primal_Entities.SHARK.get(),     SharkRenderer::new);
        event.registerEntityRenderer(Primal_Entities.CROCODILE.get(), CrocodileRenderer::new);
        event.registerEntityRenderer(Primal_Entities.EAGLE.get(),     EagleRenderer::new);
        event.registerEntityRenderer(Primal_Entities.CASSOWARY.get(), CassowaryRenderer::new);
        event.registerEntityRenderer(Primal_Entities.WALRUS.get(),    WalrusRenderer::new);
        event.registerEntityRenderer(Primal_Entities.LION.get(),      LionRenderer::new);
        event.registerEntityRenderer(Primal_Entities.SNAKE.get(),     SnakeRenderer::new);
        event.registerEntityRenderer(Primal_Entities.DEER.get(),      DeerRenderer::new);

        event.registerEntityRenderer(Primal_Entities.EXPLOSEED.get(), ThrownItemRenderer::new);

        event.registerBlockEntityRenderer(Primal_BlockEntities.NEST_BLOCK_ENTITY.get(), NestBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(Primal_BlockEntities.ROTATION_16_BLOCK_ENTITY.get(), Rotation16BlockEntityRenderer::new);
        event.registerBlockEntityRenderer(Primal_BlockEntities.CHOMP_TRAP.get(), ChompTrapRenderer::new);
        event.registerBlockEntityRenderer(Primal_BlockEntities.DREAMCATCHER.get(), DreamcatcherRenderer::new);
        event.registerBlockEntityRenderer(Primal_BlockEntities.STRAW_BASKET.get(), StrawBasketRenderer::new);
    }

    @SuppressWarnings("deprecation")
    public static void registerBlockRenderers(){
        //Blocks
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.NEST_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.SHARK_TOOTH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.RIVER_REEDS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.SHORT_RIVER_REEDS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.CATTAILS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.WARM_SEASHELLS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.COLD_SEASHELLS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.TEMPERATE_SEASHELLS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.PETRIFIED_FRUIT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.LITCHI_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.KIWANO_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.STARFRUIT_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.LITCHI_TREE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.KIWANO_BULK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.STARFRUIT_TREE.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.THORNY_ACACIA_LOG.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.THORNY_ACACIA_WOOD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.THORNY_ACACIA_LEAVES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.THORNY_ACACIA_SAPLING.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.FALLOW_DEER_ANTLER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.REINDEER_ANTLER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.WHITETAIL_DEER_ANTLER.get(), RenderType.cutout());
    }

    public static void registerItemProperties() {
        registerSnakeItemVariant(SnakeEntity.Variant.MARINE);

        registerTooting(Primal_Items.WARM_CONCH_SHELL.get());
        registerTooting(Primal_Items.TEMPERATE_CONCH_SHELL.get());
        registerTooting(Primal_Items.COLD_CONCH_SHELL.get());
    }

    private static void registerSnakeItemVariant(SnakeEntity.Variant variant) {
        ItemProperties.register(Primal_Items.PLACEHOLDER_CHESTED_SNAKE.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,variant.getSerializedName()),
                (stack, world, entity, seed) ->
                        SnakeItem.hasVariant(stack, variant) ? 1.0f : 0.0f);
    }

    private static void registerTooting(Item item) {
        ItemProperties.register(
                item,
                ResourceLocation.parse("tooting"),
                (stack, level, entity, i) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }

    @SubscribeEvent
    public static void registerColorBlocks(final RegisterColorHandlersEvent.Block event){

        event.register((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) {
                        return GrassColor.getDefaultColor();
                    }
                    return BiomeColors.getAverageGrassColor(world, pos);}, Primal_Blocks.SHORT_RIVER_REEDS.get(), Primal_Blocks.RIVER_REEDS.get(), Primal_Blocks.CATTAILS.get());
    }

    @SubscribeEvent
    public static void registerColorItems(final RegisterColorHandlersEvent.Item event){
        event.register((stack, tintIndex) -> 0x91bd59, Primal_Items.SHORT_RIVER_REEDS.get());

        event.register((stack, tintIndex) -> tintIndex==0? 0x91bd59: -1, Primal_Items.RIVER_REEDS.get());

        event.register((stack, tintIndex) -> tintIndex==0? 0x91bd59: -1, Primal_Items.CATTAILS.get());

        registerEgg(event, Primal_Items.BEAR_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.CROCODILE_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.EAGLE_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.SHARK_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.CASSOWARY_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.WALRUS_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.LION_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.SNAKE_SPAWN_EGG.get());
        registerEgg(event, Primal_Items.DEER_SPAWN_EGG.get());
    }

    private static void registerEgg(final RegisterColorHandlersEvent.Item event, Item egg){
        event.register((stack, tintIndex) ->
                //Disables the tint if modern eggs texture pack is active
                Minecraft.getInstance().getResourceManager().listPacks()
                        .anyMatch(packResources -> packResources.packId().equals("mod/primal:resourcepacks/modern_spawn_eggs"))?
                        -1
                        : FastColor.ARGB32.opaque(((SpawnEggItem) (egg)).getColor(tintIndex)), egg);
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        // There are multiple ways to register providers, all differing in the functional type they provide in the
        // second parameter. For example, #registerSpriteSet represents a Function<SpriteSet, ParticleProvider<?>>:
        event.registerSpriteSet(Primal_Particles.SNAKE_SKIN_FLAKE, SnakeSkinFlakeParticle.Factory::new);
        event.registerSpriteSet(Primal_Particles.DREAMCATCHER_ASH, DreamcatcherAshParticle.Factory::new);
    }

    @SubscribeEvent
    public static void customizeOverlay(RenderGuiLayerEvent.@NotNull Pre event) {
    }

    @SubscribeEvent
    public static void registerItemClientExtensions(final RegisterClientExtensionsEvent event) {
        event.registerItem(new ConchShellClientExtension(),
                Primal_Items.WARM_CONCH_SHELL,
                Primal_Items.TEMPERATE_CONCH_SHELL,
                Primal_Items.COLD_CONCH_SHELL);

        event.registerItem(new StrawBasketExtension(),
                Primal_Items.STRAW_BASKET);
    }

    @SubscribeEvent
    public static void modifyFovEvent(final ComputeFovModifierEvent event) {
        if(event.getPlayer() instanceof LocalPlayer localPlayer){

            ItemStack useItem = localPlayer.getUseItem();
            if(!useItem.has(Primal_Items.Components.CONCH_SHELL)) return;

            if(useItem.is(Primal_Items.WARM_CONCH_SHELL) || useItem.is(Primal_Items.TEMPERATE_CONCH_SHELL) || useItem.is(Primal_Items.COLD_CONCH_SHELL)){
                float scaleRate = -1f *(localPlayer.getUseItemRemainingTicks() - ConchShellItem.MAX_DURATION) / ConchShellItem.RELEASE_TIME;

                if (scaleRate > 1.0F)
                    scaleRate = 1.0F;
                else
                    scaleRate *= scaleRate;

                float newFov = event.getFovModifier() * (1.0F - scaleRate * 0.30f);
                event.setNewFovModifier(newFov);

            }
        }
    }
}
