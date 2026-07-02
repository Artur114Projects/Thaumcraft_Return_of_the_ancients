package com.artur114.thaumrota.common.creative;

import com.artur114.thaumrota.common.init.InitBlocks;

import com.artur114.thaumrota.common.items.IHasTabPriority;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;


public class RotACreativeTab extends CreativeTabs {
	public RotACreativeTab(String label) {
		super(label);
	}

    @Override
    public void displayAllRelevantItems(@NotNull NonNullList<ItemStack> list) {
        for (Item item : ThaumRotA.REGISTER_BUS.items()) {
            item.getSubItems(this, list);
        }
    }

    @Override
	public @NotNull ItemStack getTabIconItem() {
		return new ItemStack(InitBlocks.ANCIENT_PISTON);
	}
}
