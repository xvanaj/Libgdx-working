package com.mygdx.game.world.generator.world;

import com.mygdx.game.name.CivilizationNameGenerator;
import com.mygdx.game.name.NPCNameGenerator;
import com.mygdx.game.name.NameGenerator;
import com.mygdx.game.name.ReligionNameGenerator;
import com.mygdx.game.utils.CodeGenerator;
import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.builder.TownBuilder;
import com.mygdx.game.world.entity.EntityType;
import com.mygdx.game.world.entity.civilization.*;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.character.Gender;
import com.mygdx.game.world.exception.WorldCreationException;

import java.util.ArrayList;
import java.util.List;

public class CivilizationFactory {


    public static Civilization generateNewCivilization(final World world, final RaceType raceType, final Game game) throws WorldCreationException {
        final Civilization civilization = new Civilization();
        civilization.setCode(CodeGenerator.getNextSequenceNumber(game, EntityType.CIVILIZATION));

        civilization.setRacetype(raceType);
        civilization.setWorld(world);
        civilization.setWorldCode(world.getCode());
        civilization.setPoliticalStructure(Randomizer.get(PoliticalStructure.values()));
        civilization.setForeignPolitics(Randomizer.get(ForeignPolitics.values()));
        civilization.setMainReligion(ReligionNameGenerator.generateReligionName(civilization, raceType));
        civilization.setName(CivilizationNameGenerator.generateCivilizationName(civilization));
        generateTown(civilization, world, game);

        Currency currency = new Currency();
        currency.setName(NameGenerator.generateCurrencyName());
        currency.setPower(1f);
        civilization.setCurrency(currency);

        final NPC leader = new NPC();
        leader.setGender(Randomizer.get(Gender.values()));
        leader.setName(NPCNameGenerator.generateNPCName(raceType));
        leader.setTitle(civilization.getPoliticalStructure().getTitle() + " of " + civilization.getName());
        leader.setRaceType(raceType);
        leader.setTown(civilization.getCapitalTown());
        leader.setTownCode(civilization.getCapitalTown().getCode());

        civilization.setLeader(leader);

        return civilization;
    }

    private static void generateTown(final Civilization civ, final World world, final Game game) throws WorldCreationException {
        if (civ.getAge() == 0) {
            Town town = new TownBuilder().createTown(civ, world, game).build();
            civ.setCapitalTown(town);
            civ.setCapitalTownCode(town.getCode());

            List<Town> towns = new ArrayList<>();
            towns.add(town);
            world.addTowns(towns);

        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }

    }

    public static Civilization generateNewCivilization(final World world, final Game gamme) throws WorldCreationException {
        final RaceType randomRaceType = Randomizer.get(world.getRaces());

        return generateNewCivilization(world, randomRaceType, gamme);
    }
}
