package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GlobalTickManager {
    public long prevGameTickCounter = 0;
    public long gameTickCounter = 0;

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        this.prevGameTickCounter = this.gameTickCounter;

        if (player == null || mc.isGamePaused()) {
            return;
        }

        this.gameTickCounter++;

        if (this.gameTickCounter < 0) {
            this.prevGameTickCounter = 0;
            this.gameTickCounter = 0;
        }
    }

    public double interpolatedGameTickCounter(float pct) {
        return this.prevGameTickCounter + (this.gameTickCounter - this.prevGameTickCounter) * pct;
    }
}
