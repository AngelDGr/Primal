package org.primal.client.model.block;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.block.DreamcatcherBlock;
import org.primal.block_entity.DreamcatcherBlockEntity;
import org.primal.client.animation.block.DreamcatcherAnimations;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class DreamcatcherModel extends HierarchicalBlockEntityModel<DreamcatcherBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "dreamcatcher"), "main");
    private final ModelPart root;
    private final ModelPart catcher;
    private final ModelPart antler;
    private final ModelPart rope;
    private final ModelPart string;

    public DreamcatcherModel(ModelPart root) {
        this.root = root;
        this.catcher = root.getChild("catcher");
        this.antler = this.catcher.getChild("antler");
        this.rope = this.antler.getChild("rope");
        this.string = this.catcher.getChild("string");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition catcher = partdefinition.addOrReplaceChild("catcher", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, -7.075F));

        PartDefinition antler = catcher.addOrReplaceChild("antler", CubeListBuilder.create().texOffs(24, 12).addBox(0.0F, 6.0F, -1.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -4.0F, -1.0F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = antler.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 12).addBox(9.0F, -8.0F, -1.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -14.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r2 = antler.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, -12.0F, -1.0F, 12.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -8.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition rope = antler.addOrReplaceChild("rope", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition cube_r3 = rope.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(31, 19).addBox(1.0F, -12.0F, 1.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -6.0F, -1.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition string = catcher.addOrReplaceChild("string", CubeListBuilder.create().texOffs(0, 24).addBox(-4.5F, 0.0F, 0.0F, 9.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(DreamcatcherBlockEntity blockEntity, float partialTick, BlockState blockState, BlockPos blockPos, float ageInPartialTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        rope.visible = blockState.getValue(DreamcatcherBlock.HANGING);

        //──────────────────────────────────── Animation State Handling ────────────────────────────────────
        blockEntity.shakeAnimationState.animateWhen(blockEntity.hasMobNear>0, (int) ageInPartialTicks);

        blockEntity.attackAnimationState.animateWhen(blockEntity.doAttackTimer>0, (int) ageInPartialTicks);

        blockEntity.idleAnimationState.animateWhen(!blockEntity.shakeAnimationState.isStarted(), (int) ageInPartialTicks);

        //──────────────────────────────────── Shake ────────────────────────────────────
        float shakeSpeed = 0.6f;
        if(blockEntity.hasMobNear==2) shakeSpeed = 1.0f;
        this.animate(blockEntity.shakeAnimationState, DreamcatcherAnimations.SHAKE, ageInPartialTicks, shakeSpeed);

        //──────────────────────────────────── Idle ────────────────────────────────────
        var level = blockEntity.getLevel();
        float idleSpeed = 1.0f;

        if(level!=null && level.canSeeSky(blockPos.above()))
            if (level.isThundering()) idleSpeed = 4.0f;
            else if (level.isRaining()) idleSpeed = 2.0f;

        this.animate(blockEntity.idleAnimationState, DreamcatcherAnimations.IDLE, ageInPartialTicks, idleSpeed);

        //──────────────────────────────────── Attack ────────────────────────────────────
        this.animate(blockEntity.attackAnimationState, DreamcatcherAnimations.ATTACK, ageInPartialTicks, 1.0f);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}