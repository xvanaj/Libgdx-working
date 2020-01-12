package com.mygdx.game.world.entity.universe.world;

import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.entity.universe.IEncyclopediaItem;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.enums.town.BuildingType;
import com.mygdx.game.world.enums.town.CitizenType;
import com.mygdx.game.world.enums.town.JobType;

import com.mygdx.game.name.TownNameGenerator;
import lombok.*;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Town extends WorldEntity implements IEncyclopediaItem {

    private RaceType raceType;
    private int population;
    private Map<CitizenType, Integer> citizens = new HashMap<>(0);
    private transient World world; //todo needed?
    private Map<BuildingType, Integer> buildings = new HashMap<>(0);
    private Map<JobType, Integer> economyBuildings = new HashMap<>(0);

    private float productionPoints = 0;
    private float foodPoints = 0;
    private float citizenPoints = 0;  //1 point per one day per one citizen. Need 365 * 100 / 2 points to grow

    private float cumulativeResidentialHousesGrowth;
    private float cumulativeFoodHousesGrowth;
    private float cumulativePopulationGrowth;

    public void addBuilding(BuildingType buildingType) {
        buildings.put(buildingType, buildings.get(buildingType) + 1);
    }

    public void addBuilding(BuildingType buildingType, int count) {
        buildings.put(buildingType, buildings.get(buildingType) + count);
    }

    public void addBuildings(BuildingType... buildingTypes) {
        for (BuildingType buildingType : buildingTypes) {
            this.buildings.put(buildingType, this.buildings.get(buildingType) + 1);
        }
    }

    @Override
    public String getEncyclopediaDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<h3>");
        sb.append(TownNameGenerator.getTownPrefix(this) + " of " + name);
        sb.append("</h3>");
        sb.append("<br/>");
        sb.append("Population " + getPopulation() + " " + getRaceType() + "s");
        sb.append("<br/>");
        sb.append("Terrain " + getWorld().getTileProperties(new Point(getX(),getY())).getTerrainType());
        sb.append("<br/>");
        sb.append("<p style=\"color:#00BFFF;\">Buildings: </p>");
        for (BuildingType buildingType : buildings.keySet()) {
            if (buildings.get(buildingType) > 0) {
                sb.append(buildingType.getName() + " " + buildings.get(buildingType) + "<br/>");
            }
        }
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public String getEncyclopediaIconName() {
        return "town";
    }

    @Override
    public String getMapInfo() {
        return getName();
    }

    public String getTownInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append(" Name " + getName());
        sb.append(" Population " + getPopulation());
        sb.append(" Citizens " + getCitizens());
        sb.append(" Buildings " + getEconomyBuildings());
        sb.append(" Food stored " + getFoodPoints());
        sb.append(" Production points " + getProductionPoints());
        sb.append(" Citizen points " + getCitizenPoints());

        return sb.toString();
    }

    public void addRandomCitizens(int citizensToAdd) {
        Map<CitizenType, Integer> citizens = getCitizens();
        int newPopulation = population + citizensToAdd;
        Map<JobType, Integer> freeJobs = getFreeJobs();
        int allProbs = Arrays.stream(CitizenType.values()).mapToInt(CitizenType::getChance).sum();

        int newFoodProducers = (int) Math.ceil(newPopulation / (allProbs/CitizenType.FARMER.getChance()));
        if (freeJobs.get(JobType.FOOD) < newFoodProducers) {
            newFoodProducers = freeJobs.get(JobType.FOOD);
        }
        allProbs  = allProbs - CitizenType.FARMER.getChance();
        newPopulation = newPopulation - newFoodProducers;
        int newProdProducers = (int) Math.ceil(newPopulation / (allProbs/CitizenType.BUILDER.getChance()));
        if (freeJobs.get(JobType.PRODUCTIVITY) < newProdProducers) {
            newProdProducers = freeJobs.get(JobType.PRODUCTIVITY);
        }
        allProbs  = allProbs - CitizenType.BUILDER.getChance();
        newPopulation = newPopulation - newProdProducers;
        int newSocProducers = (int) Math.ceil(newPopulation / (allProbs/CitizenType.SOCIAL.getChance()));
        if (freeJobs.get(JobType.SOCIAL) < newSocProducers) {
            newSocProducers = freeJobs.get(JobType.SOCIAL);
        }
        newPopulation = newPopulation - newSocProducers;

        citizens.put(CitizenType.FARMER, newFoodProducers);
        citizens.put(CitizenType.BUILDER, newProdProducers);
        citizens.put(CitizenType.SOCIAL, newSocProducers);
        citizens.put(CitizenType.COMMONER, newPopulation);
        /*

        int population = citizensToAdd;
        for (int i = 0; i < population; i++) {
            int temp = allProbs;
            for (CitizenType citizenType : CitizenType.values()) {
                int random = Randomizer.generate(0, temp);
                if (random < citizenType.getChance()) {
                    addedCitizenType = citizenType;
                    break;
                } else {
                    temp = temp - citizenType.getChance();
                }
            }
            citizens.put(addedCitizenType, citizens.get(addedCitizenType) + 1);
        }
*/
        setPopulation(citizens.values().stream().mapToInt(Integer::intValue).sum());
    }

    public HashMap<JobType, Integer> getFreeJobs() {
        HashMap<JobType, Integer> freeJobs = new HashMap();
        for (JobType jobType : JobType.values()) {
            int job1Count = economyBuildings.get(jobType) == null ? 0 : economyBuildings.get(jobType);
            int job2Count = getCitizens().get(jobType.getWorkerType()) == null ? 0 : getCitizens().get(jobType.getWorkerType());
            freeJobs.put(jobType, job1Count - job2Count);
        }

        return freeJobs;
    }

    public HashMap<JobType, Integer> getJobsCapacity() {
        HashMap<JobType, Integer> freeJobs = new HashMap();
        for (JobType jobType : JobType.values()) {
            freeJobs.put(jobType, economyBuildings.get(jobType) == null ? 0 : economyBuildings.get(jobType));
        }

        return freeJobs;
    }
}
