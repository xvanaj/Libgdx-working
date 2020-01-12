package com.mygdx.game.world.enums.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.awt.*;

@Getter
@AllArgsConstructor
@ToString
public enum ResourceType {

    STONE(Color.DARK_GRAY, "silverOre"),
    TITANIUM(Color.lightGray, "mithrillOre"),
    ADAMANTITE(Color.lightGray, "silverOre"),
    GOLD(Color.YELLOW, "goldOre"),
    DIAMOND(Color.WHITE, "goldOre"),
    SILVER(Color.WHITE, "silverOre"),
    MITHRILL(Color.lightGray, "mithrillOre");

    private Color color;
    private String imageCode;
}
