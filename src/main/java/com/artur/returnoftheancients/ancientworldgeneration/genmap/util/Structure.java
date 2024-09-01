package com.artur.returnoftheancients.ancientworldgeneration.genmap.util;

public class Structure {
    private byte deformationOfBoss;
    private byte rotate;
    private byte ID;

    public Structure(byte id, byte rotate) {
        this.ID = id;
        this.rotate = rotate;
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
}
