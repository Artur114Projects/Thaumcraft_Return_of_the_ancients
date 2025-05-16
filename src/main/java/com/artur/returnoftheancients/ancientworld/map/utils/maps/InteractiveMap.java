package com.artur.returnoftheancients.ancientworld.map.utils.maps;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureInteractive;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.util.interfaces.IReadFromNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InteractiveMap extends AbstractMap implements IWriteToNBT, IReadFromNBT {
    private final Map<Class<IStructure>, List<IStructure>> structuresDictionary = new HashMap<>();
    private final World world;

    public InteractiveMap(AbstractMap map, World world) {
        this(map.size, world);

        for (int i = 0; i != map.structures.length; i++) {
            IStructure structure = map.structures[i];
            if (structure != null) {
                this.structures[i] = structure.copy();
            }
        }

        this.foundAndBindWorldInteractiveS();
    }

    public InteractiveMap(int size, World world) {
        super(size);

        this.world = world;
    }

    @Override
    public @Nullable EnumStructure structureType(StructurePos pos) {
        return this.structureType(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable EnumStructure structureType(int x, int y) {
        IStructure structure = this.structure(x, y);
        if (structure == null) return null;
        return structure.type();
    }

    @Override
    public @Nullable EnumStructure.Rotate structureRotate(StructurePos pos) {
        return this.structureRotate(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable EnumStructure.Rotate structureRotate(int x, int y) {
        IStructure structure = this.structure(x, y);
        if (structure == null) return null;
        return structure.rotate();
    }

    @Override
    public @Nullable IStructure structure(StructurePos pos) {
        return this.structure(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable IStructure structure(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return null;
        return this.structures[this.index(x, y)];
    }

    @Override
    public void insetRotate(StructurePos pos, EnumStructure.Rotate rotate) {
        this.insetRotate(pos.getX(), pos.getY(), rotate);
    }

    @Override
    public void insetRotate(int x, int y, EnumStructure.Rotate rotate) {
        IStructure structure = this.structure(x, y);
        if (structure == null) return;
        structure.setRotate(rotate);
    }

    @Override
    public void insetStructure(IStructure structure) {
        StructurePos pos = structure.pos();
        if (pos.isOutOfBounds(this.size)) return;
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;

        if (structure instanceof IStructureMultiChunk) {
            ((IStructureMultiChunk) structure).insertSegments((this::insetStructure));
        }

        this.structuresDictionary.clear();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends IStructure> List<T> foundStructures(Class<T> structureClass) {
        if (this.structuresDictionary.containsKey(structureClass)) {
            return (List<T>) this.structuresDictionary.get(structureClass);
        }

        List<T> ret = new ArrayList<>();

        for (int i = 0; i != this.structures.length; i++) {
            IStructure structure = this.structures[i];
            if (structure != null && structureClass.isInstance(structure.getClass())) {
                ret.add(structureClass.cast(structure));
            }
        }

        this.structuresDictionary.put((Class<IStructure>) structureClass, (List<IStructure>) ret);
        return ret;
    }

    public void build(int index, World world, ChunkPos pos, Random rand) {
        IStructure structure = this.structure(index);
        if (structure == null) return;
        structure.build(world, pos, rand);
    }

    public @Nullable IStructure structure(int index) {
        if (index >= this.area() || index < 0) return null;
        return this.structures[index];
    }

    public void foundAndBindWorldInteractiveS() {
        for (IStructureInteractive interactive : this.foundStructures(IStructureInteractive.class)) {
            interactive.bindWorld(this.world);
        }
    }
}
