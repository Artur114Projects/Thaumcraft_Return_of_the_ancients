package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.stream.Collectors;

public class PlayersCountDifficultyProcessor {
    private static final List<PlayersCountDifficultyPhase> countDifficultyPhases = new ArrayList<>();
    private static boolean isGood = true;
    private static final int finishDataLength = 10;
    private static final int countEffects = 6;

    public static void compile(String[] array) {
        isGood = true;
        countDifficultyPhases.clear();
        HashMap<Integer, List<String>> sortedStrings = new HashMap<>();
        List<Integer> keys = new ArrayList<>();
        if (array.length == 0) {
            return;
        }
        for (String rawData : array) {
            String data = replaceData(rawData);
            if (!isGood) {
                TRAConfigs.playersCountDifficultyERROR(() -> {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "line \" " + rawData + "\" is entered incorrectly!"));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Settings not applied"));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Default settings applied"));
                });
                compile(HandlerR.defaultCountDifficultyData);
                return;
            }

            StringBuilder spc = new StringBuilder();
            for (byte i = 0; HandlerR.isNumber(data.charAt(i)); i++) {
                spc.append(data.charAt(i));
            }

            int char0int = Integer.parseInt(spc.toString());

            if (sortedStrings.containsKey(char0int)) {
                sortedStrings.get(char0int).add(data);
            } else {
                sortedStrings.put(char0int, new ArrayList<>());
                sortedStrings.get(char0int).add(data);
                keys.add(char0int);
            }
        }
        sortedStrings.forEach((key, value) -> {
            if (value.size() > countEffects) {
                TRAConfigs.playersCountDifficultyERROR(() -> {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "you can't use more than " + countEffects + " lines per number of players, OnPlayers:" + key));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Settings not applied"));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Default settings applied"));
                });
                compile(HandlerR.defaultCountDifficultyData);
                isGood = false;
            }
        });

        if (!isGood) {
            return;
        }

        keys =  keys.stream().sorted().collect(Collectors.toList());

        for (int p : keys) {
            countDifficultyPhases.add(new PlayersCountDifficultyPhase(p, sortedStrings.get(p).toArray(new String[0])));
        }

        if (TRAConfigs.Any.debugMode) {
            sortedStrings.forEach((key, value) -> System.out.println("key: " + key + " value: " + value));
        }
    }

    private static String replaceData(String data) { // players=6, effect=resistance, amplifier=3p4
        String data1 = data.replaceAll(" ", "");
        String dataFinal = "";
        if (data1.contains("resistance")) {
            data1 = data1.replaceAll("resistance", "0");
        } else if (data1.contains("strength")) {
            data1 = data1.replaceAll("strength", "1");
        } else if (data1.contains("regeneration")) {
            data1 = data1.replaceAll("regeneration", "2");
        } else if (data1.contains("fireResistance")) {
            data1 = data1.replaceAll("fireResistance", "3");
        } else if (data1.contains("invisibility")) {
            data1 = data1.replaceAll("invisibility", "4");
        } else if (data1.contains("speed")) {
            data1 = data1.replaceAll("speed", "5");
        } else {
            isGood = false;
            return dataFinal;
        }

        if (data1.contains("players=")) {
            data1 = data1.replaceAll("players=", "");
        } else {
            isGood = false;
            return dataFinal;
        }

        if (data1.contains("effect=")) {
            data1 = data1.replaceAll("effect=", "");
        } else {
            isGood = false;
            return dataFinal;
        }

        if (data1.contains("amplifier=")) {
            data1 = data1.replaceAll("amplifier=", "");
        } else {
            isGood = false;
            return dataFinal;
        }

        if (data1.length() > finishDataLength) {
            isGood = false;
            return dataFinal;
        }

        if (!HandlerR.isNumber(data1.charAt(0))) {
            isGood = false;
            return dataFinal;
        }

        dataFinal = data1;
        return dataFinal;
    }

    public static void calculate(int playersCount, int[] potionEffects) {
        boolean isSet = false;
        for (byte i = 0; i != potionEffects.length; i++) {
            potionEffects[i] = -1;
        }

        for (int i = countDifficultyPhases.size() - 1; i != -1; i--) {
            if (countDifficultyPhases.get(i).is(playersCount)) {
                countDifficultyPhases.get(i).calculate(playersCount, potionEffects);
                isSet = true;
                break;
            }
        }

        if (!isSet) {
            for (byte i = 0; i != potionEffects.length; i++) {
                potionEffects[i] = -1;
            }
        }
    }

    private static class PlayersCountDifficultyPhase {
        private final List<CountDifficulty> countDifficulties = new ArrayList<>();

        private final int onPlayers;

        public PlayersCountDifficultyPhase(int onPlayers, String[] data) {
            this.onPlayers = onPlayers;
            if (data.length > countEffects) {
                System.out.println("ERROR " + Arrays.toString(data));
            }
            List<Byte> effectIndexes = new ArrayList<>();
            for (String string : data) {
                int onPlayers0 = -1;
                byte effectIndex = -1;
                int amplifier = -1;
                int[] mathAmplifier = new int[] {-1, -1};
                byte phase = 0;
                StringBuilder sbP0 = new StringBuilder();
                for (byte i = 0; i != string.length(); i++) {
                    if (string.charAt(i) == ',') {
                        phase++;
                        continue;
                    }

                    if (phase == 0) {
                        if (HandlerR.isNumber(string.charAt(i))) {
                            sbP0.append(string.charAt(i));
                            onPlayers0 = Integer.parseInt(sbP0.toString());
                        }
                    } else if (phase == 1) {
                        if (HandlerR.isNumber(string.charAt(i))) {
                            effectIndex = (byte) Integer.parseInt(string.charAt(i) + "");
                        }
                    } else {
                        StringBuilder s = new StringBuilder();
                        for (byte j = i; j != string.length(); j++) {
                            s.append(string.charAt(j));
                        }
                        String s1 = s.toString();
                        if (s1.length() > 3) {
                            System.out.println("ERROR s1.length() > 3 " + s1);
                            break;
                        }
                        if (HandlerR.isNumber(s1)) {
                            amplifier = Integer.parseInt(s1);
                        } else {
                            if (s1.contains("p") && s1.length() >= 2) {
                                byte index = 0;
                                for (byte j = 0; j != s1.length(); j++) {
                                    if (index > 1) {
                                        System.out.println("ERROR " + s1);
                                        break;
                                    }
                                    if (HandlerR.isNumber(s1.charAt(j))) {
                                        mathAmplifier[index] = Integer.parseInt(s1.charAt(j) + "");
                                        index++;
                                    }
                                }
                            } else if (s1.contains("r") && s1.length() >= 2) {
                                byte index = 0;
                                for (byte j = 0; j != s1.length(); j++) {
                                    if (index > 1) {
                                        System.out.println("ERROR " + s1);
                                        break;
                                    }
                                    if (HandlerR.isNumber(s1.charAt(j))) {
                                        final int in = Integer.parseInt(s1.charAt(j) + "");
                                        if (index == 0) {
                                            mathAmplifier = new int[]{in};
                                        } else {
                                            amplifier = in;
                                        }
                                        index++;
                                    }
                                }
                            } else {
                                System.out.println("ERROR s1.contains(\"r || p\") && s1.length() >= 2 " + string);
                            }
                        }
                        break;
                    } // 61,2,3p4
                }
                countDifficulties.add(new CountDifficulty(onPlayers0, amplifier, mathAmplifier, effectIndex));
                effectIndexes.add(effectIndex);
            }
            if (effectIndexes.stream().distinct().toArray().length != effectIndexes.size()) {
                TRAConfigs.playersCountDifficultyERROR(() -> {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "you can't use multiple identical effects, OnPlayers:" + onPlayers));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Settings not applied"));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Default settings applied"));
                });
                compile(HandlerR.defaultCountDifficultyData);
            }
        }

        public void calculate(int playersCount, int[] potionEffects) {
            for (CountDifficulty countDifficulty : countDifficulties) {
                potionEffects[countDifficulty.getEffectIndex()] = countDifficulty.calculate(playersCount);
            }
        }

        public boolean is(int playersCount) {
            return playersCount >= onPlayers;
        }
    }

    private static class CountDifficulty {
        private final int amplifier;
        private final int[] mathAmplifier;
        private final int onPlayers;
        private final byte effectIndex;

        public CountDifficulty(int onPlayers, int amplifier, int[] mathAmplifier, byte effectIndex) {
            this.amplifier = amplifier;
            this.mathAmplifier = mathAmplifier;
            this.onPlayers = onPlayers;
            this.effectIndex = effectIndex;
        }

        public int calculate(int playersCount) {
            if (playersCount >= onPlayers) {
                if (amplifier != -1) {
                    if (mathAmplifier.length == 1) {
                        return (HandlerR.genRandomIntRange(0, mathAmplifier[0]) == 0) ? amplifier : -1;
                    }
                    return amplifier;
                } else {
                    if (mathAmplifier.length == 2) {
                        if (mathAmplifier[1] != -1) {
                            return (Math.min((playersCount / mathAmplifier[0]), mathAmplifier[1]));
                        } else {
                            return (playersCount / mathAmplifier[0]);
                        }
                    }
                }
            }
            return -1;
        }

        public byte getEffectIndex() {
            return effectIndex;
        }
    }
}
