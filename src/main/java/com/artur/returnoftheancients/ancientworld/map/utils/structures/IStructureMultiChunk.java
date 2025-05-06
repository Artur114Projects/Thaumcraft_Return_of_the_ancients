package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IStructureMultiChunk extends IStructure {

    void insertSegments(Consumer<IStructureSegment> inserter);

    interface IStructureSegment extends IStructure {
        @NotNull IStructureMultiChunk parent();

        @Override
        @NotNull IStructureSegment copy();
    }
}
