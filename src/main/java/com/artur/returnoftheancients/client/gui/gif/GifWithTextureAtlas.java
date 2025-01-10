package com.artur.returnoftheancients.client.gui.gif;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.utils.EnumTextureLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GifWithTextureAtlas {

    private final ResourceLocation texturePath;
    private final float drawAreaHeight;
    private final float drawAreaWidth;
    private final float textureSizeX;
    private final float textureSizeY;
    private final int framesCount;
    private final int speed;


    private int currentFrame = 0;
    private long lastTime = 0;


    public GifWithTextureAtlas(String fileName, int fps, float textureSizeX, float textureSizeY, float drawAreaWidth, float drawAreaHeight) {
        this.texturePath = EnumTextureLocation.GUI_GIF_PATH.getRL(fileName);
        this.framesCount = (int) Math.ceil(textureSizeY / drawAreaHeight);
        this.drawAreaHeight = drawAreaHeight;
        this.drawAreaWidth = drawAreaWidth;
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
        this.speed = (1000 / fps);
    }

    private void update() {
        long currentTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = currentTime;
            return;
        }
        if ((currentTime - lastTime) >= speed) {
            for (byte i = 0; i < Math.round((double) (currentTime - lastTime) / speed); i++) {
                if (currentFrame + 1 > framesCount) {
                    currentFrame = 0;
                    continue;
                }
                currentFrame++;
            }
            lastTime = currentTime;
        }
    }


    public void draw(Minecraft mc, int posX, int posY) {
        draw(mc, posX, posY, 1);
    }

    public void draw(Minecraft mc, int posX, int posY, float scale) {
        update();

        mc.getTextureManager().bindTexture(texturePath);
        HandlerR.renderTextureAtlas(posX, posY, 0, drawAreaHeight * currentFrame, textureSizeX, textureSizeY, drawAreaWidth, drawAreaHeight, scale);
    }
}
