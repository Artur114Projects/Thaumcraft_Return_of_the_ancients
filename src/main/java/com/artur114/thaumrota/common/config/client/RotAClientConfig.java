package com.artur114.thaumrota.common.config.client;

import net.minecraftforge.common.config.Config;

public class RotAClientConfig {
    @Config.LangKey("thaumrota.cfg.client.fxq")
    public EnumFXQuality graphicQuality = EnumFXQuality.HIGH;

    @Config.LangKey("thaumrota.cfg.client.irt")
    public boolean doInfinityRainInTaintBiome = true;

    @Config.LangKey("thaumrota.cfg.client.dst")
    public boolean doDisableSunInTaintBiome = true;

    @Config.LangKey("thaumrota.cfg.client.igs")
    public boolean doInterceptGammaSetting = true;
}