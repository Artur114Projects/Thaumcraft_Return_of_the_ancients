package com.artur.returnoftheancients.generation.portal.generators;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;

import java.util.*;

public class GenAncientArch { // TODO: Улучшить!
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private ArchChunk[] archChunksCanFall = null;
    private TupleI[] terrainHeightArray = null;
    private ArchSegment[] archSegments = null;
    private EnumFacing offset2Arch = null;
    private EnumFacing genOffset = null;
    private BlockPos start = null;
    private BlockPos end = null;
    private World world = null;
    private Random rand = null;
    private int length = 0;
    private int dx = 0;
    private int dy = 0;
    private int dz = 0;

    public void generate(World world, BlockPos start, BlockPos end, EnumFacing.AxisDirection offsetAxis2Arch, boolean isBroken) {
        this.initObjectData(world, start, end, offsetAxis2Arch);
        this.initTerrainHeightData();
        this.genArchAndInitSegments();
        if (isBroken) {
            this.initArchChunksCanFall();
            this.fallArchChunks();
        }
        this.resetObjectData();

        if (TRAConfigs.Any.debugMode) System.out.println("Generated ancient arch! start:" + start + " end:" + end);
    }

    private void genArchAndInitSegments() {
        archSegments = new ArchSegment[length];

        for (int i = 0; i != length; i++) {
            int yOffset = getYOffsetFromX(i);
            int prevYOffset = yOffset;
            int nextYOffset = yOffset;

            if (i != 0) {
                prevYOffset = getYOffsetFromX(i - 1);
            }
            if (i != length - 1) {
                nextYOffset = getYOffsetFromX(i + 1);
            }

            ArchSegment segment = new ArchSegment(world, yOffset, i, start, genOffset, offset2Arch);

            segment.generate(prevYOffset, nextYOffset);

            archSegments[i] = segment;
        }
    }

    private void fallArchChunks() {
        boolean flag = false;
        for (ArchChunk chunk : archChunksCanFall) {
            if (rand.nextBoolean()) {
                flag = true;
                chunk.fall();
            }
        }

        if (!flag) {
            this.fallArchChunks();
        }
    }

    private void initArchChunksCanFall() {
        ArchChunkBuilder builder = new ArchChunkBuilder(world, archSegments, terrainHeightArray);
        List<ArchChunk> archChunks = new ArrayList<>();
        boolean building = false;

        for (int i = 0; i != length; i++) {
            if (!this.hasSupport(i, 16)) {
                building = true;
                builder.build(i);
            } else if (building){
                archChunks.add(builder.finishBuild());
                building = false;
            }
        }

        archChunksCanFall = archChunks.toArray(new ArchChunk[0]);
    }

    private void initTerrainHeightData() {
        terrainHeightArray = new TupleI[length];
        blockPos.setPos(start);

        for (int i = 0; i != length; i++) {
            blockPos.pushPos();

            blockPos.offset(genOffset, i);

            int y0 = blockPos.setWorldY(world, true, Blocks.AIR, BlocksTC.taintFeature, BlocksTC.taintLog).getY();
            int y1 = blockPos.offset(offset2Arch).setWorldY(world, true, Blocks.AIR, BlocksTC.taintFeature, BlocksTC.taintLog).getY();

            terrainHeightArray[i] = new TupleI(y0, y1);

            blockPos.popPos();
        }
    }

    private void initObjectData(World world, BlockPos start, BlockPos end, EnumFacing.AxisDirection offsetAxis2Arch) {
        this.rand = new Random(world.getSeed() + start.toLong() + end.toLong());
        this.rand.nextInt();
        this.world = world;
        this.start = start;
        this.end = end;

        this.dx = end.getX() - start.getX();
        this.dz = end.getZ() - start.getZ();
        this.dy = end.getY() - start.getY();

        this.length = Math.max(Math.abs(dx), Math.abs(dz));

        if (Math.abs(dx) > Math.abs(dz)) {
            this.offset2Arch = EnumFacing.getFacingFromAxis(offsetAxis2Arch, EnumFacing.Axis.Z);
            this.genOffset = EnumFacing.getFacingFromAxis(dx > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X);
        } else {
            this.offset2Arch = EnumFacing.getFacingFromAxis(offsetAxis2Arch, EnumFacing.Axis.X);
            this.genOffset = EnumFacing.getFacingFromAxis(dz > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z);
        }
    }

    private void resetObjectData() {
        this.blockPos.setToZero();

        this.terrainHeightArray = null;
        this.archChunksCanFall = null;
        this.archSegments = null;

        this.offset2Arch = null;
        this.genOffset = null;
        this.start = null;
        this.world = null;
        this.rand = null;
        this.end = null;

        this.length = 0;
        this.dx = 0;
        this.dy = 0;
        this.dz = 0;
    }

