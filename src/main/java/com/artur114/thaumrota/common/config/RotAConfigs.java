package com.artur114.thaumrota.common.config;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// TODO Переписать!
@Config(modid = ThaumRotA.MODID)
@Mod.EventBusSubscriber(modid = ThaumRotA.MODID)
public class RotAConfigs {

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal")
    public static PortalSettings PortalSettings = new PortalSettings();

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any")
    @Config.Comment("Any settings")
    public static Any Any = new Any();

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws")
    @Config.Comment("Ancient world settings")
    public static AncientWorldSettings AncientWorldSettings = new AncientWorldSettings();

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.mgs")
    public static CompatibilitySettings CompatibilitySettings = new CompatibilitySettings();

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds")
    @Config.Comment("Here you can change difficulty settings")
    public static DifficultySettings DifficultySettings = new DifficultySettings();

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ps")
    @Config.Comment("Here you can change performance settings")
    public static PerformanceSettings PerformanceSettings = new PerformanceSettings();

    @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.cs")
    @Config.Comment("Here you can change performance settings")
    public static ClientSettings ClientSettings = new ClientSettings();


    public static class ClientSettings {
        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.cs.usi")
        public boolean useStaticImageOnLoadingGui = false;
    }

    public static class DifficultySettings {
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

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ds.as")
        public int incineratorActivationSpeed = 8;
     }

    public static class PortalSettings {
        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.chunkX")
        @Config.Comment("works if notRandomGenerate = true, only affects new worlds")
        public int chunkX = 0;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.chunkZ")
        @Config.Comment("works if notRandomGenerate = true, only affects new worlds")
        public int chunkZ = 0;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.y")
        @Config.Comment("only affects new worlds")
        public int y = -31;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.gr")
        @Config.Comment("works if notRandomGenerate = false, only affects new worlds")
        @Config.RangeInt(min = 0)
        public int generationRange = 1000;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.ig")
        public boolean isGen = true;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.rg")
        public boolean isRandomGenerate = true;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.modId")
        @Config.Comment("Here you can specify with items from which mods you can get into the ancient world")
        public String[] modId = new String[] {ThaumRotA.MODID, "minecraft", "thaumcraft", "baubles"};

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.ci")
        public boolean checkItems = true;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.dwl")
        public int dimensionGenerate = 0;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.portal.dg")
        public int[] dimensionsGenerate = new int[] {0};
    }

    public static class Any {
        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any.primalbladedamage")
        @Config.RequiresMcRestart
        public int primalBladeDamage = 24;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any.primalbladespeed")
        @Config.RequiresMcRestart
        public double primalBladeSpeed = -3.2D;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any.otw")
        public boolean isPrimalBladeOneToWorld = true;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any.mcn")
        public String ModChatName = "TC RETURN OF THE ANCIENTS: ";

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any.dm")
        @Config.RequiresMcRestart
        public boolean debugMode = false;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.any.uog")
        public boolean useOldLoadingGui = false;
    }

    public static class AncientWorldSettings {

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.wgs")
        @Config.Comment("Here you can change ancient world generation settings")
        public AncientWorldGenerationSettings AncientWorldGenerationSettings = new AncientWorldGenerationSettings();

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.np")
        public boolean noPeaceful = true;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.nnv")
        public boolean noNightVision = false;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.daw")
        public boolean isDeadToAncientWorld = false;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.iaf")
        public boolean isAddFogOnAncientWorld = true;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.mpc")
        @Config.RequiresMcRestart
        public int minPlayersCount = 1;
    }

    public static class AncientWorldGenerationSettings {
        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.wgs.tc")
        @Config.Comment("the higher the number, the lower the chance")
        public int turnChance = 1;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.wgs.fc")
        @Config.Comment("the higher the number, the lower the chance")
        public int forkChance = 8;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.wgs.ctr")
        @Config.Comment("the higher the number, the lower the chance")
        public int chanceToReplaceWayToFork = 1;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.wgs.cc")
        @Config.Comment("the higher the number, the lower the chance")
        public int crossroadsChance = 16;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.aws.wgs.nt5")
        public boolean isNeedMoreThan50Fill = true;

        @Config.Comment("the higher the number, the lower the chance")
        public int incineratorGenerateChange = 200;

        @Config.Comment("the higher the number, the lower the chance")
        public int eldritchTrapGenerateChange = 3;
    }

    public static class PerformanceSettings {
        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ps.sgd")
        @Config.Comment("set the value more if the server lags during generation")
        public int structuresGenerationDelay = 1;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ps.nct")
        public int numberSetClearPerTick = 2;

        @Config.LangKey(ThaumRotA.MODID + ".cfg.sub.ps.nrt")
        public int numberSetReloadLightPerTick = 925;
    }

    public static class CompatibilitySettings {
        @Config.RequiresMcRestart
        public int ancientWorldDimId = Integer.MIN_VALUE;
    }


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ThaumRotA.MODID)) {
            ConfigManager.sync(ThaumRotA.MODID, Config.Type.INSTANCE);
        }
    }
}