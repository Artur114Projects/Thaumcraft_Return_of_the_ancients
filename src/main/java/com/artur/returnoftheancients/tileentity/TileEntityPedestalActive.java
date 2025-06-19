package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockPedestalActive;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockUseListener;
import com.artur.returnoftheancients.util.math.Pos3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TileEntityPedestalActive extends TileBase implements ITileBlockUseListener {
    private int rotate = 0;

    public int rotate() {
        return this.rotate;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return true;
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
