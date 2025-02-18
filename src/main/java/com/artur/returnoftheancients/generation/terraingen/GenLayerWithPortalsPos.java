package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.layer.GenLayer;

public abstract class GenLayerWithPortalsPos extends GenLayer {
    protected final ChunkPos[] portalsGenerationPos;
    public GenLayerWithPortalsPos(long seed, long worldSeed, GenLayer parent) {
        super(seed);
        portalsGenerationPos = new ChunkPos[AncientPortalsProcessor.portalsCount];
        GenLayersHandler.initPortalsPosOnWorld(portalsGenerationPos, worldSeed);
        this.parent = parent;
    }
}
