package com.artur.returnoftheancients.generation.portal.generators;

import net.minecraft.util.math.MathHelper;

public class GenAncientSpire {



    private int getRadius(double y, double scale) {
        y = y * scale;
        if (y <= 0) return 0;
        double g = 22.62741D;
        return MathHelper.floor((y * y) / (g * g));
    }
}
