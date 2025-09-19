package org.primal.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_Sounds;

//Declares a loopable sound
public class CrocodileThrashingSoundInstance extends AbstractTickableSoundInstance {
    private final CrocodileEntity crocodile;
    private int timer;

    public CrocodileThrashingSoundInstance(final CrocodileEntity crocodile) {
        super(Primal_Sounds.CROCODILE_THRASH.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.crocodile = crocodile;
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.delay = 0;
        this.timer=0;
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    @Override
    public void tick() {
        ++timer;

        if ((this.crocodile.isRemoved() || !crocodile.isThrashing()) && timer>2) {
            this.stop();
            return;
        }

        this.x = (float)this.crocodile.getX();
        this.y = (float)this.crocodile.getY();
        this.z = (float)this.crocodile.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.crocodile.isSilent();
    }
}
