package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import com.artur.returnoftheancients.ancientworld.system.client.IClientAncientLayer1Manager;
import com.artur.returnoftheancients.ancientworld.system.server.AncientLayer1Server;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.base.NBTPacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;
import java.util.UUID;

public class ClientPacketSyncAncientLayer1s extends NBTPacketBase {

    public ClientPacketSyncAncientLayer1s() {
    }

    public ClientPacketSyncAncientLayer1s(NBTTagCompound nbt) {
        super(nbt);
    }

    public static class HandlerSAL implements IMessageHandler<ClientPacketSyncAncientLayer1s, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketSyncAncientLayer1s message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();

                if (mc.player.dimension == InitDimensions.ancient_world_dim_id) {
                    IAncientLayer1Manager manager = mc.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

                    if (manager != null) {
                        ((IClientAncientLayer1Manager) manager).handleUpdateTag(message.nbt);
                    }
                }
            });
            return null;
        }
    }

    public static void sendCreateLayer(EntityPlayerMP player, AncientLayer1Server layer1Server) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("create", layer1Server.writeClientCreateNBT(new NBTTagCompound()));

        MainR.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendCreateLayerAndStartBuild(EntityPlayerMP player, AncientLayer1Server layer1Server) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("create", layer1Server.writeClientCreateNBT(new NBTTagCompound()));
        nbt.setBoolean("build", true);

        MainR.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendBuildState(EntityPlayerMP player, boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("build", state);

        MainR.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendUpdatePlayersState(EntityPlayerMP player, Map<UUID, String> teamState) {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (String n : teamState.values()) {
            list.appendTag(new NBTTagString(n));
        }

        nbt.setTag("playersState", list);

        MainR.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }
}
