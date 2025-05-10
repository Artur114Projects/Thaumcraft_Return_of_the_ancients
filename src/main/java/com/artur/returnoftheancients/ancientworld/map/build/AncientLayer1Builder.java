package com.artur.returnoftheancients.ancientworld.map.build;

import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuildResult;
import com.artur.returnoftheancients.structurebuilder.slowbuild.SlowBuilder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class AncientLayer1Builder extends SlowBuilder {
    private final StructuresMap map;
    private final Random buildRand;
    private final ChunkPos center;
    private final World world;
    private int currentIndex;

    public AncientLayer1Builder(StructuresMap map, World world, Random buildRand, ChunkPos center) {
        super(5);
        this.buildRand = buildRand;
        this.center = center;
        this.world = world;
        this.map = map;
    }

    @Override
    public @NotNull SlowBuildResult build() {
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

    private void clearChunk(World world, ChunkPos pos) {
        Arrays.fill(world.getChunkFromChunkCoords(pos.x, pos.z).getBlockStorageArray(), null);
        world.markBlockRangeForRenderUpdate(pos.getXStart(), 0, pos.getZStart(), pos.getXEnd(), 255, pos.getZEnd());
    }
}
