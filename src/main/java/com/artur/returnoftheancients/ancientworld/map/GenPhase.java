package com.artur.returnoftheancients.ancientworld.map;

public abstract class GenPhase {
    protected GenPhase parent = null;

    public GenPhase() {}
    public GenPhase(GenPhase parent) {
        this.parent = parent;
    }



    public static GenPhase initAllGenPhases() {
        return null;
    }
}
