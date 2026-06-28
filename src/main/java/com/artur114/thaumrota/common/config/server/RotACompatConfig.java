package com.artur114.thaumrota.common.config.server;

import net.minecraftforge.common.config.Config;

public class RotACompatConfig {
    @Config.RequiresMcRestart
    @Config.LangKey("thaumrota.cfg.server.compat.awi")
    public int ancientWorldDimId = -932;

    @Config.RequiresWorldRestart
    @Config.LangKey("thaumrota.cfg.server.compat.ibg")
    public boolean doInterceptBiomeGen = true;
}
