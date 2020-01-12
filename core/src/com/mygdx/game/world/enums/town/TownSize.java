package com.mygdx.game.world.enums.town;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jakub Vana on 10.8.2018.
 */
@Getter
@AllArgsConstructor
public enum TownSize {
    ISOLATED_DWELLING("Isolated dwelling", 0, 50),
    HAMLET("hamlet", 50, 500),
    VILLAGE("village", 500, 1500),
    TOWN("town", 1500, 20000),
    LARGE_TOWN("large town", 20000, 100000),
    CITY("city", 100000, 300000),
    LARGE_CITY("large city", 300000, 1000000),
    METROPOLIS("metropolis", 1000000, 3000000),
    CONURBATION("conurbation", 3000000, 10000000),
    MEGALOPOLIS("megalopolis", 10000000, Integer.MAX_VALUE),
    ECUMENOPOLIS("ecumenopolis", Integer.MAX_VALUE, Integer.MAX_VALUE);

    private String name;
    private int minPopulation;
    private int maxPopulation;

}
