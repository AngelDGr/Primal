package org.primal.client.model.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.block.DreamcatcherBlock;
import org.primal.block_entity.DreamcatcherBlockEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class DreamcatcherModel extends DefaultedBlockGeoModel<DreamcatcherBlockEntity> {

    public DreamcatcherModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "dreamcatcher"));
    }

    @Override
    public @Nullable RenderType getRenderType(DreamcatcherBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(DreamcatcherBlockEntity animatable, long instanceId, AnimationState<DreamcatcherBlockEntity> animationState) {
        GeoBone catcher = getAnimationProcessor().getBone("catcher");

        GeoBone rope = getAnimationProcessor().getBone("rope");
        boolean hanging = animatable.getBlockState().getValue(DreamcatcherBlock.HANGING);


        catcher.setPosZ(hanging? 7.10f: 0);

        if(rope!=null){
            rope.setHidden(!hanging);
        }
    }
}
