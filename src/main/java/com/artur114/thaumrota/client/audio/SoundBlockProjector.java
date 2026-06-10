package com.artur114.thaumrota.client.audio;

import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientProjector;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;

public class SoundBlockProjector extends MovingSound {
    private final TileEntityAncientProjector tile;
    public SoundBlockProjector(TileEntityAncientProjector tile) {
        super(InitSounds.PROJECTOR.SOUND, SoundCategory.BLOCKS);

        this.tile = tile;
        this.repeat = true;
        this.repeatDelay = 0;
        this.xPosF = tile.getPos().getX() + 0.5F;
        this.yPosF = tile.getPos().getY();
        this.zPosF = tile.getPos().getZ() + 0.5F;
    }

    @Override
    public void update() {
        this.donePlaying = this.tile.isInvalid();

        if (this.tile.isEnabled()) {
            this.volume = 0.8F;
        } else {
            this.volume = 0.0F;
        }
    }
}
