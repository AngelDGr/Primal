package org.primal.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.primal.registry.Primal_Entities;
import org.primal.util.mob_types.AttackVillagers;
import org.primal.util.mob_types.PrimalTamable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {

    @Unique
    private static final float primal$distanceMinimumDanger = 8.0f;
    @Unique
    private static final float primal$distanceDanger = 12.0f;
    @Unique
    private static final float primal$distanceMediumDanger = 16.0f;
    @Unique
    private static final float primal$distanceExtremeDanger = 24.0f;


    @Shadow @Final @Mutable
    private static ImmutableMap<Object, Object> ACCEPTABLE_DISTANCE_FROM_HOSTILES;

    static {
                ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
                .putAll(ACCEPTABLE_DISTANCE_FROM_HOSTILES.entrySet())

                        .put(Primal_Entities.BEAR.get(), primal$distanceDanger)
                        .put(Primal_Entities.SHARK.get(), primal$distanceDanger)
                        .put(Primal_Entities.CROCODILE.get(), primal$distanceMediumDanger)
                        .put(Primal_Entities.CASSOWARY.get(), primal$distanceMediumDanger)
                        .put(Primal_Entities.LION.get(), primal$distanceMediumDanger)
                        .put(Primal_Entities.SNAKE.get(), primal$distanceMinimumDanger)

                        .build();
    }

    @ModifyReturnValue(method = "isMatchingEntity", at = @At("RETURN"))
    private boolean primal$dontFearTamedAnimals(boolean original, @Local(argsOnly = true, ordinal = 1) LivingEntity target){
        return original && primal$fearTamableAnimal(target);
    }

    @Unique
    private boolean primal$fearTamableAnimal(LivingEntity target){
        //If it is a primal pet and tamed -> No feared
        if(target instanceof TamableAnimal pet && pet instanceof PrimalTamable && pet.isTame()){
            //If it is pet but is attacking villagers -> Feared
            if(pet instanceof AttackVillagers attackVillagers && attackVillagers.isTargetingVillager())
                return true;

            return false;
        }

        //Feared by default (so it go to vanilla/other mods logic)
        return true;
    }
}
