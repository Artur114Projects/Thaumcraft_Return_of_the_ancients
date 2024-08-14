package com.artur.returnoftheancients.gui;

import net.minecraft.client.Minecraft;

public class TRAGifSTime extends TRAGif {
    protected long lastTime = 0;
    public TRAGifSTime(String fileNoIndexName, int gifSize, int fps, Minecraft mc) {
        super(fileNoIndexName, gifSize, fps, mc);
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
            for (byte i = 0; i < (currentTime - lastTime) / speed; i++) {
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
