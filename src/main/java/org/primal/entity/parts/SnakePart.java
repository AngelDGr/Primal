package org.primal.entity.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SnakeEntity;

import javax.annotation.Nullable;
import java.util.List;

public class SnakePart extends PartEntity<SnakeEntity> {
        public final SnakeEntity parentMob;
        private final EntityDimensions size;
        public SnakePart(SnakeEntity parent, float width, float height) {
                super(parent);
                this.size = EntityDimensions.scalable(width, height);
                this.parentMob = parent;
                this.refreshDimensions();
        }

        @Override
        protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

        @Override
        protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

        @Override
        protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}

        @Override
        public void tick() {
                super.tick();
                if (!this.isRemoved()) {
                        this.pushEntities();
                }

                this.xo = this.getX();
                this.yo = this.getY();
                this.zo = this.getZ();
                this.xOld = this.getX();
                this.yOld = this.getY();
                this.zOld = this.getZ();
        }

        @Override
        public boolean isPickable() {
                return true;
        }

        @Override
        public boolean isPushable() {
                return true;
        }

        @Nullable
        @Override
        public ItemStack getPickResult() {
                return this.parentMob.getPickResult();
        }

        /**
         * Called when the entity is attacked.
         */
        @Override
        public boolean hurt(@NotNull DamageSource source, float amount) {
                return !this.isInvulnerableTo(source) && this.parentMob.hurt(source, amount) && (this.parentMob.isSlithering() && !this.parentMob.isBaby());
        }

        /**
         * Returns {@code true} if Entity argument is equal to this Entity
         */
        @Override
        public boolean is(@NotNull Entity entity) {
                return this == entity || this.parentMob == entity;
        }

        @Override
        public void push(@NotNull Entity entity) {
                if (entity != this.parentMob) {
                        super.push(entity);
                }
        }

        protected void doPush(Entity entity) {
                if(this.parentMob.isSlithering() && !this.parentMob.isBaby())
                        entity.push(this);
        }

        @Override
        public float maxUpStep() {
                return 1.0f;
        }

        protected void pushEntities() {
                if (this.level().isClientSide()) {
                        this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(),
                                        EntitySelector.pushableBy(this)).forEach(this::doPush);
                } else {

                        List<Entity> list = this.level().getEntities(this, this.getBoundingBox(),
                                EntitySelector.pushableBy(this)
                                //Doesn't push parent
                                .and(e-> e!=this.parentMob)
                                //Doesn't push subentities
                                .and(e -> !(e instanceof SnakePart))
                                .and(e-> !(e instanceof SnakeEntity snake) || snake.isAggressive() || snake.isSitting())
                        );
                        if (!list.isEmpty()) {
                                int i = this.level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
                                if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                                        int j = 0;

                                        for (Entity entity : list) {
                                                if (!entity.isPassenger()) {
                                                        j++;
                                                }
                                        }

                                        if (j > i - 1) {
                                                this.hurt(this.damageSources().cramming(), 6.0F);
                                        }
                                }

                                for (Entity entity1 : list) {
                                        this.doPush(entity1);
                                }
                        }
                }
        }

        @Override
        public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity entity) {
                throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
                return this.size;
        }

        @Override
        public boolean shouldBeSaved() {
                return false;
        }
}