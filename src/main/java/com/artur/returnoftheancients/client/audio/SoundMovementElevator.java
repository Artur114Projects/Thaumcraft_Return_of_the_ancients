package com.artur.returnoftheancients.client.audio;

import com.artur.returnoftheancients.generation.portal.base.client.ClientAncientPortal;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class SoundMovementElevator extends MovingSound {
    private final ClientAncientPortal.MovementElevator movement;
    private final EntityPlayerSP player;

    public SoundMovementElevator(EntityPlayerSP player, ClientAncientPortal.MovementElevator movement) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
        this.movement = movement;
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1F;
        this.attenuationType = AttenuationType.NONE;
    }

    public void update() {
        if (!this.player.isDead && !this.movement.isDoneWork()) {
            this.xPosF = (float) this.player.posX;
            this.yPosF = (float) this.player.posY;
            this.zPosF = (float) this.player.posZ;
            float f = MathHelper.sqrt(this.player.motionX * this.player.motionX + this.player.motionZ * this.player.motionZ + this.player.motionY * this.player.motionY);
            float f1 = f / 2.0F;

            if (f >= 0.01D) {
                this.volume = MathHelper.clamp(f1 * f1, 0.0F, 1.0F);
            } else {
                this.volume = 0.0F;
            }

            float f2 = 0.8F;

            if (this.volume > 0.8F) {
                this.pitch = 1.0F + (this.volume - 0.8F);
            } else {
                this.pitch = 1.0F;
            }
        } else {
            this.donePlaying = true;
        }
    }
}
