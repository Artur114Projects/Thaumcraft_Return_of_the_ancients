package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.ancientworldgeneration.util.Team;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class AncientEntryTeam extends AncientEntry {
    public AncientEntryTeam(int pos, Team team) {
        super(pos);
    }

    public AncientEntryTeam(NBTTagCompound nbt) {
        super(nbt);
    }

    @Override
    protected void onBossTiger(EntityPlayer player, World world) {

    }

    @Override
    public boolean dead(UUID id) {
        return false;
    }

    @Override
    public boolean interrupt(UUID id) {
        return false;
    }

    @Override
    public void onPlease(int x, int y) {

    }

    @Override
    public void onClear(int x, int y) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPleaseStart() {

    }

    @Override
    public void onClearStart() {

    }

    @Override
    public void onReloadLightStart() {

    }

    @Override
    public void onFinish() {

    }
}
