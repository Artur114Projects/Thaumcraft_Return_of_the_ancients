package com.artur.returnoftheancients.generation.generators.portal.base;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.portal.util.interfaces.IExplore;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blocks.BlocksTC;

import java.util.ArrayList;
import java.util.List;

// TODO: Добавить posY портала в tpToHome
// TODO: Добавить естественную генерацию портала
// TODO: Разобраться с компасом
// TODO: Решить не понятный баг с тем что при прерывании возвращает на портал 0
public abstract class AncientPortal {

    public static final String PortalID = "PortalID";

    protected final BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();
    private final List<IExplore> exploreList = new ArrayList<>();
    private boolean isExplore = false;
    private int exploreIndex = 0;

    protected final World world;
    private boolean isExploring;

    public final int dimension;
    public final int chunkX;
    public final int chunkZ;

    public final int posX;

    public final int posY;

    public final int posZ;

    public final int id;


    public AncientPortal(MinecraftServer server, int dimension, int chunkX, int chunkZ, int posY, int id) {
        if (TRAConfigs.Any.debugMode) System.out.println("New portal x:" + chunkX + "z:" + chunkZ + " id:" + id);
        if (AncientPortalsProcessor.hasPortal(chunkX, chunkZ, dimension)) isExplore = true;
        AncientPortalsProcessor.PORTALS.put(id, this);
        this.world = server.getWorld(dimension);
        this.dimension = dimension;
        this.posX = chunkX << 4;
        this.posZ = chunkZ << 4;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.posY = posY;
        this.id = id;
    }

    public AncientPortal(MinecraftServer server, NBTTagCompound compound) {
        this.chunkX = compound.getInteger("chunkX");
        this.chunkZ = compound.getInteger("chunkZ");
        this.id = compound.getInteger("id");
        if (TRAConfigs.Any.debugMode) System.out.println("Portal load x:" + chunkX + "z:" + chunkZ + " id:" + id);
        this.dimension = compound.getInteger("dimension");
        this.isExploring = compound.getBoolean("isExploring");
        this.posY = compound.getInteger("posY");
        this.world = server.getWorld(dimension);
        AncientPortalsProcessor.PORTALS.put(id, this);
        this.posX = chunkX << 4;
        this.posZ = chunkZ << 4;
        this.isExplore = false;
    }


    public abstract void build();
    protected abstract int getPortalTypeID();

    public void update(TickEvent.ServerTickEvent e) {
        if (isExploring && exploreList.isEmpty()) {
            explore();
        }
        if (!exploreList.isEmpty()) {
            if (exploreIndex == exploreList.size()) {
                exploreList.clear();
                exploreIndex = 0;
                isExplore = true;
                return;
            }
            exploreList.get(exploreIndex).explore();
            exploreIndex++;
        }
    }

    public void onCollide(EntityPlayerMP player) {
        tpToAncientWorld(player);
    }

    public void tpToAncientWorld(EntityPlayerMP player) {
        player.getEntityData().setInteger(PortalID, id);
        AncientWorld.tpToAncientWorld(player);
    }

    public void tpToHome(EntityPlayerMP player) {
        ServerEventsHandler.setTpToHomeNBTData(player);
        FreeTeleporter.teleportToDimension(player, dimension, posX + 8.0D, 3, posZ + 8.0D);
        player.getEntityData().setInteger(PortalID, 0);
    }

    public void onBlockDestroyedInPortalChunk(BlockEvent.BreakEvent e) {
        if (e.getState().getBlock().equals(InitBlocks.TP_TO_ANCIENT_WORLD_BLOCK)) {
            explore();
        }
    }

    protected void genAncientPortal() {
        int localX = posX + 5;
        int localZ = posZ + 5;
        CustomGenStructure.gen(world, localX, posY, localZ, "ancient_portal_air_cube");
        for (int y = posY + 32; y > 0; y -= 31) {
            CustomGenStructure.gen(world, localX, y - (31 + 32), localZ, "ancient_portal");
        }
        CustomGenStructure.gen(world, localX, 0, localZ, "ancient_portal_floor");
    }


    public boolean isCollide(int x, int y, int z) {
        if (x >> 4 == chunkX && z >> 4 == chunkZ) {
            return true;
        }
        return false;
    }

    public boolean isCollide(BlockPos pos) {
        return isCollide(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isExplore() {
        return isExplore;
    }

    public void explore() {
        int x =  chunkX << 4;
        int z =  chunkZ << 4;
        double eX = x + 8.0D;
        double eZ = z + 8.0D;

        for (byte dX = 0; dX != 4; dX++) {
            for (byte dZ = 0; dZ != 4; dZ++) {
                mPos.setPos(x + 6, 1, z + 6);
                world.setBlockToAir(HandlerR.addToMutableBP(mPos, dX, 0, dZ));
            }
        }

        for (byte i = 0; i != 4; i++) {
            mPos.setPos(x, 0, z);
            int p2offsetX = 0;
            int p2offsetZ = 0;
            if (i == 0) {
                HandlerR.addToMutableBP(mPos, 5, 0, 7);
                p2offsetZ = 1;
            } else if (i == 1) {
                HandlerR.addToMutableBP(mPos, 10, 0, 7);
                p2offsetZ = 1;
            } else if (i == 2) {
                HandlerR.addToMutableBP(mPos, 7, 0, 5);
                p2offsetX = 1;
            } else {
                HandlerR.addToMutableBP(mPos, 7, 0, 10);
                p2offsetX = 1;
            }
            for (byte j = 0; j != 2; j++) {
                mPos.setPos(mPos.getX(), 0, mPos.getZ());
                if (j == 1) {
                    HandlerR.addToMutableBP(mPos, p2offsetX, 0, p2offsetZ);
                }
                for (int y = 0; y != posY; y++) {
                    world.setBlockState(HandlerR.addToMutableBP(mPos, 0, 1, 0), BlocksTC.stoneArcane.getDefaultState());
                }
            }
        }

        exploreList.add(() -> world.createExplosion(null, eX, 0, eZ, 16, true));
        for (int i = 0; i != posY; i++) {
            if (i % 6 == 0) {
                int finalI = i;
                exploreList.add(() -> world.createExplosion(null, eX, finalI, eZ, 12, true));
            }
        }
        exploreList.add(() -> world.createExplosion(null, eX, posY, eZ, 16, true));
        isExploring = true;
        if (TRAConfigs.Any.debugMode) System.out.println("Portal id:" + id + " is explore!");
    }

    @Nullable
    public NBTTagCompound writeToNBT() {
        if (isExplore) {
            return null;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("portalTypeID", getPortalTypeID());
        nbt.setBoolean("isExploring", isExploring);
        nbt.setInteger("dimension", dimension);
        nbt.setInteger("chunkX", chunkX);
        nbt.setInteger("chunkZ", chunkZ);
        nbt.setInteger("posY", posY);
        nbt.setInteger("id", id);
        return nbt;
    }
}
