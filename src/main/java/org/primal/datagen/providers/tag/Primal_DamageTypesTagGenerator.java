package org.primal.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_DamageTypes;

import java.util.concurrent.CompletableFuture;

public class Primal_DamageTypesTagGenerator extends DamageTypeTagsProvider {

    public Primal_DamageTypesTagGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {

        this.tag(DamageTypeTags.NO_IMPACT)
                .add(Primal_DamageTypes.SHARK_TOOTH)
                .add(Primal_DamageTypes.THORNY_ACACIA)
                .add(Primal_DamageTypes.CHOMP_TRAP);

        this.tag(DamageTypeTags.IS_FALL)
                .add(Primal_DamageTypes.SHARK_TOOTH);

        this.tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(Primal_DamageTypes.SHARK_TOOTH)
                .add(Primal_DamageTypes.CHOMP_TRAP);
    }
}