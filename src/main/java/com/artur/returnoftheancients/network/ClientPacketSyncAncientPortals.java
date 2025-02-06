package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.generation.portal.base.client.ClientAncientPortalsProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketSyncAncientPortals implements IMessage {

    public NBTTagCompound nbt;

    public ClientPacketSyncAncientPortals() {}

    public ClientPacketSyncAncientPortals(NBTTagCompound data) {
        nbt = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class HandlerSAP implements IMessageHandler<ClientPacketSyncAncientPortals, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketSyncAncientPortals message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ClientAncientPortalsProcessor.setNewPortalData(message.nbt);
            });
            return null;
        }
    }
}
