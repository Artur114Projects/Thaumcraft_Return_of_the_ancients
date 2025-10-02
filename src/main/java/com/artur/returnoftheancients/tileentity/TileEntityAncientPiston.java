package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBBProvider;
import com.artur.returnoftheancients.util.math.CoordinateMatrix;
import com.artur.returnoftheancients.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class TileEntityAncientPiston extends TileBase implements ITileMultiBBProvider {
    private CoordinateMatrix matrix = null;
    @Override
    public AxisAlignedBB[] boundingBoxes() {
        if (this.matrix == null) {
            this.compileMatrix();
        }
        this.moveMatrix();

        return this.matrix.allBoundingBoxesArr();
    }

    private void moveMatrix() {
        float partialTicks = 1.0F;
        if (this.world.isRemote) partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        CoordinateMatrix move = this.matrix.child("move");
        move.clearTransforms(false);
        move.translate(0.0F, (-12.0F / 16.0F - (12.0F / 16.0F * MathHelper.cos((float) ((Math.PI * ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(partialTicks) % 40.0F) / 40.0F)) + (Math.PI / 2.0F))))), 0.0F);
    }

    private void compileMatrix() {
        this.matrix = new CoordinateMatrix();
        this.matrix.putBoundingBox(MathUtils.createBoundingBox(0, 0, 0, 16, 2, 16), 1);
        this.matrix.putBoundingBox(MathUtils.createBoundingBox(1, 2, 1, 15, 4, 15), 2);
        this.matrix.putBoundingBox(MathUtils.createBoundingBox(2, 4, 2, 14, 5, 14), 3);
        CoordinateMatrix move = this.matrix.child("move");
        move.putBoundingBox(MathUtils.createBoundingBox(4, 0, 4, 12, 16, 12), 1);
        move.putBoundingBox(MathUtils.createBoundingBox(3, 16, 3, 13, 18, 13), 2);
    }
}
