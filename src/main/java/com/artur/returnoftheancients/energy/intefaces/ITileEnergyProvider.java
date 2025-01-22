package com.artur.returnoftheancients.energy.intefaces;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ITileEnergyProvider extends ITileEnergy {
    @Override
    default boolean isEnergyLine() {return false;}

    /**
     * @param count takes value in kJ
     * @return value in kJ
     */
    float canAdd(float count);
    /**
     * @param count takes value in kJ
     */
    float add(float count);
    /**
     * @param count takes value in kJ
     */
    float take(float count);
    /**
     * @return value in kW
     */
    float maxInput();
    /**
     * @return value in kW
     */
    float maxOutput();

    boolean canAddFromFacing(EnumFacing facing);
    boolean canTakeFromFacing(EnumFacing facing);
    boolean isEmpty();
    boolean isNeedAdd();
    void setAdding();
    default boolean canTake() {
        boolean flag = false;

        for (EnumFacing facing : EnumFacing.values()) {
            if (isCanConnect(facing)) {
                BlockPos offsetPos = getPosE().offset(facing);
                TileEntity tileRaw = getWorldE().getTileEntity(offsetPos);
                if (tileRaw instanceof ITileEnergy && ((ITileEnergy) (tileRaw)).isCanConnect(facing.getOpposite())) {
                    if (canTakeFromFacing(facing)) {
                        flag = true;
                        break;
                    }
                }
            }
        }

        return flag;
    }
    default boolean canAdd() {
        boolean flag = false;

        for (EnumFacing facing : EnumFacing.values()) {
            if (isCanConnect(facing)) {
                BlockPos offsetPos = getPosE().offset(facing);
                TileEntity tileRaw = getWorldE().getTileEntity(offsetPos);
                if (tileRaw instanceof ITileEnergy && ((ITileEnergy) (tileRaw)).isCanConnect(facing.getOpposite())) {
                    if (canAddFromFacing(facing)) {
                        flag = true;
                        break;
                    }
                }
            }
        }

        return flag;
    }
}
