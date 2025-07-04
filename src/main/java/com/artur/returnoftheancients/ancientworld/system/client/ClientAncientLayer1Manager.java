package com.artur.returnoftheancients.ancientworld.system.client;

import com.artur.returnoftheancients.handlers.MiscHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ClientAncientLayer1Manager implements IClientAncientLayer1Manager {
    private final Minecraft mc = Minecraft.getMinecraft();
    private AncientLayer1Client layerClient = null;
    private final World world;
    public ClientAncientLayer1Manager(World world) {
        this.world = world;
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
                this.finisBuild();
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
    public void finisBuild() {
        if (this.layerClient != null) {
            this.layerClient.onBuildFinish();
        }
    }
}
