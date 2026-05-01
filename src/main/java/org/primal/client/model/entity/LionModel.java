package org.primal.client.model.entity;

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
import org.primal.client.animation.entity.LionAnimations;
import org.primal.entity.animal.LionEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class LionModel<T extends LionEntity> extends AgeableHierarchicalModel<T> {

    public LionModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    public static class Adult<T extends LionEntity> extends LionModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "lion"), "main");
        private final ModelPart root;
        private final ModelPart lion;
        private final ModelPart body;
        private final ModelPart tail;
        private final ModelPart tail_tip;
        private final ModelPart head;
        private final ModelPart mane;
        private final ModelPart jaw;
        private final ModelPart tongue;
        private final ModelPart mouth_roof;
        private final ModelPart left_leg;
        private final ModelPart left_leg_paw;
        private final ModelPart left_leg_claws;
        private final ModelPart right_leg;
        private final ModelPart right_leg_paw;
        private final ModelPart right_leg_claws;
        private final ModelPart left_arm;
        private final ModelPart left_arm_paw;
        private final ModelPart left_arm_claws;
        private final ModelPart right_arm;
        private final ModelPart right_arm_paw;
        private final ModelPart right_arm_claws;

        public Adult(ModelPart root) {
            super(0.4F, 36f);
            this.root = root;
            this.lion = root.getChild("lion");
            this.body = this.lion.getChild("body");
            this.tail = this.body.getChild("tail");
            this.tail_tip = this.tail.getChild("tail_tip");
            this.head = this.body.getChild("head");
            this.mane = this.head.getChild("mane");
            this.jaw = this.head.getChild("jaw");
            this.tongue = this.jaw.getChild("tongue");
            this.mouth_roof = this.jaw.getChild("mouth_roof");
            this.left_leg = this.lion.getChild("left_leg");
            this.left_leg_paw = this.left_leg.getChild("left_leg_paw");
            this.left_leg_claws = this.left_leg_paw.getChild("left_leg_claws");
            this.right_leg = this.lion.getChild("right_leg");
            this.right_leg_paw = this.right_leg.getChild("right_leg_paw");
            this.right_leg_claws = this.right_leg_paw.getChild("right_leg_claws");
            this.left_arm = this.lion.getChild("left_arm");
            this.left_arm_paw = this.left_arm.getChild("left_arm_paw");
            this.left_arm_claws = this.left_arm_paw.getChild("left_arm_claws");
            this.right_arm = this.lion.getChild("right_arm");
            this.right_arm_paw = this.right_arm.getChild("right_arm_paw");
            this.right_arm_claws = this.right_arm_paw.getChild("right_arm_claws");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition lion = partdefinition.addOrReplaceChild("lion", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, -1.0F));

            PartDefinition body = lion.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.0F, -10.0F, 10.0F, 11.0F, 20.0F, new CubeDeformation(0.0F))
                    .texOffs(84, 0).addBox(-5.0F, -9.0F, -10.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 4.0F, -1.0F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(46, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 10.0F));

            PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(66, 69).addBox(-2.0F, 0.0F, 9.0F, 4.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(68, 60).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(52, 46).addBox(-4.5F, -5.5F, -7.0F, 9.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 71).mirror().addBox(-5.0F, -7.5F, -6.75F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(66, 76).addBox(-2.0F, -2.5F, -10.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 71).addBox(3.0F, -7.5F, -6.75F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(2, 81).addBox(-2.0F, 1.475F, -9.75F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F))
                    .texOffs(32, 79).addBox(-2.0F, 1.525F, -7.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -9.0F));

            PartDefinition mane = head.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 71).mirror().addBox(-16.0F, 5.0F, -5.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(52, 32).mirror().addBox(-16.0F, 9.0F, -5.0F, 6.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(52, 32).addBox(-5.0F, 9.0F, -5.0F, 6.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 71).addBox(-5.0F, 5.0F, -5.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 32).addBox(-16.0F, -3.0F, -3.0F, 17.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 86).addBox(-16.0F, -3.0F, -3.0F, 17.0F, 12.0F, 9.0F, new CubeDeformation(0.3F))
                    .texOffs(43, 36).addBox(-10.0F, 7.0F, 0.95F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(60, 0).addBox(-11.0F, -5.0F, -6.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(81, 5).addBox(-9.0F, -7.0F, -6.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -3.5F, -2.0F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(26, 70).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(38, 77).addBox(1.925F, 0.0F, -2.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(38, 77).mirror().addBox(-1.925F, 0.0F, -2.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 77).addBox(-2.0F, 4.0F, -5.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(2, 83).addBox(-2.0F, 3.025F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F))
                    .texOffs(28, 83).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(30, 79).addBox(-2.0F, 0.0F, -0.55F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -5.0F));

            PartDefinition tongue = jaw.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(9, 77).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.75F, 0.0F));

            PartDefinition mouth_roof = jaw.addOrReplaceChild("mouth_roof", CubeListBuilder.create(), PartPose.offset(0.0F, 0.025F, 0.0F));

            PartDefinition left_leg = lion.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 53).addBox(-2.5F, 2.0F, -3.0F, 5.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
                    .texOffs(46, 69).addBox(-2.5F, 11.0F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -2.0F, 5.0F));

            PartDefinition left_leg_paw = left_leg.addOrReplaceChild("left_leg_paw", CubeListBuilder.create().texOffs(60, 20).addBox(-3.0F, 1.0F, -4.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 3.0F));

            PartDefinition left_leg_claws = left_leg_paw.addOrReplaceChild("left_leg_claws", CubeListBuilder.create().texOffs(62, 24).addBox(2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(62, 24).addBox(-3.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(62, 24).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

            PartDefinition right_leg = lion.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 53).mirror().addBox(-2.5F, 2.0F, -3.0F, 5.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(46, 69).mirror().addBox(-2.5F, 11.0F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -2.0F, 5.0F));

            PartDefinition right_leg_paw = right_leg.addOrReplaceChild("right_leg_paw", CubeListBuilder.create().texOffs(60, 20).mirror().addBox(-3.0F, 1.0F, -4.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 15.0F, 3.0F));

            PartDefinition right_leg_claws = right_leg_paw.addOrReplaceChild("right_leg_claws", CubeListBuilder.create().texOffs(62, 24).mirror().addBox(-3.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(62, 24).mirror().addBox(2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(62, 24).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, -4.0F));

            PartDefinition left_arm = lion.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(26, 53).addBox(-2.5F, 1.0F, -2.75F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(81, 43).addBox(-2.5F, 1.0F, -2.75F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(3.5F, 1.0F, -6.25F));

            PartDefinition left_arm_paw = left_arm.addOrReplaceChild("left_arm_paw", CubeListBuilder.create().texOffs(60, 20).addBox(-3.0F, 1.0F, -4.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.25F));

            PartDefinition left_arm_claws = left_arm_paw.addOrReplaceChild("left_arm_claws", CubeListBuilder.create().texOffs(62, 24).addBox(2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(62, 24).addBox(-3.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(62, 24).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

            PartDefinition right_arm = lion.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(26, 53).mirror().addBox(-2.5F, 1.0F, -2.75F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(81, 43).mirror().addBox(-2.5F, 1.0F, -2.75F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-3.5F, 1.0F, -6.25F));

            PartDefinition right_arm_paw = right_arm.addOrReplaceChild("right_arm_paw", CubeListBuilder.create().texOffs(60, 20).mirror().addBox(-3.0F, 1.0F, -4.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 12.0F, 0.25F));

            PartDefinition right_arm_claws = right_arm_paw.addOrReplaceChild("right_arm_claws", CubeListBuilder.create().texOffs(62, 24).mirror().addBox(-3.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(62, 24).mirror().addBox(2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(62, 24).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, -4.0F));

            return LayerDefinition.create(meshdefinition, 128, 128);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            if((!entity.roarAnimationState.isStarted() || entity.roarAnimationState.getAccumulatedTime()>2000) && !entity.maulAnimationState.isStarted()) {
                this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
                this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);
            }

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble() && !entity.maulAnimationState.isStarted() && !entity.pounceAnimationState.isStarted())
                if(entity.isCrouching())
                    this.animateWalk(LionAnimations.Adult.STALK_WALK, limbSwing, limbSwingAmount, 2.0f, 5.5F);
                else if (entity.isSprinting())
                    this.animateWalk(LionAnimations.Adult.RUN, limbSwing, limbSwingAmount, 1.5f, 5.5F);
                else
                    this.animateWalk(LionAnimations.Adult.WALK, limbSwing, limbSwingAmount, 3.5f, 5.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.idleAnimationState, LionAnimations.Adult.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 10f, 0.35f, 1.8f));
                this.animate(entity.idleAnimationState, LionAnimations.Adult.IDLE, ageInTicks, 1.0f);
            }
            else if(entity.isCrouching())
                this.animate(entity.idleAnimationState, LionAnimations.Adult.STALK, ageInTicks, 1.0f);
            else
                this.animate(entity.idleAnimationState, LionAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Roar ────────────────────────────────────
            this.animate(entity.roarAnimationState, LionAnimations.Adult.ROAR, ageInTicks, 1.0f);

            //──────────────────────────────────── Maul ────────────────────────────────────
            this.animate(entity.maulAnimationState, LionAnimations.Adult.MAUL, ageInTicks, 1.0f);

            //──────────────────────────────────── Pounce ────────────────────────────────────
            this.animate(entity.pounceAnimationState, LionAnimations.Adult.POUNCE, ageInTicks, 1.0f);

            //──────────────────────────────────── Laying ────────────────────────────────────
            this.animate(entity.startLayingAnimationState, LionAnimations.Adult.LAY_START, ageInTicks, 1.0f);
            this.animate(entity.layingAnimationState, LionAnimations.Adult.LAY, ageInTicks, 1.0f);

            if(entity.stopLayingAnimationState.isStarted() && entity.roarAnimationState.isStarted()){
                this.right_arm_claws.z=this.right_arm_claws.z-0.95f;
                this.left_arm_claws.z=this.left_arm_claws.z-0.95f;

                this.right_leg_claws.z=this.right_leg_claws.z-0.95f;
                this.left_leg_claws.z=this.left_leg_claws.z-0.95f;
            }
            this.animate(entity.stopLayingAnimationState, LionAnimations.Adult.LAY_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Baby Scale ────────────────────────────────────
            if (this.young) this.applyStatic(LionAnimations.Adult.BABY_TRANSFORM);

            //──────────────────────────────────── Fieldguide Pose ────────────────────────────────────
            if(Primal_Util.Visuals.isOnFieldGuidePage(entity)) this.applyStatic(LionAnimations.Adult.IDLE);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends LionEntity> extends LionModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "lion_baby"), "main");
        private final ModelPart root;
        private final ModelPart lion;
        private final ModelPart body;
        private final ModelPart head;
        private final ModelPart tail;
        private final ModelPart right_leg;
        private final ModelPart right_arm;
        private final ModelPart left_arm;
        private final ModelPart left_leg;

        public Baby(ModelPart root) {
            super(1.0F, 0f);
            this.root = root;
            this.lion = root.getChild("lion");
            this.body = this.lion.getChild("body");
            this.head = this.body.getChild("head");
            this.tail = this.body.getChild("tail");
            this.right_leg = this.lion.getChild("right_leg");
            this.right_arm = this.lion.getChild("right_arm");
            this.left_arm = this.lion.getChild("left_arm");
            this.left_leg = this.lion.getChild("left_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition lion = partdefinition.addOrReplaceChild("lion", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition body = lion.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-1.5F, -2.0F, -2.75F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.75F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 16).addBox(2.0F, -4.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 16).mirror().addBox(-3.0F, -4.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(16, 19).addBox(-1.5F, -1.0F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -1.75F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 2.25F));

            PartDefinition right_leg = lion.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 8).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.975F, -2.0F, 2.0F));

            PartDefinition right_arm = lion.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 8).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.975F, -2.0F, -1.0F));

            PartDefinition left_arm = lion.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 8).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.975F, -2.0F, -1.0F));

            PartDefinition left_leg = lion.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 8).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.975F, -2.0F, 2.0F));

            return LayerDefinition.create(meshdefinition, 32, 32);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            if((!entity.roarAnimationState.isStarted() || entity.roarAnimationState.getAccumulatedTime()>2000) && !entity.maulAnimationState.isStarted()) {
                this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
                this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);
            }

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble() && !entity.maulAnimationState.isStarted() && !entity.pounceAnimationState.isStarted())
                if (entity.isSprinting())
                    this.animateWalk(LionAnimations.Baby.RUN, limbSwing, limbSwingAmount, 1.5f, 2.5F);
                else
                    this.animateWalk(LionAnimations.Baby.WALK, limbSwing, limbSwingAmount, 1.5f, 2.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.idleAnimationState, LionAnimations.Baby.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 20f, 0.35f, 2.8f));
                this.animate(entity.idleAnimationState, LionAnimations.Baby.IDLE, ageInTicks, 1.0f);
            }
            else
                this.animate(entity.idleAnimationState, LionAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Pounce ────────────────────────────────────
            this.animate(entity.pounceAnimationState, LionAnimations.Baby.POUNCE, ageInTicks, 1.0f);

            //──────────────────────────────────── Laying ────────────────────────────────────
            this.animate(entity.startLayingAnimationState, LionAnimations.Baby.LAY_START, ageInTicks, 1.0f);
            this.animate(entity.layingAnimationState, LionAnimations.Baby.LAY, ageInTicks, 1.0f);
            this.animate(entity.stopLayingAnimationState, LionAnimations.Baby.LAY_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Fieldguide Pose ────────────────────────────────────
            if(Primal_Util.Visuals.isOnFieldGuidePage(entity)) this.applyStatic(LionAnimations.Baby.IDLE);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}