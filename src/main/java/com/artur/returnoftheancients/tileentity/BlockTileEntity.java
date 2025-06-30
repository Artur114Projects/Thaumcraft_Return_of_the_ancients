package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BaseBlock;
import com.artur.returnoftheancients.client.render.item.TileEntityItemStackRendererTRA;
import com.artur.returnoftheancients.client.render.tile.IItemStackRenderer;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.tileentity.interf.ITileBBProvider;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockUseListener;
import com.artur.returnoftheancients.util.MaterialArray;
import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class BlockTileEntity<T extends TileEntity> extends BaseBlock {
    private TileEntitySpecialRenderer<T> tileRender = null;
    private final boolean isBBProvider;
    private final boolean isUseListener;

    public BlockTileEntity(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        InitTileEntity.TILE_ENTITIES.add(this);

        this.isBBProvider = ITileBBProvider.class.isAssignableFrom(this.getTileEntityClass());
        this.isUseListener = ITileBlockUseListener.class.isAssignableFrom(this.getTileEntityClass());
    }

    public BlockTileEntity(String name, MaterialArray array) {
        this(name, array.material(), array.hardness(), array.resistance(), array.soundType());
    }

    protected void bindTESR(TileEntitySpecialRenderer<T> tileRender) {
        this.tileRender = tileRender;

        if (tileRender instanceof IItemStackRenderer) {
            this.item.setTileEntityItemStackRenderer(TileEntityItemStackRendererTRA.INSTANCE);
            TileEntityItemStackRendererTRA.INSTANCE.register(this.item, ((IItemStackRenderer) tileRender));
        }
    }

    public abstract Class<T> getTileEntityClass();

    public T getTileEntity(IBlockAccess world, BlockPos position) {
        return (T) world.getTileEntity(position);
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState blockState) {
        return true;
    }

    @Nullable
    @Override
    public abstract T createTileEntity(@NotNull World world, @NotNull IBlockState blockState);

    @Override
    public void registerModels() {
        super.registerModels();
        if (this.tileRender != null) {
            ClientRegistry.bindTileEntitySpecialRenderer(this.getTileEntityClass(), this.tileRender);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (this.isBBProvider) {
            TileEntity tile = source.getTileEntity(pos);
            if (this.getTileEntityClass().isInstance(tile)) {
                return ((ITileBBProvider) tile).boundingBox();
            }
        }

        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.isUseListener) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (this.getTileEntityClass().isInstance(tile)) {
                return ((ITileBlockUseListener) tile).onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    protected void getTileAndCallRunnable(World world, BlockPos pos, RunnableWithParam<T> run) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && this.getTileEntityClass().isAssignableFrom(tile.getClass())) {
            run.run(this.getTileEntityClass().cast(tile));
        }
    }
}