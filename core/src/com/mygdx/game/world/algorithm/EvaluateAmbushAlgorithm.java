package com.mygdx.game.world.algorithm;

import com.badlogic.gdx.Game;
import com.mygdx.game.controller.BattleController;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.enums.adventure.Ambush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jakub Vana on 10.8.2018.
 */
public class EvaluateAmbushAlgorithm {

    public static Random rand = new Random();

    public static void evaluateAmbush(Game game, List<Being> adventurers, Ambush ambushType) {
        List<Being> monsters = new ArrayList<>();
        BattleController battle = new BattleController();
        battle.evaluateBattle(adventurers, monsters, null);
    }

}
