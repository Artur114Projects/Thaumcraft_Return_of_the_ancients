package com.artur.returnoftheancients.util.math;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LocalPos {
    private final int chunkX;
    private final int chunkZ;
    private final int x;
    private final int y;
    private final int z;

    public LocalPos(int chunkX, int chunkZ, int x, int y, int z) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos toBlockPos(ChunkPos chunkCenter) {
        return new BlockPos(((this.chunkX + chunkCenter.x) << 4) + this.x, this.y, ((this.chunkZ + chunkCenter.z) << 4) + this.z);
    }

    public Baked bake(ChunkPos chunkCenter) {
        return new Baked(chunkCenter, this);
    }

    public static class Baked extends LocalPos implements IArea {
        private final BlockPos pos;

        public Baked(ChunkPos chunkCenter, LocalPos localPos) {
            super(localPos.chunkX, localPos.chunkZ, localPos.x, localPos.y, localPos.z);
            this.pos = localPos.toBlockPos(chunkCenter);
        }

        public BlockPos blockPos() {
            return this.pos;
        }

        @Override
        public BlockPos toBlockPos(@Nullable ChunkPos chunkCenter) {
            return this.pos;
        }

        @Override
        public Baked bake(ChunkPos chunkCenter) {
            return this;
        }

        @Override
        public int areaSize() {
            return 1;
        }

        public boolean isCollide(double x, double y, double z) {
            return Math.abs(this.pos.getX() - x) < 1.0D && Math.abs(this.pos.getY() - y) < 1.0D && Math.abs(this.pos.getZ() - z) < 1.0D;
        }

        @Override
        public @Nullable BlockPos fromIndex(int index) {
            if (index != 1){
                return null;
            }
            return this.pos;
        }

        @Override
        public List<BlockPos> points() {
            return Collections.singletonList(this.pos);
        }

        @Override
        public void renderArea(float alpha) {

        }
    }
}
