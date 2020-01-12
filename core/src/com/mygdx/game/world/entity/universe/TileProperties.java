package com.mygdx.game.world.entity.universe;

import com.mygdx.game.world.enums.world.TerrainType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class TileProperties {
    private int level;
    private int hostility;
    private float height;
    private float temperature;
    private float humidity;
    private int explored;
    private TerrainType terrainType;
    private List<Resource> resources;

}
