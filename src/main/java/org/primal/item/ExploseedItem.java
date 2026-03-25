package org.primal.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.misc.ExploseedEntity;
import org.primal.registry.Primal_Sounds;

public class ExploseedItem extends Item {

    public ExploseedItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(final Level world, final Player user, @NotNull final InteractionHand hand) {
        final ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), Primal_Sounds.EXPLOSEED_THROW.get(), SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));

        if (!world.isClientSide) {
            final ExploseedEntity exploseedEntity = new ExploseedEntity(world, user);
            exploseedEntity.setItem(itemStack);
            exploseedEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, 0.8f, 1.0f);
            world.addFreshEntity(exploseedEntity);
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
