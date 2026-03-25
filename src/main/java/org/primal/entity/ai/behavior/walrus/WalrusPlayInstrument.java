package org.primal.entity.ai.behavior.walrus;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.WalrusEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.sounds.WalrusSong;

public class WalrusPlayInstrument extends Behavior<WalrusEntity> {

    private WalrusSong song=null;
    public WalrusPlayInstrument() {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.HAS_INSTRUMENT.get(), MemoryStatus.VALUE_PRESENT
        ), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull WalrusEntity walrus) {
        song = WalrusSong.byId(walrus.getSongId());
        return song!=null;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull WalrusEntity walrus, long gameTime) {
        return hasRequiredMemories(walrus);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull WalrusEntity walrus, long gameTime) {
        walrus.setPose(Pose.ROARING);
        walrus.playSound(song.soundEvent.get(), 1f, 1f);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull WalrusEntity walrus, long gameTime) {
        walrus.stopMoving();
        walrus.setPose(Pose.ROARING);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull WalrusEntity walrus, long gameTime) {
        walrus.setPose(Pose.STANDING);
        BehaviorUtils.throwItem(walrus, walrus.getMainHandItem().copy(), walrus.position().add(0, 1, 0));
        walrus.getMainHandItem().shrink(1);
    }
}
