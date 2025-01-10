package com.artur.returnoftheancients.client.gui;

import com.artur.returnoftheancients.client.gui.gif.GifSTime;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.network.ServerPacketTpToHome;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

// TODO: Удалить!
@Deprecated
public class LoadingGui extends GuiScreen {
    private GuiButton button;
    private GifSTime gif;
    private final int Red = 16711680;
    private final int Yellow = 16776960;
    private final int White = 16777215;
    private final int WhiteD = 0xf5f5f5;
    private final int Blue = 0xBFFF;
    private final int Aqua = 0x00ffff;
    private static byte PHASE = -1;
    private static byte percentages = 0;
    private final boolean isTeam;
    private boolean isDrawTeam = false;
    private static String[] players = new String[0];
    private static ResourceLocation location;
    private byte h = 0;
    private byte t = 0;
    private byte iaDrawESCStringTime = 0;
    private byte iaDrawESCStringTime1 = 0;
    private String ts = "";
    private static final String[] constantNamesTranslate = new String[] {"rota.l-gui.generate.stage.0", "rota.l-gui.generate.stage.1", "rota.l-gui.generate.stage.2", "rota.l-gui.generate.stage.3", "rota.l-gui.generate.stage.4"};
    private final String[] names = new String[constantNamesTranslate.length];
    public boolean isDrawESCString = false;
    public boolean iaESCString = false;

    public LoadingGui(boolean isTeam) {
        this.isTeam = isTeam;
        if (!TRAConfigs.ClientSettings.useStaticImageOnLoadingGui) {
            gif = new GifSTime(Referense.MODID + ":textures/gui/gif/gen_gif/gen_gif_v2-", 18, 15, true);
        }
        location = new ResourceLocation( Referense.MODID + ":textures/gui/loading_gui_background.png");
        for (int i = 0; i != constantNamesTranslate.length; i++) {
            names[i] = I18n.format(constantNamesTranslate[i]);
        }
    }


    public static void injectPhase(byte PHASE) {
        LoadingGui.PHASE = PHASE;
    }

    public static void injectPercentages(byte percentages) {
        LoadingGui.percentages = percentages;
    }

    public static void injectPlayers(String[] players) {
        LoadingGui.players = players;
    }

    @Override
    public void initGui() {
        super.initGui();
        if (isTeam) {
            this.button = new GuiButton(0, 0, 0, fontRenderer.getStringWidth(I18n.format("rota.l-gui.button.team")) + 16, 20, "Team");
            this.buttonList.add(this.button);
        } else {
            isDrawTeam = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            isDrawTeam = !isDrawTeam;
        }
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
            isDrawESCString = !isDrawESCString;
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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (!TRAConfigs.ClientSettings.useStaticImageOnLoadingGui) {
            gif.drawInFullScreen(mc, width, height);
        } else {
            mc.getTextureManager().bindTexture(location);
            drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);

        int hei = height / 3;
        switch (PHASE) {
            case 0: {
                String text = names[0];
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawStringWithShadow(text + ts, wid, hei, White);
            }break;
            case 1: {
                String text = names[1] + " " + percentages + "%";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawStringWithShadow(text, wid, hei, White);
            }break;
            case 2: {
                String text = names[2] + " " + percentages + "%";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawStringWithShadow(text, wid, hei, White);
            }break;
            case 3: {
                String text = names[3];
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawStringWithShadow(text + ts, wid, hei, White);
            }break;
            case 4: {
                String text = names[4];
                int wid = ((width / 2) - (fontRenderer.getStringWidth(text) / 2));
                fontRenderer.drawStringWithShadow(text, wid, hei, White);
            }break;
            case -1:{
                String nameDefault = "ERROR";
                int wid = ((width / 2) - (fontRenderer.getStringWidth(nameDefault) / 2));
                fontRenderer.drawStringWithShadow(nameDefault, wid, hei, Red);
            }break;
        }

        if (isDrawESCString) {
            String s = I18n.format(Referense.MODID + ".gui.esc");
            int hieE = (int) (height / 1.25);
            int widE = ((width / 2) - (fontRenderer.getStringWidth(s) / 2));
            fontRenderer.drawStringWithShadow(s, widE, hieE, Red);
        }

        if (isDrawTeam) {
            drawTeamList();
        }
    }
    
    private void drawTeamList() {
        for (byte i = 0; i != players.length ; i++) {
            String text = (i + 1) + ": ";
            int color0 = White;
            if (players[i].isEmpty()) {
                color0 = Red;
            }
            int x = 1;
            int y = 28;
            fontRenderer.drawStringWithShadow(text, x, y + (10 * i), color0);
            int w = fontRenderer.getStringWidth(text);
            fontRenderer.drawStringWithShadow(players[i], x + w, y + (10 * i), Aqua);
        }
    }

    private void resetToDefault() {
        players = new String[0];
        percentages = 0;
        PHASE = -1;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (!iaESCString) {
                iaESCString = true;
            } else {
                iaESCString = false;
                MainR.NETWORK.sendToServer(new ServerPacketTpToHome());
                mc.displayGuiScreen(null);
            }
            return;
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
        isDrawTeam = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
