package com.mygdx.game.controller.time;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.controller.*;
import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.algorithm.CityGrowthAlgorithm;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.entity.being.InvasionArmy;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.GameEntity;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.Gate;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.events.SpecialEvent;
import com.mygdx.game.world.enums.world.TerrainType;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.mygdx.game.Settings.CHANCE_SPECIAL_EVENT;
import static com.mygdx.game.Settings.WORLD_EVENT_PREFIX;


/**
 * Created by Jakub Vana on 8.8.2018.
 */
public class TimeController {

    private final CityGrowthAlgorithm cityGrowthAlgorithm;
    private final CivilizationController civilizationController;
    private final GravityAgingController gravityAgingController;
    private final PlayerTimeController playerTimeController;
    private final ActionController actionController;
    private final AIController aiController;

    private final Game game;
    private final Map<Integer, List<EventTypeSubject>> eventsStack;

    private Stack stack;

    public TimeController(Game game) {
        this.game = game;
        this.eventsStack = game.getUniverse().getEventsStack();
        this.cityGrowthAlgorithm = new CityGrowthAlgorithm(game);
        this.actionController = new ActionController(game);
        this.aiController = new AIController(game);
        this.gravityAgingController = new GravityAgingController(game);
        this.playerTimeController = new PlayerTimeController(game);
        this.civilizationController = new CivilizationController();

        initializeEvents(game);
    }

    private void initializeEvents(final Game game) {
        final List<World> worlds = game.getUniverse().getWorlds().values().stream().collect(Collectors.toList());
        final List<Town> towns = new ArrayList<>();
        final List<ElderBeing> elders = game.getUniverse().getElders();

        worlds.stream().forEach(world -> towns.addAll(world.getTowns().values()));

        planNewEvent(EventType.UNIVERSE_EVENT, game.getUniverse());
        game.getUniverse().getPlayers().stream().forEach(player -> player.getHeroes().stream()
                .forEach(hero -> planNewEvent(EventType.GRAVITY_EVENT, hero)));

        worlds.stream().forEach(world -> planNewEvent(EventType.WORLD_EVENT, world));
        elders.stream().forEach(elder -> planNewEvent(EventType.ELDER_EVENT, elder));
        towns.stream().forEach(town -> planNewEvent(EventType.CITY_GROWTH, town));
        worlds.stream().forEach(world -> {
            world.getAncients().stream().forEach(ancientBeing -> planNewEvent(EventType.ANCIENTS_MOVE_EVENT, ancientBeing));
            world.getCivilizations().stream().forEach(civ -> planNewEvent(EventType.CIV_RANDOM_EVENT, civ));
            world.getCivilizations().stream().forEach(civ -> planNewEvent(EventType.CIV_BORDER_EXPANSION, civ));
        });
    }

    private void planNewEvent(final EventType eventType, final GameEntity gameEntity) {
        Integer eventTime = game.getUniverse().getMinute() + Randomizer.range(eventType.getMinMinutesToOccurence(), eventType.getMaxMinutesToOccurence());
        if (eventsStack.get(eventTime) == null) {
            eventsStack.put(eventTime, new ArrayList<>());
        }

        eventsStack.get(eventTime).add(new EventTypeSubject(eventType, gameEntity.getCode()));
    }

    private void addHour(World world, Game game) {
        world.setMinute(world.getMinute() + 60);

        if (world.getHour() % world.getHoursInDay() == world.getSunriseHour() + 1) {
            processNewDay(world, game);
        }

        if (world.getHour() % 8 == 0) {
            moveAncients(world);
        }
    }

    private void moveInvasions(World world) {
        world.getInvasions().stream().forEach(invasion -> {

            int westProb = getInvasionMovementProbability(world, invasion.getTileX() - 1, invasion.getTileY());
            int northProb = getInvasionMovementProbability(world, invasion.getTileX(), invasion.getTileY() - 1);
            int eastProb = getInvasionMovementProbability(world, invasion.getTileX() + 1, invasion.getTileY());
            int southProb = getInvasionMovementProbability(world, invasion.getTileX(), invasion.getTileY() + 1);
            int allProbs = westProb + northProb + eastProb + southProb;
            int random = Randomizer.generate(1, allProbs);

            if (random <= westProb) {
                invasion.setX(invasion.getTileX() - World.getPixelSize());
            } else if (random <= westProb + northProb) {
                invasion.setY(invasion.getTileY() - World.getPixelSize());
            } else if (random <= westProb + northProb + eastProb) {
                invasion.setX(invasion.getTileX() + World.getPixelSize());
            } else if (random <= westProb + northProb + eastProb + southProb) {
                invasion.setY(invasion.getTileY() + World.getPixelSize());
            }
        });
    }

