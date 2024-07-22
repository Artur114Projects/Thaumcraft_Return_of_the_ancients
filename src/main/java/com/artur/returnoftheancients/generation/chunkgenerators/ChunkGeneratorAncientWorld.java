package com.artur.returnoftheancients.generation.chunkgenerators;

import com.artur.returnoftheancients.generation.biomes.BiomeAncientLabyrinth;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChunkGeneratorAncientWorld implements IChunkGenerator {


    private final Biome biome = InitBiome.ANCIENT_LABYRINTH;
    private final World world;
    private final ChunkPrimer chunkPrimer = new ChunkPrimer();

    public ChunkGeneratorAncientWorld(World worldIn) {
        world = worldIn;
    }

    @Override
    public Chunk generateChunk(int parChunkX, int parChunkZ) {
        Chunk chunk = new Chunk(world, chunkPrimer, parChunkX, parChunkZ);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte) Biome.getIdForBiome(biome);
        }
        return chunk;
    }

    @Override
    public void populate(int x, int z) {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return true;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return BiomeAncientLabyrinth.spawnListEntryList;
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}
