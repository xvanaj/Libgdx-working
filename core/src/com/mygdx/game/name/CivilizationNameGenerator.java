package com.mygdx.game.name;

import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.ReligionType;

import java.util.Arrays;
import java.util.List;

public class CivilizationNameGenerator {
    public static String generateCivilizationName(final Civilization civilization) {
        String civName = null;
        final RaceType raceType = civilization.getRacetype();

        final boolean religious = !Arrays.asList(ReligionType.ANTI_RELIGIOUS, ReligionType.PAGAN).contains(raceType.getReligionType());
        final List<String> possibleNames = Arrays.asList("Sagoot" ,
                "Donund" ,
                "Woneoth" ,
                "Selhik" ,
                "Dinpheoh" ,
                "Knamsia" ,
                "Schidceal" ,
                "Sroweuh" ,
                "Yohrundaa" ,
                "Knimporo'");

        civName = civilization.getPoliticalStructure().name().substring(0,1) + civilization.getPoliticalStructure().name().toLowerCase().substring(1)
                + " of " + Randomizer.get(possibleNames);

        return civName;
    }
}
