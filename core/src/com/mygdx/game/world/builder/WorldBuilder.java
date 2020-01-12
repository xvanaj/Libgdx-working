package com.mygdx.game.world.builder;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.utils.TiledMapUtils;
import com.mygdx.game.controller.civilization.CivilizationGraph;
import com.mygdx.game.world.entity.EntityType;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.being.params.HeroCreationParameters;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.entity.universe.*;
import com.mygdx.game.world.entity.universe.world.*;
import com.mygdx.game.world.enums.world.*;
import com.mygdx.game.world.enums.world.tile.*;
import com.mygdx.game.world.exception.WorldCreationException;
import com.mygdx.game.world.generator.RaceTypeFactory;
import com.mygdx.game.world.generator.being.HeroFactory;
import com.mygdx.game.world.generator.being.MonsterFactory;
import com.mygdx.game.world.generator.image.PixelImageCreator;
import com.mygdx.game.world.generator.image.TileImageCreator;
import com.mygdx.game.utils.CodeGenerator;
import com.mygdx.game.name.NameGenerator;
import com.mygdx.game.world.generator.player.PlayerFactory;
import com.mygdx.game.world.generator.world.*;
import com.mygdx.game.world.params.*;
import com.mygdx.game.utils.Randomizer;
import org.junit.Assert;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.mygdx.game.world.generator.game.GameFactory.createHeroCreationParams;

/**
 * Created by Jakub Vana on 29.8.2018.
 */
public class WorldBuilder {

    private World world;
    private Date startDate;
    private Date subDate;


    public WorldBuilder(final Game game) {
        initialize(game);
    }

    public WorldBuilder generateDungeons(final DungeonCreationParameters dungeonCreationParameters) {
        final Integer count = Randomizer.get(dungeonCreationParameters.getMinCount(), dungeonCreationParameters.getMaxCount());
        if (world.getDungeons() == null) {
            world.setDungeons(new ArrayList<>());
        }

        for (int i = 0; i < count; i++) {
            final Point positionForEntity = getPositionForEntity(30, 10, true, false);
            if (positionForEntity != null) {
                Dungeon dungeon = new Dungeon(new Point(positionForEntity.y * 32, positionForEntity.x * 32),
                        Randomizer.get(Dungeon.DungeonNames.values()).getName());
                world.getDungeons().add(dungeon);
            } else {
                Gdx.app.log("World builder", "could not create dungeon");
            }
        }

        Gdx.app.log("worldGen", "Generated dungeons in " + getGenerationTime() + " millis");

        return this;
    }

    public WorldBuilder createRaces(final WorldCreatorInputParameters params) {
        //1.generate races and elders
        for (int i = 0; i < params.getRacesCount(); i++) {
            final RaceType randomRaceType = RaceTypeFactory.createRandomRaceType(world);
        }

        Gdx.app.log("worldGen", "Generated races in " + getGenerationTime() + " millis");

        return this;
    }

    public WorldBuilder createCivilizations(final WorldCreatorInputParameters params) throws WorldCreationException {

        if (params.getRacesCount() == params.getCivilizationsCount()) {
            for (RaceType raceType : world.getRaces()) {
                final Civilization civ = CivilizationFactory.generateNewCivilization(world, raceType, params.getGame());
                world.getCivilizations().add(civ);
            }
        } else {
            for (int i = 0; i < params.getCivilizationsCount(); i++) {
                final Civilization civ = CivilizationFactory.generateNewCivilization(world, params.getGame());
                world.getCivilizations().add(civ);
            }
        }

        Gdx.app.log("worldGen", "Generated civs in " + getGenerationTime() + " millis");

        return this;
    }

