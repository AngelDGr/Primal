package org.primal.client.model.entity.replaced;

import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.PolarBear;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.replaced.PolarBearAnimations;
import org.primal.entity.replaced.PolarBearReplaced;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class PolarBearModel<T extends PolarBear> extends AgeableHierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "polar_bear"), "main");
    private final ModelPart root;
    private final ModelPart behr;
    private final ModelPart left_leg;
    private final ModelPart right_leg;
    private final ModelPart body_with_arms;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart left_arm;
    private final ModelPart right_arm;

    public PolarBearModel(ModelPart root) {
        super(0.4F, 36.0F);
        this.root = root;
        this.behr = root.getChild("behr");
        this.left_leg = this.behr.getChild("left_leg");
        this.right_leg = this.behr.getChild("right_leg");
        this.body_with_arms = this.behr.getChild("body_with_arms");
        this.body = this.body_with_arms.getChild("body");
        this.tail = this.body.getChild("tail");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.left_arm = this.body_with_arms.getChild("left_arm");
        this.right_arm = this.body_with_arms.getChild("right_arm");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition behr = partdefinition.addOrReplaceChild("behr", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, -4.0F));

        PartDefinition left_leg = behr.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(52, 51).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(70, 24).addBox(-4.0F, 8.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.75F, 8.0F, 13.0F));

        PartDefinition right_leg = behr.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(52, 51).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(70, 24).mirror().addBox(-4.0F, 8.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.75F, 8.0F, 13.0F));

        PartDefinition body_with_arms = behr.addOrReplaceChild("body_with_arms", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 9.0F));

        PartDefinition body = body_with_arms.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 34).addBox(-7.5F, -8.0F, -21.0F, 15.0F, 16.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.0F, -9.0F, -10.0F, 18.0F, 17.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(50, 69).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 7.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(27, 62).addBox(-2.5F, -2.0F, -14.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(71, 1).addBox(-2.5F, 2.75F, -14.0F, 5.0F, 1.0F, 6.0F, new CubeDeformation(-0.05F))
                .texOffs(52, 34).addBox(-5.5F, -5.0F, -8.0F, 11.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(70, 26).addBox(3.5F, -7.0F, -6.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(70, 26).mirror().addBox(-5.5F, -7.0F, -6.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, -21.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(71, 9).addBox(-2.5F, 0.0F, -6.0F, 5.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(71, 17).addBox(-2.5F, -0.9F, -6.0F, 5.0F, 1.0F, 6.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, 3.0F, -8.0F));

        PartDefinition left_arm = body_with_arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 61).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(70, 32).addBox(-3.0F, 8.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.25F, 8.0F, -13.0F));

        PartDefinition right_arm = body_with_arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 61).mirror().addBox(-3.0F, 0.0F, -4.0F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(70, 32).mirror().addBox(-3.0F, 8.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.25F, 8.0F, -13.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        //──────────────────────────────────── Fish Rotations ────────────────────────────────────
        if(entity.isInWaterOrBubble()) {
            this.behr.xRot = headPitch * (float) (Math.PI / 180.0);
            this.behr.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        }

        boolean canRun = (entity.isAggressive() || entity.walkAnimation.speed()>0.75) && !entity.isInWaterOrBubble();

        //──────────────────────────────────── Head Rotations ────────────────────────────────────
        var attackAnim = ((PolarBearReplaced) entity).primal$attackAnimationState();
        long attackAnimTime = attackAnim.getAccumulatedTime();

        //Accumulated Time
        //1000 -> 1.00s after the animation started
        //750  -> 0.75s after the animation started
        if (!attackAnim.isStarted() || attackAnimTime>750 || entity.isInWaterOrBubble()) {
            this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * ((float)Math.PI / 180F);
            this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * ((float)Math.PI / 180F);
        }

        //──────────────────────────────────── Movement ────────────────────────────────────
        if(!entity.isInWaterOrBubble())
            if (canRun)
                this.animateWalk(PolarBearAnimations.RUN, limbSwing, limbSwingAmount, 1.4f, 2.5f);
            else
                this.animateWalk(PolarBearAnimations.WALK, limbSwing, limbSwingAmount, 2.6f, 2.5f);

        //──────────────────────────────────── Idle & Swim ────────────────────────────────────
        if(entity.isInWaterOrBubble())
            this.animate(((PolarBearReplaced) (entity)).primal$idleAnimationState(), PolarBearAnimations.SWIM, ageInTicks,
                    Primal_Util.Visuals.getSwimFactor(entity, 5f, 0.3f, 2f));
        else
            this.animate(((PolarBearReplaced) (entity)).primal$idleAnimationState(), PolarBearAnimations.IDLE, ageInTicks, 1.0f);

        //──────────────────────────────────── Attack ────────────────────────────────────
        if (entity.isInWaterOrBubble())
            this.animate(((PolarBearReplaced) (entity)).primal$attackAnimationState(), PolarBearAnimations.ATTACK_SWIM, ageInTicks, 1.0f);
        else
            this.animate(((PolarBearReplaced) (entity)).primal$attackAnimationState(), PolarBearAnimations.ATTACK, ageInTicks, 1.0f);

        //──────────────────────────────────── Baby Scale ────────────────────────────────────
        if (this.young) this.applyStatic(PolarBearAnimations.BABY_TRANSFORM);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}
