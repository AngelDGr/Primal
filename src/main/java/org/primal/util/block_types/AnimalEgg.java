package org.primal.util.block_types;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_Sounds;

public interface AnimalEgg {

    IntegerProperty HATCH = BlockStateProperties.HATCH;

    default void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity, int chance, Block block, IntegerProperty eggsProperty) {
        if (this.canDestroyEgg(level, entity)) {
            if (!level.isClientSide && level.random.nextInt(chance) == 0 && state.is(block)) {
                this.decreaseEggs(level, pos, state, eggsProperty);
            }
        }
    }

    default void decreaseEggs(Level level, BlockPos pos, BlockState state, @Nullable IntegerProperty eggsProperty) {
        level.playSound(null, pos, Primal_Sounds.EGG_BREAK.get(), SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        if(eggsProperty != null) {
            int i = state.getValue(eggsProperty);
            if (i <= 1) {
                level.destroyBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(eggsProperty, i - 1), 2);
                level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
                level.levelEvent(2001, pos, Block.getId(state));
            }
        } else {
            level.destroyBlock(pos, false);
        }
    }

    RegistryObject<? extends EntityType<? extends Animal>> getAnimal();

    default void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random, @Nullable IntegerProperty eggsProperty) {
        if (shouldUpdateHatchLevel(level)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                level.playSound(null, pos, Primal_Sounds.EGG_CRACK.get(), SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.setBlock(pos, state.setValue(HATCH, i + 1), 2);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
            } else {
                level.playSound(null, pos, Primal_Sounds.EGG_HATCH.get(), SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.removeBlock(pos, false);
                level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));

                int eggAmount = eggsProperty!=null? state.getValue(eggsProperty): 1;

                for (int j = 0; j < eggAmount; j++) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    Animal animal = this.getAnimal().get().create(level);
                    if (animal != null) {
                        animal.setAge(-24000);
                        animal.moveTo((double)pos.getX() + 0.3 + (double)j * 0.2, pos.getY(), (double)pos.getZ() + 0.3, 0.0F, 0.0F);
                        level.addFreshEntity(animal);
                        animal.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.BREEDING, null, null);
                    }
                }
            }
        }
    }

    static boolean shouldUpdateHatchLevel(Level level) {
        float f = level.getTimeOfDay(1.0F);
        return (double) f < 0.69 && (double) f > 0.65 || level.random.nextInt(500) == 0;
    }

    private boolean canDestroyEgg(Level level, Entity entity) {
        if (entityCannotDestroyEgg(entity)) {
            return false;
        } else {
            return entity instanceof LivingEntity && (entity instanceof Player || ForgeEventFactory.getMobGriefingEvent(level, entity));
        }
    }

    boolean entityCannotDestroyEgg(Entity entity);
}
