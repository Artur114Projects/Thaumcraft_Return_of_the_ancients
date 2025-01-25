package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.main.MainR;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketGetWeather implements IMessage {

    public ServerPacketGetWeather() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}


    public static class HandlerGW implements IMessageHandler<ServerPacketGetWeather, ClientPacketSendWeather> {

        @Override
        public ClientPacketSendWeather onMessage(ServerPacketGetWeather message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.world;
            return new ClientPacketSendWeather(world.rainingStrength, world.thunderingStrength);
        }
    }
}
