package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Collections;
import java.util.List;

public class AncientLayer1Server extends AncientLayer1 {
    public AncientLayer1Server() {}

    public AncientLayer1Server(List<EntityPlayerMP> players) {
        for (EntityPlayerMP player : players) {
            this.addPlayer(new AncientWorldPlayer(player));
        }

        this.createMap();
    }

    public AncientLayer1Server(EntityPlayerMP player) {
        this(Collections.singletonList(player));
    }

    @Override
    protected void onPlayersListChanged() {
        if (this.players.isEmpty()) {
            this.requestToDelete();
        }
    }

    public boolean onPlayerLoginIn(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.player = player;
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerLoginOut(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.player = null;
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerLost(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.onPlayerLost();
            this.removePlayer(ancientPlayer);
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerElope(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.onPlayerElope();
            this.removePlayer(ancientPlayer);
        }

        return ancientPlayer != null;
    }
}
