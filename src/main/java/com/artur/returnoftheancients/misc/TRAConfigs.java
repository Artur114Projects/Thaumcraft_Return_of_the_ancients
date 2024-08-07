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

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.cia")
        @Config.Comment("the higher the number, the lower the chance")
        public int chanceIgnoringArmor = 1;


        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.asb")
        public boolean iaAddSpeedEffectToBoss = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.ds.pcd")
        @Config.Comment({"List effects, the order of the lines does not matter, if you want to disable this mechanics, remove all lines", "example line: players=[number of players, max 99], ", "",
                "effect=[resistance or regeneration or invisibility or strength or fireResistance], ", "",
                "amplifier=[number max max potion effect amplifier or ", "",
                "[number by which the number of players is divided, the result is the potion level, max 9][p][maximum potion level that will be assigned, max 9] or ", "",
                "[every second a number is selected min 0 max the number that you enter if it is equal to 0 then the effect is assigned, max 9][r][effect level that will be assigned]]", "",
        })
        public String[] playersCountDifficulty = new String[] {
                "players=12, effect=invisibility, amplifier=1r0",
                "players=12, effect=resistance, amplifier=3p4",
                "players=12, effect=regeneration, amplifier=3p6",
                "players=12, effect=strength, amplifier=6p",
                "players=12, effect=fireResistance, amplifier=0",
                "players=12, effect=speed, amplifier=1",

                "players=6, effect=resistance, amplifier=3p4",
                "players=6, effect=regeneration, amplifier=3p6",
                "players=6, effect=invisibility, amplifier=4r0",
                "players=6, effect=strength, amplifier=6p",
                "players=6, effect=fireResistance, amplifier=0",
                "players=6, effect=speed, amplifier=1",

                "players=3, effect=resistance, amplifier=1",
                "players=3, effect=regeneration, amplifier=1",
                "players=3, effect=strength, amplifier=1",
                "players=3, effect=fireResistance, amplifier=0",
                "players=3, effect=speed, amplifier=1",

                "players=2, effect=resistance, amplifier=0",
                "players=2, effect=regeneration, amplifier=0",
                "players=2, effect=strength, amplifier=0",
                "players=2, effect=speed, amplifier=1",

                "players=1, effect=speed, amplifier=1"
        };
    }

    public static class PortalSettings {
        @Config.LangKey(Referense.MODID + ".cfg.sub.portal.slm")
        @Config.Comment("is send ancient world load and finish message")
        public boolean isSendWorldLoadMessage = true;

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

        @Config.LangKey(Referense.MODID + ".cfg.sub.any.uog")
        public boolean useOldLoadingGui = false;

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

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.og")
        @Config.Comment("Should I use an old generator?")
        public boolean isOldGenerator = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.crd")
        public boolean cantChangeRenderDistanceChunks = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.ccg")
        public boolean cantChangeGammaSetting = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.np")
        public boolean noPeaceful = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.isw")
        public boolean isSetWarp = false;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.nnv")
        public boolean noNightVision = true;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.daw")
        public boolean isDeadToAncientWorld = false;

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
        public int structuresGenerationDelay = 8;

        @Config.LangKey(Referense.MODID + ".cfg.sub.aws.wgs.nct")
        public int numberSetClearPerTick = 1;

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


    // Данное событие нужно для того, чтобы все значения которые были изменены в игре, перезаписывались в самом конфиг файле
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Referense.MODID)) {
            ConfigManager.sync(Referense.MODID, Config.Type.INSTANCE);
            PlayersCountDifficultyProcessor.compile(DifficultySettings.playersCountDifficulty);
            System.out.println("Configs is set");
        }
    }

}