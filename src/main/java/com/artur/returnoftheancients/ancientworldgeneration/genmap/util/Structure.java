package com.artur.returnoftheancients.ancientworldgeneration.genmap.util;

import com.artur.returnoftheancients.utils.interfaces.IALGS;

public class Structure {
    private byte deformationOfBoss;
    private byte rotate;
    private byte ID;

    public Structure(byte id, byte rotate) {
        this.deformationOfBoss = 0;
        this.rotate = rotate;
        this.ID = id;
    }

    public byte getRotate() {
        return rotate;
    }

    public void setRotate(byte rotate) {
        this.rotate = rotate;
    }

    public byte getID() {
        return ID;
    }

    public void setID(byte ID) {
        this.ID = ID;
    }

    public byte getDeformationOfBoss() {
        return deformationOfBoss;
    }

    public void setDeformationOfBoss(byte deformationOfBoss) {
        this.deformationOfBoss = deformationOfBoss;
    }

    @Override
    public String toString() {
        return "(id:" + ID + ", r:" + rotate + ", d:" + deformationOfBoss + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Structure) {
            Structure structure = (Structure) obj;
            return structure.ID == ID && structure.rotate == rotate && structure.deformationOfBoss == deformationOfBoss;
        } else {
            return false;
        }
    }
}
