package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.gui.LoadingGui;
import com.artur.returnoftheancients.gui.OldLoadingGui;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.referense.Referense;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketMisc implements IMessage {
    private NBTTagCompound data;

    public ClientPacketMisc() {
    }

    public ClientPacketMisc(NBTTagCompound data) {
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

    public static class HandlerLG implements IMessageHandler<ClientPacketMisc, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketMisc message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (HandlerR.isGoodNBTTagMisc(message.data)) {
                    NBTTagCompound nbt = message.data;
                    EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                    String TRA = TextFormatting.DARK_PURPLE + TRAConfigs.Any.ModChatName + TextFormatting.RESET;
                    if (nbt.hasKey("setGuiState")) {
                        if (nbt.getBoolean("setGuiState")) {
                            if (!TRAConfigs.Any.useOldLoadingGui) {
                                Minecraft.getMinecraft().displayGuiScreen(new LoadingGui());
                            } else {
                                Minecraft.getMinecraft().displayGuiScreen(new OldLoadingGui());
                            }
                        } else {
                            Minecraft.getMinecraft().displayGuiScreen(null);
                        }
                    } else if (nbt.hasKey("sendAncientWorldLoadMessage")) {
                        if (nbt.getBoolean("sendAncientWorldLoadMessage")) {
                            playerSP.sendMessage(new TextComponentString(TRA + I18n.translateToLocal(Referense.MODID + ".message.ancientworldload.start")));
                        } else {
                            playerSP.sendMessage(new TextComponentString(TRA+ I18n.translateToLocal(Referense.MODID + ".message.ancientworldload.finish")));
                        }
                    } else if (nbt.hasKey("sendMessage")) {
                        playerSP.sendMessage(new TextComponentString(TRA + nbt.getString("sendMessage")));
                    } else if (nbt.hasKey("sendMessageTranslate")) {
                        playerSP.sendMessage(new TextComponentString(TRA + I18n.translateToLocal(nbt.getString("sendMessageTranslate"))));
                    }
                    if (nbt.hasKey("injectPhase")) {
                        LoadingGui.injectPhase(nbt.getByte("injectPhase"));
                    }
                    if (nbt.hasKey("injectPercentages")) {
                        LoadingGui.injectPercentages(nbt.getByte("injectPercentages"));
                    }
                } else {
                    System.out.println("ti vtiraeh mne kakuyto dich");
                }
            });
            return null;
        }
    }
}
