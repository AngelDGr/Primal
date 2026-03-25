package org.primal.server_data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConchShellsData extends SavedData {

    private final Map<UUID, GlobalPos> pets = new HashMap<>();
    private static final String DATA_NAME = Primal_Main.MOD_ID + "_pets_conch_shell";

    ConchShellsData(){}

    public static ConchShellsData create() {
        return new ConchShellsData();
    }

    public Map<UUID, GlobalPos> getPets() {
        return pets;
    }

    public boolean hasPet(Entity pet){
        return this.pets.containsKey(pet.getUUID());
    }

    public void removePet(Entity pet){
        this.pets.remove(pet.getUUID());
        setDirty();
    }

    public void addPet(Entity pet){
        this.pets.put(pet.getUUID(), GlobalPos.of(pet.level().dimension(), pet.blockPosition()));
        setDirty();
    }

    public BlockPos getPos(Entity pet){
        return this.pets.get(pet.getUUID()).pos();
    }

    public BlockPos getPos(UUID pet){
        return this.pets.get(pet).pos();
    }

    public ResourceKey<Level> getDimension(UUID pet){
        return this.pets.get(pet).dimension();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        // Write data to tag
        tag.put("HandledPets", this.savePets());
        return tag;
    }

    private ListTag savePets() {
        ListTag allPetsTag = new ListTag();

        for(UUID petUUID : pets.keySet()) {
            CompoundTag petTag = new CompoundTag();
            CompoundTag blockPosTag = new CompoundTag();

            blockPosTag.putString("Dimension", pets.get(petUUID).dimension().location().toString());
            blockPosTag.putInt("X", pets.get(petUUID).pos().getX());
            blockPosTag.putInt("Y", pets.get(petUUID).pos().getY());
            blockPosTag.putInt("Z", pets.get(petUUID).pos().getZ());

            petTag.putUUID("UUID", petUUID);
            petTag.put("BlockPos", blockPosTag);

            allPetsTag.add(petTag);
        }

        return allPetsTag;
    }

    public static ConchShellsData load(CompoundTag tag) {
        ConchShellsData data = ConchShellsData.create();

        if (tag.contains("HandledPets", Tag.TAG_LIST)) {
            ListTag list = tag.getList("HandledPets", Tag.TAG_COMPOUND);

            for (Tag t : list) {
                CompoundTag pet = (CompoundTag) t;

                UUID petUUUID = pet.getUUID("UUID");
                CompoundTag posTag = (CompoundTag) pet.get("BlockPos");

                if(posTag!=null){
                    ResourceKey<Level> dimensionId = ResourceKey.create(
                            Registries.DIMENSION,
                            ResourceLocation.tryParse(posTag.getString("Dimension"))
                    );
                    int x = posTag.getInt("X");
                    int y = posTag.getInt("Y");
                    int z = posTag.getInt("Z");

                    BlockPos petBlockPos = new BlockPos(x, y, z);

                    data.pets.put(petUUUID, GlobalPos.of(dimensionId, petBlockPos));
                }
            }
        }

        return data;
    }

    public static ConchShellsData get(ServerLevel level) {
        ServerLevel overworld = level.getServer().overworld();

        DimensionDataStorage storage = overworld.getDataStorage();
        return storage.computeIfAbsent(ConchShellsData::load, ConchShellsData::new, DATA_NAME);
    }

    public record EntityIdentifier(UUID uuid, int clientId){
        public static EntityIdentifier of(UUID uuid, int clientId){
            return new EntityIdentifier(uuid, clientId);
        }
        public static EntityIdentifier of(Entity entity){
            return EntityIdentifier.of(entity.getUUID(), entity.getId());
        }
    }
}
