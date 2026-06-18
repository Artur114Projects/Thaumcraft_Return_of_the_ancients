package com.artur114.thaumrota.client.light;

import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.nio.FloatBuffer;

public interface ILightSource {
    float heat();
    Color color();
    float range();
    float brightness();
    EnumLightType type();
    void writeToBuff(int pass, FloatBuffer buffer);
    boolean isOnView(int maxRenderDist);
    boolean collideToPos(BlockPos pos);
    int distanceSqToPlayer();
}
