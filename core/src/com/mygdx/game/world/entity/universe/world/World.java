package com.mygdx.game.world.entity.universe.world;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.controller.civilization.CivilizationGraph;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.being.InvasionArmy;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.entity.universe.*;
import com.mygdx.game.world.enums.world.ResourceType;
import com.mygdx.game.world.enums.world.TerrainType;
import com.mygdx.game.world.enums.world.WorldType;
import com.mygdx.game.world.exception.WorldCreationException;
import com.mygdx.game.world.params.TerrainComposition;
import lombok.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class World extends GameEntity implements IEncyclopediaItem {

    private int minute = 0;
    private Map<Integer, WorldEntity> entitiesMap;
    private WorldTiles worldTiles;
    private transient Universe universe;
    private String universeCode;

    private String imageFileName;
    private int width, height;
    private int hoursInDay;
    private int daysInYear;
    private int sunriseHour;
    private int sunsetHour;


    private float[][] heightmap;
    private float[][] humidityLayer;
    private int[][] vegetationLayer;
    private float[][] temperatureLayer;
    private int maxTemperature, minTemperature, minHeight, maxHeight, avgHeight, avgTemperature;
    private float gravityG = 1.0f;
    private int[][] terrainMap;
    private int[][] voronoiMap;
    private int[][] monsterLevelLayer;
    private int[][] explorationLayer;
    private int[][] hostilityLayer;
    private int[][] regionsLayer;
    private TerrainComposition terrainComposition;
    private int[][] terrainLayer;
    private ResourceType resourceType;
    private WorldType worldType;
    private List<Civilization> civilizations = new ArrayList<>();
    private List<Gate> gates = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();
    private List<Dungeon> dungeons = new ArrayList<>();
    private List<AncientBeing> ancients = new ArrayList<>();
    private List<InvasionArmy> invasions = new ArrayList<>();
    private List<RaceType> races = new ArrayList<>();
    private List<NatureWonder> natureWonders = new ArrayList<>();
    private String name;
    private int worldPopulation;
    private Map<String, Town> towns = new HashMap<>();
    private CivilizationGraph civilizationGraph;
    public static final int pixelSize = 32;
    private String tiledMapCode;
    private String tilesetName;
    private transient TiledMap tiledMap;

    public static int getPixelSize() {
        return pixelSize;
    }

    public int getHour() {
        return (getMinute() / 60) % getHoursInDay();
    }

    public int getDay() {
        return (getMinute() / 60) / getHoursInDay();
    }

    public int getYear() {
        return (getDay()) / getDaysInYear();
    }

    public void addTowns(List<Town> towns) throws WorldCreationException {
        Random random = new Random();

        for (Town town : towns) {
            Point randomPoint = findPositionForTown(random);

            town.setX(randomPoint.x);
            town.setY(randomPoint.y);
            this.towns.put(town.getName(), town);
        }
    }

    private Point findPositionForTown(Random r) throws WorldCreationException {
        int minimumDistance = 15 * 32;
        boolean distanceOk = false;
        int counts = 0;
        Point randomPoint = null;

        while (!distanceOk) {
            if (counts >= 100) {
                minimumDistance = minimumDistance - 32;
            }
            if (minimumDistance < 4 * 32) {
                throw new WorldCreationException("Too many towns? Cant place them in sufficient distance");
            }
            randomPoint = new Point(r.nextInt(heightmap.length) * 32, r.nextInt(heightmap[0].length) * 32);
            Point finalRandomPoint = randomPoint;
            int finalMinimumDistance = minimumDistance;
            distanceOk = TerrainType.values()[this.terrainLayer[finalRandomPoint.x / 32][finalRandomPoint.y / 32]].isPassable() && getTowns().values().stream().allMatch(town1 ->
                    ((Math.abs(town1.getX() - finalRandomPoint.x) + Math.abs(town1.getY() - finalRandomPoint.y)) > finalMinimumDistance));
            counts++;
        }
        //transform to pixel coordinates
        return new Point(randomPoint.y, randomPoint.x);
    }

    @Override
    public String toString() {
        /*StringBuffer buffer = new StringBuffer();
        for (int x = 0; x < terrain.length; x++) {
            for (int y = 0; y < terrain[0].length; y++) {
                buffer.append(terrain[x][y].getName().substring(0, 1));
            }
            buffer.append(System.lineSeparator());
        }
        return buffer.toString();*/
        return name;
    }

    public float getHeight(Point point) {
        return heightmap[point.x][point.y];
    }

    public float getTemperature(Point point) {
        return temperatureLayer[point.x][point.y];
    }

    public float getHumidity(Point point) {
        return humidityLayer[point.x][point.y];
    }

    public int getLevel(Point point) {
        return monsterLevelLayer[point.x][point.y];
    }

    public int getExplorationLevel(Point point) {
        return explorationLayer[point.x][point.y];
    }

    public int getHostility(Point point) {
        return hostilityLayer[point.x][point.y];
    }

    public TerrainType getTerrainType(Point point) {
        return TerrainType.values()[this.terrainLayer[point.x][point.y]];
    }

    public TerrainType getTerrainType(int x, int y) {
        return TerrainType.values()[this.terrainLayer[x][y]];
    }


    public TileProperties getTileProperties(Point point) {
        TileProperties tileProperties = new TileProperties();

        tileProperties.setExplored(getExplorationLevel(point));
        tileProperties.setHeight(getHeight(point));
        tileProperties.setTemperature(getTemperature(point));
        tileProperties.setHostility(getHostility(point));
        tileProperties.setHumidity(getHumidity(point));
        tileProperties.setLevel(getLevel(point));
        tileProperties.setTerrainType(getTerrainType(point));
        tileProperties.setResources(getMaterials(point));

        return tileProperties;
    }

    private List<Resource> getMaterials(final Point point) {
        return resources.stream().filter(resource -> resource.getTilePosition().equals(point)).collect(Collectors.toList());
    }

    @Override
    public String getEncyclopediaDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<h3>");
        sb.append(name);
        sb.append("</h3>");
        sb.append("<span style=\"color:#00BFFF;\">Humidity:  " + getMaxOrMinFromLayer(getHumidityLayer(), true) + " </span>");
        sb.append("<span style=\"color:#00BFFF;\">Min humidity:  " + getMaxOrMinFromLayer(getHumidityLayer(), false) + " </span>");
        sb.append("<span style=\"color:#00BFFF;\">Max temperature:  " + getMaxOrMinFromLayer(getTemperatureLayer(), true) + " </span>");
        sb.append("<span style=\"color:#00BFFF;\">Min temperature:  " + getMaxOrMinFromLayer(getTemperatureLayer(), false) + " </span>");
        sb.append("<span style=\"color:#00BFFF;\">Max hostility:  " + getMaxOrMinFromLayer(getHostilityLayer(), true) + " </span>");

        sb.append("<span style=\"color:#00BFFF;\">Resources: </span>");
        for (Resource resource : getResources()) {
            sb.append(resource.getResourceType().name() + " " + resource.getResourceType().getColor() + "<br/>");
        }
        sb.append("</html>");
        return sb.toString();
    }

    public float getMaxOrMinFromLayer(float[][] layer, boolean maxValue) {
        float value = 0;
        if (maxValue) {
            value = -1;
            for (int i = 0; i < layer.length; i++) {
                for (int j = 0; j < layer[0].length; j++) {
                    if (layer[i][j] > 0 && layer[i][j] > value) {
                        value = layer[i][j];
                    }
                }
            }
        } else {
            value = 1;
            for (int i = 0; i < layer.length; i++) {
                for (int j = 0; j < layer[0].length; j++) {
                    if (layer[i][j] < value) {
                        value = layer[i][j];
                    }
                }
            }
        }

        return value;
    }

    public Point getMaxOrMinPointFromLayer(float[][] layer, boolean maxValue) {
        Point point = null;
        if (maxValue) {
            point = new Point(0,0);
            for (int i = 0; i < layer.length; i++) {
                for (int j = 0; j < layer[0].length; j++) {
                    if (layer[i][j] > 0 && layer[i][j] >= layer[point.x][point.y]) {
                        point.setLocation(j,i);
                    }
                }
            }
        } else {
            for (int i = 0; i < layer.length; i++) {
                for (int j = 0; j < layer[0].length; j++) {
                    if (layer[i][j]!=0 && (point == null || layer[i][j] <= layer[point.x][point.y]) ){
                        if (point == null) {
                            point = new Point(j,i);
                        } else {
                            point.setLocation(j, i);
                        }
                    }
                }
            }
        }

        return point;
    }

    public float getAvgFromLayer(float[][] layer) {
        float value = 0;
        int count = 0;
        for (int i = 0; i < layer.length; i++) {
            for (int j = 0; j < layer[0].length; j++) {
                if (layer[i][j] > value) {
                    if (layer[i][j] != 0) {
                        value = value + layer[i][j];
                        count++;
                    }
                }
            }
        }

        return value / count;
    }

    public int getMaxOrMinFromLayer(int[][] layer, boolean maxValue) {
        int value = 0;
        if (maxValue) {
            value = -1;
            for (int i = 0; i < layer.length; i++) {
                for (int j = 0; j < layer[0].length; j++) {
                    if (layer[i][j] > value) {
                        value = layer[i][j];
                    }
                }
            }
        } else {
            value = 1;
            for (int i = 0; i < layer.length; i++) {
                for (int j = 0; j < layer[0].length; j++) {
                    if (layer[i][j] < value) {
                        value = layer[i][j];
                    }
                }
            }
        }

        return value;
    }

    @Override
    public String getEncyclopediaIconName() {
        return "monster";
    }

    public int getDiameter() {
        return Math.max(getWidth(), getHeight()) * 10;
    }

}
