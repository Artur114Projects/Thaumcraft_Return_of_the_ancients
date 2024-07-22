package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class SkalaGui extends GuiScreen {

    private static final int textureSize = 1280;
    private static ResourceLocation location;

    public SkalaGui() {
        location = new ResourceLocation( Referense.MODID + ":textures/gui/skala.png");
    }
    float[] tc = new float[] {0,0, 1,0, 1,1, 0,1};
    float[] vertex = new float[] {-1,-1,0, 1,-1,0, 1,1,0, -1,1,0};
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glPushMatrix();
        mc.getTextureManager().bindTexture(location);
        GL11.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL_VERTEX_ARRAY);

        GL11.glVertexPointer(3, GL_FLOAT, FloatBuffer.wrap(vertex));
        GL11.glTexCoordPointer(2, GL_FLOAT, FloatBuffer.wrap(tc));
        GL11.glDrawArrays(GL_TRIANGLE_FAN, 0, 4);

//        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
//        bufferbuilder.pos(0, 0, 0).tex(0, 0).endVertex();
//        bufferbuilder.pos(1, 0, 0).tex(0, 0).endVertex();
//        bufferbuilder.pos(1, 1, 0).tex(0, 0).endVertex();
//        bufferbuilder.pos(0, 1, 0).tex(0, 0).endVertex();
//        tessellator.draw();
        GL11.glDisableClientState(GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        GL11.glPopMatrix();
        this.drawDefaultBackground();

//        drawTexturedModalRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaledWidth_double(), resolution.getScaledHeight_double());

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
