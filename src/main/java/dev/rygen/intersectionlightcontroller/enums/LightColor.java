package dev.rygen.intersectionlightcontroller.enums;

public enum LightColor {
    OFF("none"),
    GREEN("green"),
    YELLOW("yellow"),
    RED("red");

    private final String name;

    LightColor(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }
}
