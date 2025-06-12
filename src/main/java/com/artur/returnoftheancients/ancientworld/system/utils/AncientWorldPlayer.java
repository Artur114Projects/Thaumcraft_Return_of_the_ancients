package com.artur.returnoftheancients.ancientworld.system.utils;

import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureInteractive;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.UUID;

public class AncientWorldPlayer implements IWriteToNBT {
    public EntityPlayer player = null;
    public final UUID playerID;
    private IStructure currentRoom;

    public AncientWorldPlayer(EntityPlayer player) {
        this.playerID = player.getUniqueID();
        this.player = player;
    }

    public AncientWorldPlayer(UUID playerID) {
        this.playerID = playerID;
    }

    public boolean isSleep() {
        return player == null;
    }

    public IStructure currentRoom() {
        return this.currentRoom;
    }

    public void setCurrentRoom(IStructure newRoom) {
        if (this.currentRoom != newRoom) {
            if (this.currentRoom instanceof IStructureInteractive) {
                ((IStructureInteractive) this.currentRoom).onPlayerWentOut(this.player);
            }

            if (newRoom instanceof IStructureInteractive) {
                ((IStructureInteractive) newRoom).onPlayerEntered(this.player);
            }
        }

        this.currentRoom = newRoom;
    }

    public StrPos calculatePosOnMap(ChunkPos center, int mapSize) {
        if (this.isSleep()) {
            return new StrPos(mapSize / 2, mapSize / 2);
        }

        ChunkPos start = new ChunkPos(center.x + (mapSize / 2), center.z + (mapSize / 2));
        ChunkPos playerPos = new ChunkPos(((int) this.player.posX) >> 4, ((int) this.player.posZ) >> 4);

        return new StrPos((playerPos.x - start.x) * -1, (playerPos.z - start.z) * -1);
    }

    public void teleportToChunk(ChunkPos pos, int y) {
        if (!this.isSleep() && this.player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) this.player).connection.setPlayerLocation(pos.getXStart() + 8, y, pos.getZStart() + 8, this.player.rotationYaw, this.player.rotationPitch);
        }
    }

    public void onPlayerLost() {
        AncientPortalsProcessor.teleportToOverworld((EntityPlayerMP) this.player);
    }

    public void onPlayerElope() {
        AncientPortalsProcessor.teleportToOverworld((EntityPlayerMP) this.player);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setUniqueId("playerID", this.playerID);
        return nbt;
    }
}
