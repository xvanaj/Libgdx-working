package com.mygdx.game.world.builder;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.name.NPCNameGenerator;
import com.mygdx.game.name.NameTheme;
import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.ReligionType;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.being.Size;
import com.mygdx.game.world.enums.character.Alignment;
import com.mygdx.game.world.enums.world.TerrainType;
import com.mygdx.game.world.params.TerrainComposition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class RaceTypeBuilder {

    private RaceType raceType;

    public RaceTypeBuilder() {
        this.raceType = new RaceType();
    }

    public RaceType build() {
        return raceType;
    }

    public RaceTypeBuilder generate(World world) {
        raceType.setFavouriteTerrains(new HashSet<>());
        final TerrainComposition terrainComposition = world.getTerrainComposition();
        int minTemp = world.getMinTemperature();
        int maxTemp = world.getMaxTemperature();

        if ((terrainComposition.getLowlands() > terrainComposition.getHills()) && (terrainComposition.getLowlands() > terrainComposition.getMountains())) {
            if (minTemp < -20) {
                raceType.getFavouriteTerrains().add(TerrainType.TUNDRA);
            } else {
                raceType.getFavouriteTerrains().add(TerrainType.GRASSLAND);
            }
        } else if ((terrainComposition.getHills() > terrainComposition.getLowlands()) && (terrainComposition.getHills() > terrainComposition.getMountains())) {
            if (minTemp < -20) {
                raceType.getFavouriteTerrains().add(TerrainType.GRASS_HILL);
            } else {
                raceType.getFavouriteTerrains().add(TerrainType.TUNDRA);
            }
        } else if ((terrainComposition.getLowlands() > terrainComposition.getHills()) && (terrainComposition.getLowlands() > terrainComposition.getHills()))
        {
            if (minTemp < -20) {
                raceType.getFavouriteTerrains().add(TerrainType.GRASSLAND);
            } else {
                raceType.getFavouriteTerrains().add(TerrainType.TUNDRA);
            }
        }


        return this;
    }



    public RaceTypeBuilder generate() {
        raceType.setFavouriteTerrains(new HashSet<>());
        raceType.getFavouriteTerrains().add(Randomizer.get(getAllowedTerrainTypes()));
        raceType.setAlignment(Randomizer.get(Alignment.values()));
        raceType.setStartingAge(Randomizer.range(10, 40));
        raceType.setOldAge(Randomizer.range(raceType.getStartingAge() * 3, raceType.getStartingAge() * 4));
        raceType.setMaxAge(raceType.getOldAge() + Randomizer.range(raceType.getStartingAge() * 2, raceType.getStartingAge() * 3));
        raceType.setReligionType(Randomizer.get(ReligionType.values()));
        raceType.setSize(Randomizer.get(Arrays.stream(Size.values()).filter(size -> Arrays.asList(Size.LARGE, Size.MEDIUM, Size.TINY, Size.SMALL).contains(size)).collect(Collectors.toList())));
        raceType.setColdResistance(Randomizer.range(-20,20));
        raceType.setHeatResistance(Randomizer.range(-20,20));
        raceType.setIdealGravity(Randomizer.range(0.6f,1.4f));
        raceType.setFertilityRate(raceType.getMaxAge() / 4);

        raceType.setPossibleHairColors(new HashSet<>());
        raceType.getPossibleHairColors().add(Color.BLACK);
        raceType.getPossibleHairColors().add(Color.BROWN);
        if (Randomizer.range(1,10) == 10) {
            raceType.getPossibleHairColors().add(Randomizer.get(Color.BLUE,Color.RED, Color.SCARLET));
        }
        raceType.setSkinColor(Randomizer.get(ElderBeing.SkinColor.values()));
        raceType.setSpeciesType(Randomizer.get(ElderBeing.SpeciesType.values()));
        this.generateRandomNamesConfiguration();
        raceType.setSampleNames(NPCNameGenerator.generateRaceTypeNames(raceType.getNameTheme(), raceType.getFirstNameMinLength(),
                raceType.getFirstNameMaxLength(), raceType.getLastNameMinLength(), raceType.getLastNameMaxLength(), 10));
        raceType.setHeadType(Randomizer.get(ElderBeing.HeadType.values()));
        String name = new String();

        name += raceType.getSize() + " ";
        name += raceType.getAlignment() + " ";
        name += raceType.getSpeciesType();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();

        raceType.setName(name);

        return this;
    }

    private List<TerrainType> getAllowedTerrainTypes() {
        return Arrays.stream(TerrainType.values())
                .filter(terrain -> !terrain.equals(TerrainType.SHALLOW_OCEAN) && !terrain.equals(TerrainType.DEEP_OCEAN) &&
                        !terrain.equals(TerrainType.EMPTYNESS))
                .collect(Collectors.toList());
    }

    private RaceTypeBuilder generateRandomNamesConfiguration() {
        raceType.setNameTheme(Randomizer.get(NameTheme.values()));

        int random100 = Randomizer.range(0, 100);
        if (random100 > 90) {
            raceType.setFirstNameMinLength(1);
            raceType.setFirstNameMaxLength(raceType.getFirstNameMinLength() + Randomizer.range(0,2));
        } else if (random100 < 20) {
            raceType.setFirstNameMinLength(3);
            raceType.setFirstNameMaxLength(raceType.getFirstNameMinLength() + Randomizer.range(0,1));
        } else {
            raceType.setFirstNameMinLength(2);
            raceType.setFirstNameMaxLength(raceType.getFirstNameMinLength() + Randomizer.range(0,1));
        }

        random100 = Randomizer.range(0, 100);
        if (random100 > 90) {
            raceType.setLastNameMinLength(1);
            raceType.setLastNameMaxLength(raceType.getLastNameMinLength() + Randomizer.range(0,2));
        } else if (random100 < 20) {
            raceType.setLastNameMinLength(3);
            raceType.setLastNameMaxLength(raceType.getLastNameMinLength() + Randomizer.range(0,1));

        } else {
            raceType.setLastNameMinLength(2);
            raceType.setLastNameMaxLength(raceType.getLastNameMinLength() + Randomizer.range(0,1));

        }

        return this;
    }
}
