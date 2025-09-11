package com.artur.returnoftheancients.util.math;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinateMatrix {
    private final List<ITransformation> transformations = new ArrayList<>();
    private final Map<Integer, AxisAlignedBB> originalBoundingBoxes = new HashMap<>();
    private final Map<Integer, Vec3d> originalVectors = new HashMap<>();
    private final Map<Integer, AxisAlignedBB> compiledBoundingBoxes = new HashMap<>();
    private final Map<Integer, Vec3d> compiledVectors = new HashMap<>();
    public boolean forceTransform = false;

    public Vec3d applyTransformations(Vec3d vec3d) {
        Vec3dM vec3dM = new Vec3dM(vec3d);
        this.performTransforms(vec3dM);
        return vec3dM.toNormal();
    }

    public AxisAlignedBB applyTransformations(AxisAlignedBB bb) {
        AxisAlignedBBM bbm = new AxisAlignedBBM(bb);
        this.performTransforms(bbm);
        return bbm.toNormal();
    }

    public void putVector(Vec3d vec3d, int id) {
        this.originalVectors.put(id, vec3d);
    }

    public void putBoundingBox(AxisAlignedBB bb, int id) {
        this.originalBoundingBoxes.put(id, bb);
    }

    public Vec3d getVector(int id) {
        Vec3d vec3d = this.compiledVectors.get(id);

        if (vec3d == null) {
            vec3d = this.applyTransformations(this.originalVectors.get(id));
            if (vec3d != null) {
                this.compiledVectors.put(id, vec3d);
            }
        }

        return vec3d;
    }

    public AxisAlignedBB getBoundingBox(int id) {
        AxisAlignedBB bb = this.compiledBoundingBoxes.get(id);

        if (bb == null) {
            bb = this.applyTransformations(this.originalBoundingBoxes.get(id));
            if (bb != null) {
                this.compiledBoundingBoxes.put(id, bb);
            }
        }

        return bb;
    }

    public void rotate(double angle, double x, double y, double z) {
        if (!this.forceTransform) {
            this.addTransformation(new TransformationRotateD(angle, x, y, z));
        }
    }

    public void rotate(float angle, float x, float y, float z) {
        if (!this.forceTransform) {
            this.addTransformation(new TransformationRotateF(angle, x, y, z));
        }
    }

    public void translate(double x, double y, double z) {
        if (!this.forceTransform) {
            this.addTransformation(new TransformationTranslateD(x, y, z));
        }
    }

    public void translate(float x, float y, float z) {
        if (!this.forceTransform) {
            this.addTransformation(new TransformationTranslateF(x, y, z));
        }
    }

    public void scale(double x, double y, double z) {
        if (!this.forceTransform) {
            this.addTransformation(new TransformationScaleD(x, y, z));
        }
    }

    public void scale(float x, float y, float z) {
        if (!this.forceTransform) {
            this.addTransformation(new TransformationScaleF(x, y, z));
        }
    }

    private void addTransformation(ITransformation tr) {
        this.compiledBoundingBoxes.clear();
        this.compiledVectors.clear();
        this.transformations.add(tr);
    }

    private void performTransforms(ITransformable transformable) {
        for (ITransformation tr : this.transformations) {
            transformable.transform(tr);
        }
    }

    private interface ITransformation {
        void applyTransform(Vec3dM vec3dM);
    }

    private static class TransformationRotateD implements ITransformation {
        private final byte type;
        private double angle;

        public TransformationRotateD(double angle, double x, double y, double z) {
            this.angle = angle;
            if (x != 0.0D) {
                this.type = 1;
                this.angle *= x;
            } else if (y != 0.0D) {
                this.type = 2;
                this.angle *= y;
            } else if (z != 0.0D) {
                this.type = 3;
                this.angle *= z;
            } else {
                this.type = 0;
            }
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            if (this.angle == 0.0D || this.type == 0) {
                return;
            }
            double xPos, yPos, zPos;
            double rad, sin, cos;
            switch (this.type) {
                case 1:
                    rad = Math.toRadians(this.angle);
                    sin = Math.sin(rad);
                    cos = Math.cos(rad);
                    yPos = vec3dM.y * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.y * sin;
                    vec3dM.y = yPos;
                    vec3dM.z = zPos;
                    break;
                case 2:
                    rad = Math.toRadians(this.angle);
                    sin = Math.sin(rad);
                    cos = Math.cos(rad);
                    xPos = vec3dM.x * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.x * sin;
                    vec3dM.x = xPos;
                    vec3dM.z = zPos;
                    break;
                case 3: 
                    rad = Math.toRadians(this.angle);
                    sin = Math.sin(rad);
                    cos = Math.cos(rad);
                    xPos = vec3dM.x * cos + vec3dM.y * sin;
                    yPos = vec3dM.y * cos - vec3dM.x * sin;
                    vec3dM.y = yPos;
                    vec3dM.x = xPos;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.type);
            }
        }
    }

    private static class TransformationRotateF implements ITransformation {
        private final byte type;
        private float angle;

        public TransformationRotateF(float angle, float x, float y, float z) {
            this.angle = angle;
            if (x != 0.0D) {
                this.type = 1;
                this.angle *= x;
            } else if (y != 0.0D) {
                this.type = 2;
                this.angle *= y;
            } else if (z != 0.0D) {
                this.type = 3;
                this.angle *= z;
            } else {
                this.type = 0;
            }
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            if (this.angle == 0.0D || this.type == 0) {
                return;
            }
            double xPos, yPos, zPos;
            float rad, sin, cos;
            switch (this.type) {
                case 1:
                    rad = (float) Math.toRadians(this.angle);
                    sin = MathHelper.sin(rad);
                    cos = MathHelper.cos(rad);
                    yPos = vec3dM.y * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.y * sin;
                    vec3dM.y = yPos;
                    vec3dM.z = zPos;
                    break;
                case 2:
                    rad = (float) Math.toRadians(this.angle);
                    sin = MathHelper.sin(rad);
                    cos = MathHelper.cos(rad);
                    xPos = vec3dM.x * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.x * sin;
                    vec3dM.x = xPos;
                    vec3dM.z = zPos;
                    break;
                case 3:
                    rad = (float) Math.toRadians(this.angle);
                    sin = MathHelper.sin(rad);
                    cos = MathHelper.cos(rad);
                    yPos = vec3dM.y * cos - vec3dM.x * sin;
                    xPos = vec3dM.x * cos + vec3dM.y * sin;
                    vec3dM.y = yPos;
                    vec3dM.x = xPos;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.type);
            }
        }
    }

    private static class TransformationTranslateD implements ITransformation {
        private final double x;
        private final double y;
        private final double z;

        private TransformationTranslateD(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x += this.x;
            vec3dM.y += this.y;
            vec3dM.z += this.z;
        }
    }

    private static class TransformationTranslateF implements ITransformation {
        private final float x;
        private final float y;
        private final float z;

        private TransformationTranslateF(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x += this.x;
            vec3dM.y += this.y;
            vec3dM.z += this.z;
        }
    }

    private static class TransformationScaleD implements ITransformation {
        private final double x;
        private final double y;
        private final double z;

        private TransformationScaleD(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x *= this.x;
            vec3dM.y *= this.y;
            vec3dM.z *= this.z;
        }
    }

    private static class TransformationScaleF implements ITransformation {
        private final float x;
        private final float y;
        private final float z;

        private TransformationScaleF(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x *= this.x;
            vec3dM.y *= this.y;
            vec3dM.z *= this.z;
        }
    }

    private interface ITransformable {
        ITransformable transform(ITransformation transformation);
    }

    private static class Vec3dM implements ITransformable {
        public double x;
        public double y;
        public double z;

        public Vec3dM(double xIn, double yIn, double zIn) {
            if (xIn == -0.0D) {
                xIn = 0.0D;
            }

            if (yIn == -0.0D) {
                yIn = 0.0D;
            }

            if (zIn == -0.0D) {
                zIn = 0.0D;
            }

            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
        }

        public Vec3dM(Vec3d vec3d) {
            this(vec3d.x, vec3d.y, vec3d.z);
        }

        public Vec3d toNormal() {
            return new Vec3d(this.x, this.y, this.z);
        }

        @Override
        public Vec3dM transform(ITransformation transformation) {
            transformation.applyTransform(this); return this;
        }
    }

    private static class AxisAlignedBBM implements ITransformable {
        public final Vec3dM min;
        public final Vec3dM max;

        public AxisAlignedBBM(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.min = new Vec3dM(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
            this.max = new Vec3dM(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
        }

        public AxisAlignedBBM(AxisAlignedBB bb) {
            this(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
        }

        public AxisAlignedBB toNormal() {
            return new AxisAlignedBB(this.min.x, this.min.y, this.min.z, this.max.x, this.max.y, this.max.z);
        }

        @Override
        public AxisAlignedBBM transform(ITransformation transformation) {
            transformation.applyTransform(this.min);
            transformation.applyTransform(this.max);
            return this;
        }
    }
}
