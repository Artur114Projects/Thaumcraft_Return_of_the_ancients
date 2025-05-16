package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface IStructureMultiChunk extends IStructure {

    @NotNull List<IStructureSegment> segments();
    @NotNull List<IStructureSegment> segmentsWithPorts();
    void insertSegments(Consumer<IStructureSegment> inserter);

    interface IStructureSegment extends IStructure {
        @NotNull IStructureMultiChunk parent();

        @Override
        @NotNull IStructureSegment copy();
    }
}
