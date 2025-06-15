package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.init.InitBlocks;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAncientLamp1 extends BlockAncientLamp {
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");

    public BlockAncientLamp1(String name, float light) {
        super(name, light);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(UP, this.canConnectTo(world, pos, EnumFacing.UP))
                .withProperty(DOWN, this.canConnectTo(world, pos, EnumFacing.DOWN));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    private boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing){
        IBlockState state = world.getBlockState(pos.offset(facing));
        return state.getBlock() == InitBlocks.ANCIENT_LAMP_1;
    }
}
