package core.cartezza.mapgenerator.generator;

import core.cartezza.mapgenerator.generator.domain.BiomeType;
import core.map.common.CommonRNG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapGeneratorInput {

    public MapGenerationType terrainGenerationType = MapGenerationType.CARTEZZA;
    public MapGenerationType heatAndMoistureGenerationType = MapGenerationType.CARTEZZA;

    public boolean generateRivers = true;
    public boolean generateFactions = false;
    public boolean fillDepressions = true;

    public int terrainOctaves = 5;
    public int terrainRidgeOctaves = 5;
    public double terrainFrequency = 1.45;
    public double terrainNoiseScale = 1;

    public int heatOctaves = 4;
    public double heatFrequency = 1;
    public double heatNoiseScale = 1;

    public int moistureOctaves = 4;
    public double moistureFrequency = 1;
    public double moistureNoiseScale = 1;

    public int WIDTH;
    public int HEIGHT;
    public int allowedMaxHeightLevel;

    public int numFactions = CommonRNG.getRng().between(6, 15);
    public double between = CommonRNG.getRng().between(.5, 1);

    public double DeepWater = 0.2;
    public double MediumWater = 0.3;
    public double ShallowWater = 0.4;
    public double CoastalWater = 0.48;
    public double Sand = 0.5;
    public double Grass = 0.7;
    public double Forest = 0.8;
    public double Rock = 0.9;

    public double ColdestValue = 0.05;
    public double ColderValue = 0.18;
    public double ColdValue = 0.4;
    public double WarmValue = 0.6;
    public double WarmerValue = 0.8;

    public double DryerValue = 0.27;
    public double DryValue = 0.4;
    public double WetValue = 0.6;
    public double WetterValue = 0.8;
    public double WettestValue = 0.9;

    public final BiomeType[][] BIOME_TABLE = new BiomeType[][]{
            //COLDEST        //COLDER          //COLD                  //HOT                          //HOTTER                       //HOTTEST
            {BiomeType.Ice, BiomeType.Tundra, BiomeType.Grassland,/**/BiomeType.Desert,/*         */BiomeType.Desert, /*         */BiomeType.Desert},              //DRYEST
            {BiomeType.Ice, BiomeType.Tundra, BiomeType.Grassland,/**/BiomeType.Desert,/*         */ BiomeType.Desert, /*        */BiomeType.Desert},              //DRYER
            {BiomeType.Ice, BiomeType.Tundra, BiomeType.Woodland, /**/BiomeType.Woodland,/*       */ BiomeType.Savanna, /*       */BiomeType.Savanna},             //DRY
            {BiomeType.Ice, BiomeType.Tundra, BiomeType.BorealForest, BiomeType.Woodland,/*       */ BiomeType.Savanna, /*       */BiomeType.Savanna},             //WET
            {BiomeType.Ice, BiomeType.Tundra, BiomeType.BorealForest, BiomeType.SeasonalForest,/* */ BiomeType.TropicalRainforest, BiomeType.TropicalRainforest},  //WETTER
            {BiomeType.Ice, BiomeType.Tundra, BiomeType.BorealForest, BiomeType.TemperateRainforest, BiomeType.TropicalRainforest, BiomeType.TropicalRainforest}   //WETTEST
            // {BiomeType.Ice, BiomeType.Ice,         BiomeType.Grassland,/**/BiomeType.Desert,/*         */BiomeType.Desert, /*         */BiomeType.Desert},              //DRYEST
            // {BiomeType.Ice, BiomeType.Tundra,      BiomeType.Grassland,/**/BiomeType.Grassland,/*         */ BiomeType.Desert, /*        */BiomeType.Desert},              //DRYER
            // {BiomeType.Ice, BiomeType.Tundra,      BiomeType.Woodland, /**/BiomeType.Woodland,/*       */ BiomeType.Savanna, /*       */BiomeType.Desert},             //DRY
            // {BiomeType.Ice, BiomeType.Tundra,      BiomeType.SeasonalForest, BiomeType.SeasonalForest,/*       */ BiomeType.Savanna, /*       */BiomeType.Savanna},             //WET
            // {BiomeType.Ice, BiomeType.Tundra,      BiomeType.BorealForest, BiomeType.TemperateRainforest,/* */ BiomeType.TropicalRainforest, BiomeType.Savanna},  //WETTER
            // {BiomeType.Ice, BiomeType.BorealForest, BiomeType.BorealForest, BiomeType.TemperateRainforest, BiomeType.TropicalRainforest, BiomeType.TropicalRainforest}   //WETTEST
    };

    public static final Map<BiomeType, List<String>> TREE_TYPE_TABLE = new HashMap();
    static {                                                       //NONE  //SMALL   //MEDIUM     //LARGE  //DYING
        TREE_TYPE_TABLE.put(BiomeType.Savanna, Arrays.asList("", "SavannaS", "SavannaM", "SavannaL", "TundraS"));
        TREE_TYPE_TABLE.put(BiomeType.Tundra, Arrays.asList("", "TundraS", "TundraM", "TundraL", "TundraS"));
        TREE_TYPE_TABLE.put(BiomeType.Ice, Arrays.asList("", "SnowS", "SnowM", "SnowL", "TundraM"));
        TREE_TYPE_TABLE.put(BiomeType.Desert, Arrays.asList("", "DesertS", "DesertM", "DesertL", "TundraM"));
        TREE_TYPE_TABLE.put(BiomeType.Grassland, Arrays.asList("", "Small", "Medium", "Large", "TundraL"));
        TREE_TYPE_TABLE.put(BiomeType.Woodland, Arrays.asList("", "WoodlandS", "WoodlandS", "WoodlandS", "TundraS"));
        TREE_TYPE_TABLE.put(BiomeType.BorealForest, Arrays.asList("", "WoodlandS", "WoodlandS", "WoodlandS", "TundraS"));
        TREE_TYPE_TABLE.put(BiomeType.SeasonalForest, Arrays.asList("", "SeasonalForestS", "SeasonalForestM", "SeasonalForestL", "TundraS"));
        TREE_TYPE_TABLE.put(BiomeType.TemperateRainforest, Arrays.asList("", "TemperateRainforestS", "TemperateRainforestS", "TemperateRainforestS", "TundraS"));
        TREE_TYPE_TABLE.put(BiomeType.TropicalRainforest, Arrays.asList("", "TemperateRainforestS", "TemperateRainforestS", "TemperateRainforestS", "TundraS"));
    }
}
