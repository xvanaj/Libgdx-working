package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.world.TerrainType;
import com.mygdx.game.world.generator.being.MonsterFactory;
import com.mygdx.game.utils.Randomizer;

import java.util.List;


public class AmbushController {

    public void checkForAmbush(Game game, Player player) {
        if (Randomizer.generate(1, 100) < 20) {
            Gdx.app.log("ambush", "You were amushed");

            int level = player.getPlayersWorld().getMonsterLevelLayer()[player.getMapPosition().x][player.getMapPosition().y];
            TerrainType terrainType = TerrainType.values()[player.getPlayersWorld().getTerrainLayer()[player.getMapPosition().x][player.getMapPosition().y]];

            MonsterFactory generator = new MonsterFactory();
            List<Being> beings = generator.generateMonsters(level, terrainType, Randomizer.generate(1,4), Utility.assetManager);

            BattleController battleController = new BattleController();
            battleController.evaluateBattle(player.getHeroes(), beings, null);
        }
    }
}
