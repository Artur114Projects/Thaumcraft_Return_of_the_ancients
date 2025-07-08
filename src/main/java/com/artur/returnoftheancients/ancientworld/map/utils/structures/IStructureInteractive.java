package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.maps.InteractiveMap;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IStructureInteractive extends IStructure {
    InteractiveMap map();
    void bindWorld(World world);
    void bindRealPos(ChunkPos pos);
    void onPlayerEntered(EntityPlayer player);
    void onPlayerWentOut(EntityPlayer player);
    void update(List<AncientWorldPlayer> players);
    @SideOnly(Side.CLIENT) default void handleServerSyncData(NBTTagCompound data) {}
    default void sendToClient(NBTTagCompound data) { this.map().addStructuresSyncData(this, data); }
}
