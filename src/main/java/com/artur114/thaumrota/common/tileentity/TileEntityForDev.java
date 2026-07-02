package com.artur114.thaumrota.common.tileentity;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.util.ITickable;

public class TileEntityForDev extends TileBase implements ITickable {
    @Override
    public void update() {
        if (ThaumRotA.isDevEnv()) {
            ThaumRotA.DEV_SHELL.evaluate("tile_update.groovy", "world", this.world, "tile", this, "pos", this.pos);
        }
    }
}
