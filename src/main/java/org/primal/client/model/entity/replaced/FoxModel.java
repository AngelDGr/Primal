package org.primal.client.model.entity.replaced;

import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Fox;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.replaced.FoxAnimations;
import org.primal.entity.replaced.FoxReplaced;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class FoxModel<T extends Fox> extends AgeableHierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "fox"), "main");
    private final ModelPart root;
    public final ModelPart foxy;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    public final ModelPart body_group;
    private final ModelPart body;
    public final ModelPart head;
    public final ModelPart mouth;
    private final ModelPart tongue;
    private final ModelPart tail;

    public FoxModel(ModelPart root) {
        super(0.5f, 24f);
        this.foxy = root.getChild("foxy");
        this.root = root;
        this.leg1 = this.foxy.getChild("leg1");
        this.leg2 = this.foxy.getChild("leg2");
        this.leg3 = this.foxy.getChild("leg3");
        this.leg4 = this.foxy.getChild("leg4");
        this.body_group = this.foxy.getChild("body_group");
        this.body = this.body_group.getChild("body");
        this.head = this.body_group.getChild("head");
        this.mouth = this.head.getChild("mouth");
        this.tongue = this.head.getChild("tongue");
        this.tail = this.body_group.getChild("tail");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition foxy = partdefinition.addOrReplaceChild("foxy", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 1.0F));

        PartDefinition leg1 = foxy.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(22, 5).addBox(-1.001F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.25F, 2.5F, 2.75F));

        PartDefinition leg2 = foxy.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(16, 26).addBox(-0.999F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.25F, 2.5F, 2.75F));

        PartDefinition leg3 = foxy.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(22, 5).addBox(-1.001F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.25F, 2.5F, -2.75F));

        PartDefinition leg4 = foxy.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(16, 26).addBox(-0.999F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.25F, 2.5F, -2.75F));

        PartDefinition body_group = foxy.addOrReplaceChild("body_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = body_group.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -4.25F, -1.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.25F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = body_group.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-3.5F, -3.0F, -4.0F, 7.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 15).addBox(3.5F, 0.0F, -3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(18, 15).mirror().addBox(-5.5F, 0.0F, -3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 26).addBox(1.5F, -5.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-3.5F, -5.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -4.0F));

        PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offset(0.0F, 2.25F, -7.0F));

        PartDefinition tongue = head.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(6, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -6.0F));

        PartDefinition tail = body_group.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 13).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.tongue.visible=entity.getMainHandItem().isEmpty();

        //──────────────────────────────────── Pounce Rotations ────────────────────────────────────
        if ((entity.isPouncing())) {
            this.foxy.xRot = headPitch * (float) (Math.PI / 180.0);
            this.foxy.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        }

        double speed = entity.getDeltaMovement().length();
        boolean canRun = (entity.isAggressive() || speed > 0.18) && !entity.isInWaterOrBubble();

        //──────────────────────────────────── Head Rotations ────────────────────────────────────
        if(!entity.isSleeping() && !entity.isFaceplanted()){
            this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * ((float)Math.PI / 180F);
            if(!entity.isPouncing())
                this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * ((float)Math.PI / 180F);
        }

        //──────────────────────────────────── Movement ────────────────────────────────────
        if(!entity.isInWaterOrBubble() && !entity.isPouncing()){
            if (canRun)
                this.animateWalk(FoxAnimations.RUN, limbSwing, limbSwingAmount, 1.4f, 2.5f);
            else
                this.animateWalk(FoxAnimations.WALK, limbSwing, limbSwingAmount, 2.6f, 2.5f);
        }

        //──────────────────────────────────── Idle & Swim ────────────────────────────────────
        if(!entity.isSleeping() && !entity.isSitting() && !entity.isPouncing()){
            if(entity.isInWaterOrBubble())
                this.animate(((FoxReplaced) (entity)).primal$idleAnimationState(), FoxAnimations.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 5f, 0.3f, 1.8f));
            else
                this.animate(((FoxReplaced) (entity)).primal$idleAnimationState(), FoxAnimations.IDLE, ageInTicks, 1.0f);
        }

        //──────────────────────────────────── Unstuck ────────────────────────────────────
        this.animate(((FoxReplaced) (entity)).primal$unstuckAnimationState(), FoxAnimations.UNSTUCK, ageInTicks, 1.0f);

        //──────────────────────────────────── Stuck ────────────────────────────────────
        var unstuckAnim = ((FoxReplaced) entity).primal$unstuckAnimationState();
        long unstuckAnimTime = unstuckAnim.getAccumulatedTime();

        if(!unstuckAnim.isStarted() || unstuckAnimTime>2250)
            this.animate(((FoxReplaced) (entity)).primal$stuckAnimationState(), FoxAnimations.STUCK, ageInTicks, 1.0f);

        //──────────────────────────────────── Pounce ────────────────────────────────────
        if(!entity.onGround())
            this.animate(((FoxReplaced) (entity)).primal$pounceAnimationState(), FoxAnimations.POUNCE, ageInTicks, 1.0f);

        //──────────────────────────────────── Sit ────────────────────────────────────
        this.animate(((FoxReplaced) (entity)).primal$sitAnimationState(), FoxAnimations.SIT, ageInTicks, 1.0f);

        //──────────────────────────────────── Sleeping ────────────────────────────────────
        this.animate(((FoxReplaced) (entity)).primal$sleepAnimationState(), FoxAnimations.SLEEP, ageInTicks, 1.0f);

        //──────────────────────────────────── Baby Scale ────────────────────────────────────
        if (this.young) this.applyStatic(FoxAnimations.BABY_TRANSFORM);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
}