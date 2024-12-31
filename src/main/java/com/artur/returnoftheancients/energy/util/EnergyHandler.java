package com.artur.returnoftheancients.energy.util;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class EnergyHandler {

    public static final ResourceLocation barBaseTexture = new ResourceLocation(Referense.MODID, "textures/gui/container/energy_bar_base.png");

    private float lastEnergyCount = 0.0F;
    public float energyCount = 0.0F;
    public final float maxOutput;
    public final float maxEnergy;
    public final float maxInput;
    private final int syncLine;

    @SideOnly(Side.CLIENT)
    private boolean hoveredOutput = false;
    @SideOnly(Side.CLIENT)
    private boolean hoveredInput = false;



    /**
     * @param maxEnergy in kJ
     * @param maxOutput in kW
     * @param maxInput in kW
     */
    public EnergyHandler(float maxEnergy, float maxOutput, float maxInput, int syncLine) {
        this.maxOutput = maxOutput / 20.0F;
        this.maxInput = maxInput / 20.0F;
        this.maxEnergy = maxEnergy;
        this.syncLine = syncLine;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setFloat("EnergyCount", energyCount);
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        energyCount = nbt.getFloat("EnergyCount");
    }

    public float getMaxInputInkW() {
        return maxInput;
    }

    public float getMaxOutputInkW() {
        return maxOutput;
    }

    public boolean isNeedAdd() {
        return energyCount < maxEnergy;
    }

    public boolean isEmpty() {
        return energyCount <= 0;
    }

    public float canAdd(float count) {
        if (energyCount + count <= maxEnergy) {
            return count;
        } else {
            return maxEnergy - energyCount;
        }
    }

    public float add(float count) {
        if (energyCount + count <= maxEnergy) {
            energyCount += count;
            return count;
        } else {
            float localCount = this.energyCount;
            this.energyCount = maxEnergy;
            return maxEnergy - localCount;
        }
    }

    public float take(float count) {
        if (energyCount - count >= 0) {
            energyCount -= count;
            return count;
        } else {
            float localCount = this.energyCount;
            energyCount = 0;
            return localCount;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateOnClient(int data) {
        energyCount = data / 1000.0F;
    }

    @SideOnly(Side.CLIENT)
    public void drawInput(int x, int y, int drawAreaWidth, int drawAreaHeight, int mouseX, int mouseY, ResourceLocation texture) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        Minecraft mc = Minecraft.getMinecraft();
        int x1 = x + drawAreaWidth;
        int y1 = y + drawAreaHeight;
        hoveredInput = mouseX > x && mouseX < x1 && mouseY > y && mouseY < y1;
        int i = hoveredInput ? 1 : 0;

        mc.getTextureManager().bindTexture(texture);
        HandlerR.renderTextureAtlas(x, y, 0, 12, 48, 18, drawAreaWidth, drawAreaHeight);

        mc.getTextureManager().bindTexture(barBaseTexture);
        drawInputBar(x, y, drawAreaWidth, drawAreaHeight);

        mc.getTextureManager().bindTexture(texture);
        HandlerR.renderTextureAtlas(x, y, 0, 6 * i, 48, 18, drawAreaWidth, drawAreaHeight);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public void drawOutput(int x, int y, int drawAreaWidth, int drawAreaHeight, int mouseX, int mouseY, ResourceLocation texture) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        Minecraft mc = Minecraft.getMinecraft();
        int x1 = x + drawAreaWidth;
        int y1 = y + drawAreaHeight;
        hoveredOutput = mouseX > x && mouseX < x1 && mouseY > y && mouseY < y1;
        int i = hoveredOutput ? 1 : 0;

        mc.getTextureManager().bindTexture(texture);
        HandlerR.renderTextureAtlas(x, y, 0, 12, 48, 18, drawAreaWidth, drawAreaHeight);

        mc.getTextureManager().bindTexture(barBaseTexture);
        drawOutputBar(x, y, drawAreaWidth, drawAreaHeight);

        mc.getTextureManager().bindTexture(texture);
        HandlerR.renderTextureAtlas(x, y, 0, 6 * i, 48, 18, drawAreaWidth, drawAreaHeight);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    @SideOnly(Side.CLIENT)
    private void drawOutputBar(int x, int y, int drawAreaWidth, int drawAreaHeight) {
        double count = energyCount / maxEnergy;

        x += (drawAreaWidth - 2) + 1;
        y += 1;

        int x1 = (int) Math.ceil(x - (drawAreaWidth - 2) * count);
        int y1 = y + (drawAreaHeight - 2);

        double endU = (double) ((int) ((drawAreaWidth - 2) * count)) / (drawAreaWidth - 2);
        double startU = 0;

        double endV = 1;
        double startV = 0;

        HandlerR.renderPrimitive(x1, x, y, y1, endU, startU, startV, endV);
    }

    @SideOnly(Side.CLIENT)
    private void drawInputBar(int x, int y, int drawAreaWidth, int drawAreaHeight) {
        double count = energyCount / maxEnergy;

        x += 1;
        y += 1;

        int x1 = (int) (x + (drawAreaWidth - 2) * count);
        int y1 = y + (drawAreaHeight - 2);

        double endU = (double) ((int) ((drawAreaWidth - 2) * count)) / (drawAreaWidth - 2);
        double startU = 0;

        double endV = 1;
        double startV = 0;

        HandlerR.renderPrimitive(x, x1, y, y1, startU, endU, startV, endV);
    }

    public void drawHoveredText(GuiContainer container, int mouseX, int mouseY) {
        if (!hoveredOutput && !hoveredInput) {
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(TextFormatting.AQUA + I18n.format(Referense.MODID + ".energy.local.0") + TextFormatting.RESET + " " + HandlerR.kJToString(energyCount) + "/" + HandlerR.kJToString(maxEnergy));
        if (hoveredInput) {
            list.add(TextFormatting.YELLOW + I18n.format(Referense.MODID + ".energy.local.1") + TextFormatting.RESET);
        }
        if (hoveredOutput) {
            list.add(TextFormatting.YELLOW + I18n.format(Referense.MODID + ".energy.local.2") + TextFormatting.RESET);
        }
        container.drawHoveringText(list, mouseX, mouseY);

        hoveredInput = hoveredOutput = false;
    }

    public void sendWindowProperty(IContainerListener listener, Container container) {
        listener.sendWindowProperty(container, syncLine, (int) (energyCount * 1000));
    }

    public void detectAndSendChanges(IContainerListener listener, Container container) {
        if (lastEnergyCount != energyCount) {
            sendWindowProperty(listener, container);
        }
        lastEnergyCount = energyCount;
    }
}
