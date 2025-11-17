package com.artur.returnoftheancients.util.math;

import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UltraMutableBlockPos extends BlockPos.MutableBlockPos {
    private static UltraMutableBlockPos[] pool = new UltraMutableBlockPos[64];
    private static int poolHead = -1;
    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public static synchronized @NotNull UltraMutableBlockPos obtain() {
        if (poolHead != -1) {
            UltraMutableBlockPos blockPos = pool[poolHead];
            pool[poolHead--] = null;

            if (blockPos == null) {
                System.out.println("Block pos in " + poolHead + " is null wtf!?");
                return new UltraMutableBlockPos();
            }

            return blockPos;
        } else {
            return new UltraMutableBlockPos();
        }
    }

    public static synchronized void release(@NotNull UltraMutableBlockPos blockPos) {
        if (poolHead + 1 >= pool.length) {
            System.out.println("wow! poll is full!");
            pool = Arrays.copyOf(pool, pool.length * 2);
        }

        blockPos.setToZero();
        pool[++poolHead] = blockPos;
    }


    protected @Nullable List<PosContext> context = null;
    protected int contextDeep = -1;

    public UltraMutableBlockPos() {
        this(0, 0, 0);
    }

    public UltraMutableBlockPos(ChunkPos pos) {
        this(pos.x, pos.z);
    }

    public UltraMutableBlockPos(int chunkX, int chunkZ) {
        this(chunkX << 4, 0, chunkZ << 4);
    }


    public UltraMutableBlockPos(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public UltraMutableBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public @NotNull UltraMutableBlockPos add(double x, double y, double z) {
        this.x += MathHelper.floor(x);
        this.y += MathHelper.floor(y);
        this.z += MathHelper.floor(z);
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos add(Vec3i vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    public @NotNull UltraMutableBlockPos add(BlockPos vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    public @NotNull UltraMutableBlockPos subtract(Vec3i vec) {
        return this.add(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    public UltraMutableBlockPos addY(int y) {
        this.y += y;
        return this;
    }

    public UltraMutableBlockPos deduct(BlockPos pos) {
        this.x -= pos.getX();
        this.y -= pos.getY();
        this.z -= pos.getZ();
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos offset(@NotNull EnumFacing facing, int n) {
        return n == 0 ? this : this.setPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }

    @Override
    public @NotNull UltraMutableBlockPos offset(@NotNull EnumFacing facing) {
        return offset(facing, 1);
    }


    public UltraMutableBlockPos setPos(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        return this;
    }

    private void setPos(PosContext context) {
        this.x = context.x;
        this.y = context.y;
        this.z = context.z;
    }

    public UltraMutableBlockPos setPos(int chunkX, int chunkZ) {
        return this.setPos(chunkX << 4, this.getY(), chunkZ << 4);
    }

    public UltraMutableBlockPos setPos(ChunkPos pos) {
        return this.setPos(pos.x, pos.z);
    }

    @Override
    public @NotNull UltraMutableBlockPos setPos(int xIn, int yIn, int zIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos setPos(double xIn, double yIn, double zIn) {
        return this.setPos(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull UltraMutableBlockPos setPos(Entity entityIn) {
        return this.setPos(entityIn.posX, entityIn.posY, entityIn.posZ);
    }

    public UltraMutableBlockPos setPos(long serialized) {
        int i = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return this.setPos(i, j, k);
    }

    @Override
    public @NotNull UltraMutableBlockPos setPos(Vec3i vec) {
        return this.setPos(vec.getX(), vec.getY(), vec.getZ());
    }

    public UltraMutableBlockPos setWorldYFromHM(World world) {
        this.setY(world.getChunkFromChunkCoords(this.getChunkX(), this.getChunkZ()).getHeight(this));
        return this.down();
    }

    public UltraMutableBlockPos setWorldY(World world) {
        return setWorldY(world, false);
    }

    public UltraMutableBlockPos setWorldY(World world, boolean ignoringLiquids) {
        return setWorldY(world, ignoringLiquids, Blocks.AIR, Blocks.BEDROCK);
    }

    public UltraMutableBlockPos setWorldY(World world, boolean ignoringLiquids, Block... ignoringBlocks) {
        Chunk chunk = world.getChunkFromChunkCoords(this.getChunkX(), this.getChunkZ());

        ExtendedBlockStorage[] storages = chunk.getBlockStorageArray();
        ExtendedBlockStorage storage = storages[storages.length - 1];
        int storageI = storages.length - 1;
        int posY = 15;
        int posX = this.getX() & 15;
        int posZ = this.getZ() & 15;

        while (storageI >= 0) {
            if (storage == null) {
                storageI--;
                if (storageI >= 0) {
                    storage = storages[storageI];
                }
                continue;
            }

            IBlockState state = storage.get(posX, posY, posZ);

            if (!(state.getBlock() == Blocks.AIR || (state.getMaterial().isLiquid() && ignoringLiquids) || MathUtils.arrayContains(ignoringBlocks, state.getBlock()))) {
                break;
            }

            posY--;

            if (posY < 0) {
                storageI--;
                posY = 15;
                if (storageI >= 0) {
                    storage = storages[storageI];
                } else {
                    posY = -1;
                    storageI = 0;
                    break;
                }
            }
        }

        this.setY((storageI << 4) + posY); return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos move(@NotNull EnumFacing facing) {
        return this.move(facing, 1);
    }

    @Override
    public @NotNull UltraMutableBlockPos move(EnumFacing facing, int n) {
        return this.setPos(this.x + facing.getFrontOffsetX() * n, this.y + facing.getFrontOffsetY() * n, this.z + facing.getFrontOffsetZ() * n);
    }

    @Override
    public @NotNull UltraMutableBlockPos up() {
        this.y++;
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos down() {
        this.y--;
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos down(int n) {
        this.y -= n;
        return this;
    }

    @Override
    public @NotNull UltraMutableBlockPos up(int n) {
        return this.offset(EnumFacing.UP, n);
    }

    public UltraMutableBlockPos copy() {
        return copy(false);
    }

    public UltraMutableBlockPos copy(boolean isCopyContext) {
        UltraMutableBlockPos pos = new UltraMutableBlockPos(this);
        if (isCopyContext) {
            if (context != null) {
                pos.context = new LinkedList<>(context);
                pos.contextDeep = contextDeep;
            }
        }
        return pos;
    }

    @Override
    public @NotNull UltraMutableBlockPos rotate(Rotation rotationIn) {
        switch (rotationIn) {
            case NONE:
            default:
                return this;
            case CLOCKWISE_90:
                return this.setPos(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180:
                return this.setPos(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90:
                return this.setPos(this.getZ(), this.getY(), -this.getX());
        }
    }

    public int getCoordinate(EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                return this.getX();
            case Y:
                return this.getY();
            case Z:
                return this.getZ();
            default:
                return 0;
        }
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(this.getChunkX(), this.getChunkZ());
    }

    public int getChunkX() {
        return this.getX() >> 4;
    }

    public int getChunkZ() {
        return this.getZ() >> 4;
    }

    public int distanceSq(ChunkPos pos) {
        int x = MathHelper.abs(this.getChunkX() - pos.x);
        int z = MathHelper.abs(this.getChunkZ() - pos.z);
        return x * x + z * z;
    }

    public int distanceSq(BlockPos pos) {
        int x = MathHelper.abs(this.getX() - pos.getX());
        int y = MathHelper.abs(this.getY() - pos.getY());
        int z = MathHelper.abs(this.getZ() - pos.getZ());
        return x * x + y * y + z * z;
    }

    public int distanceSq(int x, int y, int z) {
        int x1 = MathHelper.abs(this.getX() - x);
        int y1 = MathHelper.abs(this.getY() - y);
        int z1 = MathHelper.abs(this.getZ() - z);
        return x1 * x1 + y1 * y1 + z1 * z1;
    }

    public int distance(ChunkPos pos) {
        int x = MathHelper.abs(this.getChunkX() - pos.x);
        int z = MathHelper.abs(this.getChunkZ() - pos.z);
        return MathHelper.floor(MathHelper.sqrt(x * x + z * z));
    }

    public int distance(BlockPos pos) {
        int x = MathHelper.abs(this.getX() - pos.getX());
        int y = MathHelper.abs(this.getY() - pos.getY());
        int z = MathHelper.abs(this.getZ() - pos.getZ());
        return MathHelper.floor(MathHelper.sqrt(x * x + y * y + z * z));
    }

    public int distance(int x, int y, int z) {
        int x1 = MathHelper.abs(this.getX() - x);
        int y1 = MathHelper.abs(this.getY() - y);
        int z1 = MathHelper.abs(this.getZ() - z);
        return MathHelper.floor(MathHelper.sqrt(x1 * x1 + y1 * y1 + z1 * z1));
    }

    public int distance(Entity entity) {
        return distance(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
    }

    public void offsetAndCallRunnable(BlockPos[] offsets, RunnableWithParam<UltraMutableBlockPos> callable) {
        for (BlockPos offset : offsets) {
            this.offsetAndCallRunnable(offset, callable);
        }
    }

    public void offsetAndCallRunnable(EnumFacing[] offsets, RunnableWithParam<UltraMutableBlockPos> callable) {
        for (EnumFacing offset : offsets) {
            this.offsetAndCallRunnable(offset, callable);
        }
    }

    public void offsetAndCallRunnable(BlockPos offset, RunnableWithParam<UltraMutableBlockPos> callable) {
        this.pushPos();
        this.add(offset);
        callable.run(this);
        this.popPos();
    }

    public void offsetAndCallRunnable(EnumFacing offset, RunnableWithParam<UltraMutableBlockPos> callable) {
        this.pushPos();
        this.offset(offset);
        callable.run(this);
        this.popPos();
    }

    public ExtendedBlockStorage ebs(World world) {
        if ((this.getY() >> 4) >= 16 || (this.getY() >> 4) < 0) return null;
        Chunk chunk = world.getChunkFromBlockCoords(this);
        ExtendedBlockStorage storage = chunk.getBlockStorageArray()[this.getY() >> 4];
        return storage == null ? chunk.getBlockStorageArray()[this.getY() >> 4] = new ExtendedBlockStorage(this.getY() >> 4 << 4, world.provider.hasSkyLight()) : storage;
    }

    public void normalizeToEBS() {
        this.setPos(this.getX() & 15, this.getY() & 15, this.getZ() & 15);
    }

    public void pushPos() {
        if (this.context != null && this.context.size() > (this.contextDeep + 1)) {
            this.context.get(++this.contextDeep).setPos(this);
        } else {
            if (this.context == null) {
                this.context = new ArrayList<>();
            }
            this.context.add(new PosContext(this));
            this.contextDeep++;
        }
    }

    public void popPos() {
        if (this.context != null && this.context.size() > this.contextDeep && this.contextDeep != -1) {
            this.setPos(this.context.get(this.contextDeep--));
        } else {
            throw new IllegalStateException("Stack is empty! Cannot be fulfilled popPos!");
        }
    }

    public void clearContext(boolean removeContext) {
        if (removeContext) {
            this.context = null;
        }
        this.contextDeep = -1;
    }

    public void setToZero() {
        this.setPos(0, 0, 0);
        this.clearContext(false);
    }

    public boolean equalsXZ(BlockPos pos) {
        return this.getX() == pos.getX() && this.getZ() == pos.getZ();
    }

    @Override
    public @NotNull String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    protected static class PosContext {
        private int x;
        private int y;
        private int z;

        private PosContext(UltraMutableBlockPos pos) {
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
        }

        public void setPos(UltraMutableBlockPos pos) {
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
        }
    }
}
