package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.ancientworldgeneration.util.Team;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.ITeamTask;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

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
        team.setToAll(updateResearch);
    }

    private final ITeamTask updateResearch = player -> {
        int dx = ((((int) player.posX - (10000 * pos)) >> 4) - 8) * -1;
        int dz = ((((int) player.posZ) >> 4) - 8) * -1;
        if (dx >= 0 && dx < map.SIZE && dz >= 0 && dz < map.SIZE) {
            if (map.getDeformation(dx, dz) > 4) {
                HandlerR.researchAndSendMessage(player, "DEFORMATION", Referense.MODID + ".text.deformation");
            }
        }
    };

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
    public boolean isBelong(EntityPlayerMP player) {
        return team.contains(player.getUniqueID());
    }

    @Override
    public boolean sleepPlayer(UUID id) {
        if (isSleep) {
            return false;
        }
        boolean flag = team.sleepPlayer(id);
        if (flag) {
            if (!team.isActive()) {
                isSleep = true;
            }
        }
        return flag;
    }

    @Override
    protected void onBossTiger(EntityPlayer player, World world) {
        team.setToAll(playerSet -> {
            playerSet.connection.setPlayerLocation(player.posX, player.posY, player.posZ, playerSet.rotationYaw, playerSet.rotationPitch);
            HandlerR.researchAndSendMessage(playerSet, "m_BOSS", Referense.MODID + ".text.boss");
        });
        Entity boss = getRandomBoss(world, bossPos);
        bossUUID = boss.getUniqueID();
        world.spawnEntity(boss);
        genBossDoors(world, bossPos);
    }

    @Override
    public boolean dead(UUID id) {
        return team.kill(id);
    }

    @Override
    protected void error(String s) {
        super.error(s);
        if (!isSleep) {
            team.setToAll(player -> {
                player.sendMessage(new TextComponentString(s).setStyle(new Style().setColor(TextFormatting.DARK_RED)));
                player.sendMessage(new TextComponentString("deleting...").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
            });
        }
    }

    @Override
    protected void onRequestToDelete() {
        if (team != null) {
            team.delete();
        }
    }

    @Override
    protected void onBossDead() {
        team.setToAll(player -> {
            player.addItemStackToInventory(getPrimordialPearl());
            HandlerR.researchTC(player, "!FINAL");
        });
    }

    @Override
    protected void addFog() {
        if (!isBossSpawn) {
            team.setToAll(setFog);
        }
    }

    private final ITeamTask setFog = player -> PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte) 2), player);

    @Override
    public boolean interrupt(UUID id) {
        if (team.contains(id)) {
            team.kill(id);
            if (!team.isActive()) {
                requestToDelete();
                if (TRAConfigs.Any.debugMode) System.out.println("build is interrupt!");
            }
            team.injectNamesToPlayers();
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
            HandlerR.setLoadingGuiState(player, true, true);
            team.injectNamesToPlayers();
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
    public void onFinalizing() {
        team.setToAll(player -> HandlerR.injectPhaseOnClient(player, (byte) 3));
    }

    @Override
    public void onFinal() {
        team.setToAll(player -> {
            HandlerR.injectPhaseOnClient(player, (byte) 4);
            HandlerR.setLoadingGuiState(player, false, true);
            player.setHealth(20);
            player.connection.setPlayerLocation(8 + (10000 * pos), 253, 8, -181, 90);
            HandlerR.researchAndSendMessage(player, "m_ENTRY_ANCIENT", Referense.MODID + ".text.entered_ancient");
        });
    }
}
