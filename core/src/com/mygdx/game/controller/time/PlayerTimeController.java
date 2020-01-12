package com.mygdx.game.controller.time;

import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;

public class PlayerTimeController {

    private final Game game;
    private final HeroTimeController heroTimeController;

    public PlayerTimeController(final Game game) {
        this.game = game;

        heroTimeController = new HeroTimeController(game);
    }

    public void processTime(int minutes, Player player) {
        for (Hero hero : player.getHeroes()) {
            heroTimeController.performActions(minutes, hero);
        }
    }
}
