package com.mygdx.game.world.generator.game;

import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.game.Difficulty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCreationParameters {

    private boolean ai;
    private Difficulty difficulty = Difficulty.MEDIUM;
    private World world;
    private Universe universe;

}
