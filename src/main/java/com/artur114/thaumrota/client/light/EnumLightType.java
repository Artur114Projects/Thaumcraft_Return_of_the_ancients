package com.artur114.thaumrota.client.light;

public enum EnumLightType {
    LINE, POINT;

    public int passCount() {
        switch (this) {
            case LINE:
                return 6;
            case POINT:
                return 3;
            default:
                return -1;
        }
    }

    public String nameForPass(int pass) {
        switch (this) {
            case LINE:
                switch (pass) {
                    case 0:
                        return "lineLightColor";
                    case 1:
                        return "lineLightParams";
                    case 2:
                        return "lineLightPosFrom";
                    case 3:
                        return "lineLightPosTo";
                    case 4:
                        return "lineLightABBMin";
                    case 5:
                        return "lineLightABBMax";
                }
            case POINT:
                switch (pass) {
                    case 0:
                        return "pointLightColor";
                    case 1:
                        return "pointLightParams";
                    case 2:
                        return "pointLightPos";
                }
            default:
                return "";
        }
    }
}
