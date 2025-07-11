package com.artur.returnoftheancients.tileentity.interf;

public interface ITileDoor {
    void open();
    void close();
    boolean isOpen();
    boolean isClose();
    boolean isMoving();
    boolean isOpenOrOpening();
}
