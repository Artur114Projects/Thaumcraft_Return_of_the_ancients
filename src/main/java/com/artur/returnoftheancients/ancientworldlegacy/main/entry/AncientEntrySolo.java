package com.artur.returnoftheancients.ancientworldlegacy.main.entry;

import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitDimensions;
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

import java.util.UUID;

public class AncientEntrySolo extends AncientEntry {

    private EntityPlayerMP player;
    private final UUID playerId;
    public AncientEntrySolo(EntityPlayerMP player, int pos) {
        super(pos);
        this.player = player;
        this.playerId = player.getUniqueID();
        this.startGen(mapSeed);
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
            AncientPortalsProcessor.teleportToOverworld(player);
            return true;
        }
        return false;
    }

    @Override
    protected void error(String s) {
        super.error(s);
        if (!isSleep) {
            player.sendMessage(new TextComponentString(s).setStyle(new Style().setColor(TextFormatting.DARK_RED)));
            player.sendMessage(new TextComponentString("deleting...").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
        }
    }

    @Override
    protected void onRequestToDelete() {}

    @Override
    protected void onBossDead() {
        if (isSleep) return;
        player.addItemStackToInventory(getPrimordialPearl());
        MiscHandler.researchTC(player, "f_!FINAL");
    }

    @Override
    protected void addFog() {
        if (!isBossSpawn && !player.isCreative() && TRAConfigs.AncientWorldSettings.isAddFogOnAncientWorld) {

        }
    }

    @Override
    public boolean isBelong(EntityPlayerMP player) {
        return player.getUniqueID().equals(playerId);
    }

    @Override
    public boolean sleepPlayer(UUID id) {
        if (isSleep) {
            return false;
        }
        if (id.equals(playerId)) {
            if (TRAConfigs.Any.debugMode) System.out.println("Player: " + player.getName() + " is out game");
            isSleep = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean interrupt(UUID id) {
        if (id.equals(playerId)) {
            requestToDelete();
            if (TRAConfigs.Any.debugMode) System.out.println("build is interrupt!");
            AncientPortalsProcessor.teleportToOverworld(player);
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

        int dx = ((((int) player.posX - (10000 * pos)) >> 4) - 8) * -1;
        int dz = ((((int) player.posZ) >> 4) - 8) * -1;
        if (dx >= 0 && dx < map.SIZE && dz >= 0 && dz < map.SIZE) {
            if (map.getDeformation(dx, dz) > 4) {
                MiscHandler.researchAndSendMessage(player, "DEFORMATION", Referense.MODID + ".text.deformation");
            }
        }
    }

    @Override
    protected void onBossTiger(EntityPlayer player, World world) {
        MiscHandler.researchAndSendMessage((EntityPlayerMP) player, "m_BOSS", Referense.MODID + ".text.boss");
        Entity boss = getRandomBoss(world, bossPos);
        bossUUID = boss.getUniqueID();
        genBossDoors(world, bossPos);
    }

    @Override
    public void onGen(int x, int y) {
        MiscHandler.injectPercentagesOnClient(player, x, y);
    }

    @Override
    public void onClear(int x, int y) {
        MiscHandler.injectPercentagesOnClient(player, x, y);
    }

    @Override
    public void onStart() {
        MiscHandler.setLoadingGuiState(player, true, false);
        MiscHandler.injectPhaseOnClient(player, (byte) 0);
        MiscHandler.injectPercentagesOnClient(player, 0, 0);
    }

    @Override
    public void onGenStart() {
        MiscHandler.injectPercentagesOnClient(player, 0, 0);
        MiscHandler.injectPhaseOnClient(player, (byte) 2);
    }

    @Override
    public void onClearStart() {
        MiscHandler.injectPercentagesOnClient(player, 0, 0);
        MiscHandler.injectPhaseOnClient(player, (byte) 1);
    }

    @Override
    public void onFinalizing() {
        MiscHandler.injectPhaseOnClient(player, (byte) 3);
    }

    @Override
    public void onFinal() {
        MiscHandler.researchAndSendMessage(player, "m_ENTRY_ANCIENT", Referense.MODID + ".text.entered_ancient");
        MiscHandler.injectPhaseOnClient(player, (byte) 4);
        MiscHandler.setLoadingGuiState(player, false, false);
        player.connection.setPlayerLocation(8 + (10000 * pos), 253, 8, player.rotationYaw, 90);
        player.setHealth(20);
    }
}
