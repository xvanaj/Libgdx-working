package com.mygdx.game.world.generator.being;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HeroClass {
    Mage(3, 10, 3, 12),
    Warrior(12, 3, 10, 3),
    Archer(10, 12, 3, 3),
    ;

    private int minStr, minAgi, minEnd, minInt;


}
