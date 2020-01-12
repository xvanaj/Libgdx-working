package com.mygdx.game.world.enums.adventure;

import java.awt.*;

public enum Direction {
    EAST,
    WEST,
    SOUTH,
    NORTH,
    ;

    public Point from(Point point){
        switch (this) {
            case EAST:
                return new Point(point.x+1, point.y);
            case WEST:
                return new Point(point.x-1, point.y);
            case SOUTH:
                return new Point(point.x, point.y-1);
            case NORTH:
                return new Point(point.x, point.y+1);
        }
        return null;
    }
}
