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
import org.primal.client.animation.entity.DeerAnimations;
import org.primal.entity.animal.DeerEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class DeerModel<T extends DeerEntity> extends AgeableHierarchicalModel<T> {

    public DeerModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    public static class Adult<T extends DeerEntity> extends DeerModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "deer"), "main");
        private final ModelPart root;
        private final ModelPart deer;
        private final ModelPart body;
        private final ModelPart tail;
        private final ModelPart head;
        private final ModelPart left_musk_ear;
        private final ModelPart right_musk_ear;
        private final ModelPart right_ear;
        private final ModelPart left_ear;
        private final ModelPart right_antler;
        private final ModelPart left_antler;
        private final ModelPart right_front_leg;
        private final ModelPart right_back_leg;
        private final ModelPart left_back_leg;
        private final ModelPart left_front_leg;

        public Adult(ModelPart root) {
            super(0.5F, 24f);
            this.root = root;
            this.deer = root.getChild("deer");
            this.body = this.deer.getChild("body");
            this.tail = this.body.getChild("tail");
            this.head = this.body.getChild("head");
            this.left_musk_ear = this.head.getChild("left_musk_ear");
            this.right_musk_ear = this.head.getChild("right_musk_ear");
            this.right_ear = this.head.getChild("right_ear");
            this.left_ear = this.head.getChild("left_ear");
            this.right_antler = this.head.getChild("right_antler");
            this.left_antler = this.head.getChild("left_antler");
            this.right_front_leg = this.deer.getChild("right_front_leg");
            this.right_back_leg = this.deer.getChild("right_back_leg");
            this.left_back_leg = this.deer.getChild("left_back_leg");
            this.left_front_leg = this.deer.getChild("left_front_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition deer = partdefinition.addOrReplaceChild("deer", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.5F));

            PartDefinition body = deer.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -7.0F, 8.0F, 8.0F, 13.0F, new CubeDeformation(0.0F))
                    .texOffs(13, 43).addBox(-4.0F, -6.0F, -7.0F, 8.0F, 8.0F, 13.0F, new CubeDeformation(0.4F))
                    .texOffs(1, 5).addBox(0.0F, -8.0F, -5.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(42, 0).addBox(-1.25F, -4.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(54, 2).addBox(-1.25F, -4.0F, 3.0F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -5.0F, 5.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(48, 23).addBox(-2.0F, -8.0F, -4.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 52).addBox(-2.0F, -8.0F, -5.0F, 4.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(45, 16).addBox(-2.0F, 3.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 35).addBox(-2.0F, -12.0F, -9.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 37).addBox(-2.0F, -8.0F, -9.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -5.0F));

            PartDefinition left_musk_ear = head.addOrReplaceChild("left_musk_ear", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, -12.0F, 0.0F, 0.0F, 1.5708F, 1.5708F));

            PartDefinition cube_r1 = left_musk_ear.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 3).mirror().addBox(1.0F, 3.0F, 8.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, 1.5708F, 0.0F, 0.0F));

            PartDefinition cube_r2 = left_musk_ear.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(36, 4).mirror().addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

            PartDefinition right_musk_ear = head.addOrReplaceChild("right_musk_ear", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, -12.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

            PartDefinition cube_r3 = right_musk_ear.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 3).addBox(-1.0F, 3.0F, 8.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, 1.5708F, 0.0F, 0.0F));

            PartDefinition cube_r4 = right_musk_ear.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 4).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

            PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(42, 8).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(36, 9).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -11.0F, -1.0F, 0.0F, -1.5708F, -1.5708F));

            PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(42, 8).mirror().addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(36, 9).mirror().addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -11.0F, -1.0F, 0.0F, 1.5708F, 1.5708F));

            PartDefinition right_antler = head.addOrReplaceChild("right_antler", CubeListBuilder.create().texOffs(42, 13).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 21).addBox(-10.0F, -8.0F, -0.5F, 11.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4F, -11.9F, -2.5F, -0.0117F, 0.2615F, 0.0421F));

            PartDefinition left_antler = head.addOrReplaceChild("left_antler", CubeListBuilder.create().texOffs(42, 13).mirror().addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 21).mirror().addBox(-1.0F, -8.0F, -0.5F, 11.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.4F, -11.9F, -2.5F, -0.0117F, -0.2615F, -0.0421F));

            PartDefinition right_front_leg = deer.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(36, 21).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.45F, 3.0F, -4.5F));

            PartDefinition right_back_leg = deer.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(36, 21).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.45F, 3.0F, 5.5F));

            PartDefinition left_back_leg = deer.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(36, 21).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.45F, 3.0F, 5.5F));

            PartDefinition left_front_leg = deer.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(36, 21).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.45F, 3.0F, -4.5F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            //──────────────────────────────────── Jump Rotations ────────────────────────────────────
            if (entity.isJumping()) {
                this.deer.xRot = headPitch * (float) (Math.PI / 180.0);
                this.deer.yRot = netHeadYaw * (float) (Math.PI / 180.0);
            }

            //──────────────────────────────────── Antlers ────────────────────────────────────
            this.right_antler.visible=entity.hasRightAntler() && !entity.isBaby();
            this.left_antler.visible=entity.hasLeftAntler()  && !entity.isBaby();

            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
            this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble() && !entity.jumpAnimationState.isStarted())
                if (entity.isSprinting())
                    this.animateWalk(DeerAnimations.Adult.RUN, limbSwing, limbSwingAmount, entity.isBaby()? 1.0f: 1.5f, entity.isBaby()? 2.5f: 5.5f);
                else
                    this.animateWalk(DeerAnimations.Adult.WALK, limbSwing, limbSwingAmount, entity.isBaby()? 1.5f: 4.0f, entity.isBaby()? 8.5f: 5.5f);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.left_ear.xRot=this.left_ear.xRot+ (20 * ((float) (Math.PI / 180.0)));
                this.right_ear.xRot=this.right_ear.xRot+ (20 * ((float) (Math.PI / 180.0)));
                this.animate(entity.idleAnimationState, DeerAnimations.Adult.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 40f, 0.35f, 2.0f));
            }
            else
                this.animate(entity.idleAnimationState, DeerAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Jump ────────────────────────────────────
            this.animate(entity.jumpAnimationState, DeerAnimations.Adult.JUMP, ageInTicks, 1.0f);

            if(!entity.isSprinting()) {
                //──────────────────────────────────── Eat ────────────────────────────────────
                this.animate(entity.eatAnimationState, DeerAnimations.Adult.EAT, ageInTicks, 1.0f);

                //──────────────────────────────────── Look ────────────────────────────────────
                this.animate(entity.lookAnimationState, DeerAnimations.Adult.LOOK, ageInTicks, 1.0f);
            }

            //──────────────────────────────────── Baby Scale ────────────────────────────────────
            if (this.young) this.applyStatic(DeerAnimations.Adult.BABY_TRANSFORM);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends DeerEntity> extends DeerModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "deer_baby"), "main");
        private final ModelPart root;
        private final ModelPart deer;
        private final ModelPart body;
        private final ModelPart tail;
        private final ModelPart head;
        private final ModelPart left_ear;
        private final ModelPart right_ear;
        private final ModelPart left_back_leg;
        private final ModelPart right_back_leg;
        private final ModelPart right_front_leg;
        private final ModelPart left_front_leg;

        public Baby(ModelPart root) {
            super(1.0F, 0f);
            this.root = root;
            this.deer = root.getChild("deer");
            this.body = this.deer.getChild("body");
            this.tail = this.body.getChild("tail");
            this.head = this.body.getChild("head");
            this.left_ear = this.head.getChild("left_ear");
            this.right_ear = this.head.getChild("right_ear");
            this.left_back_leg = this.deer.getChild("left_back_leg");
            this.right_back_leg = this.deer.getChild("right_back_leg");
            this.right_front_leg = this.deer.getChild("right_front_leg");
            this.left_front_leg = this.deer.getChild("left_front_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition deer = partdefinition.addOrReplaceChild("deer", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition body = deer.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 24).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -7.0F, 0.5F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(16, 8).addBox(-1.5F, -3.5F, -0.5F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 2.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 8).addBox(-1.5F, -6.0F, -4.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(17, 15).addBox(-1.5F, -3.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

            PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, -3.5F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -5.5F, 0.4F));

            PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-2.0F, -3.5F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, -5.5F, 0.4F));

            PartDefinition left_back_leg = deer.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(14, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -6.0F, 3.0F));

            PartDefinition right_back_leg = deer.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(14, 16).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -6.0F, 3.0F));

            PartDefinition right_front_leg = deer.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(14, 16).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -6.0F, -1.5F));

            PartDefinition left_front_leg = deer.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(14, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -6.0F, -1.5F));

            return LayerDefinition.create(meshdefinition, 32, 32);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            //──────────────────────────────────── Jump Rotations ────────────────────────────────────
            if (entity.isJumping()) {
                this.deer.xRot = headPitch * (float) (Math.PI / 180.0);
                this.deer.yRot = netHeadYaw * (float) (Math.PI / 180.0);
            }

            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
            this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble() && !entity.jumpAnimationState.isStarted())
                if (entity.isSprinting())
                    this.animateWalk(DeerAnimations.Baby.RUN, limbSwing, limbSwingAmount, 0.6f, 2.5f);
                else
                    this.animateWalk(DeerAnimations.Baby.WALK, limbSwing, limbSwingAmount, 2.0f, 5.5f);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.idleAnimationState, DeerAnimations.Baby.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 40f, 0.35f, 2.0f));
            }
            else
                this.animate(entity.idleAnimationState, DeerAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Jump ────────────────────────────────────
            this.animate(entity.jumpAnimationState, DeerAnimations.Baby.JUMP, ageInTicks, 1.0f);

            if(!entity.isSprinting()) {
                //──────────────────────────────────── Eat ────────────────────────────────────
                this.animate(entity.eatAnimationState, DeerAnimations.Baby.EAT, ageInTicks, 1.0f);

                //──────────────────────────────────── Look ────────────────────────────────────
                this.animate(entity.lookAnimationState, DeerAnimations.Baby.LOOK, ageInTicks, 1.0f);
            }

        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}