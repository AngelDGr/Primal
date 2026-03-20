package org.primal.util.mob_types;

import net.minecraft.world.entity.LivingEntity;

public interface DetectsFartherAway {

    boolean isEntityTargetable(LivingEntity attacker, LivingEntity target);

    boolean isEntityAttackable(LivingEntity attacker, LivingEntity target);
}
