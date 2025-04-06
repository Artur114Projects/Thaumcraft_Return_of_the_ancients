package com.artur.returnoftheancients.network;

import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.system.EnergySystemsManager;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.base.NBTPacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketSyncEnergySystem extends NBTPacketBase {
    public ClientPacketSyncEnergySystem() {}

    public ClientPacketSyncEnergySystem(NBTTagCompound nbt) {
        super(nbt);
    }

    public static class HandlerSES implements IMessageHandler<ClientPacketSyncEnergySystem, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketSyncEnergySystem message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;
                NBTTagCompound data = message.nbt;

                if (data.hasKey("onBlockDestroyed")) {
                    BlockPos tilePos = BlockPos.fromLong(data.getLong("onBlockDestroyed"));
                    TileEntity tileRaw = world.getTileEntity(tilePos);
                    if (tileRaw instanceof ITileEnergy) {
                        EnergySystemsManager manager = world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
                        if (manager != null) manager.onBlockDestroyed((ITileEnergy) tileRaw);
                    }
                }
            });
            return null;
        }
    }

    public static void sendOnBlockDestroyed(ITileEnergy tile) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("onBlockDestroyed", tile.pos().toLong());
        MainR.NETWORK.sendToAllTracking(new ClientPacketSyncEnergySystem(nbt), new NetworkRegistry.TargetPoint(tile.world().provider.getDimension(), tile.pos().getX(), tile.pos().getY(), tile.pos().getZ(), 0));
    }
}
