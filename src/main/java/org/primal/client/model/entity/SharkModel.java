package org.primal.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
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
import org.primal.client.animation.entity.SharkAnimations;
import org.primal.entity.animal.SharkEntity;
import org.primal.util.Primal_Util;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class SharkModel<T extends SharkEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "shark"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart tail;
    private final ModelPart tail_tip;
	private final ModelPart fin;
	private final ModelPart right_fin;
	private final ModelPart left_fin;
	private final ModelPart left_little_fin;
	private final ModelPart right_little_fin;

	public SharkModel(ModelPart root) {
		this.root=  root;
		this.body = root.getChild("body");
		this.tail = this.body.getChild("tail");
		this.tail_tip = this.tail.getChild("tail_tip");
		this.fin = this.body.getChild("fin");
		this.right_fin = this.body.getChild("right_fin");
		this.left_fin = this.body.getChild("left_fin");
		this.left_little_fin = this.body.getChild("left_little_fin");
		this.right_little_fin = this.body.getChild("right_little_fin");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -5.0F, -9.0F, 11.0F, 13.0F, 19.0F, new CubeDeformation(0.0F))
		.texOffs(42, 9).addBox(-1.0F, -8.0F, 3.0F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 45).addBox(-4.5F, 0.0F, -23.0F, 9.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-11.0F, 3.975F, -25.0F, 22.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(62, 34).addBox(-4.5F, -2.0F, -23.0F, 9.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, -1.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(46, 45).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 10.0F));

		PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(62, 8).addBox(-1.0F, -12.0F, -2.0F, 2.0F, 20.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(78, 8).addBox(-1.0F, -5.0F, -2.0F, 2.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 67).addBox(-1.0F, -12.0F, 4.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(91, 21).addBox(-1.0F, -5.0F, 4.0F, 2.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 67).addBox(-1.0F, 4.0F, 4.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition fin = body.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(14, 67).addBox(-1.0F, -11.0F, 1.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(46, 62).addBox(-1.0F, -7.0F, -3.0F, 2.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition right_fin = body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-11.0F, -0.5F, -3.5F, 11.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 7.5F, -2.5F));

		PartDefinition left_fin = body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(60, 0).addBox(0.0F, -0.5F, -3.5F, 11.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 7.5F, -2.5F));

		PartDefinition left_little_fin = body.addOrReplaceChild("left_little_fin", CubeListBuilder.create().texOffs(62, 40).addBox(0.0F, 0.0F, -1.5F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 8.0F, 8.5F));

		PartDefinition right_little_fin = body.addOrReplaceChild("right_little_fin", CubeListBuilder.create().texOffs(62, 40).mirror().addBox(-4.0F, 0.0F, -1.5F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 8.0F, 7.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public static float a(float anim_time){
		return (float) (-Math.sin(Math.toRadians((anim_time -0.3f) * 180f)) * 2);
	}

	public static float b(float anim_time){
		return (float) (-Math.sin(Math.toRadians((anim_time -0.5) * 180)) * 7);
	}

	public static float c(float anim_time){
		return (float) (Math.sin(Math.toRadians((anim_time -0.1) * 180)) * -4);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		//──────────────────────────────────── Fish Rotations ────────────────────────────────────
		this.body.xRot = headPitch * (float) (Math.PI / 180.0);
		this.body.yRot = netHeadYaw * (float) (Math.PI / 180.0);
		if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7)
			this.body.xRot = this.body.xRot + (-0.05F - 0.05F * Mth.cos(ageInTicks * 0.3F));

		if(entity.shouldBeBeached()){
			this.body.yRot = 0;
			this.body.xRot = 0;
		}

		//──────────────────────────────────── Movement ────────────────────────────────────
		if(entity.isInWaterOrBubble())
			this.animate(entity.idleAnimationState, SharkAnimations.SWIM, ageInTicks, Primal_Util.Visuals.getSwimFactor(entity, 15f, 0.3f, 4f));

		//──────────────────────────────────── Idle, Swim & Shake ────────────────────────────────────
		if(entity.isAlive())
			this.animate(entity.shakeAnimationState, SharkAnimations.SHAKE, ageInTicks, 1.5f);

		//──────────────────────────────────── Beached ────────────────────────────────────
		float move = Mth.clamp(entity.walkAnimation.speed(), 0f, 1f);
		this.animate(entity.beachedAnimationState, SharkAnimations.BEACHED, ageInTicks, 1);
		this.animate(entity.beachedShakeAnimationState, SharkAnimations.SHAKE, ageInTicks, move);
	}

	@Override
	public @NotNull ModelPart root() {
		return this.root;
	}
}