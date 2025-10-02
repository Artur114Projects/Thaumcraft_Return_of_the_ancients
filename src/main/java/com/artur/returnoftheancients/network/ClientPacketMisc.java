package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.misc.TRAConfigs;
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

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

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
                if (nbt.hasKey("sendAncientWorldLoadMessage")) {
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
                if (nbt.hasKey("playSound")) {
                    playerSP.playSound(InitSounds.SOUND_MAP.get(nbt.getString("playSound")), 1, 1);
                }
                if (nbt.hasKey("syncWorldDataFields")) {
                    WorldDataFields.readOnClient(nbt.getCompoundTag("syncWorldDataFields"));
                }
            });
            return null;
        }
    }
}
