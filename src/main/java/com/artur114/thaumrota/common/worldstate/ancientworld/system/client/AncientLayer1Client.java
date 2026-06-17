package com.artur114.thaumrota.common.worldstate.ancientworld.system.client;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.IStructure;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.utils.AncientWorldPlayer;
import com.artur114.thaumrota.client.gui.CoolLoadingGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class AncientLayer1Client extends AncientLayer1 {
    protected List<String> playersState = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    protected boolean isPlayerWasHigh = false;
    private final AncientWorldPlayer player;

    public AncientLayer1Client(EntityPlayerSP player) {
        this.addPlayer(new AncientWorldPlayer(player));

        this.player = this.players.get(0);
    }

    @Override
    public void constructFinish() {
        this.createMap();
    }

    @Override
    public void update() {
        super.update();

        if (this.player.player.posY > 140) {
            this.isPlayerWasHigh = true;
        } else if (this.player.player.onGround && this.isPlayerWasHigh) {
            this.player.player.playSound(SoundEvents.ENTITY_PLAYER_BIG_FALL, 1.0F, 1.0F); this.isPlayerWasHigh = false;
        }

        HeatRenderer.clearLight();
        StrPos pos = this.player.calculatePosOnMap(this.pos, this.size);
        int radius = 3;
        for (int i = -radius; i != radius + 1; i++) {
            for (int j = -radius; j != radius + 1; j++) {
                StrPos p = pos.add(i, j);
                IStructure str = this.map.structure(p);
                if (str != null) {
                    int x = this.pos.x + (this.size / 2) - (p.getX());
                    int z = this.pos.z + (this.size / 2) - (p.getY());
                    HeatRenderer.addLight(str.light(new ChunkPos(x, z)));
                }
            }
        }
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

    protected void handleStructuresUpdate(NBTTagCompound data) {
        this.map.handleServerSyncData(data);
    }

    public List<String> playersState() {
        return this.playersState;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.pos = BananaMC.chunkPosFromLong(nbt.getLong("pos"));
        this.posIndex = nbt.getInteger("posIndex");
        this.size = nbt.getInteger("size");

        NBTTagList list = nbt.getTagList("playersState", 8);

        for (int i = 0; i != list.tagCount(); i++) {
            this.playersState.add(list.getStringTagAt(i));
        }

        this.mapData = nbt.getCompoundTag("map");
    }
}