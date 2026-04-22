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
import org.primal.client.animation.entity.CassowaryAnimations;
import org.primal.entity.animal.CassowaryEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class CassowaryModel<T extends CassowaryEntity> extends AgeableHierarchicalModel<T> {

    public CassowaryModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    public static class Adult<T extends CassowaryEntity> extends CassowaryModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "cassowary"), "main");
        private final ModelPart root;
        private final ModelPart cassowary;
        private final ModelPart body;
        private final ModelPart neck;
        private final ModelPart head;
        private final ModelPart jaw;
        private final ModelPart left_wattle;
        private final ModelPart right_wattle;
        private final ModelPart tail;
        private final ModelPart right_leg;
        private final ModelPart right_foot;
        private final ModelPart left_leg;
        private final ModelPart left_foot;

        public Adult(ModelPart root) {
            super(0.6F, 16f);
            this.root = root;
            this.cassowary = root.getChild("cassowary");
            this.body = this.cassowary.getChild("body");
            this.neck = this.body.getChild("neck");
            this.head = this.neck.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.left_wattle = this.neck.getChild("left_wattle");
            this.right_wattle = this.neck.getChild("right_wattle");
            this.tail = this.body.getChild("tail");
            this.right_leg = this.cassowary.getChild("right_leg");
            this.right_foot = this.right_leg.getChild("right_foot");
            this.left_leg = this.cassowary.getChild("left_leg");
            this.left_foot = this.left_leg.getChild("left_foot");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition cassowary = partdefinition.addOrReplaceChild("cassowary", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 1.0F));

            PartDefinition body = cassowary.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.9F, 8.0F, 10.0F, 13.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 23).addBox(-4.0F, 0.0F, -4.9F, 8.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -0.1F));

            PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(46, 48).mirror().addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -3.0F, -0.9F, -0.231F, 0.3426F, -0.0595F));

            PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(46, 48).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, -0.9F, -0.231F, -0.3426F, 0.0595F));

            PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(20, 38).addBox(-1.5F, -10.0F, -3.0F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -4.9F));

            PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(42, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(42, 32).addBox(-1.0F, -1.0F, -7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(42, 7).addBox(-1.0F, -6.0F, -5.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 49).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(42, 37).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, -1.5F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(42, 44).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

            PartDefinition left_wattle = neck.addOrReplaceChild("left_wattle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -1.0F, -3.0F, -0.0873F, 0.0F, 0.0F));

            PartDefinition cube_r3 = left_wattle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(42, 48).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

            PartDefinition right_wattle = neck.addOrReplaceChild("right_wattle", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, -1.0F, -3.0F, -0.0873F, 0.0F, 0.0F));

            PartDefinition cube_r4 = right_wattle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(42, 48).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 38).addBox(-3.0F, -1.0F, 2.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(32, 38).addBox(-3.0F, 6.0F, 2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 6.1F));

            PartDefinition right_leg = cassowary.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(42, 21).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.75F, 2.0F, 2.0F));

            PartDefinition right_foot = right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(42, 17).mirror().addBox(-3.0F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(32, 44).mirror().addBox(-1.0F, 0.0F, -4.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, 0.0F));

            PartDefinition cube_r5 = right_foot.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(10, 49).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.75F, -4.0F, -0.6109F, 0.0F, 0.0F));

            PartDefinition cube_r6 = right_foot.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(6, 49).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.75F, 0.0F, 0.0F, 0.0F, -0.3927F));

            PartDefinition cube_r7 = right_foot.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(8, 49).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.75F, 0.0F, 0.0F, 0.0F, 0.3927F));

            PartDefinition left_leg = cassowary.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 21).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, 2.0F, 2.0F));

            PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(42, 17).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(32, 44).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

            PartDefinition cube_r8 = left_foot.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(10, 49).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.75F, -4.0F, -0.6109F, 0.0F, 0.0F));

            PartDefinition cube_r9 = left_foot.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(6, 49).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.75F, 0.0F, 0.0F, 0.0F, 0.3927F));

            PartDefinition cube_r10 = left_foot.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(6, 49).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.75F, 0.0F, 0.0F, 0.0F, -0.3927F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            if(!entity.fruitPickAnimationState.isStarted() || entity.fruitPickAnimationState.getAccumulatedTime()>2100){
                this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * ((float)Math.PI / 180F);
                this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * ((float)Math.PI / 180F);
            }

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                if (entity.isSprinting())
                    this.animateWalk(CassowaryAnimations.Adult.RUN, limbSwing, limbSwingAmount, 1.2f, 5.5F);
                else
                    this.animateWalk(CassowaryAnimations.Adult.WALK, limbSwing, limbSwingAmount, 3.0f, 5.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.idleAnimationState, CassowaryAnimations.Adult.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 5f, 0.3f, 1.8f));
            else
                this.animate(entity.idleAnimationState, CassowaryAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Sit ────────────────────────────────────
            this.animate(entity.startSittingAnimationState, CassowaryAnimations.Adult.SIT_START, ageInTicks, 1.0f);
            this.animate(entity.sittingAnimationState, CassowaryAnimations.Adult.SIT, ageInTicks, 1.0f);
            this.animate(entity.stopSittingAnimationState, CassowaryAnimations.Adult.SIT_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Attack ────────────────────────────────────
            this.animate(entity.startAttackingAnimationState, CassowaryAnimations.Adult.ATTACK_START, ageInTicks, 1.0f);
            this.animate(entity.attackingAnimationState, CassowaryAnimations.Adult.ATTACK, ageInTicks, 1.0f);
            this.animate(entity.stopAttackingAnimationState, CassowaryAnimations.Adult.ATTACK_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Fruit Pick ────────────────────────────────────
            this.animate(entity.fruitPickAnimationState, CassowaryAnimations.Adult.FRUIT_PICK, ageInTicks, 1.0f);

            //──────────────────────────────────── Baby Scale ────────────────────────────────────
            if (this.young) this.applyStatic(CassowaryAnimations.Adult.BABY_TRANSFORM);

            //──────────────────────────────────── Fieldguide Pose ────────────────────────────────────
            if(Primal_Util.Visuals.isOnFieldGuidePage(entity)) this.applyStatic(CassowaryAnimations.Adult.IDLE);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends CassowaryEntity> extends CassowaryModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "cassowary_baby"), "main");
        private final ModelPart root;
        private final ModelPart cassobaby;
        private final ModelPart body;
        private final ModelPart neck;
        private final ModelPart head;
        private final ModelPart jaw;
        private final ModelPart right_leg;
        private final ModelPart left_leg;

        public Baby(ModelPart root) {
            super(1.0f, 0.0f);
            this.root=root;
            this.cassobaby = root.getChild("cassobaby");
            this.body = this.cassobaby.getChild("body");
            this.neck = this.body.getChild("neck");
            this.head = this.neck.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.right_leg = this.cassobaby.getChild("right_leg");
            this.left_leg = this.cassobaby.getChild("left_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition cassobaby = partdefinition.addOrReplaceChild("cassobaby", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition body = cassobaby.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(18, 0).addBox(0.0F, -4.0F, 2.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

            PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 16).addBox(2.0F, -4.0F, -1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -3.0F, -2.5F));

            PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 9).addBox(-0.5F, -2.0F, -6.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(18, 5).addBox(-0.5F, -1.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, 0.0F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(16, 13).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -3.0F));

            PartDefinition right_leg = cassobaby.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(8, 18).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(8, 16).mirror().addBox(-1.5F, 5.0F, -2.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -5.0F, 0.0F));

            PartDefinition left_leg = cassobaby.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(8, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(8, 16).addBox(-1.5F, 5.0F, -2.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -5.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 32, 32);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            if(!entity.fruitPickAnimationState.isStarted() || entity.fruitPickAnimationState.getAccumulatedTime()>2100){
                this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * ((float)Math.PI / 180F);
                this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * ((float)Math.PI / 180F);
            }

            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble())
                this.animateWalk(CassowaryAnimations.Baby.WALK, limbSwing, limbSwingAmount, 2.0f, 5.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.idleAnimationState, CassowaryAnimations.Baby.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 5f, 0.3f, 1.8f));
            else
                this.animate(entity.idleAnimationState, CassowaryAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Sit ────────────────────────────────────
            this.animate(entity.startSittingAnimationState, CassowaryAnimations.Baby.SIT_START, ageInTicks, 1.0f);
            this.animate(entity.sittingAnimationState, CassowaryAnimations.Baby.SIT, ageInTicks, 1.0f);
            this.animate(entity.stopSittingAnimationState, CassowaryAnimations.Baby.SIT_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Fruit Pick ────────────────────────────────────
            this.animate(entity.fruitPickAnimationState, CassowaryAnimations.Baby.FRUIT_PICK, ageInTicks, 1.0f);

            //──────────────────────────────────── Fieldguide Pose ────────────────────────────────────
            if(Primal_Util.Visuals.isOnFieldGuidePage(entity)) this.applyStatic(CassowaryAnimations.Baby.IDLE);

        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}