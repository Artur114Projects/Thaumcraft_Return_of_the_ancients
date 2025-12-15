package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumFace;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworldlegacy.genmap.util.StructurePos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GenPhaseBuildWays extends GenPhase {
    private final RandomReplacingManager wallDodgeReplacer;
    private final RandomReplacingManager wayBuildReplacer;

    public GenPhaseBuildWays(GenPhase parent) {
        super(parent);

        this.wallDodgeReplacer = this.initWallDodgeReplacer();
        this.wayBuildReplacer = this.initWayBuildReplacer();
    }

    private RandomReplacingManager initWallDodgeReplacer() {
        return new RandomReplacingManager(
            new ReplacerForkWallDodge(0.5F),
            new ReplacerTurnWallDodge(0.5F)
        );
    }

    private RandomReplacingManager initWayBuildReplacer() {
        return new RandomReplacingManager(
            new ReplacerCrossroads(0.05F),
            new ReplacerFork(0.10F),
            new ReplacerTurn(0.35F),
            new ReplacerNothing(0.50F)
        );
    }

    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        ImmutableMap map = this.parent.getMap(seed, size);
        StrPos.MutableStrPos pos = new StrPos.MutableStrPos();
        Random rand = new Random(seed);
        boolean hasFreePorts;

        do {
            hasFreePorts = false;
            map.freezeChanges();

            for (int y = 0; y != map.size(); y++) {
                for (int x = 0; x != map.size(); x++) {
                    IStructure str = map.structure(x, y);

                    if (str == null) {
                        continue;
                    }

                    for (EnumFace face : str.ports()) {
                        pos.setPos(x, y).offset(face);

                        IStructure str1 = map.structure(pos);

                        if (str1 == null) {
                            hasFreePorts = true;

                            this.buildWay(map, pos, face, rand);
                        }
                    }
                }
            }

            map.acceptChanges();

        } while (hasFreePorts);

        return map;
    }

    private void buildWay(ImmutableMap map, StrPos.MutableStrPos pos, EnumFace direction, Random rand) {
        IStructure str = map.structure(pos.offset(direction));
        if (str != null && !str.canConnect(direction.getOppose())) {
            this.dodgeWall(map, pos.offset(direction.getOppose()), direction, rand);
        } else {
            this.insertWay(map, pos.offset(direction.getOppose()), direction, rand);
        }
    }

    private void dodgeWall(ImmutableMap map, StrPos.MutableStrPos pos, EnumFace direction, Random rand) {
        RandomReplacerBase replacer = this.wallDodgeReplacer.randomReplacerCanReplace(map, pos, direction, rand);

        if (replacer != null) {
            replacer.replace(map, pos, direction, rand);
        } else {
            map.insetStructure(EnumStructureType.WAY.create(wayRotateFromAxis(direction.axis()), pos));
        }
    }

    private void insertWay(ImmutableMap map, StrPos.MutableStrPos pos, EnumFace direction, Random rand) {
        RandomReplacerBase replacer = this.wayBuildReplacer.randomReplacer(rand);

        if (replacer != null && replacer.canReplace(map, pos, direction)) {
            replacer.replace(map, pos, direction, rand);
        } else {
            map.insetStructure(EnumStructureType.WAY.create(wayRotateFromAxis(direction.axis()), pos));
        }
    }

    private static EnumRotate wayRotateFromAxis(EnumFace.Axis axis) {
        switch (axis) {
            case X:
                return EnumRotate.NON;
            case Y:
                return EnumRotate.C90;
            default:
                throw new NullPointerException();
        }
    }

    private static class RandomReplacingManager {
        private final RandomReplacerBase[] replacerBases;

        private RandomReplacingManager(RandomReplacerBase... replacerBases) {
            float percentages = 0;

            for (RandomReplacerBase replacerBase : replacerBases) {
                percentages += replacerBase.chance;
            }

            percentages = Math.round((percentages * 100));

            if (percentages != 100) {
                throw new IllegalArgumentException("The amount of chance is not equal to one, value:" + percentages);
            }

            this.replacerBases = replacerBases;
        }

        protected @Nullable RandomReplacerBase randomReplacer(Random rand) {
            float ran = rand.nextFloat();
            float chanceSum = 0;

            for (RandomReplacerBase replacer : this.replacerBases) {
                chanceSum += replacer.chance;
                if (ran < chanceSum) {
                    return replacer;
                }
            }

            return null;
        }

        protected @Nullable RandomReplacerBase randomReplacerCanReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            List<RandomReplacerBase> canReplace = new ArrayList<>(this.replacerBases.length);
            float cantPercent = 0;

            for (RandomReplacerBase replacer : this.replacerBases) {
                if (replacer.canReplace(map, pos, buildDirection)) {
                    canReplace.add(replacer);
                } else {
                    cantPercent += replacer.chance;
                }
            }

            cantPercent = cantPercent / canReplace.size();
            float ran = rand.nextFloat();
            float chanceSum = 0;

            for (RandomReplacerBase replacer : canReplace) {
                chanceSum += (replacer.chance + cantPercent);
                if (ran < chanceSum) {
                    return replacer;
                }
            }

            return null;
        }
    }

    private abstract static class RandomReplacerBase { // TODO: 14.12.2025 Переписать
        private final float chance;
        protected RandomReplacerBase(float chance) {
            this.chance = chance;
        }

        protected abstract boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection);
        protected abstract void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand);

        protected List<EnumFace> foundPorts(ImmutableMap map, StrPos pos) {
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            List<EnumFace> faces = new ArrayList<>();

            for (EnumFace face : EnumFace.values()) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str == null || str.canConnect(face.getOppose())) {
                    faces.add(face);
                }
            }

            return faces;
        }
    }

    private static class ReplacerTurnWallDodge extends RandomReplacerBase {

        protected ReplacerTurnWallDodge(float chance) {
            super(chance);
        }

        @Override
        protected boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection) {
            boolean has = false;
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            for (EnumFace face : EnumFace.allFromAxis(buildDirection.axis().opposite())) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str == null || str.canConnect(face.getOppose())) {
                    has = true;
                    break;
                }
            }
            return has;
        }

        @Override
        protected void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            List<EnumFace> has = new ArrayList<>(2);
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            EnumFace[] faces = EnumFace.allFromAxis(buildDirection.axis().opposite());

            for (EnumFace face : faces) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str == null || str.canConnect(face.getOppose())) {
                    has.add(face);
                }
            }

            if (has.size() > 1) {
                has.remove(rand.nextFloat() <= 0.5F ? 0 : 1);
            }

            has.add(buildDirection.getOppose());
            EnumRotate rotate = EnumStructureType.TURN.rotateFromPorts(has);

            if (rotate != null) {
                map.insetStructure(EnumStructureType.TURN.create(rotate, pos));
            }
        }
    }

    private static class ReplacerForkWallDodge extends RandomReplacerBase {

        protected ReplacerForkWallDodge(float chance) {
            super(chance);
        }

        @Override
        protected boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection) {
            boolean has = true;
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            for (EnumFace face : EnumFace.allFromAxis(buildDirection.axis().opposite())) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str != null && !str.canConnect(face.getOppose())) {
                    has = false;
                    break;
                }
            }
            return has;
        }

        @Override
        protected void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            List<EnumFace> has = new ArrayList<>(2);
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            EnumFace[] faces = EnumFace.allFromAxis(buildDirection.axis().opposite());

            for (EnumFace face : faces) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str == null || str.canConnect(face.getOppose())) {
                    has.add(face);
                }
            }

            has.add(buildDirection.getOppose());
            EnumRotate rotate = EnumStructureType.FORK.rotateFromPorts(has);

            if (rotate != null) {
                map.insetStructure(EnumStructureType.FORK.create(rotate, pos));
            }
        }
    }

    private static class ReplacerNothing extends RandomReplacerBase {

        protected ReplacerNothing(float chance) {
            super(chance);
        }

        @Override
        protected boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection) {
            return true;
        }

        @Override
        protected void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            map.insetStructure(EnumStructureType.WAY.create(wayRotateFromAxis(buildDirection.axis()), pos));
        }
    }

    private static class ReplacerTurn extends RandomReplacerBase {

        protected ReplacerTurn(float chance) {
            super(chance);
        }

        @Override
        protected boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection) {
            boolean has = false;
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            for (EnumFace face : EnumFace.allFromAxis(buildDirection.axis().opposite())) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str == null || str.canConnect(face.getOppose())) {
                    has = true;
                    break;
                }
            }
            return has;
        }

        @Override
        protected void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            List<EnumFace> has = new ArrayList<>(2);
            StrPos.MutableStrPos strPos = new StrPos.MutableStrPos();
            EnumFace[] faces = EnumFace.allFromAxis(buildDirection.axis().opposite());

            for (EnumFace face : faces) {
                IStructure str = map.structure(strPos.setPos(pos).offset(face));
                if (str == null || str.canConnect(face.getOppose())) {
                    has.add(face);
                }
            }

            if (has.size() > 1) {
                has.remove(rand.nextFloat() <= 0.5F ? 0 : 1);
            }

            has.add(buildDirection.getOppose());
            EnumRotate rotate = EnumStructureType.TURN.rotateFromPorts(has);

            if (rotate != null) {
                map.insetStructure(EnumStructureType.TURN.create(rotate, pos));
            }
        }
    }

    private static class ReplacerFork extends RandomReplacerBase {

        protected ReplacerFork(float chance) {
            super(chance);
        }

        @Override
        protected boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection) {
            return this.foundPorts(map, pos).size() >= 3;
        }

        @Override
        protected void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            List<EnumFace> ports = this.foundPorts(map, pos);
            ports.remove(buildDirection.getOppose());

            if (ports.size() > 2) {
                ports.remove(rand.nextInt(ports.size()));
            }

            ports.add(buildDirection.getOppose());

            EnumRotate rotate = EnumStructureType.FORK.rotateFromPorts(ports);

            if (rotate != null) {
                map.insetStructure(EnumStructureType.FORK.create(rotate, pos));
            }
        }
    }

    private static class ReplacerCrossroads extends RandomReplacerBase {

        protected ReplacerCrossroads(float chance) {
            super(chance);
        }

        @Override
        protected boolean canReplace(ImmutableMap map, StrPos pos, EnumFace buildDirection) {
            return this.foundPorts(map, pos).size() == 4;
        }

        @Override
        protected void replace(ImmutableMap map, StrPos pos, EnumFace buildDirection, Random rand) {
            map.insetStructure(EnumStructureType.CROSSROADS.create(EnumRotate.NON, pos));
        }
    }
}
