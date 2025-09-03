package com.artur.returnoftheancients.client.audio;

import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.tileentity.TileEntityAncientFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public abstract class SoundTile<T extends TileEntity> extends PositionedSound implements ITickableSound {
    protected boolean donePlaying = false;
    protected final T tile;

    public SoundTile(SoundEvent sound, SoundCategory category, T tile) {
        super(sound, category);

        this.tile = tile;
        this.xPosF = this.tile.getPos().getX() + 0.5F;
        this.yPosF = this.tile.getPos().getY() + 0.5F;
        this.zPosF = this.tile.getPos().getZ() + 0.5F;
        this.attenuationType = AttenuationType.LINEAR;
    }

    @Override
    public void update() {
        if (this.isDonePlaying()) {
            return;
        }
        if (this.tile.isInvalid()) {
            this.donePlaying = true;
        }
    }

    @Override
    public boolean isDonePlaying() {
        return this.donePlaying;
    }

    public void stop() {
        this.donePlaying = true;
    }
}
