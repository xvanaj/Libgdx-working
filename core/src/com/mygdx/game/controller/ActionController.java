package com.mygdx.game.controller;

import com.mygdx.game.world.entity.universe.TileProperties;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.player.PlayerAction;
import com.mygdx.game.world.enums.world.TerrainType;
import com.mygdx.game.world.utils.EntryType;
import com.mygdx.game.world.utils.LogEntry;

import java.awt.*;

public class ActionController {

    private final Game game;
    private RaidController raidController;

    public ActionController(final Game game) {
        this.game = game;
    }

    public void processPlayerAction(Game game, Player player) {
/*        Assert.notNull(player);
        Assert.notNull(player.getPlayerAction());*/

        if (!player.getPlayerAction().equals(PlayerAction.IDLE) && !player.getPlayerAction().equals(PlayerAction.TOWN)) {
            player.getHeroes().stream().forEach(hero -> hero.setFatigue(hero.getFatigue() + 1));
            //ConsoleUtils.print("Heroes got one fatigue point");
            player.getHeroes().stream().filter(hero -> hero.getFatigue() > hero.getEndurance()).forEach(hero -> {
                hero.setHp((int) (hero.getHp() - hero.getFatigue() - (int) hero.getEndurance()));
                if (hero.getHp() < 1) {
                    //ConsoleUtils.print("Hero " + hero.getName() + " died of exhaustion");
                }
            });
        }

        switch (player.getPlayerAction()) {
            case TOWN:
                break;
            case MOVE_WEST:
            case MOVE_EAST:
            case MOVE_NORTH:
            case MOVE_SOUTH:
                processMoveAction(game, player);
                break;
            case RAID:
                processRaidAction(game, player);
                break;
            case EXPLORE:
                processExploreAction(player);
                break;
            case REST:
            case IDLE: {
                player.getHeroes().stream().forEach(hero -> {
                    hero.setFatigue(hero.getFatigue() - 2);
                    hero.setHp(hero.getHp() + (int) hero.getEndurance());
                    if (hero.getHp() > hero.getHpMax()) {
                        hero.setHp(hero.getHpMax());
                    }
                    hero.setMp(hero.getMp() + (int) hero.getIntelligence());
                    if (hero.getMp() > hero.getMpMax()) {
                        hero.setMp(hero.getMpMax());
                    }
                });
            }
            break;
        }

    }

    private void processRaidAction(Game game, Player player) {
        raidController.checkForEnemies(game, player);
    }

    private void processExploreAction(Player player) {
        Point playerPos = player.getMapPosition();
        World playersWorld = player.getPlayersWorld();

        TileProperties tile = playersWorld.getTileProperties(playerPos);

        if (tile.getExplored() < 100) {
            playersWorld.getExplorationLayer()[playerPos.x][playerPos.y] = tile.getExplored() + 1;
        }
    }

    private void processMoveAction(Game game, Player player) {
        TerrainType targetTile = getTargetTile(player);
        int difficulty = targetTile.getDifficulty();
        int pixelSize = player.getPlayersWorld().getPixelSize();
        int pixelsPassed = 5 - difficulty;
        int x = player.getMapPosition().x;
        int y = player.getMapPosition().y;

        if (player.getPlayerAction().equals(PlayerAction.MOVE_WEST)) {
            pixelsPassed = x % pixelSize == 0 || x % pixelSize > pixelsPassed
                    ? pixelsPassed
                    : x % pixelSize;
            x = x - pixelsPassed;
        } else if (player.getPlayerAction().equals(PlayerAction.MOVE_EAST)) {
            pixelsPassed = x % pixelSize > pixelSize - pixelsPassed ? pixelSize - (x % pixelSize) : pixelsPassed;
            x = x + pixelsPassed;
        } else if (player.getPlayerAction().equals(PlayerAction.MOVE_NORTH)) {
            pixelsPassed = y % pixelSize == 0 || y % pixelSize > pixelsPassed
                    ? pixelsPassed
                    : y % pixelSize;
            y = y - pixelsPassed;
        } else if (player.getPlayerAction().equals(PlayerAction.MOVE_SOUTH)) {
            pixelsPassed = y % pixelSize > pixelSize - pixelsPassed ? pixelSize - (y % pixelSize) : pixelsPassed;
            y = y + pixelsPassed;
        } else {
            throw new UnsupportedOperationException("This is not a movement action");
        }
        player.setPosition(x, y);

        //movement completed
        if (processMovementCompleted(player, targetTile, pixelSize)) {
            player.setPosition(player.getMapPosition().x / pixelSize, player.getMapPosition().y / pixelSize);
            game.getGameLog().get(player.getPlayersWorld().getDay()).add(new LogEntry(EntryType.PLAYER, null,
                    "Party arrived at " + targetTile.name()
                            + " [" + player.getMapPosition().x + "/" + player.getMapPosition().y + "]"));
            game.setGameSpeed(0);
        }
    }

    private boolean processMovementCompleted(Player player, TerrainType targetTile, int pixelSize) {
        if (player.getMapPosition().x % pixelSize == 0 && player.getMapPosition().y % pixelSize == 0) {
            player.setPlayerAction(PlayerAction.IDLE);
            return true;
        }
        return false;
    }

    private TerrainType getTargetTile(Player player) {
        TerrainType targetTile;
        final World world = player.getPlayersWorld();

        if (player.getPlayerAction().equals(PlayerAction.MOVE_WEST)) {
            targetTile = TerrainType.values()[world.getTerrainLayer()[player.getMapPosition().x - 1][player.getMapPosition().y]];
        } else if (player.getPlayerAction().equals(PlayerAction.MOVE_EAST)) {
            targetTile = TerrainType.values()[world.getTerrainLayer()[player.getMapPosition().x + 1][player.getMapPosition().y]];
        } else if (player.getPlayerAction().equals(PlayerAction.MOVE_NORTH)) {
            targetTile = TerrainType.values()[world.getTerrainLayer()[player.getMapPosition().x][player.getMapPosition().y - 1]];
        } else if (player.getPlayerAction().equals(PlayerAction.MOVE_SOUTH)) {
            targetTile = TerrainType.values()[world.getTerrainLayer()[player.getMapPosition().x][player.getMapPosition().y + 1]];
        } else {
            throw new UnsupportedOperationException("This is not a movement action");
        }
        return targetTile;
    }
}
