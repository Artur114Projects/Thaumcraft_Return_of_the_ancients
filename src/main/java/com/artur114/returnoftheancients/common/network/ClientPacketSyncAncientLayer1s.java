package com.artur114.returnoftheancients.common.network;

import com.artur114.returnoftheancients.common.ancientworld.system.base.IAncientLayer1Manager;
import com.artur114.returnoftheancients.common.ancientworld.system.client.IClientAncientLayer1Manager;
import com.artur114.returnoftheancients.common.ancientworld.system.server.AncientLayer1Server;
import com.artur114.returnoftheancients.common.init.InitCapabilities;
import com.artur114.returnoftheancients.common.init.InitDimensions;
import com.artur114.returnoftheancients.main.ThaumicRotA;
import com.artur114.returnoftheancients.common.network.base.NBTPacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
                    IAncientLayer1Manager manager = mc.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

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

        ThaumicRotA.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendCreateLayerAndStartBuild(EntityPlayerMP player, AncientLayer1Server layer1Server) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("create", layer1Server.writeClientCreateNBT(new NBTTagCompound()));
        nbt.setBoolean("build", true);

        ThaumicRotA.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendBuildState(EntityPlayerMP player, boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("build", state);

        ThaumicRotA.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendUpdatePlayersState(EntityPlayerMP player, Map<UUID, String> teamState) {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (String n : teamState.values()) {
            list.appendTag(new NBTTagString(n));
        }

        nbt.setTag("playersState", list);

        ThaumicRotA.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }

    public static void sendSyncStructures(EntityPlayerMP player, NBTTagCompound data) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag("structuresSync", data);

        ThaumicRotA.NETWORK.sendTo(new ClientPacketSyncAncientLayer1s(nbt), player);
    }
}
