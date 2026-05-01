package org.primal.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.CrocodileAnimations;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class CrocodileModel<T extends CrocodileEntity> extends AgeableHierarchicalModel<T> {

    public CrocodileModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    protected abstract ModelPart getBody();

    protected abstract ModelPart getHead();

    public abstract void translateToMouth(PoseStack poseStack);

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        //──────────────────────────────────── Fish Rotations ────────────────────────────────────
        if(entity.isInWaterOrBubble()) {
            this.getBody().xRot = headPitch * (float) (Math.PI / 180.0);
            this.getBody().yRot = netHeadYaw * (float) (Math.PI / 180.0);
        } else {
            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            this.getHead().xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
            this.getHead().yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);
        }
    }

    public static class Adult<T extends CrocodileEntity> extends CrocodileModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "crocodile"), "main");
        private final ModelPart root;
        private final ModelPart croc;
        private final ModelPart body;
        private final ModelPart head;
        private final ModelPart jaw;
        private final ModelPart mouth;
        private final ModelPart tail;
        private final ModelPart tail_tip;
        private final ModelPart right_back_leg;
        private final ModelPart right_back_foot;
        private final ModelPart left_back_leg;
        private final ModelPart left_back_foot;
        private final ModelPart right_front_leg;
        private final ModelPart right_front_foot;
        private final ModelPart left_front_leg;
        private final ModelPart left_front_foot;

        public Adult(ModelPart root) {
            super(0.5f, 24f);
            this.root = root;
            this.croc = root.getChild("croc");
            this.body = this.croc.getChild("body");
            this.head = this.body.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.mouth = this.head.getChild("mouth");
            this.tail = this.body.getChild("tail");
            this.tail_tip = this.tail.getChild("tail_tip");
            this.right_back_leg = this.croc.getChild("right_back_leg");
            this.right_back_foot = this.right_back_leg.getChild("right_back_foot");
            this.left_back_leg = this.croc.getChild("left_back_leg");
            this.left_back_foot = this.left_back_leg.getChild("left_back_foot");
            this.right_front_leg = this.croc.getChild("right_front_leg");
            this.right_front_foot = this.right_front_leg.getChild("right_front_foot");
            this.left_front_leg = this.croc.getChild("left_front_leg");
            this.left_front_foot = this.left_front_leg.getChild("left_front_foot");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition croc = partdefinition.addOrReplaceChild("croc", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 4.0F));

            PartDefinition body = croc.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -4.0F, -10.0F, 14.0F, 10.0F, 19.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 29).mirror().addBox(-7.0F, -6.0F, -10.0F, 6.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(48, 29).addBox(1.0F, -6.0F, -10.0F, 6.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 1.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(34, 56).addBox(2.0F, -3.0F, -8.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(34, 56).mirror().addBox(-4.0F, -3.0F, -8.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 81).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 84).addBox(-4.0F, -2.0F, -8.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(66, 0).addBox(-3.0F, -2.0F, -18.0F, 6.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                    .texOffs(92, 70).addBox(-3.0F, -3.0F, -18.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -10.0F));

            PartDefinition top_teeth_r1 = head.addOrReplaceChild("top_teeth_r1", CubeListBuilder.create().texOffs(34, 61).mirror().addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 1.5F, -6.0F, 0.0F, 0.0F, 0.4363F));

            PartDefinition top_teeth_r2 = head.addOrReplaceChild("top_teeth_r2", CubeListBuilder.create().texOffs(34, 61).addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.5F, -6.0F, 0.0F, 0.0F, -0.4363F));

            PartDefinition top_teeth_r3 = head.addOrReplaceChild("top_teeth_r3", CubeListBuilder.create().texOffs(66, 27).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.5F, -18.0F, -0.3491F, 0.0F, 0.0F));

            PartDefinition top_teeth_r4 = head.addOrReplaceChild("top_teeth_r4", CubeListBuilder.create().texOffs(48, 92).mirror().addBox(0.0F, 0.0F, -5.0F, 0.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.5F, -13.0F, 0.0F, 0.0F, 0.3491F));

            PartDefinition top_teeth_r5 = head.addOrReplaceChild("top_teeth_r5", CubeListBuilder.create().texOffs(48, 92).addBox(0.0F, 0.0F, -5.0F, 0.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.25F, -13.0F, 0.0F, 0.0F, -0.3491F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(72, 84).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(92, 92).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(68, 92).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(-0.01F))
                    .texOffs(90, 77).addBox(-4.0F, 2.0F, -8.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(66, 14).addBox(-3.0F, 2.0F, -18.0F, 6.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition bottom_teeth_r1 = jaw.addOrReplaceChild("bottom_teeth_r1", CubeListBuilder.create().texOffs(0, 89).addBox(0.0F, -2.0F, -5.0F, 0.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3F, 2.0F, -13.0F, 0.0F, 0.0F, 0.3491F));

            PartDefinition bottom_teeth_r2 = jaw.addOrReplaceChild("bottom_teeth_r2", CubeListBuilder.create().texOffs(0, 89).mirror().addBox(0.0F, -2.0F, -5.0F, 0.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.3F, 2.0F, -13.0F, 0.0F, 0.0F, -0.3491F));

            PartDefinition bottom_teeth_r3 = jaw.addOrReplaceChild("bottom_teeth_r3", CubeListBuilder.create().texOffs(34, 67).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0F, -17.4F, 0.3491F, 0.0F, 0.0F));

            PartDefinition bottom_teeth_r4 = jaw.addOrReplaceChild("bottom_teeth_r4", CubeListBuilder.create().texOffs(92, 95).mirror().addBox(0.0F, -2.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.15F, 2.0F, -6.0F, 0.0F, 0.0F, -0.4363F));

            PartDefinition bottom_teeth_r5 = jaw.addOrReplaceChild("bottom_teeth_r5", CubeListBuilder.create().texOffs(92, 95).addBox(0.0F, -2.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.15F, 2.0F, -6.0F, 0.0F, 0.0F, 0.4363F));

            PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, -16.0F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 29).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 9.0F, 18.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 50).addBox(-3.0F, -6.0F, 0.0F, 6.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 9.0F));

            PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -5.0F, 0.0F, 0.0F, 8.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 18.0F));

            PartDefinition right_back_leg = croc.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(34, 70).mirror().addBox(-4.0F, -0.975F, -4.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, 3.975F, 8.0F));

            PartDefinition right_back_foot = right_back_leg.addOrReplaceChild("right_back_foot", CubeListBuilder.create().texOffs(60, 70).mirror().addBox(-6.0F, -0.025F, -4.0F, 9.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 6.025F, 1.0F));

            PartDefinition left_back_leg = croc.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(34, 70).addBox(-2.0F, -0.975F, -4.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 3.975F, 8.0F));

            PartDefinition left_back_foot = left_back_leg.addOrReplaceChild("left_back_foot", CubeListBuilder.create().texOffs(60, 70).addBox(-3.0F, -0.025F, -4.0F, 9.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 6.025F, 1.0F));

            PartDefinition right_front_leg = croc.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(26, 84).mirror().addBox(-5.0F, -0.975F, -2.5F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 3.975F, -5.5F));

            PartDefinition right_front_foot = right_front_leg.addOrReplaceChild("right_front_foot", CubeListBuilder.create().texOffs(60, 77).mirror().addBox(-6.0F, -0.025F, -4.0F, 8.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 6.025F, -1.5F));

            PartDefinition left_front_leg = croc.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(26, 84).addBox(-1.0F, -0.975F, -2.5F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 3.975F, -5.5F));

            PartDefinition left_front_foot = left_front_leg.addOrReplaceChild("left_front_foot", CubeListBuilder.create().texOffs(60, 77).addBox(-2.0F, -0.025F, -4.0F, 8.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 6.025F, -1.5F));

            return LayerDefinition.create(meshdefinition, 128, 128);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                if (entity.isSprinting())
                    this.animateWalk(CrocodileAnimations.Adult.RUN, limbSwing, limbSwingAmount, 1.5f, 2.5f);
                else
                    this.animateWalk(CrocodileAnimations.Adult.WALK, limbSwing, limbSwingAmount, 5.0f, 2.5f);

            //──────────────────────────────────── Attack ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.attackAnimationState, CrocodileAnimations.Adult.ATTACK_UNDERWATER, ageInTicks, 1.0f);
            else
                this.animate(entity.attackAnimationState, CrocodileAnimations.Adult.ATTACK, ageInTicks, 1.0f);

            //──────────────────────────────────── Thrashing ────────────────────────────────────
            this.animate(entity.thrashingAnimationState, CrocodileAnimations.Adult.THRASH, ageInTicks, 1.0f);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                if(entity.swimIdleAnimationState.isStarted()){
                    this.animate(entity.idleAnimationState, CrocodileAnimations.Adult.SWIM_IDLE, ageInTicks,
                            1f);
                } else if(!entity.isAnimationInProgress()) {
                    this.animate(entity.idleAnimationState, CrocodileAnimations.Adult.SWIM, ageInTicks,
                            Primal_Util.Visuals.getSwimFactor(entity, 20f, 0.2f, 2.5f));
                }
            }
            else
                this.animate(entity.idleAnimationState, CrocodileAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Swim Idle ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.startSwimIdleAnimationState, CrocodileAnimations.Adult.SWIM_IDLE_START, ageInTicks, 1.0f);
                this.animate(entity.stopSwimIdleAnimationState, CrocodileAnimations.Adult.SWIM_IDLE_END, ageInTicks, 1.0f);
            }

            //──────────────────────────────────── Basking ────────────────────────────────────
            this.animate(entity.startBaskingAnimationState, CrocodileAnimations.Adult.BASKING_START, ageInTicks, 1.0f);
            this.animate(entity.baskingAnimationState, CrocodileAnimations.Adult.BASKING, ageInTicks, 1.0f);
            this.animate(entity.stopBaskingAnimationState, CrocodileAnimations.Adult.BASKING_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Vomits ────────────────────────────────────
            this.animate(entity.vomitsAnimationState, CrocodileAnimations.Adult.VOMITS, ageInTicks, 1.0f);

            //──────────────────────────────────── Shake Off ────────────────────────────────────
            this.animate(entity.shakeOffAnimationState, CrocodileAnimations.Adult.SHAKE_OFF, ageInTicks, 1.0f);
        }

        @Override
        public ModelPart getBody() {
            return croc;
        }

        @Override
        public ModelPart getHead() {
            return head;
        }

        @Override
        public void translateToMouth(PoseStack poseStack) {
            this.root().translateAndRotate(poseStack);
            this.croc.translateAndRotate(poseStack);
            this.body.translateAndRotate(poseStack);
            this.head.translateAndRotate(poseStack);
            this.mouth.translateAndRotate(poseStack);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends CrocodileEntity> extends CrocodileModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "crocodile_baby"), "main");
        private final ModelPart root;
        private final ModelPart babydile;
        private final ModelPart body;
        private final ModelPart head;
        private final ModelPart jaw;
        private final ModelPart tail;
        private final ModelPart left_front_leg;
        private final ModelPart right_front_leg;
        private final ModelPart right_back_leg;
        private final ModelPart left_back_leg;

        public Baby(ModelPart root) {
            super(1.0f, 0f);
            this.root = root;
            this.babydile = root.getChild("babydile");
            this.body = this.babydile.getChild("body");
            this.head = this.body.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.tail = this.body.getChild("tail");
            this.left_front_leg = this.babydile.getChild("left_front_leg");
            this.right_front_leg = this.babydile.getChild("right_front_leg");
            this.right_back_leg = this.babydile.getChild("right_back_leg");
            this.left_back_leg = this.babydile.getChild("left_back_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition babydile = partdefinition.addOrReplaceChild("babydile", CubeListBuilder.create(), PartPose.offset(0.0F, 21.5F, 0.0F));

            PartDefinition body = babydile.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 9).addBox(1.0F, -3.0F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 9).mirror().addBox(-1.0F, -3.0F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 24).addBox(0.5F, -2.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 24).addBox(-1.5F, -2.0F, -7.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 17).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 9).addBox(-1.5F, -1.0F, -7.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(26, 24).mirror().addBox(-1.5F, -2.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -2.0F, -3.0F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 27).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(18, 17).addBox(-1.5F, 1.0F, -6.0F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition jaw_r1 = jaw.addOrReplaceChild("jaw_r1", CubeListBuilder.create().texOffs(30, 19).mirror().addBox(0.0F, -1.0F, -1.5F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 1.0F, -3.5F, 0.0F, 0.0F, -0.1309F));

            PartDefinition jaw_r2 = jaw.addOrReplaceChild("jaw_r2", CubeListBuilder.create().texOffs(30, 19).addBox(0.0F, -1.0F, -1.5F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.0F, -3.5F, 0.0F, 0.0F, 0.1309F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, -2).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 3.0F));

            PartDefinition left_front_leg = babydile.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 1.5F, -2.5F));

            PartDefinition left_front_leg_r1 = left_front_leg.addOrReplaceChild("left_front_leg_r1", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

            PartDefinition right_front_leg = babydile.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 1.5F, -2.5F));

            PartDefinition right_front_leg_r1 = right_front_leg.addOrReplaceChild("right_front_leg_r1", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-5.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

            PartDefinition right_back_leg = babydile.addOrReplaceChild("right_back_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 1.5F, 2.5F));

            PartDefinition right_back_leg_r1 = right_back_leg.addOrReplaceChild("right_back_leg_r1", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-5.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

            PartDefinition left_back_leg = babydile.addOrReplaceChild("left_back_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 1.5F, 2.5F));

            PartDefinition left_back_leg_r1 = left_back_leg.addOrReplaceChild("left_back_leg_r1", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                this.animateWalk(CrocodileAnimations.Baby.WALK, limbSwing, limbSwingAmount, 2.0f, 2.5f);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                if(entity.swimIdleAnimationState.isStarted()){
                    this.animate(entity.idleAnimationState, CrocodileAnimations.Baby.SWIM_IDLE, ageInTicks,
                            1f);
                } else if(!entity.isAnimationInProgress()) {
                    this.animate(entity.idleAnimationState, CrocodileAnimations.Baby.SWIM, ageInTicks,
                            Primal_Util.Visuals.getSwimFactor(entity, 20f, 0.2f, 2.5f));
                }
            }
            else
                this.animate(entity.idleAnimationState, CrocodileAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Swim Idle ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.startSwimIdleAnimationState, CrocodileAnimations.Baby.SWIM_IDLE_START, ageInTicks, 1.0f);
                this.animate(entity.stopSwimIdleAnimationState, CrocodileAnimations.Baby.SWIM_IDLE_END, ageInTicks, 1.0f);
            }

            //──────────────────────────────────── Basking ────────────────────────────────────
            this.animate(entity.startBaskingAnimationState, CrocodileAnimations.Baby.BASKING_START, ageInTicks, 1.0f);
            this.animate(entity.baskingAnimationState, CrocodileAnimations.Baby.BASKING, ageInTicks, 1.0f);
            this.animate(entity.stopBaskingAnimationState, CrocodileAnimations.Baby.BASKING_END, ageInTicks, 1.0f);
        }

        @Override
        public ModelPart getBody() {
            return babydile;
        }

        @Override
        public ModelPart getHead() {
            return head;
        }

        @Override
        public void translateToMouth(PoseStack poseStack) {
            this.root().translateAndRotate(poseStack);
            this.getBody().translateAndRotate(poseStack);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}
