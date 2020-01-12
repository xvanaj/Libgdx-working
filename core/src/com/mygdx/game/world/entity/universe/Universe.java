package com.mygdx.game.world.entity.universe;

import com.mygdx.game.controller.EventTypeSubject;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.universe.world.World;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Universe extends GameEntity {

    private List<Player> players = new ArrayList();
    private List<ElderBeing> elders = new ArrayList<>();
    private List<SpellResearch> spellResearches = new ArrayList<>();

    private HashMap<String, World> worlds = new HashMap<>();
    private Map<Integer, List<EventTypeSubject>> eventsStack = new HashMap<>();
    //private List<EventTypeSubject> events = new SortedList<>();
    private int lastEventExecution;

    private int minute;

    //private List<Resource>
}
