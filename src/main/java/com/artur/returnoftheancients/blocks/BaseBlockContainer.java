package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseBlockContainer<T extends TileEntity> extends BlockContainer implements IHasModel {

    protected Item item;
    protected BaseBlockContainer(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(material);

        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);

        InitBlocks.BLOCKS.add(this);
        InitTileEntity.TILE_ENTITIES_CONTAINER.add(this);
        item = new ItemBlock(this).setRegistryName(this.getRegistryName());
        InitItems.ITEMS.add(item);
    }


    public abstract Class<T> getTileEntityClass();

    public T getTileEntity(IBlockAccess world, BlockPos position) {
        return (T) world.getTileEntity(position);
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState blockState) {
        return true;
    }

    @Override
    public void registerModels() {
        MainR.proxy.registerItemRenderer(item, 0, "inventory");
    }
}
