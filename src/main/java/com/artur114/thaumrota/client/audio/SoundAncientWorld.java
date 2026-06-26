package com.artur114.thaumrota.client.audio;

import com.artur114.thaumrota.common.init.InitDimensions;
import com.artur114.thaumrota.common.init.InitSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundAncientWorld extends PositionedSound implements ITickableSound {
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean stop = false;

    private SoundAncientWorld() {
        super(InitSounds.ANCIENT_WORLD_AMBIENT, SoundCategory.AMBIENT);
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.3F;
        this.attenuationType = AttenuationType.NONE;
    }

    @Override
    public boolean isDonePlaying() {
        return this.mc.player.dimension != InitDimensions.ANCIENT_WORLD_ID || this.stop;
    }

    @Override
    public void update() {
        this.xPosF = (float) this.mc.player.posX;
        this.yPosF = (float) this.mc.player.posY;
        this.zPosF = (float) this.mc.player.posZ;
    }

    private static SoundAncientWorld sound = null;

    public static void play() {
        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        soundHandler.playSound(sound = new SoundAncientWorld());
        if (sound != null) sound.stop = true;
    }
}
