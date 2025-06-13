package com.artur.returnoftheancients.ancientworld.system.client;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.client.gui.CoolLoadingGui;
import com.artur.returnoftheancients.handlers.MiscHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class AncientLayer1Client extends AncientLayer1 {
    protected List<String> playersState = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public AncientLayer1Client(EntityPlayerSP player) {
        this.addPlayer(new AncientWorldPlayer(player));
    }

    @Override
    public void constructFinish() {
        this.createMap();
    }

    protected void onBuildFinish() {
        if (this.mc.currentScreen instanceof CoolLoadingGui) {
            ((CoolLoadingGui) this.mc.currentScreen).close();
        }
    }

    protected void onBuildStart() {
        this.mc.displayGuiScreen(new CoolLoadingGui(this));
    }

    protected void updatePlayersState(NBTTagList list) {
        this.playersState.clear();

        for (int i = 0; i != list.tagCount(); i++) {
            this.playersState.add(list.getStringTagAt(i));
        }
    }

    public List<String> playersState() {
        return this.playersState;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.pos = MiscHandler.chunkPosFromLong(nbt.getLong("pos"));
        this.posIndex = nbt.getInteger("posIndex");
        this.size = nbt.getInteger("size");

        NBTTagList list = nbt.getTagList("playersState", 8);

        for (int i = 0; i != list.tagCount(); i++) {
            this.playersState.add(list.getStringTagAt(i));
        }
    }
}