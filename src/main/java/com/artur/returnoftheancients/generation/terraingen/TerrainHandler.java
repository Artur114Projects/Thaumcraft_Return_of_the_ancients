package com.artur.returnoftheancients.generation.terraingen;


import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TerrainHandler {
    @SubscribeEvent
    public void initializeAllBiomeGenerators(InitBiomeGens event) {
        GenLayer[] genlayer = GenLayerTaint.initializeAllBiomeGenerators(event.getSeed(), event.getWorldType());
        event.setNewBiomeGens(genlayer);
    }
}