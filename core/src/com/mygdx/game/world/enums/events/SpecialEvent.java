package com.mygdx.game.world.enums.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jakub Vana on 10.8.2018.
 */
@Getter
@AllArgsConstructor
public enum SpecialEvent {
    DISASTER("Torrential rains unleashed floods and set off landslides west from the city. Some buildings were destroyed"),
    WAR("City was attacked by competing tower. Some of the population were drafted to war."),
    IMMIGRANTS("New wave of immigrants were draught to the city. Population increases"),
    PLAGUE("Unknown plague hit the lands. Minority of population was destroyed"),
    NEW_TOWN("You heard about new town funded somewhere nearby");

    private String text;
}
