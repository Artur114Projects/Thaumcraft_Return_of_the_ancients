package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructureEntry extends StructureMultiChunk {
    public StructureEntry(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumMultiChunkStrType.ENTRY, pos);

        this.y = 87;
    }

    protected StructureEntry(StructureEntry parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureEntry(this);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        super.build(world, pos, rand);
    }

    @Override
    protected char[][] structureForm() {
        return new char[][] {
            {' ',' ','p',' ',' '},
            {' ','s','s','s',' '},
            {'p','s','c','s','p'},
            {' ','s','s','s',' '},
            {' ',' ','p',' ',' '}
        };
    }
}
