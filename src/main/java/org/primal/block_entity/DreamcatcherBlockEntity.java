package org.primal.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.block.DreamcatcherBlock;
import org.primal.injection.BedBlockHasDreamcatcher;
import org.primal.registry.Primal_Particles;
import org.primal.registry.Primal_Sounds;
import org.primal.util.block_types.AnimatableBlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class DreamcatcherBlockEntity extends BlockEntity implements AnimatableBlockEntity {
    public DreamcatcherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public int hasMobNear=0;
    private boolean hasBedNear=false;
    @Nullable
    private LivingEntity destroyTarget;
    @Nullable
    private UUID destroyTargetUUID;
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("HasMobNear", this.hasMobNear);
        tag.putBoolean("HasBedNear", this.hasBedNear);
        if (this.destroyTarget != null) {
            tag.putUUID("Target", this.destroyTarget.getUUID());
        }
        tag.putInt("doAttackTimer", this.doAttackTimer);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.hasMobNear = tag.getInt("HasMobNear");
        this.hasBedNear = tag.getBoolean("HasBedNear");
        if (tag.hasUUID("Target")) {
            this.destroyTargetUUID = tag.getUUID("Target");
        } else {
            this.destroyTargetUUID = null;
        }
        this.doAttackTimer = tag.getInt("doAttackTimer");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookup) {
        this.loadAdditional(pkt.getTag(), lookup);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        var dreamcatcher = this;
        prevAnimationTick = animationTick;
        animationTick++;

        Vec3 center = Vec3.atBottomCenterOf(pos);

        updateClientTarget(level, pos, dreamcatcher);
        animationTick(level, dreamcatcher.destroyTarget);

        if(dreamcatcher.hasBedNear) {
            //Particles
            if (level.getGameTime() % 5 == 0) {

                Direction direction = state.getValue(DreamcatcherBlock.FACING);
                boolean hanging = state.getValue(DreamcatcherBlock.HANGING);

                double offset = 0.45;

                double px = center.x + (!hanging? direction.getStepX() * offset: 0);
                double pz = center.z + (!hanging? direction.getStepZ() * offset: 0);

                level.addParticle(
                        ParticleTypes.MYCELIUM,
                        px + (direction.getStepX()==0? (level.random.nextGaussian()* (0.25)): 0),
                        center.y+0.5 + (level.random.nextGaussian()*0.25),
                        pz + (direction.getStepZ()==0? (level.random.nextGaussian() * (0.25)): 0),
                        level.random.nextGaussian() * (0.75),
                        level.random.nextGaussian() * (0.75),
                        level.random.nextGaussian() * (0.75));

                level.addParticle(
                        Primal_Particles.DREAMCATCHER_ASH,
                        px + (direction.getStepX()==0? (level.random.nextGaussian()*0.15): 0),
                        center.y + (level.random.nextGaussian()*0.10),
                        pz + (direction.getStepZ()==0? (level.random.nextGaussian()*0.15): 0),
                        0.0,
                        dreamcatcher.hasMobNear==2? 4.5f: dreamcatcher.hasMobNear==1? 2.5f: 0.75f,
                        0.0);
            }
        }
    }

    private static void animationTick(Level level, @Nullable Entity entity) {

        if (entity != null) {
            Vec3 vec31 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            level.addParticle(Primal_Particles.DREAMCATCHER_ASH,
                    vec31.x + level.random.nextGaussian() * (0.5F),
                    vec31.y+ level.random.nextGaussian() * (0.35F),
                    vec31.z+ level.random.nextGaussian() * (0.5F),
                    0,
                    0,
                    0);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DreamcatcherBlockEntity dreamcatcher) {
        Vec3 center = Vec3.atBottomCenterOf(pos);

        if(dreamcatcher.doAttackTimer>0){
            dreamcatcher.doAttackTimer--;
            dreamcatcher.changeDoAttack(dreamcatcher.doAttackTimer, state, pos);
        }

        if (level.getGameTime() % 40L == 0L) {
            updateDestroyTarget(level, pos, state, dreamcatcher);
        }

        //At less than 8x5x8 blocks
        AABB range = new AABB(
                center.x() - 8.0, center.y() - 5.0, center.z() - 8.0,
                center.x() + 8.0, center.y() + 5.0, center.z() + 8.0
        );

        List<Monster> monsters = level.getEntitiesOfClass(
                Monster.class,
                range,
                m -> !(m instanceof ZombifiedPiglin piglin) || piglin.isAngry()
        );

        boolean near = false;
        boolean veryNear = false;

        for (Monster m : monsters) {
            near = true;
            //At less than 4x2x4 blocks
            if (Math.abs(m.getX() - center.x()) <= 4 &&
                    Math.abs(m.getY() - center.y()) <= 2 &&
                    Math.abs(m.getZ() - center.z()) <= 4) {
                veryNear = true;
                break;
            }
        }

        int wantedState = veryNear ? 2 : near ? 1 : 0;

        if(wantedState != dreamcatcher.hasMobNear) {
            dreamcatcher.hasMobNear=wantedState;
            dreamcatcher.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }

        int radius = 3;

        boolean wantedBed = false;
        for (BlockPos checkPos : BlockPos.betweenClosed(
                pos.offset(-radius, -radius, -radius),
                pos.offset(radius, radius, radius))) {

            BlockState nearbyState = level.getBlockState(checkPos);

            if (nearbyState.is(BlockTags.BEDS)) {
                if (nearbyState.hasProperty(BedBlock.PART)) {
                    if (nearbyState.getValue(BedBlock.PART) == BedPart.FOOT) {
                        Direction dir = nearbyState.getValue(BedBlock.FACING);
                        checkPos = checkPos.relative(dir); // move to HEAD
                    }
                }

                if (level.getBlockEntity(checkPos) instanceof BedBlockHasDreamcatcher bed) {
                    if (!bed.primal$hasDreamcatcher()) {
                        bed.primal$setDreamcatcher(true);
                    }
                    wantedBed = true;
                }
                break;
            }
        }

        if(wantedBed != dreamcatcher.hasBedNear){
            dreamcatcher.hasBedNear=wantedBed;
            dreamcatcher.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private static void updateClientTarget(Level level, BlockPos pos, DreamcatcherBlockEntity blockEntity) {
        if (blockEntity.destroyTargetUUID == null) {
            blockEntity.destroyTarget = null;
        } else if (blockEntity.destroyTarget == null || !blockEntity.destroyTarget.getUUID().equals(blockEntity.destroyTargetUUID)) {
            blockEntity.destroyTarget = findDestroyTarget(level, pos, blockEntity.destroyTargetUUID);
            if (blockEntity.destroyTarget == null) {
                blockEntity.destroyTargetUUID = null;
            }
        }
    }

    private static void updateDestroyTarget(Level level, BlockPos pos, BlockState state, DreamcatcherBlockEntity blockEntity) {
        LivingEntity livingentity = blockEntity.destroyTarget;

        if (blockEntity.destroyTarget == null && blockEntity.destroyTargetUUID != null) {
            blockEntity.destroyTarget = findDestroyTarget(level, pos, blockEntity.destroyTargetUUID);
            blockEntity.destroyTargetUUID = null;
        } else if (blockEntity.destroyTarget == null) {
            List<LivingEntity> list = level.getEntitiesOfClass(
                    LivingEntity.class, getDestroyRangeAABB(pos), livingEntity -> livingEntity instanceof Phantom
            );
            if (!list.isEmpty()) {
                blockEntity.destroyTarget = list.get(level.random.nextInt(list.size()));
            }
        } else if (!blockEntity.destroyTarget.isAlive() || !pos.closerThan(blockEntity.destroyTarget.blockPosition(), 8.0)) {
            blockEntity.destroyTarget = null;
        }

        if (blockEntity.destroyTarget != null) {
            level.playSound(
                    null,
                    blockEntity.destroyTarget.getX(),
                    blockEntity.destroyTarget.getY(),
                    blockEntity.destroyTarget.getZ(),
                    Primal_Sounds.DREAMCATCHER_ATTACK_TARGET,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );
            blockEntity.destroyTarget.hurt(level.damageSources().magic(), 4.0F);
            blockEntity.changeDoAttack(10, state, pos);
        }

        if (livingentity != blockEntity.destroyTarget) {
            level.sendBlockUpdated(pos, state, state, 2);
        }
    }

    public int doAttackTimer=0;
    public void changeDoAttack(int doAttack, BlockState state, BlockPos pos) {
        this.doAttackTimer =doAttack;
        this.setChanged();
        if(this.getLevel()!=null) this.getLevel().sendBlockUpdated(pos, state, state, 3);
    }

    @Nullable
    private static LivingEntity findDestroyTarget(Level level, BlockPos pos, UUID targetId) {
        List<LivingEntity> list = level.getEntitiesOfClass(
                LivingEntity.class, getDestroyRangeAABB(pos), livingEntity -> livingEntity.getUUID().equals(targetId)
        );
        return list.size() == 1 ? list.getFirst() : null;
    }

    private static AABB getDestroyRangeAABB(BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        return new AABB(i, j, k, i + 1, j + 1, k + 1).inflate(12.0);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (level == null || level.isClientSide || !level.isLoaded(worldPosition)) return;

        int radius = 3;

        //Erases the state
        for (BlockPos checkPos : BlockPos.betweenClosed(
                worldPosition.offset(-radius, -radius, -radius),
                worldPosition.offset(radius, radius, radius))) {

            if (level.getBlockEntity(checkPos) instanceof BedBlockHasDreamcatcher bed) {
                if (bed.primal$hasDreamcatcher()) {
                    bed.primal$setDreamcatcher(false);
                }
            }
        }
    }

    //──────────────────────────────────── Visuals ────────────────────────────────────
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shakeAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    private float animationTick = 0;
    private float prevAnimationTick = 0;

    /** Smooth animation time in ticks with partial-tick interpolation. */
    public float getAnimationTime(float partialTick) {
        return prevAnimationTick + (animationTick - prevAnimationTick) * partialTick;
    }
}
