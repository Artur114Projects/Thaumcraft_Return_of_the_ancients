package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;

import com.artur114.bananalib.mc.math.EnumRot;
import net.minecraft.util.Rotation;

public enum EnumRotate {
    NON(1),
    C90(2),
    C180(4),
    C270(3);

    public final int id;
    EnumRotate(int id) {
        this.id = id;
    }

    public static Rotation toMc(EnumRotate rotate) {
        switch (rotate) {
            case C90:
                return Rotation.CLOCKWISE_90;
            case C180:
                return Rotation.CLOCKWISE_180;
            case C270:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }


    public float lightDegrees() {
        switch (this) {
            case C90:
                return -90.0F;
            case C180:
                return -180.0F;
            case C270:
                return -270.0F;
            default:
                return -0.0F;
        }
    }

    public EnumRot toBanana() {
        switch (this) {
            case C90:
                return EnumRot.C90;
            case C180:
                return EnumRot.C180;
            case C270:
                return EnumRot.C270;
            default:
                return EnumRot.NON;
        }
    }

    public EnumRotate wrap(EnumRotate maxRot) {
        return EnumRotate.values()[this.ordinal() % (maxRot.ordinal() + 1)];
    }
}
