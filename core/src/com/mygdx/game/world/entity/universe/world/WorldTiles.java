package com.mygdx.game.world.entity.universe.world;

import com.mygdx.game.world.enums.world.tile.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorldTiles {

    private GrassTileType grassTileType;
    private DirtTileType dirtTileType;
    private SandTileType sandTileType;
    private SnowTileType snowTileType;
    private WaterTileType waterTileType;
    private DeepWaterTileType deepWaterTileType;
}
