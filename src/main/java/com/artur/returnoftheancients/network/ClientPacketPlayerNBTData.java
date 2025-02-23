package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.handlers.MiscHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientPacketPlayerNBTData implements IMessage {

    private NBTTagCompound data;

    public ClientPacketPlayerNBTData() {
    }

    public ClientPacketPlayerNBTData(NBTTagCompound data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    public static class HandlerPND implements IMessageHandler<ClientPacketPlayerNBTData, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(ClientPacketPlayerNBTData packet, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                System.out.println("ClientPacketPlayerNBTData packet!");
                EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                switch (packet.data.getByte("dataIndex")) {
                    case 1:
                        playerSP.getEntityData().setBoolean(packet.data.getString("tagSetName"), packet.data.getBoolean("data"));
                        break;
                    case 2:
                        playerSP.getEntityData().setInteger(packet.data.getString("tagSetName"), packet.data.getInteger("data"));
                        break;
                    case 3:
                        playerSP.getEntityData().setString(packet.data.getString("tagSetName"), packet.data.getString("data"));
                        break;
                }
            });
            return null;
        }
    }
}
