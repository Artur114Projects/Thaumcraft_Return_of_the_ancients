package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;

public interface IStructureInteractive extends IStructure {
    void bindWorld(World world);
    void bindRealPos(ChunkPos pos);
    void onPlayerEntered(EntityPlayer player);
    void onPlayerWentOut(EntityPlayer player);
    void update(List<AncientWorldPlayer> players);
}
