package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Objects;
import java.util.UUID;

public class AncientEntrySolo extends AncientEntry {
    private final EntityPlayerMP player;
    public AncientEntrySolo(EntityPlayerMP player, int pos) {
        super(pos);
        this.player = player;
        startGen();
    }

    public AncientEntrySolo(NBTTagCompound nbt) {
        super(nbt);
        if (!nbt.hasKey("IsTeam")) throw new RuntimeException("AncientEntrySolo.class, transferred incorrect NBTTag EC:-1");
        if (nbt.getBoolean("IsTeam")) throw new RuntimeException("AncientEntrySolo.class, transferred incorrect NBTTag EC:0");

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!nbt.hasKey("playerMost") || !nbt.hasKey("playerLeast")) throw new RuntimeException("AncientEntrySolo.class, transferred incorrect NBTTag EC:1");
        Entity entity = server.getEntityFromUuid(Objects.requireNonNull(nbt.getUniqueId("player")));
        if (entity instanceof EntityPlayerMP) {
            player = (EntityPlayerMP) entity;
        } else {
            throw new RuntimeException("AncientEntrySolo.class, entity not instanceof EntityPlayerMP: " + entity);
        }

        if (!isBuild) startGen();
    }

    @Override
    public void dead(UUID id) {
        if (id.equals(player.getUniqueID())) {
            requestToDelete();
        }
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = super.toNBT();
        nbt.setBoolean("IsTeam", false);
        nbt.setUniqueId("player", player.getUniqueID());
        return nbt;
    }

    @Override
    public void update() {
        if (player.dimension != InitDimensions.ancient_world_dim_id) {
            requestToDelete();
        }
    }

    @Override
    public void onPlease(int x, int y) {
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
    public void onPleaseStart() {
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
    public void onFinish() {
        HandlerR.injectPhaseOnClient(player, (byte) 4);
        HandlerR.setLoadingGuiState(player, false);
        player.connection.setPlayerLocation(8 + (10000 * pos), 253, 8, -181, 0);
        player.setHealth(20);
    }
}
