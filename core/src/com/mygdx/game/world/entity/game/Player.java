package com.mygdx.game.world.entity.game;

import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.enums.game.GameState;
import com.mygdx.game.world.enums.player.PlayerAction;
import com.mygdx.game.world.enums.town.BuildingType;
import com.mygdx.game.utils.DefaultHashMap;
import lombok.*;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player extends WorldEntity implements Serializable {

    public static final String PLAYER_CODE = "player";

    //descriptive
    private String name;

    private float gold = 1000;
    private int gems = 0;
    private int souls = 2;
    private float income = 0;
    private int adventurePoints = 0;
    private GameState gameState = GameState.MAIN_MENU;   //only for human players

    private PlayerAction playerAction = PlayerAction.IDLE;

    private transient World playersWorld;
    private String playersWorldCode;
    
    private transient World selectedWorld;
    private String selectedWorldCode;
    
    private Point moveDestInPixels;
    
    private boolean quickAdventure = false;
    
    private com.mygdx.game.world.enums.player.AIAction AIAction;
    
    private int AIActionDuration;
    
    private List<Hero> heroes = new ArrayList<>();
    
    private Map<BuildingType, Integer> buildings = new DefaultHashMap<>(0);

    private Map<PlayerProperty, Integer> properties = new DefaultHashMap(0);

    @Override
    public String getMapInfo() {
        return getName();
    }

    public Integer getArenaWins() {
        return getProperties().get(PlayerProperty.ARENA_WINS);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public enum PlayerProperty {
        ARENA_RANK,
        ARENA_WINS,
        ARENA_TIES,
        ARENA_LOSES,
    }

}
