package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.containers.ContainerWithPages;
import com.artur.returnoftheancients.containers.IContainerWithPages;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class ServerPacketSyncContainerHideSlots implements IMessage {
    private int dimension;
    private BlockPos pos;
    private List<Integer> hide;
    private List<Integer> notHide;

    public ServerPacketSyncContainerHideSlots() {
    }

    public ServerPacketSyncContainerHideSlots(IContainerWithPages tile, List<Integer> hide, List<Integer> notHide) {
        this.dimension = tile.getDimension();
        this.pos = tile.getPosC();
        this.notHide = notHide;
        this.hide = hide;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());

        int size0 = buf.readInt();
        hide = new ArrayList<>(size0);
        for (int i = 0; i != size0; i++) {
            hide.add(buf.readInt());
        }

        int size1 = buf.readInt();
        notHide = new ArrayList<>(size1);
        for (int i = 0; i != size1; i++) {
            notHide.add(buf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeLong(pos.toLong());

        buf.writeInt(hide.size());
        for (int slot : hide) {
            buf.writeInt(slot);
        }

        buf.writeInt(notHide.size());
        for (int slot : notHide) {
            buf.writeInt(slot);
        }
    }


    public static class HandlerSHS implements IMessageHandler<ServerPacketSyncContainerHideSlots, IMessage> {

        @Override
        public IMessage onMessage(ServerPacketSyncContainerHideSlots message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            if (!world.isBlockLoaded(message.pos)) {
                return null;
            }
            TileEntity tileRaw = world.getTileEntity(message.pos);
            if (tileRaw instanceof IContainerWithPages) {
                IContainerWithPages tile = (IContainerWithPages) tileRaw;
                ContainerWithPages container = tile.getContainer();
                if (container != null) {
                    container.hideSlots(message.hide);
                    container.unHideSlots(message.notHide);
                }
            }
            return null;
        }
    }

}
