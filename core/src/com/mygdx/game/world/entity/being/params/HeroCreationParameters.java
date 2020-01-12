package com.mygdx.game.world.entity.being.params;

import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.generator.being.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeroCreationParameters extends BeingCreationParameters {

    private Civilization civilization;
    private HeroClass heroClass;
    private StartingGear startingGear = StartingGear.MINIMAL;
    private boolean firstBorn;
    private World world;
    private Town town;


}
