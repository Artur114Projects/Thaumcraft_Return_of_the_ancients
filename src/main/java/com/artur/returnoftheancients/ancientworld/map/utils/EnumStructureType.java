package com.artur.returnoftheancients.ancientworld.map.utils;

import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureBase;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureLadder;
import com.artur.returnoftheancients.util.interfaces.Function3;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public enum EnumStructureType implements IStructureType {
    FORK("ancient_fork_rotate-", EnumRotate.C270, EnumFace.UP, EnumFace.LEFT, EnumFace.RIGHT),
    LADDER("ancient_ladder_rotate-", EnumRotate.C270, (rot, pos, type) -> new StructureLadder(rot, pos), EnumFace.RIGHT, EnumFace.LEFT),
    WAY("ancient_way_rotate-", EnumRotate.C90, EnumFace.RIGHT, EnumFace.LEFT),
    TURN("ancient_turn_rotate-", EnumRotate.C270, EnumFace.UP, EnumFace.RIGHT),
    CROSSROADS("ancient_crossroads", EnumRotate.NON, EnumFace.values()),
    END("ancient_end_rotate-", EnumRotate.C270, EnumFace.LEFT);

    private final Function3<EnumRotate, StrPos, EnumStructureType, IStructure> creator;
    private final Map<EnumRotate, Set<EnumFace>> ports;
    private final String id;

    EnumStructureType(String id, EnumRotate maxRotate, Function3<EnumRotate, StrPos, EnumStructureType, IStructure> creator, EnumFace... ports) {
        this.creator = creator;
        this.id = id;

        this.ports = this.compilePorts(ports, maxRotate);
    }

    EnumStructureType(String id, EnumRotate rotate, EnumFace... ports) {
        this(id, rotate, (rot, pos, type) -> new StructureBase(rot, type, pos), ports);
    }

    @Override
    public IStructure create(EnumRotate rotate, StrPos pos) {
        return this.creator.apply(rotate, pos, this);
    }

    @Override
    public String stringId(EnumRotate rotate) {
        return this.id.endsWith("-") ? this.id + rotate.id : this.id;
    }

    public EnumRotate rotateFromPorts(Collection<EnumFace> ports) {
        AtomicReference<EnumRotate> ret = new AtomicReference<>(null);

        this.ports.forEach(((rotate, enumFaces) -> {
            if (ret.get() == null && enumFaces.containsAll(ports)) {
                ret.set(rotate);
            }
        }));

        return ret.get();
    }

    public Set<EnumFace> ports(EnumRotate rotate) {
        return this.ports.get(rotate);
    }

    private Map<EnumRotate, Set<EnumFace>> compilePorts(EnumFace[] defPorts, EnumRotate maxRotate) {
        Map<EnumRotate, Set<EnumFace>> ret = new HashMap<>();

        for (EnumRotate rotate : EnumRotate.values()) {
            ret.put(rotate, new HashSet<>(Arrays.asList(EnumFace.rotateAll(rotate, defPorts))));
            if (rotate == maxRotate) {
                break;
            }
        }

        return ret;
    }
}
