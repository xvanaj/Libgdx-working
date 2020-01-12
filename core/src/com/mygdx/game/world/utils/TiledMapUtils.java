package com.mygdx.game.world.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.gdxutils.MyTmxMapLoader;
import com.mygdx.game.utils.SaveLoadUtils;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.entity.universe.*;
import com.mygdx.game.world.entity.universe.world.Dungeon;
import com.mygdx.game.world.entity.universe.world.Gate;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.world.TerrainType;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.Iterator;

public class TiledMapUtils {

    public static final String FOLDER_CORE_ASSETS_MAPS = "core/assets/maps/";
    private static final String HEADER_CONSTANT = "<map version=\"1.0\" tiledversion=\"1.2.2\" orientation=\"orthogonal\" renderorder=\"left-up\" ";
    private static final String portalGid = "1473";
    private static final String townGid = "1216";
    public static final boolean flipY = false;
    private static final int pixelSize = World.getPixelSize();

    private static Date subDate;

    public static void loadAndInitializeTiledMap(World world) {
        subDate = new Date();

        final TiledMap loadedMap = loadTileMapFromPath(world);
        world.setTiledMap(loadedMap);

        //todo deprecated
        //setAnimations(loadedMap, world);

        synchronizeEntities(world);
    }

    private static void synchronizeEntities(final World world) {
        for (AncientBeing ancient : world.getAncients()) {
            final Iterator<MapObject> objectsLayer = world.getTiledMap().getLayers().get("Objects layer").getObjects().iterator();
            while (objectsLayer.hasNext()) {
                final TextureMapObject next = (TextureMapObject) objectsLayer.next();
                if (((int) next.getX()) == ancient.getX()
                        && ((int) (next.getY())) == (TiledMapUtils.flipY ? world.getHeight() * World.getPixelSize() - ancient.getY() : ancient.getY())) {
                    ancient.setTextureMapObject(next);
                    continue;
                }
            }
            if (ancient.getTextureMapObject() == null) {
                Game.log("worldGen", "One of ancients does not have matching entity in tiled map");
            }
        }
    }

