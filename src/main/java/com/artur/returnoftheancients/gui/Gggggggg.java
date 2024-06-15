package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Gggggggg extends GuiScreen {

    private static ResourceLocation location;

    public Gggggggg() {
        location = new ResourceLocation( Referense.MODID + ":textures/gui/call.png");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int j = this.width / 2;
        this.mc.getTextureManager().bindTexture(location);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(j + 0, 30, 0, 0, 99, 44);
        this.drawTexturedModalRect(j + 99, 30, 129, 0, 27, 44);
        this.drawTexturedModalRect(j + 99 + 26, 30, 126, 0, 3, 44);
        this.drawTexturedModalRect(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
        this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
