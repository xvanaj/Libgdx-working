package com.mygdx.game.world.algorithm;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.controller.TownController;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.enums.town.BuildingType;
import com.mygdx.game.world.enums.town.CitizenType;
import com.mygdx.game.world.enums.town.JobType;
import com.mygdx.game.name.TownNameGenerator;

import java.util.Map;
import java.util.Random;

/**
 * Created by Jakub Vana on 8.8.2018.
 */
public class CityGrowthAlgorithm {

    public static final float AVERAGE_PERCENT_GROWTH_RATE = 0.01f / 12;
    private final Game game;

    TownController townController;

    public static Random rand = new Random();

    public CityGrowthAlgorithm(final Game game) {
        this.game = game;
    }

    public void addCitizens(Town town) {

    }

    public int addCitizensAfterDay(Town town) {
        float growthConstant = 2;
        int populationDecreaseStarvation = 0;
        int populationIncreaseBirthrate = 0;
        int populationIncreaseSocial = 0;
        int populationIncreaseJobs = 0;
        int populationDecreaseHousing = 0;

        int originalPopulation = town.getPopulation();

        Map<CitizenType, Integer> citizens = town.getCitizens();
        Map<JobType, Integer> buildings = town.getEconomyBuildings();

        /*float foodProducingEffectivity = 100 + town.getRaceType().getFarmingBonus() / 100;
        float buildingsProducingEffectivity = 100 + town.getRace().getBuildingSpeedBonus() / 100;
        float newProductionPoints = town.getProductionPoints() + citizens.get(CitizenType.BUILDER) * buildingsProducingEffectivity * 4;
        float newFood = town.getFoodPoints() + citizens.get(CitizenType.FARMER) * foodProducingEffectivity * 4 + 1;
        int newCitizenPoints = (int) (town.getPopulation() * growthConstant + town.getCitizenPoints());

        int popIncreaseConstant = 365 * 100 / 2;
        if (newCitizenPoints > popIncreaseConstant) {
            populationIncreaseBirthrate = newCitizenPoints / popIncreaseConstant;
            town.setCitizenPoints(newCitizenPoints - popIncreaseConstant * populationIncreaseBirthrate);
        } else {
            town.setCitizenPoints(newCitizenPoints);
        }

        town.setProductionPoints(town.getProductionPoints() + newProductionPoints);
        if (newProductionPoints > popIncreaseConstant) {
            buildings.put(JobType.FOOD, buildings.get(JobType.FOOD) + 1);
            buildings.put(JobType.PRODUCTIVITY, buildings.get(JobType.PRODUCTIVITY) + 1);
            buildings.put(JobType.SOCIAL, buildings.get(JobType.SOCIAL) + 1);
            buildings.put(JobType.HOUSING, buildings.get(JobType.HOUSING) + 1);
            town.setProductionPoints(newProductionPoints - popIncreaseConstant);
        } else {
            town.setProductionPoints(newProductionPoints);
        }

        if (originalPopulation - newFood > 0) {
            populationDecreaseStarvation = (int) ((originalPopulation - newFood) / 10);
        }
        town.setFoodPoints(newFood - originalPopulation);

        DefaultHashMap<JobType, Integer> openedJobPositions = new DefaultHashMap(0);
        openedJobPositions.put(JobType.FOOD, town.getEconomyBuildings().get(JobType.FOOD));
        openedJobPositions.put(JobType.PRODUCTIVITY, town.getEconomyBuildings().get(JobType.PRODUCTIVITY));
        openedJobPositions.put(JobType.SOCIAL, town.getEconomyBuildings().get(JobType.SOCIAL));

        int housing = town.getEconomyBuildings().get(JobType.HOUSING);

        populationDecreaseHousing = (originalPopulation - housing > 0 ? originalPopulation - housing : 0);

        int citizensToAdd = populationIncreaseBirthrate + populationIncreaseJobs + populationIncreaseSocial
                - populationDecreaseHousing - populationDecreaseStarvation;

        town.addRandomCitizens(citizensToAdd);*/

        return 0;
    }

