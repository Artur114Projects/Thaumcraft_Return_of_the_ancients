package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.blocks.*;
import com.artur.returnoftheancients.main.MainR;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import thaumcraft.api.ThaumcraftMaterials;

import java.util.ArrayList;
import java.util.List;

public class InitBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    //Blocks
    public static final Block TP_TO_HOME_BLOCK = new BlockTpToHome("tp_to_home_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);
    public static final Block TAINT_ANCIENT_STONE = new BlockTaintAncientStone("taint_ancient_stone", Material.ROCK, -1, 999999999, SoundType.STONE);
    public static final Block CLEANED_VOID_STONE = new BlockCleanedVoidStone("cleaned_void_stone", Material.ROCK, 0.5F, 5, SoundType.STONE).setTRACreativeTab();
    public static final Block TP_TO_ANCIENT_WORLD_BLOCK = new BlockTpToAncientWorld("tp_to_ancient_world_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);
    public static final Block TAINT_VOID_STONE = new BlockTaintVoidStone("taint_void_stone", ThaumcraftMaterials.MATERIAL_TAINT, 3.0F, 20, SoundType.STONE).setTRACreativeTab();
    public static final Block TAINT_VOID_COBBLESTONE = new BlockTaintVoidStone("taint_void_cobblestone", ThaumcraftMaterials.MATERIAL_TAINT, 2.0F, 15, SoundType.STONE).setTRACreativeTab();
    public static final Block INCANDESCENT_TAINT_VOID_STONE = new BlockIncandescentTaintVoidStone("incandescent_taint_void_stone", ThaumcraftMaterials.MATERIAL_TAINT, 2.0F, 15, SoundType.STONE).setTRACreativeTab();
    public static final Block ANCIENT_SANCTUARY_CONTROLLER_BROKEN = new BlockAncientSanctuaryControllerBroken("ancient_sanctuary_controller_broken", Material.ROCK, -1, Integer.MAX_VALUE, SoundType.STONE);
    public static final Block ANCIENT_LAMP_1 = new BlockAncientLamp1("ancient_lamp_1", 1.0F).setNotFillAndOpaqueCube().setTRACreativeTab().setForCreative();
    public static final Block ANCIENT_LAMP_0 = new BlockAncientLamp("ancient_lamp_0", 1.0F).setTRACreativeTab().setForCreative();
    public static final Block DUMMY_ANCIENT_STONE = new BlockDummy("dummy_ancient_stone", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static Block ELDRITCH_STAIRS = null;
    public static Block ANCIENT_STAIRS = null;
}