    private void moveAncients(World world) {
        world.getAncients().stream().forEach(ancient -> {
            if (Randomizer.get(1, 7) == 1) {

                int westProb = getMovementProbability(world, ancient.getTileX() - 1, ancient.getTileY());
                int northProb = getMovementProbability(world, ancient.getTileX(), ancient.getTileY() - 1);
                int eastProb = getMovementProbability(world, ancient.getTileX() + 1, ancient.getTileY());
                int southProb = getMovementProbability(world, ancient.getTileX(), ancient.getTileY() + 1);
                int allProbs = westProb + northProb + eastProb + southProb;
                int random = Randomizer.range(1, allProbs);

                if (random <= westProb) {
                    ancient.setX(ancient.getX() - World.pixelSize);
                } else if (random <= westProb + northProb) {
                    ancient.setY(ancient.getY() - World.pixelSize);
                } else if (random <= westProb + northProb + eastProb) {
                    ancient.setX(ancient.getX() + World.pixelSize);
                } else if (random <= westProb + northProb + eastProb + southProb) {
                    ancient.setY(ancient.getY() + World.pixelSize);
                }
            }
        });
    }

    private void moveAncient(AncientBeing ancient) {
        final World world = ancient.getWorld();

        int westProb = getMovementProbability(world, ancient.getTileX() - 1, ancient.getTileY());
        int northProb = getMovementProbability(world, ancient.getTileX(), ancient.getTileY() - 1);
        int eastProb = getMovementProbability(world, ancient.getTileX() + 1, ancient.getTileY());
        int southProb = getMovementProbability(world, ancient.getTileX(), ancient.getTileY() + 1);
        int allProbs = westProb + northProb + eastProb + southProb;
        int random = Randomizer.range(0, allProbs);

        if (allProbs == 0) {
            Game.log("Move ancients", ancient.getName() + " cant move " + ancient.getX() + " x " + ancient.getY()  , world.getMinute());
        } else {
            if (random <= westProb) {
                ancient.setX(ancient.getX() - World.pixelSize);
            } else if (random <= westProb + northProb) {
                ancient.setY(ancient.getY() - World.pixelSize);
            } else if (random <= westProb + northProb + eastProb) {
                ancient.setX(ancient.getX() + World.pixelSize);
            } else if (random <= westProb + northProb + eastProb + southProb) {
                ancient.setY(ancient.getY() + World.pixelSize);
            }

            ancient.getTextureMapObject().setX(ancient.getX());
            ancient.getTextureMapObject().setY(ancient.getY());
            Game.log("Move ancients", ancient.getName() + " moved to " + ancient.getX() + " x " + ancient.getY(), world.getMinute());
        }
    }


    private int getInvasionMovementProbability(World world, int x, int y) {
        int maxValue = world.getMaxOrMinFromLayer(world.getMonsterLevelLayer(), true);
        if (x < 0 || y < 0 || x >= world.getWidth() || y >= world.getHeight() || !TerrainType.values()[world.getTerrainLayer()[x][y]].isPassable()) {
            return 0;
        }

        return maxValue - world.getLevel(new Point(x, y));
    }

    private int getMovementProbability(World world, int x, int y) {
        //note y <= 0 because of flipped y coords
        if (x < 0 || y < 0 || x >= world.getWidth() || y >= world.getHeight() || !TerrainType.values()[world.getTerrainLayer()[x][y]].isPassable()) {
            //if (x < 0 || y <= 0 || x >= world.getWidth() || y >= world.getHeight() || !world.getTerrain()[x][world.getHeight() - y].isPassable()) {
            return 0;
        }

        return (int) Math.sqrt(world.getLevel(new Point(x, y)));
    }

    public void addDays(int days, Game game) {
        game.getUniverse().getWorlds().values().stream().forEach(world -> {
            int endingDay = world.getDay() + days;
            boolean continueAddingDays = true;

            while (continueAddingDays && world.getDay() != endingDay) {
                continueAddingDays = processNewDay(world, game);
            }
        });
    }

    private boolean processEndOfYear(World game) {
        Gdx.app.log("town", WORLD_EVENT_PREFIX + "Year " + game.getDay() / 365 + " started.");
        return true;
    }

