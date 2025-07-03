package com.artur.returnoftheancients.ancientworld.map.utils.maps;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.IStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureInteractive;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureSerializable;
import com.artur.returnoftheancients.util.interfaces.IReadFromNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InteractiveMap extends AbstractMap implements IWriteToNBT, IReadFromNBT {
    private final Map<Class<IStructure>, List<IStructure>> structuresDictionary = new HashMap<>();
    private final ChunkPos center;
    private final World world;

    public InteractiveMap(AbstractMap map, World world, ChunkPos center) {
        this(map.size, world, center);

        this.copyFromMap(map);

        this.foundAndBindInteractiveS();
    }

    public InteractiveMap(int size, World world, ChunkPos center) {
        super(size);

        this.center = center;
        this.world = world;
    }

    @Override
    public @Nullable IStructureType structureType(StrPos pos) {
        return this.structureType(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable IStructureType structureType(int x, int y) {
        IStructure structure = this.structure(x, y);
        if (structure == null) return null;
        return structure.type();
    }

    @Override
    public @Nullable EnumRotate structureRotate(StrPos pos) {
        return this.structureRotate(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable EnumRotate structureRotate(int x, int y) {
        IStructure structure = this.structure(x, y);
        if (structure == null) return null;
        return structure.rotate();
    }

    @Override
    public @Nullable IStructure structure(StrPos pos) {
        return this.structure(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable IStructure structure(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return null;
        return this.structures[this.index(x, y)];
    }

    @Override
    public void insetRotate(StrPos pos, EnumRotate rotate) {
        this.insetRotate(pos.getX(), pos.getY(), rotate);
    }

    @Override
    public void insetRotate(int x, int y, EnumRotate rotate) {
        IStructure structure = this.structure(x, y);
        if (structure == null) return;
        structure.setRotate(rotate);
    }

    @Override
    public void insetStructure(IStructure structure) {
        StrPos pos = structure.pos();
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
        NBTTagList list = nbt.getTagList("strSerializable", 10);
        StrPos.MutableStrPos pos = new StrPos.MutableStrPos();

        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            IStructure str = this.structure(pos.fromLong(data.getLong("pos")));

            if (str instanceof IStructureSerializable) {
                ((IStructureSerializable) str).readFromNBT(data.getCompoundTag("data"));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (IStructureSerializable serializable : this.foundStructures(IStructureSerializable.class)) {
            NBTTagCompound str = new NBTTagCompound();
            str.setTag("data", serializable.writeToNBT(new NBTTagCompound()));
            str.setLong("pos", serializable.pos().asLong());
            list.appendTag(str);
        }
        nbt.setTag("strSerializable", list);
        return nbt;
    }

    @SuppressWarnings("unchecked")
    public <T extends IStructure> List<T> foundStructures(Class<T> structureClass) {
        if (this.structuresDictionary.containsKey(structureClass)) {
            return (List<T>) this.structuresDictionary.get(structureClass);
        }

        List<T> ret = new ArrayList<>();

        for (int i = 0; i != this.structures.length; i++) {
            IStructure structure = this.structures[i];
            if (structureClass.isInstance(structure)) {
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

    private void foundAndBindInteractiveS() {
        for (IStructureInteractive interactive : this.foundStructures(IStructureInteractive.class)) {
            interactive.bindWorld(this.world);
            this.bindRealPos(interactive);
        }
    }

    private void bindRealPos(IStructureInteractive interactive) {
        int x = this.center.x + (this.size() / 2) - (interactive.pos().getX());
        int z = this.center.z + (this.size() / 2) - (interactive.pos().getY());
        interactive.bindRealPos(new ChunkPos(x, z));
    }

    private void copyFromMap(AbstractMap map) {
        for (int i = 0; i != map.structures.length; i++) {
            IStructure structure = map.structures[i];
            if (structure != null && !(structure instanceof IStructureMultiChunk.IStructureSegment)) {
                this.insetStructure(structure.copy());
            }
        }
    }
}
