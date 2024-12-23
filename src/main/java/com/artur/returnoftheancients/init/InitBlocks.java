package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class InitBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    //Blocks
    public static final Block BOSS_TRIGGER_BLOCK = new BossTriggerBlock("boss_trigger_block", Material.ROCK, -1, 999999999, SoundType.STONE);
    public static final Block TP_TO_HOME_BLOCK = new TpToHomeBlock("tp_to_home_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);
    public static final Block TAINT_ANCIENT_STONE = new TaintAncientStone("taint_ancient_stone", Material.ROCK, -1, 999999999, SoundType.STONE);
    public static final Block TP_TO_ANCIENT_WORLD_BLOCK = new TpToAncientWorldBlock("tp_to_ancient_world_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);

    //    public static final Block BLOCK_HEAVY_AIR = new BlockHeavyAir("heavy_air", Material.AIR, -1, 999999999, SoundType.GLASS);
}
