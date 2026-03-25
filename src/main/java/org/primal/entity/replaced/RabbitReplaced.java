package org.primal.entity.replaced;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Rabbit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.replaced.RabbitAnimations;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.IntFunction;

public class RabbitReplaced implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RabbitReplaced(){
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum PrimalVariant implements StringRepresentable {
        GRAY(0, "gray"),
        SAND(1, "sand"),
        NONE(99, "none");

        public static final Codec<RabbitReplaced.PrimalVariant> CODEC = StringRepresentable.fromEnum(RabbitReplaced.PrimalVariant::values);

        private static final IntFunction<PrimalVariant> BY_ID = ByIdMap.continuous(PrimalVariant::ordinal, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static RabbitReplaced.PrimalVariant byId(int id) {
            return BY_ID.apply(id);
        }

        final int id;

        private final String name;

        PrimalVariant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    @Override
    public EntityType<Rabbit> getReplacingEntityType() {
        return EntityType.RABBIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(RabbitAnimations.mainController(this));
    }

    @Nullable
    public Rabbit getEntityFromState(AnimationState<RabbitReplaced> state) {
        Entity entity = state.getData(DataTickets.ENTITY);
        if (!(entity instanceof Rabbit rabbit)) return null;
        return rabbit;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
