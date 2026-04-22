package org.primal.client.model.entity.replaced;

import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.primal.client.animation.replaced.WolfAnimations;
import org.primal.entity.replaced.WolfReplaced;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class WolfModel<T extends Wolf> extends AgeableHierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "wolf"), "main");
    private final ModelPart root;
    private final ModelPart dawg;
    private final ModelPart body;
    private final ModelPart mane;
    private final ModelPart tail;
    private final ModelPart head;
    private final ModelPart tongue;
    private final ModelPart right_front_leg;
    private final ModelPart left_front_leg;
    private final ModelPart left_back_leg;
    private final ModelPart right_back_leg;

    public WolfModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root=root;
        this.dawg = root.getChild("dawg");
        this.body = this.dawg.getChild("body");
        this.mane = this.body.getChild("mane");
        this.tail = this.body.getChild("tail");
        this.head = this.body.getChild("head");
        this.tongue = this.head.getChild("tongue");
        this.right_front_leg = this.dawg.getChild("right_front_leg");
        this.left_front_leg = this.dawg.getChild("left_front_leg");
        this.left_back_leg = this.dawg.getChild("left_back_leg");
        this.right_back_leg = this.dawg.getChild("right_back_leg");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition dawg = partdefinition.addOrReplaceChild("dawg", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = dawg.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 40).addBox(-3.5F, -4.0F, -8.0F, 7.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(0.0F, -7.0F, -8.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-3.5F, -4.0F, 0.0F, 7.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-3.5F, -4.0F, 2.8F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.2F))
                .texOffs(28, 0).addBox(-3.5F, -4.0F, -2.6F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.2F))
                .texOffs(28, 0).addBox(-3.5F, -4.0F, -8.0F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -12.0F, 1.5F));

        PartDefinition mane = body.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -4.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(30, 18).addBox(-5.0F, -5.0F, 4.0F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition cube_r1 = mane.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-9.0F, -11.0F, -7.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.0F, -6.0F, 7.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 34).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 40).addBox(-1.5F, 9.0F, 0.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 8.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 30).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 37).addBox(-6.0F, -1.0F, -2.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(22, 37).mirror().addBox(4.0F, -1.0F, -2.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(22, 34).addBox(-4.0F, -5.0F, -2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 34).mirror().addBox(2.0F, -5.0F, -2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(22, 21).mirror().addBox(2.0F, -7.0F, -2.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(22, 21).addBox(-5.0F, -7.0F, -2.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 11).addBox(-1.5F, 0.0F, -8.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -8.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 6).addBox(-2.0F, -11.0F, -3.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(7.0F, -2.0F, -1.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition tongue = head.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(37, 8).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -5.0F));

        PartDefinition right_front_leg = dawg.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(12, 34).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.475F, -8.0F, -3.0F));

        PartDefinition left_front_leg = dawg.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(12, 34).mirror().addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.475F, -8.0F, -3.0F));

        PartDefinition left_back_leg = dawg.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(12, 34).mirror().addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -8.0F, 7.0F));

        PartDefinition right_back_leg = dawg.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(12, 34).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -8.0F, 7.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        boolean canRun = (entity.isAggressive() || entity.walkAnimation.speed()>0.75) && !entity.isInWaterOrBubble() && !(entity.isLeashed() && entity.isInSittingPose());

        //──────────────────────────────────── Head Rotations ────────────────────────────────────
        this.head.xRot = Mth.clamp(headPitch, -entity.getMaxHeadXRot(), entity.getMaxHeadXRot()) * (float) (Math.PI / 180.0);
        this.head.yRot = Mth.clamp(netHeadYaw, -entity.getMaxHeadYRot(), entity.getMaxHeadYRot()) * (float) (Math.PI / 180.0);

        //──────────────────────────────────── Tail Depending on Health ────────────────────────────────────
        if(!entity.isInSittingPose() && entity.isTame()){
            double healthPercentage = entity.getHealth()/entity.getMaxHealth();

            //Max -80° -> 1.0
            //Min 20° -> 0.0
            // Clamp just in case
            healthPercentage = Math.max(0.0, Math.min(1.0, healthPercentage));

            // 0.0 -> 20°, 1.0 -> -80°
            double angleDeg = 40 +(canRun? 30:0) + (-80.0 - 20.0) * healthPercentage;

            tail.xRot= tail.xRot - (float) Math.toRadians(angleDeg);
        }

        //──────────────────────────────────── Movement ────────────────────────────────────
        if(!entity.isInWaterOrBubble())
            if (canRun)
                this.animateWalk(WolfAnimations.RUN, limbSwing, limbSwingAmount, 1.8f, 2.5f);
            else
                this.animateWalk(WolfAnimations.WALK, limbSwing, limbSwingAmount, 2.6f, 2.5f);

        //──────────────────────────────────── Idle & Swim ────────────────────────────────────
        if(entity.isInWaterOrBubble())
            this.animate(((WolfReplaced) (entity)).primal$idleAnimationState(), WolfAnimations.WALK, ageInTicks,
                    Primal_Util.Visuals.getSwimFactor(entity, 4f, 0.2f, 1.5f));
        else
            this.animate(((WolfReplaced) (entity)).primal$idleAnimationState(), WolfAnimations.IDLE, ageInTicks, 1.0f);

        //──────────────────────────────────── Water Shake ────────────────────────────────────
        this.animate(((WolfReplaced) (entity)).primal$wetAnimationState(), WolfAnimations.WET, ageInTicks, 1.0f);

        //──────────────────────────────────── Sitting Pose ────────────────────────────────────
        float sitSpeed = 1.0f;
        if (entity.getOwner() != null && !entity.getOwner().isSpectator()) {
            //Increase speed if the owner is nearby, cute detail
            float distance = entity.distanceTo(entity.getOwner());

            // Tunables
            float minSpeed = 0.6f;
            float maxSpeed = 2.0f;
            float maxDistance = 15.0f; // distance at which speed is minimal

            // Normalize distance (0 = close, 1 = far)
            float t = Math.min(distance / maxDistance, 1.0f);

            // Invert so closer = faster
            sitSpeed=maxSpeed - (t * (maxSpeed - minSpeed));
        }

        this.animate(((WolfReplaced) (entity)).primal$sitAnimationState(), WolfAnimations.SIT, ageInTicks, sitSpeed);

        //──────────────────────────────────── Baby Scale ────────────────────────────────────
        if (this.young) this.applyStatic(WolfAnimations.BABY_TRANSFORM);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}
