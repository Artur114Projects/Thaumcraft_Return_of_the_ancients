package com.artur.returnoftheancients.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderHandler {
    @SideOnly(Side.CLIENT)
    public static float interpolate(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @SideOnly(Side.CLIENT)
    public static double interpolate(double start, double end, float pct) {
        return start + (end - start) * pct;
    }

    @SideOnly(Side.CLIENT)
    private void renderBox(Tessellator tessellator, BufferBuilder bufferBuilder, double x, double y, double z, double x1, double y1, double z1, int a, int b, int c) {
        GlStateManager.glLineWidth(2.0F);
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(x, y, z).color((float) b, (float) b, (float) b, 0.0F).endVertex();
        bufferBuilder.pos(x, y, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z).color(b, c, c, a).endVertex();
        bufferBuilder.pos(x1, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y, z).color(c, c, b, a).endVertex();
        bufferBuilder.pos(x, y1, z).color(c, b, c, a).endVertex();
        bufferBuilder.pos(x1, y1, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y1, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z).color((float) b, (float) b, (float) b, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.glLineWidth(1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static void renderPrimitive(int x, int x1, int y, int y1, double startU, double endU, double startV, double endV) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y1, 0).tex(startU, endV).endVertex();
        builder.pos(x1, y1, 0).tex(endU, endV).endVertex();
        builder.pos(x1, y, 0).tex(endU, startV).endVertex();
        builder.pos(x, y, 0).tex(startU, startV).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float drawAreaWidth, float drawAreaHeight, float scale) {
        startDrawX = (scale * startDrawX);
        startDrawY = (scale * startDrawY);
        textureSizeX = (scale * textureSizeX);
        textureSizeY = (scale * textureSizeY);
        drawAreaWidth = (scale * drawAreaWidth);
        drawAreaHeight = (scale * drawAreaHeight);

        float posX1 = posX + drawAreaWidth;
        float posY1 = posY + drawAreaHeight;

        float iX = startDrawX / drawAreaWidth;
        float iY = startDrawY / drawAreaHeight;

        double endU = (double) (drawAreaWidth * (iX + 1)) / textureSizeX;
        double startU = (double) (drawAreaWidth * iX) / textureSizeX;

        double endV = (double) (drawAreaHeight * (iY + 1)) / textureSizeY;
        double startV = (double) (drawAreaHeight * iY) / textureSizeY;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX, posY1, 0).tex(startU, endV).endVertex();
        builder.pos(posX1, posY1, 0).tex(endU, endV).endVertex();
        builder.pos(posX1, posY, 0).tex(endU, startV).endVertex();
        builder.pos(posX, posY, 0).tex(startU, startV).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float drawAreaWidth, float drawAreaHeight) {
        renderTextureAtlas(posX, posY, startDrawX, startDrawY, textureSizeX, textureSizeY, drawAreaWidth, drawAreaHeight, 1);
    }

    /**
     * @param posX drawing start position
     * @param posY drawing start position
     * @param startDrawX drawing start position in texture
     * @param startDrawY drawing start position in texture
     * @param textureSizeX texture size
     * @param textureSizeY texture size
     * @param scale scale
     */
    @SideOnly(Side.CLIENT)
    public static void renderQuadTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float drawQuadSize, float scale) {
        startDrawX = (scale * startDrawX);
        startDrawY = (scale * startDrawY);
        textureSizeX = (scale * textureSizeX);
        textureSizeY = (scale * textureSizeY);

        float posX1 = posX + drawQuadSize;
        float posY1 = posY + drawQuadSize;

        float iX = startDrawX / drawQuadSize;
        float iY = startDrawY / drawQuadSize;

        double endU = (double) (drawQuadSize * (iX + 1)) / textureSizeX;
        double startU = (double) (drawQuadSize * iX) / textureSizeX;

        double endV = (double) (drawQuadSize * (iY + 1)) / textureSizeY;
        double startV = (double) (drawQuadSize * iY) / textureSizeY;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX, posY1, 0).tex(startU, endV).endVertex();
        builder.pos(posX1, posY1, 0).tex(endU, endV).endVertex();
        builder.pos(posX1, posY, 0).tex(endU, startV).endVertex();
        builder.pos(posX, posY, 0).tex(startU, startV).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderQuadTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float scale) {
        renderQuadTextureAtlas(posX, posY, startDrawX, startDrawY, textureSizeX, textureSizeY, Math.min(textureSizeX, textureSizeY), scale);
    }

    @SideOnly(Side.CLIENT)
    public static void renderQuadTextureAtlas(int posX, int posY, int startDrawX, int startDrawY, int textureSizeX, int textureSizeY) {
        renderQuadTextureAtlas(posX, posY, startDrawX, startDrawY, textureSizeX, textureSizeY, 1);
    }

    @SideOnly(Side.CLIENT)
    public static Tuple<Integer, Integer> getTextureSize(ResourceLocation textureRL) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(textureRL);
        int x = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int y = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        Tuple<Integer, Integer> size = new Tuple<>(x, y);
        GlStateManager.popMatrix();
        return size;
    }
}
