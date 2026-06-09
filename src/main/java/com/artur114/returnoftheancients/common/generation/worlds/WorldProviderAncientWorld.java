package com.artur114.returnoftheancients.common.generation.worlds;

import com.artur114.returnoftheancients.common.generation.chunkgens.ChunkGenAncientWorld;
import com.artur114.returnoftheancients.common.init.InitDimensions;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldProviderAncientWorld extends WorldProvider {

    public WorldProviderAncientWorld() {

    }

    @Override
    public @NotNull DimensionType getDimensionType() {
        return InitDimensions.ancient_world_dim_type;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public @NotNull IChunkGenerator createChunkGenerator() {
        return new ChunkGenAncientWorld(this.world);
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce(@NotNull Chunk chunk)
    {
        return false;
    }

    @Override
    public boolean canSnowAt(@NotNull BlockPos pos, boolean checkLight)
    {
        return false;
    }

    @Override
    public boolean hasSkyLight() {
        return false;
    }

    @Override
    public boolean canDoLightning(@NotNull Chunk chunk) {
        return false;
    }

    @Nullable
    @Override
    public MusicTicker.MusicType getMusicType() {
        return MusicTicker.MusicType.NETHER;
    }
}
