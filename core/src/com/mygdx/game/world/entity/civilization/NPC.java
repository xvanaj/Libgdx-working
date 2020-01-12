package com.mygdx.game.world.entity.civilization;

import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.character.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NPC {

    private String title, title2;
    private String name;
    private Gender gender;
    private transient RaceType raceType;
    private int x,y;
    private transient Town town;
    private String townCode;
    private transient NPC parent;
    private String parentCode;

}
