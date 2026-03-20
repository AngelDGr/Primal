// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


//public class CustomModel<T extends Entity> extends EntityModel<T> {
//	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "custommodel"), "main");
//	private final ModelPart group2;
//	private final ModelPart left;
//	private final ModelPart right;
//
//	public CustomModel(ModelPart root) {
//		this.group2 = root.getChild("group2");
//		this.left = root.getChild("left");
//		this.right = root.getChild("right");
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition group2 = partdefinition.addOrReplaceChild("group2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.24F, -5.5F, -2.0F, 0.0F, 0.829F, 0.0F));
//
//		PartDefinition left = partdefinition.addOrReplaceChild("left",
//				CubeListBuilder.create(),
//				PartPose.offsetAndRotation(2.99F, -6.5F, 1.0F, 0.2967F, 0.3142F, -0.2094F));
//
//		PartDefinition left_horn_r1 = left.addOrReplaceChild("left_horn_r1", CubeListBuilder.create().texOffs(18, 54).addBox(-1.0F, 4.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//		.texOffs(12, 55).addBox(-1.0F, 0.5F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 0.0F, -1.1345F));
//
//		PartDefinition right = partdefinition.addOrReplaceChild("right",
//				CubeListBuilder.create(), PartPose.offsetAndRotation(-2.99F, -6.5F, 1.0F, 0.2967F, -0.3142F, 0.2094F));
//
//		PartDefinition right_horn_r1 = right.addOrReplaceChild("right_horn_r1", CubeListBuilder.create().texOffs(18, 54).mirror().addBox(-1.0F, 4.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
//		.texOffs(12, 55).mirror().addBox(-1.0F, 0.5F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 0.0F, 1.1345F));
//
//		return LayerDefinition.create(meshdefinition, 64, 64);
//	}
//
//	@Override
//	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//	}
//
//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		group2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//}