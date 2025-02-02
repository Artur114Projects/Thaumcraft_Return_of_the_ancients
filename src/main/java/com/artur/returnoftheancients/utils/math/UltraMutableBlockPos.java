package com.artur.returnoftheancients.utils.math;

import com.artur.returnoftheancients.utils.interfaces.RunnableWithParam;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class UltraMutableBlockPos extends BlockPos.MutableBlockPos {

    private static final UltraMutableBlockPos[] stack = new UltraMutableBlockPos[8];
    private static int stackHead = -1;

    public static UltraMutableBlockPos getBlockPosFromPoll() {
        if (stackHead != -1) {
            UltraMutableBlockPos blockPos = stack[stackHead];
            stack[stackHead] = null;
            stackHead--;

            return blockPos;
        } else {
            return new UltraMutableBlockPos();
        }
    }

    public static void returnBlockPosToPoll(@NotNull UltraMutableBlockPos blockPos) {
        if (stackHead + 1 >= stack.length) {
            return;
        }

        blockPos.setToZero();
        stack[++stackHead] = blockPos;
    }


    protected @Nullable LinkedList<PosContext> contextPos = null;
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
        this.y += MathHelper.floor(y);;
        this.z += MathHelper.floor(z);;
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

    public UltraMutableBlockPos addY(int y) {
        this.y += y;
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

    public UltraMutableBlockPos setPos(ChunkPos pos) {
        return this.setPos(pos.x << 4, this.getY(), pos.z << 4);
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

    @Override
    public @NotNull UltraMutableBlockPos setPos(Vec3i vec) {
        return this.setPos(vec.getX(), vec.getY(), vec.getZ());
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
        return this.offset(EnumFacing.DOWN, n);
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
            if (contextPos != null) {
                pos.contextPos = new LinkedList<>(contextPos);
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

    public int getChunkX() {
        return this.getX() >> 4;
    }

    public int getChunkZ() {
        return this.getZ() >> 4;
    }

    public ChunkPos getChunkPos() {
        return new ChunkPos(this);
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

    public void offsetAndCallRunnable(BlockPos offset, RunnableWithParam<UltraMutableBlockPos> callable) {
        this.pushPos();
        this.add(offset);
        callable.run(this);
        this.popPos();
    }

    public void pushPos() {
        if (contextPos != null && contextPos.size() > (contextDeep + 1)) {
            contextPos.get(++contextDeep).setPos(this);
        } else {
            if (contextPos == null) {
                contextPos = new LinkedList<>();
            }
            contextPos.add(new PosContext(this));
            contextDeep++;
        }
    }

    public void popPos() {
        if (contextPos != null && contextPos.size() > contextDeep && contextDeep != -1) {
            this.setPos(contextPos.get(contextDeep--));
        } else {
            throw new RuntimeException("Stack is empty! Cannot be fulfilled popPos!");
        }
    }

    public void clearContext() {
        contextPos = null;
        contextDeep = -1;
    }

    public void setToZero() {
        setPos(0, 0, 0);
        clearContext();
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
