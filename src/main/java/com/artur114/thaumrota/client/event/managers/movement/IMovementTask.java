package com.artur114.thaumrota.client.event.managers.movement;

import net.minecraft.entity.player.EntityPlayer;

public interface IMovementTask {
    void move(EntityPlayer player);
    boolean isDoneWork();
    boolean isNeedToWorkAlone();
    default void onDoneWork() {}
}
