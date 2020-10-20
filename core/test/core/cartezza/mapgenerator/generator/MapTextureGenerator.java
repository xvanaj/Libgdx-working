package core.cartezza.mapgenerator.generator;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import core.cartezza.mapgenerator.generator.domain.BiomeType;
import core.cartezza.mapgenerator.generator.domain.HeatType;
import core.cartezza.mapgenerator.generator.domain.HeightType;
import core.cartezza.mapgenerator.generator.domain.Tile;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SquidColorCenter;
import squidpony.squidgrid.mapping.WorldMapGenerator;
import squidpony.squidmath.OrderedMap;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zachcarter on 12/15/16.
 */
public class MapTextureGenerator {

    private static boolean debug = false;
    private static int pixelSize = 1;
    private static SquidColorCenter squidColorCenter = new SquidColorCenter();

    // here the 1x1 white pixel image is called "white"
    private static TextureAtlas atlas = new TextureAtlas("skin/uiskin/uiskin.atlas");
    private static TextureRegion dot = atlas.findRegion("white");{
        dot.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    // Heightmap colors
    private static Color DeepColor = new Color(0 / 255f, 27 / 255f, 72 / 255f, 1);
    private static Color MediumColor = new Color(0 / 255f, 69 / 255f, 129 / 255f, 1);
    private static Color ShallowColor = squidColorCenter.light(MediumColor.cpy());
    private static Color CoastalColor = squidColorCenter.light(MediumColor.cpy());
    private static Color FoamColor = new Color(161 / 255f, 252 / 255f, 255 / 255f, 1);

    private static Color IceWater = new Color(210 / 255f, 255 / 255f, 252 / 255f, 1);
    private static Color ColdWater = MediumColor.cpy();
    private static Color RiverWater = ShallowColor.cpy();

    private static Color RiverColor = new Color(30 / 255f, 120 / 255f, 200 / 255f, 1);
    private static Color SandColor = new Color(240 / 255f, 240 / 255f, 64 / 255f, 1);
    private static Color GrassColor = new Color(50 / 255f, 220 / 255f, 20 / 255f, 1);
    private static Color ForestColor = new Color(16 / 255f, 160 / 255f, 0, 1);
    private static Color RockColor = new Color(0.5f, 0.5f, 0.5f, 1);
    private static Color SnowColor = new Color(1, 1, 1, 1);

    // Heat map colors
    private static Color Coldest = new Color(0, 1, 1, 1);
    private static Color Colder = new Color(170 / 255f, 1, 1, 1);
    private static Color Cold = new Color(0, 229 / 255f, 133 / 255f, 1);
    private static Color Warm = new Color(1, 1, 100 / 255f, 1);
    private static Color Warmer = new Color(1, 100 / 255f, 0, 1);
    private static Color Warmest = new Color(241 / 255f, 12 / 255f, 0, 1);

    // Moisture map colors
    private static Color Dryest = new Color(255 / 255f, 139 / 255f, 17 / 255f, 1);
    private static Color Dryer = new Color(245 / 255f, 245 / 255f, 23 / 255f, 1);
    private static Color Dry = new Color(80 / 255f, 255 / 255f, 0 / 255f, 1);
    private static Color Wet = new Color(85 / 255f, 255 / 255f, 255 / 255f, 1);
    private static Color Wetter = new Color(20 / 255f, 70 / 255f, 255 / 255f, 1);
    private static Color Wettest = new Color(0 / 255f, 0 / 255f, 100 / 255f, 1);

    // Biome map colors
    private static Color Ice = Color.WHITE;
    private static Color DarkIce = squidColorCenter.dimmer(Ice.cpy());

    private static Color Desert = new Color(238 / 255f, 218 / 255f, 130 / 255f, 1);
    private static Color DarkDesert = squidColorCenter.dimmer(Desert.cpy());

    private static Color Savanna = new Color(177 / 255f, 209 / 255f, 110 / 255f, 1);
    private static Color DarkSavanna = squidColorCenter.dimmer(Savanna.cpy());

    private static Color TropicalRainforest = new Color(66 / 255f, 123 / 255f, 25 / 255f, 1);
    private static Color DarkTropicalRainforest = squidColorCenter.dimmer(TropicalRainforest.cpy());

    private static Color Tundra = new Color(96 / 255f, 131 / 255f, 112 / 255f, 1);
    private static Color DarkTundra = squidColorCenter.dimmer(Tundra.cpy());

    private static Color TemperateRainforest = new Color(29 / 255f, 73 / 255f, 40 / 255f, 1);
    private static Color DarkTemperateRainforest = squidColorCenter.dimmer(TemperateRainforest.cpy());

    private static Color Grassland = new Color(164 / 255f, 225 / 255f, 99 / 255f, 1);
    private static Color DarkGrassland = squidColorCenter.dimmer(Grassland.cpy());

    private static Color SeasonalForest = new Color(73 / 255f, 100 / 255f, 35 / 255f, 1);
    private static Color DarkSeasonalForest = squidColorCenter.dimmer(SeasonalForest.cpy());

    private static Color BorealForest = new Color(95 / 255f, 115 / 255f, 62 / 255f, 1);
    private static Color DarkBorealForest = squidColorCenter.dimmer(BorealForest.cpy());

    private static Color Woodland = new Color(139 / 255f, 175 / 255f, 90 / 255f, 1);
    private static Color DarkWoodland = squidColorCenter.dimmer(Woodland.cpy());

    // Wind Colors
    private static Color WindWest = new Color(1, 0, 0, 1);
    private static Color WindNorth = new Color(0, 1, 0, 1);
    private static Color WindEast = new Color(0, 0, 1, 1);
    private static Color WindSouth = new Color(1, 1, 0, 1);

    private static boolean isometric = true;

    public static Texture generateWaterMapTexture(MapGeneratorInput input, char[][] waterLandMap) {
        Pixmap pixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        int width = input.WIDTH;
        int height = input.HEIGHT;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (waterLandMap[x][y] == '#') {
                    pixmap.setColor(Color.rgba8888(DeepColor));
                    fillRectangle(pixmap, x, y, width, height);
                } else {
                    pixmap.setColor(Color.rgba8888(GrassColor));
                    fillRectangle(pixmap, x, y, width, height);
                }
            }
        }

