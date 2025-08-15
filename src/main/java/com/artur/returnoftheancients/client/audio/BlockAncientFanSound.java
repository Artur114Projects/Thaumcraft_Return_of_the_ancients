package com.artur.returnoftheancients.client.audio;

import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.tileentity.TileEntityAncientFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BlockAncientFanSound extends PositionedSound implements ITickableSound {
    private final TileEntityAncientFan tile;
    private boolean donePlaying = false;

    public BlockAncientFanSound(TileEntityAncientFan tile) {
        super(InitSounds.FAN.SOUND, SoundCategory.BLOCKS);

        this.tile = tile;
        this.repeat = true;
        this.volume = 0.6F;
        this.repeatDelay = 0;
        this.xPosF = tile.getPos().getX() + 0.5F;
        this.yPosF = tile.getPos().getY() + 0.5F;
        this.zPosF = tile.getPos().getZ() + 0.5F;
        this.attenuationType = AttenuationType.NONE;
    }

    @Override
    public void update() {
        if (this.isDonePlaying()) {
            return;
        }
        if (this.tile.isInvalid()) {
            this.donePlaying = true;
        }

        if (this.tile.spinSpeed(1) > 3) {
            this.pitch = 1.0F + (0.8F * ((this.tile.spinSpeed(1) - 3.0F) / 3.0F));
            this.volume = 0.4F + (0.4F * ((this.tile.spinSpeed(1) - 3.0F) / 3.0F));
        }
    }

    @Override
    public boolean isDonePlaying() {
        return this.donePlaying;
    }
}
