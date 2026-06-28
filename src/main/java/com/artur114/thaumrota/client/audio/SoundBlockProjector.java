package com.artur114.thaumrota.client.audio;

import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientProjector;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;

public class SoundBlockProjector extends SoundTile<TileEntityAncientProjector> {
    public SoundBlockProjector(TileEntityAncientProjector tile) {
        super(InitSounds.PROJECTOR, SoundCategory.BLOCKS, tile);

        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void update() {
        super.update();

        if (this.tile.isEnabled()) {
            this.volume = 0.8F;
        } else {
            this.volume = 0.0F;
        }
    }
}
