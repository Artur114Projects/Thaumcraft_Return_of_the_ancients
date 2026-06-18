package com.artur114.thaumrota.common.worldstate.ancientworld.system.client;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.util.graphs.BananaGraphs;
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
    private StrPos lastPlayerPos = null;

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

        IStructure curr = this.player.currentRoom();
        if (curr != null) {
            StrPos pos = curr.pos();

            if (!pos.equals(this.lastPlayerPos)) {
                this.lastPlayerPos = pos;
                HeatRenderer.clearLight("ancient_world");
                HeatRenderer.addLight("ancient_world", curr.light(new ChunkPos(this.pos.x + (this.size / 2) - (pos.getX()), this.pos.z + (this.size / 2) - (pos.getY()))));
                BananaGraphs.bfs(pos, this.map::connectedStructures, p -> p.distanceM(pos) <= 3, (p) -> {
                    IStructure str = this.map.structure(p);
                    if (str != null) {
                        int x = this.pos.x + (this.size / 2) - (p.getX());
                        int z = this.pos.z + (this.size / 2) - (p.getY());
                        HeatRenderer.addLight("ancient_world", str.light(new ChunkPos(x, z)));
                    }
                    return false;
                });
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
        this.seed = nbt.getLong("seed");

        NBTTagList list = nbt.getTagList("playersState", 8);

        for (int i = 0; i != list.tagCount(); i++) {
            this.playersState.add(list.getStringTagAt(i));
        }

        this.mapData = nbt.getCompoundTag("map");
    }
}