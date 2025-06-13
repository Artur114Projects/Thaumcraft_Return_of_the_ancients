package com.artur.returnoftheancients.ancientworld.map.build;

import com.artur.returnoftheancients.ancientworld.map.utils.maps.InteractiveMap;
import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuildResult;
import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuilder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class AncientLayer1Builder extends SlowBuilder {
    private static int buildersCount = 0;

    private final InteractiveMap map;
    private final Random buildRand;
    private final ChunkPos center;
    private final World world;
    private boolean isCleared;
    private int currentIndex;

    public AncientLayer1Builder(InteractiveMap map, World world, Random buildRand, ChunkPos center) {
        super(1);
        this.buildRand = buildRand;
        this.center = center;
        this.world = world;
        this.map = map;

        buildersCount++;
    }

    @Override
    public @NotNull SlowBuildResult build() {
        if (!this.isCleared) {
            this.clearAll(this.world); this.isCleared = true; // TODO: 08.06.2025 Rewrite!!!
        }

        int x = this.center.x + (this.map.size() / 2) - (this.currentIndex % this.map.size());
        int z = this.center.z + (this.map.size() / 2) - (this.currentIndex / this.map.size());

        ChunkPos pos = new ChunkPos(x, z);

        this.map.build(this.currentIndex++, this.world, pos, this.buildRand);

        if (this.currentIndex >= this.map.area()) {

            return SlowBuildResult.FINISH;
        } else {
            return SlowBuildResult.SUCCESSFULLY;
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();

        buildersCount--;
    }

    @Override
    public boolean isReady(long tickCount) {
        return tickCount % buildersCount == 0;
    }

    private void clearChunk(World world, int x, int z) {
        Chunk chunk = world.getChunkFromChunkCoords(x, z);
        Arrays.fill(chunk.getBlockStorageArray(), null);
        chunk.markDirty();
    }

    private void clearChunk(World world, ChunkPos pos) {
        this.clearChunk(world, pos.x, pos.z);
    }

    private void clearAll(World world) {
        for (int i = 0; i != this.map.area(); i++) {
            int x = this.center.x + (this.map.size() / 2) - (i % this.map.size());
            int z = this.center.z + (this.map.size() / 2) - (i / this.map.size());

            this.clearChunk(world, x, z);
        }
    }
}
