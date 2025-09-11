package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityIncineratorRender;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityIncinerator;
import com.artur.returnoftheancients.util.MaterialArray;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockIncinerator extends BlockTileEntity<TileEntityIncinerator> {

    public BlockIncinerator(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.bindTESR(new TileEntityIncineratorRender());
        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public Class<TileEntityIncinerator> getTileEntityClass() {
        return TileEntityIncinerator.class;
    }

    @Override
    public @Nullable TileEntityIncinerator createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityIncinerator();
    }
}
