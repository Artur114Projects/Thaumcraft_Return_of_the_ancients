package com.artur.returnoftheancients.energy.system;


import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergyProvider;
import com.artur.returnoftheancients.handlers.CollectionsHandler;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import com.artur.returnoftheancients.util.multitread.AsyncCompileManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;


public class EnergySystem {
    private final AsyncCompileManager<Map<OutputInput, EnergyWay>> waysMap;
    private final Set<ITileEnergyProvider> storages;
    private final Set<ITileEnergy> lines;

    public final long id;


    public EnergySystem(Set<ITileEnergyProvider> storages, Set<ITileEnergy> lines, long id) {
        this.storages = storages;
        this.lines = lines;
        this.id = id;

        this.waysMap = new AsyncCompileManager<>((prevData) -> new EnergyWaysMapCompiler(prevData, this.storages, this.lines));
        if (!storages.isEmpty()) this.waysMap.reCompile();
    }

    public boolean isEmpty() {
        return storages.isEmpty() && lines.isEmpty();
    }

    public EnergySystem bindTile(ITileEnergy tile) {
        tile.setNetworkId(id);

        if (tile.isEnergyLine()) {
            this.lines.add(tile);
        } else {
            this.storages.add((ITileEnergyProvider) tile);
        }

        this.waysMap.reCompile();

        return this;
    }

    public EnergySystem remove(ITileEnergy tile) {
        if (tile.isEnergyLine()) {
            this.lines.remove(tile);
        } else {
            this.storages.remove((ITileEnergyProvider) tile);
        }

        this.waysMap.reCompile();

        return this;
    }

    public void merge(EnergySystem system) {
        for (ITileEnergy tile : system.storages) tile.setNetworkId(id);
        for (ITileEnergy tile : system.lines) tile.setNetworkId(id);

        this.storages.addAll(system.storages);
        this.lines.addAll(system.lines);

        system.storages.clear();
        system.lines.clear();

        this.waysMap.reCompile();
    }

    public void update(boolean isStart) {
        Map<OutputInput, EnergyWay> map = this.waysMap.get();

        map.forEach((oi, way) -> way.transfer(oi.input.world().rand.nextFloat() * 4));
    }

    private static class OutputInput {
        private final ITileEnergyProvider output;
        private final ITileEnergyProvider input;

        private OutputInput(ITileEnergyProvider output, ITileEnergyProvider input) {
            this.output = output;
            this.input = input;
        }

        @Override
        public int hashCode() {
            return Objects.hash(output, input);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof OutputInput)) return false;

            OutputInput that = (OutputInput) obj;

