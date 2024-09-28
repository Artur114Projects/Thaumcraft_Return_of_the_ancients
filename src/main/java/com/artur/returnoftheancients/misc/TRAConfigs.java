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

    public static class DifficultySettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.bc")
        public int baseChange = 50;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.io")
        public int ignoringOffset = 25;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.ao")
        public int additionalOffset = 40;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.asb")
        public boolean iaAddSpeedEffectToBoss = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.sa")
        public int speedAmplifier = 2;
     }

    public static class PortalSettings {

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.x")
        @Config.Comment("only affects new worlds")
        public int x = 5;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.z")
        @Config.Comment("only affects new worlds")
        public int z = 5;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.y")
        @Config.Comment("only affects new worlds")
        public int y = -31;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.isgen")
        public boolean isGen = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.tobedrock")
        public boolean toBedrock = true;

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
        public boolean noNightVision = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.daw")
        public boolean isDeadToAncientWorld = false;

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

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.sgd")
        @Config.Comment("set the value more if the server lags during generation")
        public int structuresGenerationDelay2 = 1;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.nct")
        public int numberSetClearPerTick2 = 2;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.nrt")
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