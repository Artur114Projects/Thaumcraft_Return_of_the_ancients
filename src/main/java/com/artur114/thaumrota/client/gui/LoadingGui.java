package com.artur114.thaumrota.client.gui;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.thaumrota.common.init.InitBlocks;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.client.AncientLayer1Client;
import com.artur114.thaumrota.client.gui.buttons.RotAButton;
import com.artur114.thaumrota.main.ThaumRotA;
import com.artur114.thaumrota.common.network.ServerPacketInterruptBuild;
import com.artur114.thaumrota.common.util.EnumAsset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.blocks.BlocksTC;

import java.io.IOException;
import java.util.List;
import java.util.Random;

// TODO: Сделать картинку не изменяемой в зависимости от соотношения сторон
public class LoadingGui extends GuiScreen {
    private static final ItemStack[] LOADING_STACKS = new ItemStack[] {
            new ItemStack(BlocksTC.stoneAncient),
            new ItemStack(BlocksTC.stoneAncient),
            new ItemStack(BlocksTC.stoneAncient),
            new ItemStack(BlocksTC.stoneAncient),
            new ItemStack(BlocksTC.stoneEldritchTile),
            new ItemStack(InitBlocks.ANCIENT_LAMP_0),
            new ItemStack(InitBlocks.ANCIENT_LAMP_1),
            new ItemStack(InitBlocks.ANCIENT_LAMP_1),
            new ItemStack(InitBlocks.ANCIENT_LAMP_1),
            new ItemStack(InitBlocks.PEDESTAL_ACTIVE)
    };
    private static final ResourceLocation BLUR_TEXTURE = EnumAsset.TEXTURES_GUI.png("blur.png");
    private static final int BACKGROUNDS_COUNT = 7;
    private static final int LORE_COUNT = 4;
    private static final int YELLOW = 0xffff40;
    private static final int WHITE = 16777215;
    private static final int AQUA = 0x00ffff;
    private static final int RED = 16711680;

    private final AncientLayer1Client layer1Client;
    private final ResourceLocation background;
    private final EntityItem loadingItem;
    private final boolean isTeam;
    private final String lore;
    private ScaledResolution resolution;
    private boolean isInterrupt = false;
    private boolean isDrawTeam = false;
    private boolean isClosing = false;
    private float prevOpeningTime = 0;
    private float prevClosingTime = 0;
    private boolean isOpening = true;
    private boolean isDraw = false;
    private float closingTime = 5;
    private float openingTime = 0;
    private int buttonTime = 0;
    private GuiButton button;


