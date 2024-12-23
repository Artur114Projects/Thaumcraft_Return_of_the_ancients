package com.artur.returnoftheancients.energy.intefaces;

public interface ITileEnergyProvider extends ITileEnergy {
    @Override
    default boolean isEnergyLine() {return false;}
}
