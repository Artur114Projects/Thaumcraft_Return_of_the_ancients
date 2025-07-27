package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.blocks.BlockTaintVoidStone;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
            Biome biome = worldIn.getBiome(position);
            int h = biome == InitBiome.INFERNAL_CRATER || biome == InitBiome.TAINT_WASTELAND ? 0 : worldIn.getSeaLevel() - 10;
            for (blockPos.setPos(position); blockPos.getY() > h; blockPos.down()) {
                IBlockState replacingState = worldIn.getBlockState(blockPos);
                Material material = replacingState.getMaterial();
                Block block = replacingState.getBlock();

                if (replacingState.getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT || block.isAir(replacingState, worldIn, blockPos) || material == Material.SNOW || material.isLiquid() || block instanceof BlockTC || block.hasTileEntity(replacingState) || !block.isOpaqueCube(replacingState) || block == Blocks.BEDROCK || block == InitBlocks.CLEANED_VOID_STONE) {
                    continue;
                }

                if (biome == InitBiome.INFERNAL_CRATER) {
                    ExtendedBlockStorage storage = blockPos.ebs(worldIn);
                    IBlockState state1 = blockPos.getY() <= 20 ? InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState() : InitBlocks.TAINT_VOID_STONE.getDefaultState();
                    storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, state1);
                    continue;
                }

                for (EnumFacing facing : EnumFacing.values()) {
                    blockPos.offset(facing);
                    IBlockState state = worldIn.getBlockState(blockPos);
                    boolean isNeedReplace = state.getMaterial() == Material.AIR || !state.getBlock().isOpaqueCube(state) || state.getMaterial() == Material.SNOW;

                    blockPos.offset(facing.getOpposite());

                     if (isNeedReplace) {
                         ExtendedBlockStorage storage = blockPos.ebs(worldIn);
                         IBlockState state1 = blockPos.getY() <= 20 ? InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState() : InitBlocks.TAINT_VOID_STONE.getDefaultState();
                         storage.set(blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15, state1);
                         break;
                    }
                }
            }
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
            return true;
        }
    }

    public static class WorldGenAddTaintFeature extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
            if (rand.nextInt(33) == 0) {
                blockPos.setPos(position);
                if (blockPos.getY() < 100 && worldIn.getBlockState(blockPos).getBlock() == BlocksTC.taintSoil) {
                    IBlockState state = BlocksTC.taintFeature.getDefaultState();
                    blockPos.up();
                    if (worldIn.isAirBlock(blockPos)) {
                        worldIn.setBlockState(blockPos, state.withProperty(BlockDirectional.FACING, EnumFacing.UP), 2);
                    }
                }
            }
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
            return true;
        }
    }

    public static class WorldGenAddSnow extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(position);
            if (blockPos.getY() >= 100 && worldIn.getBiome(position) != InitBiome.TAINT_WASTELAND) {
                if (blockPos.getY() > 110) {
                    IBlockState state = worldIn.getBlockState(blockPos);
                    if (state.getBlock() == BlocksTC.taintSoil || state.getMaterial() == Material.SNOW) blockPos.down();
                    worldIn.setBlockState(blockPos.up(), Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, MathHelper.clamp((int) MiscHandler.interpolate(1, 8, (blockPos.getY() - 110.0F) / (148.0F - 110.0F)), 1, 8)));
                } else if (rand.nextBoolean()) {
                    IBlockState state = worldIn.getBlockState(blockPos);
                    if (state.getMaterial() == Material.SNOW) blockPos.down();
                    worldIn.setBlockState(blockPos.up(), Blocks.SNOW_LAYER.getDefaultState());
                }
            }
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
            return true;
        }
    }

    public static class WorldGenInfernalCircle extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            if (rand.nextFloat() > 0.1) {
                return false;
            }
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(position);
            blockPos.add(rand.nextInt(16), 0, rand.nextInt(16));
            if (MiscHandler.getBiomeIdOnPos(worldIn, blockPos) != Biome.getIdForBiome(InitBiome.TAINT_WASTELAND)) {
                UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
                return false;
            }
            genCircle(worldIn, rand, blockPos, 4 + (rand.nextInt(2) + 1));
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
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
                        if (worldIn.getBlockState(pos).getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT) {
                            worldIn.setBlockState(pos, state);
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
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
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

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
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
            UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(position);

            for (int x = 0; x != 16; x++) {
                for (int z = 0; z != 16; z++) {
                    pos.setPos(position).add(x, 0, z).setWorldY(world);

                    int h = pos.getY();

                    for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                        pos.pushPos();
                        boolean flag = false;
                        IBlockState state = world.getBlockState(pos);
                        if (pos.offset(facing).setWorldY(world).getY() < h && (state.getBlock() instanceof BlockTaintVoidStone || state.getMaterial() == Material.LAVA)) {
                            if (h < 60 && h - pos.getY() == 1) {
                                if (state.getMaterial() != Material.LAVA) {
                                    world.setBlockState(pos.down(), InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                                    world.setBlockState(pos.up(), Blocks.LAVA.getDefaultState());
                                }

                                for (EnumFacing facing1 : EnumFacing.HORIZONTALS) {
                                    pos.pushPos();
                                    pos.offset(facing1);
                                    if (world.getBlockState(pos).getMaterial() != Material.LAVA) {
                                        world.setBlockState(pos, InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                                    }
                                    pos.popPos();
                                }
                            }

                            flag = true;
                        }
                        pos.popPos();

                        if (flag) {
                            world.setBlockState(pos, InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                        }
                    }

                }
            }

            UltraMutableBlockPos.returnBlockPosToPoll(pos);
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
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(pos);
            for (int i = 0; i != 16; i++) {
                blockPos.pushPos();
                blockPos.add(rand.nextInt(16), rand.nextInt(20), rand.nextInt(16));
                if (world.getBiome(blockPos) != InitBiome.INFERNAL_CRATER) {
                    UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
                    return false;
                }

                ArrayDeque<BlockPos> queue = new ArrayDeque<>(32);
                int vineSize = rand.nextInt(8 + 1) + 8;
                int addedBlocks = 0;

                queue.addLast(blockPos.toImmutable());

                while (!queue.isEmpty() && addedBlocks < vineSize) {
                    BlockPos pos1 = queue.poll();

                    if (pos1 != null) {
                        IBlockState state = world.getBlockState(pos1);
                        if (state.getBlock() != InitBlocks.CLEANED_VOID_STONE && state.getMaterial() != Material.AIR && !state.getMaterial().isLiquid()) {
                            world.setBlockState(pos1, InitBlocks.CLEANED_VOID_STONE.getDefaultState());
                            addedBlocks++;

                            for (EnumFacing facing : EnumFacing.VALUES) {
                                queue.addLast(pos1.offset(facing));
                            }
                        }
                    }
                }
                blockPos.popPos();
            }

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
            return true;
        }

        @Override
        protected Biome[] biomesWhiteList() {
            return new Biome[] {InitBiome.INFERNAL_CRATER};
        }
    }
}
