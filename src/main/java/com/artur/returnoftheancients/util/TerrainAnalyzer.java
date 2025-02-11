package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.util.context.MethodContext;
import com.artur.returnoftheancients.util.context.MethodParams4;
import com.artur.returnoftheancients.util.context.MethodsContextManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class TerrainAnalyzer {
    private static final AnalyzingArea EMPTY_AREA = new AnalyzingArea(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0));
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private final World world;

    private MethodsContextManager<Integer, MethodParams4<Integer, Integer, Integer, Integer>> CONTEXT_4_INT_RES_INT = new MethodsContextManager<>(20);
    private AnalyzingArea analyzingArea = EMPTY_AREA;
    private int[] heightMap = null;


    public TerrainAnalyzer(World world) {
        this.world = world;
    }

    public void startAnalyzing(ChunkPos pos) {
        this.resetObjectData();

        this.analyzingArea = new AnalyzingArea(pos);
    }

    public void startAnalyzing(BlockPos from, BlockPos to) {
        this.resetObjectData();

        this.analyzingArea = new AnalyzingArea(from, to);
    }

    @Nullable
    public MethodContext<Integer, MethodParams4<Integer, Integer, Integer, Integer>> get4IntRetIntMethodContext(String name) {
        if (!CONTEXT_4_INT_RES_INT.hasMethodContext(name)) {
            return null;
        }

        return CONTEXT_4_INT_RES_INT.getMethodContext(name);
    }

    public int getHeight(BlockPos pos) {
        blockPos.pushPos();

        this.analyzingArea.localize(blockPos.setPos(pos));

        int y = heightMap[blockPos.getX() + blockPos.getZ() * analyzingArea.getAreaXSize()];

        blockPos.popPos();

        return y;
    }

    public int getHeight(int x, int z) {
        this.checkOutOfBound(x, z);

        return heightMap[x + z * analyzingArea.getAreaXSize()];
    }

    public int getHeightVariation() {
        return getHeightVariation(0, 0, analyzingArea.getAreaXSize(), analyzingArea.getAreaZSize());
    }

    public int getHeightVariation(int inX, int inZ, int toX, int toZ) {
        this.checkOutOfBound(inX, inZ, toX, toZ);

        if (this.CONTEXT_4_INT_RES_INT.getMethodContext("getHeightVariation").hasResultFromParam(new MethodParams4<>(inX, inZ, toX, toZ))) {
            return this.CONTEXT_4_INT_RES_INT.getMethodContext("getHeightVariation").getResultFromParam(new MethodParams4<>(inX, inZ, toX, toZ));
        }
        if (heightMap == null) {
            loadHeightMap();
        }

        int areaX = this.analyzingArea.getAreaXSize();

        int min = world.getHeight();
        int max = 0;

        for (int x = inX; x != toX; x++) {
            for (int z = inZ; z != toZ; z++) {
                int height = heightMap[x + z * areaX];

                if (height < min) {
                    min = height;
                }

                if (height > max) {
                    max = height;
                }
            }
        }

        int heightVariation = max - min;

        this.CONTEXT_4_INT_RES_INT.onMethodInvoke("getHeightVariation", new MethodParams4<>(inX, inZ, toX, toZ), heightVariation);

        return heightVariation;
    }

    public int getTerrainFlow() {
        return getTerrainFlow(0, 0, analyzingArea.getAreaXSize(), analyzingArea.getAreaZSize());
    }

    public int getTerrainFlow(int inX, int inZ, int toX, int toZ) {
        this.checkOutOfBound(inX, inZ, toX, toZ);

        if (this.CONTEXT_4_INT_RES_INT.getMethodContext("getTerrainFlow").hasResultFromParam(new MethodParams4<>(inX, inZ, toX, toZ))) {
            return this.CONTEXT_4_INT_RES_INT.getMethodContext("getTerrainFlow").getResultFromParam(new MethodParams4<>(inX, inZ, toX, toZ));
        }
        if (heightMap == null) {
            loadHeightMap();
        }

        int terrainFlow = 0;

        int areaX = this.analyzingArea.getAreaXSize();
        int areaZ = this.analyzingArea.getAreaZSize();

        for (int x = inX; x != toX; x++) {
            for (int z = inZ; z != toZ; z++) {
                int height = heightMap[x + z * areaX];

                int[] heightArray = new int[4];
                Arrays.fill(heightArray, height);

                if (z + 1 < areaZ) {
                    heightArray[0] = heightMap[x + (z + 1) * areaX];
                }
                if (z - 1 >= 0) {
                    heightArray[1] = heightMap[x + (z - 1) * areaX];
                }
                if (x + 1 < areaX) {
                    heightArray[2] = heightMap[(x + 1) + z * areaX];
                }
                if (x - 1 >= 0) {
                    heightArray[3] = heightMap[(x - 1) + z * areaX];
                }

                for (int i = 0; i != 4; i++) {
                    if (heightArray[i] != height) {
                        terrainFlow += Math.abs(heightArray[i] - height);
                    }
                }
            }
        }

        this.CONTEXT_4_INT_RES_INT.onMethodInvoke("getTerrainFlow", new MethodParams4<>(inX, inZ, toX, toZ), terrainFlow);

        return terrainFlow;
    }

    public void updateHeightMap() {
        this.loadHeightMap();
    }

    private void loadHeightMap() {
        this.heightMap = new int[this.analyzingArea.getSpace()];

        int areaX = this.analyzingArea.getAreaXSize();
        int areaZ = this.analyzingArea.getAreaZSize();

        blockPos.pushPos();

        this.analyzingArea.offsetToStart(blockPos);

        for (int x = 0; x != areaX; x++) {
            for (int z = 0; z != areaZ; z++) {
                blockPos.pushPos();

                this.analyzingArea.offset(blockPos, x, z);
                this.setHeightToUMBP();

                this.heightMap[x + z * areaX] = blockPos.getY();

                blockPos.popPos();
            }
        }

        blockPos.popPos();
    }

    private void checkOutOfBound(int inX, int inZ, int toX, int toZ) {
        int areaX = this.analyzingArea.getAreaXSize();
        int areaZ = this.analyzingArea.getAreaZSize();

        if (inX < 0 || inX > areaX) {
            throw new IllegalArgumentException("Out of bound area! AreaXSize:" + areaX + "Index:" + inX);
        }
        if (toX < 0 || toX > areaX) {
            throw new IllegalArgumentException("Out of bound area! AreaXSize:" + areaX + "Index:" + toX);
        }
        if (inZ < 0 || inZ > areaX) {
            throw new IllegalArgumentException("Out of bound area! AreaZSize:" + areaZ + "Index:" + inZ);
        }
        if (toZ < 0 || toZ > areaX) {
            throw new IllegalArgumentException("Out of bound area! AreaXSize:" + areaX + "Index:" + toZ);
        }
    }

    private void checkOutOfBound(int x, int z) {
        int areaX = this.analyzingArea.getAreaXSize();
        int areaZ = this.analyzingArea.getAreaZSize();

        if (x < 0 || x >= areaX) {
            throw new IllegalArgumentException("Out of bound area! AreaXSize:" + areaX + "Index:" + x);
        }
        if (z < 0 || z >= areaX) {
            throw new IllegalArgumentException("Out of bound area! AreaXSize:" + areaX + "Index:" + z);
        }
    }


    private void setHeightToUMBP() {
        setHeightToUMBP(Blocks.AIR, Blocks.BEDROCK);
    }

    private void setHeightToUMBP(Block... ignoringBlocks) {

        blockPos.setY(world.getHeight());

        while (blockPos.getY() >= 0) {
            Block block = world.getBlockState(blockPos).getBlock();
            boolean flag = true;
            for (Block ignoring : ignoringBlocks) {
                if (block == ignoring) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                break;
            }
            blockPos.down();
        }
    }

    private void resetObjectData() {
        this.CONTEXT_4_INT_RES_INT = new MethodsContextManager<>(20);
        this.analyzingArea = EMPTY_AREA;
        this.heightMap = null;
    }

    private static class AnalyzingArea {
        private final EnumFacing.AxisDirection xDirection;
        private final EnumFacing.AxisDirection zDirection;
        private final BlockPos startCords;
        private final int areaXSize;
        private final int areaZSize;


        public AnalyzingArea(ChunkPos pos) {
            this.xDirection = EnumFacing.AxisDirection.POSITIVE;
            this.zDirection = EnumFacing.AxisDirection.POSITIVE;
            this.startCords = pos.getBlock(0, 0, 0);
            this.areaXSize = 16;
            this.areaZSize = 16;
        }

        public AnalyzingArea(BlockPos from, BlockPos to) {
            this.xDirection = to.getX() - from.getX() > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE;
            this.zDirection = to.getZ() - from.getZ() > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE;
            this.areaXSize = (to.getX() - from.getX()) + xDirection.getOffset();
            this.areaZSize = (to.getZ() - from.getZ()) + zDirection.getOffset();
            this.startCords = new BlockPos(from.getX(), 0, from.getZ());
        }

        public int getAreaXSize() {
            return Math.abs(areaXSize);
        }

        public int getAreaZSize() {
            return Math.abs(areaZSize);
        }

        public void offsetToStart(UltraMutableBlockPos pos) {
            int y = pos.getY();
            pos.setPos(startCords).setY(y);
        }

        public void localize(UltraMutableBlockPos pos) {
            pos.deduct(startCords);
            if (pos.getX() < 0 || pos.getX() >= this.getAreaXSize()) {
                throw new IllegalArgumentException("Out of bound area! AreaXSize:" + this.getAreaXSize() + "Pos:" + pos.getX());
            }
            if (pos.getZ() < 0 || pos.getZ() >= this.getAreaZSize()) {
                throw new IllegalArgumentException("Out of bound area! AreaZSize:" + this.getAreaZSize() + " Pos:" + pos.getZ());
            }
        }

        public void offset(UltraMutableBlockPos pos, int toAreaCordX, int toAreaCordZ) {
            if (toAreaCordX < 0 || toAreaCordX >= this.getAreaXSize()) {
                throw new IllegalArgumentException("Out of bound area! AreaXSize:" + this.getAreaXSize() + "Index:" + toAreaCordX);
            }
            if (toAreaCordZ < 0 || toAreaCordZ >= this.getAreaZSize()) {
                throw new IllegalArgumentException("Out of bound area! AreaZSize:" + this.getAreaZSize() + " Index:" + toAreaCordZ);
            }

            int localToAreaCordX = toAreaCordX * xDirection.getOffset();
            int localToAreaCordZ = toAreaCordZ * zDirection.getOffset();

            pos.add(localToAreaCordX, 0, localToAreaCordZ);
        }

        public int getSpace() {
            return this.getAreaXSize() * this.getAreaZSize();
        }
    }
}
