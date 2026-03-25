package org.primal.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.item.ConchShellClientExtension;
import org.primal.item.component.ConchShellComponent;
import org.primal.networking.DelayedTasks;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_Sounds;
import org.primal.server_data.ConchShellsData;
import org.primal.util.Primal_Util;

import java.util.List;
import java.util.function.Consumer;

public class ConchShellItem extends Item {

    public static final int MAX_DURATION = 72000;
    public static final int RELEASE_TIME = 80;

    public ConchShellItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand usedHand) {
        if(target instanceof OwnableEntity ownedEntity && ownedEntity.getOwner()!=null && ownedEntity.getOwner().equals(player) && !Primal_Util.OneTwentyEquivalent.Components.has(stack, Primal_Items.Components.CONCH_SHELL)){
            if (player.level() instanceof ServerLevel serverLevel) {
                assignPet(serverLevel, stack, target);

                serverLevel.playSound(
                        null,
                        player.blockPosition(),
                        Primal_Sounds.PET_BIND.get(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.8f
                );
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemstack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeCharged) {
        if(livingEntity instanceof Player player){
            var conchShellComponent = Primal_Util.OneTwentyEquivalent.Components.get(itemstack, Primal_Items.Components.CONCH_SHELL);

            if(conchShellComponent!=null && (ConchShellItem.MAX_DURATION - timeCharged)>= ConchShellItem.RELEASE_TIME){

                Pair<Entity, GlobalPos> success;
                Entity pet;
                if(player.level() instanceof ServerLevel serverLevel){
                    success = doServerOperations(serverLevel, player, conchShellComponent, false);
                    if(success==null || player.getServer()==null) return;

                    pet = success.getFirst();
                    var petDimension = player.getServer().getLevel(success.getSecond().dimension());
                    var playerDimension = player.getServer().getLevel(player.level().dimension());
                    if(playerDimension==null) return;

                    var oldPetPos = success.getSecond().pos();
                    AABB entityBox = pet.getBoundingBox();

                    //Loads the chunk per 20 ticks each 1 tick
                    DelayedTasks.runEvery(1, 20, () -> loadChunk(petDimension, oldPetPos, true));

                    //Teleport later
                    DelayedTasks.runLater(10, () -> {
                        pet.stopRiding();
                        // Recreate the entity (ignore the existential dilemmas, vanilla does something similar)
                        Entity teleported = pet.getType().create(playerDimension);

                        if (teleported == null) return;

                        teleported.restoreFrom(pet);
                        pet.discard();
                        teleported.moveTo(player.position());
                        playerDimension.addFreshEntity(teleported);

                        // Update UUID
                        assignPet(serverLevel, itemstack, teleported);
                        teleported.playSound(SoundEvents.ENDERMAN_TELEPORT);
                    });

                    //Unload chunk later, just in case
                    DelayedTasks.runLater(30, () ->
                            loadChunk(petDimension, oldPetPos, false)
                    );

                    spawnParticleCircle(serverLevel, player.getX(), player.getY()+0.5f, player.getZ(), getEntityParticleRadius(entityBox),  0.3f);
                }

                float f = 256 / 16.0F;
                level.playSound(player, player, Primal_Sounds.CONCH_SHELL_CALL.get(), SoundSource.RECORDS, f, 1.0F);
                level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
                player.getCooldowns().addCooldown(this, 100);
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemstack, int remainingUseDuration) {
        if(livingEntity instanceof Player player){
            var conchShellComponent = Primal_Util.OneTwentyEquivalent.Components.get(itemstack, Primal_Items.Components.CONCH_SHELL);

            if(conchShellComponent!=null)
                if(player.level() instanceof ServerLevel serverLevel)
                    doServerOperations(serverLevel, player, conchShellComponent, true);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        var conchShellComponent = Primal_Util.OneTwentyEquivalent.Components.get(itemstack, Primal_Items.Components.CONCH_SHELL);

        if(conchShellComponent!=null){
            if(player.level() instanceof ServerLevel serverLevel){

                var success= doServerOperations(serverLevel, player, conchShellComponent, true);
                if(success==null) return InteractionResultHolder.fail(itemstack);

                player.startUsingItem(usedHand);
                return InteractionResultHolder.success(itemstack);
            }
        } else {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.pass(itemstack);
        }

        return InteractionResultHolder.pass(itemstack);
    }

    public static double getEntityParticleRadius(AABB box) {
        double halfWidth = Math.max(box.getXsize(), box.getZsize()) * 0.5;
        return halfWidth + 0.3; // margin so particles don't clip
    }

    public static void spawnParticleCircle(
            ServerLevel level,
            double centerX,
            double centerY,
            double centerZ,
            double radius,
            double spacing
    ) {
        int count = Math.max(8, (int) Math.ceil((Math.PI * 2 * radius) / spacing));
        double step = (Math.PI * 2) / count;

        for (int i = 0; i < count; i++) {

            var particle= i%2==0?  ParticleTypes.NAUTILUS: ParticleTypes.GLOW;
            double angle = i * step;
            double x = centerX + Math.cos(angle) * radius;
            double z = centerZ + Math.sin(angle) * radius;

            level.sendParticles(
                    particle,
                    x, centerY, z,
                    1,        // count
                    0, 0, 0,  // offset
                    0         // speed
            );
        }
    }

    public static void loadChunk(ServerLevel level, BlockPos worldPosition, boolean add) {
        try {
            // Force load the chunk containing this anchor
            level.setChunkForced(worldPosition.getX() >> 4, worldPosition.getZ() >> 4, add);
        } catch (Exception e) {
            // Handle any chunk loading errors gracefully
        }
    }

    private static @Nullable Pair<Entity, GlobalPos> doServerOperations(ServerLevel serverLevel, Player player, ConchShellComponent conchShellComponent, boolean loadChunk){
        var data = ConchShellsData.get(serverLevel);
        if(player.getServer() == null) return null;

        var dimensionLevel = player.getServer().getLevel(data.getDimension(conchShellComponent.pet()));
        if(dimensionLevel==null) return null;

        //Get pos from server stored data
        var pos = data.getPos(conchShellComponent.pet());
        if(pos==null) return null;

        //Loads chunk
        if(loadChunk){
            loadChunk(dimensionLevel, pos, true);

            //To unload the chunk automatically after 3s, to avoid too much forced chunks
            DelayedTasks.runLater(60, () ->
                    loadChunk(dimensionLevel, pos, false));
        }

        //Get the entity itself
        var pet = dimensionLevel.getEntity(conchShellComponent.pet());
        if(pet==null) return null;

        return new Pair<>(pet, GlobalPos.of(data.getDimension(conchShellComponent.pet()), pos));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return super.isFoil(itemStack) || Primal_Util.OneTwentyEquivalent.Components.has(itemStack, Primal_Items.Components.CONCH_SHELL);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        if(Primal_Util.OneTwentyEquivalent.Components.has(itemStack, Primal_Items.Components.CONCH_SHELL))
            return Rarity.RARE;

        return super.getRarity(itemStack);
    }

    public static ItemStack removePet(ItemStack stack){
        Primal_Util.OneTwentyEquivalent.Components.remove(stack, Primal_Items.Components.CONCH_SHELL);
        return stack;
    }

    public static void assignPet(ServerLevel serverLevel, ItemStack stack, Entity target){
        Primal_Util.OneTwentyEquivalent.Components.set(stack, new ConchShellComponent(target.getDisplayName(), target.getUUID()));
        ConchShellsData.get(serverLevel).addPet(target);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if(!Primal_Util.OneTwentyEquivalent.Components.has(stack, Primal_Items.Components.CONCH_SHELL))
            tooltipComponents.add(Component.translatable("item.primal.conch_shell.tooltip").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Primal_Util.OneTwentyEquivalent.Components.has(stack, Primal_Items.Components.CONCH_SHELL)? Component.translatable("item.primal.bound_conch_shell") : super.getName(stack);
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return Primal_Util.OneTwentyEquivalent.Components.has(stack, Primal_Items.Components.CONCH_SHELL);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return ConchShellItem.MAX_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ConchShellClientExtension());
    }
}
