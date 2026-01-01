package com.artur.returnoftheancients.ancientworld.map.utils;

import java.util.*;

public abstract class MultiChunkStrForm implements IMultiChunkStrForm {
    private final Map<StrPos, Map<EnumRotate, IOffset[]>> offsets = new HashMap<>();
    private final Map<EnumRotate, IOffset[]> zeroOffsets;
    private final char[][] rawForm;

    public MultiChunkStrForm() {
        this.rawForm = this.form();

        this.zeroOffsets = this.initZeroOffsets();
    }

    public abstract char[][] form();

    @Override
    public IOffset[] offsets() {
        return Arrays.copyOf(this.zeroOffsets.get(EnumRotate.NON), this.zeroOffsets.get(EnumRotate.NON).length);
    }

    @Override
    public IOffset[] offsets(StrPos center) {
        center = center.toImmutable();

        if (this.offsets.containsKey(center)) {
            IOffset[] o = this.offsets.get(center).get(EnumRotate.NON);
            if (o != null) return o;
        }

        IOffset[] zero = this.zeroOffsets.get(EnumRotate.NON);
        IOffset[] ret = new IOffset[zero.length];

        for (int i = 0; i != ret.length; i++) {
            IOffset offset = zero[i];
            ret[i] = new Offset(center, offset.localPos(), offset.ports(), offset.voidCollides());
        }

        if (this.offsets.containsKey(center)) {
            this.offsets.get(center).put(EnumRotate.NON, ret);
        } else {
            Map<EnumRotate, IOffset[]> map = new HashMap<>();
            map.put(EnumRotate.NON, ret);
            this.offsets.put(center, map);
        }

        return Arrays.copyOf(ret, ret.length);
    }

    @Override
    public IOffset[] offsets(EnumRotate rotate) {
        IOffset[] ret = this.zeroOffsets.get(rotate);
        return Arrays.copyOf(ret, ret.length);
    }

    @Override
    public IOffset[] offsets(StrPos center, EnumRotate rotate) {
        center = center.toImmutable();

        if (this.offsets.containsKey(center)) {
            IOffset[] o = this.offsets.get(center).get(rotate);
            if (o != null) return o;
        }

        IOffset[] zero = this.zeroOffsets.get(rotate);
        IOffset[] ret = new IOffset[zero.length];

        for (int i = 0; i != ret.length; i++) {
            IOffset offset = zero[i];
            ret[i] = new Offset(center, offset.localPos(), offset.ports(), offset.voidCollides());
        }

        if (this.offsets.containsKey(center)) {
            this.offsets.get(center).put(rotate, ret);
        } else {
            Map<EnumRotate, IOffset[]> map = new HashMap<>();
            map.put(rotate, ret);
            this.offsets.put(center, map);
        }

        return Arrays.copyOf(ret, ret.length);
    }

    private Map<EnumRotate, IOffset[]> initZeroOffsets() {
        char[][] form = this.rawForm;

        for (int i = 0; i != form.length; i++) {
            if (form[0].length != form[i].length) {
                throw new IllegalArgumentException();
            }
        }

        int centerX = -1;
        int centerY = -1;
        for (int y = 0; y != form.length; y++) {
            for (int x = 0; x != form[y].length; x++) {
                if (form[y][x] == 'c') {
                    if (centerY == -1) {
                        centerX = x;
                        centerY = y;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        if (centerY == -1) {
            throw new IllegalArgumentException();
        }

        List<IOffset> offsets = new ArrayList<>(form.length * form[0].length);
        StrPos c = new StrPos(0, 0);

        for (int y = 0; y != form.length; y++) {
            for (int x = 0; x != form[y].length; x++) {
                if (form[y][x] == 's') {
                    EnumFace[] ports = this.getPortsFromForm(x, y, form).toArray(new EnumFace[0]);
                    EnumFace[] voidCollides = this.getVoidCollidesFromForm(x, y, form).toArray(new EnumFace[0]);
                    StrPos pos = new StrPos(x - centerX, y - centerY);
                    offsets.add(new Offset(c, pos, ports, voidCollides));
                }
            }
        }

        Map<EnumRotate, IOffset[]> ret = new HashMap<>();

        for (EnumRotate rotate : EnumRotate.values()) {
            List<IOffset> off = new ArrayList<>();
            for (IOffset offset : offsets) {
                off.add(new Offset(offset.centerPos(), offset.localPos().rotate(rotate), EnumFace.rotateAll(rotate, offset.ports()), EnumFace.rotateAll(rotate, offset.voidCollides())));
            }
            ret.put(rotate, off.toArray(off.toArray(new IOffset[0])));
        }

        return ret;
    }

    protected List<EnumFace> getPortsFromForm(int x, int y, char[][] form) {
        List<EnumFace> faces = new ArrayList<>(4);
        if (x - 1 >= 0 && form[y][x - 1] == 'p') {
            faces.add(EnumFace.LEFT);
        }
        if (x + 1 < form[y].length && form[y][x + 1] == 'p') {
            faces.add(EnumFace.RIGHT);
        }
        if (y - 1 >= 0 && form[y - 1][x] == 'p') {
            faces.add(EnumFace.UP);
        }
        if (y + 1 < form.length && form[y + 1][x] == 'p') {
            faces.add(EnumFace.DOWN);
        }
        return faces;
    }

    protected List<EnumFace> getVoidCollidesFromForm(int x, int y, char[][] form) {
        List<EnumFace> faces = new ArrayList<>(4);
        if (x - 1 >= 0) {
            char c = form[y][x - 1];
            if (c == 'p' || c == ' ') {
                faces.add(EnumFace.LEFT);
            }
        }
        if (x + 1 < form[y].length) {
            char c = form[y][x + 1];
            if (c == 'p' || c == ' ') {
                faces.add(EnumFace.RIGHT);
            }
        }
        if (y - 1 >= 0) {
            char c = form[y - 1][x];
            if (c == 'p' || c == ' ') {
                faces.add(EnumFace.UP);
            }
        }
        if (y + 1 < form.length) {
            char c = form[y + 1][x];
            if (c == 'p' || c == ' ') {
                faces.add(EnumFace.DOWN);
            }
        }
        return faces;
    }

    public static class Offset implements IOffset {
        private final EnumFace[] voidCollides;
        private final EnumFace[] ports;
        private final StrPos globalPos;
        private final StrPos localPos;
        private final StrPos center;
        public Offset(StrPos center, StrPos localPos, EnumFace[] ports, EnumFace[] voidCollides) {
            this.globalPos = center.add(localPos);
            this.voidCollides = voidCollides;
            this.localPos = localPos;
            this.center = center;
            this.ports = ports;
        }

        @Override
        public EnumFace[] voidCollides() {
            return Arrays.copyOf(this.voidCollides, this.voidCollides.length);
        }

        @Override
        public EnumFace[] ports() {
            return Arrays.copyOf(this.ports, this.ports.length);
        }

        @Override
        public StrPos centerPos() {
            return this.center;
        }

        @Override
        public StrPos globalPos() {
            return this.globalPos;
        }

        @Override
        public StrPos localPos() {
            return this.localPos;
        }

        @Override
        public int hashCode() {
            return globalPos.hashCode();
        }

        @Override
        public String toString() {
            return "Global:" + this.globalPos.toString() + " Local:" + this.localPos.toString() + " Center:" + this.center + " Ports:" + Arrays.toString(this.ports);
        }
    }
}
