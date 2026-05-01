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
import org.primal.client.animation.entity.BearAnimations;
import org.primal.entity.animal.BearEntity;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class BearModel<T extends BearEntity> extends AgeableHierarchicalModel<T> {

    public BearModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    protected abstract ModelPart getBody();

    protected abstract ModelPart getHead();

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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

    public static class Adult<T extends BearEntity> extends BearModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "bear"), "main");
        private final ModelPart root;
        private final ModelPart grizzler;
        private final ModelPart body;
        private final ModelPart head;
        private final ModelPart jaw;
        private final ModelPart tail;
        private final ModelPart right_leg;
        private final ModelPart left_leg;
        private final ModelPart right_arm;
        private final ModelPart left_arm;

        public Adult(ModelPart root) {
            super(0.4F, 36f);
            this.root = root;
            this.grizzler = root.getChild("grizzler");
            this.body = this.grizzler.getChild("body");
            this.head = this.body.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.tail = this.body.getChild("tail");
            this.right_leg = this.grizzler.getChild("right_leg");
            this.left_leg = this.grizzler.getChild("left_leg");
            this.right_arm = this.grizzler.getChild("right_arm");
            this.left_arm = this.grizzler.getChild("left_arm");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition grizzler = partdefinition.addOrReplaceChild("grizzler", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 2.0F));

            PartDefinition body = grizzler.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 31).addBox(-8.5F, -9.0F, 0.0F, 17.0F, 17.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 99).addBox(-8.5F, -9.0F, 0.0F, 17.0F, 17.0F, 12.0F, new CubeDeformation(0.2F))
                    .texOffs(0, 0).addBox(-8.5F, -11.0F, -12.0F, 17.0F, 19.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 60).addBox(-8.5F, -11.0F, -12.0F, 17.0F, 19.0F, 12.0F, new CubeDeformation(0.2F))
                    .texOffs(58, 64).addBox(8.5F, -6.0F, -9.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F))
                    .texOffs(58, 64).mirror().addBox(-17.5F, -6.0F, -9.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(96, 83).addBox(-7.0F, -8.0F, -12.5F, 14.0F, 16.0F, 2.0F, new CubeDeformation(0.01F))
                    .texOffs(57, 97).addBox(0.0F, -12.0F, -2.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(98, 32).addBox(-2.5F, -1.0F, -13.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                    .texOffs(66, 100).addBox(-1.5F, 4.0F, -12.925F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(58, 83).addBox(-6.0F, -5.0F, -7.0F, 12.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(72, 47).addBox(-6.0F, -5.0F, -7.0F, 12.0F, 10.0F, 7.0F, new CubeDeformation(0.2F))
                    .texOffs(92, 103).addBox(-6.0F, 5.0F, -6.0F, 12.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                    .texOffs(60, 118).addBox(-6.0F, -15.0F, -5.5F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 75).mirror().addBox(-6.0F, -7.0F, -5.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 75).addBox(4.0F, -7.0F, -5.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -12.0F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(104, 60).addBox(-2.5F, -0.025F, -6.0F, 5.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                    .texOffs(85, 101).addBox(-2.5F, -1.0F, -6.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 4.025F, -7.0F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(104, 67).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 12.0F));

            PartDefinition right_leg = grizzler.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(84, 112).mirror().addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 81).mirror().addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 81).mirror().addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 81).mirror().addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 8.0F, 7.5F));

            PartDefinition left_leg = grizzler.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(84, 112).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 81).addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 81).addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 81).addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 8.0F, 7.5F));

            PartDefinition right_arm = grizzler.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(66, 101).mirror().addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 81).mirror().addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 81).mirror().addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(104, 81).mirror().addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 8.0F, -7.0F));

            PartDefinition left_arm = grizzler.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(66, 101).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 81).addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 81).addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(104, 81).addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 8.0F, -7.0F));

            return LayerDefinition.create(meshdefinition, 128, 128);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                if (entity.isSprinting())
                    this.animateWalk(BearAnimations.Adult.RUN, limbSwing, limbSwingAmount, 1.2f, 5.5F);
                else
                    this.animateWalk(BearAnimations.Adult.WALK, limbSwing, limbSwingAmount, 3.0f, 2.5F);

            float speed = (float) entity.getDeltaMovement().length();
            speed = Mth.clamp(speed, 0.0f, 1.0f);
            float healthFactor = Mth.clamp((entity.getHealth() / entity.getMaxHealth()) * (speed * 15.0f), 0.3f, 1.5f);
            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.idleAnimationState, BearAnimations.Adult.SWIM, ageInTicks, healthFactor);
            else
                this.animate(entity.idleAnimationState, BearAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Attack ────────────────────────────────────
            this.animate(entity.attackAnimationState, BearAnimations.Adult.ATTACK, ageInTicks, 1.0f);

            //──────────────────────────────────── Roar ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.roarAnimationState, BearAnimations.Adult.ROAR_SWIM, ageInTicks, 1.0f);
            else
                this.animate(entity.roarAnimationState, BearAnimations.Adult.ROAR, ageInTicks, 1.0f);

            //──────────────────────────────────── Sleep ────────────────────────────────────
            this.animate(entity.startSleepingAnimationState, BearAnimations.Adult.SLEEP_START, ageInTicks, 1.0f);
            this.animate(entity.sleepingAnimationState, BearAnimations.Adult.SLEEP, ageInTicks, 1.0f);
            this.animate(entity.stopSleepingAnimationState, BearAnimations.Adult.SLEEP_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Beg ────────────────────────────────────
            this.animate(entity.startBeggingAnimationState, BearAnimations.Adult.BEG_START, ageInTicks, 1.0f);
            this.animate(entity.beggingAnimationState, BearAnimations.Adult.BEG, ageInTicks, 1.0f);
            this.animate(entity.stopBeggingAnimationState, BearAnimations.Adult.BEG_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Baby Scale ────────────────────────────────────
            if (this.young) this.applyStatic(BearAnimations.Adult.BABY_TRANSFORM);
        }

        @Override
        public ModelPart getBody() {
            return grizzler;
        }

        @Override
        public ModelPart getHead() {
            return head;
        }

        @Override
        public @NotNull ModelPart root() {
            return root;
        }

        public static class Grolar<T extends BearEntity> extends Adult<T> {
            public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "grolar"), "main");

            public Grolar(ModelPart root) {
                super(root);
            }

            public static LayerDefinition createLayer() {
                MeshDefinition meshdefinition = new MeshDefinition();
                PartDefinition partdefinition = meshdefinition.getRoot();

                PartDefinition grizzler = partdefinition.addOrReplaceChild("grizzler", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 2.0F));

                PartDefinition body = grizzler.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 31).addBox(-8.5F, -9.0F, 0.0F, 17.0F, 17.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-8.5F, -11.0F, -12.0F, 17.0F, 19.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(58, 31).addBox(-7.0F, -9.0F, -12.0F, 14.0F, 17.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(58, 0).addBox(-8.5F, -11.0F, 0.0F, 17.0F, 19.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(58, 64).addBox(7.0F, -6.0F, -9.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(58, 64).mirror().addBox(-16.0F, -6.0F, -9.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(96, 83).addBox(-7.0F, -8.0F, -12.0F, 14.0F, 16.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

                PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 102).addBox(-2.5F, -1.0F, -13.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(66, 100).addBox(-1.5F, 4.0F, -12.925F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(58, 83).addBox(-6.0F, -5.0F, -7.0F, 12.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(92, 103).addBox(-6.0F, 5.0F, -6.0F, 12.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(60, 118).addBox(-6.0F, -15.0F, -5.5F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 75).mirror().addBox(-6.0F, -7.0F, -5.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 75).addBox(4.0F, -7.0F, -5.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -12.0F));

                PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(104, 60).addBox(-2.5F, -0.025F, -6.0F, 5.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(85, 101).addBox(-2.5F, -1.0F, -6.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 4.025F, -7.0F));

                PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(104, 67).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 12.0F));

                PartDefinition right_leg = grizzler.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(38, 100).mirror().addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 81).mirror().addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 81).mirror().addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 81).mirror().addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 8.0F, 7.5F));

                PartDefinition left_leg = grizzler.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(38, 100).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 81).addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 81).addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 81).addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 8.0F, 7.5F));

                PartDefinition right_arm = grizzler.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(66, 101).mirror().addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 81).mirror().addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 81).mirror().addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(104, 81).mirror().addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 8.0F, -7.0F));

                PartDefinition left_arm = grizzler.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(66, 101).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 81).addBox(2.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 81).addBox(-3.0F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 81).addBox(-0.5F, 7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 8.0F, -7.0F));

                return LayerDefinition.create(meshdefinition, 128, 128);
            }
        }
    }

    public static class Baby<T extends BearEntity> extends BearModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "bear_baby"), "main");
        private final ModelPart root;
        private final ModelPart bearby;
        private final ModelPart body;
        private final ModelPart tail;
        private final ModelPart head;
        private final ModelPart left_front_leg;
        private final ModelPart right_front_leg;
        private final ModelPart right_back_leg;
        private final ModelPart left_back_leg;

        public Baby(ModelPart root) {
            super(1.0f, 0.0F);
            this.root = root;
            this.bearby = root.getChild("bearby");
            this.body = this.bearby.getChild("body");
            this.tail = this.body.getChild("tail");
            this.head = this.body.getChild("head");
            this.left_front_leg = this.bearby.getChild("left_front_leg");
            this.right_front_leg = this.bearby.getChild("right_front_leg");
            this.right_back_leg = this.bearby.getChild("right_back_leg");
            this.left_back_leg = this.bearby.getChild("left_back_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition bearby = partdefinition.addOrReplaceChild("bearby", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition body = bearby.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -6.0F, -4.5F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 1.0F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 20).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 3.5F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 26).addBox(-3.0F, -5.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 26).mirror().addBox(2.0F, -5.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 25).addBox(-2.0F, -1.0F, -6.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -3.5F));

            PartDefinition left_front_leg = bearby.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(22, 14).addBox(2.0F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(12, 25).addBox(2.0F, 2.0F, -2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, -3.0F, -1.75F));

            PartDefinition right_front_leg = bearby.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(22, 14).mirror().addBox(-5.0F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(12, 25).mirror().addBox(-5.0F, 2.0F, -2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.75F, -3.0F, -1.75F));

            PartDefinition right_back_leg = bearby.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(22, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(12, 25).mirror().addBox(-1.5F, 2.0F, -2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.75F, -3.0F, 3.25F));

            PartDefinition left_back_leg = bearby.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(22, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(12, 25).addBox(-1.5F, 2.0F, -2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -3.0F, 3.25F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                if (entity.isSprinting())
                    this.animateWalk(BearAnimations.Baby.RUN, limbSwing, limbSwingAmount, 0.6f, 5.5F);
                else
                    this.animateWalk(BearAnimations.Baby.WALK, limbSwing, limbSwingAmount, 1.5f, 2.5F);

            float speed = (float) entity.getDeltaMovement().length();
            speed = Mth.clamp(speed, 0.0f, 1.0f);
            float healthFactor = Mth.clamp((entity.getHealth() / entity.getMaxHealth()) * (speed * 15.0f), 0.3f, 1.5f);
            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.idleAnimationState, BearAnimations.Baby.SWIM, ageInTicks,  healthFactor);
            else
                this.animate(entity.idleAnimationState, BearAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Sleep ────────────────────────────────────
            this.animate(entity.startSleepingAnimationState, BearAnimations.Baby.SLEEP_START, ageInTicks, 1.0f);
            this.animate(entity.sleepingAnimationState, BearAnimations.Baby.SLEEP, ageInTicks, 1.0f);
            this.animate(entity.stopSleepingAnimationState, BearAnimations.Baby.SLEEP_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Beg ────────────────────────────────────
            this.animate(entity.startBeggingAnimationState, BearAnimations.Baby.BEG_START, ageInTicks, 1.0f);
            this.animate(entity.beggingAnimationState, BearAnimations.Baby.BEG, ageInTicks, 1.0f);
            this.animate(entity.stopBeggingAnimationState, BearAnimations.Baby.BEG_END, ageInTicks, 1.0f);
        }

        @Override
        public ModelPart getBody() {
            return bearby;
        }

        @Override
        public ModelPart getHead() {
            return head;
        }

        @Override
        public @NotNull ModelPart root() {
            return root;
        }
    }
}
