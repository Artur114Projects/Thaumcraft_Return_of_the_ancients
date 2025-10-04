package com.artur.returnoftheancients.util.math;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class CoordinateMatrix {
    private final Map<Integer, AxisAlignedBB> originalBoundingBoxes = new HashMap<>();
    private final Map<Integer, AxisAlignedBB> compiledBoundingBoxes = new HashMap<>();
    private final LinkedList<LinkedList<ITransformation>> stack = new LinkedList<>();
    private LinkedList<ITransformation> transformations = new LinkedList<>();
    private final Map<Integer, Vec3d> originalVectors = new HashMap<>();
    private final Map<Integer, Vec3d> compiledVectors = new HashMap<>();
    private Map<String, CoordinateMatrix> children = null;
    private final CoordinateMatrix parent;
    public final boolean angleInRadians;

    public CoordinateMatrix(boolean angleInRadians) {
        this(angleInRadians, null);
    }

    private CoordinateMatrix(boolean angleInRadians, CoordinateMatrix parent) {
        this.angleInRadians = angleInRadians;
        this.parent = parent;
    }

    public CoordinateMatrix() {
        this(false);
    }

    public boolean isEmpty() {
        return this.originalBoundingBoxes.isEmpty() && this.originalVectors.isEmpty() && (this.children == null || children.isEmpty());
    }

    public CoordinateMatrix child(String name) {
        if (this.children == null) {
            this.children = new HashMap<>();
        }
        CoordinateMatrix child = this.children.get(name);
        if (child == null) {
            child = new CoordinateMatrix(this.angleInRadians, this);
            this.children.put(name, child);
        }
        return child;
    }

    public void removeChild(String name) {
        if (this.children != null) {
            this.children.remove(name);
        }
    }

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

    public List<Vec3d> allVectors() {
        List<Vec3d> list = new ArrayList<>();
        for (int id : this.originalVectors.keySet()) {
            list.add(this.getVector(id));
        }
        if (this.children != null) {
            for (CoordinateMatrix child : this.children.values()) {
                list.addAll(child.allVectors());
            }
        }
        return list;
    }

    public Vec3d[] allVectorsArr() {
        return this.allVectors().toArray(new Vec3d[0]);
    }

    public List<AxisAlignedBB> allBoundingBoxes() {
        List<AxisAlignedBB> list = new ArrayList<>();
        for (int id : this.originalBoundingBoxes.keySet()) {
            list.add(this.getBoundingBox(id));
        }
        if (this.children != null) {
            for (CoordinateMatrix child : this.children.values()) {
                list.addAll(child.allBoundingBoxes());
            }
        }
        return list;
    }

    public AxisAlignedBB[] allBoundingBoxesArr() {
        return this.allBoundingBoxes().toArray(new AxisAlignedBB[0]);
    }

    public void removeVector(int id) {
        this.originalVectors.remove(id);
        this.compiledVectors.remove(id);
    }

    public void removeBoundingBox(int id) {
        this.originalBoundingBoxes.remove(id);
        this.compiledBoundingBoxes.remove(id);
    }

    public void rotate(double angle, double x, double y, double z) {
        this.addTransformation(new TransformationRotateD(angle, x, y, z, this.angleInRadians));
    }

    public void rotate(float angle, float x, float y, float z) {
        this.addTransformation(new TransformationRotateF(angle, x, y, z, this.angleInRadians));
    }

    public void translate(int x, int y, int z) {
        this.translate(x / 16.0F, y / 16.0F, z / 16.0F);
    }

    public void translate(double x, double y, double z) {
        this.addTransformation(new TransformationTranslateD(x, y, z));
    }

    public void translate(float x, float y, float z) {
        this.addTransformation(new TransformationTranslateF(x, y, z));
    }

    public void scale(double x, double y, double z) {
        this.addTransformation(new TransformationScaleD(x, y, z));
    }

    public void scale(float x, float y, float z) {
        this.addTransformation(new TransformationScaleF(x, y, z));
    }

    public void pushMatrix() {
        this.stack.addLast(new LinkedList<>(this.transformations));
    }

    public void popMatrix() {
        if (!this.stack.isEmpty()) {
            this.transformations = this.stack.pollLast();
        }
    }

    public void clearTransforms(boolean clearContext) {
        if (clearContext) this.stack.clear();
        this.transformations.clear();
        if (this.children != null) {
            this.children.forEach((k, v) -> v.clearTransforms(clearContext));
        }
    }

    public void clear() {
        this.stack.clear();
        this.transformations.clear();
        this.originalVectors.clear();
        this.compiledVectors.clear();
        this.originalBoundingBoxes.clear();
        this.compiledBoundingBoxes.clear();
        if (this.children != null) {
            this.children.forEach((k, v) -> v.clear());
        }
    }

    public void clearAll() {
        this.stack.clear();
        this.children.clear();
        this.transformations.clear();
        this.originalVectors.clear();
        this.compiledVectors.clear();
        this.originalBoundingBoxes.clear();
        this.compiledBoundingBoxes.clear();
    }

    private void addTransformation(ITransformation tr) {
        this.compiledBoundingBoxes.clear();
        this.compiledVectors.clear();
        ITransformation lastTransformation = this.transformations.peekLast();
        if (lastTransformation == null || lastTransformation.type() != tr.type() || !lastTransformation.update(tr)) {
            this.transformations.add(tr);
        }
    }

    private void performTransforms(ITransformable transformable) {
        if (this.parent != null) {
            for (ITransformation tr : this.parent.transformations) {
                transformable.transform(tr);
            }
        }
        for (ITransformation tr : this.transformations) {
            transformable.transform(tr);
        }
    }

    private interface ITransformation {
        void applyTransform(Vec3dM vec3dM);
        boolean update(ITransformation tr);
        int type();
    }

    private static class TransformationRotateD implements ITransformation {
        private final boolean angleInRadians;
        private double angle;
        private byte type;

        public TransformationRotateD(double angle, double x, double y, double z, boolean angleInRadians) {
            this(angleInRadians);
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

        private TransformationRotateD(boolean angleInRadians) {
            this.angleInRadians = angleInRadians;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            if (this.angle == 0.0D || this.type == 0) {
                return;
            }
            double xPos, yPos, zPos;
            double rad, sin, cos;
            switch (this.type) {
                case 1: {
                    if (this.angleInRadians) {
                        rad = this.angle;
                    } else {
                        rad = Math.toRadians(this.angle);
                    }
                    sin = Math.sin(rad);
                    cos = Math.cos(rad);
                    yPos = vec3dM.y * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.y * sin;
                    vec3dM.y = yPos;
                    vec3dM.z = zPos;
                }
                break;
                case 2: {
                    if (this.angleInRadians) {
                        rad = this.angle;
                    } else {
                        rad = Math.toRadians(this.angle);
                    }
                    sin = Math.sin(rad);
                    cos = Math.cos(rad);
                    xPos = vec3dM.x * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.x * sin;
                    vec3dM.x = xPos;
                    vec3dM.z = zPos;
                }
                break;
                case 3: {
                    if (this.angleInRadians) {
                        rad = this.angle;
                    } else {
                        rad = Math.toRadians(this.angle);
                    }
                    sin = Math.sin(rad);
                    cos = Math.cos(rad);
                    xPos = vec3dM.x * cos + vec3dM.y * sin;
                    yPos = vec3dM.y * cos - vec3dM.x * sin;
                    vec3dM.y = yPos;
                    vec3dM.x = xPos;
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.type);
            }
        }

        @Override
        public boolean update(ITransformation tr) {
            if (tr.type() == this.type()) {
                TransformationRotateD trd = (TransformationRotateD) tr;
                if (this.type == trd.type) {
                    this.angle += trd.angle;
                    return true;
                }
            }
            return false;
        }

        @Override
        public int type() {
            return 0;
        }
    }

    private static class TransformationRotateF implements ITransformation {
        private final boolean angleInRadians;
        private float angle;
        private byte type;

        public TransformationRotateF(float angle, float x, float y, float z, boolean angleInRadians) {
            this(angleInRadians);
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

        private TransformationRotateF(boolean angleInRadians) {
            this.angleInRadians = angleInRadians;
        }

        public TransformationRotateF update(float angle, float x, float y, float z) {
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
            return this;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            if (this.angle == 0.0D || this.type == 0) {
                return;
            }
            double xPos, yPos, zPos;
            float rad, sin, cos;
            switch (this.type) {
                case 1: {
                    if (this.angleInRadians) {
                        rad = this.angle;
                    } else {
                        rad = (float) Math.toRadians(this.angle);
                    }
                    sin = MathHelper.sin(rad);
                    cos = MathHelper.cos(rad);
                    yPos = vec3dM.y * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.y * sin;
                    vec3dM.y = yPos;
                    vec3dM.z = zPos;
                }
                break;
                case 2: {
                    if (this.angleInRadians) {
                        rad = this.angle;
                    } else {
                        rad = (float) Math.toRadians(this.angle);
                    }
                    sin = MathHelper.sin(rad);
                    cos = MathHelper.cos(rad);
                    xPos = vec3dM.x * cos - vec3dM.z * sin;
                    zPos = vec3dM.z * cos + vec3dM.x * sin;
                    vec3dM.x = xPos;
                    vec3dM.z = zPos;
                }
                break;
                case 3: {
                    if (this.angleInRadians) {
                        rad = this.angle;
                    } else {
                        rad = (float) Math.toRadians(this.angle);
                    }
                    sin = MathHelper.sin(rad);
                    cos = MathHelper.cos(rad);
                    yPos = vec3dM.y * cos - vec3dM.x * sin;
                    xPos = vec3dM.x * cos + vec3dM.y * sin;
                    vec3dM.y = yPos;
                    vec3dM.x = xPos;
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.type);
            }
        }

        @Override
        public boolean update(ITransformation tr) {
            if (tr.type() == this.type()) {
                TransformationRotateF trf = (TransformationRotateF) tr;
                if (this.type == trf.type) {
                    this.angle += trf.angle;
                    return true;
                }
            }
            return false;
        }

        @Override
        public int type() {
            return 1;
        }
    }

    private static class TransformationTranslateD implements ITransformation {
        private double x;
        private double y;
        private double z;

        private TransformationTranslateD(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private TransformationTranslateD() {}

        public TransformationTranslateD update(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x += this.x;
            vec3dM.y += this.y;
            vec3dM.z += this.z;
        }


        @Override
        public boolean update(ITransformation tr) {
            if (tr.type() == this.type()) {
                TransformationTranslateD trd = (TransformationTranslateD) tr;
                this.x += trd.x;
                this.y += trd.y;
                this.z += trd.z;
                return true;
            }
            return false;
        }

        @Override
        public int type() {
            return 2;
        }
    }

    private static class TransformationTranslateF implements ITransformation {
        private float x;
        private float y;
        private float z;

        private TransformationTranslateF(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private TransformationTranslateF() {}

        public TransformationTranslateF update(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x += this.x;
            vec3dM.y += this.y;
            vec3dM.z += this.z;
        }

        @Override
        public boolean update(ITransformation tr) {
            if (tr.type() == this.type()) {
                TransformationTranslateF trd = (TransformationTranslateF) tr;
                this.x += trd.x;
                this.y += trd.y;
                this.z += trd.z;
                return true;
            }
            return false;
        }

        @Override
        public int type() {
            return 3;
        }
    }

    private static class TransformationScaleD implements ITransformation {
        private double x;
        private double y;
        private double z;

        private TransformationScaleD(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private TransformationScaleD() {}

        public TransformationScaleD update(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x *= this.x;
            vec3dM.y *= this.y;
            vec3dM.z *= this.z;
        }

        @Override
        public boolean update(ITransformation tr) {
            if (tr.type() == this.type()) {
                TransformationScaleD trd = (TransformationScaleD) tr;
                this.x *= trd.x;
                this.y *= trd.y;
                this.z *= trd.z;
                return true;
            }
            return false;
        }

        @Override
        public int type() {
            return 4;
        }
    }

    private static class TransformationScaleF implements ITransformation {
        private float x;
        private float y;
        private float z;

        private TransformationScaleF(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private TransformationScaleF() {}

        public TransformationScaleF update(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public void applyTransform(Vec3dM vec3dM) {
            vec3dM.x *= this.x;
            vec3dM.y *= this.y;
            vec3dM.z *= this.z;
        }

        @Override
        public boolean update(ITransformation tr) {
            if (tr.type() == this.type()) {
                TransformationScaleF trd = (TransformationScaleF) tr;
                this.x *= trd.x;
                this.y *= trd.y;
                this.z *= trd.z;
                return true;
            }
            return false;
        }

        @Override
        public int type() {
            return 5;
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

        public Vec3dM set(Vec3d vec3d) {
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            return this;
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

        public AxisAlignedBBM set(AxisAlignedBB bb) {
            this.min.x = bb.minX;
            this.min.y = bb.minY;
            this.min.z = bb.minZ;
            this.max.x = bb.maxX;
            this.max.y = bb.maxY;
            this.max.z = bb.maxZ;
            return this;
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