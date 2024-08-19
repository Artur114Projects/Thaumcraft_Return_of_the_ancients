package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;

public class AncientEntrySolo extends AncientEntry {

    private EntityPlayerMP player;
    private final UUID playerId;
    public AncientEntrySolo(EntityPlayerMP player, int pos) {
        super(pos);
        this.player = player;
        this.playerId = player.getUniqueID();
        this.startGen();
    }

    public AncientEntrySolo(NBTTagCompound nbt) {
        super(nbt);
        if (!nbt.hasKey("IsTeam")) error("AncientEntrySolo.class, transferred incorrect NBTTag EC:-1");
        if (nbt.getBoolean("IsTeam")) error("AncientEntrySolo.class, transferred incorrect NBTTag EC:0");
        if (!nbt.hasKey("playerMost") || !nbt.hasKey("playerLeast")) error("AncientEntrySolo.class, transferred incorrect NBTTag EC:1");
        playerId = nbt.getUniqueId("player");
        isSleep = true;
    }

    @Override
    public boolean dead(UUID id) {
        if (isSleep) {
            return false;
        }
        if (id.equals(playerId)) {
            requestToDelete();
            return true;
        }
        return false;
    }

    @Override
    protected void onRequestToDelete() {}

    @Override
    protected void onBossDead() {
        player.addItemStackToInventory(getPrimordialPearl());
    }

    @Override
    public boolean interrupt(UUID id) {
        if (id.equals(playerId)) {
            requestToDelete();
            if (TRAConfigs.Any.debugMode) System.out.println("build is interrupt!");
            return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        if (!isRequestToDelete()) {
            NBTTagCompound nbt = super.writeToNBT();
            nbt.setBoolean("IsTeam", false);
            nbt.setUniqueId("player", playerId);
            return nbt;
        }
        return new NBTTagCompound();
    }

    @Override
    public boolean wakeUp(EntityPlayerMP player) {
        if (!isSleep) {
            return false;
        }
        if (player.getUniqueID().equals(playerId)) {
            this.player = player;
            return super.wakeUp(player);
        }
        return false;
    }

    @Override
    public void update(World world) {
        super.update(world);
        if (player == null) {
            isSleep = true;
        }
        if (isSleep) {
            return;
        }
        if (isRequestToDelete()) {
            return;
        }
        if (player.dimension != InitDimensions.ancient_world_dim_id) {
            requestToDelete();
        }
    }

    @Override
    protected void onBossTiger(EntityPlayer player, World world) {
        Entity boss = getRandomBoss(world, bossPos);
        bossUUID = boss.getUniqueID();
        world.spawnEntity(boss);
        pleaseBossDoors(world, bossPos);
    }

    @Override
    public void onGen(int x, int y) {
        HandlerR.injectPercentagesOnClient(player, x, y);
    }

    @Override
    public void onClear(int x, int y) {
        HandlerR.injectPercentagesOnClient(player, x, y);
    }

    @Override
    public void onStart() {
        HandlerR.setLoadingGuiState(player, true);
        HandlerR.injectPhaseOnClient(player, (byte) 0);
        HandlerR.injectPercentagesOnClient(player, 0, 0);
    }

    @Override
    public void onGenStart() {
        HandlerR.injectPercentagesOnClient(player, 0, 0);
        HandlerR.injectPhaseOnClient(player, (byte) 2);
    }

    @Override
    public void onClearStart() {
        HandlerR.injectPercentagesOnClient(player, 0, 0);
        HandlerR.injectPhaseOnClient(player, (byte) 1);
    }

    @Override
    public void onReloadLightStart() {
        HandlerR.injectPhaseOnClient(player, (byte) 3);
    }

    @Override
    public void onFinal() {
        HandlerR.injectPhaseOnClient(player, (byte) 4);
        HandlerR.setLoadingGuiState(player, false);
        if (player.isCreative() && TRAConfigs.Any.debugMode) {
            player.connection.setPlayerLocation(8 + (10000 * pos), 126, -10, -181, 0);
        } else {
            player.connection.setPlayerLocation(8 + (10000 * pos), 253, 8, -181, 90);
        }
        player.setHealth(20);
    }
}
