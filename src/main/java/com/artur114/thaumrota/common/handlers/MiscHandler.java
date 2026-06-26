package com.artur114.thaumrota.common.handlers;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.Random;

@Deprecated
public class MiscHandler {

    public static boolean isWithinRadius(double x1, double z1, double x2, double z2, double radius) {
        double dx = x1 - x2, dz = z1 - z2;
        return dx * dx + dz * dz <= radius * radius;
    }

    public static boolean getIgnoringChance(int percentage, Random rand) {
        if (percentage <= 0) {
            return true;
        }
        if (percentage >= 100) {
            return false;
        }
        return rand.nextInt(100) < percentage;
    }


    public static BlockPos.MutableBlockPos addToMutableBP(BlockPos.MutableBlockPos mPos, int addX, int addY, int addZ) {
        return mPos.setPos(mPos.getX() + addX, mPos.getY() + addY, mPos.getZ() + addZ);
    }

    @Nullable
    public static String getAspectChatColor(Aspect aspect) {
        String color = aspect.getChatcolor();
        if (color != null) {
            return "\u00a7" + color;
        }
        if (aspect == Aspect.DARKNESS) {
            return TextFormatting.BLACK.toString();
        } else if (aspect == Aspect.ELDRITCH) {
            return TextFormatting.DARK_PURPLE.toString();
        }
        return null;
    }

    public static boolean arrayContains(int[] array, int param) {
        for (int i : array) {
            if (i == param) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCollide(int pointX, int pointY, int point1X, int point1Y, int offset) {
        if (offset == 0) {
            return point1X == pointX && point1Y == pointY;
        }
        return Math.abs(point1X - pointX) <= offset && Math.abs(point1Y - pointY) <= offset;
    }

    public static boolean intTagListContains(NBTTagList list, int value) {
        if (list.hasNoTags()) {
            return false;
        }
        if (list.getTagType() != 3) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i != list.tagCount(); i++) {
            int v = list.getIntAt(i);

            if (v == value) {
                return true;
            }
        }

        return false;
    }
}
