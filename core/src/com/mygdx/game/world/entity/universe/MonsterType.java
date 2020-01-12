package com.mygdx.game.world.entity.universe;

import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.enums.being.Size;
import com.mygdx.game.world.enums.world.TerrainType;

import java.util.Set;

public class MonsterType {

    private String name;
    private int level;
    private Set<TerrainType> terrainTypes;

    private ElderBeing.SkinColor skinColor;
    private ElderBeing.HeadType headType;
    private MonsterSpeciesType monsterSpeciesType;
    private Size size;


}
