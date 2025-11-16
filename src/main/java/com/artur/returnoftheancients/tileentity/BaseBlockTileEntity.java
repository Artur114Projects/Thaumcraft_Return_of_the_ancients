package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BaseBlock;
import com.artur.returnoftheancients.client.render.item.TileEntityItemStackRendererTRA;
import com.artur.returnoftheancients.client.render.item.IItemStackRenderer;
import com.artur.returnoftheancients.tileentity.interf.*;
import com.artur.returnoftheancients.util.MaterialArray;
import com.artur.returnoftheancients.util.interfaces.IHasTileEntity;
import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class BaseBlockTileEntity<T extends TileEntity> extends BaseBlock implements IHasTileEntity<T> {
    private TileEntitySpecialRenderer<T> tileRender = null;
    private final boolean isBBProvider;
    private final boolean isUseListener;
    private final boolean isPlaceListener;
    private final boolean isMultiBBProvider;
    private final boolean isNeighborListener;

    public BaseBlockTileEntity(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.isBBProvider = ITileBBProvider.class.isAssignableFrom(this.tileEntityClass());
        this.isUseListener = ITileBlockUseListener.class.isAssignableFrom(this.tileEntityClass());
        this.isPlaceListener = ITileBlockPlaceListener.class.isAssignableFrom(this.tileEntityClass());
        this.isMultiBBProvider = ITileMultiBBProvider.class.isAssignableFrom(this.tileEntityClass());
        this.isNeighborListener = ITileNeighborChangeListener.class.isAssignableFrom(this.tileEntityClass());
    }

    public BaseBlockTileEntity(String name, MaterialArray array) {
        this(name, array.material(), array.hardness(), array.resistance(), array.soundType());
    }

    protected void setRenderer(TileEntitySpecialRenderer<T> tileRender) {
        this.tileRender = tileRender;

        if (tileRender instanceof IItemStackRenderer) {
            this.item.setTileEntityItemStackRenderer(TileEntityItemStackRendererTRA.INSTANCE);
            TileEntityItemStackRendererTRA.INSTANCE.register(this.item, ((IItemStackRenderer) tileRender));
        }
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
            ClientRegistry.bindTileEntitySpecialRenderer(this.tileEntityClass(), this.tileRender);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (this.isNeighborListener) {
            TileEntity tile = world.getTileEntity(pos);
            if (this.tileEntityClass().isInstance(tile)) {
                ((ITileNeighborChangeListener) tile).onNeighborChange(world, pos, neighbor);
            }
        }
    }

    @Nullable
    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        if (this.isMultiBBProvider) {
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(pos);
            if (this.tileEntityClass().isInstance(tile)) {
                Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
                Vec3d vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
                RayTraceResult raytraceresult = null;
                for (AxisAlignedBB bb : ((ITileMultiBBProvider) tile).boundingBoxes()) {
                    raytraceresult = bb.calculateIntercept(vec3d, vec3d1);

                    if (raytraceresult != null) {
                        break;
                    }
                }
                return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
            }
        }

        return super.rayTrace(pos, start, end, boundingBox);

    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (this.isMultiBBProvider) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (this.tileEntityClass().isInstance(tile)) {
                for (AxisAlignedBB bb : ((ITileMultiBBProvider) tile).boundingBoxes()) {
                    Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, bb);
                }
                return;
            }
        }

        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (this.isBBProvider) {
            TileEntity tile = source.getTileEntity(pos);
            if (this.tileEntityClass().isInstance(tile)) {
                return ((ITileBBProvider) tile).boundingBox();
            }
        }

        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.isUseListener) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (this.tileEntityClass().isInstance(tile)) {
                return ((ITileBlockUseListener) tile).onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (this.isPlaceListener) {
            T tile = this.createTileEntity(worldIn, state);
            if (tile != null) {
                worldIn.setTileEntity(pos, tile);
                ((ITileBlockPlaceListener) tile).onBlockPlacedBy(worldIn, pos, state, placer, stack);
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    protected void getTileAndCallRunnable(World world, BlockPos pos, RunnableWithParam<T> run) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && this.tileEntityClass().isInstance(tile)) {
            run.run(this.tileEntityClass().cast(tile));
        }
    }
}