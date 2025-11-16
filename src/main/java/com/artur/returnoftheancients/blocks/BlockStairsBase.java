package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.util.interfaces.IHasModel;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BlockStairsBase extends BlockStairs implements IHasModel {
    protected boolean isForCreative = false;
    public Item item;
    public BlockStairsBase(IBlockState modelState, String name) {
        super(modelState);
        this.setLightOpacity(0);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);

        InitBlocks.BLOCKS_REGISTER_BUSS.add(this);
        this.item = new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
        InitItems.ITEMS_REGISTER_BUSS.add(this.item);
    }

    public BlockStairsBase setTRACreativeTab() {
        this.setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
        return this;
    }

    public BlockStairsBase setForCreative() {
        this.isForCreative = true;
        return this;
    }

    protected void addForCreativeOnlyTooltip(List<String> tooltip) {
        tooltip.add(TextFormatting.RED + I18n.format("returnoftheancients.for_creative_only"));
    }

    @Override
    public void registerModels() {
        MainR.proxy.registerItemRenderer(this.item, 0, "inventory");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            this.addForCreativeOnlyTooltip(tooltip);
        }
        super.addInformation(stack, player, tooltip, advanced);
    }
}
