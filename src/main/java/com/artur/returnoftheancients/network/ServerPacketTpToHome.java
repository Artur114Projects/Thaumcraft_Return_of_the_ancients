package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1EventsHandler;
import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
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
            if (!AncientLayer1EventsHandler.SERVER_MANAGER.playerInterruptBuild(ctx.getServerHandler().player)) AncientPortalsProcessor.teleportToOverworld(ctx.getServerHandler().player);
            return null;
        }
    }
}
