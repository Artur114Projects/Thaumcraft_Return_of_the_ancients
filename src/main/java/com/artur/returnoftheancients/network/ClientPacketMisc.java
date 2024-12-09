package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.client.gui.CoolLoadingGui;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.client.gui.LoadingGui;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.referense.Referense;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public static class HandlerM implements IMessageHandler<ClientPacketMisc, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public  IMessage onMessage(ClientPacketMisc message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                NBTTagCompound nbt = message.data;
                EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                String TITLE = TextFormatting.DARK_PURPLE + TRAConfigs.Any.ModChatName + TextFormatting.RESET;
                if (nbt.hasKey("changeTitle")) {
                    TITLE = nbt.getString("changeTitle");
                }
                if (nbt.hasKey("setGuiState")) {
                    if (nbt.getBoolean("setGuiState")) {

                        LoadingGui.injectPercentages((byte) 0);
                        LoadingGui.injectPhase((byte) 0);

                        if (TRAConfigs.Any.useOldLoadingGui) {
                            Minecraft.getMinecraft().displayGuiScreen(new LoadingGui(nbt.getBoolean("isTeam")));
                        } else {
                            Minecraft.getMinecraft().displayGuiScreen(new CoolLoadingGui(nbt.getBoolean("isTeam")));
                        }

                    } else {
                        if (TRAConfigs.Any.useOldLoadingGui) {
                            Minecraft.getMinecraft().displayGuiScreen(null);
                        } else {
                            CoolLoadingGui.instance.close();
                        }
                    }
                } else if (nbt.hasKey("sendAncientWorldLoadMessage")) {
                    if (nbt.getBoolean("sendAncientWorldLoadMessage")) {
                        playerSP.sendMessage(new TextComponentString(TITLE + I18n.translateToLocal(Referense.MODID + ".message.ancientworldload.start")));
                    } else {
                        playerSP.sendMessage(new TextComponentString(TITLE + I18n.translateToLocal(Referense.MODID + ".message.ancientworldload.finish")));
                    }
                } else if (nbt.hasKey("sendMessage")) {
                    playerSP.sendMessage(new TextComponentString(TITLE + nbt.getString("sendMessage")));
                } else if (nbt.hasKey("sendMessageTranslate")) {
                    playerSP.sendMessage(new TextComponentString(TITLE + I18n.translateToLocal(nbt.getString("sendMessageTranslate"))));
                }
                if (nbt.hasKey("injectPhase")) {
                    LoadingGui.injectPhase(nbt.getByte("injectPhase"));
                }
                if (nbt.hasKey("injectPercentages")) {
                    LoadingGui.injectPercentages(nbt.getByte("injectPercentages"));
                }
                if (nbt.hasKey("playSound")) {
                    playerSP.playSound(InitSounds.SOUND_MAP.get(nbt.getString("playSound")), 1, 1);
                }
                if (nbt.hasKey("stopSound")) {
                    playerSP.playSound(InitSounds.SOUND_MAP.get(nbt.getString("stopSound")), 0, 1);
                }
                if (nbt.hasKey("syncWorldDataFields")) {
                    WorldDataFields.readOnClient(nbt.getCompoundTag("syncWorldDataFields"));
                }
                if (nbt.hasKey("injectNamesOnClient")) {
                    NBTTagList list = nbt.getTagList("injectNamesOnClient", 8);
                    String[] names = new String[list.tagCount()];
                    for (int i = 0; i != list.tagCount(); i++) {
                        names[i] = list.getStringTagAt(i);
                    }
                    if (TRAConfigs.Any.useOldLoadingGui) {
                        LoadingGui.injectPlayers(names);
                    } else {
                        CoolLoadingGui.instance.updatePlayersList(names);
                    }
                }
            });
            return null;
        }
    }
}
