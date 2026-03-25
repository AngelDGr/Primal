package org.primal.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_GameEventTagsGenerator extends IntrinsicHolderTagsProvider<GameEvent> {

    public Primal_GameEventTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.GAME_EVENT, lookupProvider, (gameEvent) -> gameEvent.builtInRegistryHolder().key(), Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(Primal_Tags.GameEvent.SCARE_DEER)
                .add(GameEvent.LIGHTNING_STRIKE)
                .add(GameEvent.INSTRUMENT_PLAY)
                .add(GameEvent.PROJECTILE_LAND)
                .add(GameEvent.PRIME_FUSE)
                .add(GameEvent.EXPLODE)
        ;
    }
}