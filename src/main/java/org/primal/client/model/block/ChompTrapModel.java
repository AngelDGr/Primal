package org.primal.client.model.block;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.block.ChompTrapBlock;
import org.primal.block_entity.ChompTrapBlockEntity;
import org.primal.client.animation.block.ChompTrapAnimations;

@SuppressWarnings("FieldCanBeLocal, unused")
@OnlyIn(Dist.CLIENT)
public class ChompTrapModel extends HierarchicalBlockEntityModel<ChompTrapBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "chomp_trap"), "main");
    private final ModelPart root;
    private final ModelPart left_jaw;
    private final ModelPart right_jaw;

    public ChompTrapModel(ModelPart root) {
        this.root = root;
        this.left_jaw = root.getChild("left_jaw");
        this.right_jaw = root.getChild("right_jaw");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition left_jaw = partdefinition.addOrReplaceChild("left_jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-7.975F, -20.0F, -8.025F, 8.0F, 20.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.975F, 0.0F));

        PartDefinition right_jaw = partdefinition.addOrReplaceChild("right_jaw", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.025F, -20.0F, -8.025F, 8.0F, 20.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 23.975F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ChompTrapBlockEntity blockEntity, float partialTick, BlockState blockState, BlockPos blockPos, float ageInPartialTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        //──────────────────────────────────── Animation State Handling ────────────────────────────────────
        if(blockEntity.mustReset){
            blockEntity.transitionAnimationState.stop();
            blockEntity.transitionAnimationState.startIfStopped((int) ageInPartialTicks);
        }

        blockEntity.idleAnimationState.animateWhen(true, (int) ageInPartialTicks);

        //──────────────────────────────────── Transition ────────────────────────────────────
        if(blockState.getValue(ChompTrapBlock.OPEN)){

            //Particles
            if(isStateBetweenMS(blockEntity.transitionAnimationState, 250, 260)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 333, 343)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 416, 426)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 500, 510)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 583, 593)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 667, 677)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 750, 760)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 833, 843)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 916, 926)
                    || isStateBetweenMS(blockEntity.transitionAnimationState, 990, 999)
            ) spawnGroundParticles(blockEntity.getLevel(), blockEntity.getBlockPos());

            //Animation
            if(blockState.getValue(ChompTrapBlock.BROKEN)) this.animate(blockEntity.transitionAnimationState, ChompTrapAnimations.LOWER_BROKEN, ageInPartialTicks, 1.8f);
            else this.animate(blockEntity.transitionAnimationState, ChompTrapAnimations.LOWER, ageInPartialTicks, 1.8f);
        } else {
            //Particles
            if(isStateBetweenMS(blockEntity.transitionAnimationState, 208, 228))
                spawnChompParticles(blockEntity.getLevel(), blockEntity.getBlockPos());

            //Animation
            this.animate(blockEntity.transitionAnimationState, ChompTrapAnimations.SNAP, ageInPartialTicks, 2f);
        }

        //──────────────────────────────────── Pose (For reentering the world) ────────────────────────────────────
        if(!blockEntity.transitionAnimationState.isStarted()){
            if(blockState.getValue(ChompTrapBlock.BROKEN))
                this.animate(blockEntity.idleAnimationState, ChompTrapAnimations.BROKEN, ageInPartialTicks, 1.0f);
            else if (blockState.getValue(ChompTrapBlock.OPEN))
                this.animate(blockEntity.idleAnimationState, ChompTrapAnimations.LOWERED, ageInPartialTicks, 1.0f);
            else
                this.animate(blockEntity.idleAnimationState, ChompTrapAnimations.SNAPPED, ageInPartialTicks, 1.0f);
        }
    }

    private static void spawnChompParticles(Level level, BlockPos blockPos) {
        for (int i = 0; i < 12; i++) {
            double d0 = blockPos.getX() + (level.getRandom().nextDouble());
            double d1 = blockPos.getY() + level.getRandom().nextDouble();
            double d2 = blockPos.getZ() + (level.getRandom().nextDouble());

            double d3 = (level.getRandom().nextDouble() * 2.0 - 1.0) * 0.3;
            double d4 = 0.3 + level.getRandom().nextDouble() * 0.3;
            double d5 = (level.getRandom().nextDouble() * 2.0 - 1.0) * 0.3;

            level.addParticle(ParticleTypes.CRIT,
                    d0, d1 + 0.8, d2,
                    d3, d4, d5);
        }
    }

    private static void spawnGroundParticles(Level level, BlockPos blockPos) {
        if (level == null) return;

        var state = level.getBlockState(blockPos.below());
        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, state);

        double centerX = blockPos.getCenter().x;
        double centerY = blockPos.getY();
        double centerZ = blockPos.getCenter().z;

        for (int i = 0; i < 2; i++) {

            // Radius around block (adjust for spread)
            double radius = 0.7;

            double offsetX = (level.getRandom().nextDouble() * 2 - 1) * radius;
            double offsetZ = (level.getRandom().nextDouble() * 2 - 1) * radius;

            double x = centerX + offsetX;
            double y = centerY + (level.getRandom().nextDouble() * 0.5);
            double z = centerZ + offsetZ;

            double motionX = (level.getRandom().nextDouble() * 2 - 1) * 0.1;
            double motionY = 0.2 + level.getRandom().nextDouble() * 0.2;
            double motionZ = (level.getRandom().nextDouble() * 2 - 1) * 0.1;

            level.addParticle(particle, x, y, z, motionX, motionY, motionZ);
        }
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
}