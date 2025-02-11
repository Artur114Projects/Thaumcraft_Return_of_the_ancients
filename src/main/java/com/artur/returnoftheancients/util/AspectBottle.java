package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

public class AspectBottle {

    public final Aspect aspect;
    public final int maxCont;
    public int lastCount;
    private final int x;
    private final int y;
    private int count;

    @SideOnly(Side.CLIENT)
    private boolean hovered = false;


    public AspectBottle(Aspect aspect, int maxCount, int x, int y) {
        this.maxCont = maxCount;
        this.aspect = aspect;
        this.count = 0;
        this.x = x;
        this.y = y;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("count", count);
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        count = nbt.getInteger("count");
    }

    @SideOnly(Side.CLIENT)
    public void draw(GuiContainer container, int mouseX, int mouseY, int width, int height, ResourceLocation texture) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int dx = (container.width - container.xSize) / 2 + x;
        int dy = (container.height - container.ySize) / 2 + y;

        int x1 = dx + width;
        int y1 = dy + height;

        double endU;
        double startU;

        int color = aspect.getColor();

        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = 1;

        startU = 64.0F / 96.0F;
        endU = 1;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        container.mc.getTextureManager().bindTexture(texture);
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(dx, y1, 0).tex(startU, 1).endVertex();
        builder.pos(x1, y1, 0).tex(endU, 1).endVertex();
        builder.pos(x1, dy, 0).tex(endU, 0).endVertex();
        builder.pos(dx, dy, 0).tex(startU, 0).endVertex();
        tessellator.draw();


        GlStateManager.disableTexture2D();
        GlStateManager.color(red, green, blue, alpha);

        float ix = width / 32.0F;
        float iy = height / 64.0F;

        int localHeight = (int) (height - 8 * iy);

        int ay = (int) ((dy) + (localHeight - (localHeight * ((float) count / maxCont))));

        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(dx + 7 * ix, dy + localHeight + 4, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(x1 - 7 * ix, dy + localHeight + 4, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(x1 - 7 * ix, ay + 4, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(dx + 7 * ix, ay + 4, 0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);


        hovered = mouseX > dx && mouseX < x1 && mouseY > dy && mouseY < y1;

        int i = hovered ? 1 : 0;

        startU = (32 * i) / 96.0F;
        endU = (32 * (i + 1)) / 96.0F;

        container.mc.getTextureManager().bindTexture(texture);
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(dx, y1, 0).tex(startU, 1).endVertex();
        builder.pos(x1, y1, 0).tex(endU, 1).endVertex();
        builder.pos(x1, dy, 0).tex(endU, 0).endVertex();
        builder.pos(dx, dy, 0).tex(startU, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public void drawHoveredText(GuiContainer container, int mouseX, int mouseY) {
        if (hovered) {
            char s = Character.toUpperCase(aspect.getName().charAt(0));
            String string = s + aspect.getName().replaceAll(String.valueOf(aspect.getName().charAt(0)), "");
            String sColor = "";
            String chatColor = HandlerR.getAspectChatColor(aspect);
            if (chatColor != null) {
                sColor = chatColor;
            }
            container.drawHoveringText(sColor + string + TextFormatting.RESET + " " + count + "/" + maxCont, mouseX, mouseY);
            hovered = false;
        }
    }

    public boolean isCanAdd() {
        return count < maxCont;
    }

    public void setCount(int count) {
        this.count = Math.min(count, maxCont);
    }

    public int getCount() {
        return count;
    }

    public boolean takeCount(int take) {
        if (count >= take) {
            count -= take;
        }
        return false;
    }

    public int addCount(int add) {
        if (this.count + add <= maxCont) {
            this.count += add;
            return add;
        } else {
            int localCount = this.count;
            this.count = maxCont;
            return maxCont - localCount;
        }
    }

    public boolean canAdd(int count) {
        return !(this.count + count > maxCont);
    }
}
