package org.primal.client.model.helmet_decoration;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DeerAntlersModel<T extends LivingEntity> extends HumanoidModel<T> {

    public DeerAntlersModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition headRoot = meshdefinition.getRoot().getChild("head");

        headRoot.addOrReplaceChild("right",
                CubeListBuilder.create()
                        .texOffs(42, 13)
                        .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 21)
                        .addBox(-10.0F, -8.0F, -0.5F, 11.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.4F, -7.9F, -1.0F, -0.0117F, 0.2615F, 0.0421F));

        headRoot.addOrReplaceChild("left", CubeListBuilder.create()
                        .texOffs(42, 13)
                        .mirror()
                        .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 21).mirror()
                        .addBox(-1.0F, -8.0F, -0.5F, 11.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                        .mirror(false),
                PartPose.offsetAndRotation(3.4F, -7.9F, -1.0F, -0.0117F, -0.2615F, -0.0421F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}
