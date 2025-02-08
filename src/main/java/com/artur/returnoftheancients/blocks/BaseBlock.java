package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public abstract class BaseBlock extends Block implements IHasModel {
    public Item item;
    protected BaseBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(material);

        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);

        InitBlocks.BLOCKS.add(this);
        item = new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
        InitItems.ITEMS.add(item);
    }

    protected void setTRACreativeTab() {
        this.setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
    }

    @Override
    public void registerModels() {
        MainR.proxy.registerItemRenderer(item, 0, "inventory");
    }
}