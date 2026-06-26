package com.artur114.thaumrota.client.render.tile;

import com.artur114.thaumrota.common.tileentity.TileEntityForDev;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;

public class TileEntityForDevRenderer extends TileEntitySpecialRendererBase<TileEntityForDev> {
    @Override
    public void doRender(TileEntityForDev tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (DevScriptsShell.isDev()) {
            ThaumRotA.DEV_SHELL.evaluate("render_tsr.groovy", "tile", tile, "x", x, "y", y, "z", z, "partialTicks", partialTicks, "destroyStage", destroyStage, "alpha", alpha, "world", Minecraft.getMinecraft().world);
        }
    }
}
