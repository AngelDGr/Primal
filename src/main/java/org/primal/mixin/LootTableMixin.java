package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LootTable.class)
public class LootTableMixin {

    @ModifyArg(method = "fill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private ItemStack primal$spawnSnakeFromChest(ItemStack itemstack, @Local(argsOnly = true)Container container, @Local(argsOnly = true)LootParams params){

        if(itemstack.is(Primal_Items.PLACEHOLDER_CHESTED_SNAKE)){
            if(params.getParamOrNull(LootContextParams.ORIGIN)!=null){
                var level = params.getLevel();
                var blockpos = BlockPos.containing(params.getParameter(LootContextParams.ORIGIN));
                SnakeEntity snake = Primal_Entities.SNAKE.get().create(params.getLevel());
                var snakeComponent = itemstack.get(Primal_Items.Components.SNAKE_SPAWN);

                if(snake!=null && snakeComponent!=null){

                    //Sets the variant of the component or from the biome
                    if(snakeComponent.variant()!=null) snake.setVariant(snakeComponent.variant());
                    else snake.setVariantFromBiome(snake, level.getBiome(blockpos));

                    //Set random effect
                    snake.setSnakeEffect(SnakeEntity.SnakeEffect.byId(level.getRandom().nextIntBetweenInclusive(0, SnakeEntity.SnakeEffect.values().length-1)));

                    snake.moveTo(blockpos.getX(), blockpos.getY()+1, blockpos.getZ());
                    level.addFreshEntity(snake);
                    snake.playSound(Primal_Sounds.SNAKE_IDLE.get());
                    //Possible banner pattern, 25% probability
                    if(snake.getRandom().nextFloat()<0.25){
                        snake.playSound(SoundEvents.ITEM_PICKUP);
                        BehaviorUtils.throwItem(snake, Primal_Items.SLITHER_BANNER_PATTERN.get().getDefaultInstance(), new Vec3(0, 1, 0));
                    }

                    //Attacks the entity that opened the chest
                    if(params.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity player){
                        //Trigger advancement
                        if(player instanceof ServerPlayer serverPlayer)
                            Primal_Advancements.SNAKE_CHEST.get().trigger(serverPlayer);

                        snake.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
                    }
                }

                itemstack.shrink(1);

                return itemstack;
            }
        }

        return itemstack;
    }
}
