package com.artur114.thaumrota.common.network;

import com.artur114.thaumrota.client.fx.FXEntitySpawn;
import com.artur114.thaumrota.common.network.base.NBTPacketBase;
import com.artur114.thaumrota.main.ThaumRotA;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ClientPacketCreateFX extends NBTPacketBase {
    private static final Logger log = LogManager.getLogger("ThaumRotA/Network");
    private BlockPos pos;
    private FXType type;

    public ClientPacketCreateFX() {}

    public ClientPacketCreateFX(BlockPos pos, FXType type) {
        this(pos, type, null);
    }

    public ClientPacketCreateFX(BlockPos pos, FXType type, @Nullable NBTTagCompound nbt) {
        super(nbt);
        this.type = type;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        int id = buf.readInt();
        if (id >= 0 && id < FXType.values().length) {
            this.type = FXType.values()[id];
        } else {
            log.warn("Unknown fx id {}", id);
        }
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(this.type.ordinal());
        buf.writeLong(this.pos.toLong());
    }

    public enum FXType {
        ENTITY_SPAWN
    }

    public static class HandlerCFX implements IMessageHandler<ClientPacketCreateFX, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketCreateFX message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                switch (message.type) {
                    case ENTITY_SPAWN:
                        FXEntitySpawn.draw(message.pos);
                        break;
                    default:
                        log.warn("Unknown fx type {}", message.type);
                }
            });
            return null;
        }
    }

    public static void send(World world, BlockPos pos, FXType type) {
        send(world, pos, type, null);
    }

    public static void send(World world, BlockPos pos, FXType type, @Nullable NBTTagCompound nbt) {
        NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 32);
        ThaumRotA.NETWORK.sendToAllAround(new ClientPacketCreateFX(pos, type, nbt), point);
    }
}
