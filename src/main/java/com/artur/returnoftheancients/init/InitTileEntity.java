package com.artur.returnoftheancients.init;


import com.artur.returnoftheancients.blocks.AncientTeleport;
import com.artur.returnoftheancients.blocks.BaseBlockContainer;
import com.artur.returnoftheancients.blocks.EldritchTrap;
import com.artur.returnoftheancients.blocks.FireTrap;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import com.artur.returnoftheancients.tileentity.TileEntityEldritchTrap;
import com.artur.returnoftheancients.tileentity.TileEntityFireTrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class InitTileEntity {
    public static final List<BlockTileEntity<?>> TILE_ENTITIES = new ArrayList<>();
    public static final List<BaseBlockContainer<?>> TILE_ENTITIES_CONTAINER = new ArrayList<>();
    public static final BlockTileEntity<TileEntityFireTrap> FIRE_TRAP = new FireTrap("fire_trap", Material.ROCK, 100, 999999999, SoundType.STONE);
    public static final BlockTileEntity<TileEntityEldritchTrap> ELDRITCH_TRAP = new EldritchTrap("eldritch_trap", Material.ROCK, 100, 999999999, SoundType.STONE);
    public static final BaseBlockContainer<TileEntityAncientTeleport> ANCIENT_TELEPORT = new AncientTeleport("ancient_teleport", Material.IRON, 100, 1000, SoundType.STONE);

}
