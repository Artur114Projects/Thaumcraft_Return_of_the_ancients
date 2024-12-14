package com.artur.returnoftheancients.client.gui.container;

import com.artur.returnoftheancients.client.gui.buttons.ButtonAncientTeleportMain;
import com.artur.returnoftheancients.client.gui.buttons.ButtonsPageManager;
import com.artur.returnoftheancients.containers.ContainerAncientTeleport;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ServerPacketTileAncientTeleportData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import com.artur.returnoftheancients.utils.AspectBottle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.SlotItemHandler;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiAncientTeleport extends GuiContainer {
    private final ResourceLocation[] texturesBackground = new ResourceLocation[] {
            new ResourceLocation(Referense.MODID, "textures/gui/container/ancient_teleport_gui_page0.png"),
            new ResourceLocation(Referense.MODID, "textures/gui/container/ancient_teleport_gui_page1.png")
    };

    public final ContainerAncientTeleport container;
    private final TileEntityAncientTeleport tile;
    private ButtonAncientTeleportMain buttonMain;
    private final InventoryPlayer inventory;
    public ButtonsPageManager pageManager;

    public GuiAncientTeleport(TileEntityAncientTeleport tile, EntityPlayer player) {
        super(new ContainerAncientTeleport(player.inventory, tile));
        this.container = (ContainerAncientTeleport) this.inventorySlots;
        this.inventory = player.inventory;
        this.tile = tile;
        this.xSize = 239;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonMain = new ButtonAncientTeleportMain(0, (width - xSize) / 2 + (xSize - 16 - 64), (height - ySize) / 2 + 16);
        buttonList.add(buttonMain);
        ResourceLocation baseTexture = new ResourceLocation(Referense.MODID, "textures/gui/button/ancient_button_page1.png");
        pageManager = new ButtonsPageManager(buttonList, ((width - xSize) / 2 + 1 - 16), (height - ySize) / 2 + 0, 100, 2, 16, 1.0F, 'y', baseTexture, new ResourceLocation[] {new ResourceLocation(Referense.MODID, "textures/gui/button/ancient_button_page_icon0.png"), null}, new String[] {"Main page", "Activating"}) {
            @Override
            public void onPageChange(int page, int pageIn) {
                super.onPageChange(page, pageIn);
                if (pageIn == 1) {
                    container.unHideAll();
                } else {
                    container.hideAll();
                }
            }
        };
        pageManager.putButtonsOnPage(0, 0);
        pageManager.changePage(0);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        pageManager.processButtonActionPerformed(button);
        switch (button.id) {
            case 0:{
                buttonMain.press();
                MainR.NETWORK.sendToServer(new ServerPacketTileAncientTeleportData(tile.getPos(), tile.getWorld().provider.getDimension(), 0));
            } break;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        pageManager.update();
        buttonMain.update();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        buttonMain.enabled = tile.isActive != 1;
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (pageManager.getCurrentPage() == 1) {
            this.tile.aspectBottles.aspectBottles[0].draw(this, (width - xSize) / 2 + 10, (height - ySize) / 2 + 16, mouseX, mouseY, 32, 64);
            this.tile.aspectBottles.aspectBottles[1].draw(this, (width - xSize) / 2 + 10 + 32 + 4, (height - ySize) / 2 + 16, mouseX, mouseY, 32, 64);
        }
        drawHovered(mouseX, mouseY);
    }

    public void drawHovered(int mouseX, int mouseY) {
        this.renderHoveredToolTip(mouseX, mouseY);
        this.renderMainButtonHoveredToolTip(mouseX, mouseY);
        this.pageManager.renderHoveredText(this, mouseX, mouseY);
        for (AspectBottle bottle : tile.aspectBottles.aspectBottles) {
            bottle.drawHoveredText(this, mouseX, mouseY);
        }
    }

    public void renderMainButtonHoveredToolTip(int mouseX, int mouseY) {
        if  (pageManager.getCurrentPage() != 0) {
            return;
        }
        int x = buttonMain.x;
        int y = buttonMain.y;

        int x1 = x + buttonMain.width;
        int y1 = y + buttonMain.height;

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
        mc.getTextureManager().bindTexture(texturesBackground[pageManager.getCurrentPage()]);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
