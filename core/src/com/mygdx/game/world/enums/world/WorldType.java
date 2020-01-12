package com.mygdx.game.world.enums.world;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * Created by Jakub Vana on 8.10.2018.
 */
@Getter
@AllArgsConstructor
public enum WorldType {

    EARTH("bigTileset", "Gaia"),
    ABYSS("bigTilesetRed", "Abyss"),
    SILMAR("bigTilesetGreen", "Sylvan"),
    ICE("bigTilesetBlue", "Ice"),

    ;

    WorldType(String imagePath, String name) {
        this.name = name;
    }

    private BufferedImage image;
    private String name;



    public BufferedImage getImage() {
        return image;
    }
}
