package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumFace;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.IStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GenPhasePolishing extends GenPhase {
    private final Map<Integer, EnumStructureType> replacingMap;

    public GenPhasePolishing(GenPhase parent) {
        super(parent);

        this.replacingMap = this.initReplacingMap();
    }

    private Map<Integer, EnumStructureType> initReplacingMap() {
        Map<Integer, EnumStructureType> map = new HashMap<>();
        map.put(1, EnumStructureType.END);
        map.put(2, EnumStructureType.TURN);
        map.put(3, EnumStructureType.FORK);
        map.put(4, EnumStructureType.CROSSROADS);
        return map;
    }

    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        ImmutableMap map = this.parent.getMap(seed, size);
        StrPos.MutableStrPos pos = new StrPos.MutableStrPos();
        Random rand = new Random(seed);

        for (int y = 0; y != map.size(); y++) {
            for (int x = 0; x != map.size(); x++) {
                IStructure str = map.structure(x, y);

                if (str == null) {
                    continue;
                }

                for (EnumFace face : str.ports()) {
                    pos.setPos(x, y).offset(face);

                    IStructure str1 = map.structure(pos);

                    if (str1 != null && !str1.canConnect(face.getOppose())) {
                        this.fixStructuresConnection(map, str.pos(), str1.pos(), face, seed);
                    }
                }
            }
        }

        return map;
    }

    private void fixStructuresConnection(ImmutableMap map, StrPos strPos, StrPos str1Pos, EnumFace badPort, long seed) {
        IStructure str = map.structure(strPos);
        IStructure str1 = map.structure(str1Pos);

        if (str == null || str1 == null) {
            return;
        }

        if ((str.canConnect(badPort) && str1.canConnect(badPort.getOppose())) || (!str.canConnect(badPort) && !str1.canConnect(badPort.getOppose()))) {
            return;
        }

        if (!str.canReplace() && !str1.canReplace()) {
            throw new IllegalStateException("Illegal state. Two nearby, immutable structures with blocked ports. Str1:" + strPos + ", Str2:" + str1Pos + ", Seed:" + seed);
        }

        if (str1.canReplace()) {
            this.replaceSecondStructure(map, str, str1, badPort);
        } else {
            this.replaceMainStructure(map, str, str1, badPort);
        }
    }

    private void replaceMainStructure(ImmutableMap map, IStructure str, IStructure str1, EnumFace badPort) {
        List<EnumFace> ports = new ArrayList<>(Arrays.asList(str.ports()));
        ports.remove(badPort);
        map.insetStructure(this.createStructure(ports, str.pos()));
    }

    private void replaceSecondStructure(ImmutableMap map, IStructure str, IStructure str1, EnumFace badPort) {
        List<EnumFace> ports = new ArrayList<>(Arrays.asList(str1.ports()));
        ports.add(badPort.getOppose());
        map.insetStructure(this.createStructure(ports, str1.pos()));
    }

    private IStructure createStructure(List<EnumFace> ports, StrPos pos) {
        if (ports.size() == 2 && ports.get(0).getOppose() == ports.get(1)) {
            return EnumStructureType.WAY.create(EnumStructureType.WAY.rotateFromPorts(ports), pos);
        }
        EnumStructureType type = this.replacingMap.get(ports.size());
        return type.create(type.rotateFromPorts(ports), pos);
    }
}
