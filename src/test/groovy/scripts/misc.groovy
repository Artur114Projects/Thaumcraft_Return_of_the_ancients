
package scripts

import com.artur114.bananalib.math.core.m3d.vec.IVec3IC
import com.artur114.bananalib.math.m3d.box.Box3I
import com.artur114.bananalib.math.m3d.box.Box3IM
import com.artur114.bananalib.math.m3d.box.IBox3I
import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.math.m3d.vec.Vec3D
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.bananalib.util.graphs.BananaGraphs
import com.artur114.thaumrota.client.light.ILightSource
import com.artur114.thaumrota.client.light.LineLightSource
import com.artur114.thaumrota.client.light.LineLightSourceD
import com.artur114.thaumrota.client.util.LightCompressor
import groovy.transform.BaseScript
import org.jetbrains.annotations.Contract

@BaseScript
RotADevScript script

compress(lightIn)

List<ILightSource> compress(List<ILightSource> light) {
    List<LineLightData> lineData = extractLinesData(light);
    List<ILightSource> other = extractOther(light);
    lineData = mergeLinesP1(lineData);
    lineData = mergeLinesP2(lineData);
    other.addAll(extractLinesFromData(lineData));
    light.clear();
    light.addAll(other);
    return light;
}

private List<LineLightData> mergeLinesP2(List<LineLightData> lineData) {
    List<LineLightData> added = new ArrayList<>();
    List<LineLightData> ret = new ArrayList<>();

    for (LineLightData data : lineData) {
        if (added.contains(data)) {
            continue;
        }

        List<LineLightData> toMerge = findCanMergeP2(lineData, data)

        if (toMerge.isEmpty()) {
            ret.add(data);
            added.add(data)
        } else {
            LineLightData line = toMerge[0]

            Box3IM merged = Box3IM.obtain();
            merged.set(data.box)
            merged.union(line.box)
            added.add(line)
            added.add(data)

            IVec3D axis = data.axis
            int dx = 0, dy = 0, dz = 0
            if (axis.x() == 0) dx = 1
            if (axis.y() == 0) dy = 1
            if (axis.z() == 0) dz = 1

            Vec3DM from = new Vec3DM()
            Vec3DM to = new Vec3DM()

            from.set(
                merged.minX() + ((merged.maxX() - merged.minX()) / 2) * dx,
                merged.minY() + ((merged.maxY() - merged.minY()) / 2) * dy,
                merged.minZ() + ((merged.maxZ() - merged.minZ()) / 2) * dz
            )

            to.set(
                merged.maxX() - ((merged.maxX() - merged.minX()) / 2) * dx,
                merged.maxY() - ((merged.maxY() - merged.minY()) / 2) * dy,
                merged.maxZ() - ((merged.maxZ() - merged.minZ()) / 2) * dz
            )

            ret.add(new LineLightData(new LineLightSourceD(from, to, data.self.color(), (data.self.brightness() * 2.0F) as float, data.self.range() + 0.5F as float, (data.self.heat() * 2.0F) as float)));

            Box3IM.release(merged)
        }
    }

    return ret;
}

private List<LineLightData> findCanMergeP2(List<LineLightData> list, LineLightData line) {
    if (!line.isDirect()) return Collections.emptyList();
    List<LineLightData> ret = null;
    IVec3D axis = line.axis
    int dx = 0, dy = 0, dz = 0
    if (axis.x() == 0) dx = 1
    if (axis.y() == 0) dy = 1
    if (axis.z() == 0) dz = 1
    List<Box3I> boxes = []

    if (dx != 0) boxes << line.box.grow(dx, 0, 0)
    if (dy != 0) boxes << line.box.grow(0, dy, 0)
    if (dz != 0) boxes << line.box.grow(0, 0, dz)

    for (LineLightData data : list) {
        if (data == line) continue
        if (data.isDirect() && data.axis.equalsEps(line.axis)) {
            if (data.self.brightness() == line.self.brightness() && data.self.heat() == line.self.heat() && data.self.color().equals(line.self.color()) && data.self.range() == line.self.range()) {
                if (boxes.any{it.contains(data.box)}) {
                    if (ret == null) ret = new ArrayList<>();
                    ret.add(data);
                }
            }
        }
    }

    if (ret == null) {
        return Collections.emptyList();
    }

    return ret;
}

private List<LineLightData> mergeLinesP1(List<LineLightData> lineData) {
    List<LineLightData> added = new ArrayList<>();
    List<LineLightData> ret = new ArrayList<>();
    for (LineLightData data : lineData) {
        if (added.contains(data)) {
            continue;
        }

        List<LineLightData> toMerge = new ArrayList<>()
        BananaGraphs.bfs(data, {findCanMergeP1(lineData, it)}, {
            toMerge << it; return false
        })

        if (toMerge.isEmpty()) {
            ret.add(data);
            added.add(data)
        } else {
            Box3IM merged = Box3IM.obtain();
            merged.set(data.box)
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
            ret.add(new LineLightData(new LineLightSource(from, to, data.self.color(), data.self.brightness(), data.self.range(), data.self.heat())));

            Box3IM.release(merged);
        }
    }
    return ret;
}

private List<LineLightData> findCanMergeP1(List<LineLightData> list, LineLightData line) {
    if (!line.isDirect()) return Collections.emptyList();
    List<LineLightData> ret = null;
    IBox3I box = line.box.grow(line.axis);
    for (LineLightData data : list) {
        if (data == line) continue;
        if (data.isDirect() && data.box.intersects(box) && data.axis.equalsEps(line.axis)) {
            if (data.self.brightness() == line.self.brightness() && data.self.heat() == line.self.heat() && data.self.color().equals(line.self.color()) && data.self.range() == line.self.range()) {
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


private List<ILightSource> extractLinesFromData(List<LineLightData> light) {
    return light.collect {it.self}
}

private List<LineLightData> extractLinesData(List<ILightSource> light) {
    List<LineLightData> ret = new ArrayList<>();
    for (ILightSource source : light) {
        if (source instanceof LineLightSource) {
            ret.add(new LineLightData((LineLightSource) source));
        }
    }
    return ret
}

private List<ILightSource> extractOther(List<ILightSource> light) {
    List<ILightSource> ret = new ArrayList<>();
    for (ILightSource source : light) {
        if (!(source instanceof LineLightSource)) {
            ret.add(source);
        }
    }
    return ret;
}

final class LineLightData {
    public final ILightSource self;
    public final IVec3D axis;
    public final IBox3I box;

    LineLightData(LineLightSource self) {
        this.axis = self.to().toImmutable().subtract((IVec3IC) self.from()).normalize().abs();
        this.box = new Box3I(self.from(), self.to());
        this.self = self;
    }

    LineLightData(LineLightSourceD self) {
        this.axis = self.to().toImmutable().subtract(self.from()).normalize().abs();
        this.box = new Box3I(self.from(), self.to());
        this.self = self;
    }

    boolean isDirect() {
        return (this.axis.x() == 0 || this.axis.x() == 1) && (this.axis.y() == 0 || this.axis.y() == 1) && (this.axis.z() == 0 || this.axis.z() == 1);
    }
}