package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ServerPacketTpToHome implements IMessage {
    public ServerPacketTpToHome() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class HandlerTTH implements IMessageHandler<ServerPacketTpToHome, IMessage> {
        @Override
        public IMessage onMessage(ServerPacketTpToHome message, MessageContext ctx) {
            if (AncientWorld.interrupt(ctx.getServerHandler().player)) AncientPortalsProcessor.tpToHome(ctx.getServerHandler().player);
            return null;
        }
    }
}
