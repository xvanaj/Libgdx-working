package com.mygdx.game.world.utils;

import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.game.Player;

public class PlayerUtils {

    public static void addCombatVictory(BattleContext battleContext, Player player1, Player player2) {
        if (battleContext.getHeroesWon() == null) {
            player1.getProperties().put(Player.PlayerProperty.ARENA_TIES, player1.getProperties().get(Player.PlayerProperty.ARENA_TIES) + 1);
            player2.getProperties().put(Player.PlayerProperty.ARENA_TIES, player2.getProperties().get(Player.PlayerProperty.ARENA_TIES) + 1);
        } else if (battleContext.getHeroesWon()) {
            player1.getProperties().put(Player.PlayerProperty.ARENA_WINS, player1.getProperties().get(Player.PlayerProperty.ARENA_WINS) + 1);
            player2.getProperties().put(Player.PlayerProperty.ARENA_LOSES, player2.getProperties().get(Player.PlayerProperty.ARENA_LOSES) + 1);
        } else {
            player2.getProperties().put(Player.PlayerProperty.ARENA_WINS, player2.getProperties().get(Player.PlayerProperty.ARENA_WINS) + 1);
            player1.getProperties().put(Player.PlayerProperty.ARENA_LOSES, player1.getProperties().get(Player.PlayerProperty.ARENA_LOSES) + 1);
        }
    }
}
