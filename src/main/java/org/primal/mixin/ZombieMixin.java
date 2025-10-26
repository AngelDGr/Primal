package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.primal.entity.ai.goals.ZombieAttackEggGoal;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.SharkEntity;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Entities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

    @Unique
    Zombie p$THIS = (Zombie)(Object)this;

    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At(value = "HEAD"))
    private void primal$zombiesTryToDestroyEggs(CallbackInfo ci){
        this.goalSelector.addGoal(4, new ZombieAttackEggGoal(Primal_Blocks.CROCODILE_EGG.get(),this, 1.0, 3));
        this.goalSelector.addGoal(4, new ZombieAttackEggGoal(Primal_Blocks.EAGLE_EGG.get(),this, 1.0, 3));
    }

    @Unique
    boolean primal$primalJockeySpawned =false;

    @Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;setBaby(Z)V"))
    private void primal$setJockeysSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir){
        RandomSource randomsource = level.getRandom();

        if (spawnGroupData instanceof Zombie.ZombieGroupData zombie$zombiegroupdata && zombie$zombiegroupdata.canSpawnJockey) {
            //Read with Jack Black voice: SHARK JOCKEY
            if(p$THIS instanceof Drowned){
                if ((double)randomsource.nextFloat() < 0.03){
                    SharkEntity shark = Primal_Entities.SHARK.get().create(this.level());
                    if (shark != null) {
                        shark.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        shark.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                        shark.setSharkJockey(true);
                        this.startRiding(shark);
                        level.addFreshEntity(shark);
                        primal$primalJockeySpawned = true;
                    }
                }
            }

            //Read with Jack Black voice: BEAR JOCKEY
            if ((double)randomsource.nextFloat() < 0.03 && !primal$primalJockeySpawned){
                BearEntity bear = Primal_Entities.BEAR.get().create(this.level());
                if (bear != null) {
                    bear.setBaby(true);
                    bear.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    bear.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                    bear.setBearJockey(true);
                    this.startRiding(bear);
                    level.addFreshEntity(bear);
                    primal$primalJockeySpawned = true;
                }
            }
        }
    }

    @ModifyExpressionValue(method = "finalizeSpawn", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Zombie$ZombieGroupData;canSpawnJockey:Z"))
    private boolean primal$avoidOtherJockeySpawn(boolean canSpawnJockey){
        return canSpawnJockey && !primal$primalJockeySpawned;
    }
}
