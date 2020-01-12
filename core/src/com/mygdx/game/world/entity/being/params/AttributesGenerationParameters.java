package com.mygdx.game.world.entity.being.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributesGenerationParameters {

    private int minAttributeValue = 20;
    private int maxAttributeValue = 40;

    private boolean levelBasedValue = false;
    private int levelBasedValueVariance = 10;
    private boolean gaussianDistribution = true;

}
