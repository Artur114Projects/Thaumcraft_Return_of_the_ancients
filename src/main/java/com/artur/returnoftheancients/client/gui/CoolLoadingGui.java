package com.artur.returnoftheancients.client.gui;

import com.artur.returnoftheancients.client.gui.buttons.TRAButton;
import com.artur.returnoftheancients.client.gui.gif.Gif;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ServerPacketTpToHome;
import com.artur.returnoftheancients.referense.Referense;
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
import java.util.List;
import java.util.Random;

// TODO: Сделать картинку не изменяемой в зависимости от соотношения сторон
public class CoolLoadingGui extends GuiScreen {
    public static CoolLoadingGui instance;
    private GuiButton button;

    protected final ResourceLocation background;
    private Gif gif = new Gif(Referense.MODID + ":textures/gui/gif/loading/loading", 12, 1, false);
    protected static final ResourceLocation blur = new ResourceLocation(Referense.MODID + ":textures/gui/v.png");
    private final int Red = 16711680;
    private final int Yellow = 0xffff40;
    private final int White = 16777215;
    private final int Aqua = 0x00ffff;
    private String[] players = new String[0];

    protected ScaledResolution resolution;

    protected Random rand = new Random();
    protected boolean isTpToHome = false;
    protected boolean isDrawTeam = false;
    protected boolean isClosing = false;
    protected boolean isOpening = true;
    protected boolean isDraw = false;
    protected float closingTime = 10;
    protected float openingTime = 0;
    protected int buttonTime = 0;
    protected boolean isTeam;
    protected String lore;


    public CoolLoadingGui(boolean isTeam) {
        int id = rand.nextInt(3);
        background = new ResourceLocation(Referense.MODID + ":textures/gui/loading_gui_backgrounds/background" + id + ".png");
        int loreId = rand.nextInt(2);
        lore = I18n.format("rota.l-gui.lore." + loreId);
        this.isTeam = isTeam;
        instance = this;
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

            if (rand.nextInt(11) != 0) {
                gif.update();
            }

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
            drawDark(1.0F - openingTime / 10.0F);
        } else if (isClosing) {
            drawDark(1.0F - closingTime / 10.0F);
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

    public void updatePlayersList(String[] players) {
        this.players = players;
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
            fontRenderer.drawStringWithShadow(players[i], x + w + fontRenderer.getStringWidth(" ") + 1, y + (10 * i), Aqua);
            fontRenderer.drawStringWithShadow("[",x + w, y + (10 * i), White);
            fontRenderer.drawStringWithShadow("]",x + w + 2 + fontRenderer.getStringWidth(" " + players[i]), y + (10 * i), White);
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
        int x = width - width / 60;
        int y = height - height / 12;
        int size = 16;
        if (resolution.getScaleFactor() == 3) {
            size = 12;
            y = height - height / 14;
        } else if (resolution.getScaleFactor() == 1) {
            y = height - height / 20;
        }
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        gif.bindGifTexture(mc);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x - size, y, 0).tex(0, 1).endVertex();
        bufferBuilder.pos(x, y, 0).tex(1, 1).endVertex();
        bufferBuilder.pos(x, y - size, 0).tex(1, 0).endVertex();
        bufferBuilder.pos(x - size, y - size, 0).tex(0, 0).endVertex();
        tessellator.draw();

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