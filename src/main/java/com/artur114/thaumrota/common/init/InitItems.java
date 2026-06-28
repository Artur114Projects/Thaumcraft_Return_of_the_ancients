package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.thaumrota.common.blocks.MaterialArrays;
import com.artur114.thaumrota.common.items.*;
import net.minecraft.item.Item;

@RegistryContainer
public class InitItems {
    public static final Item PHANTOM_TABLET = new ItemPhantomTablet("phantom_tablet");
    public static final Item COMPASS = new ItemPortalCompass("portal_compass");
    public static final Item SOUL_BINDER = new ItemSoulBinder("soul_binder");
    public static final Item IMITATION_ANCIENT_FUSE = new ItemImitationAncientFuse("imitation_ancient_fuse");
    public static final Item DEBUG_CARROT = new ItemDebugCarrot("debug_carrot");
}
