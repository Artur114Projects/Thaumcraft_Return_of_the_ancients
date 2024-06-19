package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SkalaGui extends GuiScreen {

    private static ResourceLocation location;

    public SkalaGui() {
        location = new ResourceLocation( Referense.MODID + ":textures/gui/skala.png");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(location);
        this.drawDefaultBackground();

        ScaledResolution resolution = new ScaledResolution(mc);
        drawTexturedModalRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaledWidth_double(), resolution.getScaledHeight_double());

        super.drawScreen(mouseX, mouseY, partialTicks);
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
