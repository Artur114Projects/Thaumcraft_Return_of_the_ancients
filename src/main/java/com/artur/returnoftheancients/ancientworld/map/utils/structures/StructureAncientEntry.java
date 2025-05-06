package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructureAncientEntry extends StructureBase {
    public StructureAncientEntry(EnumStructure.Rotate rotate, StructurePos pos) {
        super(rotate, EnumStructure.ENTRY, pos);
    }

    protected StructureAncientEntry(StructureAncientEntry parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureAncientEntry(this);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        super.build(world, pos, rand);
    }
}
