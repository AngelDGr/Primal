package org.primal.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_GameEventTagsGenerator extends TagsProvider<GameEvent> {

    public Primal_GameEventTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.GAME_EVENT, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(Primal_Tags.GameEvent.SCARE_DEER)
                .add(GameEvent.LIGHTNING_STRIKE.key())
                .add(GameEvent.INSTRUMENT_PLAY.key())
                .add(GameEvent.PROJECTILE_LAND.key())
                .add(GameEvent.PRIME_FUSE.key())
                .add(GameEvent.EXPLODE.key())
        ;
    }
}