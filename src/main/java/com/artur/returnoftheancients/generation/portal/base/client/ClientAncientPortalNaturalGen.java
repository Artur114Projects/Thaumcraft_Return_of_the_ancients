package com.artur.returnoftheancients.generation.portal.base.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ClientAncientPortalNaturalGen extends ClientAncientPortal {
    boolean isActive;
    public ClientAncientPortalNaturalGen(NBTTagCompound data) {
        super(data);

        this.isActive = data.getBoolean("isActive");
    }

    @Override
    public void update(EntityPlayer player, World world) {
        if (!isActive) {
            return;
        }
        super.update(player, world);
    }
}
