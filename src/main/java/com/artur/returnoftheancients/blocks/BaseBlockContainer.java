package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.util.interfaces.IHasModel;
import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseBlockContainer<T extends TileEntity> extends BlockContainer implements IHasModel {

    protected boolean isForCreative = false;
    public Item item;

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

    @SuppressWarnings("unchecked")
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

    protected void getTileAndCallRunnable(World world, BlockPos pos, RunnableWithParam<T> run) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && this.getTileEntityClass().isAssignableFrom(tile.getClass())) {
            run.run(this.getTileEntityClass().cast(tile));
        }
    }

    public BaseBlockContainer<T> setTRACreativeTab() {
        this.setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
        return this;
    }

    public BaseBlockContainer<T> setForCreative() {
        this.isForCreative = true;
        return this;
    }

    public BaseBlockContainer<T> addForCreativeOnlyTooltip(List<String> tooltip) {
        tooltip.add(TextFormatting.RED + I18n.format("returnoftheancients.for_creative_only"));
        return this;
    }

    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            this.addForCreativeOnlyTooltip(tooltip);
        }
        super.addInformation(stack, player, tooltip, advanced);
    }
}
