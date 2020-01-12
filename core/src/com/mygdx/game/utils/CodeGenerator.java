package com.mygdx.game.utils;

import com.mygdx.game.world.entity.EntityType;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.universe.GameEntity;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class CodeGenerator {

    private static final String GAME_CODE_PREFIX = "Game";


    @Deprecated
    public static String generateCode(final EntityType entityType, final Game game) {
        final Collection<World> worlds = getWorlds(game);
        final List<Town> towns = new ArrayList<>();
        worlds.stream().forEach(world -> towns.addAll(world.getTowns().values()));
        final List<Player> players = game.getUniverse().getPlayers();

        final Set<String> usedNames = new HashSet<>();
        int startingIndex = 0;

        switch (entityType) {
            case UNIVERSE:
                return entityType.name().substring(0,1) + entityType.name().toLowerCase().substring(1) + "1";
            case HERO:
                for (Player player : players) {
                    usedNames.addAll(player.getHeroes().stream().map(hero -> hero.getCode()).collect(Collectors.toList()));
                }
                break;
            case GAME:
                return entityType.name().substring(0,1) + entityType.name().toLowerCase().substring(1) + "1";
            case TOWN:
                usedNames.addAll(towns.stream().map(town -> town.getCode()).collect(Collectors.toList()));
                break;
            case WORLD:
                usedNames.addAll(worlds.stream().map(world -> world.getCode()).collect(Collectors.toList()));
                break;
            case CIVILIZATION:
                for (World world : worlds) {
                    usedNames.addAll(world.getCivilizations().stream().map(civ -> civ.getCode()).collect(Collectors.toList()));
                }
                break;
            case PLAYER:
                usedNames.addAll(players.stream().map(player -> player.getCode()).collect(Collectors.toList()));
                break;
            case ANCIENT:
                for (World world : worlds) {
                    usedNames.addAll(world.getAncients().stream().map(ancient -> ancient.getCode()).collect(Collectors.toList()));
                }
                break;
        }
        usedNames.remove(null);

        if (usedNames.size() == 0) {
            return entityType.name().substring(0,1) + entityType.name().substring(1).toLowerCase() + "1";
        } else {
            final List<String> sortedList = usedNames.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
            final String integerPart = sortedList.get(sortedList.size() - 1).substring(entityType.name().length());

            return entityType.name().substring(0,1) + entityType.name().substring(1).toLowerCase() + (Integer.valueOf(integerPart) + 1);
        }
    }

    private static Collection<World> getWorlds(final Game game) {
        return (game.getUniverse() == null || game.getUniverse().getWorlds() == null) ? new ArrayList<>() : game.getUniverse().getWorlds().values();
    }

    public static void generateEntityCodes(final Game game) {
        setCodeIfNull(game, game, EntityType.GAME);
        setCodeIfNull(game, game.getUniverse(), EntityType.UNIVERSE);

        game.getUniverse().getWorlds().values().stream().forEach(world -> {
            setCodesIfNull(game, world.getAncients(), EntityType.ANCIENT);
            setCodeIfNull(game, world, EntityType.WORLD);
            setCodesIfNull(game, world.getTowns().values(), EntityType.TOWN);
            setCodesIfNull(game, world.getCivilizations(), EntityType.CIVILIZATION);
        });

        setCodesIfNull(game, game.getUniverse().getPlayers(), EntityType.PLAYER);
        game.getUniverse().getPlayers().stream().forEach(player -> {
            setCodesIfNull(game, player.getHeroes(), EntityType.HERO);
        });
    }

    private static <T extends GameEntity> void setCodesIfNull(final Game game, final Collection<T> entities, final EntityType entityType) {
        for (GameEntity entity : entities) {
            if (entity.getCode() == null) {
                entity.setCode(getNextSequenceNumber(game, entityType));
            }
        }
    }

    private static void setCodeIfNull(final Game game, final GameEntity entity, final EntityType entityType) {
        if (entity.getCode() == null) {
            entity.setCode(getNextSequenceNumber(game, entityType));
        }
    }

    public static String getNextSequenceNumber(final Game game, final EntityType entityType) {
        game.getCodeSequences().put(entityType, game.getCodeSequences().get(entityType) + 1l);
        return (entityType.name().toLowerCase().concat(String.valueOf(game.getCodeSequences().get(entityType))));
    }
}
