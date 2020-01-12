package com.mygdx.game.world.enums.world;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Jakub Vana on 8.10.2018.
 */
@Getter
@AllArgsConstructor
public enum TerrainType {

    SHALLOW_OCEAN("0", new Color(0, 255, 255), new Point(4, 0), false, 0),
    DEEP_OCEAN("O", new Color(0, 160, 255), new Point(7, 0), false, 0),
    GLACIER("L", new Color(255, 255, 255), new Point(3, 0), false, 4),

    DESERT("D", new Color(255, 230, 0), new Point(0, 0), true, 2),
    GRASSLAND("G", new Color(50, 205, 50), new Point(1, 0), true, 1),
    DIRTLAND("G", new Color(50, 205, 50), new Point(2, 0), true, 1),
    SNOWLAND("G", new Color(50, 205, 50), new Point(3, 0), true, 1),

    SAND_HILL("H", new Color(0, 128, 0), new Point(0, 1), true, 2),
    GRASS_HILL("H", new Color(0, 128, 0), new Point(1, 1), true, 2),
    DRY_HILL("H", new Color(139, 69, 19), new Point(2, 1), true, 2),
    SNOW_HILL("H", new Color(139, 69, 19), new Point(3, 1), true, 2),

    SAND_MOUNTAIN("S", new Color(245, 245, 245), new Point(0, 2), true, 3),
    MOUNTAIN("M", new Color(220, 220, 220), new Point(1, 2), true, 2),
    DIRT_MOUNTAIN("M", new Color(220, 220, 220), new Point(2, 2), true, 2),
    SNOWED_MOUNTAIN("S", new Color(245, 245, 245), new Point(3, 2), true, 3),


    JUNGLE("J", new Color(173, 255, 47), new Point(2, 0), true, 2),
    TUNDRA("T", new Color(128, 128, 19), new Point(4, 0), true, 2),
    EMPTYNESS("X", new Color(0, 0, 0), new Point(0, 0), false, 0);

    private String name;
    private Color color;
    private Point point;
    private boolean passable;
    private int difficulty;

    public BufferedImage getTileImage(WorldType worldType, int pixelSize) {
        return worldType.getImage().getSubimage(point.x * pixelSize, point.y * pixelSize, pixelSize, pixelSize);
    }


}
