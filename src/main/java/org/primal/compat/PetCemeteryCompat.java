package org.primal.compat;

import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.primal.client.renderer.replaced.WolfRenderer;

public class PetCemeteryCompat {

    public static void registerMobsRenderer(){
        EntityRenderers.register(PCEntityTypes.SKELETON_WOLF.get(), WolfRenderer::new);
        EntityRenderers.register(PCEntityTypes.ZOMBIE_WOLF.get(), WolfRenderer::new);
    }
}
