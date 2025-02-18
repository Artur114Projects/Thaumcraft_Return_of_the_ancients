package com.artur.returnoftheancients.client.gui.buttons;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonPage extends GuiButton {

    private final ResourceLocation baseTexture;
    private final ResourceLocation icon;
    private final String hoveredText;
    private final float scale;

    public ButtonPage(int buttonId, int x, int y, float scale, ResourceLocation baseTexture, ResourceLocation icon, String hoveredText) {
        super(buttonId, x, y, (int) (16 * scale), (int) (16 * scale), "");
        this.hoveredText = hoveredText;
        this.baseTexture = baseTexture;
        this.scale = scale;
        this.icon = icon;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            int i = hovered ? 1 : 0;

            if (enabled) {
                mc.getTextureManager().bindTexture(baseTexture);
                RenderHandler.renderQuadTextureAtlas(x, y, 0, 16 * i, 16, 48, scale);
                if (icon != null) {
                    mc.getTextureManager().bindTexture(icon);
                    RenderHandler.renderQuadTextureAtlas(x, y, 0, 0, 16, 16, scale);
                }
            } else {
                mc.getTextureManager().bindTexture(baseTexture);
                RenderHandler.renderQuadTextureAtlas(x, y, 0, 0, 16, 48, scale);
                if (icon != null) {
                    mc.getTextureManager().bindTexture(icon);
                    RenderHandler.renderQuadTextureAtlas(x, y, 0, 0, 16, 16, scale);
                }
                mc.getTextureManager().bindTexture(baseTexture);
                RenderHandler.renderQuadTextureAtlas(x, y, 0, 32, 16, 48, scale);
            }

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    public void renderHoveredText(GuiScreen screen, int mouseX, int mouseY) {
        if (hoveredText == null) {
            return;
        }
        if (hovered) {
            screen.drawHoveringText(hoveredText, mouseX, mouseY);
        }
    }
}
