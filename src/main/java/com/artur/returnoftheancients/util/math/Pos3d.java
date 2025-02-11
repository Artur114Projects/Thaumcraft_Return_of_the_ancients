package com.artur.returnoftheancients.util.math;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

public class Pos3d extends Vec3d {
    public Pos3d(double x, double y, double z) {
        super(x, y, z);
    }

    public Pos3d(int x, int y, int z) {
        super(x, y, z);
    }

    public Pos3d(Vec3i vector) {
        super(vector);
    }

    public Pos3d(EntityPlayer player) {
        super(player.posX, player.posY, player.posZ);
    }

    public Pos3d(Vec3d vector) {
        super(vector.x, vector.y, vector.z);
    }

    public @NotNull Pos3d scale(double scale) {
        return new Pos3d(x * scale, y * scale, z * scale);
    }

    public Pos3d multiply(Vec3d vec3d) {
        return scale(vec3d.x, vec3d.y, vec3d.z);
    }

    public Pos3d scale(double x, double y, double z) {
        return new Pos3d(this.x * x, this.y * y, this.z * z);
    }

    public Pos3d add(int x, int y, int z) {
        return new Pos3d(this.x + x, this.y + y, this.z + z);
    }

    public @NotNull Pos3d add(Vec3d vec3d) {
        return new Pos3d(this.x + vec3d.x, this.y + vec3d.y, this.z + vec3d.z);
    }

    public Pos3d add(double x, double y, double z) {
        return new Pos3d(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull Pos3d rotateYaw(float yaw) {
        double yawRadians = Math.toRadians(yaw);
        double xPos = x;
        double zPos = z;
        if (yaw != 0) {
            double sin = Math.sin(yawRadians);
            double cos = Math.cos(yawRadians);
            xPos = x * cos - z * sin;
            zPos = z * cos + x * sin;
        }
        return new Pos3d(xPos, y, zPos);
    }

    @Override
    public @NotNull Pos3d rotatePitch(float pitch) {
        double pitchRadians = Math.toRadians(pitch);
        double yPos = y;
        double zPos = z;
        if (pitch != 0) {
            double sin = Math.sin(pitchRadians);
            double cos = Math.cos(pitchRadians);
            yPos = y * cos - z * sin;
            zPos = z * cos + y * sin;
        }
        return new Pos3d(x, yPos, zPos);
    }

    public @NotNull Pos3d normalize() {
        return new Pos3d(super.normalize());
    }
}
