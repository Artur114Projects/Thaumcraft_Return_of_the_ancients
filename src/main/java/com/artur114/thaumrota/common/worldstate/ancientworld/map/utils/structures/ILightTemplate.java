package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.thaumrota.client.light.ILightSource;

import java.util.List;

public interface ILightTemplate {
    List<ILightSource> createLights();
}