    public LoadingGui(AncientLayer1Client layer1Client) {
        Random rand = new Random();
        this.layer1Client = layer1Client;
        this.loadingItem = new EntityItem(Minecraft.getMinecraft().world, 0.0, 0.0, 0.0, LOADING_STACKS[rand.nextInt(LOADING_STACKS.length)].copy());
        this.background = EnumAsset.TEXTURES_GUI.png("/lgbacks/background" + rand.nextInt(BACKGROUNDS_COUNT));
        this.lore = I18n.format("rota.l-gui.lore." + rand.nextInt(LORE_COUNT));
        this.isTeam = layer1Client.playersState().size() > 1;

        this.loadingItem.hoverStart = 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            button.enabled = false;
            this.isDrawTeam = !this.isDrawTeam;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.isDraw) {
            if (this.button != null) {
                if (!this.button.enabled) {
                    this.buttonTime++;
                    if (this.buttonTime >= 2) {
                        this.buttonTime = 0;
                        this.button.enabled = true;
                    }
                }
            }

            this.prevOpeningTime = this.openingTime;
            this.prevClosingTime = this.closingTime;

            if (this.isOpening) {
                this.openingTime += 0.5F;
                if (this.openingTime >= 10) {
                    this.isOpening = false;
                }
            }

            if (this.isClosing) {
                this.closingTime -= 0.5F;
                if (this.closingTime <= 0) {
                    this.mc.displayGuiScreen(null);
                    if (this.isInterrupt) {
                        ThaumRotA.NETWORK.sendToServer(new ServerPacketInterruptBuild());
                    }
                    this.isClosing = false;
                }
            }
        }
    }

    @Override
    public void initGui() {
        if (this.isTeam) {
            this.button = new RotAButton(0, 0, 0, this.fontRenderer.getStringWidth(I18n.format("rota.l-gui.button.team")) + 16, 20, I18n.format("rota.l-gui.button.team"), EnumAsset.TEXTURES_GUI_BUTTON.png("ancient_button"));
            this.buttonList.add(this.button);
        }
        this.resolution = new ScaledResolution(this.mc);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawBackground();
        this.drawLoading();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawLore();

        if (this.isDrawTeam) {
            this.drawTeamList();
        }

        if (this.isOpening) {
            this.drawDark(1.0F - BananaMath.lerp(this.prevOpeningTime, this.openingTime, partialTicks) / 10.0F);
        } else if (this.isClosing) {
            this.drawDark(1.0F - BananaMath.lerp(this.prevClosingTime, this.closingTime, partialTicks) / 5.0F);
        }

        this.isDraw = true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.isInterrupt = true;
            this.close();
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    public void close() {
        this.isClosing = true;
    }

    private void drawLore() {
        int y = (int) (this.height - (((float) this.height / 10) * 1.2F));
        float scaleText = 1.0F;
        float scaleMainText = 1.2F;
        int x = this.width / 40;

        switch (this.resolution.getScaleFactor()) {
            case 3:
                y = (int) (this.height - (((float) this.height / 6) * 1.0F));
                scaleMainText = 1.0F;
                scaleText = 0.6F;
                break;
            case 1:
                y = (int) (this.height - (((float) this.height / 20) * 1.6F));
                scaleMainText = 1.6F;
                scaleText = 1.2F;
                break;
        }

        this.drawScaledText(TextFormatting.UNDERLINE + I18n.format("rota.l-gui.lore.title"), x, y, YELLOW, scaleMainText);

        y += (int) (5 * scaleMainText);
        List<String> text = this.fontRenderer.listFormattedStringToWidth(this.lore, (int) ((((float) this.width / 2) - ((float) this.width / 40) * 2) * (2 - scaleText)));
        for (int i = 0; i != text.size(); i++) {
            this.drawScaledText(text.get(i), x, y + 10 * (i + 1), WHITE, scaleText);
        }
    }

    private void drawScaledText(String text, int x, int y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1.0F);
        this.fontRenderer.drawStringWithShadow(text, 0, 0, color);
        GlStateManager.popMatrix();
    }

    private void drawTeamList() {
        List<String> players = this.layer1Client.playersState();

        for (byte i = 0; i != players.size() ; i++) {
            String text = (i + 1) + ": ";
            int color0 = WHITE;
            int color1 = AQUA;
            int color2 = WHITE;
            String[] split = players.get(i).split("\\|");
            if (players.get(i).isEmpty() || split.length > 1) {
                color0 = RED;
                color1 = RED;
                color2 = RED;
            }
            int x = 1;
            int y = 28;
            String resText = split[0];
            this.fontRenderer.drawStringWithShadow(text, x, y + (10 * i), color0);
            int w = this.fontRenderer.getStringWidth(text);
            this.fontRenderer.drawStringWithShadow(resText, x + w + this.fontRenderer.getStringWidth(" ") + 1, y + (10 * i), color1);
            this.fontRenderer.drawStringWithShadow("[",x + w, y + (10 * i), color2);
            this.fontRenderer.drawStringWithShadow("]",x + w + 2 + this.fontRenderer.getStringWidth(" " + resText), y + (10 * i), color2);
            if (split.length == 2) {
                this.fontRenderer.drawStringWithShadow(": " + I18n.format(split[1]), x + w + 2 + this.fontRenderer.getStringWidth("  " + resText), y + (10 * i), color0);
            }
        }
    }


    private void drawDark(float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.color(0, 0, 0, alpha);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(0, this.height, 100).color(0, 0, 0, alpha).endVertex();
        bufferBuilder.pos(this.width, this.height, 100).color(0, 0, 0, alpha).endVertex();
        bufferBuilder.pos(this.width, 0, 100).color(0, 0, 0, alpha).endVertex();
        bufferBuilder.pos(0, 0, 100).color(0, 0, 0, alpha).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    private void drawBackground() {
        this.mc.getTextureManager().bindTexture(this.background);
        this.drawOnFullScreen(this.width, this.height);

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(BLUR_TEXTURE);
        this.drawOnFullScreen(this.width, this.height);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }


    private void drawLoading() {
        int x = this.width - this.width / 30;
        int y = this.height - this.height / 32;
        int scale = 50;
        switch (this.resolution.getScaleFactor()) {
            case 3:
                scale = 30; y = this.height - this.height / 12;
                break;
            case 1:
                y = this.height - this.height / 20;
                break;
        }

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50);
        GlStateManager.rotate(-22.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-360.0F * ((System.currentTimeMillis() % 2000L) / 2000.0F), 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-x, -y, -50);
        this.drawEntityItem(x, y, scale, this.loadingItem);
        GlStateManager.popMatrix();

        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    private void drawEntityItem(int posX, int posY, int scale, EntityItem ent) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        RenderManager renderer = Minecraft.getMinecraft().getRenderManager();
        renderer.setPlayerViewY(180.0F);
        boolean rs = renderer.isRenderShadow();
        renderer.setRenderShadow(false);
        renderer.renderEntity(ent, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, false);
        renderer.setRenderShadow(rs);
        GlStateManager.popMatrix();
    }

    private void drawOnFullScreen(int screenWidth, int screenHeight) {
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
