package com.mygdx.game.world.enums.adventure;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jakub Vana on 8.8.2018.
 */
@Getter
@AllArgsConstructor
public enum Ambush {

    GOBLINS("Wandering goblins", 5),
    ORC_BRIGANDS("Orc brigands", 7),
    SATYR_ENCHANTRESS("Satyr warriors", 10),
    ORC_FEROCIOUS("Ferocious orcs", 10),
    GREEN_DRAGON("Green dragon", 500),
    BLUE_DRAGON("Blue dragon", 600),
    RED_DRAGON("Red dragon", 700),
    BLACK_DRAGON("Black dragon", 1000),
    GOLD_DRAGON("Gold dragon", 1000),
    ;

    private String name;
    private int startingDistance;
}
