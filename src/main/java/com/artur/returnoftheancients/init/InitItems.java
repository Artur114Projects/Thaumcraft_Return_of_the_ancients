package com.artur.returnoftheancients.init;

import java.util.ArrayList;
import java.util.List;

import com.artur.returnoftheancients.misc.ReturnOfTheAncientsMaterials;
import com.artur.returnoftheancients.items.*;
import net.minecraft.item.Item;

public class InitItems {
	public static final List<Item> ITEMS = new ArrayList<>();

	// Items
	public static final Item PRIMAL_BLADE = new ItemPrimalBlade("itemprimalblade", ReturnOfTheAncientsMaterials.TOOLMAT_PRIMAL);
	public static final Item IMITATION_ANCIENT_FUSE = new ItemImitationAncientFuse("imitation_ancient_fuse");
	public static final Item PHANTOM_TABLET = new ItemPhantomTablet("phantom_tablet");
	public static final Item SOUL_BINDER = new ItemSoulBinder("soul_binder");
	public static final Item COMPASS = new ItemPortalCompass("portal_compass");
	public static final Item GAVNO = new ItemDebug("gavno");
}
