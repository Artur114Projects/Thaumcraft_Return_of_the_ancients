package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.gui.LoadingGui;
import com.artur.returnoftheancients.gui.OldLoadingGui;
import com.artur.returnoftheancients.handlers.HandlerR;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketLoadingGui implements IMessage {
    private NBTTagCompound data;

    public ClientPacketLoadingGui() {
    }

    public ClientPacketLoadingGui(NBTTagCompound data) {
        this.data = data;
    }

    /**
     * Читает данные пакета из ByteBuf при получении.
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    /**
     * Записывает данные пакета в ByteBuf перед отправкой.
     */
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    public static class HandlerLG implements IMessageHandler<ClientPacketLoadingGui, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketLoadingGui message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (HandlerR.isGoodNBTTagLG(message.data)) {
                    NBTTagCompound nbt = message.data;
                    if (nbt.getBoolean("setGuiState")) {
                        if (!TRAConfigs.Any.useOldLoadingGui) {
                            Minecraft.getMinecraft().displayGuiScreen(new LoadingGui());
                        } else {
                            Minecraft.getMinecraft().displayGuiScreen(new OldLoadingGui());
                        }
                    } else {
                        Minecraft.getMinecraft().displayGuiScreen(null);
                    }
                }
            });
            return null;
        }
    }
}
