package org.primal.client.model.entity.replaced;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Dolphin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.replaced.DolphinAnimations;
import org.primal.entity.replaced.DolphinReplaced;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class DolphinModel<T extends Dolphin> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "dolphin"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart tail_fin;
    private final ModelPart head;
    private final ModelPart back_fin;
    private final ModelPart left_fin;
    private final ModelPart right_fin;

    public DolphinModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.tail = this.body.getChild("tail");
        this.tail_fin = this.tail.getChild("tail_fin");
        this.head = this.body.getChild("head");
        this.back_fin = this.body.getChild("back_fin");
        this.left_fin = this.body.getChild("left_fin");
        this.right_fin = this.body.getChild("right_fin");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(22, 0).addBox(-4.0F, -7.0F, -5.0F, 8.0F, 7.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(16, 41).addBox(-5.5F, -8.0F, -5.0F, 11.0F, 10.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 2.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 8.0F));

        PartDefinition tail_fin = tail.addOrReplaceChild("tail_fin", CubeListBuilder.create().texOffs(19, 20).addBox(-5.0F, -0.5F, -1.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 10.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-1.0F, 1.0F, -10.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 47).addBox(-2.0F, 1.0F, -9.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 27).addBox(-1.0F, 1.0F, -12.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -5.0F));

        PartDefinition back_fin = body.addOrReplaceChild("back_fin", CubeListBuilder.create().texOffs(51, 0).addBox(-0.5F, -2.0F, 6.0F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(48, 31).addBox(-0.5F, 1.0F, 8.75F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, -7.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition left_fin = body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(48, 20).addBox(0.0F, -2.0F, 0.0F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -4.0F));

        PartDefinition right_fin = body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(48, 20).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -4.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        //──────────────────────────────────── Fish Rotations ────────────────────────────────────
        this.body.xRot = headPitch * (float) (Math.PI / 180.0);
        this.body.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7)
            this.body.xRot = this.body.xRot + (-0.05F - 0.05F * Mth.cos(ageInTicks * 0.3F));

        //──────────────────────────────────── Movement ────────────────────────────────────
        if(entity.isInWaterOrBubble())
            this.animate(((DolphinReplaced) (entity)).primal$idleAnimationState(), DolphinAnimations.SWIM, ageInTicks, Primal_Util.Visuals.getSwimFactor(entity, 15f, 0.6f, 4f));
        else
            this.animate(((DolphinReplaced) (entity)).primal$idleAnimationState(), DolphinAnimations.SHAKE, ageInTicks, 1);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}
