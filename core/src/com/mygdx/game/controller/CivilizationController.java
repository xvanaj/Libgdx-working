package com.mygdx.game.controller;

import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.civilization.ForeignPolitics;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.enums.adventure.Direction;
import com.mygdx.game.utils.Randomizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CivilizationController {

    private static final String TAG = CivilizationController.class.getSimpleName();

    public void processBordersExpansion(final Game game, final Civilization civilization) {

        final int[][] regionsLayer = civilization.getWorld().getRegionsLayer();
        final int[][] hostilityLayer = civilization.getWorld().getHostilityLayer();

        final int regionId = civilization.getRegionId();

        final List<Point> points = new ArrayList<>();
        for (int i = 0; i < regionsLayer.length; i++) {
            for (int j = 0; j < regionsLayer[0].length; j++) {
                if (regionsLayer[i][j] == regionId) {
                    points.add(new Point(i, j));
                    //lower hostility on civ points by 1
                    hostilityLayer[i][j] = hostilityLayer[i][j] - 1;
                }
            }
        }

        int count = 0;
        while (points.iterator().hasNext()) {
            Point point = Randomizer.get(points);
            points.remove(point);

            //Direction direction = Randomizer.get(Direction.values());
            for (Direction direction : Direction.values()) {
                Point updatedPoint = direction.from(point);
                if (updatedPoint.x >= 0
                        && updatedPoint.y >= 0
                        && updatedPoint.x < regionsLayer.length
                        && updatedPoint.y < regionsLayer[0].length) {
                    if (Randomizer.range(0, 10) > hostilityLayer[updatedPoint.x][updatedPoint.y]
                            && regionsLayer[updatedPoint.x][updatedPoint.y] == 0
                            && hostilityLayer[updatedPoint.x][updatedPoint.y] < 10) {
                        regionsLayer[updatedPoint.x][updatedPoint.y] = regionId;
                        count++;
                    }
                }
            }
        }

        Game.log(TAG, civilization.getName() + " expanded its territory by " + count + " tiles", civilization.getWorld().getMinute());
    }

    public void updateCivilizationRelations(final Game game, final Civilization civilization) {
        final List<Civilization> worldCivs = civilization.getWorld().getCivilizations();
        final Map<Integer, Civilization> civsByRegions = worldCivs.stream().collect(Collectors.toMap(Civilization::getRegionId, civ -> civ));
        //civilization.getWorld().getCivilizationGraph().setRelation();

        final int[][] regionsLayer = civilization.getWorld().getRegionsLayer();

        final int regionId = civilization.getRegionId();

        final List<Point> points = new ArrayList<>();
        for (int i = 0; i < regionsLayer.length; i++) {
            for (int j = 0; j < regionsLayer[0].length; j++) {
                if (regionsLayer[i][j] == regionId) {
                    points.add(new Point(i, j));
                }
            }
        }

        for (Civilization civ : worldCivs) {
            int count = 0;
            for (Point point : points) {
                //Direction direction = Randomizer.get(Direction.values());

                //
                for (Direction direction : Direction.values()) {
                    Point updatedPoint = direction.from(point);

                    if (updatedPoint.x >= 0
                            && updatedPoint.y >= 0
                            && updatedPoint.x < regionsLayer.length
                            && updatedPoint.y < regionsLayer[0].length
                            && regionsLayer[updatedPoint.x][updatedPoint.y] == civ.getRegionId()) {
                        count++;
                    }
                }

            }

            if (count > 10) {
                String relationDesc = null;
                if (civilization.getForeignPolitics().equals(ForeignPolitics.AGGRESSIVE)) {
                    civilization.getWorld().getCivilizationGraph().setRelation(civilization.getCode(), civ.getCode(), -1);
                    relationDesc = civilization.getName() + " feels more aggresive against " + civ.getName();
                } else if (civilization.getForeignPolitics().equals(ForeignPolitics.PACIFIST)) {
                    civilization.getWorld().getCivilizationGraph().setRelation(civilization.getCode(), civ.getCode(), +1);
                    relationDesc = civilization.getName() + " feels more friendly " + civ.getName();
                }
                Game.log(TAG, civilization.getName() + " shares borders with " + civ.getName() + " civilization on " + count + " tiles. " + relationDesc, civilization.getWorld().getMinute());
            }
        }
    }

    public void randomCivilizationEvent(final Game game, final Civilization civilization) {

    }
}
