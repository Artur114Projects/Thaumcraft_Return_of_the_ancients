package com.artur.returnoftheancients.init;


import com.artur.returnoftheancients.blocks.FireTrap;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityFireTrap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class InitTileEntity {
    public static final List<BlockTileEntity> TILE_ENTITIES = new ArrayList<>();

    public static final BlockTileEntity<TileEntityFireTrap> FAIR_TRAP = new FireTrap("fire_trap", Material.ROCK, -1, 999999999, SoundType.STONE);
}