        PixmapIO.writePNG(Gdx.files.getFileHandle("watermap.png" , Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    public static Texture generateBiomeMapTexture(final MapGeneratorInput input, Tile[][] tiles) {
        return generateBiomeMapTexture("biomemap.png" , input, tiles);
    }

    public static Texture generateBiomeMapTexture(String filename, final MapGeneratorInput input, Tile[][] tiles) {
        Pixmap pixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        int width = input.WIDTH;
        int height = input.HEIGHT;

        Map<Double, Double> heights = new HashMap<Double, Double>();
        heights.put(0.0, input.DeepWater);
        heights.put(input.DeepWater, input.ShallowWater);
        heights.put(input.ShallowWater, input.Sand);
        heights.put(input.Sand, input.Grass);
        heights.put(input.Grass, input.Forest);
        heights.put(input.Forest, input.Rock);
        heights.put(input.Rock, 1.0);

        for (int x = 0; x < input.WIDTH; x++) {
            for (int y = 0; y < input.HEIGHT; y++) {

                BiomeType value = tiles[x][y].biomeType;
                double heightValue = tiles[x][y].heightValue;
                if (tiles[x][y].heightType == HeightType.River)
                    heightValue = tiles[x][y].originalHeightValue;

                double min = Double.MAX_VALUE, max = Double.MIN_VALUE;

                for (Map.Entry<Double, Double> heightRange : heights.entrySet()) {
                    if (heightValue >= heightRange.getKey() && (heightValue <= heightRange.getValue())) {
                        min = heightRange.getKey();
                        max = heightRange.getValue();
                    }
                }

                if (value != null) {

                    switch (value) {
                        case Ice:
                            pixmap.setColor(Color.rgba8888(DarkIce.cpy().lerp(Ice, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case BorealForest:
                            pixmap.setColor(Color.rgba8888(DarkBorealForest.cpy().lerp(BorealForest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Desert:
                            pixmap.setColor(Color.rgba8888(DarkDesert.cpy().lerp(Desert, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Grassland:
                            pixmap.setColor(Color.rgba8888(DarkGrassland.cpy().lerp(Grassland, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case SeasonalForest:
                            pixmap.setColor(Color.rgba8888(DarkSeasonalForest.cpy().lerp(SeasonalForest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Tundra:
                            pixmap.setColor(Color.rgba8888(DarkTundra.cpy().lerp(Tundra, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Savanna:
                            pixmap.setColor(Color.rgba8888(DarkSavanna.cpy().lerp(Savanna, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case TemperateRainforest:
                            pixmap.setColor(Color.rgba8888(DarkTemperateRainforest.cpy().lerp(TemperateRainforest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case TropicalRainforest:
                            pixmap.setColor(Color.rgba8888(DarkTropicalRainforest.cpy().lerp(TropicalRainforest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Woodland:
                            pixmap.setColor(Color.rgba8888(DarkWoodland.cpy().lerp(Woodland, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                    }
                }

                // Water tiles
                if (tiles[x][y].heightType == HeightType.DeepWater) {
                    pixmap.setColor(Color.rgba8888(DeepColor.cpy().lerp(MediumColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                } else if (tiles[x][y].heightType == HeightType.MediumWater) {
                    pixmap.setColor(Color.rgba8888(MediumColor.cpy().lerp(ShallowColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                } else if (tiles[x][y].heightType == HeightType.ShallowWater) {
                    pixmap.setColor(Color.rgba8888(ShallowColor.cpy().lerp(CoastalColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                } else if (tiles[x][y].heightType == HeightType.CoastalWater) {
                    pixmap.setColor(Color.rgba8888(ShallowColor.cpy().lerp(FoamColor, (float) heightValue)));
                    fillRectangle(pixmap, x, y, width, height);
                }

                // draw riverPaths
                double oldRange = 0.5;
                if (tiles[x][y].heightType == HeightType.River) {
                    /*if(heightValue >= Rock)
                        pixmap.drawPixel(x,y, Color.rgba8888(IceWater.cpy().lerp(ColdWater, (float) ((heightValue - Rock)/(1 - Rock)))));
                    else*/
                    pixmap.setColor(Color.rgba8888(ShallowColor.cpy().lerp(CoastalColor, (float) heightValue)));
                    fillRectangle(pixmap, x, y, width, height);

                }

                if(tiles[x][y].heightType == HeightType.DebugSource) {
                    pixmap.setColor(Color.RED);
                    pixmap.fillCircle(x,y,3);
                }

                if(tiles[x][y].heightType == HeightType.DebugDestination) {
                    pixmap.setColor(Color.GREEN);
                    pixmap.fillCircle(x,y,3);
                }

                if(tiles[x][y].heightType == HeightType.DebugCoastline || tiles[x][y].heightType == HeightType.Shore) {
                    pixmap.setColor(Color.YELLOW);
                    fillRectangle(pixmap, x, y, width, height);
                }


                // add a outline
               /* if (tiles[x][y].heightType.getNumVal() >= HeightType.Shore.getNumVal() && tiles[x][y].heightType != HeightType.River)
                {
                    if (tiles[x][y].biomeBitmask != 15) {
                        color = pixmap.getPixel(x,y);
                        tgt = 0;
                        pixmap.drawPixel(x * pixelSize, y* pixelSize, ((int)((color >>> 24) * inv + (tgt >>> 24) * degree) << 24) | ((int)((color >>> 16 & 0xff) * inv + (tgt >>> 16 & 0xff) * degree) << 16) | ((int)((color >>> 8 & 0xff) * inv + (tgt >>> 8 & 0xff) * degree) << 8) | 0xff);
                        pixmap.drawPixel(x * pixelSize, y* pixelSize, Color.rgba8888(new Color(pixmap.getPixel(x, y)).lerp(Color.BLACK, 0.35f)));
                    }

                }*/
/*                if (pixmap.getPixel(x,y) == 1 || pixmap.getPixel(x,y) == 0) {
                    pixmap.setColor(Color.BLACK);
                    pixmap.fillCircle(x,y,1);
                }*/
            }
        }
        PixmapIO.writePNG(Gdx.files.getFileHandle(filename, Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    public static Texture generateBiomeMapTexture(String filename, int[][] biomeCodeMap) {
        int width = biomeCodeMap[0].length;
        int height = biomeCodeMap.length;

        Pixmap pixmap = new Pixmap(width * pixelSize * (isometric ? 2 : 1), height * pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(width * pixelSize * (isometric ? 2 : 1), height * pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        String[] biomeTable = WorldMapGenerator.SimpleBiomeMapper.biomeTable;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String value = biomeTable[biomeCodeMap[x][y]];

                if (value != null) {

                    switch (value) {
                        case "Ice":
                            pixmap.setColor(Color.rgba8888(Ice));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "BorealForest":
                            pixmap.setColor(Color.rgba8888(BorealForest));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Desert":
                            pixmap.setColor(Color.rgba8888(Desert));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Grassland":
                            pixmap.setColor(Color.rgba8888(Grassland));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "SeasonalForest":
                            pixmap.setColor(Color.rgba8888(SeasonalForest));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Tundra":
                            pixmap.setColor(Color.rgba8888(Tundra));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Savanna":
                            pixmap.setColor(Color.rgba8888(Savanna));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "TemperateRainforest":
                            pixmap.setColor(Color.rgba8888(TemperateRainforest));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "TropicalRainforest":
                            pixmap.setColor(Color.rgba8888(TropicalRainforest));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Woodland":
                            pixmap.setColor(Color.rgba8888(Woodland));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Rocky":
                            pixmap.setColor(Color.rgba8888(MediumColor));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Beach":
                            pixmap.setColor(Color.rgba8888(CoastalColor));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "River":
                            pixmap.setColor(Color.rgba8888(MediumColor));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case "Ocean":
                            pixmap.setColor(Color.rgba8888(DeepColor));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                    }
                }
            }

                /*if(tiles[x][y].heightType == HeightType.DebugSource) {
                    pixmap.setColor(Color.RED);
                    pixmap.fillCircle(x,y,3);
                }

                if(tiles[x][y].heightType == HeightType.DebugDestination) {
                    pixmap.setColor(Color.GREEN);
                    pixmap.fillCircle(x,y,3);
                }

                if(tiles[x][y].heightType == HeightType.DebugCoastline) {
                    pixmap.setColor(Color.YELLOW);
                    pixmap.fillCircle(x,y,1);
                }
*/

                    // add a outline
               /* if (tiles[x][y].heightType.getNumVal() >= HeightType.Shore.getNumVal() && tiles[x][y].heightType != HeightType.River)
                {
                    if (tiles[x][y].biomeBitmask != 15) {
                        color = pixmap.getPixel(x,y);
                        tgt = 0;
                        pixmap.drawPixel(x * pixelSize, y* pixelSize, ((int)((color >>> 24) * inv + (tgt >>> 24) * degree) << 24) | ((int)((color >>> 16 & 0xff) * inv + (tgt >>> 16 & 0xff) * degree) << 16) | ((int)((color >>> 8 & 0xff) * inv + (tgt >>> 8 & 0xff) * degree) << 8) | 0xff);
                        pixmap.drawPixel(x * pixelSize, y* pixelSize, Color.rgba8888(new Color(pixmap.getPixel(x, y)).lerp(Color.BLACK, 0.35f)));
                    }

                }*/
            }
            PixmapIO.writePNG(Gdx.files.getFileHandle(filename, Files.FileType.Local), pixmap);
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            texture.draw(pixmap, 0, 0);
            return texture;
    }


    public static Texture generateMoistureMapTexture(MapGeneratorInput input, Tile[][] tiles) {
        Pixmap pixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        int width = input.WIDTH;
        int height = input.HEIGHT;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                switch (tiles[x][y].moistureType) {
                    case Dryest:
                        pixmap.setColor(Color.rgba8888(Dryest));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Dryer:
                        pixmap.setColor(Color.rgba8888(Dryer));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Dry:
                        pixmap.setColor(Color.rgba8888(Dry));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Wet:
                        pixmap.setColor(Color.rgba8888(Wet));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Wetter:
                        pixmap.setColor(Color.rgba8888(Wetter));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    default:
                        pixmap.setColor(Color.rgba8888(Wettest));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                }
            }
        }
        PixmapIO.writePNG(Gdx.files.getFileHandle("moisturemap.png" , Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    public static Texture generateHeatMapTexture(MapGeneratorInput input, Tile[][] tiles) {
        Pixmap pixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        int width = input.WIDTH;
        int height = input.HEIGHT;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                switch (tiles[x][y].heatType) {
                    case Coldest:
                        pixmap.setColor(Color.rgba8888(Coldest));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Colder:
                        pixmap.setColor(Color.rgba8888(Colder));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Cold:
                        pixmap.setColor(Color.rgba8888(Cold));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Warm:
                        pixmap.setColor(Color.rgba8888(Warm));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Warmer:
                        pixmap.setColor(Color.rgba8888(Warmer));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                    case Warmest:
                        pixmap.setColor(Color.rgba8888(Warmest));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                }

                /*if (tiles[x][y].heightType.getNumVal() > 2 && tiles[x][y].bitmask != 15)
                    pixmap.drawPixel(x * pixelSize, y* pixelSize, Color.rgba8888(new Color(pixmap.getPixel(x,y)).lerp(Color.BLACK, 0.4f)));*/
            }

        }
        PixmapIO.writePNG(Gdx.files.getFileHandle("heatmap.png" , Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }


    public static Texture generateHeightMapTexture(MapGeneratorInput input, Tile[][] tiles, boolean generateNormalMap) {
        return generateHeightMapTexture(null, input, tiles, generateNormalMap);
    }

    public static Texture generateHeightMapTexture(String fileName, MapGeneratorInput input, Tile[][] tiles, boolean generateNormalMap) {
        Pixmap pixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        int width = input.WIDTH;
        int height = input.HEIGHT;

        Pixmap noisePixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Pixmap invertedNoisePixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Pixmap bwHeightmapPixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);


        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                double heightValue = tiles[x][y].heightValue;
                double invertedHeightValue = 1-heightValue;
                pixmap.setColor(Color.rgba8888(new Color((float)heightValue, (float)heightValue, (float)heightValue, pixelSize)));
                fillRectangle(noisePixmap, x, y, width, height);

                pixmap.setColor(Color.rgba8888(new Color((float)invertedHeightValue, (float)invertedHeightValue, (float)invertedHeightValue, pixelSize)));
                fillRectangle(invertedNoisePixmap, x, y, width, height);

                switch (tiles[x][y].heightType)
                {
                    case DeepWater:
                        pixmap.setColor(Color.rgba8888(DeepColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.DeepWater, (float)input.DeepWater, (float)input.DeepWater, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case ShallowWater:
                        pixmap.setColor(Color.rgba8888(ShallowColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.ShallowWater, (float)input.ShallowWater, (float)input.ShallowWater, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case MediumWater:
                        pixmap.setColor(Color.rgba8888(MediumColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.MediumWater, (float)input.MediumWater, (float)input.MediumWater, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case CoastalWater:
                        pixmap.setColor(Color.rgba8888(CoastalColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.CoastalWater, (float)input.CoastalWater, (float)input.CoastalWater, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case Sand:
                        pixmap.setColor(Color.rgba8888(SandColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.Sand, (float)input.Sand, (float)input.Sand, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case Grass:
                        pixmap.setColor(Color.rgba8888(GrassColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.Grass, (float)input.Grass, (float)input.Grass, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case Forest:
                        pixmap.setColor(Color.rgba8888(ForestColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.Forest, (float)input.Forest, (float)input.Forest, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case Rock:
                        pixmap.setColor(Color.rgba8888(RockColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color((float)input.Rock, (float)input.Rock, (float)input.Rock, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case Snow:
                        pixmap.setColor(Color.rgba8888(SnowColor));
                        fillRectangle(pixmap, x, y, width, height);

                        pixmap.setColor(Color.rgba8888(new Color(1, 1, 1, 1)));
                        fillRectangle(bwHeightmapPixmap, x, y, width, height);
                        break;
                    case River:
                        pixmap.setColor(Color.rgba8888(RiverColor));
                        fillRectangle(pixmap, x, y, width, height);
                        break;
                }

                //darken the color if a edge tile
                /*if (tiles[x][y].heightType.getNumVal() > 2 && tiles[x][y].bitmask != 15)
                    pixmap.drawPixel(x,y, Color.rgba8888(new Color(pixmap.getPixel(x,y)).lerp(Color.BLACK, 0.4f)));*/


            }
        }
        if (fileName == null) {
            PixmapIO.writePNG(Gdx.files.getFileHandle("color_heightmap.png" , Files.FileType.Local), pixmap);
            PixmapIO.writePNG(Gdx.files.getFileHandle("heightmap.png" , Files.FileType.Local), bwHeightmapPixmap);
            PixmapIO.writePNG(Gdx.files.getFileHandle("noisemap.png" , Files.FileType.Local), noisePixmap);
            PixmapIO.writePNG(Gdx.files.getFileHandle("invertedNoiseMap.png" , Files.FileType.Local), invertedNoisePixmap);
        } else {
            PixmapIO.writePNG(Gdx.files.getFileHandle(fileName, Files.FileType.Local), pixmap);
        }

        // Generate Normal map
        if(generateNormalMap) {
            generateNormalMapTexture(input, tiles, 5.0f);
        }

        bwHeightmapPixmap.dispose();
        noisePixmap.dispose();
        invertedNoisePixmap.dispose();


        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    private static Texture generateNormalMapTexture(MapGeneratorInput input, Tile[][] heightmap, float strength) {
        Pixmap pixmap = new Pixmap(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(input.WIDTH * pixelSize * (isometric ? 2 : 1), input.HEIGHT* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        int width = input.WIDTH;
        int height = input.HEIGHT;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // surrounding pixels
                final double topLeft = heightmap[clampTile(x - 1, width-1)][clampTile(y - 1, height-1)].heightValue;
                final double top = heightmap[clampTile(x - 1, width-1)][clampTile(y, height-1)].heightValue;
                final double topRight = heightmap[clampTile(x - 1, width-1)][clampTile(y + 1, height-1)].heightValue;
                final double right = heightmap[clampTile(x, width-1)][clampTile(y + 1, height-1)].heightValue;
                final double bottomRight = heightmap[clampTile(x + 1, width-1)][clampTile(y + 1, height-1)].heightValue;
                final double bottom = heightmap[clampTile(x + 1, width-1)][clampTile(y, height - 1)].heightValue;
                final double bottomLeft = heightmap[clampTile(x + 1, width-1)][clampTile(y - 1, height-1)].heightValue;
                final double left = heightmap[clampTile(x, width-1)][clampTile(y - 1, height-1)].heightValue;


                // their intensities
                /*final double tl = pixelIntensity(topLeft);
                final double t = pixelIntensity(top);
                final double tr = pixelIntensity(topRight);
                final double r = pixelIntensity(right);
                final double br = pixelIntensity(bottomRight);
                final double b = pixelIntensity(bottom);
                final double bl = pixelIntensity(bottomLeft);
                final double l = pixelIntensity(left);*/

                final float dX = (float) ((topRight + 2.0f * right + bottomRight) - (topLeft + 2.0f * left + bottomLeft));
                final float dY = (float) ((bottomLeft + 2.0f * bottom + bottomRight) - (topLeft + 2.0f * top + topRight));
                final float dZ = 1.0f / strength;

                Vector3 normal = new Vector3(dX, dY, dZ).nor();

                normal.x = (((normal.x +1) * 1) / 2) + 0;
                normal.y = (((normal.y +1) * 1) / 2) + 0;
                normal.z = (((normal.z +1) * 1) / 2) + 0;

                pixmap.setColor(Color.rgba8888(new Color(normal.x, normal.y, normal.z, 1)));
                fillRectangle(pixmap, x, y, width, height);
            }
        }

        PixmapIO.writePNG(Gdx.files.getFileHandle("normalmap.png", Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    public static Texture generatePoliticalMapTexture(MapGeneratorInput input, OrderedMap<Character, String> atlas, char[][] politicalMap, final Map<Character, SColor> factionColorMap, Tile[][] tiles) {
        int width = input.WIDTH;
        int height = input.HEIGHT;
        Pixmap pixmap = new Pixmap(width * pixelSize * (isometric ? 2 : 1), height* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        Texture texture = new Texture(width * pixelSize * (isometric ? 2 : 1), height* pixelSize * (isometric ? 2 : 1), Pixmap.Format.RGBA8888);
        
        for(int i = 0; i < atlas.keySet().size(); i++) {
            char symbol = atlas.keyAt(i);
            if(Character.isLetter(symbol)) {
                factionColorMap.put(symbol, factionColorMap.get(symbol));
            }
        }

        Map<Double, Double> heights = new HashMap<Double, Double>();
        heights.put(0.0, input.DeepWater);
        heights.put(input.DeepWater, input.ShallowWater);
        heights.put(input.ShallowWater, input.Sand);
        heights.put(input.Sand, input.Grass);
        heights.put(input.Grass, input.Forest);
        heights.put(input.Forest, input.Rock);
        heights.put(input.Rock, 1.0);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                BiomeType value = tiles[x][y].biomeType;
                double heightValue = tiles[x][y].heightValue;

                double min = Double.MAX_VALUE, max = Double.MIN_VALUE;

                for (Map.Entry<Double, Double> heightRange : heights.entrySet()) {
                    if (heightValue >= heightRange.getKey() && (heightValue <= heightRange.getValue())) {
                        min = heightRange.getKey();
                        max = heightRange.getValue();
                    }
                }


                if (value != null) {

                    switch (value) {
                        case Ice:
                            pixmap.setColor(Color.rgba8888(DarkIce.cpy().lerp(Ice, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case BorealForest:
                            pixmap.setColor(Color.rgba8888(DarkBorealForest.cpy().lerp(BorealForest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Desert:
                            pixmap.setColor(Color.rgba8888(DarkDesert.cpy().lerp(Desert, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Grassland:
                            pixmap.setColor(Color.rgba8888(DarkGrassland.cpy().lerp(Grassland, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case SeasonalForest:
                            pixmap.setColor(Color.rgba8888(DarkSeasonalForest.cpy().lerp(SeasonalForest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Tundra:
                            pixmap.setColor(Color.rgba8888(DarkTundra.cpy().lerp(Tundra, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Savanna:
                            pixmap.setColor(Color.rgba8888(DarkSavanna.cpy().lerp(Savanna, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case TemperateRainforest:
                            pixmap.setColor(Color.rgba8888(DarkTemperateRainforest.cpy().lerp(TemperateRainforest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case TropicalRainforest:
                            pixmap.setColor(Color.rgba8888(DarkTropicalRainforest.cpy().lerp(TropicalRainforest, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                        case Woodland:
                            pixmap.setColor(Color.rgba8888(DarkWoodland.cpy().lerp(Woodland, (float) ((heightValue - min) / (max - min)))));
                            fillRectangle(pixmap, x, y, width, height);
                            break;
                    }
                }

                // Water tiles
                if (tiles[x][y].heightType == HeightType.DeepWater) {
                    pixmap.setColor(Color.rgba8888(DeepColor.cpy().lerp(MediumColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                } else if (tiles[x][y].heightType == HeightType.MediumWater) {
                    pixmap.setColor(Color.rgba8888(MediumColor.cpy().lerp(ShallowColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                } else if (tiles[x][y].heightType == HeightType.ShallowWater) {
                    pixmap.setColor(Color.rgba8888(ShallowColor.cpy().lerp(CoastalColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                } else if (tiles[x][y].heightType == HeightType.CoastalWater) {
                    pixmap.setColor(Color.rgba8888(CoastalColor.cpy().lerp(FoamColor, (float) ((heightValue - min) / (max - min)))));
                    fillRectangle(pixmap, x, y, width, height);
                }

                // draw riverPaths
                if (tiles[x][y].heightType == HeightType.River) {
                    double heatValue = tiles[x][y].heatValue;

                    if (tiles[x][y].heatType == HeatType.Coldest) {
                        pixmap.setColor(Color.rgba8888(DeepColor.cpy().lerp(MediumColor, (float) ((heatValue) / (input.ColdestValue)))));
                        fillRectangle(pixmap, x, y, width, height);
                    } else if (tiles[x][y].heatType == HeatType.Colder) {
                        pixmap.setColor(Color.rgba8888(MediumColor.cpy().lerp(ShallowColor, (float) ((heatValue - input.ColdestValue) / (input.ColderValue - input.ColdestValue)))));
                        fillRectangle(pixmap, x, y, width, height);
                    } else if (tiles[x][y].heatType == HeatType.Cold) {
                        pixmap.setColor(Color.rgba8888(ShallowColor.cpy().lerp(CoastalColor, (float) ((heatValue - input.ColderValue) / (input.ColdValue - input.ColderValue)))));
                        fillRectangle(pixmap, x, y, width, height);
                    } else {
                        pixmap.setColor(Color.rgba8888(FoamColor));
                        fillRectangle(pixmap, x, y, width, height);
                    }

                }

                char symbol = politicalMap[x][y];
                if (factionColorMap.get(symbol) != null) {
                    pixmap.setColor(Color.rgba8888(new Color(pixmap.getPixel(x, y)).lerp(factionColorMap.get(symbol), .75f)));
                    fillRectangle(pixmap, x, y, width, height);
                }
            }
        }

        //DungeonUtility.debugPrint(politicalMap);
        if (debug) {
            for (int i = 0; i < factionColorMap.size() + 2; i++) {
                System.out.println("  " + atlas.keyAt(i) + "  :  " + atlas.getAt(i));
            }
            System.out.println();

            for (int i = 0; i < atlas.size(); i++) {
                SColor c = factionColorMap.get(atlas.keyAt(i));
                if(c != null)
                    System.out.println("  " + atlas.keyAt(i) + "  :  " + c.getName().replace("DB ", "") + "  :  " + atlas.getAt(i));
                else
                    System.out.println("  " + atlas.keyAt(i) + "  :  " + atlas.getAt(i));
            }
            System.out.println();
        }

        PixmapIO.writePNG(Gdx.files.getFileHandle("territorymap.png", Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    public static Texture getWindMapTexture(int width, int height, Tile[][] tiles, double[][] wind) {
        Pixmap pixmap = new Pixmap(width * pixelSize, height* pixelSize, Pixmap.Format.RGBA8888);
        Texture texture = new Texture(width * pixelSize, height* pixelSize, Pixmap.Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixmap.drawPixel(x * pixelSize, y* pixelSize, Color.rgba8888(windColor(wind[y][x])));
            }
        }

        PixmapIO.writePNG(Gdx.files.getFileHandle("windmap.png", Files.FileType.Local), pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.draw(pixmap, 0, 0);
        return texture;
    }

    private static Color windColor(double direction) {
        if(direction > 0.75)
            return gradient(direction, 0.75, 1.0, WindWest, WindNorth);
        if(direction > 0.5)
            return gradient(direction, 0.5, 0.75, WindSouth, WindWest);
        if(direction > 0.25)
            return gradient(direction, 0.25, 0.5, WindEast, WindSouth);
        else
            return gradient(direction, 0, 0.25, WindNorth, WindEast);
    }

    private static Color gradient(double value, double low, double high, Color lowColor, Color highColor) {
        if(high == low) return lowColor.cpy();

        double range =  high - low;
        double x = (value - low) / range;
        double ix = 1.0 - x;

        return new Color(
                (float)(lowColor.r * ix + highColor.r  * x)
                , (float)(lowColor.g * ix + highColor.g  * x)
                , (float)(lowColor.b * ix + highColor.b  * x)
                , 1f
        );
    }

    private static final float getPixelWrap(int x, int y, int width, int height, Pixmap pixmap) {
        if(MathUtils.isPowerOfTwo(width) && MathUtils.isPowerOfTwo(height)) {
            if (x < 0 || x >= width) x = (width + x) & (width - 1);
            if (y < 0 || y >= height) y = (height + y) & (height - 1);
            return pixmap.getPixel(x,y);
        } else {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return pixmap.getPixel((y + height) % height,(x + width) % width);
            } else {
                return pixmap.getPixel(y,x);
            }
        }
    }

    private static final int clampTile(int pX, int pMax)
    {
        if (pX > pMax)
        {
            return pMax;
        }
        else if (pX < 0)
        {
            return 0;
        }
        else
        {
            return pX;
        }
    }

    private static void fillRectangle(Pixmap pixmap, int x, int y, int width, int height) {
        if (isometric) {
            pixmap.fillRectangle(x - y + width, height*2 - (x + y) , pixelSize*2, pixelSize);
        } else {
            pixmap.fillRectangle(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
        }
    }

    private static final Vector3 negate(Vector3 v) {
        return v.set(-v.x, -v.y, -v.z);
    }

}
