package com.artur114.returnoftheancients.misc;

import com.artur114.returnoftheancients.init.InitItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReturnOfTheAncientsTab extends CreativeTabs {

	public ReturnOfTheAncientsTab(String label) {
		super(label);
	}

	@Override
	public @NotNull ItemStack getTabIconItem() {
		return new ItemStack(InitItems.PRIMAL_BLADE);
	}

}
