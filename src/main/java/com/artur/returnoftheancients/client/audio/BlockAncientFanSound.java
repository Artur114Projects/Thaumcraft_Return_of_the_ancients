package com.artur.returnoftheancients.client.audio;

import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.tileentity.TileEntityAncientFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BlockAncientFanSound extends SoundTile<TileEntityAncientFan> {

    public BlockAncientFanSound(TileEntityAncientFan tile) {
        super(InitSounds.FAN.SOUND, SoundCategory.BLOCKS, tile);

        this.repeat = true;
        this.volume = 0.6F;
        this.repeatDelay = 0;
    }

    @Override
    public void update() {
        super.update();

        if (this.tile.spinSpeed(1) > 3) { // TODO: 13.09.2025 Переделать!
            this.pitch = 1.0F + (0.8F * ((this.tile.spinSpeed(1) - 3.0F) / 3.0F));
            this.volume = 0.4F + (0.4F * ((this.tile.spinSpeed(1) - 3.0F) / 3.0F));
        }
    }
}
