package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketTileAncientTeleportData implements IMessage {

    private int dimension;
    private BlockPos pos;
    private int id;

    public ServerPacketTileAncientTeleportData() {

    }

    public ServerPacketTileAncientTeleportData(TileEntity tile, int id) {
        this.dimension = tile.getWorld().provider.getDimension();
        this.pos = tile.getPos();
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
        id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeLong(pos.toLong());
        buf.writeInt(id);
    }

    public static class HandlerTATD implements IMessageHandler<ServerPacketTileAncientTeleportData, IMessage> {

        @Override
        public IMessage onMessage(ServerPacketTileAncientTeleportData message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            if (!world.isBlockLoaded(message.pos)) {
                return null;
            }
            TileEntity tileRaw = world.getTileEntity(message.pos);
            if (tileRaw instanceof TileEntityAncientTeleport) {
                TileEntityAncientTeleport tile = (TileEntityAncientTeleport) tileRaw;
                switch (message.id) {
                    case 0:{
                        tile.requestToActivate(ctx.getServerHandler().player);
                    } break;
                }
            }
            return null;
        }
    }
}
