package com.artur114.thaumrota.common.generation.chunkgens;

import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChunkGenAncientWorld implements IChunkGenerator {
    private final Biome biome = InitBiomes.ANCIENT_LABYRINTH;
    private final World world;

    public ChunkGenAncientWorld(World worldIn) {
        this.world = worldIn;
    }

    @Override
    public @NotNull Chunk generateChunk(int parChunkX, int parChunkZ) {
        Chunk chunk = new Chunk(this.world, parChunkX, parChunkZ);
        Arrays.fill(chunk.getBiomeArray(), (byte) Biome.getIdForBiome(this.biome));
        return chunk;
    }

    @Override
    public void populate(int x, int z) {}

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {}

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}
