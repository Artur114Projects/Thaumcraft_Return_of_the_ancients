package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.generation.portal.naturalgen.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.generation.portal.naturalgen.AncientSanctuary;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityAncientSanctuaryController extends TileEntity implements ITickable {
    private @Nullable AncientSanctuary sanctuary = null;
    private final int maxDoorMovingProgress = 40;
    private int prevDoorMovingProgress = 0;
    private int doorMovingOrientation = -1;
    private int doorMovingProgress = 0;
    private boolean hasItem = false;


    public void bindSanctuary(AncientSanctuary sanctuary) {
        this.sanctuary = sanctuary;
    }

    public boolean isDone() {
        return hasItem() && isClose();
    }

    public boolean hasItem() {
        return hasItem;
    }

    public void setHasItem(boolean in) {
        hasItem = in;
    }

    public void open() {
        if (isClose()) {
            doorMovingOrientation = -1;
        }
    }

    public void close() {
        if (isOpen()) {
            doorMovingOrientation = 1;
        }
    }
    public boolean isOpen() {
        return doorMovingProgress == 0;
    }

    private boolean isPrevClose() {
        return prevDoorMovingProgress == maxDoorMovingProgress;
    }

    public boolean isClose() {
        return doorMovingProgress == maxDoorMovingProgress;
    }

    @SideOnly(Side.CLIENT)
    public float getDoorMovingProgress(boolean prev) {
        float localDoorMovingProgress = prev ? prevDoorMovingProgress : doorMovingProgress;

        return 1 - MathHelper.cos((float) ((Math.PI / 2) * (localDoorMovingProgress / maxDoorMovingProgress)));
    }

    @Override
    public void update() {
        prevDoorMovingProgress = doorMovingProgress;

        doorMovingProgress += doorMovingOrientation;
        doorMovingProgress = MathHelper.clamp(doorMovingProgress, 0, maxDoorMovingProgress);

        if (!world.isRemote) {
            if (isDone() && !isPrevClose()) {
                if (sanctuary != null) {
                    sanctuary.onTileStateChanged(true);
                }
            } else if (isPrevClose() && !isDone()) {
                if (sanctuary != null) {
                    sanctuary.onTileStateChanged(false);
                }
            }
        }
    }

    @Override
    public void onLoad() {
        if (world.isRemote) {
            return;
        }

        AncientPortal portalRaw = AncientPortalsProcessor.getPortalOnPos(pos);

        if (portalRaw instanceof AncientPortalNaturalGeneration) {
            AncientPortalNaturalGeneration portal = (AncientPortalNaturalGeneration) portalRaw;
            AncientSanctuary sanctuary = portal.getNotBrokenSanctuary();
            if (sanctuary != null) {
                sanctuary.onTileLoad(this.isDone(), pos);
            }
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        NBTTagCompound nbt = super.writeToNBT(compound);
        nbt.setInteger("doorMovingOrientation", doorMovingOrientation);
        nbt.setInteger("doorMovingProgress", doorMovingProgress);
        nbt.setBoolean("hasItem", hasItem);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        doorMovingOrientation = compound.getInteger("doorMovingOrientation");
        doorMovingProgress = compound.getInteger("doorMovingProgress");
        hasItem = compound.getBoolean("hasItem");
    }

    @Override
    public @NotNull NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
}
