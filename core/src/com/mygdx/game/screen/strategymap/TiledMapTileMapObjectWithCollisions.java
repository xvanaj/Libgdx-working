package com.mygdx.game.screen.strategymap;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;

public class TiledMapTileMapObjectWithCollisions extends TiledMapTileMapObject {

    public TiledMapTileMapObjectWithCollisions(final TiledMapTile tile, final boolean flipHorizontally, final boolean flipVertically) {
        super(tile, flipHorizontally, flipVertically);
    }
}
