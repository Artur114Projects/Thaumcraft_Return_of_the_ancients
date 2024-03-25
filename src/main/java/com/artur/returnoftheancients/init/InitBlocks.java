package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.blocks.BossTriggerBlock;
import com.artur.returnoftheancients.blocks.TaintAncientStone;
import com.artur.returnoftheancients.blocks.TpToAncientWorldBlock;
import com.artur.returnoftheancients.blocks.TpToHomeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class InitBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    //Blocks
    public static final Block BOSS_TRIGGER_BLOCK = new BossTriggerBlock("boss_trigger_block", Material.ROCK, -1, 999999999, SoundType.STONE);
    public static final Block TP_TO_HOME_BLOCK = new TpToHomeBlock("tp_to_home_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);
    public static final Block TAINT_ANCIENT_STONE = new TaintAncientStone("taint_ancient_stone", Material.ROCK, -1, 999999999, SoundType.STONE);
    public static final Block TP_TO_ANCIENT_WORLD_BLOCK = new TpToAncientWorldBlock("tp_to_ancient_world_block", Material.PORTAL, -1, 999999999, SoundType.GLASS);

    public static void registerTileEntity() {
//        GameRegistry.registerTileEntity(((BossTriggerBlock) BOSS_TRIGGER_BLOCK).getTileEntityClass(), BOSS_TRIGGER_BLOCK.getRegistryName().toString());
    }
}
