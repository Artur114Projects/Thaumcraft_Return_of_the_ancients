package com.artur114.thaumrota.common.tileentity;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.base.tileabs.ITileMultiBBProvider;
import com.artur114.thaumrota.common.generation.portal.naturalgen.AncientPortalNaturalGen;
import com.artur114.thaumrota.common.generation.portal.base.AncientPortal;
import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.generation.portal.naturalgen.AncientSanctuary;
import com.artur114.thaumrota.common.init.InitSounds;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

// TODO: 25.02.2025 Сделать звук процесса закрытия
public class TileEntityAncientSanctuaryController extends TileEntity implements ITickable, ITileMultiBBProvider {
    private static final List<AxisAlignedBB> boundingBox = Arrays.asList(
        BananaMC.createAABBFromPixels(1, 0, 1, 15, 1, 15),
        BananaMC.createAABBFromPixels(2, 1, 2, 14, 2, 14),
        BananaMC.createAABBFromPixels(3, 2, 3, 13, 16, 13)
    );

    private @Nullable AncientSanctuary sanctuary = null;
    private final int maxDoorMovingProgress = 40;
    private int prevDoorMovingProgress = 0;
    private int doorMovingOrientation = -1;
    private int doorMovingProgress = 0;
    private boolean hasItem = false;
    private int openTimer = 0;


    private void onSateChanged(boolean state) {
        if (sanctuary != null) {
            sanctuary.onTileStateChanged(state);
        }

        SoundEvent sound = state ? InitSounds.ANCIENT_CONTROLLER_ACTIVATE : InitSounds.ANCIENT_CONTROLLER_DEACTIVATE;

        this.world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1, 1);
    }

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
        this.hasItem = in;
    }

    public void open() {
        if (isClose()) {
            this.openTimer = 40;
            if (!world.isRemote) {
                this.onSateChanged(false);
            }
        }
    }

    public void close() {
        if (isOpen()) {
            this.doorMovingOrientation = 1;
        }
    }
    public boolean isOpen() {
        return doorMovingProgress == 0;
    }

    private boolean isPrevClose() {
        return prevDoorMovingProgress == maxDoorMovingProgress && openTimer == 0;
    }

    public boolean isClose() {
        return doorMovingProgress == maxDoorMovingProgress && openTimer == 0;
    }

    @SideOnly(Side.CLIENT)
    public float doorMovingProgress(boolean prev) {
        float localDoorMovingProgress = prev ? prevDoorMovingProgress : doorMovingProgress;

        return 1 - MathHelper.cos((float) ((Math.PI / 2) * (localDoorMovingProgress / maxDoorMovingProgress)));
    }

    @Override
    public void update() {
        if (openTimer > 0) {
            openTimer--;
            if (openTimer == 0) {
                this.doorMovingOrientation = -1;
            } else {
                return;
            }
        }

        prevDoorMovingProgress = doorMovingProgress;

        doorMovingProgress += doorMovingOrientation;
        doorMovingProgress = MathHelper.clamp(doorMovingProgress, 0, maxDoorMovingProgress);

        if (!world.isRemote) {
            if (isDone() && !isPrevClose()) {
                this.onSateChanged(true);
            }
        }
    }

    @Override
    public void onLoad() {
        if (world.isRemote) {
            return;
        }

        AncientPortal portalRaw = AncientPortalsProcessor.getPortalOnPos(pos);

        if (portalRaw instanceof AncientPortalNaturalGen) {
            AncientPortalNaturalGen portal = (AncientPortalNaturalGen) portalRaw;
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

    @Override
    public List<AxisAlignedBB> boundingBoxes() {
        return boundingBox;
    }
}
