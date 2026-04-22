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
import org.primal.client.animation.entity.SnakeAnimations;
import org.primal.entity.animal.SnakeEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class SnakeModel<T extends SnakeEntity> extends AgeableHierarchicalModel<T> {

    public SnakeModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    public static class Adult<T extends SnakeEntity> extends SnakeModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "snake"), "main");
        private final ModelPart root;
        private final ModelPart body;
        private final ModelPart tail_neckless;
        private final ModelPart tail;
        private final ModelPart tail_tip;
        private final ModelPart neck;
        private final ModelPart head;
        private final ModelPart throat;
        private final ModelPart jaw;
        private final ModelPart tongue;
        private final ModelPart rictals;

        public Adult(ModelPart root) {
            super(0.4F, 36f);
            this.root = root;
            this.body = root.getChild("body");
            this.tail_neckless = this.body.getChild("tail_neckless");
            this.tail = this.tail_neckless.getChild("tail");
            this.tail_tip = this.tail.getChild("tail_tip");
            this.neck = this.body.getChild("neck");
            this.head = this.neck.getChild("head");
            this.throat = this.head.getChild("throat");
            this.jaw = this.head.getChild("jaw");
            this.tongue = this.jaw.getChild("tongue");
            this.rictals = this.jaw.getChild("rictals");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.05F));

            PartDefinition tail_neckless = body.addOrReplaceChild("tail_neckless", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.5F));

            PartDefinition tail = tail_neckless.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 0.0F, 9.0F));

            PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(24, 6).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

            PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -9.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 0.0F, -4.5F));

            PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 0).addBox(-3.5F, -2.0F, -4.0F, 7.0F, 3.0F, 4.0F, new CubeDeformation(0.001F))
                    .texOffs(41, 11).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 23).addBox(-2.5F, -2.75F, -3.975F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 23).mirror().addBox(0.5F, -2.75F, -3.975F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(32, 24).mirror().addBox(0.5F, -3.75F, -3.975F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(32, 24).addBox(-2.5F, -3.75F, -3.975F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 26).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 29).addBox(-1.5F, 1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -9.0F));

            PartDefinition throat = head.addOrReplaceChild("throat", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -1.0F));

            PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(41, 21).addBox(1.0F, 1.0F, -5.0F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 17).addBox(-1.0F, -1.0F, -4.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(-0.001F))
                    .texOffs(45, 3).addBox(-1.0F, 0.95F, -4.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(52, 7).addBox(1.0F, 0.95F, -5.0F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(39, 8).addBox(-1.0F, -2.0F, -1.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(-0.001F))
                    .texOffs(32, 23).addBox(1.0F, 1.0F, -5.0F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.0F, 0.0F));

            PartDefinition tongue = jaw.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(40, 15).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.7F, 0.0F));

            PartDefinition rictals = jaw.addOrReplaceChild("rictals", CubeListBuilder.create().texOffs(37, 8).mirror().addBox(-3.45F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(37, 8).addBox(3.45F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 1.0F, -4.0F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void prepareMobModel(@NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick) {
            super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            if(!entity.isSlithering()){
                //──────────────────────────────────── Head Rotations ────────────────────────────────────
                float offsetRad = 107.5f * Mth.DEG_TO_RAD;
                float yaw = netHeadYaw * Mth.DEG_TO_RAD;

                head.xRot = headPitch * Mth.DEG_TO_RAD;
                head.yRot = Mth.cos(offsetRad) * yaw;
                head.zRot = Mth.sin(offsetRad) * yaw;

                if(entity.wrappedAnimationState.isStarted()){
                    head.xRot = 0;
                    head.yRot = 0;
                    head.zRot = 0;
                }
            }
            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble() && !entity.sittingAnimationState.isStarted())
                if (entity.slitherAnimationState.isStarted())
                    this.animateWalk(SnakeAnimations.Adult.SLITHER, limbSwing, limbSwingAmount, 1.8f, 6.5F);
                else if(entity.isAggressive() || entity.isCautious())
                    this.animateWalk(SnakeAnimations.Adult.SLITHER_CAUTIOUS, limbSwing, limbSwingAmount, 3.0f, 2.5F);
                else
                    this.animateWalk(SnakeAnimations.Adult.SLITHER_STANDING, limbSwing, limbSwingAmount, 3.0f, 2.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.swimAnimationState, SnakeAnimations.Adult.SLITHER, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 30f, 0.5f, 3.0f));

            if(entity.isCautious())
                this.animate(entity.idleAnimationState, SnakeAnimations.Adult.IDLE_CAUTIOUS, ageInTicks, 1.0f);
            else
                this.animate(entity.idleAnimationState, SnakeAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Attack ────────────────────────────────────
            if(entity.wrappedAnimationState.isStarted())
                this.animate(entity.attackAnimationState, SnakeAnimations.Adult.BITE_WRAPPED, ageInTicks, 1.0f);
            else
                if(entity.isSlithering())
                    this.animate(entity.attackAnimationState, SnakeAnimations.Adult.BITE_SLITHERING, ageInTicks, 1.0f);
                else
                    this.animate(entity.attackAnimationState, SnakeAnimations.Adult.BITE_STANDING, ageInTicks, 1.0f);

            //──────────────────────────────────── Slithering ────────────────────────────────────
            this.animate(entity.startSlitherAnimationState, SnakeAnimations.Adult.SLITHER_START, ageInTicks, 1.0f);
            this.animate(entity.stopSlitherAnimationState, SnakeAnimations.Adult.SLITHER_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Dancing ────────────────────────────────────
            this.animate(entity.danceAnimationState, SnakeAnimations.Adult.DANCE, ageInTicks, 1.0f);

            //──────────────────────────────────── Eat ────────────────────────────────────
            this.animate(entity.eatAnimationState, SnakeAnimations.Adult.EAT, ageInTicks, 1.0f);

            //──────────────────────────────────── Flick ────────────────────────────────────
            this.animate(entity.flickAnimationState, SnakeAnimations.Adult.TONGUE_FLICK, ageInTicks, 1.0f);

            //──────────────────────────────────── Sitting ────────────────────────────────────
            this.animate(entity.sittingAnimationState, SnakeAnimations.Adult.SIT, ageInTicks, 1.0f);

            //──────────────────────────────────── Wrapped ────────────────────────────────────
            this.animate(entity.wrappedAnimationState, SnakeAnimations.Adult.WRAPPED, ageInTicks, 1.0f);

            //──────────────────────────────────── Baby Scale ────────────────────────────────────
            if (this.young) this.applyStatic(SnakeAnimations.Adult.BABY_TRANSFORM);

            //──────────────────────────────────── Fieldguide Pose ────────────────────────────────────
            if(Primal_Util.Visuals.isOnFieldGuidePage(entity)) this.applyStatic(SnakeAnimations.Adult.IDLE);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends SnakeEntity> extends SnakeModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "snake_baby"), "main");
        private final ModelPart root;
        private final ModelPart body;
        private final ModelPart tail_neckless;
        private final ModelPart tail_tip;
        private final ModelPart neck;
        private final ModelPart head;
        private final ModelPart tongue;

        public Baby(ModelPart root) {
            super(1.0F, 0f);
            this.root = root;
            this.body = root.getChild("body");
            this.tail_neckless = this.body.getChild("tail_neckless");
            this.tail_tip = this.tail_neckless.getChild("tail_tip");
            this.neck = this.body.getChild("neck");
            this.head = this.neck.getChild("head");
            this.tongue = this.head.getChild("tongue");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, 1.0F));

            PartDefinition tail_neckless = body.addOrReplaceChild("tail_neckless", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition tail_tip = tail_neckless.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(12, 8).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

            PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.5F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(12, 5).addBox(-2.5F, -2.5F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(12, 5).mirror().addBox(0.5F, -2.5F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(12, 12).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

            PartDefinition tongue = head.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(16, 12).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.45F, -2.0F));

            return LayerDefinition.create(meshdefinition, 32, 32);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            if(!entity.isSlithering()){
                //──────────────────────────────────── Head Rotations ────────────────────────────────────
                float offsetRad = 107.5f * Mth.DEG_TO_RAD;
                float yaw = netHeadYaw * Mth.DEG_TO_RAD;

                head.xRot = headPitch * Mth.DEG_TO_RAD;
                head.yRot = Mth.cos(offsetRad) * yaw;
                head.zRot = Mth.sin(offsetRad) * yaw;

                if(entity.wrappedAnimationState.isStarted()){
                    head.xRot = 0;
                    head.yRot = 0;
                    head.zRot = 0;
                }
            }
            //──────────────────────────────────── Movement ────────────────────────────────────
            if(!entity.isInWaterOrBubble() && !entity.sittingAnimationState.isStarted())
                if (entity.slitherAnimationState.isStarted())
                    this.animateWalk(SnakeAnimations.Baby.SLITHER, limbSwing, limbSwingAmount, 1.8f, 6.5F);
                else if(entity.isAggressive() || entity.isCautious())
                    this.animateWalk(SnakeAnimations.Baby.SLITHER_CAUTIOUS, limbSwing, limbSwingAmount, 2.0f, 2.5F);
                else
                    this.animateWalk(SnakeAnimations.Baby.SLITHER_STANDING, limbSwing, limbSwingAmount, 2.0f, 2.5F);

            //──────────────────────────────────── Idle & Swim ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.swimAnimationState, SnakeAnimations.Baby.SLITHER, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 30f, 0.5f, 3.0f));


            this.animate(entity.idleAnimationState, SnakeAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Slithering ────────────────────────────────────
            this.animate(entity.startSlitherAnimationState, SnakeAnimations.Baby.SLITHER_START, ageInTicks, 1.0f);
            this.animate(entity.stopSlitherAnimationState, SnakeAnimations.Baby.SLITHER_END, ageInTicks, 1.0f);

            //──────────────────────────────────── Dancing ────────────────────────────────────
            this.animate(entity.danceAnimationState, SnakeAnimations.Baby.DANCE, ageInTicks, 1.0f);

            //──────────────────────────────────── Eat ────────────────────────────────────
            this.animate(entity.eatAnimationState, SnakeAnimations.Baby.EAT, ageInTicks, 1.0f);

            //──────────────────────────────────── Flick ────────────────────────────────────
            this.animate(entity.flickAnimationState, SnakeAnimations.Baby.TONGUE_FLICK, ageInTicks, 1.0f);

            //──────────────────────────────────── Sitting ────────────────────────────────────
            this.animate(entity.sittingAnimationState, SnakeAnimations.Baby.SIT, ageInTicks, 1.0f);

            //──────────────────────────────────── Wrapped ────────────────────────────────────
            this.animate(entity.wrappedAnimationState, SnakeAnimations.Baby.WRAPPED, ageInTicks, 1.0f);

            //──────────────────────────────────── Fieldguide Pose ────────────────────────────────────
            if(Primal_Util.Visuals.isOnFieldGuidePage(entity)) this.applyStatic(SnakeAnimations.Baby.IDLE);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}