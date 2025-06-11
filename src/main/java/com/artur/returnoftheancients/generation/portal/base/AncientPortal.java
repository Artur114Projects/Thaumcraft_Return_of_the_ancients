package com.artur.returnoftheancients.generation.portal.base;

import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.blocks.BlockTpToAncientWorld;
import com.artur.returnoftheancients.capabilities.IPlayerTimerCapability;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.generation.portal.util.OffsetsUtil;
import com.artur.returnoftheancients.generation.portal.util.interfaces.IExplore;
import com.artur.returnoftheancients.handlers.TeleportHandler;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.ArrayList;
import java.util.List;

import static com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld.tpToAncientWorld;

// TODO: Решить не понятный баг с тем что при прерывании возвращает на портал 0
public abstract class AncientPortal implements IWriteToNBT {

    public static final String portalID = "PortalID";
    public static final String tpToHomeNBT = "tpToHomeNBT";
    public static final String elevatingUp = "startUpNBT";


    protected final UltraMutableBlockPos mPos = new UltraMutableBlockPos();
    private final List<IExplore> explosionList = new ArrayList<>();
    private boolean isRequestToUpdateOnClient = false;
    private boolean isExploded = false;
    public final ChunkPos portalPos;
    private int exploreIndex = 0;

    protected final World world;
    private boolean isGenerated;
    private boolean isExplodes;

    public final int dimension;
    public final int chunkX;
    public final int chunkZ;

    public final int posX;

    public final int posZ;

    protected int id;

    public int posY;


    public AncientPortal(MinecraftServer server, int dimension, int chunkX, int chunkZ, int posY, int id) {
        if (TRAConfigs.Any.debugMode) System.out.println("New portal x:" + chunkX + "z:" + chunkZ + " id:" + id);
        if (AncientPortalsProcessor.hasPortal(chunkX, chunkZ, dimension)) isExploded = true;
        this.portalPos = new ChunkPos(chunkX, chunkZ);
        this.world = server.getWorld(dimension);
        this.dimension = dimension;
        this.posX = chunkX << 4;
        this.posZ = chunkZ << 4;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.posY = posY;
        this.id = id;
    }

    protected AncientPortal(MinecraftServer server, NBTTagCompound compound) {
        this.chunkX = compound.getInteger("chunkX");
        this.chunkZ = compound.getInteger("chunkZ");
        this.portalPos = new ChunkPos(chunkX, chunkZ);
        this.id = compound.getInteger("id");
        if (TRAConfigs.Any.debugMode) System.out.println("Portal load x:" + chunkX + "z:" + chunkZ + " id:" + id);
        this.isGenerated = compound.getBoolean("isGenerated");
        this.dimension = compound.getInteger("dimension");
        this.isExplodes = compound.getBoolean("isExplodes");
        this.posY = compound.getInteger("posY");
        this.world = server.getWorld(dimension);
        this.posX = chunkX << 4;
        this.posZ = chunkZ << 4;
        this.isExploded = false;
    }

    public abstract void build();
    protected abstract int getPortalTypeID();

    public void update(TickEvent.ServerTickEvent e) {
        if (isExplodes && explosionList.isEmpty()) {
            explosion();
        }
        if (!explosionList.isEmpty()) {
            if (exploreIndex == explosionList.size()) {
                explosionList.clear();
                exploreIndex = 0;
                isExploded = true;
                return;
            }
            explosionList.get(exploreIndex).explore();
            exploreIndex++;
        }
    }

    public void onCollide(EntityPlayerMP player) {
        tpToAncientWorld(player);
    }

    public void teleportToAncientWorld(EntityPlayerMP player) {
        player.getEntityData().setInteger(portalID, id);
        tpToAncientWorld(player);
    }

    public void teleportToOverworld(EntityPlayerMP player) {
        setTpToHomeNBTData(player);
        TeleportHandler.teleportToDimension(player, dimension, posX + 8.0D, 3, posZ + 8.0D);
    }

