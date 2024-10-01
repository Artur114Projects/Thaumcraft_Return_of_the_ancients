package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Referense.MODID)
@Mod.EventBusSubscriber(modid = Referense.MODID)
public class TRAConfigs {

    @Config.LangKey(Referense.MODID + ".cfg.sub.portal")
    @Config.Comment("Here you can change item description")
    public static PortalSettings PortalSettings = new PortalSettings();

    @Config.LangKey(Referense.MODID + ".cfg.sub.any")
    @Config.Comment("Any settings")
    public static Any Any = new Any();

    @Config.LangKey(Referense.MODID + ".cfg.sub.aws")
    @Config.Comment("Ancient world settings")
    public static AncientWorldSettings AncientWorldSettings = new AncientWorldSettings();

    @Config.LangKey(Referense.MODID + ".cfg.sub.mgs")
    @Config.Comment("(Don`t work)")
    public static MobGenSettings MobGenSettings = new MobGenSettings();

    @Config.LangKey(Referense.MODID + ".cfg.sub.ds")
    @Config.Comment("Here you can change difficulty settings")
    public static DifficultySettings DifficultySettings = new DifficultySettings();

    @Config.LangKey(Referense.MODID + ".cfg.sub.ps")
    @Config.Comment("Here you can change performance settings")
    public static PerformanceSettings PerformanceSettings = new PerformanceSettings();

    @Config.LangKey(Referense.MODID + ".cfg.sub.cs")
    @Config.Comment("Here you can change performance settings")
    public static ClientSettings ClientSettings = new ClientSettings();


