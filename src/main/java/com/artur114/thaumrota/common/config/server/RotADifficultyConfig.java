package com.artur114.thaumrota.common.config.server;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraftforge.common.config.Config;

public class RotADifficultyConfig {
    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds.bc")
    public int baseChange = 50;

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds.io")
    public int ignoringOffset = 25;

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds.ao")
    @Config.Comment("The higher the value, the lower the chance of armor penetration.")
    @Config.RangeDouble(min = -2.0D, max = 2.0D)
    public double additionalOffset = 0.0D;

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds.asb")
    public boolean iaAddSpeedEffectToBoss = false;

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds.sa")
    public int speedAmplifier = 2;
}
