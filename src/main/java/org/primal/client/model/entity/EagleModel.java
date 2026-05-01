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
import org.primal.client.animation.entity.EagleAnimations;
import org.primal.entity.animal.EagleEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public abstract class EagleModel<T extends EagleEntity> extends AgeableHierarchicalModel<T> {

    public EagleModel(float youngScaleFactor, float bodyYOffset) {
        super(youngScaleFactor, bodyYOffset);
    }

    public static class Adult<T extends EagleEntity> extends EagleModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle"), "main");
        private final ModelPart root;
        private final ModelPart mighty;
        private final ModelPart body;
        private final ModelPart tail;
        private final ModelPart right_wing;
        private final ModelPart right_idle_feathers;
        private final ModelPart right_flight_feathers;
        private final ModelPart left_wing;
        private final ModelPart left_idle_feathers;
        private final ModelPart left_flight_feathers;
        private final ModelPart head;
        private final ModelPart large_feathers;
        private final ModelPart small_feathers;
        private final ModelPart right_leg;
        private final ModelPart left_leg;

        public Adult(ModelPart root) {
            super(0.5f, 24f);
            this.root = root;
            this.mighty = root.getChild("mighty");
            this.body = this.mighty.getChild("body");
            this.tail = this.body.getChild("tail");
            this.right_wing = this.body.getChild("right_wing");
            this.right_idle_feathers = this.right_wing.getChild("right_idle_feathers");
            this.right_flight_feathers = this.right_wing.getChild("right_flight_feathers");
            this.left_wing = this.body.getChild("left_wing");
            this.left_idle_feathers = this.left_wing.getChild("left_idle_feathers");
            this.left_flight_feathers = this.left_wing.getChild("left_flight_feathers");
            this.head = this.body.getChild("head");
            this.large_feathers = this.head.getChild("large_feathers");
            this.small_feathers = this.head.getChild("small_feathers");
            this.right_leg = this.mighty.getChild("right_leg");
            this.left_leg = this.mighty.getChild("left_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition mighty = partdefinition.addOrReplaceChild("mighty", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, -1.0F));

            PartDefinition body = mighty.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-3.5F, -4.0F, -3.0F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -0.5F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(15, 0).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 4.0F));

            PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(28, 8).addBox(-1.25F, -2.7654F, -0.1522F, 2.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -2.0F, -1.5F, -0.3927F, 0.0F, 0.0F));

            PartDefinition right_idle_feathers = right_wing.addOrReplaceChild("right_idle_feathers", CubeListBuilder.create().texOffs(0, -2).addBox(-1.0F, -8.0F, -10.0F, 0.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.2346F, 10.8478F));

            PartDefinition right_flight_feathers = right_wing.addOrReplaceChild("right_flight_feathers", CubeListBuilder.create().texOffs(0, 23).addBox(-1.0F, -11.0F, -10.0F, 0.0F, 10.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.2346F, 10.8478F));

            PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(28, 8).mirror().addBox(-0.75F, -2.7654F, -0.1522F, 2.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -2.0F, -1.5F, -0.3927F, 0.0F, 0.0F));

            PartDefinition left_idle_feathers = left_wing.addOrReplaceChild("left_idle_feathers", CubeListBuilder.create().texOffs(0, -2).mirror().addBox(1.0F, -8.0F, -10.0F, 0.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.2346F, 10.8478F));

            PartDefinition left_flight_feathers = left_wing.addOrReplaceChild("left_flight_feathers", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(1.0F, -11.0F, -10.0F, 0.0F, 10.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.2346F, 10.8478F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 22).addBox(-2.5F, -7.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(28, 34).addBox(-2.5F, -7.0F, -7.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                    .texOffs(14, 36).addBox(-2.0F, -2.0F, -7.5F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(10, 37).addBox(-3.5F, -7.0F, 2.5F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 1.5F));

            PartDefinition large_feathers = head.addOrReplaceChild("large_feathers", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 1.25F));

            PartDefinition cube_r1 = large_feathers.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(39, 7).addBox(-2.0F, -5.0F, 0.0F, 11.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.25F, 0.0F, -0.3709F, 0.0F, 0.0F));

            PartDefinition small_feathers = head.addOrReplaceChild("small_feathers", CubeListBuilder.create(), PartPose.offset(0.0F, -6.25F, 0.075F));

            PartDefinition cube_r2 = small_feathers.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(46, 15).addBox(-2.0F, -5.0F, 0.0F, 9.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 2.0F, -0.75F, -0.3622F, 0.0F, 0.0F));

            PartDefinition right_leg = mighty.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(24, 8).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 36).mirror().addBox(-1.5F, 3.0F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 0).mirror().addBox(-0.5F, 2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 3.0F, 0.5F));

            PartDefinition left_leg = mighty.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 8).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 36).addBox(-1.5F, 3.0F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 2).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, 0.5F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            //──────────────────────────────────── Bird Rotations ────────────────────────────────────
            if(entity.flyAnimationState.isStarted()) {
                this.mighty.xRot = headPitch * (float) (Math.PI / 180.0);
                this.mighty.yRot = netHeadYaw * (float) (Math.PI / 180.0);
            } else {
                //──────────────────────────────────── Head Rotations ────────────────────────────────────
                this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
                this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);
            }

            //──────────────────────────────────── Wings Change ────────────────────────────────────
            left_flight_feathers.visible = entity.flyAnimationState.isStarted();
            right_flight_feathers.visible = entity.flyAnimationState.isStarted();

            left_idle_feathers.visible = !left_flight_feathers.visible;
            right_idle_feathers.visible = !right_flight_feathers.visible;

            //──────────────────────────────────── Movement ────────────────────────────────────
            double speed = entity.getDeltaMovement().length();
            boolean canSwoop = !entity.onGround() && entity.getXRot() > 50 && speed>0.35 && entity.isAggressive();

            if(canSwoop){
                this.animate(entity.flyAnimationState, EagleAnimations.Adult.SWOOP, ageInTicks, 1.0f);
            } else if(entity.onGround()){
                this.animate(entity.flyAnimationState, EagleAnimations.Adult.WALK, ageInTicks, 1.0f);
            } else if(speed>0.50){
                this.animate(entity.flyAnimationState, EagleAnimations.Adult.GLIDE, ageInTicks, 1.0f);
            } else {
                this.animate(entity.flyAnimationState, EagleAnimations.Adult.FLY, ageInTicks, 1.0f);
            }

            //──────────────────────────────────── Idle ────────────────────────────────────
            this.animate(entity.idleAnimationState, EagleAnimations.Adult.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Sitting ────────────────────────────────────
            this.animate(entity.sittingAnimationState, EagleAnimations.Adult.SIT, ageInTicks, 1.0f);

            //──────────────────────────────────── Look Out ────────────────────────────────────
            this.animate(entity.lookOutAnimationState, EagleAnimations.Adult.LOOK_OUT, ageInTicks, 1.0f);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }

    public static class Baby<T extends EagleEntity> extends EagleModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle_baby"), "main");
        private final ModelPart root;
        private final ModelPart eagle_chick;
        private final ModelPart body;
        private final ModelPart head;
        private final ModelPart tail;
        private final ModelPart left_wing;
        private final ModelPart right_wing;
        private final ModelPart left_leg;
        private final ModelPart right_leg;

        public Baby(ModelPart root) {
            super(1.0f, 0.0F);
            this.root = root;
            this.eagle_chick = root.getChild("eagle_chick");
            this.body = this.eagle_chick.getChild("body");
            this.head = this.body.getChild("head");
            this.tail = this.body.getChild("tail");
            this.left_wing = this.body.getChild("left_wing");
            this.right_wing = this.body.getChild("right_wing");
            this.left_leg = this.eagle_chick.getChild("left_leg");
            this.right_leg = this.eagle_chick.getChild("right_leg");
        }

        public static LayerDefinition createLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition eagle_chick = partdefinition.addOrReplaceChild("eagle_chick", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

            PartDefinition body = eagle_chick.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 3).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(12, 14).addBox(-1.5F, -5.0F, -4.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 11).addBox(-0.5F, -2.0F, -4.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 7).addBox(-1.0F, -5.0F, 1.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -1.5F));

            PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 2.5F));

            PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -1.0F, -1.5F));

            PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 9).mirror().addBox(-5.0F, 0.0F, -1.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -1.0F, -1.5F));

            PartDefinition left_leg = eagle_chick.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 0).mirror().addBox(-1.5F, 2.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(20, 9).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(0, 2).addBox(-0.5F, 1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, 2.0F, -0.5F));

            PartDefinition right_leg = eagle_chick.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 0).addBox(-1.5F, 2.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(20, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 0).mirror().addBox(-0.5F, 1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.75F, 2.0F, -0.5F));

            return LayerDefinition.create(meshdefinition, 32, 32);
        }

        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            //──────────────────────────────────── Head Rotations ────────────────────────────────────
            this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
            this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);

            //──────────────────────────────────── Idle ────────────────────────────────────
            if(entity.isInWaterOrBubble())
                this.animate(entity.idleAnimationState, EagleAnimations.Baby.WALK, ageInTicks,
                        Primal_Util.Visuals.getSwimFactor(entity, 4f, 0.35f, 1.3f));
            else
                this.animate(entity.idleAnimationState, EagleAnimations.Baby.IDLE, ageInTicks, 1.0f);

            //──────────────────────────────────── Movement ────────────────────────────────────
            this.animateWalk(EagleAnimations.Baby.WALK, limbSwing, limbSwingAmount, 1.5f, 2.5f);

            //──────────────────────────────────── Sitting ────────────────────────────────────
            this.animate(entity.sittingAnimationState, EagleAnimations.Baby.SIT, ageInTicks, 1.0f);
        }

        @Override
        public @NotNull ModelPart root() {
            return this.root;
        }
    }
}