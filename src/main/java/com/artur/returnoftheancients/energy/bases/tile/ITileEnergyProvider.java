package com.artur.returnoftheancients.energy.bases.tile;

import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ITileEnergyProvider extends ITileEnergy {
    @Override
    default boolean isEnergyLine() {return false;}
    /**
     * @param count takes value in kJ
     * @return value in kJ the amount of energy that can be added
     */
    float canAdd(float count);
    /**
     * @param count takes value in kJ
     * @return value in kJ the amount of energy that was added to add
     */
    float add(float count);
    /**
     * @param count takes value in kJ
     * @return the amount of energy that was able to pick up
     */
    float take(float count);
    /**
     * @param count takes value in kJ
     * @return the amount of energy that can be taken
     */
    float canTake(float count);

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
    default boolean canTake() {
        return this.can((this::canTakeFromFacing));
    }
    default boolean canAdd() {
        return this.can((this::canAddFromFacing));
    }
    default boolean can(Predicate<EnumFacing> can) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (canConnect(facing)) {
                BlockPos offsetPos = this.pos().offset(facing);
                TileEntity tileRaw = this.world().getTileEntity(offsetPos);
                if (tileRaw instanceof ITileEnergy && ((ITileEnergy) (tileRaw)).canConnect(facing.getOpposite())) {
                    if (can.test(facing)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    default boolean can(UltraMutableBlockPos util, Predicate<EnumFacing> can, Function<BlockPos, ITileEnergy> getTile) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (canConnect(facing)) {
                BlockPos offsetPos = util.setPos(this.pos()).offset(facing);
                ITileEnergy tile = getTile.apply(offsetPos);
                if (tile != null && tile.canConnect(facing.getOpposite())) {
                    if (can.test(facing)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