    public WorldBuilder createCivRegions(RegionsCreationParameters regionsCreationParameter) {
        Map<Integer, List<Point>> regions = new HashMap<>();
        world.setRegionsLayer(new int[world.getWidth()][world.getHeight()]);
        int[][] regionsLayer = world.getRegionsLayer();

        //initialize nonpassable
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                if (!isPassable(x, y)) {
                    regionsLayer[x][y] = -1;
                }
            }
        }


        if (world.getTowns() == null || world.getTowns().isEmpty()) {
            throw new IllegalArgumentException("Cant generate regions based on towns when there are no towns");
        }
        int counter = 1;
        for (Civilization civilization : world.getCivilizations()) {
            civilization.setRegionId(counter);
            final Town town = civilization.getCapitalTown();

            regions.put(counter, new ArrayList<>());
            regions.get(counter).add(new Point(town.getX() / World.pixelSize, town.getY() / World.pixelSize));
            counter++;
        }

        for (int i = 0; i < regionsCreationParameter.getIterations(); i++) {
            for (Integer regionId : regions.keySet()) {
                List<Point> addedPoints = new ArrayList<>();
                for (Point point : regions.get(regionId)) {
                    assignToRegionConditionally(new Point(point.x + 1, point.y), regionsLayer, regionId, addedPoints);
                    assignToRegionConditionally(new Point(point.x, point.y + 1), regionsLayer, regionId, addedPoints);
                    assignToRegionConditionally(new Point(point.x - 1, point.y), regionsLayer, regionId, addedPoints);
                    assignToRegionConditionally(new Point(point.x, point.y - 1), regionsLayer, regionId, addedPoints);
                }
                regions.get(regionId).addAll(addedPoints);
            }
        }

        Gdx.app.log("worldGen", "Generated regions in " + getGenerationTime() + " millis");

        return this;
    }

    private void assignToRegionConditionally(Point newPoint, int[][] regionsLayer, Integer regionId, List<Point> addedPoints) {
        if (newPoint.x < 0 || newPoint.x >= regionsLayer.length || newPoint.y < 0 || newPoint.y >= regionsLayer[0].length) {
            return;
        } else if (regionsLayer[newPoint.x][newPoint.y] == 0 && Randomizer.get(0, 1) == 0) {
            regionsLayer[newPoint.x][newPoint.y] = regionId;
            addedPoints.add(newPoint);
        }
    }

    public WorldBuilder generateAncients(AncientsCreationParameters ancientsCreationParameters) {
        final int maxLevel = world.getMaxOrMinFromLayer(world.getMonsterLevelLayer(), true);

        for (int i = 0; i < ancientsCreationParameters.getCount(); i++) {
            AncientBeing being = MonsterFactory.generateAncientMonster(maxLevel, TerrainType.GRASS_HILL);
            being.setWorld(world);
            world.getAncients().add(being);
        }

        placeBeings(world.getAncients(), maxLevel);

        Gdx.app.log("worldGen", "Generated " + ancientsCreationParameters.getCount() + " ancients in " + getGenerationTime() + " millis");
        return this;
    }

    private void placeBeings(List<AncientBeing> beings, final int maxLevel) {
        List<Point> availablePoints = new ArrayList<>();
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (world.getLevel(new Point(i, j)) >= Math.min(maxLevel / 2, 10)) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }

        if (availablePoints == null || availablePoints.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        beings.stream().forEach(being -> {
            being.setWorldCode(world.getCode());
            final Point point = Randomizer.get(availablePoints);
            being.setPosition(point.y * World.getPixelSize(), point.x * World.getPixelSize());
            availablePoints.remove(point);
        });
    }

    public WorldBuilder generateMiscelaneous() {
        world.setHoursInDay(Randomizer.generate(10, 34));
        world.setDaysInYear(Randomizer.generate(100, 10000));
        world.setSunriseHour(world.getHoursInDay() / 5);
        world.setSunsetHour(world.getHoursInDay() / 5 * 4);

        world.setMinHeight((int) world.getMaxOrMinFromLayer(world.getHeightmap(), false));
        world.setMaxHeight((int) world.getMaxOrMinFromLayer(world.getHeightmap(), true));
        world.setMinTemperature((int) world.getMaxOrMinFromLayer(world.getTemperatureLayer(), false));
        world.setMaxTemperature((int) world.getMaxOrMinFromLayer(world.getTemperatureLayer(), true));
        world.setAvgTemperature((int) world.getAvgFromLayer(world.getTemperatureLayer()));
        world.setAvgHeight((int) world.getAvgFromLayer(world.getHeightmap()));
        world.setGravityG(Randomizer.range(0.6f, 1.4f));

        return this;
    }

    public WorldBuilder generateResources(ResourceCreationParameters params) throws WorldCreationException {
        if (params.isCountBasedAlgorithm()) {
            generateResourcesByCount(params);
        } else if (!params.isCountBasedAlgorithm()) {
            generateResourcesByProbability(params);
        }

        Gdx.app.log("worldGen", "Generated resources in " + getGenerationTime() + " millis");

        return this;
    }

    private void generateResourcesByProbability(ResourceCreationParameters params) {
        for (int i = 0; i < world.getHeightmap().length; i++) {
            for (int j = 0; j < world.getHeightmap()[0].length; j++) {
                if (isPassable(i, j)) {
                    if (Randomizer.generate(0, 1000) < params.getTileProbability()) {
                        Resource resource = new Resource(j * 32, i * 32, Randomizer.get(ResourceType.values()));
                        resource.setName(NameGenerator.getResourceName(null));
                        world.getResources().add(resource);
                    }
                }
            }
        }
    }

    private void generateResourcesByCount(ResourceCreationParameters params) throws WorldCreationException {
        int count = 0;
        int attempts = 0;
        while (count < params.getMaxCount() && attempts < 1000) {
            int x = Randomizer.generate(0, world.getHeightmap().length);
            int y = Randomizer.generate(0, world.getHeightmap()[0].length);
            if (isPassable(x, y)) {
                Resource resource = new Resource(y, x, Randomizer.get(ResourceType.values()));
                world.getResources().add(resource);
                count++;
            }
            attempts++;
        }
        if (count < params.getMinCount()) {
            throw new WorldCreationException("Was not able to crete enough resources. Created = " + count
                    + ". Required = " + params.getMinCount());
        }
    }

    private void createDefaultImageFileNameIfNeeded(WorldCreatorInputParameters params) {
        if (params.getMapImageParameters().getName() == null) {
            params.getMapImageParameters().setName(
                    "O" + params.getLayersCreationInputParams().getOctaves() +
                            "_R" + String.format("%.2f", params.getLayersCreationInputParams().getRougness()) +
                            "_S" + String.format("%.2f", params.getLayersCreationInputParams().getScale()) +
                            "mask" + params.getMaskCreatorInputParameters().getMaskType() +
                            "_F" + params.getMaskCreatorInputParameters().getEdgeFade() +
                            "_P" + params.getMaskCreatorInputParameters().getFillPercentCellularMask() +
                            "_S" + params.getMaskCreatorInputParameters().getSmoothFactorCellularMask() +
                            "TM" + String.format("%.2f", params.getTerrainComposition().getMountains()) +
                            "_H" + String.format("%.2f", params.getTerrainComposition().getHills()) +
                            "_L" + String.format("%.2f", params.getTerrainComposition().getLowlands()) +
                            "_W" + String.format("%.2f", params.getTerrainComposition().getWater())
            );
        }
    }

    public WorldBuilder createMapImage(WorldCreatorInputParameters params) {
        createDefaultImageFileNameIfNeeded(params);

        return createMapImage(params.getMapImageParameters().getMapImageType(), params.getMapImageParameters().getName());
    }

    public WorldBuilder generateFreeTowns(TownCreationInputParameters townCreationInputParameters) throws WorldCreationException {
        //1.generate towns
        List<Town> towns = new ArrayList<>();
        for (int i = 0; i < townCreationInputParameters.getFreeTownsCount(); i++) {
            final Town town = new TownBuilder().createMinimalTown().build();
            towns.add(town);
        }
        towns.stream().forEach(town -> town.setWorld(world));
        world.addTowns(towns);

        Gdx.app.log("worldGen", "Generated free towns in " + getGenerationTime() + " millis");

        return this;
    }

    public WorldBuilder createTerrain(TerrainComposition terrainComposition) {
        return createTerrain(terrainComposition.getMountains(), terrainComposition.getHills(), terrainComposition.getLowlands(), terrainComposition.getWater());
    }

    public WorldBuilder generateWildernessLevels() {
        new TerrainGenerator().generateWildernessLevels(world);

        world.setHostilityLayer(new int[world.getWidth()][world.getHeight()]);
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                world.getHostilityLayer()[i][j] = world.getMonsterLevelLayer()[i][j];
            }
        }


        return this;
    }

    public WorldBuilder generateGates(GatesCreationInputParameters gatesCreationInputParameters) {

        for (int i = 0; i < gatesCreationInputParameters.getGatesCount(); i++) {
            placeGate();
        }

        return this;
    }

    private void placeGate() {
        Point point = getPositionForEntity(30, 10, true, true);
        if (point != null) {
            Gate gate = new Gate();
            gate.setWorldCode(world.getCode());
            gate.setPosition(point.y * World.pixelSize, point.x * World.pixelSize);
            world.getGates().add(gate);
        }
    }

    private Point getPositionForEntity(int idealDistance, int minAllowedDistance, boolean checkTownDistance, boolean checkGateDistance) {
        int distance = idealDistance;
        boolean distanceOk = false;
        int attempts = 0;
        Point randomPoint = null;

        while (!distanceOk) {
            if (attempts >= 5) {
                distance--;
            }
            if (distance < minAllowedDistance) {
                System.out.println("Too many towns or gates? Cant place them in sufficient distance");
                return null;
            }
            randomPoint = new Point(Randomizer.range(0, world.getHeightmap().length - 1), Randomizer.range(0, world.getHeightmap()[0].length - 1));

            distanceOk = !checkTownDistance ||
                    (isPassable(randomPoint) && isDistant(new ArrayList(world.getTowns().values()), randomPoint, distance, true));
            distanceOk = distanceOk && (!checkGateDistance || isDistant(new ArrayList<>(world.getGates()), randomPoint, distance));

            attempts++;
        }
        return randomPoint;
    }

    private boolean isDistant(final List<WorldEntity> entities, final Point finalRandomPoint, final int finalMinimumDistance) {
        return isDistant(entities, finalRandomPoint, finalMinimumDistance, false);
    }

    private boolean isDistant(final List<WorldEntity> entities, final Point finalRandomPoint, final int finalMinimumDistance, boolean coordinatesInPixels) {
        int divisor = coordinatesInPixels ? World.pixelSize : 1;
        return entities.stream().allMatch(entity -> entity.getMapPosition() == null
                || ((Math.abs(entity.getX() / divisor - finalRandomPoint.x) + Math.abs(entity.getY() / divisor - finalRandomPoint.y)) > finalMinimumDistance));
    }

    private boolean isPassable(final int x, final int y) {
        return TerrainType.values()[world.getTerrainLayer()[x][y]].isPassable();
    }

    private boolean isPassable(final Point point) {
        return TerrainType.values()[world.getTerrainLayer()[point.x][point.y]].isPassable();
    }

    private WorldBuilder createMapImage(MapImageType imageType, String s) {
        if (imageType != null) {
            subDate = new Date();

            PixelImageCreator pixelImageCreator = new PixelImageCreator();
            TileImageCreator tileImageCreator = new TileImageCreator();
            switch (imageType) {
                case ALL:
                    pixelImageCreator.visualizeFromEnum(world, s == null ? "generatedQuickMap" : s, false);
                    tileImageCreator.visualizeFromEnum(world, s == null ? "generatedQuickMap" : s, false);
                    break;
                case PIXEL:
                    pixelImageCreator.visualizeFromEnum(world, s == null ? "generatedQuickMap" : s, false);
                    break;
                case TILE:
                    tileImageCreator.visualizeFromEnum(world, s == null ? "generatedQuickMap" : s, false);
                    break;
            }

            Gdx.app.log("worldGen", "Created map image in " + getGenerationTime() + " millis");
        }

        return this;
    }

    private WorldBuilder createTerrain(float pcMountains, float pcHills, float pcLowlands, float pcWater) {
        Gdx.app.log("worldGen", "Generating terrain");

        TerrainGenerator generator = new TerrainGenerator();
        generator.setCustomTerrainComposition(pcMountains, pcHills, pcLowlands, pcWater);
        generator.fillWorldWithTerrain(world);

        Gdx.app.log("worldGen", "Generated terrain in " + getGenerationTime() + " millis");

        return this;
    }

    public WorldBuilder randomName() {

        world.setName(NameGenerator.generateWorldName(world.getWorldType().name()));

        Gdx.app.log("worldGen", "Generated world name " + world.getWorldType().getName());

        return this;
    }

    public WorldBuilder randomMaterialType() {
        ResourceType resourceType = Randomizer.get(ResourceType.values());
        world.setResourceType(resourceType);

        Gdx.app.log("worldGen", "Generated world composition is 100% " + resourceType);

        return this;
    }

    public WorldBuilder randomWorldType() {
        Gdx.app.log("worldGen", "Generating world type randomly");

        WorldType worldType = WorldType.values()[new Random().nextInt(WorldType.values().length)];
        world.setWorldType(worldType);

        WorldTiles worldTiles = new WorldTiles();
        worldTiles.setGrassTileType(GrassTileType.COMMON);
        worldTiles.setDirtTileType(DirtTileType.COMMON);
        worldTiles.setSandTileType(SandTileType.COMMON);
        worldTiles.setSnowTileType(SnowTileType.COMMON);
        worldTiles.setWaterTileType(WaterTileType.COMMON);
        worldTiles.setDeepWaterTileType(DeepWaterTileType.COMMON);

        Gdx.app.log("worldGen", "Generated world type is " + worldType);

        return this;
    }

    public WorldBuilder createOrUpdateLayer(int width, int height, LayerType layerType, int octaves, float roughness, float scale) {
        return createOrUpdateLayer(0, 0, width, height, layerType, octaves, roughness, scale);
    }

    public WorldBuilder createOrUpdateLayer(int originX, int originY, int width, int height, LayerType layerType, int octaves, float roughness, float scale) {
        float[][] layer = getLayer(layerType);
        float[][] layerRegion = getLayerRegion(originX, originY, width, height, layer);

        IWorldGenerator worldGen = new BasicLayerGenerator();

        worldGen.updateLayer(layerRegion, octaves, roughness, scale);

        updateLayerRegion(originX, originY, layer, layerRegion);

        return this;
    }

    private float[][] getLayer(final LayerType layerType) {
        float[][] layer = null;
        switch (layerType) {
            case HEIGHTMAP:
                layer = world.getHeightmap();
                break;
            case TEMPERATURE:
                layer = world.getTemperatureLayer();
                break;
            case HUMIDITY:
                layer = world.getHumidityLayer();
                break;
        }

        if (layer == null) {
            layer = new float[world.getWidth()][world.getHeight()];
            switch (layerType) {
                case HEIGHTMAP:
                    world.setHeightmap(layer);
                    break;
                case TEMPERATURE:
                    world.setTemperatureLayer(layer);
                    break;
                case HUMIDITY:
                    world.setHumidityLayer(layer);
                    break;
            }
        }
        return layer;
    }

    private void updateLayerRegion(final int originX, final int originY, final float[][] originalLayer, final float[][] layerRegion) {
        for (int i = 0; i < layerRegion.length; i++) {
            for (int j = 0; j < layerRegion[0].length; j++) {
                originalLayer[i + originX][j + originY] = layerRegion[i][j];
            }
        }
    }

    private float[][] getLayerRegion(final int originX, final int originY, final int layerWidth, final int layerHeight, float[][] layer) {
        float[][] layerRegion = new float[layerWidth][layerHeight];
        for (int i = 0; i < layerWidth; i++) {
            for (int j = 0; j < layerHeight; j++) {
                layerRegion[i][j] = layer[i + originX][j + originY];
            }
        }
        return layerRegion;
    }

    public WorldBuilder createBasicLayers(WorldCreatorInputParameters parameters) {
        world.setWidth(parameters.getWidth());
        world.setHeight(parameters.getHeight());

        int width = parameters.getWidth();
        int height = parameters.getHeight();
        int octaves = parameters.getLayersCreationInputParams().getOctaves();
        float rougness = parameters.getLayersCreationInputParams().getRougness();
        float scale = parameters.getLayersCreationInputParams().getScale();
        int continents = parameters.getContinentsCount();

        Gdx.app.log("worldGen", "Creating basic layers for world of size " + width + " x " + height);
        if (continents == 1) {
            createOrUpdateLayer(width, height, LayerType.HEIGHTMAP, octaves, rougness, scale);
            updateMask(world.getHeightmap(), parameters.getMaskCreatorInputParameters());


        } else if (continents == 2) {
            int w1 = width / 2 + Randomizer.range(-width / 5, width / 5);
            int w2 = width - w1;
            createOrUpdateLayer(0, 0, w1, height, LayerType.HEIGHTMAP, octaves, rougness, scale);
            createOrUpdateMask(0, 0, w1, height, LayerType.HEIGHTMAP, parameters.getMaskCreatorInputParameters());

            createOrUpdateLayer(w1, 0, w2, height, LayerType.HEIGHTMAP, octaves, rougness, scale);
            createOrUpdateMask(w1, 0, w2, height, LayerType.HEIGHTMAP, parameters.getMaskCreatorInputParameters());

        } else if (continents == 3) {
            int w1 = width / 2 + Randomizer.range(-width / 5, width / 5);

            int w2 = width - w1;
            int h1 = height / 2 + Randomizer.range(-height / 5, height / 5);

            int h2 = height - h1;

            createOrUpdateLayer(0, 0, w1, height, LayerType.HEIGHTMAP, octaves, rougness, scale);
            createOrUpdateMask(0, 0, w1, height, LayerType.HEIGHTMAP, parameters.getMaskCreatorInputParameters());

            createOrUpdateLayer(w1, 0, w2, h1, LayerType.HEIGHTMAP, octaves, rougness, scale);
            createOrUpdateMask(w1, 0, w2, h1, LayerType.HEIGHTMAP, parameters.getMaskCreatorInputParameters());

            createOrUpdateLayer(w1, h1, w2, h2, LayerType.HEIGHTMAP, octaves, rougness, scale);
            createOrUpdateMask(w1, h1, w2, h2, LayerType.HEIGHTMAP, new MaskCreationInputParameters(MaskType.SHEET));
        }

        createOrUpdateLayer(width, height, LayerType.HUMIDITY, octaves, rougness, scale);
        //createOrUpdateLayer(width, height, LayerType.TEMPERATURE, octaves, rougness, scale);
        createOrUpdateMask(width, height, LayerType.TEMPERATURE, new MaskCreationInputParameters(parameters.getMaskCreatorInputParameters().getTemperatureMaskType()));
        //createOrUpdateLayer(width, height, LayerType.TEMPERATURE, 1, 0.6f, 0.03f);

        world.setExplorationLayer(new int[width][height]);
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                world.getExplorationLayer()[i][j] = 0;
            }
        }

        Gdx.app.log("worldGen", "Created basic layers in " + getGenerationTime() + " millis");

        return this;
    }

    private MaskCreationInputParameters createRandomMaskCreationParams() {
        MaskCreationInputParameters parameters = new MaskCreationInputParameters();
        parameters.setMaskType(Randomizer.get(MaskType.values()));
        parameters.setSmoothFactorCellularMask(2);
        parameters.setFillPercentCellularMask(45);
        parameters.setEdgeFade(0.2f);

        return parameters;
    }

    private WorldBuilder createOrUpdateMask(int width, int height, LayerType layerType, MaskCreationInputParameters params) {
        createOrUpdateMask(0, 0, width, height, layerType, params);

        return this;
    }

    private WorldBuilder createOrUpdateMask(int originX, int originY, int width, int height, LayerType layerType, MaskCreationInputParameters params) {
        int layerWidth = width;
        int layerHeight = height;

        float[][] layer = getLayer(layerType);
        float[][] layerRegion = getLayerRegion(originX, originY, layerWidth, layerHeight, layer);

        updateMask(layerRegion, params);
        updateLayerRegion(originX, originY, layer, layerRegion);

        return this;
    }

    private WorldBuilder updateMask(float[][] layer, MaskCreationInputParameters params) {
        switch (params.getMaskType()) {
            case REAL_TEMPERATURE_ONE_POLE:
                MaskGenerator.createTempMask(layer, 0.2f, 0.8f, 0.2f);
                break;
            case REAL_TEMPERATURE_TWO_POLES:
                MaskGenerator.createTempMask2Poles(layer, 0.2f, 0.8f, 0.2f);
                break;
            case EDGE:
                MaskGenerator.createEdgeMask(layer, params.getEdgeFade());
                break;
            case RADIAL:
                MaskGenerator.createRadialMask(layer);
                MaskGenerator.createEdgeMask(layer, params.getEdgeFade());//todo
                break;
            case CELLULAR:
                MaskGenerator.createCellularAutomataMask(layer, params.getFillPercentCellularMask(), params.getSmoothFactorCellularMask());
                break;
            case CIRCLE:
                MaskGenerator.createArtMask(layer, MaskGenerator.MapArtType.CIRCLE, layer.length, layer[0].length);
                break;
            case ASTERISK:
                MaskGenerator.createArtMask(layer, MaskGenerator.MapArtType.ASTERISK, layer.length, layer[0].length);
                break;
            case SHEET:
                MaskGenerator.createArtMask(layer, MaskGenerator.MapArtType.SHEET, layer.length, layer[0].length);
                break;
        }

        Gdx.app.log("worldGen", "Created mask " + params.getMaskType() + " in " + getGenerationTime() + " millis");
        return this;
    }

    private long getGenerationTime() {
        final long generationTimeInMillis = new Date().getTime() - subDate.getTime();
        subDate = new Date();

        return generationTimeInMillis;
    }

    public World build() {
        return this.world;
    }

    public WorldBuilder createTiledMap() {
        subDate = new Date();

        final String fileName = world.getCode();
        world.setTiledMapCode(fileName);

        TiledMapUtils.createTiledMapForWorld(this.world, fileName);
        Gdx.app.log("worldGen", "Generated tiledMap in " + getGenerationTime() + " millis");

        TiledMapUtils.loadAndInitializeTiledMap(world);

        Gdx.app.log("worldGen", "Loaded tiledMap in " + getGenerationTime() + " millis");
        return this;
    }



    private WorldBuilder createVoronoi() {
        VoronoiGenerator voronoi = new VoronoiGenerator();
        world.setVoronoiMap(voronoi.generateVoronoi(3, world.getHeightmap().length, world.getHeightmap()[0].length, VoronoiGenerator.VoronoiType.EUCLIDIAN));

        return this;
    }

    public WorldBuilder createNatureWonders() {
        Point highestPoint = world.getMaxOrMinPointFromLayer(world.getHeightmap(), true);
        NatureWonder highestPlace = new NatureWonder();
        highestPlace.setWorldCode(world.getCode());
        highestPlace.setPosition(highestPoint.x, highestPoint.y);
        highestPlace.setName(Randomizer.get(NatureWonder.HighPlaceName.values()).getName());

        Point lowestPoint = world.getMaxOrMinPointFromLayer(world.getHeightmap(), false);
        NatureWonder lowestPlace = new NatureWonder();
        lowestPlace.setWorldCode(world.getCode());
        lowestPlace.setPosition(lowestPoint.x, lowestPoint.y);
        lowestPlace.setName(Randomizer.get(NatureWonder.LowPlaceName.values()).getName());


        world.getNatureWonders().add(lowestPlace);
        world.getNatureWonders().add(highestPlace);

        return this;
    }

    public WorldBuilder createPlayers(final WorldCreatorInputParameters params) {
        //creates AI's but also human player
        for (int i = 0; i < params.getAICount() + 1; i++) {
            params.getPlayerCreationParameters().setWorld(world);
            params.getPlayerCreationParameters().setUniverse(params.getGame().getUniverse());

            final Player player = PlayerFactory.createPlayer(params.getPlayerCreationParameters());

            final HeroCreationParameters heroCreationParameters = createHeroCreationParams(player);
            final Hero hero = HeroFactory.createHero(heroCreationParameters);
            hero.setCode(CodeGenerator.getNextSequenceNumber(params.getGame(), EntityType.HERO));

            player.getHeroes().add(hero);
            player.setPosition(player.getHeroes().get(0).getX(), player.getHeroes().get(0).getY());
            player.setCode(CodeGenerator.getNextSequenceNumber(params.getGame(), EntityType.PLAYER));
        }

        return this;
    }

    public WorldBuilder createCivRelations() {
        world.setCivilizationGraph(new CivilizationGraph(world.getCivilizations().stream()
                .map(civ -> civ.getCode())
                .collect(Collectors.toList())));

        return this;
    }


    public WorldBuilder setUniverse(final Universe universe) {
        world.setUniverse(universe);
        world.setUniverseCode(universe.getCode());

        return this;
    }

    public void resetWorld(Game game) {
        if (world == null) {
            initialize(game);
        }
    }

    private void initialize(final Game game) {
        this.world = new World();
        this.startDate = new Date();
        this.subDate = new Date();
        world.setCode(CodeGenerator.getNextSequenceNumber(game, EntityType.WORLD));
    }

    public WorldBuilder createVegetation(final WorldCreatorInputParameters params) {
        world.setVegetationLayer(new int[world.getWidth()][world.getHeight()]);

        Assert.assertNotNull(this.world.getHumidityLayer());
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (world.getHumidityLayer()[i][j] > 0.5f && TerrainType.values()[world.getTerrainLayer()[i][j]].isPassable()) {
                    world.getVegetationLayer()[i][j] = 1;
                }
            }
        }

        return this;
    }
}