    private static void setAnimations(final TiledMap loadedMap,  final World world) {
        final TiledMapTileSet tileSet = loadedMap.getTileSets().getTileSet(0);

        final StaticTiledMapTile water1 = (StaticTiledMapTile) tileSet.getTile(4);
        final StaticTiledMapTile water2 = (StaticTiledMapTile) tileSet.getTile(5);
        final StaticTiledMapTile water3 = (StaticTiledMapTile) tileSet.getTile(6);
        final Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>();
        frameTiles.add(water1, water2, water3);
        final AnimatedTiledMapTile shallowWater = new AnimatedTiledMapTile(0.5f, frameTiles);

        final StaticTiledMapTile dw1 = (StaticTiledMapTile) tileSet.getTile(7);
        final StaticTiledMapTile dw2 = (StaticTiledMapTile) tileSet.getTile(8);
        final StaticTiledMapTile dw3 = (StaticTiledMapTile) tileSet.getTile(9);
        final Array<StaticTiledMapTile> dwFrameTiles = new Array<StaticTiledMapTile>();
        dwFrameTiles.add(dw1, dw2, dw3);
        final AnimatedTiledMapTile deepWater = new AnimatedTiledMapTile(0.5f, dwFrameTiles);


        final TiledMapTileLayer mainLayer = (TiledMapTileLayer) loadedMap.getLayers().get("Main layer");

        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (mainLayer.getCell(i, j).getTile().getId() == 4) {
                    mainLayer.getCell(i, j).setTile(shallowWater);
                }
                if (mainLayer.getCell(i, j).getTile().getId() == 7) {
                    mainLayer.getCell(i, j).setTile(deepWater);
                }
            }
        }
    }

    private static TiledMap loadTileMapFromPath(final World world) {
        String path = FOLDER_CORE_ASSETS_MAPS + world.getTiledMapCode() + ".tmx";
        File file = new File(path);
        if (!file.exists()) {
            path = path.replace("core/", "");
        }

        MyTmxMapLoader loader = new MyTmxMapLoader();
        final TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.flipY = TiledMapUtils.flipY;
        parameters.generateMipMaps = true;
        return loader.load(path, parameters);
    }

    public static void createTiledMapForWorld(final World world, String savePath) {
        subDate = new Date();
        savePath = FOLDER_CORE_ASSETS_MAPS + savePath + ".tmx";
        final int worldWidth = world.getWidth();
        final int worldHeight = world.getHeight();

        createTilesetFile(world);

        String tmxHeader = getTmxHeader(world);

        StringBuilder terrainLayer = new StringBuilder();
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                final Point point = TerrainType.values()[world.getTerrainLayer()[i][j]].getPoint();
                terrainLayer.append((point.x + point.y * 10) + ",");
            }
            terrainLayer.append("\n");
        }

        final StringBuilder treesLayer = new StringBuilder();
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                treesLayer.append(2000 + world.getVegetationLayer()[i][j] + ",");
            }
            treesLayer.append("\n");
        }

        int objectId = 1;
        final StringBuilder objects = new StringBuilder();
        objectId = createMapObjects(world, objectId, objects);

        final StringBuilder natureObjects = new StringBuilder();
        /*objectId = 1;*/
        objectId = createNatureObjects(world, objectId, natureObjects);

        String tmxLayers =
                createLayerData(1, "Main layer", worldWidth, worldHeight, terrainLayer) +
                        createLayerData(2, "Trees layer", worldWidth, worldHeight, treesLayer) +
                        createObjectsData(3, "Objects layer", objects) +
                        createObjectsData(4, "Points layer", natureObjects) +
                        "</map>\n";

        String tmxString = tmxHeader + tmxLayers;

        SaveLoadUtils.writeStringToFile(savePath, tmxString);
    }

    private static String createObjectsData(final int id, final String layerName, final StringBuilder objects) {
        return "<objectgroup " +
                "id=\"" + id + "\" " +
                "name=\"" + layerName + "\">\n" +
                objects.toString() +
                "</objectgroup>";
    }

    private static String createLayerData(final int id, final String name, final int worldWidth, final int worldHeight, final StringBuilder layerData) {
        return "<layer id=\"" + id + "\" name=\"" + name + "\" width=\"" + worldWidth + "\" height=\"" + worldHeight + "\">\n" +
                "  <data encoding=\"csv\">\n"
                + layerData.toString() +
                "</data>\n" +
                "</layer>\n";
    }

    private static int createNatureObjects(final World world, int objectId, StringBuilder objectsGroup) {
        for (NatureWonder wonder : world.getNatureWonders()) {
            objectsGroup.append(addObject(objectId, wonder));
            objectId++;
        }
        Gdx.app.log("tiledMapUtils", "saved nature wonders in " + getGenerationTime() + " millis");
        return objectId;
    }

    private static int createMapObjects(final World world, int objectId, final StringBuilder objects) {
        for (Town town : world.getTowns().values()) {
            int x = (int) town.getMapPosition().getX();
            int y = (int) town.getMapPosition().getY();
            objects.append("<object id=\"" + objectId + "\" gid=\"" + townGid + "\" name=\"" + town.getName() + "\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + pixelSize + "\" height=\"" + pixelSize + "\"/>\n");
            objectId++;
        }
        Gdx.app.log("tiledMapUtils", "saved towns in " + getGenerationTime() + " millis");

        for (AncientBeing being : world.getAncients()) {
            //objects.append(addObject(objectId, being, String.valueOf(612 + Randomizer.range(0, 60))));
            objects.append(addObject(objectId, being, String.valueOf(612)));
            objectId++;
        }
        for (Dungeon dungeon : world.getDungeons()) {
            objects.append(addObject(objectId, dungeon, String.valueOf(1470)));
            objectId++;
        }
        for (Resource resource : world.getResources()) {
            objects.append(addObject(objectId, resource, String.valueOf(2975)));
            objectId++;
        }
        for (Player player : world.getUniverse().getPlayers()) {
            objects.append(addObject(objectId, player, String.valueOf(616)));
            objectId++;
        }
        for (Gate gate : world.getGates()) {
            objects.append(addObject(objectId, gate, portalGid));
            objectId++;
        }
        return objectId;
    }

    private static String getTmxHeader(final World world) {
        final String tileSetName = world.getTilesetName();
        final String objectsTileSetName = "tilesets/ProjectUtumnoTileset.tsx";
        final String treesTileSetName = "tilesets/trees.tsx";
        final int worldWidth = world.getWidth();
        final int worldHeight = world.getHeight();

        return HEADER_CONSTANT +
                "width=\"" + worldWidth + "\" height=\"" + worldHeight + "\" " +
                "tilewidth=\"" + pixelSize + "\" tileheight=\"" + pixelSize + "\" infinite=\"0\" nextlayerid=\"3\" nextobjectid=\"1\">\n" +
                "<tileset firstgid=\"0\" source=\"" + tileSetName + "\"/>\n" +
                "<tileset firstgid=\"484\" source=\"" + objectsTileSetName + "\"/>\n" +
                "<tileset firstgid=\"2000\" source=\"" + treesTileSetName + "\"/>\n";
    }

    private static String createTilesetFile(final World world) {
        final String tileSetName = "tilesets/" + world.getTiledMapCode() + ".tsx";
        world.setTilesetName(tileSetName);
        final int frameDuration = 1000;

        String animatedTiles = "<tile id=\"4\"> " +
                "<animation> <frame tileid = \"4\" duration = \"" + frameDuration + "\"/>  " +
                "<frame tileid = \"5\" duration = \"" + frameDuration + "\"/> " +
                "<frame tileid = \"6\" duration = \"" + frameDuration + "\"/> " +
                "</animation> " +
                "</tile>\n" +
                "<tile id=\"7\"> " +
                "<animation> <frame tileid = \"7\" duration = \"" + frameDuration + "\"/>  " +
                "<frame tileid = \"8\" duration = \"" + frameDuration + "\"/> " +
                "<frame tileid = \"9\" duration = \"" + frameDuration + "\"/> " +
                "</animation> " +
                "</tile>\n";
        //animatedTiles = "";
        String tileset = "<tileset version=\"1.2\" tiledversion=\"1.2.2\" name=\"world001_tileset\" tilewidth=\"32\" tileheight=\"32\" tilecount=\"18\" columns=\"3\">" +
                "<image source=\"world001_tileset.png\" width=\"736\" height=\"672\"/>\n" +
                animatedTiles +
                "</tileset>";

        SaveLoadUtils.writeStringToFile(FOLDER_CORE_ASSETS_MAPS + tileSetName, tileset);

        return tileSetName;
    }

    private static String addObject(final int objectId, final WorldEntity entity) {
        int x = (int) entity.getTileX() * pixelSize;
        int y = (int) (entity.getTileY()) * pixelSize;
        String objectName = "";
        if (entity.getName() != null) {
            objectName = "\" name=\"" + entity.getName();
        }
        return "<object id=\"" + objectId + objectName + "\" x=\"" + x + "\" y=\"" + y + "\"/>\n";
    }

    private static String addObject(final int objectId, WorldEntity entity, final String gid) {
        int x = (int) entity.getTileX() * pixelSize;
        int y = (int) entity.getTileY() * pixelSize;
        String objectName = "";
        if (entity.getName() != null) {
            objectName = "\" name=\"" + entity.getName();
        }
        return "<object id=\"" + objectId + "\" gid=\"" + gid + objectName + "\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + pixelSize + "\" height=\"" + pixelSize + "\"/>\n";
    }

    private static long getGenerationTime() {
        final long generationTimeInMillis = new Date().getTime() - subDate.getTime();
        subDate = new Date();

        return generationTimeInMillis;
    }
}
