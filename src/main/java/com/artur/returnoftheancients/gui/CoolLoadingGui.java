package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBow;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.util.Random;

public class CoolLoadingGui extends GuiScreen {
    private GuiButton button;

    protected final ResourceLocation background;
    private Gif gif = new GifSTime(Referense.MODID + ":textures/gui/gif/loading/loading", 12, 20, false);
    protected static final ResourceLocation blur = new ResourceLocation(Referense.MODID + ":textures/gui/v.png");

    public CoolLoadingGui() {
        int id = new Random().nextInt(3);
        background = new ResourceLocation(Referense.MODID + ":textures/gui/loading_gui_backgrounds/background" + id + ".png");
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        super.initGui();
//        this.button = new TRAButton(0, 0, 0, fontRenderer.getStringWidth(I18n.format("rota.l-gui.button.team")) + 16, 20, "Team");
//        this.buttonList.add(this.button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawDefaultBackground();
        drawBackground();
        drawGif();
    }

    protected void drawBackground() {
        mc.getTextureManager().bindTexture(background);
        drawOnFullScreen(width, height);

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(blur);
        drawOnFullScreen(width, height);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    protected void drawGif() {
        int x = width - height / 100;
        int y = height - height / 10;
        int size = 16;
        gif.bindGifTexture(mc);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x - size, y, 0).tex(0, 1).endVertex();
        bufferBuilder.pos(x, y, 0).tex(1, 1).endVertex();
        bufferBuilder.pos(x, y - size, 0).tex(1, 0).endVertex();
        bufferBuilder.pos(x - size, y - size, 0).tex(0, 0).endVertex();
        tessellator.draw();
    }

    protected void drawOnFullScreen(int screenWidth, int screenHeight) {
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
    }

}
