package com.artur114.thaumrota.common.worldstate.ancientworld.system.client;

import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ClientAncientLayer1Manager implements IClientAncientLayer1Manager {
    private final Minecraft mc = Minecraft.getMinecraft();
    private AncientLayer1Client layerClient = null;
    private final World world;
    public ClientAncientLayer1Manager(World world) {
        this.world = world;
    }

    @Override
    public @Nullable AncientLayer1 sectorFor(EntityPlayer player) {
        if (player == this.mc.player) {
            return layerClient;
        }
        return null;
    }

    @Override
    public void worldTick() {
        if (this.layerClient == null) {
            return;
        }

        if (this.layerClient.isRequestToDelete()) {
            this.layerClient = null;
            return;
        }

        this.layerClient.update();
    }

    @Override
    public void createAncientLayer(NBTTagCompound data) {
        this.layerClient = new AncientLayer1Client(mc.player);
        this.layerClient.setWorld(this.world);
        this.layerClient.readFromNBT(data);
        this.layerClient.constructFinish();
    }

    @Override
    public void handleUpdateTag(NBTTagCompound data) {
        if (data.hasKey("create")) {
            this.createAncientLayer(data.getCompoundTag("create"));
        }
        if (data.hasKey("build")) {
            if (data.getBoolean("build")) {
                this.startBuild();
            } else {
                this.finishBuild();
            }
        }
        if (data.hasKey("playersState")) {
            this.layerClient.updatePlayersState(data.getTagList("playersState", 8));
        }
        if (data.hasKey("structuresSync")) {
            this.layerClient.handleStructuresUpdate(data.getCompoundTag("structuresSync"));
        }
    }

    @Override
    public void startBuild() {
        if (this.layerClient != null) {
            this.layerClient.onBuildStart();
        }
    }

    @Override
    public void finishBuild() {
        if (this.layerClient != null) {
            this.layerClient.onBuildFinish();
        }
    }
}
