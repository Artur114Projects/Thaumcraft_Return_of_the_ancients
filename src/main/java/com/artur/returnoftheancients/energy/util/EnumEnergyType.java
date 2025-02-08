package com.artur.returnoftheancients.energy.util;

import org.jetbrains.annotations.Nullable;

public enum EnumEnergyType {
    KILO(null, 1),
    MEGA(KILO, 1000),
    GIGA(MEGA, 1000),
    TERRA(GIGA, 1000);

    private final @Nullable EnumEnergyType parent;
    private final int multiplier;

    EnumEnergyType(@Nullable EnumEnergyType parent, int multiplier) {
        this.multiplier = multiplier;
        this.parent = parent;
    }

    public float format(float count) {
        if (parent != null) {
            return parent.format(count) * multiplier;
        } else {
            return count * multiplier;
        }
    }
}
