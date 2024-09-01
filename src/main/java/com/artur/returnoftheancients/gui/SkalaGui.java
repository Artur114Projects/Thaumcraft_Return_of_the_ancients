package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SkalaGui extends GuiScreen {

    private static GifSTime gif;
    private static ResourceLocation location;

    public SkalaGui() {
        gif = new GifSTime( Referense.MODID + ":textures/gui/gif/gen1v/gen_gif_v1-", 65, 20, mc);
        location = new ResourceLocation( Referense.MODID + ":textures/gui/skala.png");
    }
    float[] tc = new float[] {0,0, 1,0, 1,1, 0,1};
    float[] vertex = new float[] {-1,-1,0, 1,-1,0, 1,1,0, -1,1,0};

    public void drawScreenq(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(location);
        GlStateManager.popMatrix();
        GlStateManager.loadIdentity();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferBuilder.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, 0).endVertex();
        bufferBuilder.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, 0).endVertex();
        bufferBuilder.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, 0).endVertex();
        bufferBuilder.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, 0).endVertex();
        tessellator.draw();
        GlStateManager.pushMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void skala() {
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(location);

        // Получаем текущие размеры экрана
        int screenWidth = width;
        int screenHeight = height;

        // Рисуем текстуру с фиксированными размерами
//        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
        gif.drawModalRectWithCustomSizedGif(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight, mc);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {

    }

    private void quads() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(50, 50, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.pos(150, 50, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.pos(150, 150, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.pos(50, 150, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.draw();
    }


    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, double width, double height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x), (double)(y), (double)this.zLevel).tex((double)((float)(textureX) * 0.00390625F), (double)((float)(textureY) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