    private int getYOffsetFromX(int x) {
        double t = (double) x / length;

        int h = MathHelper.floor(Math.abs(dy) + MathHelper.clamp(length / 16.0D, 0.0D, 16.0D));

        double y = start.getY();
        y += dy * t;
        y += h * (4 * t * (1 -  t));

        return MathHelper.floor(y);
    }

    private boolean hasSupport(int segmentIdIn, int range) {
        int startId = segmentIdIn - range;
        int endId = segmentIdIn + range;
        if (startId < 0) {
            return true;
        }
        if (endId >= length) {
            return true;
        }

        for (int i = startId; i <= endId; i++) {
            if (hasSupport(i)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasSupport(int segmentIdIn) {
        ArchSegment segment = archSegments[segmentIdIn];
        TupleI tuple = terrainHeightArray[segmentIdIn];
        return tuple.getMost() - (segment.getY() - 1) >= 0;
    }

    private static class ArchSegment {
        private final Set<Tuple<BlockPos, IBlockState>> generatedBlocks = new HashSet<>();
        private final EnumFacing genOffsetFacing;
        private final EnumFacing offset2Arch;
        private final BlockPos startPos;
        private int abstractMove = -1;
        private final int genOffset;
        private final World world;
        private final int yOffset;
        private int minY;


        protected ArchSegment(World world, int yOffset, int genOffset, BlockPos startPos, EnumFacing genOffsetFacing, EnumFacing offset2Arch) {
            this.genOffsetFacing = genOffsetFacing;
            this.offset2Arch = offset2Arch;
            this.genOffset = genOffset;
            this.startPos = startPos;
            this.yOffset = yOffset;
            this.world = world;
        }

        protected void generate(int prevYOffset, int nextYOffset) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(startPos);
            blockPos.offset(genOffsetFacing, genOffset);
            blockPos.setY(yOffset);

            if (yOffset != prevYOffset || yOffset != nextYOffset) {
                blockPos.pushPos();
                blockPos.setY(yOffset != prevYOffset ? prevYOffset : nextYOffset);
                setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
                setBlockState(blockPos.offset(this.offset2Arch), BlocksTC.stoneEldritchTile.getDefaultState());
                blockPos.popPos();
            }

            setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
            setBlockState(blockPos.addY(1), BlocksTC.stoneAncient.getDefaultState());

            setBlockState(blockPos.offset(this.offset2Arch).addY(-1), BlocksTC.stoneEldritchTile.getDefaultState());
            setBlockState(blockPos.addY(1), BlocksTC.stoneAncient.getDefaultState());

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);

            this.calculateMinY();
        }

        protected void move() {
            if (abstractMove != -1) {
                move(yOffset + (abstractMove - minY));
                abstractMove = -1;
            }
        }

        protected void move(int toY) {
            if (toY == yOffset) {
                return;
            }

            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
            Set<Tuple<BlockPos, IBlockState>> localBlocks = new HashSet<>(generatedBlocks);
            generatedBlocks.clear();

            for (Tuple<BlockPos, IBlockState> tuple : localBlocks) {
                world.setBlockToAir(tuple.getFirst());

                blockPos.setPos(tuple.getFirst()).addY(-yOffset).addY(toY);
                setBlockState(blockPos, tuple.getSecond());
            }

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);

            this.calculateMinY();
        }

        protected void moveAbstract(int toY) {
            abstractMove = toY;
        }

        protected void moveDownAbstract() {
            if (abstractMove == -1) {
                abstractMove = this.getY();
            }

            abstractMove--;
        }

        protected int getAbstractY() {
            return abstractMove;
        }

        protected int getY() {
            return minY;
        }

        public int getYOffset() {
            return yOffset;
        }

        protected BlockPos getStartPosForSegment() {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(startPos);
            blockPos.offset(genOffsetFacing, genOffset);
            blockPos.setY(yOffset);
            BlockPos ret = blockPos.toImmutable();
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
            return ret;
        }

        protected boolean isOnGround(TupleI terrainHeight) {
            return terrainHeight.getMost() - (this.getY() - 1) >= 0;
        }

        protected boolean isConnected(ArchSegment segmentIn) {
            return Math.abs(this.getY() - segmentIn.getY()) < Math.abs(this.genOffset - segmentIn.genOffset) * 2;
        }

        protected boolean isAbstractOnGround(TupleI terrainHeight) {
            if (this.getAbstractY() == -1) {
                return false;
            }
            return terrainHeight.getMost() - (this.getAbstractY() - 1) >= 0;
        }

        private void calculateMinY() {
            this.minY = generatedBlocks.stream().min(Comparator.comparingInt(v0 -> v0.getFirst().getY())).get().getFirst().getY();
        }

        private void setBlockState(BlockPos pos, IBlockState state) {
            generatedBlocks.add(new Tuple<>(pos.toImmutable(), state));
            world.setBlockState(pos, state);
        }
    }

