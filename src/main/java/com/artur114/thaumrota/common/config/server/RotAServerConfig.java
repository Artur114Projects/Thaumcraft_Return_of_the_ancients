package com.artur114.thaumrota.common.config.server;

import net.minecraftforge.common.config.Config;

public class RotAServerConfig {
    @Config.LangKey("thaumrota.cfg.server.compat")
    public RotACompatConfig compat = new RotACompatConfig();
    
    @Config.LangKey("thaumrota.cfg.server.diff")
    public RotADifficultyConfig difficulty = new RotADifficultyConfig();
    
    @Config.LangKey("thaumrota.cfg.server.daw")
    public boolean canDeadInAncientWorld = false;
}
