package com.mygdx.game.screen.strategymap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.World;

public class MapManager {

    public static final float UNIT_SCALE = 1f;
    private Game game;
    private TiledMap currentMap;

    public MapManager(final Game gameInstance) {
        game = gameInstance;
    }

    public TiledMap getCurrentMap(){
        final World playersWorld = game.getHumanPlayer().getSelectedWorld();

        if (playersWorld.getTiledMap() != null) {
            currentMap = playersWorld.getTiledMap();
        } else {
            TmxMapLoader loader = new TmxMapLoader();
            currentMap = loader.load("core/assets/maps/" + playersWorld.getCode() + ".tmx");
        }

        return currentMap;
    }
}
