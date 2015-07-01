package com.connorhenke.mcts3000;

public class Route {

    public static final String NUMBER = "rt";
    public static final String NAME = "rtnm";
    public static final String COLOR = "rtclr";

    private String name;
    private String number;
    private String color;

    public Route(String number, String name, String color) {
        this.name = name;
        this.number = number;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }
}
