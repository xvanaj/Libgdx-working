package com.mygdx.game.world.generator.world;

import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.world.TerrainType;
import com.mygdx.game.world.params.TerrainComposition;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Vana on 29.8.2018.
 */
public class TerrainGenerator {

    private float mountainsAmount = 0.25f;
    private float hillsAmount = 0.25f;
    private float lowlandsAmount = 0.25f;
    private float oceansAmount = 0.25f;

    public void setCustomTerrainComposition(float pcMountains, float pcHills, float pcLowlands, float pcWater) {
        float totalChance = pcMountains + pcHills + pcLowlands + pcWater;

        mountainsAmount = pcMountains * (1 / totalChance);
        hillsAmount = pcHills * (1 / totalChance);
        lowlandsAmount = pcLowlands * (1 / totalChance);
        oceansAmount = pcWater * (1 / totalChance);
    }


    public void fillWorldWithTerrain(World world) {
        float[][] heightmap = world.getHeightmap();
        float[][] temperatureLayer = world.getTemperatureLayer();
        float[][] humidityLayer = world.getHumidityLayer();

        world.setTerrainComposition(new TerrainComposition(mountainsAmount, hillsAmount, lowlandsAmount, oceansAmount));
        world.setTerrainLayer(new int[heightmap.length][heightmap[0].length]);

        float max = 0;
        float min = 1;

        for (int x = 0; x < heightmap.length; x++) {
            for (int y = 0; y < heightmap[x].length; y++) {
                if (heightmap[x][y] > max && heightmap[x][y] != 1) {
                    max = (heightmap[x][y]);
                }
                if (heightmap[x][y] < min && heightmap[x][y] != 0) {
                    min = (heightmap[x][y]);
                }
                //Defining coloring rules for each value
                //You may also use enums with switch case here
                world.getTerrainLayer()[x][y] = determineTerrain(heightmap[x][y], temperatureLayer[x][y], humidityLayer[x][y]).ordinal();
                world.getHeightmap()[x][y] = (float) Math.floor(world.getHeightmap()[x][y] * 15000 * (hillsAmount + mountainsAmount));
                world.getTemperatureLayer()[x][y] = (float) Math.floor(world.getTemperatureLayer()[x][y] * 100 - 50);
            }
        }
    }

    private TerrainType determineTerrain(float height, float temperature, float humidity) {
        //water
        if (height < oceansAmount / 3 * 2) {
            return TerrainType.DEEP_OCEAN;
        } else if (height < oceansAmount) {
            /*if (humidity < 0.7 && humidity > 0.3) {
                return TerrainType.GREEN_ISLAND;
            } else {*/
            return TerrainType.SHALLOW_OCEAN;
        }

        //lowlands
        else if (height < oceansAmount + lowlandsAmount) {
            if (temperature < 0.25) {
                return TerrainType.SNOWLAND;
            } else if (temperature < 0.5) {
                return TerrainType.DIRTLAND;
            } else if (temperature < 0.75) {
                return TerrainType.GRASSLAND;
            } else {
                return TerrainType.DESERT;
            }
        }

        //hills
        else if (height < oceansAmount + lowlandsAmount + hillsAmount) {
            if (temperature < 0.25) {
                return TerrainType.SNOW_HILL;
            } else if (temperature < 0.5) {
                return TerrainType.DRY_HILL;
            } else if (temperature < 0.75) {
                return TerrainType.GRASS_HILL;
            } else {
                return TerrainType.SAND_HILL;
            }
            //mountains
        } else if (height <= oceansAmount + lowlandsAmount + hillsAmount + mountainsAmount) {
            if (temperature < 0.25) {
                return TerrainType.SNOWED_MOUNTAIN;
            } else if (temperature < 0.5) {
                return TerrainType.DIRT_MOUNTAIN;
            } else if (temperature < 0.75) {
                return TerrainType.MOUNTAIN;
            } else {
                return TerrainType.SAND_MOUNTAIN;
            }
        }
        return TerrainType.EMPTYNESS;
    }

    public void generateWildernessLevels(World world) {
        world.setMonsterLevelLayer(new int[world.getHeightmap().length][world.getHeightmap()[0].length]);
        int[][] wildernessMap = world.getMonsterLevelLayer();
        List<Point> points = new ArrayList<>();

        world.getTowns().values().stream().forEach(town -> {
            wildernessMap[town.getX() / 32][town.getY() / 32] = -1;
            points.add(new Point(town.getX() / 32, town.getY() / 32));
        });

        int iterations = 0;
        while (points.iterator().hasNext()) {

            Point next = points.iterator().next();
            if ((next.x + 1) < world.getHeightmap().length && wildernessMap[next.x + 1][next.y] == 0 && world.getTerrainType(next.x + 1, next.y).isPassable()) {
                wildernessMap[next.x + 1][next.y] = wildernessMap[next.x][next.y] == -1 ? 1 : wildernessMap[next.x][next.y] + 1;
                points.add(new Point(next.x + 1, next.y));
            }
            if ((next.y + 1) < world.getHeightmap()[0].length && wildernessMap[next.x][next.y + 1] == 0 && world.getTerrainType(next.x, next.y + 1).isPassable()) {
                wildernessMap[next.x][next.y + 1] = wildernessMap[next.x][next.y] == -1 ? 1 : wildernessMap[next.x][next.y] + 1;
                points.add(new Point(next.x, next.y + 1));
            }
            if ((next.x - 1) > -1 && wildernessMap[next.x - 1][next.y] == 0 && world.getTerrainType(next.x - 1, next.y).isPassable()) {
                wildernessMap[next.x - 1][next.y] = wildernessMap[next.x][next.y] == -1 ? 1 : wildernessMap[next.x][next.y] + 1;
                points.add(new Point(next.x - 1, next.y));
            }
            if (((next.y - 1) > -1) && wildernessMap[next.x][next.y - 1] == 0 && world.getTerrainType(next.x, next.y - 1).isPassable()) {
                wildernessMap[next.x][next.y - 1] = wildernessMap[next.x][next.y] == -1 ? 1 : wildernessMap[next.x][next.y] + 1;
                points.add(new Point(next.x, next.y - 1));
            }
            points.remove(next);
            iterations++;
        }

        world.setMonsterLevelLayer(wildernessMap);
    }
}
