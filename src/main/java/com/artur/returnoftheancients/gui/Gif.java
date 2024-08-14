package com.artur.returnoftheancients.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Gif {
    protected boolean isFirstDraw;
    protected int drawIndex;
    protected int time;
    protected int speed;
    protected final ResourceLocation[] gif;

    public Gif(String fileNoIndexName, int gifSize, int speed, Minecraft mc) {
        this.time = 0;
        this.speed = speed;
        this.isFirstDraw = true;
        fileNoIndexName = fileNoIndexName.replaceAll(".png", "");
        gif = new ResourceLocation[gifSize];
        for (int i = 0; i < gifSize; i++) {
            gif[i] = new ResourceLocation(fileNoIndexName + i + ".png");
        }
    }

    protected void onDraw(Minecraft mc) {
        if (isFirstDraw) {
            for (ResourceLocation l : gif) {
                mc.getTextureManager().bindTexture(l);
            }
            isFirstDraw = false;
        }
    }

    public void update() {
        time++;
        if (time >= speed) {
            swapImage();
            time = 0;
        }
    }

    protected void swapImage() {
        drawIndex = ((drawIndex + 1) >= gif.length) ? (0) : (drawIndex + 1);
    }

    public void drawInFullScreen(Minecraft mc, int screenWidth, int screenHeight) {
        onDraw(mc);
        mc.getTextureManager().bindTexture(gif[drawIndex]);
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
    }

    public void drawModalRectWithCustomSizedGif(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight, Minecraft mc) {
        onDraw(mc);
        mc.getTextureManager().bindTexture(gif[drawIndex]);
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public void drawGifModalRect(int x, int y, int textureX, int textureY, int width, int height, float zLevel, Minecraft mc) {
        onDraw(mc);
        mc.getTextureManager().bindTexture(gif[drawIndex]);
        drawTexturedModalRect(x, y, textureX, textureY, width ,height, zLevel);
    }

    private static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }

    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, float zLevel)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
