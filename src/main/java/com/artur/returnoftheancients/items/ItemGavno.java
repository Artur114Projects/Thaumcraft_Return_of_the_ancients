package com.artur.returnoftheancients.items;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGavno extends BaseItem{


	public ItemGavno(String name) {
		super(name);
		setMaxStackSize(8);

	}



	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Don t click");
	}
}
