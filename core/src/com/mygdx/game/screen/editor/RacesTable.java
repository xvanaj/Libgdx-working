package com.mygdx.game.screen.editor;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.world.entity.universe.RaceType;

public class RacesTable extends Table {

    private int selected = 0;
    private RaceType selectedRace;

    public RacesTable(List<RaceType> raceTypes) {

    }
}
