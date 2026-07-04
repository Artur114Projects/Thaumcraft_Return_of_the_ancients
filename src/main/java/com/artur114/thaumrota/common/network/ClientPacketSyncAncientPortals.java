package com.artur114.thaumrota.common.network;

import com.artur114.thaumrota.common.generation.portallegacy.base.client.ClientAncientPortalsProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(ClientPacketSyncAncientPortals message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ClientAncientPortalsProcessor.setNewPortalData(message.nbt);
            });
            return null;
        }
    }
}