    private boolean processNewDay(World world, Game game) {
        int currentDay = world.getDay();

        if (currentDay % 365 == 0) {
            if (!processEndOfYear(world)) {
                return false;
            }
        }

        if (currentDay % 30 == 0) {
            if (!processEndOfMonth(world)) {
                return false;
            }
        }

        if (currentDay % 7 == 0) {
            if (!processEndOfWeek()) {
                return false;
            }
        }

        if (false) {
            moveInvasions(world);
        }

        if (true && world.getInvasions().size() < world.getGates().size()) {
            InvasionArmy invasionArmy = new InvasionArmy();
            List<Gate> gates = world.getGates();
            final Gate gate = Randomizer.get(gates);

            invasionArmy.setWorldCode(world.getCode());
            invasionArmy.setPosition(gate.getX(), gate.getY());
            world.getInvasions().add(invasionArmy);
        }

/*
        for (String playerName : game.getUniverse().getPlayers().stream().map(p -> p.getCode()).collect(Collectors.toList())) {
            if (!playerName.equals(PLAYER_CODE)) {
                Gdx.app.log("player", "Its time for " + playerName + " to play.");
                aiController.evaluateAIAction(game.getUniverse().getPlayers().get(playerName), game);
            }
        }*/

        if (new Random().nextInt(1000) < CHANCE_SPECIAL_EVENT) {
            processSpecialEvent(world);
            if (game.getHumanPlayer() != null) {
                //return !questionController.processYesNoQuestion("Do you want to react on world event? (y)es or (n)o");
            }
        }

        return true;
    }

    private boolean processEndOfWeek() {
        //loggingUtils.printOutput(WORLD_EVENT_PREFIX + "City is getting new supplies!!");
        //loggingUtils.printOutput(WORLD_EVENT_PREFIX + "Arena is filled with new monsters!!");
        return true;
    }

    private boolean processSpecialEvent(World world) {
        SpecialEvent event = SpecialEvent.values()[new Random().nextInt(SpecialEvent.values().length)];

        //loggingUtils.printOutput(WORLD_EVENT_PREFIX + event.getText());
        switch (event) {
            case DISASTER:
                break;
            case WAR:
                break;
            case PLAGUE:
                break;
            case IMMIGRANTS:
                break;
            case NEW_TOWN:
                break;
            default:
                throw new IllegalArgumentException("EVENT " + event + " not implemented yet");
        }

        return true;
    }

    private boolean processEndOfMonth(World world) {
        for (Town town : world.getTowns().values()) {
            cityGrowthAlgorithm.addCitizensAfterMonth(town);
        }
        return true;
    }

    public void addMinutes(final Game gameInstance, int minutes) {
        final Universe universe = gameInstance.getUniverse();
        universe.setMinute(universe.getMinute() + minutes);
        /*if (universe.getMinute() == 60) {
            universe.setHour(universe.getHour());
            eventsStack.keySet().stream().forEach(eventType -> eventsStack.put(eventType, eventsStack.get(eventType) - 1));
        }
*/
        universe.getWorlds().values().stream().forEach(world -> addMinutes(minutes, world));
        //processEvents
        for (int i = universe.getLastEventExecution(); i < universe.getMinute(); i++) {
            final List<EventTypeSubject> events = eventsStack.get(i);
            if (events == null) {
                continue;
            }

            for (EventTypeSubject event : events) {
                final GameEntity gameEntity = game.getGameEntity(event.getSubjectCode());

                switch (event.getEventType()) {
                    case CITY_GROWTH:
                        cityGrowthAlgorithm.addCitizens((Town) gameEntity);
                        break;
                    case CIV_RANDOM_EVENT:
                        civilizationController.randomCivilizationEvent(game, (Civilization) gameEntity);
                        break;
                    case CIV_BORDER_EXPANSION:
                        civilizationController.processBordersExpansion(game, (Civilization) gameEntity);
                        break;
                    case CIV_RELATIONS_UPDATE:
                        civilizationController.updateCivilizationRelations(game, (Civilization) gameEntity);
                        break;
                    case GRAVITY_EVENT:
                        gravityAgingController.ageHero(game, (Hero) gameEntity);
                        break;
                    case WORLD_EVENT:
                        break;
                    case ELDER_EVENT:
                        break;
                    case ANCIENTS_MOVE_EVENT:
                        moveAncient((AncientBeing) gameEntity);
                        break;
                    case UNIVERSE_EVENT:
                        break;
                }
                planNewEvent(event.getEventType(), gameEntity);
            }
            eventsStack.remove(i);
        }

        universe.setLastEventExecution(universe.getLastEventExecution() + minutes);
        //universe.getPlayers().values().stream().forEach(player -> playerTimeController.processTime(10, player));
    }

    private void addMinutes(final int minutes, final World world) {
        world.setMinute(world.getMinute() + minutes);
    }
}
