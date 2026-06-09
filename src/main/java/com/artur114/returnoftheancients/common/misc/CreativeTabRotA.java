package com.artur114.returnoftheancients.common.misc;

import com.artur114.returnoftheancients.common.init.InitBlocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreativeTabRotA extends CreativeTabs {
	public CreativeTabRotA(String label) {
		super(label);
	}

    @Override
	public @NotNull ItemStack getTabIconItem() {
		return new ItemStack(InitBlocks.PEDESTAL_ACTIVE);
	}
}
