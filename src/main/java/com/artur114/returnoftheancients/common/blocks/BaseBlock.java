package com.artur114.returnoftheancients.common.blocks;

import com.artur114.returnoftheancients.common.init.InitBlocks;
import com.artur114.returnoftheancients.common.init.InitItems;
import com.artur114.returnoftheancients.main.ThaumicRotA;
import com.artur114.returnoftheancients.common.util.MaterialArray;
import com.artur114.returnoftheancients.common.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class BaseBlock extends Block implements IHasModel {
    private boolean isFullCube = true;
    private boolean isOpaqueCube = true;
    protected boolean isForCreative = false;
    public final Item item;

    protected BaseBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(material);

        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);

        InitBlocks.BLOCKS_REGISTER_BUSS.add(this);
        this.item = this.createItemBlock();
        InitItems.ITEMS_REGISTER_BUSS.add(this.item);
    }

    public BaseBlock(String name, MaterialArray array) {
        this(name, array.material(), array.hardness(), array.resistance(), array.soundType());
    }

    public BaseBlock setNotFillAndOpaqueCube() {
        this.isOpaqueCube = false;
        this.isFullCube = false;
        return this;
    }

    public BaseBlock setNotFullCube() {
        this.isFullCube = false;
        return this;
    }

    public BaseBlock setNotOpaqueCube() {
        this.isOpaqueCube = false;
        return this;
    }

    public BaseBlock setTRACreativeTab() {
        this.setCreativeTab(ThaumicRotA.ROTA_CREATIVE_TAB);
        return this;
    }

    public BaseBlock setForCreative() {
        this.isForCreative = true;
        return this;
    }

    public void addForCreativeOnlyTooltip(List<String> tooltip) {
        tooltip.add(TextFormatting.RED + I18n.format("returnoftheancients.for_creative_only"));
    }

    protected Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    @Override
    public void registerModels() {
        ThaumicRotA.proxy.registerItemRenderer(this.item, 0, "inventory");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            this.addForCreativeOnlyTooltip(tooltip);
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return this.isFullCube;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return this.isOpaqueCube;
    }
}