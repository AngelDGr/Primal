package org.primal.client.model.entity;

import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.WalrusAnimations;
import org.primal.entity.animal.WalrusEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class WalrusModel<T extends WalrusEntity> extends AgeableHierarchicalModel<T> {
    public WalrusModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    protected abstract ModelPart getBody();

    protected abstract ModelPart getHead();

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        //──────────────────────────────────── Fish Rotations ────────────────────────────────────
        if(entity.isInWaterOrBubble() && !entity.isVehicle()) {
            this.getBody().xRot = headPitch * (float) (Math.PI / 180.0);
            this.getBody().yRot = netHeadYaw * (float) (Math.PI / 180.0);
        } else if(!entity.hasInstrument()) {
            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            this.getHead().xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
            this.getHead().yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);
        }
    }

    public static class Adult<T extends WalrusEntity> extends WalrusModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "walrus"), "main");
        private final ModelPart root;
        public final ModelPart walrus;
        private final ModelPart body_rear;
        private final ModelPart body;
        private final ModelPart body_squish;
        private final ModelPart head;
        private final ModelPart tiny_head;
        private final ModelPart snout;
        private final ModelPart mouth;
        private final ModelPart rear;
        private final ModelPart right_foot;
        private final ModelPart left_foot;
        public final ModelPart right_arm;
        public final ModelPart right_hand;
        public final ModelPart right_item;
        public final ModelPart left_arm;
        public final ModelPart left_hand;
        public final ModelPart left_item;
        private final ModelPart whirlpool;

        public Adult(ModelPart root) {
            super(0.25F, 71.75f);
            this.root=root;
            this.walrus = root.getChild("walrus");
            this.body_rear = this.walrus.getChild("body_rear");
            this.body = this.body_rear.getChild("body");
            this.body_squish = this.body.getChild("body_squish");
            this.head = this.body.getChild("head");
            this.tiny_head = this.head.getChild("tiny_head");
            this.snout = this.tiny_head.getChild("snout");
            this.mouth = this.tiny_head.getChild("mouth");
            this.rear = this.body_rear.getChild("rear");
            this.right_foot = this.rear.getChild("right_foot");
            this.left_foot = this.rear.getChild("left_foot");
            this.right_arm = this.walrus.getChild("right_arm");
            this.right_hand = this.right_arm.getChild("right_hand");
            this.right_item = this.right_hand.getChild("right_item");
            this.left_arm = this.walrus.getChild("left_arm");
            this.left_hand = this.left_arm.getChild("left_hand");
            this.left_item = this.left_hand.getChild("left_item");
            this.whirlpool = this.walrus.getChild("whirlpool");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition walrus = partdefinition.addOrReplaceChild("walrus", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 2.0F));

            PartDefinition body_rear = walrus.addOrReplaceChild("body_rear", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, -2.0F));

            PartDefinition body = body_rear.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -10.05F, 2.0F));

            PartDefinition body_squish = body.addOrReplaceChild("body_squish", CubeListBuilder.create().texOffs(0, 39).addBox(-8.0F, -16.0F, -13.0F, 16.0F, 16.0F, 28.0F, new CubeDeformation(0.0F))
                    .texOffs(68, 8).addBox(-8.5F, -16.75F, 1.0F, 17.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(88, 0).addBox(-8.5F, -18.75F, 10.0F, 17.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(60, 27).addBox(-7.0F, -21.0F, -8.0F, 14.0F, 25.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -6.0F));

            PartDefinition tiny_head = head.addOrReplaceChild("tiny_head", CubeListBuilder.create().texOffs(9, 84).addBox(-5.0F, -5.05F, -5.0F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(86, 79).addBox(-5.5F, -4.05F, -6.0F, 11.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.925F, -6.0F));

            PartDefinition snout = tiny_head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(38, 121).mirror().addBox(-6.5F, 1.95F, -4.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(56, 116).mirror().addBox(-5.5F, 2.95F, -4.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(52, 107).addBox(-6.5F, -3.05F, -4.0F, 13.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(40, 109).addBox(6.5F, -3.05F, -2.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(40, 109).mirror().addBox(-12.5F, -3.05F, -2.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(38, 121).addBox(1.5F, 1.95F, -4.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(56, 116).addBox(3.5F, 2.95F, -4.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -5.0F));

            PartDefinition mouth = tiny_head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(24, 109).addBox(-2.5F, -2.0F, -1.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.925F, -7.0F));

            PartDefinition rear = body_rear.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(39, 23).addBox(-5.5F, -5.05F, 0.0F, 11.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 17.0F));

            PartDefinition right_foot = rear.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(52, 98).mirror().addBox(-10.0F, 0.0F, -3.0F, 12.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 4.0F, 6.0F));

            PartDefinition left_foot = rear.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(52, 98).addBox(-2.0F, 0.0F, -3.0F, 12.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 4.0F, 6.0F));

            PartDefinition right_arm = walrus.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 109).mirror().addBox(-2.0F, -1.0F, -5.0F, 2.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.0F, 0.0F, -6.0F));

            PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(52, 83).mirror().addBox(-9.0F, 0.0F, -4.0F, 9.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 8.0F, -1.0F));

            PartDefinition right_item = right_hand.addOrReplaceChild("right_item", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 9.0F));

            PartDefinition left_arm = walrus.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 109).addBox(0.0F, -1.0F, -5.0F, 2.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -6.0F));

            PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(52, 83).addBox(0.0F, 0.0F, -4.0F, 9.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 8.0F, -1.0F));

            PartDefinition left_item = left_hand.addOrReplaceChild("left_item", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 9.0F));

            PartDefinition whirlpool = walrus.addOrReplaceChild("whirlpool", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 3.0F));

            PartDefinition body_r1 = whirlpool.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(100, 118).addBox(-9.0F, -31.0F, -8.0F, 17.0F, 33.0F, 17.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.5F, 1.5F, 13.75F, 1.5708F, 0.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 168, 168);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                if (entity.isSprinting())
                    this.animateWalk(WalrusAnimations.Adult.RUN, limbSwing, limbSwingAmount, 1.5f, 5.5F);
                else
                    this.animateWalk(WalrusAnimations.Adult.WALK, limbSwing, limbSwingAmount, 3.5f, 5.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                if(entity.swimIdleAnimationState.isStarted())
                    this.animate(entity.idleAnimationState, WalrusAnimations.Adult.SWIM_IDLE, ageInTicks,
                            1.0f);
                else if(!entity.isAnimationInProgress())
                    this.animate(entity.idleAnimationState, WalrusAnimations.Adult.SWIM, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 15f, 0.5f, 2.0f));
            }
            else
                this.animate(entity.idleAnimationState, WalrusAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Laying ────────────────────────────────────
            this.animate(entity.startLayingAnimationState, WalrusAnimations.Adult.LAY_START, ageInTicks, 1.0f);
            this.animate(entity.layingAnimationState, WalrusAnimations.Adult.LAY, ageInTicks, 1.0f);
            this.animate(entity.stopLayingAnimationState, WalrusAnimations.Adult.LAY_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Ground Pound ────────────────────────────────────
            this.animate(entity.groundPoundAnimationState, WalrusAnimations.Adult.GROUND_POUND, ageInTicks, 1.0f);

            //──────────────────────────────────── Swim Idle ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.startSwimIdleAnimationState, WalrusAnimations.Adult.SWIM_IDLE_START, ageInTicks, 1.0f);
                this.animate(entity.stopSwimIdleAnimationState, WalrusAnimations.Adult.SWIM_IDLE_END, ageInTicks, 1.0f);
            }

            //──────────────────────────────────── Swim Attack ────────────────────────────────────
            this.animate(entity.swimAttackAnimationState, WalrusAnimations.Adult.SWIM_ATTACK, ageInTicks, 1.0f);

            //──────────────────────────────────── Play ────────────────────────────────────
            this.animate(entity.playingAnimationState, WalrusAnimations.Adult.PLAY, ageInTicks, 1.0f);

            //──────────────────────────────────── Baby Scale ────────────────────────────────────
            if (this.young) this.applyStatic(WalrusAnimations.Adult.BABY_TRANSFORM);
        }

        @Override
        public ModelPart getBody() {
            return walrus;
        }

        @Override
        public ModelPart getHead() {
            return head;
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends WalrusEntity> extends WalrusModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "walrus_baby"), "main");
        private final ModelPart root;
        private final ModelPart walbaby;
        private final ModelPart body_rear;
        private final ModelPart body;
        private final ModelPart left_arm;
        private final ModelPart left_hand;
        private final ModelPart right_arm;
        private final ModelPart right_hand;
        private final ModelPart head;
        private final ModelPart mouth;
        private final ModelPart rear;
        private final ModelPart left_foot;
        private final ModelPart right_foot;

        public Baby(ModelPart root) {
            super(1.0F, 0f);
            this.root=root;
            this.walbaby = root.getChild("walbaby");
            this.body_rear = this.walbaby.getChild("body_rear");
            this.body = this.body_rear.getChild("body");
            this.left_arm = this.body.getChild("left_arm");
            this.left_hand = this.left_arm.getChild("left_hand");
            this.right_arm = this.body.getChild("right_arm");
            this.right_hand = this.right_arm.getChild("right_hand");
            this.head = this.body.getChild("head");
            this.mouth = this.head.getChild("mouth");
            this.rear = this.body_rear.getChild("rear");
            this.left_foot = this.rear.getChild("left_foot");
            this.right_foot = this.rear.getChild("right_foot");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition walbaby = partdefinition.addOrReplaceChild("walbaby", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, -3.0F));

            PartDefinition body_rear = walbaby.addOrReplaceChild("body_rear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 3.0F));

            PartDefinition body = body_rear.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -6.0F, 8.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

            PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 37).addBox(0.0F, -1.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -4.0F, -1.5F));

            PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(28, 28).addBox(0.0F, 0.0F, -2.5F, 5.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 4.0F, 0.0F));

            PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-1.0F, -1.0F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -4.0F, -1.5F));

            PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(28, 28).mirror().addBox(-5.0F, 0.0F, -2.5F, 5.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 4.0F, 0.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(38, 4).mirror().addBox(-2.5F, -5.0F, -7.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 19).addBox(-3.5F, -9.0F, -5.0F, 7.0F, 11.0F, 7.0F, new CubeDeformation(0.0F))
                    .texOffs(12, 37).addBox(-2.5F, -7.0F, -7.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(38, 7).addBox(2.5F, -7.0F, -6.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(38, 7).mirror().addBox(-4.5F, -7.0F, -6.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(38, 4).addBox(0.5F, -5.0F, -7.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -2.0F));

            PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(38, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -5.25F));

            PartDefinition rear = body_rear.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(28, 19).addBox(-3.0F, -2.025F, 0.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

            PartDefinition left_foot = rear.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(28, 34).addBox(-1.0F, 0.0F, -1.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, 3.0F));

            PartDefinition right_foot = rear.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(28, 34).mirror().addBox(-5.0F, 0.0F, -1.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 2.0F, 3.0F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                if (entity.isSprinting())
                    this.animateWalk(WalrusAnimations.Baby.RUN, limbSwing, limbSwingAmount, 0.8f, 1.0F);
                else
                    this.animateWalk(WalrusAnimations.Baby.WALK, limbSwing, limbSwingAmount, 1.25f, 2.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                if(entity.swimIdleAnimationState.isStarted())
                    this.animate(entity.idleAnimationState, WalrusAnimations.Baby.SWIM_IDLE, ageInTicks,
                            1.0f);
                else if(!entity.isAnimationInProgress())
                    this.animate(entity.idleAnimationState, WalrusAnimations.Baby.SWIM, ageInTicks,
                            Primal_Util.Visuals.getSwimFactor(entity, 15f, 0.5f, 2.0f));
            }
            else
                this.animate(entity.idleAnimationState, WalrusAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Laying ────────────────────────────────────
            this.animate(entity.startLayingAnimationState, WalrusAnimations.Baby.LAY_START, ageInTicks, 1.0f);
            this.animate(entity.layingAnimationState, WalrusAnimations.Baby.LAY, ageInTicks, 1.0f);
            this.animate(entity.stopLayingAnimationState, WalrusAnimations.Baby.LAY_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Swim Idle ────────────────────────────────────
            if(entity.isInWaterOrBubble()){
                this.animate(entity.startSwimIdleAnimationState, WalrusAnimations.Baby.SWIM_IDLE_START, ageInTicks, 1.0f);
                this.animate(entity.stopSwimIdleAnimationState, WalrusAnimations.Baby.SWIM_IDLE_END, ageInTicks, 1.0f);
            }
        }

        @Override
        public ModelPart getBody() {
            return walbaby;
        }

        @Override
        public ModelPart getHead() {
            return head;
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}