    public void teleportToOverworld(EntityPlayerMP player, boolean isTeleporting) {
        setTpToHomeNBTData(player);
        if (isTeleporting) {
            TeleportHandler.teleportToDimension(player, dimension, posX + 8.0D, 3, posZ + 8.0D);
        } else {
            player.getEntityData().setInteger(portalID, id);
        }
    }


    public void onBlockDestroyedInPortalArea(BlockEvent.BreakEvent e) {
        if (e.getState().getBlock().equals(InitBlocks.TP_TO_ANCIENT_WORLD_BLOCK)) {
            explosion();
        }
    }

    public void playerHasPortalIdUpdate(EntityPlayerMP player) {
        if (player.dimension == dimension) {
            NBTTagCompound data = player.getEntityData();
            if (data.getBoolean(tpToHomeNBT)) {
                if (isCollide(player.getPosition())) {
                    data.setBoolean(tpToHomeNBT, false);
                    data.setBoolean(elevatingUp, true);
                    onPlayerTpToHome(player);
                }
            }
            if (data.getBoolean(elevatingUp)) {
                player.fallDistance = 0;
                player.motionY += 0.5 - player.motionY;
                if (player.posY >= posY || player.posY >= 110) {
                    player.fallDistance = 0;
                    data.setInteger(portalID, -1);
                }
            }
        }
    }

    protected static void onPlayerTpToHome(EntityPlayerMP player) {
        if (!ThaumcraftCapabilities.knowsResearchStrict(player, "DEAD")) {
            MiscHandler.researchAndSendMessage(player, "DEAD", Referense.MODID + ".text.dead");
            IPlayerTimerCapability timer = TRACapabilities.getTimer(player);
            timer.createTimer("recovery");
        }
    }

    public static void setTpToHomeNBTData(EntityPlayerMP player) {
        player.getEntityData().setBoolean(BlockTpToAncientWorld.noCollisionNBT, true);
        player.getEntityData().setBoolean(tpToHomeNBT, true);
    }

    protected void genAncientPortal(boolean needProtect) {
        int localX = posX + 5;
        int localZ = posZ + 5;
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        StructureBuildersManager.createBuildRequest(world, blockPos.setPos(localX, posY, localZ), "ancient_portal_air_cube").setNeedProtect(needProtect).build();
        for (int y = posY + 32; y > 0; y -= 31) {
            StructureBuildersManager.createBuildRequest(world, blockPos.setPos(localX, y - (31 + 32), localZ), "ancient_portal").setNeedProtect(needProtect).build();
        }
        StructureBuildersManager.createBuildRequest(world, blockPos.setPos(localX, 0, localZ), "ancient_portal_floor").setNeedProtect(needProtect).build();
    }

    protected void onChunkPopulatePre(int chunkX, int chunkZ) {}

    public boolean isOnPortalRange(int chunkX, int chunkZ) {
        return chunkX == this.chunkX && chunkZ == this.chunkZ;
    }

    public boolean isOnPortalRange(ChunkPos pos) {
        return isOnPortalRange(pos.x, pos.z);
    }

    public boolean isOnPortalRange(BlockPos pos) {
        return isOnPortalRange(pos.getX() >> 4, pos.getZ() >> 4);
    }

    public boolean isCollide(int x, int z) {
        if (x >> 4 == chunkX && z >> 4 == chunkZ) {
            mPos.pushPos();
            mPos.setPos(portalPos);
            for (BlockPos pos : OffsetsUtil.portalCollideOffsetsArray) {
                mPos.pushPos();
                mPos.add(pos);
                if (mPos.getX() == x && mPos.getZ() == z) {
                    mPos.popPos();
                    mPos.popPos();
                    return true;
                }
                mPos.popPos();
            }
            mPos.popPos();
        }
        return false;
    }

    public boolean isCollide(BlockPos pos) {
        return isCollide(pos.getX(), pos.getZ());
    }

