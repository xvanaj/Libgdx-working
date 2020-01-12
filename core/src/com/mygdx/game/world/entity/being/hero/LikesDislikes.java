package com.mygdx.game.world.entity.being.hero;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum LikesDislikes {

    RUNNING("running"),
    SWIMMING("swimming"),
    COOKING("cooking"),
    CLIMBING("climbing"),
    WEAPONS("weapons"),
    VIOLENCE("violence"),
    CANIBALISM("canibalism"),
    SWEET_FOOD("sweet food"),
    HOT_WEATHER("hot weather"),
    COLD_WEATHER("cold weather"),
    RAIN("rain"),
    CIVILIZATION("civilization"),
    EXERCISING,
    WORK,

    NATURE,
    MOUNTAINS,
    SWAMPS,
    HUGS,
    KISSES,

    ;

    private String description;
    private int chance;  //bigger = bigger chance to have this like/dislike

    LikesDislikes() {
        this.chance = 1;
        this.description = name().toLowerCase();
    }

    LikesDislikes(final String description) {
        this.description = description;
        this.chance = 1;
    }
}
