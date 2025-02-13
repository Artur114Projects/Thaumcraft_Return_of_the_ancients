package com.artur.returnoftheancients.generation.portal;

import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Random;

public class AncientPortalNaturalGeneration extends AncientPortal {
    public AncientPortalNaturalGeneration(MinecraftServer server, int dimension, int chunkX, int chunkZ, int posY) {
        super(server, dimension, chunkX, chunkZ, posY >= 4 ? posY : server.getWorld(dimension).getSeaLevel(), AncientPortalsProcessor.getFreeId());
    }

    public AncientPortalNaturalGeneration(MinecraftServer server, NBTTagCompound nbt) {
        super(server, nbt);
    }

    @Override
    protected void onChunkPopulatePre(int chunkX, int chunkZ) {
        if (this.isOnPortalRange(chunkX, chunkZ)) {
            this.build();
        }
    }

    @Override
    public boolean isOnPortalRange(int chunkX, int chunkZ) {
        return super.isOnPortalRange(chunkX, chunkZ);
    }

    @Override
    public boolean isLoaded() {
        return super.isLoaded();
    }

    @Override
    public void build() {
        this.genAncientPortal();
        this.setGenerated();
    }

    @Override
    protected int getPortalTypeID() {
        return 0;
    }

    private static class PortalGenManager {
        private final AncientPortalNaturalGeneration portal;
        private final int minSanctuaryGenRange;
        private final int genRange;
        private final Random rand;
        private final World world;
        private final int chunkX;
        private final int chunkZ;

        private PortalGenManager(AncientPortalNaturalGeneration portal, int genRange, int minSanctuaryGenRange) {
            this.rand = new Random(portal.world.getSeed() + portal.chunkX + portal.chunkZ);
            this.minSanctuaryGenRange = minSanctuaryGenRange;
            this.chunkX = portal.chunkX;
            this.chunkZ = portal.chunkZ;
            this.world = portal.world;
            this.genRange = genRange;
            this.portal = portal;
        }

        private void generate() {

        }

        private boolean isOnRange(int chunkX, int chunkZ) {
            return Math.abs(this.chunkX - chunkX) <= genRange && Math.abs(this.chunkZ - chunkZ) <= genRange;
        }

        private boolean isAnyChunkOnRangeLoaded() {
            for (int x = chunkX - genRange; x != chunkX + genRange + 1; x++) {
                for (int z = chunkZ - genRange; z != chunkZ + genRange + 1; z++) {
                    if (isChunkLoaded(x, z)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isChunkLoaded(int chunkX, int chunkZ) {
            UltraMutableBlockPos blockPos0 = UltraMutableBlockPos.getBlockPosFromPoll();
            UltraMutableBlockPos blockPos1 = UltraMutableBlockPos.getBlockPosFromPoll();

            boolean flag = world.isAreaLoaded(blockPos0.setPos(chunkX, chunkZ), blockPos1.setPos(chunkX, chunkZ).add(16, 0, 16));

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos1);
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos0);
            return flag;
        }
    }
}
