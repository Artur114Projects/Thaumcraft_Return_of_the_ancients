package com.artur114.thaumrota.common.config.client;

public enum EnumFXQuality {
    HIGH, MEDIUM, LOW, POTATO;

    public int particleMaxDist() {
        switch (this) {
            case HIGH:
                return 24;
            case MEDIUM:
                return 16;
            case LOW:
                return 8;
            default:
                return 0;
        }
    }

    public int renderDistance() {
        switch (this) {
            case HIGH:
                return 64;
            case MEDIUM:
                return 48;
            default:
                return 0;
        }
    }

    public int maxLight() {
        switch (this) {
            case HIGH:
                return 48;
            case MEDIUM:
                return 32;
            default:
                return 0;
        }
    }

    public int bsfDistance() {
        switch (this) {
            case HIGH:
                return 5;
            case MEDIUM:
                return 4;
            default:
                return 0;
        }
    }

    public int bsfDirectDistance() {
        switch (this) {
            case HIGH:
                return 3;
            case MEDIUM:
                return 2;
            default:
                return 0;
        }
    }

    public int toInt() {
        return 4 - this.ordinal();
    }
}