            return Objects.equals(this.output, that.output) && Objects.equals(this.input, that.input);
        }
    }

    private static class EnergyWay {
        private final OutputInput outputInput;
        private final Set<ITileEnergy> lines;

        private EnergyWay(OutputInput outputInput, Set<ITileEnergy> lines) {
            this.outputInput = outputInput;
            this.lines = lines;
        }

        private float transfer(float count) {
            if (outputInput.input == outputInput.output) {
                return count;
            }

            count = outputInput.output.take(count);

            for (ITileEnergy tile : lines) {
                count = tile.transferEnergy(count);
            }

            return outputInput.input.add(count);
        }
    }

    private static class EnergyWayBuilder {
        private static EnergyWayBuilder[] STACK = new EnergyWayBuilder[128];
        private static int stackHead = -1;

        private synchronized static @NotNull EnergyWayBuilder getBuilderFromPoll() {
            if (stackHead != -1) {
                EnergyWayBuilder builder = STACK[stackHead];
                STACK[stackHead] = null;
                stackHead--;

                if (builder == null) {
                    System.out.println("Object in " + stackHead + " is null wtf!?");
                    return getBuilderFromPoll();
                }

                return builder;
            } else {
                return new EnergyWayBuilder();
            }
        }

        private synchronized static EnergyWayBuilder copy(EnergyWayBuilder parent) {
            EnergyWayBuilder builder = getBuilderFromPoll();
            builder.setData(parent);
            return builder;
        }

        private synchronized static void returnBuilderToPoll(@NotNull EnergyWayBuilder builder) {
            if (stackHead + 1 >= STACK.length) {
                System.out.println("wow! poll is full!");
                STACK = Arrays.copyOf(STACK, STACK.length * 2);
            }

            builder.clear();
            STACK[++stackHead] = builder;
        }

        private final Set<ITileEnergy> lines = new HashSet<>();
        private ITileEnergy lastTile = null;

        private EnergyWayBuilder build(ITileEnergy tile) {
            this.lines.add(tile);
            this.lastTile = tile;
            return this;
        }

        private ITileEnergy getLastTile() {
            return this.lastTile;
        }

        private EnergyWay finishBuild(OutputInput outputInput) {
            Set<ITileEnergy> finalLines = new HashSet<>(this.lines);
            finalLines.remove(outputInput.output);
            finalLines.remove(outputInput.input);
            return new EnergyWay(outputInput, finalLines);
        }

        private void clear() {
            this.lastTile = null;
            this.lines.clear();
        }

        private void setData(EnergyWayBuilder parent) {
            this.clear();

            this.lines.addAll(parent.lines);
            this.lastTile = parent.lastTile;
        }
    }

    private static class EnergyWaysMapCompiler implements Supplier<Map<OutputInput, EnergyWay>> {
        private final Set<ITileEnergyProvider> outputs = new HashSet<>();
        private final Set<ITileEnergyProvider> inputs = new HashSet<>();
        private final Map<OutputInput, EnergyWay> prevData;
        private final Set<ITileEnergyProvider> storages;
        private Map<BlockPos, ITileEnergy> map;
        private final Set<ITileEnergy> lines;


        private EnergyWaysMapCompiler(Map<OutputInput, EnergyWay> prevData, Set<ITileEnergyProvider> storages, Set<ITileEnergy> lines) {
            this.storages = new HashSet<>(storages);
            this.lines = new HashSet<>(lines);
            this.prevData = prevData;
        }

        private void fillOutputsInputs() {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
            for (ITileEnergyProvider provider : this.storages) {
                if (provider.can(blockPos, provider::canAddFromFacing, this.map::get)) {
                    this.inputs.add(provider);
                }
                if (provider.can(blockPos, provider::canTakeFromFacing, this.map::get)) {
                    this.outputs.add(provider);
                }
            }
            UltraMutableBlockPos.release(blockPos);
        }

        private void fillMap() {
            Set<ITileEnergy> set = new HashSet<>(this.lines);
            set.addAll(this.storages);
            this.map = CollectionsHandler.toMap(new HashMap<>(), set, ITileEnergy::pos);
        }

        @Override
        public Map<OutputInput, EnergyWay> get() {
            this.fillMap();
            this.fillOutputsInputs();

            return this.compileNewData();
        }

        private Map<OutputInput, EnergyWay> processChangedData() {
            return null;
        }

        private Map<OutputInput, EnergyWay> compileNewData() {
            Map<OutputInput, EnergyWay> ret = new HashMap<>();

            for (ITileEnergyProvider output : outputs) {
                this.buildWay(ret, output, inputs);
            }

            return ret;
        }

        private Map<OutputInput, EnergyWay> buildWay(Map<OutputInput, EnergyWay> map, ITileEnergyProvider output, Set<ITileEnergyProvider> inputs) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
            ArrayDeque<EnergyWayBuilder> queue = new ArrayDeque<>(100);
            Set<ITileEnergy> checked = new HashSet<>(100);
            Set<ITileEnergy> neighborsBuffer = new HashSet<>();
            int foundInputs = 0;

            queue.addLast(EnergyWayBuilder.getBuilderFromPoll().build(output));

            while (!queue.isEmpty()) {
                EnergyWayBuilder builder = queue.poll();
                neighborsBuffer.clear();

                if (builder == null) {
                    continue;
                }

                if (foundInputs == inputs.size()) {
                    EnergyWayBuilder.returnBuilderToPoll(builder);
                    continue;
                }

                if (builder.getLastTile() == null) {
                    EnergyWayBuilder.returnBuilderToPoll(builder);
                    continue;
                }

                Set<ITileEnergy> neighbors = this.getNeighbors(blockPos, neighborsBuffer, builder.getLastTile());

                for (ITileEnergy neighbor : neighbors) {
                    if (checked.contains(neighbor)) {
                        continue;
                    }

                    if (neighbor instanceof ITileEnergyProvider && inputs.contains(neighbor)) {
                        OutputInput outputInput = new OutputInput(output, (ITileEnergyProvider) neighbor);
                        map.put(outputInput, builder.finishBuild(outputInput));
                        foundInputs++;
                    }

                    EnergyWayBuilder newBuilder = EnergyWayBuilder.copy(builder);
                    queue.addLast(newBuilder.build(neighbor));
                    checked.add(neighbor);
                }

                EnergyWayBuilder.returnBuilderToPoll(builder);
            }

            UltraMutableBlockPos.release(blockPos);
            return map;
        }


        private Set<ITileEnergy> getNeighbors(UltraMutableBlockPos util, Set<ITileEnergy> buffer, ITileEnergy tile) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                if (tile.canConnect(facing)) {
                    ITileEnergy neighbor = this.map.get(util.setPos(tile.pos()).offset(facing));
                    if (neighbor != null && neighbor.canConnect(facing.getOpposite())) {
                        buffer.add(neighbor);
                    }
                }
            }
            return buffer;
        }
    }
}