    public int addCitizensAfterMonth(Town town) {
        int originalPopulation = town.getPopulation();

        if (town.getPopulation() < 1) {
            Gdx.app.log("cityGrowth", TownNameGenerator.getTownPrefix(town) + " of " + town.getName() + " is depopulated.");
            return 0;
        }

        //0. New buildings are being built
        createBuildings(town, originalPopulation);

        //1. Citizens might leave because of lack of houses
        checkLivingSpace(town);

        //2. Citizens might starve to death because of lack of food
        boolean cityStarving = checkFoodProduced(town);

        //3. check unhapiness? ie emigration

        //4. New births
        if (!cityStarving) {
            checkBirths(town);
        }

        return town.getPopulation() - originalPopulation;
    }

    private void checkBirths(Town town) {
        float sizeCoefficient = (float) (5 / (Math.pow(town.getPopulation(), 1 / 3)));
        float randomness = ((float)rand.nextInt(10) + 5) / 10;

        float newCitizens = (town.getPopulation() * sizeCoefficient * randomness / 100 / 12) + town.getCumulativePopulationGrowth();
        if (newCitizens < 1) {
            town.setCumulativePopulationGrowth(town.getCumulativePopulationGrowth() + newCitizens);
        } else {

            town.setCumulativePopulationGrowth(0);
            town.setPopulation((int) (town.getPopulation() + newCitizens));
            Gdx.app.log("town",TownNameGenerator.getTownPrefix(town) + " of " + town.getName() + "'s population has increased. "+  (int) newCitizens + " new population were born in the town. Population = " + town.getPopulation());
        }
    }

    private boolean checkFoodProduced(Town town) {
        int foodUnitsProduced = 2 /*foraging*/ + town.getBuildings().keySet().stream()
                .filter(building -> building.getFoodProduced() > 0)
                .mapToInt(building -> getFoodProduced(building, town))
                .sum();

        if (foodUnitsProduced < town.getPopulation()) {
            int starvingCitizens = new Random().nextInt(((town.getPopulation() - foodUnitsProduced) + 1));

            town.setPopulation(town.getPopulation() - starvingCitizens);
            Gdx.app.log("town","Town " + town.getName() + " is not producing enough food. " + starvingCitizens + " population starved to death.");
            return true;
        } else {
            return false;
        }
    }

    private void checkLivingSpace(Town town) {
        int maxPossibleCitizens = town.getBuildings().keySet().stream()
                .filter(building -> building.getResidents() > 0)
                .mapToInt(building -> getMaxBuildingResidents(building, town))
                .sum();

        if (maxPossibleCitizens < town.getPopulation()) {
            int emigratingCitizens = new Random().nextInt(((town.getPopulation() - maxPossibleCitizens)) + 1);

            town.setPopulation(town.getPopulation() - emigratingCitizens);
            Gdx.app.log("town","Town " + town.getName() + " has not enough living quarters. " + emigratingCitizens + " left the city");
        }
    }

    private void createBuildings(Town town, int population) {
        //0. Add residential buildings
        town.setCumulativeResidentialHousesGrowth(town.getCumulativeResidentialHousesGrowth()
                + (AVERAGE_PERCENT_GROWTH_RATE * (population < 100 ? 2 : 1))); //small hamlets are growing faster

        int houseCreationPoints = (int) (town.getCumulativeResidentialHousesGrowth() * population);
/*        int housesCount = houseCreationPoints / BuildingType.SMALL_HOUSE.getResidents();
        if (housesCount > 0) {
            town.addBuilding(BuildingType.SMALL_HOUSE, housesCount);
            town.setCumulativeResidentialHousesGrowth(0);
        }*/

        //1. Add residential buildings
        town.setCumulativeFoodHousesGrowth(town.getCumulativeFoodHousesGrowth()
                + (AVERAGE_PERCENT_GROWTH_RATE * (population < 100 ? 2 : 1))); //small hamlets are growing faster
  /*      int foodHousesCreationPoints = (int) (town.getCumulativeFoodHousesGrowth() * population);
        int foodHousesCount = foodHousesCreationPoints / BuildingType.FISHING_HUT.getFoodProduced();
        if (foodHousesCount > 0) {
            town.addBuilding(BuildingType.FISHING_HUT, foodHousesCount);
            town.setCumulativeFoodHousesGrowth(0);
        }*/
    }

    private int getFoodProduced(BuildingType building, Town town) {
        return town.getBuildings().get(building) * building.getFoodProduced();
    }

    private int getMaxBuildingResidents(BuildingType building, Town town) {
        return town.getBuildings().get(building) * building.getResidents();
    }

}
