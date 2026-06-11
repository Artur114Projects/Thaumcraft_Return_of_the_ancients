package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import com.artur114.thaumrota.common.util.math.UltraMutableBlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiFunction;

public abstract class StructureCombatRoom extends StructureMultiChunk implements IStructureInteractive {
    private final Map<Class<? extends TileEntity>, List<BlockPos>> tilesMap = new HashMap<>();
    private ChunkPos chunkPos = null;
    private Random rand = null;
    private World world = null;

    public StructureCombatRoom(EnumRotate rotate, EnumMultiChunkStrType type, StrPos pos) {
        super(rotate, type, pos);
    }

    protected StructureCombatRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
        blockPos.setPos(pos).add(8, 0, 8).setY(this.y);
        BiFunction<TileEntity, NBTTagCompound, TileEntity> hook = (tile, data) -> {
            List<BlockPos> list = this.tilesMap.computeIfAbsent(tile.getClass(), k -> new ArrayList<>());
            list.add(tile.getPos());
            return tile;
        };
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().setPosAsXZCenter().addTileEntityHook(hook).build();
        UltraMutableBlockPos.release(blockPos);
    }

    @Override
    public void bindWorld(World world) {
        this.world = world;
    }

    @Override
    public void bindRealPos(ChunkPos pos) {
        this.rand = new Random();
        this.chunkPos = pos;
    }
}
