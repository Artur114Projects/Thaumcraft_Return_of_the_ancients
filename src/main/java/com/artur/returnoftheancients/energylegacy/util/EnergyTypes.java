package com.artur.returnoftheancients.energylegacy.util;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.resources.I18n;
import org.jetbrains.annotations.Nullable;

public enum EnergyTypes {
    KILO(null, 1),
    MEGA(KILO, 1000),
    GIGA(MEGA, 1000),
    TERRA(GIGA, 1000);

    private final @Nullable EnergyTypes parent;
    private final int multiplier;

    EnergyTypes(@Nullable EnergyTypes parent, int multiplier) {
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



    public static String kJToString(float count) {
        String[] prefixes = I18n.format(Referense.MODID + ".kJ.prefixes").split("/");
        float localCount = count;
        if (localCount < 1) {
            return ((int) (localCount * 1000)) + prefixes[0];
        }
        for (int i = 1; i != prefixes.length; i++) {
            if (localCount < 1000) {
                return (((int) (localCount * 100)) / 100.0F) + prefixes[i];
            }
            localCount /= 1000;
        }
        return "";
    }

    public static String kWToString(float count) {
        String[] prefixes = I18n.format(Referense.MODID + ".kW.prefixes").split("/");
        float localCount = count;
        if (localCount < 1) {
            return ((int) (localCount * 1000)) + prefixes[0];
        }
        for (int i = 1; i != prefixes.length; i++) {
            if (localCount < 1000) {
                return (((int) (localCount * 100)) / 100.0F) + prefixes[i];
            }
            localCount /= 1000;
        }
        return "";
    }
}
