package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ReturnOfTheAncientsTab extends CreativeTabs {

	public ReturnOfTheAncientsTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(InitItems.PRIMAL_BLADE);
	}

}
