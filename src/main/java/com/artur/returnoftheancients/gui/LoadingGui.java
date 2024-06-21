package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator.PHASE;

public class LoadingGui extends GuiScreen {
    private final int Green = 32768;
    private final int Red = 16711680;
    private final int White = 16777215;
    private int MainStringColor = White;
    private static final int Y = 100;
    private static final int X = 427;
    private static ResourceLocation location;
    private int s = 0;
    private byte h = 0;
    private byte t = 0;
//    private int xd = width + 40;
    private String ts = "";
    private final String[] constantNames = new String[] {"generating structures map", "cleaning area", "place structures in world", "reload light", "Finish!"};
    private String name = "gui error";
    private final int[] colors = new int[7];


    public LoadingGui() {
        location = new ResourceLocation( Referense.MODID + ":textures/gui/boba.png");
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateScreen() {
        if (h == 20) {
            if (!AncientLabyrinthGenerator.isGen)
                s++;
            h = 0;
        }
        if (h % 10 == 0) {
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
        if (AncientLabyrinthGenerator.isGen) {
            MainStringColor = Green;
        }
        h++;
//        if (xd <= -40) {
//            xd = width + 40;
//        }
//        xd -= 4;
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(location);
        this.drawDefaultBackground();
        drawTexturedModalRect(0, 0, 0, 0, width, height);

//        fontRenderer.drawString("Generating ancient world (" + s + "s)", (width / 2) - (fontRenderer.getStringWidth("Generating ancient world (" + s + "s)") / 2), height / 5, MainStringColor);

        int hei = height / 2 + 4;
        switch (PHASE) {
            case 0: {
                String text = constantNames[0] + ts;
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }break;
            case 1: {
                String text = constantNames[1] + " " + Math.round(AncientLabyrinthGenerator.getPercentages()) + "%";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }break;
            case 2: {
                String text = constantNames[2] + " " + Math.round(AncientLabyrinthGenerator.getPercentages()) + "%";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }break;
            case 3: {
                String text = constantNames[3] + ts;
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }break;
            case 4: {
                String text = constantNames[4];
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }
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
        return false;
    }
}
