package com.artur.returnoftheancients.client.gui;

import com.artur.returnoftheancients.containers.ContainerAncientTeleport;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ServerPacketTileAncientTeleportData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiAncientTeleport extends GuiContainer {
    private final ResourceLocation textureBackground = new ResourceLocation(Referense.MODID, "textures/gui/container/mystorage.png");
    private final ResourceLocation textureButton = new ResourceLocation(Referense.MODID, "textures/gui/container/ancient_teleport_main_button_icon.png");
    private final TileEntityAncientTeleport tile;
    private final InventoryPlayer inventory;
    private GuiButton buttonMain;

    public GuiAncientTeleport(TileEntityAncientTeleport tile, EntityPlayer player) {
        super(new ContainerAncientTeleport(player.inventory, tile));
        inventory = player.inventory;
        this.tile = tile;
        this.xSize = 246;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonMain = new ButtonAncientTeleportMain(0, (width - xSize) / 2 + 155, (height - ySize) / 2 + 22);
        buttonList.add(buttonMain);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:{
                MainR.NETWORK.sendToServer(new ServerPacketTileAncientTeleportData(tile.getPos(), tile.getWorld().provider.getDimension(), 0));
            } break;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        buttonMain.enabled = tile.isActive != 1;
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);
        this.renderMainButtonHoveredToolTip(mouseX, mouseY);
    }

    public void renderMainButtonHoveredToolTip(int mouseX, int mouseY) {
        int x = (width - xSize) / 2 + 155;
        int y = (height - ySize) / 2 + 22;

        int x1 = x + 64;
        int y1 = y + 64;

        if (mouseX > x && mouseX < x1 && mouseY > y && mouseY < y1) {
            if (tile.isActive != 1) {
                String formatting = TextFormatting.WHITE.toString();
                drawHoveringText(formatting + "Start", mouseX, mouseY);
            } else {
                drawHoveringText(TextFormatting.GREEN + "Launched, works stably", mouseX, mouseY);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        mc.getTextureManager().bindTexture(textureBackground);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(I18n.format(inventory.getName()),  12, ySize - 70, 0xffffff);
    }
}
