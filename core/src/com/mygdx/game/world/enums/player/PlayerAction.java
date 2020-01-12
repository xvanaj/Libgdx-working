package com.mygdx.game.world.enums.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerAction {

    IDLE("Idle"),
    MOVE_WEST("Traveling west"),
    MOVE_NORTH("Traveling north"),
    MOVE_SOUTH("Traveling south"),
    MOVE_EAST("Traveling east"),
    RAID("Searching for beasts"),
    EXPLORE("Exploring"),
    TOWN("In town"),
    REST("Resting");

    private String name;
}
