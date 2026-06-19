package com.artur114.thaumrota.client.util;

import com.artur114.bananalib.math.core.m3d.vec.IVec3IC;
import com.artur114.bananalib.math.m3d.box.Box3I;
import com.artur114.bananalib.math.m3d.box.Box3IM;
import com.artur114.bananalib.math.m3d.box.IBox3I;
import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.bananalib.util.graphs.BananaGraphs;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import com.artur114.thaumrota.main.ThaumRotA;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LightCompressor {

    @Contract("_ -> _")
    public static List<ILightSource> compress(List<ILightSource> light) {
        List<LineLightData> lineData = extractLines(light);
        List<ILightSource> other = extractOther(light);
        other.addAll(mergeLines(lineData));
        light.clear();
        light.addAll(other);
        return light;
    }

    private static List<ILightSource> mergeLines(List<LineLightData> lineData) {
        List<LineLightData> added = new ArrayList<>();
        List<ILightSource> ret = new ArrayList<>();
        for (LineLightData data : lineData) {
            if (added.contains(data)) {
                continue;
            }

            List<LineLightData> toMerge = new ArrayList<>();
            BananaGraphs.bfs(data, (it) -> findCanMerge(lineData, it), (it) -> {toMerge.add(it); return false;});

            if (toMerge.isEmpty()) {
                ret.add(data.self);
                added.add(data);
            } else {
                Box3IM merged = Box3IM.obtain();
                merged.set(data.box);
                for (LineLightData merge : toMerge) {
                    if (added.contains(merge)) {
                        continue;
                    }
                    merged.union(merge.box);
                    added.add(merge);
                }
                added.add(data);

                PosMc3IM from = new PosMc3IM(merged.minX(), merged.minY(), merged.minZ());
                PosMc3IM to = new PosMc3IM(merged.maxX(), merged.maxY(), merged.maxZ());
                ret.add(new LineLightSource(from, to, data.self.color(), data.self.brightness(), data.self.range(), data.self.heat()));

                Box3IM.release(merged);
            }
        }
        return ret;
    }

    private static List<LineLightData> findCanMerge(List<LineLightData> list, LineLightData line) {
        if (!line.isDirect()) return Collections.emptyList();
        List<LineLightData> ret = null;
        IBox3I box = line.box.grow(line.axis);
        for (LineLightData data : list) {
            if (data == line) continue;
            if (data.isDirect() && data.box.intersects(box) && data.axis.equalsEps(line.axis)) {
                if (data.self.brightness() == line.self.brightness() && data.self.heat() == line.self.heat() && data.self.color() == line.self.color() && data.self.range() == line.self.range()) {
                    if (ret == null) ret = new ArrayList<>();
                    ret.add(data);
                }
            }
        }

        if (ret == null) {
            return Collections.emptyList();
        }

        return ret;
    }

    private static List<LineLightData> extractLines(List<ILightSource> light) {
        List<LineLightData> ret = new ArrayList<>();
        for (ILightSource source : light) {
            if (source instanceof LineLightSource) {
                ret.add(new LineLightData((LineLightSource) source));
            }
        }
        return ret;
    }

    private static List<ILightSource> extractOther(List<ILightSource> light) {
        List<ILightSource> ret = new ArrayList<>();
        for (ILightSource source : light) {
            if (!(source instanceof LineLightSource)) {
                ret.add(source);
            }
        }
        return ret;
    }

    private static final class LineLightData {
        private final LineLightSource self;
        private final IVec3D axis;
        private final IBox3I box;

        private LineLightData(LineLightSource self) {
            this.axis = self.to().toImmutable().subtract((IVec3IC) self.from()).normalize().abs();
            this.box = new Box3I(self.from(), self.to());
            this.self = self;
        }

        private boolean isDirect() {
            return (this.axis.x() == 0 || this.axis.x() == 1) && (this.axis.y() == 0 || this.axis.y() == 1) && (this.axis.z() == 0 || this.axis.z() == 1);
        }
    }
}
