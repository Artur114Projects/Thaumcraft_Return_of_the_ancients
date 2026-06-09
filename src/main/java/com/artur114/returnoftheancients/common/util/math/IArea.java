package com.artur114.returnoftheancients.common.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface IArea {
    int areaSize();
    boolean isCollide(double x, double y, double z);
    @Nullable BlockPos fromIndex(int index);
    List<BlockPos> points();
    @SideOnly(Side.CLIENT) void renderArea(float alpha);

    default boolean isCollide(BlockPos pos) {
        return this.isCollide(pos.getX(), pos.getY(), pos.getZ());
    }
    default boolean isCollide(int x, int y, int z) {
        return this.isCollide(x, y, (double) z);
    }
    default boolean isCollide(Entity entity) {
        return this.isCollide(entity.posX, entity.posY, entity.posZ);
    }
    default <T extends Entity> List<T> sortCollided(List<T> entities) {
        List<T> ret = new ArrayList<>(entities);
        ret.removeIf(e -> !this.isCollide(e));
        return ret;
    }
    default <T> List<T> sortCollided(List<T> entities, Function<T, Entity> extractor) {
        List<T> ret = new ArrayList<>(entities);
        ret.removeIf(e -> !this.isCollide(extractor.apply(e)));
        return ret;
    }
}