    public static class ClientSettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.cs.usi")
        public boolean useStaticImageOnLoadingGui = false;
    }

    public static class DifficultySettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.bc")
        public int baseChange = 50;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.io")
        public int ignoringOffset = 25;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.ao")
        @Config.Comment("The higher the value, the lower the chance of armor penetration.")
        @Config.RangeDouble(min = -2.0D, max = 2.0D)
        public double additionalOffset = 0.0D;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.asb")
        public boolean iaAddSpeedEffectToBoss = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.sa")
        public int speedAmplifier = 2;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.as")
        public int incineratorActivationSpeed = 8;
     }

    public static class PortalSettings {

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.chunkX")
        @Config.Comment("works if notRandomGenerate = true, only affects new worlds")
        public int chunkX = 0;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.chunkZ")
        @Config.Comment("works if notRandomGenerate = true, only affects new worlds")
        public int chunkZ = 0;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.y")
        @Config.Comment("only affects new worlds")
        public int y = -31;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.gr")
        @Config.Comment("works if notRandomGenerate = false, only affects new worlds")
        @Config.RangeInt(min = 0)
        public int generationRange = 1000;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.ig")
        public boolean isGen = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.rg")
        public boolean isRandomGenerate = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.modId")
        @Config.Comment("Here you can specify with items from which mods you can get into the ancient world")
        public String[] modId = new String[] {Referense.MODID, "minecraft", "thaumcraft", "baubles"};

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.ci")
        public boolean checkItems = true;
    }

    public static class Any {
        @Config.LangKey(Referense.MODID + ".cfg.sub.any.primalbladedamage")
        @Config.RequiresMcRestart
        public int primalBladeDamage = 24;

        @Config.LangKey(Referense.MODID + ".cfg.sub.any.primalbladespeed")
        @Config.RequiresMcRestart
        public double primalBladeSpeed = -3.2D;

        @Config.LangKey(Referense.MODID + ".cfg.sub.any.mcn")
        public String ModChatName = "TC RETURN OF THE ANCIENTS: ";

        @Config.LangKey(Referense.MODID + ".cfg.sub.any.dm")
        @Config.RequiresMcRestart
        public boolean debugMode = false;
    }

    public static class AncientWorldSettings {

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs")
        @Config.Comment("Here you can change ancient world generation settings")
        public AncientWorldGenerationSettings AncientWorldGenerationSettings = new AncientWorldGenerationSettings();

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.np")
        public boolean noPeaceful = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.nnv")
        public boolean noNightVision = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.daw")
        public boolean isDeadToAncientWorld = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.iaf")
        public boolean isAddFogOnAncientWorld = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.mpc")
        @Config.RequiresMcRestart
        public int minPlayersCount = 1;
    }

    public static class AncientWorldGenerationSettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.tc")
        @Config.Comment("the higher the number, the lower the chance")
        public int turnChance = 1;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.fc")
        @Config.Comment("the higher the number, the lower the chance")
        public int forkChance = 8;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.ctr")
        @Config.Comment("the higher the number, the lower the chance")
        public int chanceToReplaceWayToFork = 1;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.cc")
        @Config.Comment("the higher the number, the lower the chance")
        public int crossroadsChance = 16;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.nt5")
        public boolean isNeedMoreThan50Fill = true;

        @Config.Comment("the higher the number, the lower the chance")
        public int incineratorGenerateChange = 200;

        @Config.Comment("the higher the number, the lower the chance")
        public int eldritchTrapGenerateChange = 3;
    }
    public static class PerformanceSettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.ps.sgd")
        @Config.Comment("set the value more if the server lags during generation")
        public int structuresGenerationDelay = 1;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ps.nct")
        public int numberSetClearPerTick = 2;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ps.nrt")
        public int numberSetReloadLightPerTick = 925;
    }


    public static class MobGenSettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.c_c")
        @Config.Comment({"EntityInhabitedZombie = 1", "EntityEldritchGuardian = 2", "EntityMindSpider = 3"})
        public int[] CROSSROADS_CHANGE = new int[] {0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.c_g")
        @Config.Comment({"EntityInhabitedZombie: 1 = min, 2 = max", "EntityEldritchGuardian: 3 = min, 4 = max", "EntityMindSpider: 5 = min, 6 = max"})
        public int[] CROSSROADS_GROUP = new int[] {0, 0, 0, 0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.w_c")
        @Config.Comment({"EntityInhabitedZombie = 1", "EntityEldritchGuardian = 2", "EntityMindSpider = 3"})
        public int[] WAY_CHANGE = new int[] {0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.w_g")
        @Config.Comment({"EntityInhabitedZombie: 1 = min, 2 = max", "EntityEldritchGuardian: 3 = min, 4 = max", "EntityMindSpider: 5 = min, 6 = max"})
        public int[] WAY_GROUP = new int[] {0, 0, 0, 0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.f_c")
        @Config.Comment({"EntityInhabitedZombie = 1", "EntityEldritchGuardian = 2", "EntityMindSpider = 3"})
        public int[] FORK_CHANGE = new int[] {0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.f_g")
        @Config.Comment({"EntityInhabitedZombie: 1 = min, 2 = max", "EntityEldritchGuardian: 3 = min, 4 = max", "EntityMindSpider: 5 = min, 6 = max"})
        public int[] FORK_GROUP = new int[] {0, 0, 0, 0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.t_c")
        @Config.Comment({"EntityInhabitedZombie = 1", "EntityEldritchGuardian = 2", "EntityMindSpider = 3"})
        public int[] TURN_CHANGE = new int[] {0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.t_g")
        @Config.Comment({"EntityInhabitedZombie: 1 = min, 2 = max", "EntityEldritchGuardian: 3 = min, 4 = max", "EntityMindSpider: 5 = min, 6 = max"})
        public int[] TURN_GROUP = new int[] {0, 0, 0, 0, 0, 0};

        @Config.LangKey(Referense.MODID + ".cfg.sub.mgs.umg")
        @Config.Comment("(false)")
        public boolean useNewMobGeneration = false;
    }


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Referense.MODID)) {
            ConfigManager.sync(Referense.MODID, Config.Type.INSTANCE);
        }
    }
}