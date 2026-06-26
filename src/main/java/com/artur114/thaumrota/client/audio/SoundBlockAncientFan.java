package com.artur114.thaumrota.client.audio;

import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientFan;
import net.minecraft.util.SoundCategory;

public class SoundBlockAncientFan extends SoundTile<TileEntityAncientFan> {
    private final float baseVolume;

    public SoundBlockAncientFan(TileEntityAncientFan tile) {
        super(InitSounds.FAN, SoundCategory.BLOCKS, tile);

        this.repeat = true;
        this.baseVolume = 0.3F;
        this.repeatDelay = 0;
        this.volume = this.baseVolume;
    }

    @Override
    public void update() {
        super.update();

        if (this.tile.spinSpeed(1) > 3) {
            this.pitch = 1.0F + (0.8F * ((this.tile.spinSpeed(1) - 3.0F) / 3.0F));
            this.volume = this.baseVolume + (this.baseVolume * ((this.tile.spinSpeed(1) - 3.0F) / 3.0F));
        } else {
            this.pitch = 1.0F;
            this.volume = this.baseVolume;
        }
    }
}