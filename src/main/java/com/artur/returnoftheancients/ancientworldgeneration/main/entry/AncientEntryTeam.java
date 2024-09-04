package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.ancientworldgeneration.util.Team;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;

public class AncientEntryTeam extends AncientEntry {
    protected final Team team;
    public AncientEntryTeam(int pos, Team team) {
        super(pos);
        this.team = team;
        if (!isBuild) startGen(mapSeed);
    }

    public AncientEntryTeam(NBTTagCompound nbt) {
        super(nbt);
        if (!nbt.hasKey("IsTeam")) error("AncientEntryTeam.class, transferred incorrect NBTTag EC:-1");
        if (!nbt.getBoolean("IsTeam")) error("AncientEntryTeam.class, transferred incorrect NBTTag EC:0");
        if (!nbt.hasKey("team")) error("AncientEntryTeam.class, transferred incorrect NBTTag EC:1");
        team = new Team(nbt.getCompoundTag("team"));
        if (team.isRequestDelete()) requestToDelete();
        isSleep = true;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = super.writeToNBT();
        nbt.setBoolean("IsTeam", true);
        nbt.setTag("team", team.toNBT());
        return nbt;
    }

    @Override
    public void update(World world) {
        if (team == null) {
            requestToDelete();
            return;
        }
        if (team.isRequestDelete()) {
            team.setToAll(ServerEventsHandler::tpToHome);
            requestToDelete();
        }
        if (!team.isActive()) isSleep = true;
        super.update(world);
        team.update();
    }

    @Override
    public boolean wakeUp(EntityPlayerMP player) {
        if (!isSleep) {
            return false;
        }
        if (team.initialise(player)) {
            return super.wakeUp(player);
        }
        return false;
    }

    @Override
    protected void onBossTiger(EntityPlayer player, World world) {
        team.setToAll(playerSet -> playerSet.connection.setPlayerLocation(player.posX, player.posY, player.posZ, playerSet.rotationYaw, playerSet.rotationPitch));
        Entity boss = getRandomBoss(world, bossPos);
        bossUUID = boss.getUniqueID();
        world.spawnEntity(boss);
        pleaseBossDoors(world, bossPos);
    }

    @Override
    public boolean dead(UUID id) {
        return team.kill(id);
    }

    @Override
    protected void onRequestToDelete() {
        if (team != null) {
            team.delete();
        }
    }

    @Override
    protected void onBossDead() {
        team.setToAll(player -> player.addItemStackToInventory(getPrimordialPearl()));
    }

    @Override
    public boolean interrupt(UUID id) {
        if (team.contains(id)) {
            team.kill(id);
            if (!team.isActive()) {
                requestToDelete();
                if (TRAConfigs.Any.debugMode) System.out.println("build is interrupt!");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onGen(int x, int y) {
        team.setToAll(player -> HandlerR.injectPercentagesOnClient(player, x, y));
    }

    @Override
    public void onClear(int x, int y) {
        team.setToAll(player -> HandlerR.injectPercentagesOnClient(player, x, y));
    }

    @Override
    public void onStart() {
        team.setToAll(player -> {
            HandlerR.setLoadingGuiState(player, true);
            HandlerR.injectPhaseOnClient(player, (byte) 0);
            HandlerR.injectPercentagesOnClient(player, 0, 0);
        });
    }

    @Override
    public void onGenStart() {
        team.setToAll(player -> {
            HandlerR.injectPercentagesOnClient(player, 0, 0);
            HandlerR.injectPhaseOnClient(player, (byte) 2);
        });
    }

    @Override
    public void onClearStart() {
        team.setToAll(player -> {
            HandlerR.injectPercentagesOnClient(player, 0, 0);
            HandlerR.injectPhaseOnClient(player, (byte) 1);
        });

    }

    @Override
    public void onReloadLightStart() {
        team.setToAll(player -> HandlerR.injectPhaseOnClient(player, (byte) 3));
    }

    @Override
    public void onFinal() {
        team.setToAll(player -> {
            HandlerR.injectPhaseOnClient(player, (byte) 4);
            HandlerR.setLoadingGuiState(player, false);
            player.setHealth(20);
            player.connection.setPlayerLocation(8 + (10000 * pos), 253, 8, -181, 90);
            HandlerR.researchAndSendMessage(player, "m_ENTRY_ANCIENT", Referense.MODID + ".text.entered_ancient");
        });
    }
}
