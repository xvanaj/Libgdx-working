package com.mygdx.game.name;

import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.ReligionType;

import java.util.Arrays;

public class ReligionNameGenerator {
    public static String generateReligionName(final Civilization civilization, final RaceType raceType) {
        String religionName = null;

        if (raceType.getReligionType().equals(ReligionType.ANTI_RELIGIOUS)) {
            return null;
        } else if (raceType.getReligionType().equals(ReligionType.PAGAN)) {
            return "Pagan";
        } else {
            return Randomizer.get(Arrays.asList("Angels of the Rapture",
                    "Children of Redemption",
                    "Creed of our New Lord",
                    "Faith of the Martyr",
                    "Healers of Blicrazor",
                    "Oracles of Gixito",
                    "Cult of Klogifis"));
        }
    }
}
