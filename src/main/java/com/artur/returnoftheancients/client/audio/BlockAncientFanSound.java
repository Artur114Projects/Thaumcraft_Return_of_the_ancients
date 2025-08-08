package com.artur.returnoftheancients.client.audio;

import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.tileentity.TileEntityAncientFan;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BlockAncientFanSound extends MovingSound {
    private final TileEntityAncientFan tile;
    public BlockAncientFanSound(TileEntityAncientFan tile) {
        super(InitSounds.FAN.SOUND, SoundCategory.BLOCKS);


        this.tile = tile;
        this.repeat = true;
        this.volume = 0.6F;
        this.repeatDelay = 0;
        this.xPosF = tile.getPos().getX() + 0.5F;
        this.yPosF = tile.getPos().getY();
        this.zPosF = tile.getPos().getZ() + 0.5F;
    }

    @Override
    public void update() {
        this.donePlaying = this.tile.isInvalid();

        if (tile.spinSpeed(1) > 2) {
            this.pitch = 1.0F + (0.8F * ((tile.spinSpeed(1) - 2.0F) / 4.0F));
            this.volume = 0.4F + (0.4F * ((tile.spinSpeed(1) - 2.0F) / 4.0F));
        }
    }
}
