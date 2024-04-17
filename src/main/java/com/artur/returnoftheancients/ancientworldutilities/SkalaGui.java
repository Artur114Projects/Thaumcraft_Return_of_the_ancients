package com.artur.returnoftheancients.ancientworldutilities;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
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
        mc.getTextureManager().bindTexture(location); // текстура 1280 на 720
        this.drawDefaultBackground();
        drawTexturedModalRect(width / 2, height / 2, 1280, 1280, width, height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
