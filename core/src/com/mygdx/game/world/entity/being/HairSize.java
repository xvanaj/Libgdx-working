package com.mygdx.game.world.entity.being;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HairSize {

    VERY_SHORT("grass-like"),
    SHORT("short"),
    LONG("long"),
    EXTRA_LONG("extra long"),
    MEGA_LONG("of half of his body"),
    ;

    private String description;
}
