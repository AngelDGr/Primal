package org.primal.client.model.entity.replaced;

import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Rabbit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.replaced.RabbitAnimations;
import org.primal.entity.replaced.RabbitReplaced;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class RabbitModel<T extends Rabbit> extends AgeableHierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "rabbit"), "main");
    private final ModelPart root;
    private final ModelPart rabbit;
    private final ModelPart body_arms;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart head;
    private final ModelPart left_ear;
    private final ModelPart right_ear;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public RabbitModel(ModelPart root) {
        super(0.4f, 36f);
        this.root = root;
        this.rabbit = root.getChild("rabbit");
        this.body_arms = this.rabbit.getChild("body_arms");
        this.body = this.body_arms.getChild("body");
        this.tail = this.body.getChild("tail");
        this.head = this.body.getChild("head");
        this.left_ear = this.head.getChild("left_ear");
        this.right_ear = this.head.getChild("right_ear");
        this.left_arm = this.body_arms.getChild("left_arm");
        this.right_arm = this.body_arms.getChild("right_arm");
        this.right_leg = this.rabbit.getChild("right_leg");
        this.left_leg = this.rabbit.getChild("left_leg");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rabbit = partdefinition.addOrReplaceChild("rabbit", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 1.75F));

        PartDefinition body_arms = rabbit.addOrReplaceChild("body_arms", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 1.0F));

        PartDefinition body = body_arms.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(2, 2).addBox(0.0F, -5.0F, -9.0F, 6.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 16).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -6.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -4.0F, -0.5F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -4.0F, -0.5F));

        PartDefinition left_arm = body_arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(36, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -4.0F, -7.0F));

        PartDefinition right_arm = body_arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(36, 18).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.75F, -4.0F, -7.0F));

        PartDefinition right_leg = rabbit.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-3.0F, 2.0F, 1.0F));

        PartDefinition cube_r2 = right_leg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 24).mirror().addBox(-1.0F, 0.0F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition left_leg = rabbit.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(3.0F, 2.0F, 1.0F));

        PartDefinition cube_r3 = left_leg.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 24).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float partialTick = ageInTicks - (float)entity.tickCount;

        //──────────────────────────────────── Head Rotations ────────────────────────────────────
        this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * ((float)Math.PI / 180F);
        this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * ((float)Math.PI / 180F);

        //──────────────────────────────────── Movement ────────────────────────────────────
        var state = ((RabbitReplaced) (entity)).primal$hopAnimationState();
        state.animateWhen(entity.getJumpCompletion(partialTick) > 0, entity.tickCount);
        this.animate(state, RabbitAnimations.HOP, ageInTicks, 1.0f);

        //──────────────────────────────────── Idle ────────────────────────────────────
         this.animate(((RabbitReplaced) (entity)).primal$idleAnimationState(), RabbitAnimations.IDLE, ageInTicks, 1.0f);

        //──────────────────────────────────── Rear ────────────────────────────────────
        this.animate(((RabbitReplaced) (entity)).primal$rearAnimationState(), RabbitAnimations.REAR, ageInTicks, 1.0f);

        //──────────────────────────────────── Baby Scale ────────────────────────────────────
        if (this.young) this.applyStatic(RabbitAnimations.BABY_TRANSFORM);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
}
