package com.mygdx.game.world.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceCreationParameters {

    private int minCount = 0;
    private int maxCount = 999;
    private int minHostility = 10;
    private boolean countBasedAlgorithm = false;
    private int tileProbability = 10;

}
