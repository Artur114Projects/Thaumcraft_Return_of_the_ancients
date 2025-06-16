package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.blockprotect.IProtectedChunk;
import com.artur.returnoftheancients.blockprotect.client.IClientProtectedChunk;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.handlers.MiscHandler;
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
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.dimension = buf.readInt();

        this.data = ByteBufUtils.readTag(buf);

        this.chunk = MiscHandler.chunkPosFromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dimension);

        ByteBufUtils.writeTag(buf, this.data);

        buf.writeLong(MiscHandler.chunkPosAsLong(this.chunk));
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
                    IProtectedChunk protectedChunk = chunk.getCapability(TRACapabilities.PROTECTED_CHUNK, null);
                    if (protectedChunk instanceof IClientProtectedChunk) {
                        ((IClientProtectedChunk) protectedChunk).processSyncData(message.data);
                    }
                }
            });
            return null;
        }
    }
}
