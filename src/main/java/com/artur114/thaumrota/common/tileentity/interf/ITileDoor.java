package com.artur114.thaumrota.common.tileentity.interf;

public interface ITileDoor {
    void open();
    void close();
    boolean isOpen();
    boolean isClose();
    boolean isMoving();
    boolean isOpenOrOpening();
}
