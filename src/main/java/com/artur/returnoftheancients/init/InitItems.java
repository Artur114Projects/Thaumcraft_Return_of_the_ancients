package com.artur.returnoftheancients.init;

import java.util.ArrayList;
import java.util.List;

import com.artur.returnoftheancients.ancientworldutilities.ReturnOfTheAncientsMaterials;
import com.artur.returnoftheancients.items.*;
import net.minecraft.item.Item;

public class InitItems {
	public static final List<Item> ITEMS = new ArrayList<Item>();

	// Items
	public static final Item PRIMAL_BLADE = new ItemPrimalBlade("itemprimalblade", ReturnOfTheAncientsMaterials.TOOLMAT_PRIMAL);
	public static final Item GAVNO = new ItemGavno("gavno");
}
