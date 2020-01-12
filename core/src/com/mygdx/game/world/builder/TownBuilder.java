package com.mygdx.game.world.builder;

import com.mygdx.game.name.TownNameGenerator;
import com.mygdx.game.utils.CodeGenerator;
import com.mygdx.game.world.algorithm.CityGrowthAlgorithm;
import com.mygdx.game.world.entity.EntityType;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.enums.town.JobType;
import com.mygdx.game.world.exception.WorldCreationException;
import com.mygdx.game.world.generator.game.GameFactory;

import java.util.HashMap;
import java.util.Random;

public class TownBuilder {

    private Town town;

    public TownBuilder() {
        town = new Town();
    }

    private String createRandomTownName() {
        return TownNameGenerator.getTownName(town.getRaceType() == null ? "" : town.getRaceType().getName());
    }

    public TownBuilder createMinimalTown() {
        Random rand = new Random();

        final HashMap economyBuildings = new HashMap();
        economyBuildings.put(JobType.FOOD, 10);
        economyBuildings.put(JobType.PRODUCTIVITY, 10);
        economyBuildings.put(JobType.HOUSING, 10);
        economyBuildings.put(JobType.SOCIAL, 1);

        town.setEconomyBuildings(economyBuildings);
        town.addRandomCitizens(2);
        town.setName(createRandomTownName());

        return this;
    }

    public TownBuilder createTown(int population) {
        Random rand = new Random();

        final HashMap economyBuildings = new HashMap();
        economyBuildings.put(JobType.FOOD, population);
        economyBuildings.put(JobType.PRODUCTIVITY, population);
        economyBuildings.put(JobType.HOUSING, population);
        economyBuildings.put(JobType.SOCIAL, population/10);

        town.setEconomyBuildings(economyBuildings);
        town.addRandomCitizens(population);
        town.setName(createRandomTownName());

        return this;
    }

    public Town build() {
        return town;
    }

    public TownBuilder citizens(int townPopulation) {
        town.setPopulation(townPopulation);

        return this;
    }

    public static void main(String[] args) throws WorldCreationException {
        TownBuilder townBuilder = new TownBuilder();
        //Town town = townBuilder.createMinimalTown().build();
        Town town = townBuilder.createTown(1000000).build();

        CityGrowthAlgorithm algorithm = new CityGrowthAlgorithm(GameFactory.createGame(Difficulty.MEDIUM));
        for (int i = 0; i < 365 * 10; i++) {
            System.out.println(town.getTownInfo());
            algorithm.addCitizensAfterDay(town);
        }
    }

    public TownBuilder createTown(final Civilization civ, final World world, final Game game) {
        createTown(10);
        town.setRaceType(civ.getRacetype());
        town.setWorld(world);
        town.setCode(CodeGenerator.getNextSequenceNumber(game, EntityType.TOWN));

        return this;
    }
}
