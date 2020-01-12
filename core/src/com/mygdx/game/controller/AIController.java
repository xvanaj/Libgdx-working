package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.player.AIAction;

import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Jakub Vana on 8.8.2018.
 */
public class AIController {


    private final Game game;

    public AIController(final Game game) {
        this.game = game;
    }

    public void evaluateAIAction(Player player, Game game) {

        if (player.getAIActionDuration() > 0) {
            player.setAIActionDuration(player.getAIActionDuration() -1);
        } else {
            if (player.getHeroes().stream().filter(hero -> hero.getHp() > 0).collect(Collectors.toList()).isEmpty()) {
                //ressurect if possible, otherwise player is dead? //todo
            } else if (player.getAdventurePoints() > 10 && player.getHeroes().stream().filter(o -> o.getHp() > 0).mapToInt(o -> o.getPower()).sum() > 150) {
                //go on adventure if heroes were idle for at least some time and power is bigger then x
            } else {
                Random random = new Random();
                int randomNumber = random.nextInt(100);

                AIAction aiAction;
                if (randomNumber < 10) {
                    aiAction = AIAction.BUILDER;
                } else if (randomNumber < 30) {
                    aiAction = AIAction.TRAINER;
                } else {
                    aiAction = AIAction.JOB;
                }

                //AIAction aiAction = AIAction.values()[new Random().nextInt(AIAction.values().length - 1) + 1];
                //AIAction aiType = player.getAIAction();  //todo unused yet

                switch (aiAction) {
                    case JOB:
                        Gdx.app.log("battleController",player.getName() + " found one month job for " + 7 + " gold coins and is busy now. ");
                        player.setGold(player.getGold() + 7);
                        player.setAIActionDuration(30);
                        break;
                    case BUILDER:
                        Gdx.app.log("ai",player.getName() + " is interested in towns infrastructure but it seems theres nothing he can do to help town. ");
                        player.setAIActionDuration(7);
                        break;
                    case TRAINER:
                        Gdx.app.log("ai",player.getName() + " wants to train his skills but there is no training facility in town? ");
                        player.setAIActionDuration(7);
                        break;
                    case ARENA_MASTER:
                        Gdx.app.log("ai",player.getName() + " wants to train his skills but there is no training facility in town? ");
                        player.setAIActionDuration(7);
                        break;
                }
            }
        }
    }
}
