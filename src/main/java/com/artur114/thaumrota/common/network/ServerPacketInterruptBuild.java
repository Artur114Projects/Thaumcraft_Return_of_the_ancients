package com.artur114.thaumrota.common.network;

import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1EventsHandler;
import com.artur114.thaumrota.common.generation.portallegacy.base.AncientPortalsProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ServerPacketInterruptBuild implements IMessage {
    public ServerPacketInterruptBuild() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class HandlerTTH implements IMessageHandler<ServerPacketInterruptBuild, IMessage> {
        @Override
        public IMessage onMessage(ServerPacketInterruptBuild message, MessageContext ctx) {
            if (!AncientLayer1EventsHandler.SERVER_MANAGER.playerInterruptBuild(ctx.getServerHandler().player)) AncientPortalsProcessor.teleportToOverworld(ctx.getServerHandler().player);
            return null;
        }
    }
}
