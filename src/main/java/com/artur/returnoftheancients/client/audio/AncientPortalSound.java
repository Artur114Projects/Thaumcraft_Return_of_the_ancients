package com.artur.returnoftheancients.client.audio;

import com.artur.returnoftheancients.client.event.managers.PlayerDistanceToPortalManager;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AncientPortalSound extends PositionedSound implements ITickableSound, ISoundTRA /*<- Мой бесполезный интерфейс*/ {

    private final PlayerDistanceToPortalManager manager;
    private boolean donePlaying = false;
    private final float finalVolume;
    private final Minecraft mc;

    public AncientPortalSound(Type type, PlayerDistanceToPortalManager manager, Minecraft mc) {
        super(type == Type.IMPACT ? InitSounds.PORTAL_IMPACT.SOUND : InitSounds.PORTAL_HEARTBEAT.SOUND, SoundCategory.AMBIENT);
        this.finalVolume = type == Type.IMPACT ? 0.5F : 10.0F;
        this.repeatDelay = type == Type.IMPACT ? 0 : 4;
        this.volume = this.finalVolume;
        this.manager = manager;
        this.repeat = true;
        this.mc = mc;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    @Override
    public void update() {
        final int minDistanceToPlaySound = 128;

        if (manager.distanceToPortal < minDistanceToPlaySound) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
            float scale = 1.0F - (manager.distanceToPortal + 16.0F) / minDistanceToPlaySound;
            scale = MathHelper.clamp(scale, 0.0F, 1.0F);

            blockPos.setPos(manager.nearestPortalPos);

            Vec3d vec3d = new Vec3d(blockPos.getX() - mc.player.posX, 0, blockPos.getZ() - mc.player.posZ).normalize();

            blockPos.setPos(mc.player);

            this.setPos(blockPos);
            this.addPos(vec3d.scale(2));

            volume = finalVolume * scale;

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
        }
    }

    @Override
    public boolean isStopped() { // Метод моего интерфейса. Зачем? Я не знаю...
        return donePlaying;
    }

    @Override
    public void stop() { // Метод моего интерфейса.
        this.donePlaying = true;
        this.repeat = false;
    }

    private void setPos(UltraMutableBlockPos pos) {
        this.xPosF = pos.getX() + 0.5F;
        this.yPosF = pos.getY() + 0.5F;
        this.zPosF = pos.getZ() + 0.5F;
    }

    private void addPos(Vec3d vec3d) {
        this.xPosF += (float) vec3d.x;
        this.yPosF += (float) vec3d.y;
        this.zPosF += (float) vec3d.z;
    }

    public enum Type {
        HEARTBEAT, IMPACT
    }
}
