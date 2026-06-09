package com.artur114.returnoftheancients.client.gui;

import com.artur114.bananalib.mc.register.ann.AutoInstantiate;
import com.artur114.bananalib.mc.register.interf.ILoadStageInit;
import com.artur114.returnoftheancients.client.gui.container.GuiAncientTeleport;
import com.artur114.returnoftheancients.common.containers.ContainerAncientTeleport;
import com.artur114.returnoftheancients.common.tileentity.TileEntityAncientTeleport;
import com.artur114.returnoftheancients.main.ThaumicRotA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@AutoInstantiate
public class RotAGuiHandler implements IGuiHandler, ILoadStageInit {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
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
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te != null) {
            if (ID == 0) {
                return new GuiAncientTeleport((TileEntityAncientTeleport)te, player);
            }
        }
        return null;
    }

    @Override
    public void onInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ThaumicRotA.INSTANCE, this);
    }
}