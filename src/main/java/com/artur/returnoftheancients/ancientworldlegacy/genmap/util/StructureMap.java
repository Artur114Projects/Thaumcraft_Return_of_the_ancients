package com.artur.returnoftheancients.ancientworldlegacy.genmap.util;

import com.artur.returnoftheancients.util.interfaces.IALGS;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StructureMap implements IALGS {
    private final Structure[] changedStructures;
    private Structure[] structures;
    public final int SIZE;

    public StructureMap(byte[][] structures, byte[][] rotates) {
        if (!(structures.length == rotates.length && structures[0].length == rotates[0].length && structures.length == structures[0].length)) throw new RuntimeException("StructureMap.class, transferred incorrect arrays");
        this.SIZE = structures.length;
        this.structures = new Structure[SIZE * SIZE];

        for (int y = 0; y != SIZE; y++) {
            for (int x = 0; x != SIZE; x++) {
                this.structures[x + y * SIZE] = new Structure(structures[y][x], rotates[y][x]);
            }
        }

        this.changedStructures = this.structures;
    }

//    public StructureMap(NBTTagCompound nbt) {
//        if (!nbt.hasKey("SIZE")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:0");
//        SIZE = nbt.getInteger("SIZE");
//        rotates = new byte[SIZE][SIZE];
//        structures = new byte[SIZE][SIZE];
//
//        if (!nbt.hasKey("structures")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:1");
//        NBTTagCompound structuresNBT = nbt.getCompoundTag("structures");
//        if (!nbt.hasKey("rotates")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:2");
//        NBTTagCompound rotatesNBT = nbt.getCompoundTag("rotates");
//
//        arrayInNBT(structuresNBT, structures);
//        arrayInNBT(rotatesNBT, rotates);
//    }

    private void arrayInNBT(NBTTagCompound nbt, byte[][] array) {
        if (!nbt.hasKey("y:0")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:3");
        for (int i = 0; i != SIZE; i++) {
            NBTTagCompound compound = nbt.getCompoundTag("y:" + i);
            if (!compound.hasKey("x:0")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:4");
            for (int j = 0; j != SIZE; j++) {
                array[i][j] = compound.getByte("x:" + j);
            }
        }
    }

    private NBTTagCompound NBTInArray(byte[][] array) {
        NBTTagCompound nbt = new NBTTagCompound();
        for (int i = 0; i != array.length; i++) {
            NBTTagCompound compound = new NBTTagCompound();
            for (int j = 0; j != array[i].length; j++) {
                compound.setByte("x:" + j, array[i][j]);
            }
            nbt.setTag("y:" + i, compound);
        }
        return nbt;
    }

    public byte getStructure(int x, int y) {
        return structures[x + y * SIZE].getID();
    }

    public byte getStructure(StructurePos pos) {
        return getStructure(pos.getX(), pos.getY());
    }


    public Structure getStructureObject(int x, int y) {
        Structure structure = new Structure(structures[x + y * SIZE].getID(), structures[x + y * SIZE].getRotate());
        structure.setDeformationOfBoss(structures[x + y * SIZE].getDeformationOfBoss());
        return structure;
    }

    public byte getDeformation(int x, int y) {
        return structures[x + y * SIZE].getDeformationOfBoss();
    }

    public void setDeformation(int x, int y, byte value) {
        structures[x + y * SIZE].setDeformationOfBoss(value);
    }

    public byte getRotate(int x, int y) {
        return structures[x + y * SIZE].getRotate();
    }

    public void setStructure(int x, int y, byte value) {
        changedStructures[x + y * SIZE].setID(value);
    }

    public void setRotate(int x, int y, byte value) {
        changedStructures[x + y * SIZE].setRotate(value);
    }

    public void swapBuffers() {
        structures = changedStructures;
    }


//    public NBTTagCompound toNBT() {
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setInteger("SIZE", SIZE);
//        nbt.setTag("structures", NBTInArray(structures));
//        nbt.setTag("rotates", NBTInArray(rotates));
//        return nbt;
//    }

    public List<StructurePos> getConnectedStructures(StructurePos pos) {
        return getConnectedStructures(pos.getX(), pos.getY());
    }

    public List<StructurePos> getConnectedStructures(int x, int y) {
        List<StructurePos> posList = new ArrayList<>();
        byte structure = getStructure(x, y);
        byte structureRotate = getRotate(x, y);
        boolean[][] ports = new boolean[7][7];

        if (structure == 0) {
            return posList;
        }

        addPorts(structure, structureRotate, ports, 0, 0);

        for (byte i = 0; i != 4; i++) {
            int sx = x;
            int sy = y;

            switch (i) {
                case 0:{
                    sy = y + 1;
                } break;
                case 1:{
                    sx = x + 1;
                } break;
                case 2:{
                    sy = y - 1;
                } break;
                case 3:{
                    sx = x - 1;
                } break;
            }

            final boolean isNotOutFromArray = sy < SIZE && sy >= 0 && sx < SIZE && sx >= 0;
            byte structureC = isNotOutFromArray ? getStructure(sx, sy) : 0;
            byte structureRotateC = isNotOutFromArray ? getRotate(sx, sy) : 0;

            if (structureC == 0) {
                continue;
            }

            boolean[][] portsC = new boolean[7][7];

            // 0, 1, 2, 3, 4, 5, 6; 0
            // 0, 1, 2, 3, 4, 5, 6; 1
            // 0, 1, 2, D, 4, 5, 6; 2
            // 0, 1, D, C, D, 5, 6; 3
            // 0, 1, 2, D, 4, 5, 6; 4
            // 0, 1, 2, 3, 4, 5, 6; 5
            // 0, 1, 2, 3, 4, 5, 6; 6
            
            addPorts(structureC, structureRotateC, portsC, (sx - x) * 2, (sy - y) * 2);

            if (comparePorts(ports, portsC)) posList.add(new StructurePos(sx, sy));
        }
        return posList;
    }
    
    private void addPorts(byte structure, byte structureRotate, boolean[][] ports, int xOffset, int yOffset) {
        int cx = 3 + xOffset;
        int cy = 3 + yOffset;
        int leftC = cx - 1;
        int rightC = cx + 1;
        int upC = cy - 1;
        int downC = cy + 1;

        switch (structure) {
            case WAY_ID:{
                if (structureRotate == 1) {
                    ports[cy][leftC] = true;
                    ports[cy][rightC] = true;
                }
                if (structureRotate == 2) {
                    ports[upC][cx] = true;
                    ports[downC][cx] = true;
                }
            } break;
            case BOSS_ID:
            case ENTRY_ID:
            case CROSSROADS_ID: {
                ports[cy][leftC] = true;
                ports[cy][rightC] = true;
                ports[upC][cx] = true;
                ports[downC][cx] = true;
            } break;
            case FORK_ID:{
                if (structureRotate == 1) {
                    ports[cy][leftC] = true;
                    ports[cy][rightC] = true;
                    ports[upC][cx] = true;
                } else if (structureRotate == 2) {
                    ports[cy][rightC] = true;
                    ports[upC][cx] = true;
                    ports[downC][cx] = true;
                } else if (structureRotate == 3) {
                    ports[cy][leftC] = true;
                    ports[upC][cx] = true;
                    ports[downC][cx] = true;
                } else if (structureRotate == 4) {
                    ports[cy][leftC] = true;
                    ports[cy][rightC] = true;
                    ports[downC][cx] = true;
                }
            } break;
            case TURN_ID:{
                if (structureRotate == 1) {
                    ports[cy][rightC] = true;
                    ports[upC][cx] = true;
                } else if (structureRotate == 2) {
                    ports[cy][rightC] = true;
                    ports[downC][cx] = true;
                } else if (structureRotate == 3) {
                    ports[cy][leftC] = true;
                    ports[upC][cx] = true;
                } else if (structureRotate == 4) {
                    ports[cy][leftC] = true;
                    ports[downC][cx] = true;
                }
            } break;
            case END_ID:{
                if (structureRotate == 1) {
                    ports[cy][leftC] = true;
                } else if (structureRotate == 2) {
                    ports[upC][cx] = true;
                } else if (structureRotate == 3) {
                    ports[downC][cx] = true;
                } else if (structureRotate == 4) {
                    ports[cy][rightC] = true;
                }
            } break;
        }
    }

    private boolean comparePorts(boolean[][] ports0, boolean[][] ports1) {
        for (int y = 0; y != ports0.length; y++) {
            for (int x = 0; x != ports0.length; x++) {
                if (ports1[y][x] == ports0[y][x] && ports1[y][x]) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder finalS = new StringBuilder("\n");
        for (int y = 0; y != SIZE; y++) {
            StringBuilder s = new StringBuilder();
            for (int x = 0; x != SIZE; x++) {
                s.append(" {").append(structures[x + y * SIZE]).append("},");
            }
            finalS.append(s).append("\n");
        }
        return String.valueOf(finalS);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StructureMap) {
            StructureMap map = (StructureMap) obj;
            return Arrays.deepEquals(map.structures, structures);
        } else {
            return false;
        }
    }
}
