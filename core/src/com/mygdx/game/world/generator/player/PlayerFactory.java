package com.mygdx.game.world.generator.player;

import com.mygdx.game.world.builder.PlayerBuilder;

import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.generator.game.PlayerCreationParameters;

public class PlayerFactory {

    public static Player createPlayer(PlayerCreationParameters parameters) {
        Player aiPlayer = new PlayerBuilder()
                .newAIPlayer(parameters.getDifficulty())
                .playersWorld(parameters.getWorld())
                .universe(parameters.getUniverse())
                .build();

        return aiPlayer;
    }
}