    protected void setGenerated() {
        isGenerated = true;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public boolean isExploded() {
        return isExploded;
    }

    public boolean isExplodes() {
        return isExplodes;
    }

    public int getId() {
        return id;
    }

    public boolean isLoaded() {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();

        mPos.setPos(0, 0, 0);
        boolean flag = world.isAreaLoaded(mPos.setPos(portalPos), blockPos.setPos(portalPos).add(16, 0, 16));

        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);

        return flag;
    }

    public void explosion() {
        int x =  chunkX << 4;
        int z =  chunkZ << 4;
        double eX = x + 8.0D;
        double eZ = z + 8.0D;

        for (byte dX = 0; dX != 4; dX++) {
            for (byte dZ = 0; dZ != 4; dZ++) {
                mPos.setPos(x + 6, 1, z + 6);
                world.setBlockToAir(MiscHandler.addToMutableBP(mPos, dX, 0, dZ));
            }
        }

        for (byte i = 0; i != 4; i++) {
            mPos.setPos(x, 0, z);
            int p2offsetX = 0;
            int p2offsetZ = 0;
            if (i == 0) {
                MiscHandler.addToMutableBP(mPos, 5, 0, 7);
                p2offsetZ = 1;
            } else if (i == 1) {
                MiscHandler.addToMutableBP(mPos, 10, 0, 7);
                p2offsetZ = 1;
            } else if (i == 2) {
                MiscHandler.addToMutableBP(mPos, 7, 0, 5);
                p2offsetX = 1;
            } else {
                MiscHandler.addToMutableBP(mPos, 7, 0, 10);
                p2offsetX = 1;
            }
            for (byte j = 0; j != 2; j++) {
                mPos.setPos(mPos.getX(), 0, mPos.getZ());
                if (j == 1) {
                    MiscHandler.addToMutableBP(mPos, p2offsetX, 0, p2offsetZ);
                }
                for (int y = 0; y != posY; y++) {
                    world.setBlockState(MiscHandler.addToMutableBP(mPos, 0, 1, 0), BlocksTC.stoneArcane.getDefaultState());
                }
            }
        }

        explosionList.add(() -> world.createExplosion(null, eX, 1, eZ, 16, true));
        for (int i = 0; i != posY; i++) {
            if (i % 6 == 0) {
                int finalI = i;
                explosionList.add(() -> world.createExplosion(null, eX, finalI, eZ, 12, true));
            }
        }
        explosionList.add(() -> world.createExplosion(null, eX, posY, eZ, 16, true));
        isExplodes = true;
        if (TRAConfigs.Any.debugMode) System.out.println("Portal id:" + id + " is explosion!");
    }

    public boolean isNeedUpdateOnClient() {
        if (isRequestToUpdateOnClient) {
            isRequestToUpdateOnClient = false;
            return true;
        }
        return false;
    }

    public void requestToUpdateOnClient() {
        isRequestToUpdateOnClient = true;
    }

    @Nullable
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (isExploded) {
            return null;
        }
        nbt.setInteger("portalTypeID", getPortalTypeID());
        nbt.setBoolean("isGenerated", isGenerated);
        nbt.setBoolean("isExplodes", isExplodes);
        nbt.setInteger("dimension", dimension);
        nbt.setInteger("chunkX", chunkX);
        nbt.setInteger("chunkZ", chunkZ);
        nbt.setInteger("posY", posY);
        nbt.setInteger("id", id);
        return nbt;
    }

    @Nullable
    public NBTTagCompound createClientUpdateNBT() {
        if (isExploded) {
            return null;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("portalTypeID", getPortalTypeID());
        nbt.setInteger("dimension", dimension);
        nbt.setInteger("chunkX", chunkX);
        nbt.setInteger("chunkZ", chunkZ);
        nbt.setInteger("posY", posY);
        nbt.setInteger("id", id);
        return nbt;
    }
}
