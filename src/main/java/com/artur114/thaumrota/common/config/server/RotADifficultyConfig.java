package com.artur114.thaumrota.common.config.server;

import net.minecraftforge.common.config.Config;

public class RotADifficultyConfig {
    @Config.LangKey("thaumrota.cfg.server.diff.daw")
    public boolean canDeadInAncientWorld = false;

    @Config.RangeDouble(min = 0)
    @Config.LangKey("thaumrota.cfg.server.diff.dm")
    public double damageMultiplier = 125.0D;

    @Config.RangeDouble(min = 0)
    @Config.LangKey("thaumrota.cfg.server.diff.mmd")
    public double minimalMobDamage = 2.5D;

    @Config.RangeDouble(min = 0)
    @Config.LangKey("thaumrota.cfg.server.diff.mfd")
    public double minimalFireDamage = 0.5D;
}
