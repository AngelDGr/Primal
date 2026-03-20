package org.primal.entity.replaced;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Dolphin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.replaced.DolphinAnimations;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.IntFunction;

public class DolphinReplaced implements GeoReplacedEntity {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        LUKEWARM(0, "lukewarm"),
        WARM(1, "warm"),
        COLD(2, "cold");

        public static final Codec<DolphinReplaced.Variant> CODEC = StringRepresentable.fromEnum(DolphinReplaced.Variant::values);

        private static final IntFunction<DolphinReplaced.Variant> BY_ID = ByIdMap.continuous(DolphinReplaced.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static DolphinReplaced.Variant byId(int id) {
            return BY_ID.apply(id);
        }

        final int id;

        private final String name;

        Variant(int id, String name) {
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

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DolphinReplaced(){
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public EntityType<Dolphin> getReplacingEntityType() {
        return EntityType.DOLPHIN;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DolphinAnimations.mainController(this));
    }

    @Nullable
    public Dolphin getEntityFromState(AnimationState<DolphinReplaced> state) {
        Entity entity = state.getData(DataTickets.ENTITY);
        if (!(entity instanceof Dolphin dolphin)) return null;
        return dolphin;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
