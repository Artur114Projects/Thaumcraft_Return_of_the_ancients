package com.artur114.thaumrota.common.biomes.decorate;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.*;

public abstract class WorldGeneratorOreVine extends WorldGeneratorBiomeWhiteList {
    private final Set<IBlockState> statesCanReplace;
    private final IBlockState ore;
    private final int minVeneSize;
    private final int maxVeneSize;
    private final int minY, maxY;
    private final int vinesCount;

    protected WorldGeneratorOreVine(Collection<IBlockState> statesCanReplace, IBlockState ore, int vinesCount, int minVeneSize, int maxVeneSize, int minY, int maxY) {
        super(CheckType.FAST);
        this.statesCanReplace = new HashSet<>(statesCanReplace);
        this.minVeneSize = minVeneSize;
        this.maxVeneSize = maxVeneSize;
        this.vinesCount = vinesCount;
        this.minY = minY;
        this.maxY = maxY;
        this.ore = ore;
    }

    @Override
    public boolean gen(World world, Random rand, BlockPos pos) {
        PosMc3IM blockPos = PosMc3IM.obtain().set(pos);
        try {
            for (int i = 0; i != this.vinesCount; i++) {
                blockPos.pushPos();
                blockPos.add(rand.nextInt(16), rand.nextInt(this.maxY - this.minY) + this.minY, rand.nextInt(16));
                if (!this.isWhitelistedBiome(world.getBiome(blockPos))) {
                    continue;
                }

                ArrayDeque<BlockPos> queue = new ArrayDeque<>(32);
                int vineSize = rand.nextInt(this.maxVeneSize - this.minVeneSize) + this.minVeneSize;
                int addedBlocks = 0;

                queue.addLast(blockPos.toImmutable());

                while (!queue.isEmpty() && addedBlocks < vineSize) {
                    BlockPos pos1 = queue.poll();

                    if (pos1 != null) {
                        Chunk chunk = world.getChunkFromBlockCoords(pos1);
                        ExtendedBlockStorage storage = pos1.getY() >> 4 < 16 && pos1.getY() >> 4 >= 0 ? chunk.getBlockStorageArray()[pos1.getY() >> 4] : null;
                        if (storage != null) {
                            IBlockState state = storage.get(pos1.getX() & 15, pos1.getY() & 15, pos1.getZ() & 15);
                            if (this.statesCanReplace.contains(state) && state.getMaterial() != Material.AIR && !state.getMaterial().isLiquid()) {
                                storage.set(pos1.getX() & 15, pos1.getY() & 15, pos1.getZ() & 15, this.ore);
                                addedBlocks++;

                                for (EnumFacing facing : EnumFacing.VALUES) {
                                    queue.addLast(pos1.offset(facing));
                                }
                            }
                        }
                    }
                }
                blockPos.popPos();
            }
        } finally {
            PosMc3IM.release(blockPos);
        }

        return true;
    }
}