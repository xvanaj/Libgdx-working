package com.mygdx.game.world.entity.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.entity.EntityType;
import com.mygdx.game.world.entity.universe.GameEntity;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.utils.DefaultHashMap;
import com.mygdx.game.world.utils.LogEntry;
import com.strongjoshua.console.GUIConsole;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends GameEntity {

    private static transient GUIConsole console;

    private String name;
    private Difficulty difficulty = Difficulty.MEDIUM;
    private Universe universe;
    private String playerCode = "player";
    private DefaultHashMap<Integer, List<LogEntry>> gameLog = new DefaultHashMap<>(new ArrayList());
    private Map<EntityType, Long> codeSequences = new HashMap<>();
    {
        Arrays.stream(EntityType.values()).forEach(entityType -> codeSequences.put(entityType, 0L));
    }
    //private transient MessageController messageController;

    private transient Map<String, GameEntity> entities = new HashMap<>();

    private transient int gameSpeed = 1;
    private transient boolean fastMode = false;

    public Player getHumanPlayer() {
        return (Player) getGameEntity(playerCode);
    }

    public void setHumanPlayer(Player player) {
        setPlayerCode(player.getCode());
    }

    public GameEntity getGameEntity(final String code) {
        if (code == null) {
            throw new IllegalArgumentException("Code must not be null");
        }
        final GameEntity gameEntity = entities.get(code);
        if (gameEntity == null) {
            collectGameEntitiesToMap();
            return entities.get(code);
        } else {
            return gameEntity;
        }
    }

    private void collectGameEntitiesToMap() {
        entities.put(universe.getCode(), universe);
        universe.getWorlds().values().stream().forEach(world -> {
            entities.put(world.getCode(), world);
            world.getTowns().values().stream().forEach(town -> entities.put(town.getCode(), town));
            world.getCivilizations().stream().forEach(civ -> entities.put(civ.getCode(), civ));
            world.getAncients().stream().forEach(ancient -> entities.put(ancient.getCode(), ancient));
        });
        universe.getPlayers().stream().forEach(player -> {
            entities.put(player.getCode(), player);
            player.getHeroes().stream().forEach(hero -> entities.put(hero.getCode(), hero));
        });
    }

    public static void log(final String tag, final String message) {
        if (console != null) {
            console.log("[" + tag + "] " + message);
        }
        Gdx.app.log(tag, message);
    }

    public static void log(final String tag, final String message, final int time) {
        log(tag, String.format("%6s", time) + ": " + message);
    }

    public void setConsole(GUIConsole console) {
        this.console = console;
    }
}
