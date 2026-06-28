package com.artur114.thaumrota.common.worldstate.ancientworld.map.gen;

import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.*;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.ImmutableMap;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.IStructure;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.StructureLongRoom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenPhaseLongRooms extends GenPhase {

    public GenPhaseLongRooms(GenPhase parent) {
        super(parent);
    }

    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        ImmutableMap map = this.parent.getMap(seed, size);

        this.replaceWaysToLongRooms(map);
        this.addSecretRoom(map, seed);

        return map;
    }

    private void replaceWaysToLongRooms(ImmutableMap map) {
        List<IStructure> strs = new ArrayList<>();
        int size = map.size();

        for (int i = 0; i != size * size; i++) {
            int x = i % size, y = i / size;
            IStructure str = map.structure(x, y);

            if (str != null && str.type() == EnumStructureType.WAY) {
                IStructure structure = this.tryToCreateStr(str, map);

                if (structure != null) {
                    strs.add(structure);
                }
            }
        }

        strs.forEach(map::insetStructure);
    }

    private @Nullable IStructure tryToCreateStr(IStructure way, ImmutableMap map) {
        IBox2I box = EnumMultiChunkStrType.LONG_ROOM.form().box(way.rotate());

        for (int x = box.minX(); x != box.maxX() + 1; x++) {
            for (int y = box.minY(); y != box.maxY() + 1; y++) {
                IStructure str = map.structure(x, y);
                if (str == null || str.type() != EnumStructureType.WAY || str.rotate() != way.rotate()) {
                    return null;
                }
            }
        }

        return EnumMultiChunkStrType.LONG_ROOM.create(way.rotate(), way.pos());
    }

    private void addSecretRoom(ImmutableMap map, long seed) {
        List<StructureLongRoom> rooms = new ArrayList<>();
        Random rand = new Random(seed);
        int size = map.size();

        for (int i = 0; i != size * size; i++) {
            int x = i % size, y = i / size;
            IStructure str = map.structure(x, y);
            if (str instanceof StructureLongRoom && str.rotate() == EnumRotate.C90) {
                rooms.add((StructureLongRoom) str);
            }
        }

        if (!rooms.isEmpty()) {
            rooms.get(rand.nextInt(rooms.size())).setSecret();
        }
    }
}
