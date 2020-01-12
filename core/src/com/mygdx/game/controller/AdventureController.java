package com.mygdx.game.controller;

import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.adventure.Direction;
import com.mygdx.game.world.enums.game.GameState;
import com.mygdx.game.world.enums.world.TerrainType;

import java.awt.*;

public class AdventureController {

    private AmbushController ambushController;

    public void moveToAnotherTile(Game game, Direction direction, Player player) {
        Point originalPosition = player.getMapPosition();
        Point newPosition = getNewPosition(originalPosition, direction);

        if (!TerrainType.values()[player.getPlayersWorld().getTerrainLayer()[newPosition.x][newPosition.y]].isPassable()) {
            throw new UnsupportedOperationException("Cannot move to impassable tile " + newPosition);
        }

        player.setPosition(newPosition.x, newPosition.y);
        if (playerCameToTown(player, newPosition)) {
            player.setGameState(GameState.TOWN);
        } else if (playerCameToGate(player, newPosition)) {
            player.setGameState(GameState.GATE);
        } else {
            player.setGameState(GameState.ADVENTURE);
        }

        ambushController.checkForAmbush(game, player);
    }

    private boolean playerCameToTown(Player player, Point newPosition) {
        return player.getPlayersWorld().getTowns().values().stream().anyMatch(town -> town.getMapPosition().equals(newPosition));
    }

    private boolean playerCameToGate(Player player, Point newPosition) {
        return !player.getPlayersWorld().getGates().isEmpty()
                && player.getPlayersWorld().getGates().stream()
                .anyMatch(town -> (town.getMapPosition() != null && town.getMapPosition().equals(newPosition))
                        || (town.getMapPosition() != null && town.getMapPosition().equals(newPosition)));
    }

    private Point getNewPosition(Point originalPosition, Direction direction) {
        switch (direction) {
            case EAST:
                return new Point(originalPosition.x+1, originalPosition.y);
            case WEST:
                return new Point(originalPosition.x-1, originalPosition.y);
            case SOUTH:
                return new Point(originalPosition.x, originalPosition.y+1);
            case NORTH:
                return new Point(originalPosition.x, originalPosition.y-1);
        }
        throw new IllegalArgumentException("Direction has to be specified");
    }
}
