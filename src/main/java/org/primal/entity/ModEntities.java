package org.primal.entity;

import org.primal.Primal_Main;
import org.primal.client.renderer.entity.BearRenderer;
import org.primal.entity.animal.Bear;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE,
            Primal_Main.MODID);

    public static DeferredHolder<EntityType<?>, EntityType<Bear>> BEAR = ENTITIES.register("bear",
            () -> EntityType.Builder.<Bear>of(Bear::new, MobCategory.CREATURE).sized(1.5f, 1.75f).clientTrackingRange(8).build("bear"));

    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(BEAR.get(), Bear.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BEAR.get(), ctx -> new BearRenderer(ctx));
    }

    public static void bootstrap(IEventBus bus) {
        ENTITIES.register(bus);
        bus.register(ModEntities.class);
    }
}
