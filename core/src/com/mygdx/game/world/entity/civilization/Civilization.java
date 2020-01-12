package com.mygdx.game.world.entity.civilization;

import com.mygdx.game.world.entity.universe.GameEntity;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Civilization extends GameEntity {

    private String name;
    private int age;
    private int regionId;
    private transient RaceType racetype;
    private transient World world;
    private String worldCode;
    private NPC leader;
    private PoliticalStructure politicalStructure;
    private ForeignPolitics foreignPolitics;
    private String mainReligion;
    private Currency currency;

    private transient Town capitalTown;
    private String capitalTownCode;


}
