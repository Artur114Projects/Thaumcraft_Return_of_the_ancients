package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.tiles.essentia.TileCentrifuge;

public class ClientPacketSyncTileAncientTeleport implements IMessage {
    private NBTTagCompound data;
    private BlockPos pos;
    private int id;

    public ClientPacketSyncTileAncientTeleport() {

    }

    public ClientPacketSyncTileAncientTeleport(BlockPos pos, int id, NBTTagCompound syncData) {
        this.data = syncData;
        this.pos = pos;
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        data = ByteBufUtils.readTag(buf);
        id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeTag(buf, data);
        buf.writeInt(id);
    }

    public static class HandlerCSTAT implements IMessageHandler<ClientPacketSyncTileAncientTeleport, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketSyncTileAncientTeleport message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            TileEntity tileRaw = world.getTileEntity(message.pos);
            if (tileRaw instanceof TileEntityAncientTeleport) {
                TileEntityAncientTeleport tile = (TileEntityAncientTeleport) tileRaw;
                switch (message.id) {
                    case 0:{
                        tile.aspectBottles.syncInClient(message.data);
                    } break;
                }
            }
            return null;
        }
    }
}
