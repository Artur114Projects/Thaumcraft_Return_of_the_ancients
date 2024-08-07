package com.artur.returnoftheancients.gui;

import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.network.ServerPacketTpToHome;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;


public class LoadingGui extends GuiScreen {
    private static byte PHASE = -1;
    private static byte percentages = 0;
    private final int Red = 16711680;
    private final int White = 16777215;
    private static final int Y = 100;
    private static final int X = 427;
    private static ResourceLocation location;
    private byte h = 0;
    private byte t = 0;
    private byte iaDrawESCStringTime = 0;
    private byte iaDrawESCStringTime1 = 0;
//    private int xd = width + 40;
    private String ts = "";
    private final String[] constantNames = new String[] {"generating structures map", "cleaning area", "place structures in world", "reload light", "Finish!"};
    public boolean iaDrawESCString = false;
    public boolean iaESCString = false;


    public LoadingGui() {
        location = new ResourceLocation( Referense.MODID + ":textures/gui/loading_gui_background.png");
    }

    public static void injectPhase(byte PHASE) {
        LoadingGui.PHASE = PHASE;
    }

    public static void injectPercentages(byte percentages) {
        LoadingGui.percentages = percentages;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateScreen() {
        if (iaESCString) {
            iaDrawESCStringTime++;
            iaDrawESCStringTime1++;
        }
        if (iaDrawESCStringTime == 80) {
            iaESCString = false;
            iaDrawESCStringTime = 0;
        }
        if (iaDrawESCStringTime1 == 10) {
            iaDrawESCStringTime1 = 0;
            iaDrawESCString = !iaDrawESCString;
        }
        if (h == 20) {
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
        h++;
        super.updateScreen();
    }

    private void drawTexture() {
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(location);

        // Получаем текущие размеры экрана
        int screenWidth = width;
        int screenHeight = height;

        // Рисуем текстуру с фиксированными размерами
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glColor4f(1, 1, 1, 1);
        drawTexture();

        int hei = height / 3;
        switch (PHASE) {
            case 0: {
                String text = constantNames[0] + ts;
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }break;
            case 1: {
                String text = constantNames[1] + " " + percentages + "%";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawString(text, wid, hei, White);
            }break;
            case 2: {
                String text = constantNames[2] + " " + percentages + "%";
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
            }break;
            case -1:{
                String nameDefault = "ERROR";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(nameDefault) / 2));
                fontRenderer.drawString(nameDefault, wid, hei, Red);
            }break;
        }

        if (iaDrawESCString) {
            String s = new TextComponentTranslation(Referense.MODID + ".gui.esc").getFormattedText();
            int hieE = (int) (height / 1.25);
            int widE = ((width / 2) - (fontRenderer.getStringWidth(s) / 2));
            fontRenderer.drawString(s, widE, hieE, Red);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
       if (!TRAConfigs.Any.debugMode) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                if (!iaESCString) {
                    iaESCString = true;
                } else {
                    iaESCString = false;
                    MainR.NETWORK.sendToServer(new ServerPacketTpToHome());
                }
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
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
