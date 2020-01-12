package com.mygdx.game.world.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DungeonCreationParameters {

    private int minCount = 1;
    private int maxCount = 10;
    private int minHostility = 10;
    private boolean countBasedAlgorithm = false;
    private int tileProbability = 1;
}
