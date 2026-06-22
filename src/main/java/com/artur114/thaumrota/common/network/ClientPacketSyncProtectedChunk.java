package com.artur114.thaumrota.common.network;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.util.CapUtils;
import com.artur114.thaumrota.common.worldstate.blockprotect.IProtectedChunk;
import com.artur114.thaumrota.common.worldstate.blockprotect.client.IClientProtectedChunk;
import com.artur114.thaumrota.common.init.InitCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientPacketSyncProtectedChunk implements IMessage {
    private NBTTagCompound data;
    private ChunkPos chunk;
    private int dimension;

    public ClientPacketSyncProtectedChunk() {}

    public ClientPacketSyncProtectedChunk(int dimensionIn, ChunkPos chunkIn, NBTTagCompound dataIn) {
        this.dimension = dimensionIn;
        this.chunk = chunkIn;
        this.data = dataIn;

        this.data.setLong("chunkPos", BananaMC.chunkPosAsLong(chunkIn));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.dimension = buf.readInt();

        this.data = ByteBufUtils.readTag(buf);

        if (this.data != null) {
            this.chunk = BananaMC.chunkPosFromLong(this.data.getLong("chunkPos"));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dimension);

        ByteBufUtils.writeTag(buf, this.data);
    }

    @SideOnly(Side.CLIENT)
    public static class HandlerSPC implements IMessageHandler<ClientPacketSyncProtectedChunk, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(ClientPacketSyncProtectedChunk message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.world.provider.getDimension() == message.dimension) {
                    Chunk chunk = mc.world.getChunkFromChunkCoords(message.chunk.x, message.chunk.z);
                    CapUtils.capability(chunk, InitCapabilities.PROTECTED_CHUNK, IClientProtectedChunk.class).ifPresent(protectedChunk -> {
                        protectedChunk.processSyncData(message.data);
                    });
                }
            });
            return null;
        }
    }
}
