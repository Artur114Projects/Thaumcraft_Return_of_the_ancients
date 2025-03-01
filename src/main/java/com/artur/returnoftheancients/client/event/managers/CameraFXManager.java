package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.LinkedList;

@SideOnly(Side.CLIENT)
public class CameraFXManager {
    private final LinkedList<CameraShake> SHAKES = new LinkedList<>();

    public void entityViewRenderEventCameraSetup(EntityViewRenderEvent.CameraSetup e) {
        Minecraft mc = Minecraft.getMinecraft();

        if (e.getEntity() == null || mc.player == null || mc.isGamePaused()) {
            return;
        }

        this.addCameraShake(e);
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.isGamePaused()) {
            return;
        }

        this.updateCameraShake();
    }

    private void addCameraShake(EntityViewRenderEvent.CameraSetup e) {
        int shakesCount = SHAKES.size();
        float retRoll = 0;

        for (CameraShake shake : SHAKES) {
            retRoll += shake.getShake((float) e.getRenderPartialTicks());
        }

        retRoll = retRoll / shakesCount;

        e.setRoll(retRoll);
    }

    private void updateCameraShake() {
        Iterator<CameraShake> iterator = SHAKES.iterator();

        while (iterator.hasNext()) {
            CameraShake shake = iterator.next();

            shake.update();

            if (shake.isDone()) {
                iterator.remove();
            }
        }
    }

    public void startShake(int duration, float speed, float amplifier) {
        this.SHAKES.add(new CameraShake(duration, speed, amplifier));
    }

    public void startShake(int duration) {
        this.startShake(duration, 360 * 6, 1.2F);
    }

    private static class CameraShake {
        private final float amplifier;
        private final int duration;
        private final float speed;
        private int prevTick;
        private int tick;

        private CameraShake(int duration, float speed, float amplifier) {
            this.amplifier = amplifier;
            this.duration = duration;
            this.speed = speed;
        }

        private void update() {
            this.prevTick = this.tick;

            this.tick++;
        }

        private boolean isDone() {
            return this.tick >= duration;
        }


        private float getShake(float partialTicks) {
            return MathHelper.cos((float) Math.toRadians(RenderHandler.interpolate((float) prevTick * (speed / 20.0F), (float) tick * (speed / 20.0F), partialTicks))) * amplifier;
        }
    }
}
