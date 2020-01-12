package com.mygdx.game.world.enums.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jakub Vana on 12.9.2018.
 */
@Getter
@AllArgsConstructor
public enum GameState {

    MAIN_MENU("=================MAIN MAIN_MENU============="),
    TOWN("=================TOWN============="),
    TOWN_BUILDING("=================MAIN MAIN_MENU============="),
    NEW_GAME("=================NEW GAME============="),
    ADVENTURE("=================ADVENTURE============="),
    WORLD_PLACE("=================MAIN MAIN_MENU============="),
    QUITTING("=================MAIN MAIN_MENU============="),
    DEBUG("=================DEBUG============="),
    SETTINGS("=================DEBUG============="),
    GATE(""),;

    private String name;
}
