package com.artur114.thaumrota.common.blocks;

import com.artur114.bananalib.mc.registry.IRegisterBus;
import com.artur114.bananalib.mc.registry.data.ModelRegData;
import com.artur114.bananalib.mc.registry.interf.IHasModel;
import com.artur114.bananalib.mc.registry.interf.IHasMoreRegisters;
import com.artur114.thaumrota.common.init.InitBlocks;
import com.artur114.thaumrota.common.init.InitItems;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BlockStairsBase extends BlockStairs implements IHasModel, IHasMoreRegisters {
    protected boolean isForCreative = false;
    @Nullable public final Item item;

    public BlockStairsBase(IBlockState modelState, String name) {
        super(modelState);
        this.setLightOpacity(0);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.item = this.createItemBlock();
    }

    @Override
    public @NotNull BlockStairsBase setCreativeTab(@NotNull CreativeTabs tab) {
        return (BlockStairsBase) super.setCreativeTab(tab);
    }

    public BlockStairsBase setForCreative() {
        this.isForCreative = true;
        return this;
    }

    protected @Nullable Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    @Override
    public List<ModelRegData> registerModelsData() {
        if (this.item == null) return Collections.emptyList();
        return Collections.singletonList(ModelRegData.inventory(this.item, 0));
    }

    @Override
    public void registerOther(IRegisterBus bus) {
        if (this.item == null) return;
        bus.registerItem(this.item);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            tooltip.add(TextFormatting.RED + I18n.format("thaumrota.for_creative_only"));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }
}
