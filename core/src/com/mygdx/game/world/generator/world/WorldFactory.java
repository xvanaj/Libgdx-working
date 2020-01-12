package com.mygdx.game.world.generator.world;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.params.PresetWorldCreationParameters;
import com.mygdx.game.world.params.WorldCreatorParametersFactory;
import com.mygdx.game.world.builder.WorldBuilder;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.enums.world.MapImageType;
import com.mygdx.game.world.exception.WorldCreationException;
import com.mygdx.game.world.params.WorldCreatorInputParameters;

import java.util.Date;

public class WorldFactory {

    public static final String PARAMS_DELIMITER = " ";

    public static World createWorld(WorldCreatorInputParameters params) {
        WorldBuilder builder = new WorldBuilder(params.getGame());
        Date startDate = new Date();
        int attempts = 0;

        while (attempts < params.getCreationAttempts()) {
            try {
                Gdx.app.log("worldGen", "Creating world of size " + params.getWidth() + " x " + params.getHeight());
                builder.resetWorld(params.getGame());
                World world = builder.createBasicLayers(params)
                        .setUniverse(params.getGame().getUniverse())
                        .createTerrain(params.getTerrainComposition())
                        .createVegetation(params)
                        .createNatureWonders()
                        .randomWorldType()
                        .randomMaterialType()
                        .randomName()
                        .createRaces(params)
                        .createCivilizations(params)
                        .createPlayers(params)
                        .generateFreeTowns(params.getTownCreationInputParameters())
                        .generateGates(params.getGatesCreationInputParameters())
                        .generateWildernessLevels()
                        .generateResources(params.getResourceCreationParameters())
                        .generateDungeons(params.getDungeonCreationParameters())
                        .generateAncients(params.getAncientsCreationParameters())
                        .generateMiscelaneous()
                        //.createVoronoi()
                        .createCivRegions(params.getRegionsCreationParameter())
                        .createCivRelations()
                        .createTiledMap()
                        .createMapImage(params)
                        .build();

                Gdx.app.log("worldGen", "Generation done in " + (new Date().getTime() - startDate.getTime()) + " millis");
                return world;
            } catch (WorldCreationException e) {
                attempts++;

                if (params.getPresetWorldCreationParameters() != null) {
                    Game game = params.getGame();
                    params = WorldCreatorParametersFactory.createWorldCreationParams(params.getPresetWorldCreationParameters());
                    params.setGame(game);
                }

                System.err.println(e.getStackTrace()[0]);
                Gdx.app.log("worldGen", "Generation failed in " + (new Date().getTime() - startDate.getTime()) + " millis");
            }
        }

        return null;
    }

    public static World createWorldByDifficulty(Difficulty difficulty) {
        WorldCreatorInputParameters parameters = new WorldCreatorInputParameters();

        parameters.setWidth(difficulty.getWorldWidth());
        parameters.setHeight(difficulty.getWorldHeight());
        parameters.getGatesCreationInputParameters().setGatesCount(difficulty.getStartingGates());
        parameters.getTownCreationInputParameters().setFreeTownsCount(difficulty.getTownsCount());
        parameters.getMapImageParameters().setMapImageType(MapImageType.TILE);
        parameters.getMapImageParameters().setName("generatedTileQuickMap");

        return createWorld(parameters);
    }

    public static World createRandomWorld() {
        WorldCreatorInputParameters parameters = WorldCreatorParametersFactory.createWorldCreationParams(PresetWorldCreationParameters.RANDOM);
        final Game game = new Game();
        game.setName("testGame");
        game.setCode("game1");
        parameters.setGame(game);
        final Universe universe = new Universe();
        game.setUniverse(universe);
        return createWorld(parameters);
    }

/*    public static World createWorld(String params, GameAssetManager gameAssetManager) throws WorldCreationException {
        String[] parameters = params.split(PARAMS_DELIMITER);

        WorldBuilder builder = new WorldBuilder();

        WorldCreatorInputParameters builderParams = new WorldCreatorInputParameters();
        Arrays.stream(parameters).forEach(param -> setParam(param, builderParams));

        return builder.createCustomWorld(universe, builderParams, gameAssetManager).build();
    }

    public static World createWorld(GameAssetManager gameAssetManager, String... params) throws WorldCreationException {
        WorldBuilder builder = new WorldBuilder();

        WorldCreatorInputParameters builderParams = new WorldCreatorInputParameters();
        Arrays.stream(params).forEach(param -> setParam(param, builderParams));

        return builder.createCustomWorld(universe, builderParams, gameAssetManager).build();
    }

    private static void setParam(final String param, final WorldCreatorInputParameters builderParams) {
        switch (param.charAt(0)) {
            case 's': {
                builderParams.getLayersCreationInputParams().setScale(Float.parseFloat(param.substring(1)));
                break;
            }
            case 'o': {
                builderParams.getLayersCreationInputParams().setOctaves(Integer.parseInt(param.substring(1)));
                break;
            }
            case 'r': {
                builderParams.getLayersCreationInputParams().setRougness(Float.parseFloat(param.substring(1)));
                break;
            }
            case 'w': {
                builderParams.setWidth(Integer.parseInt(param.substring(1)));
                break;
            }
            case 'h': {
                builderParams.setHeight(Integer.parseInt(param.substring(1)));
                break;
            }
            case 't': {
                builderParams.getTownCreationInputParameters().setFreeTownsCount(Integer.parseInt(param.substring(1)));
                break;
            }
        }
    }

*/
}
