package com.artur.returnoftheancients.generation.portal.base;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.blocks.BlockTpToAncientWorld;
import com.artur.returnoftheancients.capabilities.IPlayerTimerCapability;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.generation.portal.util.interfaces.IExplore;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.ISaveToNBT;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
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
import java.util.Objects;

// TODO: Добавить естественную генерацию портала
// TODO: Разобраться с компасом
// TODO: Решить не понятный баг с тем что при прерывании возвращает на портал 0
public abstract class AncientPortal implements ISaveToNBT {

    public static final String PortalID = "PortalID";
    public static final String tpToHomeNBT = "tpToHomeNBT";
    public static final String startUpNBT = "startUpNBT";


    protected final UltraMutableBlockPos mPos = new UltraMutableBlockPos();
    private final List<IExplore> explosionList = new ArrayList<>();
    private boolean isRequestToUpdateOnClient = false;
    private boolean isExploded = false;
    public final ChunkPos portalPos;
    private int exploreIndex = 0;

    protected final World world;
    private boolean isExplodes;

    public final int dimension;
    public final int chunkX;
    public final int chunkZ;

    public final int posX;

    public final int posY;

    public final int posZ;

    protected int id;


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

        AncientPortalsProcessor.addNewPortal(this);
        this.requestToUpdateOnClient();
    }

    protected AncientPortal(MinecraftServer server, NBTTagCompound compound) {
        this.chunkX = compound.getInteger("chunkX");
        this.chunkZ = compound.getInteger("chunkZ");
        this.portalPos = new ChunkPos(chunkX, chunkZ);
        this.id = compound.getInteger("id");
        if (TRAConfigs.Any.debugMode) System.out.println("Portal load x:" + chunkX + "z:" + chunkZ + " id:" + id);
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

    public void tpToAncientWorld(EntityPlayerMP player) {
        player.getEntityData().setInteger(PortalID, id);
        AncientWorld.tpToAncientWorld(player);
    }

    public void tpToHome(EntityPlayerMP player) {
        setTpToHomeNBTData(player);
        FreeTeleporter.teleportToDimension(player, dimension, posX + 8.0D, 3, posZ + 8.0D);
    }

    public void tpToHome(EntityPlayerMP player, boolean isTeleporting) {
        setTpToHomeNBTData(player);
        if (isTeleporting) {
            FreeTeleporter.teleportToDimension(player, dimension, posX + 8.0D, 3, posZ + 8.0D);
        } else {
            player.getEntityData().setInteger(PortalID, id);
        }
    }


    public void onBlockDestroyedInPortalChunk(BlockEvent.BreakEvent e) {
        if (e.getState().getBlock().equals(InitBlocks.TP_TO_ANCIENT_WORLD_BLOCK)) {
            explosion();
        }
    }

    public void playerHasPortalIdUpdate(EntityPlayerMP player) {
        if (player.dimension == dimension) {
            NBTTagCompound data = player.getEntityData();
            if (data.getBoolean(tpToHomeNBT)) {
                if (isCollide(player.getPosition())) {
//                    player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 1));
                    data.setBoolean(tpToHomeNBT, false);
                    HandlerR.setStartUpNBT(player, true);
                    onPlayerTpToHome(player);
                }
            }
            if (data.getBoolean(startUpNBT)) {
                player.motionY += 0.5 - player.motionY;
                if (player.posY >= posY || player.posY >= 110) {
                    HandlerR.setStartUpNBT(player, false);
                    player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(15)));
                    player.fallDistance = 0;
                    data.setInteger(PortalID, -1);
                }
            }
        }
    }

    protected static void onPlayerTpToHome(EntityPlayerMP player) {
        if (!ThaumcraftCapabilities.knowsResearchStrict(player, "DEAD")) {
            HandlerR.researchAndSendMessage(player, "DEAD", Referense.MODID + ".text.dead");
            IPlayerTimerCapability timer = TRACapabilities.getTimer(player);
            timer.createTimer("recovery");
        }
    }

    public static void setTpToHomeNBTData(EntityPlayerMP player) {
        player.getEntityData().setBoolean(BlockTpToAncientWorld.noCollisionNBT, true);
        player.getEntityData().setBoolean(tpToHomeNBT, true);
    }

    protected void genAncientPortal() {
        if (world.isRemote) {
            return;
        }

        int localX = posX + 5;
        int localZ = posZ + 5;
        CustomGenStructure.gen(world, localX, posY, localZ, "ancient_portal_air_cube");
        for (int y = posY + 32; y > 0; y -= 31) {
            CustomGenStructure.gen(world, localX, y - (31 + 32), localZ, "ancient_portal");
        }
        CustomGenStructure.gen(world, localX, 0, localZ, "ancient_portal_floor");
    }

    public boolean isCollide(int x, int z) {
        if (x >> 4 == chunkX && z >> 4 == chunkZ) {
            return true;
        }
        return false;
    }

    public boolean isCollide(BlockPos pos) {
        return isCollide(pos.getX(), pos.getZ());
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
        if (world.isRemote) {
            return;
        }

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
    public NBTTagCompound writeToNBT() {
        if (isExploded) {
            return null;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("portalTypeID", getPortalTypeID());
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
