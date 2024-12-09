package com.artur.returnoftheancients.client.gui;

import com.artur.returnoftheancients.containers.ContainerAncientTeleport;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te != null) {
            if (ID == 0) {
                return new ContainerAncientTeleport(player.inventory, (TileEntityAncientTeleport) te);
            }
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te != null) {
            if (ID == 0) {
                return new GuiAncientTeleport((TileEntityAncientTeleport)te, player);
            }
        }
        return null;
    }
}