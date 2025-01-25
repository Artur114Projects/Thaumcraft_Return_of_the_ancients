package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketSendWeather implements IMessage {

    public float rain;

    public float thunder;

    public ClientPacketSendWeather() {}

    public ClientPacketSendWeather(float rain, float thunder) {
        this.thunder = thunder;
        this.rain = rain;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        thunder = buf.readFloat();
        rain = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(thunder);
        buf.writeFloat(rain);
    }

    public static class HandlerSW implements IMessageHandler<ClientPacketSendWeather, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketSendWeather message, MessageContext ctx) {
            ClientEventsHandler.PLAYER_DISTANCE_TO_PORTAL_MANAGER.setServerRain(message.rain, message.thunder);
            return null;
        }
    }
}
