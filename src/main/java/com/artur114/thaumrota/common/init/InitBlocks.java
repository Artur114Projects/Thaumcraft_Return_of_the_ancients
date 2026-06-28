package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.thaumrota.common.blocks.*;
import com.artur114.thaumrota.common.tileentity.*;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import thaumcraft.api.ThaumcraftMaterials;

@RegistryContainer
public class InitBlocks {
    public static final Block ANCIENT_GLASS = new BlockAncientGlass("ancient_glass", Material.GLASS, 2.0F, 10.0F, SoundType.GLASS);
    public static final Block ANCIENT_GRID = new BlockAncientGrid("ancient_grid", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block ANCIENT_LAMP_0 = new BlockAncientLamp("ancient_lamp_0", 1.0F).setCreativeTab(ThaumRotA.CREATIVE_TAB).setForCreative();
    public static final Block ANCIENT_LAMP_1 = new BlockAncientLamp1("ancient_lamp_1", 1.0F).setNotFillAndOpaqueCube().setCreativeTab(ThaumRotA.CREATIVE_TAB).setForCreative();
    public static final BaseBlockTile<TileEntityAncientFan> ANCIENT_FAN = new BlockAncientFan("ancient_fan");
    public static final BaseBlockTile<TileEntityAncientDoor4X3> ANCIENT_DOOR_4_X_3 = new BlockAncientDoor4X3("ancient_door_4x3");
    public static final BaseBlockTile<TileEntityAncientDoor8X6> ANCIENT_DOOR_8_X_6 = new BlockAncientDoor8x6("ancient_door_8x6");
    public static final BaseBlockTile<TileEntityPedestalActive> PEDESTAL_ACTIVE = new BlockPedestalActive("pedestal_active");
    public static final BaseBlockTile<TileEntityAncientPiston> ANCIENT_PISTON = new BlockAncientPiston("ancient_piston");
    public static final BaseBlockTile<TileEntityIncinerator> INCINERATOR = new BlockIncinerator("incinerator");
    public static final BaseBlockTile<TileEntityAncientSanctuaryController> ANCIENT_SANCTUARY_CONTROLLER = new BlockAncientSanctuaryController("ancient_sanctuary_controller", Material.ROCK, -1, 999999999, SoundType.METAL);
    public static final Block ANCIENT_SANCTUARY_CONTROLLER_BROKEN = new BlockAncientSanctuaryControllerBroken("ancient_sanctuary_controller_broken", Material.ROCK, -1, Integer.MAX_VALUE, SoundType.STONE);
    public static final BaseBlockTile<TileEntityPhantomPedestal> PHANTOM_PEDESTAL = new BlockPhantomPedestal("pedestal_phantom");
    public static final Block TAINT_VOID_STONE = new BlockTaintVoidStone("taint_void_stone", ThaumcraftMaterials.MATERIAL_TAINT, 3.0F, 20, SoundType.STONE).setCreativeTab(ThaumRotA.CREATIVE_TAB);
    public static final Block TAINT_VOID_COBBLESTONE = new BlockTaintVoidStone("taint_void_cobblestone", ThaumcraftMaterials.MATERIAL_TAINT, 2.0F, 15, SoundType.STONE).setCreativeTab(ThaumRotA.CREATIVE_TAB);
    public static final Block INCANDESCENT_TAINT_VOID_STONE = new BlockIncandescentTaintVoidStone("incandescent_taint_void_stone", ThaumcraftMaterials.MATERIAL_TAINT, 2.0F, 15, SoundType.STONE).setCreativeTab(ThaumRotA.CREATIVE_TAB);
    public static final Block CLEANED_VOID_STONE = new BlockCleanedVoidStone("cleaned_void_stone", Material.ROCK, 0.5F, 5, SoundType.STONE).setCreativeTab(ThaumRotA.CREATIVE_TAB);

    public static final BaseBlockTile<TileImitationAncientFuse> IMITATION_ANCIENT_FUSE = new BlockImitationAncientFuse("imitation_ancient_fuse_block", Material.CLAY, 0.01F, 1, SoundType.CLOTH);
    public static final BaseBlockTile<TileEntityAncientDoorH4x4> ANCIENT_DOOR_H_4_X_4 = new BlockAncientDoorH4x4("ancient_door_h_4x4");
    public static final BaseBlockTile<TileEntityAncientProjector> ANCIENT_PROJECTOR = new BlockAncientProjector("ancient_projector");
    public static final BaseBlockTile<TileEntityEnergyLine> ENERGY_LINE = new BlockEnergyLine("energy_line", Material.CLAY, 0.2F, 8, SoundType.METAL);
    public static final BaseBlockTile<TileEnergySource> ENERGY_SOURCE = new BlockEnergySource("energy_source", Material.CLAY, 1.0F, 10, SoundType.METAL);
    public static final BaseBlockTile<TileEntityForDev> DEV_BLOCK = new BlockForDev("dev_block", Material.ROCK, -1, 999999999, SoundType.METAL);
    public static final Block TP_TO_ANCIENT_WORLD_BLOCK = new BlockAncientWorldPortal("tp_to_ancient_world_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);
    public static final Block ELDRITCH_STONE_LIGHTNING = new BlockLightningStoneTC("stone_eldritch_tile");
    public static final Block ANCIENT_STONE_LIGHTNING = new BlockLightningStoneTC("stone_ancient");
    public static final Block DUMMY_ANCIENT_STONE = new BlockDummy("dummy_ancient_stone", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MOVING_LAVA_BLOCK = new BlockMovingLava("moving_lava_block");
    public static final Block TP_TO_HOME_BLOCK = new BlockTpToHome("tp_to_home_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);
    public static Block ELDRITCH_STAIRS = null;
    public static Block ANCIENT_STAIRS = null;
}