    private static class ArchChunk {
        private final TupleI[] terrainHeightArray;
        private final ArchSegment[] archSegments;
        private final BlockPos start;
        private final BlockPos end;
        private final World world;
        private final int length;

        protected ArchChunk(World world, int startId, int endId, ArchSegment[] archSegments, TupleI[] terrainHeightArray) {
            this.length = endId - startId + 1;
            this.archSegments = new ArchSegment[length];
            this.terrainHeightArray = new TupleI[length];
            this.start = archSegments[startId].getStartPosForSegment();
            this.end = archSegments[endId].getStartPosForSegment();
            this.world = world;

            for (int i = startId; i <= endId; i++) {
                this.archSegments[i - startId] = archSegments[i];
                this.terrainHeightArray[i - startId] = terrainHeightArray[i];
            }
        }

        protected void fall() {
            while (true) {
                boolean flag = false;
                for (int i = 0; i != length; i++) {
                    ArchSegment segment = archSegments[i];
                    TupleI tupleI = terrainHeightArray[i];
                    if (segment.isAbstractOnGround(tupleI)) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    break;
                }

                for (ArchSegment segment : archSegments) {
                    segment.moveDownAbstract();
                }
            }

            for (ArchSegment segment : archSegments) {
                segment.move();
            }

            if ((!hasSupport(0) || !hasSupport(length - 1) && length > 16)) {
                ArchChunkBuilder builder = new ArchChunkBuilder(world, archSegments, terrainHeightArray);
                List<ArchChunk> archChunks = new ArrayList<>();
                boolean building = false;

                for (int i = 0; i != length; i++) {
                    if (!this.hasSupport(i, 16)) {
                        building = true;
                        builder.build(i);
                    } else if (building) {
                        archChunks.add(builder.finishBuild());
                        building = false;
                    }
                }

                if (building) {
                    archChunks.add(builder.finishBuild());
                }

                for (ArchChunk archChunk : archChunks) {
                    archChunk.fall();
                }
            }
        }

        private void move(int toY, int from) {
            for (ArchSegment segment : archSegments) {
                segment.move((segment.getYOffset() - from) + toY);
            }
        }

        private boolean hasSupport(int segmentIdIn, int range) {
            int startId = segmentIdIn - range;
            int endId = segmentIdIn + range;

            ArchSegment segment = archSegments[segmentIdIn];

            for (int i = startId; i <= endId; i++) {
                if (i < 0) {
                    continue;
                }
                if (i >= length) {
                    continue;
                }

                if (hasSupport(i)) {
                    return true;
                }
            }

            return false;
        }

        private boolean hasSupport(int segmentIdIn) {
            ArchSegment segment = archSegments[segmentIdIn];
            TupleI tuple = terrainHeightArray[segmentIdIn];
            return tuple.getMost() - (segment.getY() - 1) >= 0;
        }
    }

    private static class ArchChunkBuilder {
        private final List<Integer> archSegmentsI = new ArrayList<>(20);
        private final TupleI[] terrainHeightArray;
        private final ArchSegment[] archSegments;
        private final World world;

        protected ArchChunkBuilder(World world, ArchSegment[] archSegments, TupleI[] terrainHeightArray) {
            this.terrainHeightArray = terrainHeightArray;
            this.archSegments = archSegments;
            this.world = world;
        }

        protected void build(int segmentIdIn) {
            archSegmentsI.add(segmentIdIn);
        }

        protected ArchChunk finishBuild() {
            int start = archSegmentsI.stream().min(Comparator.comparingInt(Integer::intValue)).get();
            int end = archSegmentsI.stream().max(Comparator.comparingInt(Integer::intValue)).get();
            archSegmentsI.clear();

            return new ArchChunk(world, start, end, archSegments, terrainHeightArray);
        }
    }

    private static class TupleI extends Tuple<Integer, Integer> {

        public TupleI(Integer aIn, Integer bIn) {
            super(aIn, bIn);
        }

        protected int getAverageValue() {
            return (getFirst() + getSecond()) / 2;
        }

        protected int getMost() {
            if (getFirst() > getSecond()) {
                return getFirst();
            } else {
                return getSecond();
            }
        }
    }
}