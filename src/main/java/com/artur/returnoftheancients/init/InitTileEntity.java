package com.artur.returnoftheancients.init;


import com.artur.returnoftheancients.blocks.*;
import com.artur.returnoftheancients.energy.blocks.BlockEnergySource;
import com.artur.returnoftheancients.energy.tiles.TileEnergySource;
import com.artur.returnoftheancients.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class InitTileEntity {
    public static final List<BlockTileEntity<?>> TILE_ENTITIES = new ArrayList<>();
    public static final List<BaseBlockContainer<?>> TILE_ENTITIES_CONTAINER = new ArrayList<>();

    public static final BlockTileEntity<TileEntityAncientSanctuaryController> ANCIENT_SANCTUARY_CONTROLLER = new BlockAncientSanctuaryController("ancient_sanctuary_controller", Material.ROCK, -1, 999999999, SoundType.METAL);
    public static final BaseBlockContainer<TileEntityAncientTeleport> ANCIENT_TELEPORT = new AncientTeleport("ancient_teleport", Material.IRON, 1.4F, 10, SoundType.STONE);
    public static final BlockTileEntity<TileEntityEldritchTrap> ELDRITCH_TRAP = new EldritchTrap("eldritch_trap", Material.ROCK, 100, 999999999, SoundType.STONE);
    public static final BlockTileEntity<TileEnergySource> ENERGY_SOURCE = new BlockEnergySource("energy_source", Material.CLAY, 1.0F, 10, SoundType.METAL);
    public static final BlockTileEntity<TileEntityEnergyLine> ENERGY_LINE = new BlockEnergyLine("energy_line", Material.CLAY, 0.2F, 8, SoundType.METAL);
    public static final BlockTileEntity<TileEntityFireTrap> FIRE_TRAP = new FireTrap("fire_trap", Material.ROCK, 100, 999999999, SoundType.STONE);
}
