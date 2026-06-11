package com.artur114.thaumrota.common.blocks;

import com.artur114.bananalib.mc.base.BBlockContainerBase;
import com.artur114.bananalib.mc.base.MaterialArray;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseBlockContainer<T extends TileEntity> extends BBlockContainerBase<T> {
    public BaseBlockContainer(String name, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType) {
        super(name, material, mapColor, hardness, resistance, soundType);
    }

    public BaseBlockContainer(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    public BaseBlockContainer(String name, MaterialArray mat) {
        super(name, mat);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            tooltip.add(TextFormatting.RED + I18n.format("thaumrota.for_creative_only"));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }
}
