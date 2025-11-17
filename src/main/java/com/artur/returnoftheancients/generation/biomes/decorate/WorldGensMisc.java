package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.blocks.BlockTaintVoidStone;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.util.math.MathUtils;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenerator;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;

import java.util.ArrayDeque;
import java.util.Random;

public class WorldGensMisc {
    public final WorldGenerator REPLACE_VISIBLE_BLOCKS = new WorldGenReplaceVisibleBlocks();
    public final WorldGenerator ADD_TAINT_FEATURE = new WorldGenAddTaintFeature();
    public final WorldGenerator CLEAN_VOID_STONE = new WorldGenCleanedVoidStone();
    public final WorldGenerator INFERNAL_CIRCLE = new WorldGenInfernalCircle();
    public final WorldGenerator REMOVE_LIQUIDS = new WorldGenRemoveLiquids();
    public final WorldGenerator LAVA_STAIRS = new WorldGenLavaStairs();
    public final WorldGenerator ADD_SNOW = new WorldGenAddSnow();


    public static class WorldGenReplaceVisibleBlocks extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
            Biome biome = worldIn.getBiome(position);
            int h = this.replaceHFromBiome(biome, rand);
            for (blockPos.setPos(position); blockPos.getY() > h; blockPos.down()) {
                ExtendedBlockStorage storage = blockPos.ebs(worldIn);
                if (storage == null) continue;
                IBlockState replacingState = storage.get(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15);
                Material material = replacingState.getMaterial();
                Block block = replacingState.getBlock();

                if (replacingState.getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT || block.isAir(replacingState, worldIn, blockPos) || material == Material.SNOW || material.isLiquid() || block instanceof BlockTC || block.hasTileEntity(replacingState) || !block.isOpaqueCube(replacingState) || block == Blocks.BEDROCK || block == InitBlocks.CLEANED_VOID_STONE) {
                    continue;
                }

                IBlockState state1 = blockPos.getY() <= 20 ? InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState() : InitBlocks.TAINT_VOID_STONE.getDefaultState();
                storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, state1);
            }
            UltraMutableBlockPos.release(blockPos);
            return true;
        }

        private int replaceHFromBiome(Biome biome, Random rand) {
            if (biome == InitBiome.TAINT_WASTELAND) {
                return 0;
            } else if (biome == InitBiome.INFERNAL_CRATER) {
                return 0;
            } else if (biome == InitBiome.TAINT_SEA) {
                return 24 + rand.nextInt(4);
            } else {
                return 32 + rand.nextInt(8);
            }
        }
    }

    public static class WorldGenAddTaintFeature extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
            if (rand.nextInt(33) == 0) {
                blockPos.setPos(position);
                if (blockPos.getY() < 100 && worldIn.getBlockState(blockPos).getBlock() == BlocksTC.taintSoil) {
                    IBlockState state = BlocksTC.taintFeature.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP);
                    ExtendedBlockStorage storage = blockPos.up().ebs(worldIn);
                    if (storage != null) {
                        storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, state);
                        worldIn.getChunkFromBlockCoords(position).resetRelightChecks();
                        worldIn.checkLight(blockPos);
                    }
                }
            }
            UltraMutableBlockPos.release(blockPos);
            return true;
        }
    }

    public static class WorldGenAddSnow extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain().setPos(position);
            if (blockPos.getY() >= 100 && worldIn.getBiome(position) != InitBiome.TAINT_WASTELAND) {
                if (blockPos.getY() > 110) {
                    IBlockState state = worldIn.getBlockState(blockPos);
                    if (state.getBlock() == BlocksTC.taintSoil || state.getMaterial() == Material.SNOW) blockPos.down();
                    ExtendedBlockStorage storage = blockPos.up().ebs(worldIn);
                    if (storage != null) {
                        storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, MathHelper.clamp((int) MathUtils.interpolate(1, 8, (blockPos.getY() - 110.0F) / (148.0F - 110.0F)), 1, 8)));
                    }
                } else if (rand.nextBoolean()) {
                    IBlockState state = worldIn.getBlockState(blockPos);
                    if (state.getMaterial() == Material.SNOW) blockPos.down();
                    ExtendedBlockStorage storage = blockPos.up().ebs(worldIn);
                    if (storage != null) {
                        storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, Blocks.SNOW_LAYER.getDefaultState());
                    }
                }
            }
            UltraMutableBlockPos.release(blockPos);
            return true;
        }
    }

    public static class WorldGenInfernalCircle extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            if (rand.nextFloat() > 0.1) {
                return false;
            }
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain().setPos(position);
            blockPos.add(rand.nextInt(16), 0, rand.nextInt(16));
            if (MiscHandler.getBiomeIdOnPos(worldIn, blockPos) != Biome.getIdForBiome(InitBiome.TAINT_WASTELAND)) {
                UltraMutableBlockPos.release(blockPos);
                return false;
            }
            genCircle(worldIn, rand, blockPos, 4 + (rand.nextInt(2) + 1));
            UltraMutableBlockPos.release(blockPos);
            return true;
        }

        private void genCircle(World worldIn, Random rand, UltraMutableBlockPos pos, int radius) {
            for (int range1 = 1; range1 <= radius; range1++) {
                int pointsOnCircle = 16 * range1;
                for (int i = 0; i < pointsOnCircle; i++) {
                    float angle = (float) ((Math.PI * 2.0) * i / pointsOnCircle);
                    int xOffset = (int) (range1 * MathHelper.cos(angle));
                    int zOffset = (int) (range1 * MathHelper.sin(angle));

                    pos.pushPos();
                    pos.add(xOffset, 0, zOffset).setWorldY(worldIn);
                    if (range1 != radius || (rand == null || rand.nextInt(4) == 0)) {
                        IBlockState state = (range1 < radius - 2) && (rand == null || rand.nextFloat() < 0.5) ? Blocks.LAVA.getDefaultState() : InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState();
                        Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
                        ExtendedBlockStorage storage = pos.ebs(worldIn);
                        if (storage != null && storage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15).getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT) {
                            storage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);
                            chunk.resetRelightChecks();
                        }
                    }
                    pos.popPos();
                }
            }
        }
    }

    public static class WorldGenRemoveLiquids extends WorldGeneratorBiomeWhiteList {

        public WorldGenRemoveLiquids() {
            super(CheckType.BLOCK);
        }

        @Override
        public boolean gen(World world, Random rand, BlockPos pos) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
            Chunk chunk = world.getChunkFromBlockCoords(pos);

            ExtendedBlockStorage[] blockStorages = chunk.getBlockStorageArray();
            for (blockPos.setPos(pos); blockPos.getY() > 0; blockPos.down()) {
                ExtendedBlockStorage storage = blockStorages[blockPos.getY() >> 4];
                if (storage == null){
                    continue;
                }
                if (storage.get(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15).getMaterial() == Material.WATER) {
                    storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, Blocks.AIR.getDefaultState());
                }
            }

            UltraMutableBlockPos.release(blockPos);
            chunk.markDirty();
            return true;
        }

        @Override
        protected Biome[] biomesWhiteList() {
            return new Biome[] {InitBiome.TAINT_WASTELAND, InitBiome.INFERNAL_CRATER};
        }
    }

    public static class WorldGenLavaStairs extends WorldGeneratorBiomeWhiteList {

        public WorldGenLavaStairs() {
            super(CheckType.FAST);
        }

        @Override
        public boolean gen(World world, Random rand, BlockPos position) {
            UltraMutableBlockPos pos = UltraMutableBlockPos.obtain().setPos(position);

            for (int x = 0; x != 16; x++) {
                for (int z = 0; z != 16; z++) {
                    if (!CheckType.BLOCK.check(world, pos.setPos(position).add(x, 0, z), this.biomes)) {
                        continue;
                    }
                    pos.setWorldY(world);

                    int h = pos.getY();

                    for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                        pos.pushPos();
                        boolean flag = false;
                        IBlockState state = world.getBlockState(pos);
                        if (pos.offset(facing).setWorldY(world).getY() < h && (state.getBlock() instanceof BlockTaintVoidStone || state.getMaterial() == Material.LAVA)) {
                            if (h < 60 && h - pos.getY() == 1) {
                                if (state.getMaterial() != Material.LAVA) {
                                    ExtendedBlockStorage storage1 = pos.down().ebs(world);
                                    storage1.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                                    ExtendedBlockStorage storage2 = pos.up().ebs(world);
                                    storage2.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, Blocks.LAVA.getDefaultState());
                                    world.checkLight(pos);
                                }

                                for (EnumFacing facing1 : EnumFacing.HORIZONTALS) {
                                    pos.pushPos();
                                    pos.offset(facing1);
                                    ExtendedBlockStorage storage = pos.ebs(world);
                                    if (storage != null && storage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15).getMaterial() != Material.LAVA) {
                                        storage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                                    }
                                    pos.popPos();
                                }
                            }

                            flag = true;
                        }
                        pos.popPos();

                        if (flag) {
                            ExtendedBlockStorage storage = pos.ebs(world);
                            if (storage != null) {
                                storage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                                world.checkLight(pos);
                            }
                        }
                    }

                }
            }

            world.getChunkFromBlockCoords(position).resetRelightChecks();
            UltraMutableBlockPos.release(pos);
            return true;

        }

        @Override
        protected Biome[] biomesWhiteList() {
            return new Biome[] {InitBiome.TAINT_WASTELAND, InitBiome.INFERNAL_CRATER};
        }
    }

    public static class WorldGenCleanedVoidStone extends WorldGeneratorBiomeWhiteList {

        protected WorldGenCleanedVoidStone() {
            super(CheckType.FAST);
        }

        @Override
        public boolean gen(World world, Random rand, BlockPos pos) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain().setPos(pos);
            for (int i = 0; i != 16; i++) {
                blockPos.pushPos();
                blockPos.add(rand.nextInt(16), rand.nextInt(20), rand.nextInt(16));
                if (world.getBiome(blockPos) != InitBiome.INFERNAL_CRATER) {
                    UltraMutableBlockPos.release(blockPos);
                    return false;
                }

                ArrayDeque<BlockPos> queue = new ArrayDeque<>(32);
                int vineSize = rand.nextInt(8 + 1) + 8;
                int addedBlocks = 0;

                queue.addLast(blockPos.toImmutable());

                while (!queue.isEmpty() && addedBlocks < vineSize) {
                    BlockPos pos1 = queue.poll();

                    if (pos1 != null) {
                        Chunk chunk = world.getChunkFromBlockCoords(pos1);
                        ExtendedBlockStorage storage = pos1.getY() >> 4 < 16 && pos1.getY() >> 4 >= 0 ? chunk.getBlockStorageArray()[pos1.getY() >> 4] : null;
                        if (storage != null) {
                            IBlockState state = storage.get(pos1.getX() & 15, pos1.getY() & 15, pos1.getZ() & 15);
                            if (state.getBlock() != InitBlocks.CLEANED_VOID_STONE && state.getMaterial() != Material.AIR && !state.getMaterial().isLiquid()) {
                                storage.set(pos1.getX() & 15, pos1.getY() & 15, pos1.getZ() & 15, InitBlocks.CLEANED_VOID_STONE.getDefaultState());
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

            UltraMutableBlockPos.release(blockPos);
            return true;
        }

        @Override
        protected Biome[] biomesWhiteList() {
            return new Biome[] {InitBiome.INFERNAL_CRATER};
        }
    }
}
