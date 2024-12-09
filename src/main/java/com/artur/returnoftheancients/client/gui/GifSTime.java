package com.artur.returnoftheancients.client.gui;

import net.minecraft.client.Minecraft;

public class GifSTime extends Gif {
    protected long lastTime = 0;
    public GifSTime(String fileNoIndexName, int gifSize, int fps, boolean isStartIn0) {
        super(fileNoIndexName, gifSize, fps, isStartIn0);
        this.speed = (1000 / fps);
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = currentTime;
            return;
        }
        if ((currentTime - lastTime) >= speed) {
            for (byte i = 0; i < Math.round((double) (currentTime - lastTime) / speed); i++) {
                swapImage();
            }
            lastTime = currentTime;
        }
    }

    @Override
    protected void onDraw(Minecraft mc) {
        super.onDraw(mc);
        update();
    }
}
