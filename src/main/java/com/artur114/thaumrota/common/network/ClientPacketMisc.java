package com.artur114.thaumrota.common.network;

import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.common.misc.RotAConfigs;
import com.artur114.thaumrota.main.ThaumRotA;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientPacketMisc implements IMessage { // TODO: 16.11.2025 Упразднить!
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
                String TITLE = TextFormatting.DARK_PURPLE + RotAConfigs.Any.ModChatName + TextFormatting.RESET;
                if (nbt.hasKey("changeTitle")) {
                    TITLE = nbt.getString("changeTitle");
                }
                if (nbt.hasKey("sendAncientWorldLoadMessage")) {
                    if (nbt.getBoolean("sendAncientWorldLoadMessage")) {
                        playerSP.sendMessage(new TextComponentString(TITLE + I18n.translateToLocal(ThaumRotA.MODID + ".message.ancientworldload.start")));
                    } else {
                        playerSP.sendMessage(new TextComponentString(TITLE + I18n.translateToLocal(ThaumRotA.MODID + ".message.ancientworldload.finish")));
                    }
                } else if (nbt.hasKey("sendMessage")) {
                    playerSP.sendMessage(new TextComponentString(TITLE + nbt.getString("sendMessage")));
                } else if (nbt.hasKey("sendMessageTranslate")) {
                    playerSP.sendMessage(new TextComponentString(TITLE + I18n.translateToLocal(nbt.getString("sendMessageTranslate"))));
                }
                if (nbt.hasKey("playSound")) {
                    playerSP.playSound(InitSounds.SOUND_MAP.get(nbt.getString("playSound")), 1, 1);
                }
            });
            return null;
        }
    }
}
