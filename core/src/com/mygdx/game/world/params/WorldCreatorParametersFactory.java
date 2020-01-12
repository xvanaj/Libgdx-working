package com.mygdx.game.world.params;

import com.mygdx.game.world.enums.world.MapImageType;
import com.mygdx.game.world.enums.world.MaskType;
import com.mygdx.game.utils.Randomizer;

import java.util.Random;

public class WorldCreatorParametersFactory {

    public static WorldCreatorInputParameters createWorldCreationParams(final PresetWorldCreationParameters preset) {
        final WorldCreatorInputParameters params;
        switch (preset) {
            case SMALL:
                 params = createSmallWorldCreationParams();
                 break;
            case LARGE:
                params = createLargeWorldCreationParams();
                break;
            case LARGEST:
                params = createLargestWorldCreationParams();
                break;
            case RANDOM:
                params = createRandomWorldCreationParams();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + preset);
        }

        params.setPresetWorldCreationParameters(preset);
        return params;
    }


    public static WorldCreatorInputParameters createRandomWorldCreationParams() {
        WorldCreatorInputParameters parameters = new WorldCreatorInputParameters();

        parameters.getMapImageParameters().setMapImageType(MapImageType.TILE);
        parameters.setRacesCount(Randomizer.range(2,  (int) Math.sqrt(parameters.getHeight() + parameters.getWidth())));
        if (parameters.getHeight() * parameters.getWidth() < 1600) {
            parameters.setContinentsCount(1);
        } else {
            parameters.setContinentsCount(Randomizer.get(1, 3));
        }
        parameters.setAICount(Randomizer.range(2,2));
        parameters.setCivilizationsCount(Randomizer.range(2, (int) Math.sqrt(parameters.getHeight() + parameters.getWidth())-3));
        parameters.getMaskCreatorInputParameters().setMaskType(Randomizer.get(MaskType.valuesBasicLayers()));
        //parameters.getMaskCreatorInputParameters().setTemperatureMaskType(Randomizer.get(MaskType.valuesTemperatureLayers()));
        parameters.getMaskCreatorInputParameters().setTemperatureMaskType(MaskType.REAL_TEMPERATURE_TWO_POLES);
        parameters.getLayersCreationInputParams().setOctaves((Randomizer.range(1,1)));
        parameters.getLayersCreationInputParams().setRougness((float) (new Random().nextDouble() / 2 + 0.2));
        parameters.getLayersCreationInputParams().setScale((float) (new Random().nextDouble() / 15 + 0.03));
        parameters.getTerrainComposition().setWater((float) (Math.abs(new Random().nextGaussian()) + 0.2d)); //0.1
        parameters.getTerrainComposition().setHills((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setLowlands((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setMountains((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getAncientsCreationParameters().setCount((int) Math.sqrt(parameters.getHeight() + parameters.getWidth()
                + Randomizer.generate(0, parameters.getHeight() + parameters.getWidth())));
        //parameters.getAncientsCreationParameters().setCount(1);
        parameters.getTownCreationInputParameters().setFreeTownsCount(1);
        parameters.getGatesCreationInputParameters().setGatesCount(2);
        parameters.getResourceCreationParameters().setTileProbability(Randomizer.range(10, 20));

        return parameters;
    }

    public static WorldCreatorInputParameters createSmallWorldCreationParams() {
        WorldCreatorInputParameters parameters = new WorldCreatorInputParameters();

        parameters.setWidth(20);
        parameters.setHeight(20);

        parameters.getMapImageParameters().setMapImageType(MapImageType.TILE);
        parameters.setRacesCount(2);
        parameters.setAICount(2);
        parameters.setCivilizationsCount(2);

        parameters.getMaskCreatorInputParameters().setMaskType(Randomizer.get(MaskType.valuesBasicLayers()));
        parameters.getMaskCreatorInputParameters().setTemperatureMaskType(MaskType.REAL_TEMPERATURE_TWO_POLES);

        parameters.getLayersCreationInputParams().setOctaves((Randomizer.range(1,1)));
        parameters.getLayersCreationInputParams().setRougness((float) (new Random().nextDouble() / 2 + 0.2));
        parameters.getLayersCreationInputParams().setScale((float) (new Random().nextDouble() / 15 + 0.03));

        parameters.getTerrainComposition().setWater((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setHills((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setLowlands((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setMountains((float) (Math.abs(new Random().nextGaussian()) + 0.5d));

        parameters.getAncientsCreationParameters().setCount(1);
        parameters.getTownCreationInputParameters().setFreeTownsCount(0);
        parameters.getGatesCreationInputParameters().setGatesCount(1);
        parameters.getResourceCreationParameters().setTileProbability(Randomizer.range(10, 20));

        return parameters;
    }

    public static WorldCreatorInputParameters createLargestWorldCreationParams() {
        WorldCreatorInputParameters parameters = new WorldCreatorInputParameters();

        parameters.setWidth(500);
        parameters.setHeight(500);

        parameters.getMapImageParameters().setMapImageType(MapImageType.TILE);
        parameters.setRacesCount(2);
        parameters.setAICount(2);
        parameters.setCivilizationsCount(20);

        parameters.getMaskCreatorInputParameters().setMaskType(Randomizer.get(MaskType.valuesBasicLayers()));
        parameters.getMaskCreatorInputParameters().setTemperatureMaskType(MaskType.REAL_TEMPERATURE_TWO_POLES);

        parameters.getLayersCreationInputParams().setOctaves((Randomizer.range(1,1)));
        parameters.getLayersCreationInputParams().setRougness((float) (new Random().nextDouble() / 2 + 0.2));
        parameters.getLayersCreationInputParams().setScale((float) (new Random().nextDouble() / 15 + 0.03));

        parameters.getTerrainComposition().setWater((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setHills((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setLowlands((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setMountains((float) (Math.abs(new Random().nextGaussian()) + 0.5d));

        parameters.getAncientsCreationParameters().setCount(1000);
        parameters.getDungeonCreationParameters().setMinCount(500);
        parameters.getDungeonCreationParameters().setMaxCount(500);
        parameters.getTownCreationInputParameters().setFreeTownsCount(0);
        parameters.getGatesCreationInputParameters().setGatesCount(100);
        parameters.getResourceCreationParameters().setTileProbability(Randomizer.range(5, 5));

        return parameters;
    }

    public static WorldCreatorInputParameters createLargeWorldCreationParams() {
        WorldCreatorInputParameters parameters = new WorldCreatorInputParameters();

        parameters.setWidth(200);
        parameters.setHeight(200);

        parameters.getMapImageParameters().setMapImageType(MapImageType.TILE);
        parameters.setRacesCount(2);
        parameters.setAICount(2);
        parameters.setCivilizationsCount(20);

        parameters.getMaskCreatorInputParameters().setMaskType(Randomizer.get(MaskType.valuesBasicLayers()));
        parameters.getMaskCreatorInputParameters().setTemperatureMaskType(MaskType.REAL_TEMPERATURE_TWO_POLES);

        parameters.getLayersCreationInputParams().setOctaves((Randomizer.range(1,1)));
        parameters.getLayersCreationInputParams().setRougness((float) (new Random().nextDouble() / 2 + 0.2));
        parameters.getLayersCreationInputParams().setScale((float) (new Random().nextDouble() / 15 + 0.03));

        parameters.getTerrainComposition().setWater((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setHills((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setLowlands((float) (Math.abs(new Random().nextGaussian()) + 0.5d));
        parameters.getTerrainComposition().setMountains((float) (Math.abs(new Random().nextGaussian()) + 0.5d));

        parameters.getAncientsCreationParameters().setCount(2000);
        parameters.getDungeonCreationParameters().setMinCount(100);
        parameters.getDungeonCreationParameters().setMaxCount(100);
        parameters.getTownCreationInputParameters().setFreeTownsCount(0);
        parameters.getGatesCreationInputParameters().setGatesCount(20);
        parameters.getResourceCreationParameters().setTileProbability(Randomizer.range(10, 10));

        return parameters;
    }
}
