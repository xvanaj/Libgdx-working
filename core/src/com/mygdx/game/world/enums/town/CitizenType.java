package com.mygdx.game.world.enums.town;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CitizenType {

    COMMONER(35, "Commoner"),
    FARMER(25, "Farmer"),
    BUILDER(25, "Builder"),
    SOCIAL(10, "Social worker"),
    ARMY(5, "Soldier"),

    ;

    private int chance;
    private String name;


}
