package com.connorhenke.mcts3000.models;

public class Direction {

    public static final String DIRECTION = "dir";

    private String dir;

    public Direction(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}
