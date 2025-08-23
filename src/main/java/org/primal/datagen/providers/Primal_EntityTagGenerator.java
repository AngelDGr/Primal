package org.primal.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.tags.EntityTypeTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;

public final class Primal_EntityTagGenerator extends EntityTypeTagsProvider {

    public Primal_EntityTagGenerator(PackOutput output, CompletableFuture<Provider> provider,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull Provider provider) {
        this.tag(Primal_Tags.BEAR_HUNTABLE)
                .add(
                        EntityType.PLAYER,
                        EntityType.VILLAGER,
                        EntityType.PILLAGER,
                        EntityType.VINDICATOR,
                        EntityType.PIG,
                        EntityType.SHEEP,
                        EntityType.CHICKEN,
                        EntityType.COW,
                        EntityType.HORSE,
                        EntityType.DONKEY,
                        EntityType.LLAMA,
                        EntityType.RABBIT);

        this.tag(Primal_Tags.SHARK_HUNTABLE)
                .add(
                        EntityType.SQUID,
                        EntityType.GLOW_SQUID);

        this.tag(EntityTypeTags.AQUATIC)
                .add(Primal_Entities.SHARK.get());

        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(Primal_Entities.SHARK.get());
    }
}
