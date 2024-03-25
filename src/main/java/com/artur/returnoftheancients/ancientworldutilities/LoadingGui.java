package com.artur.returnoftheancients.ancientworldutilities;

import com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class LoadingGui extends GuiScreen {
    private final int Green = 32768;
    private final int White = 16777215;
    private int MainStringColor = White;
    private static final int Y = 100;
    private static final int X = 427;
    private static ResourceLocation location;
    private int s = 0;
    private byte i = 0;
    private byte t = 0;
    private String ts = "";
    private final String[] constantNames = new String[] {"Generating random structures", "Generating right left way", "Generating down up way",
            "Generating ancient entry way", "Generating clear area", "Generate structures in world", "Reload light"};
    private final String[] names = new String[] {"Generating random structures", "Generating right left way", "Generating down up way",
            "Generating ancient entry way", "Generating clear area", "Generate structures in world", "Reload light"};
    private final int[] colors = new int[7];


    public LoadingGui() {
        location = new ResourceLocation( Referense.MODID + ":textures/gui/boba.png");
    }

    private String formatString(String s) {
        return s.replaceAll("NL", "\n\n");
    }

    @Override
    public void initGui() {
        for (byte i = 0; i != colors.length; i++) {
            colors[i] = White;
        }
        super.initGui();
    }

    @Override
    public void updateScreen() {
        if (i == 20) {
            if (!AncientLabyrinthGenerator.isGen)
                s++;
            i = 0;
        }
        if (i == 10) {
            t++;
        }
        switch (t) {
            case 0:
                ts = "";
                break;
            case 1:
                ts = ".";
                break;
            case 2:
                ts = "..";
                break;
            case 3:
                ts = "...";
                break;
            case 4:
                t = 0;
                break;
        }
        if (AncientLabyrinthGenerator.PHASE > -1) {
            if (AncientLabyrinthGenerator.PHASE == 4 || AncientLabyrinthGenerator.PHASE == 5) {
                names[AncientLabyrinthGenerator.PHASE] = constantNames[AncientLabyrinthGenerator.PHASE] + ts + " " + AncientLabyrinthGenerator.getPercentages() + "%";
            } else if (AncientLabyrinthGenerator.PHASE < 7){
                names[AncientLabyrinthGenerator.PHASE] = constantNames[AncientLabyrinthGenerator.PHASE] + ts;
            }
        }
        if (AncientLabyrinthGenerator.isGen) {
            MainStringColor = Green;
        }
        for (byte i = 0; i != colors.length; i++) {
            if (AncientLabyrinthGenerator.PHASE > i) {
                colors[i] = Green;
                names[i] = constantNames[i];
            }
        }
        i++;
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(location);
        this.drawDefaultBackground();
        drawTexturedModalRect(0, 0, 0, 0, width, height);
        fontRenderer.drawString("Generating ancient world (" + s + "s)", width / 2 - 80, height / 2 - 60, MainStringColor);
        fontRenderer.drawString(names[0], width / 2 - 80, height / 2 - 40, colors[0]);
        fontRenderer.drawString(names[1], width / 2 - 80, height / 2 - 30, colors[1]);
        fontRenderer.drawString(names[2], width / 2 - 80, height / 2 - 20, colors[2]);
        fontRenderer.drawString(names[3], width / 2 - 80, height / 2 - 10, colors[3]);
        fontRenderer.drawString(names[4], width / 2 - 80, height / 2, colors[4]);
        fontRenderer.drawString(names[5], width / 2 - 80, height / 2 + 10, colors[5]);
        fontRenderer.drawString(names[6], width / 2 - 80, height / 2 + 20, colors[6]);
        if (AncientLabyrinthGenerator.isGen) {
            fontRenderer.drawString("Finish!", width / 2 - 80, height / 2 + 60, White);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != Keyboard.KEY_ESCAPE)
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
