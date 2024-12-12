package com.artur.returnoftheancients.client.gui;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ButtonAncientTeleportMain extends GuiButton {

    private final ResourceLocation textureButton = new ResourceLocation(Referense.MODID, "textures/gui/container/ancient_teleport_main_button_icon.png");
    private boolean press = true;
    private int buttonTime = 0;


    public ButtonAncientTeleportMain(int buttonId, int x, int y) {
        super(buttonId, x, y, 64, 64, "");
    }

    public ButtonAncientTeleportMain(int buttonId, int x, int y, int widthIn, int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(textureButton);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            int x1 = x + width;
            int y1 = y + height;

            double endV;
            double startV;

            double u = 1.0D;
            double u1 = 0.0D;

            int i = 0;

            if (!this.press) {
                i = 2;
            } else if (hovered) {
                i = 1;
            }

            startV = (32 * i) / 96.0D;
            endV = (32 * (i + 1)) / 96.0D;

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            builder.pos(x, y1, 0).tex(u1, endV).endVertex();
            builder.pos(x1, y1, 0).tex(u, endV).endVertex();
            builder.pos(x1, y, 0).tex(u, startV).endVertex();
            builder.pos(x, y, 0).tex(u1, startV).endVertex();
            tessellator.draw();

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    public void press() {
        press = false;
    }

    public void update() {
        if (!press) {
            buttonTime++;
            if (buttonTime >= 2) {
                buttonTime = 0;
                press = true;
            }
        }
    }
}
