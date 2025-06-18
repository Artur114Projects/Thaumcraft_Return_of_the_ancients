package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockPedestalActive;
import net.minecraft.block.state.IBlockState;

public class TileEntityPedestalActive extends TileBase {
    private int rotate = 0;

    public int rotate() {
        return this.rotate;
    }

    @Override
    public void onLoad() {
        IBlockState state = this.world.getBlockState(this.pos);

        switch (state.getValue(BlockPedestalActive.DIRECTION)) {
            case EAST:
                this.rotate = 0;
                break;
            case WEST:
                this.rotate = 180;
                break;
            case NORTH:
                this.rotate = 270;
                break;
            case SOUTH:
                this.rotate = 90;
                break;
        }

        if (state.getValue(BlockPedestalActive.ROTATE)) {
            this.rotate += 45;
        }
    }
}
