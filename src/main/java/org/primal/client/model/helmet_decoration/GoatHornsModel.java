package org.primal.client.model.helmet_decoration;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class GoatHornsModel<T extends LivingEntity> extends HumanoidModel<T> {

    public GoatHornsModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition headRoot = meshdefinition.getRoot().getChild("head");

        PartDefinition left = headRoot.addOrReplaceChild("left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.99F, -6.5F, 1.0F, 0.2967F, 0.3142F, -0.2094F));

        left.addOrReplaceChild("left_horn_r1",
                CubeListBuilder.create().texOffs(18, 54)
                        .addBox(-1.0F, 4.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 55)
                        .addBox(-1.0F, 0.5F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 0.0F, -1.1345F));

        PartDefinition right = headRoot.addOrReplaceChild("right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.99F, -6.5F, 1.0F, 0.2967F, -0.3142F, 0.2094F));

        right.addOrReplaceChild("right_horn_r1",
                CubeListBuilder.create().texOffs(18, 54)
                        .mirror()
                        .addBox(-1.0F, 4.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .mirror(false)
                        .texOffs(12, 55).mirror()
                        .addBox(-1.0F, 0.5F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                        .mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 0.0F, 1.1345F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}
