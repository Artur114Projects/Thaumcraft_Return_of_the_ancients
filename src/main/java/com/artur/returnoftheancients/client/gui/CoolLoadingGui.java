package com.artur.returnoftheancients.client.gui;

import com.artur.returnoftheancients.ancientworld.system.client.AncientLayer1Client;
import com.artur.returnoftheancients.client.fx.shader.InitShaders;
import com.artur.returnoftheancients.client.gui.buttons.TRAButton;
import com.artur.returnoftheancients.client.gui.gif.GifWithTextureAtlas;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ServerPacketTpToHome;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO: Сделать картинку не изменяемой в зависимости от соотношения сторон
public class CoolLoadingGui extends GuiScreen {
    private GuiButton button;
    protected final ResourceLocation background;
    private final GifWithTextureAtlas gif_2_0 = new GifWithTextureAtlas("loading", 20, 8, 8 * 12, 8, 8);
    protected static final ResourceLocation blur = EnumAssetLocation.TEXTURES_GUI.getPngRL("v.png");
    protected final AncientLayer1Client layer1Client;
    private final int Red = 16711680;
    private final int Yellow = 0xffff40;
    private final int White = 16777215;
    private final int Aqua = 0x00ffff;
    protected ScaledResolution resolution;
    protected Random rand = new Random();
    protected boolean isTpToHome = false;
    protected boolean isDrawTeam = false;
    protected boolean isClosing = false;
    protected float prevOpeningTime = 0;
    protected float prevClosingTime = 0;
    protected boolean isOpening = true;
    protected boolean isDraw = false;
    protected float closingTime = 5;
    protected float openingTime = 0;
    protected int buttonTime = 0;
    protected boolean isTeam;
    protected String lore;


    public CoolLoadingGui(AncientLayer1Client layer1Client) {
        this.layer1Client = layer1Client;
        int id = rand.nextInt(3);
        background = EnumAssetLocation.TEXTURES_GUI.getPngRL("/loading_gui_backgrounds/background" + id);
        int loreId = rand.nextInt(2);
        lore = I18n.format("rota.l-gui.lore." + loreId);
        this.isTeam = layer1Client.playersState().size() > 1;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            button.enabled = false;
            isDrawTeam = !isDrawTeam;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (isDraw) {
            if (button != null) {
                if (!button.enabled) {
                    buttonTime++;
                    if (buttonTime >= 2) {
                        buttonTime = 0;
                        button.enabled = true;
                    }
                }
            }

            prevOpeningTime = openingTime;
            prevClosingTime = closingTime;

            if (isOpening) {
                openingTime += 0.5F;
                if (openingTime >= 10) {
                    isOpening = false;
                }
            }

            if (isClosing) {
                closingTime -= 0.5F;
                if (closingTime <= 0) {
                    mc.displayGuiScreen(null);
                    if (isTpToHome) {
                        MainR.NETWORK.sendToServer(new ServerPacketTpToHome());
                    }
                    isClosing = false;
                }
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        resolution = new ScaledResolution(mc);
        if (isTeam) {
            this.button = new TRAButton(0, 0, 0, fontRenderer.getStringWidth(I18n.format("rota.l-gui.button.team")) + 16, 20, I18n.format("rota.l-gui.button.team"), new ResourceLocation(Referense.MODID + ":textures/gui/tra_button.png"));
            this.buttonList.add(this.button);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackground();
        drawGif();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawLore();

        if (isDrawTeam) {
            drawTeamList();
        }

        if (isOpening) {
            drawDark(1.0F - RenderHandler.interpolate(prevOpeningTime, openingTime, partialTicks) / 10.0F);
        } else if (isClosing) {
            drawDark(1.0F - RenderHandler.interpolate(prevClosingTime, closingTime, partialTicks) / 5.0F);
        }

        isDraw = true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            isTpToHome = true;
            close();
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    public void close() {
        isClosing = true;
    }

    protected void drawLore() {
        int scale = resolution.getScaleFactor();
        int y = (int) (height - ((height / 10) * 1.2F));
        float scaleText = 1.0F;
        float scaleMainText = 1.2F;
        int x = width / 40;

        if (scale == 3) {
            scaleText = 0.6F;
            scaleMainText = 1.0F;
            y = (int) (height - ((height / 6) * 1.0F));
        } else if (scale == 2) {

        } else if (scale == 1) {
            y = (int) (height - ((height / 20) * 1.6F));
            scaleMainText = 1.6F;
            scaleText = 1.2F;
        }

        drawScaledText("\u00A7n" + I18n.format("rota.l-gui.lore.title"), x, y, Yellow, scaleMainText);

        y += (int) (5 * scaleMainText);
        List<String> text = fontRenderer.listFormattedStringToWidth(lore, (int) (((width / 2) - (width / 40) * 2) * (2 - scaleText)));
        for (int i = 0; i != text.size(); i++) {
            drawScaledText(text.get(i), x, y + 10 * (i + 1), White, scaleText);
        }
    }

    protected void drawScaledText(String text, int x, int y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1.0F);
        fontRenderer.drawStringWithShadow(text, 0, 0, color);
        GlStateManager.popMatrix();
    }

    private void drawTeamList() {
        List<String> players = this.layer1Client.playersState();

        for (byte i = 0; i != players.size() ; i++) {
            String text = (i + 1) + ": ";
            int color0 = White;
            int color1 = Aqua;
            int color2 = White;
            String[] split = players.get(i).split("\\|");
            if (players.get(i).isEmpty() || split.length > 1) {
                color0 = Red;
                color1 = Red;
                color2 = Red;
            }
            int x = 1;
            int y = 28;
            String resText = split[0];
            fontRenderer.drawStringWithShadow(text, x, y + (10 * i), color0);
            int w = fontRenderer.getStringWidth(text);
            fontRenderer.drawStringWithShadow(resText, x + w + fontRenderer.getStringWidth(" ") + 1, y + (10 * i), color1);
            fontRenderer.drawStringWithShadow("[",x + w, y + (10 * i), color2);
            fontRenderer.drawStringWithShadow("]",x + w + 2 + fontRenderer.getStringWidth(" " + resText), y + (10 * i), color2);
            if (split.length == 2) {
                fontRenderer.drawStringWithShadow(": " + I18n.format(split[1]), x + w + 2 + fontRenderer.getStringWidth("  " + resText), y + (10 * i), color0);
            }
        }
    }


    protected void drawDark(float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.color(0, 0, 0, alpha);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(0, height, 0).color(0, 0, 0, alpha).endVertex();
        bufferBuilder.pos(width, height, 0).color(0, 0, 0, alpha).endVertex();
        bufferBuilder.pos(width, 0, 0).color(0, 0, 0, alpha).endVertex();
        bufferBuilder.pos(0, 0, 0).color(0, 0, 0, alpha).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
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
        int x = width - width / 30;
        int y = height - height / 10;
        final int baseSize = 8;
        int size = 16;
        if (resolution.getScaleFactor() == 3) {
            size = 10;
            y = height - height / 12;
        } else if (resolution.getScaleFactor() == 1) {
            y = height - height / 20;
        }
        float scale = (float) size / baseSize;

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        gif_2_0.draw(mc, x, y, scale);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    protected void drawOnFullScreen(int screenWidth, int screenHeight) {
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
