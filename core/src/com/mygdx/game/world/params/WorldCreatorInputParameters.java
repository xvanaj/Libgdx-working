package com.mygdx.game.world.params;

import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.generator.game.PlayerCreationParameters;
import com.mygdx.game.world.generator.world.LayersCreationInputParams;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorldCreatorInputParameters {

    private Game game;
    private PresetWorldCreationParameters presetWorldCreationParameters;

    private int creationAttempts = 6;
    private int width = 100;
    private int height = 100;
    private int startingAgeDays = 0;
    private int civilizationsCount = 3;
    private int AICount = 3;
    private int racesCount = 3;
    private int continentsCount = 1;
    private boolean civPerRace = true;

    private boolean heightmapLayer = true;
    private boolean humidityLayer = true;
    private boolean voronoiLayer = true;

    private LayersCreationInputParams layersCreationInputParams = new LayersCreationInputParams();
    private MaskCreationInputParameters maskCreatorInputParameters =  new MaskCreationInputParameters();
    private TownCreationInputParameters townCreationInputParameters = new TownCreationInputParameters();
    private GatesCreationInputParameters gatesCreationInputParameters = new GatesCreationInputParameters();
    private MapImageParameters mapImageParameters = new MapImageParameters();
    private TerrainComposition terrainComposition = new TerrainComposition();
    private DungeonCreationParameters dungeonCreationParameters = new DungeonCreationParameters();
    private ResourceCreationParameters resourceCreationParameters = new ResourceCreationParameters();
    private AncientsCreationParameters ancientsCreationParameters = new AncientsCreationParameters();
    private RegionsCreationParameters regionsCreationParameter = new RegionsCreationParameters();
    private PlayerCreationParameters playerCreationParameters = new PlayerCreationParameters();

}
