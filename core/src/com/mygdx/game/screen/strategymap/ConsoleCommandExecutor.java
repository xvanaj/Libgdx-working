package com.mygdx.game.screen.strategymap;

import com.mygdx.game.world.entity.game.Game;
import com.strongjoshua.console.CommandExecutor;

public class ConsoleCommandExecutor extends CommandExecutor {

    final Game game;

    public ConsoleCommandExecutor(Game game) {
        this.game = game;
    }

}
