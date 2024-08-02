package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityBossTriggerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BossTriggerBlockT extends BlockTileEntity<TileEntityBossTriggerBlock> {
    public BossTriggerBlockT(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (!world.isRemote) {

            TileEntityBossTriggerBlock tileEntity = getTileEntity(world, position);

            if (side == EnumFacing.DOWN) {

                tileEntity.decrementCount();

            }

            else if (side == EnumFacing.UP) {

                tileEntity.incrementCount();
            }

            player.sendMessage(new TextComponentString("Count: " + tileEntity.getCount()));
        }

        return true;
    }

    @Override
    public Class<TileEntityBossTriggerBlock> getTileEntityClass() {
        return TileEntityBossTriggerBlock.class;
    }

    @Override
    public @Nullable TileEntityBossTriggerBlock createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityBossTriggerBlock();
    }
}
