package com.mygdx.game.name;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.TextFileContent;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.enums.town.TownSize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TownNameGenerator {

    public static String getTownName(final String type) {
        final TextFileContent namesFile = GameAssetManager.getTextFileContentsStatic("towns");

        Map<TextFileContent.FileKeyType, String> filter = new HashMap<>();
        filter.put(TextFileContent.FileKeyType.TYPE, String.valueOf(type));

        return NameGenerator.generateNameFromFileByFilters(filter, namesFile);
    }

    public static String getTownPrefix(Town town) {
        Arrays.stream(TownSize.values()).filter(townSize -> townSize.getMinPopulation() < town.getPopulation()
                && townSize.getMaxPopulation() < town.getPopulation());

        if (town.getPopulation() < 50) {
            return "Isolated dwelling";
        } else if (town.getPopulation() < 500) {
            return "Hamlet";
        } else if (town.getPopulation() < 1500) {
            return "Village";
        } else if (town.getPopulation() < 20000) {
            return "Town";
        } else if (town.getPopulation() < 100000) {
            return "Large town";
        } else if (town.getPopulation() < 300000) {
            return "City";
        } else if (town.getPopulation() < 1000000) {
            return "Large city";
        } else if (town.getPopulation() < 3000000) {
            return "Metropolis";
        } else if (town.getPopulation() < 10000000) {
            return "Conurbation";
        } else if (town.getPopulation() < Integer.MAX_VALUE) {
            return "Megalopolis";
        } else {
            return "Ecumenopolis";
        }
    }
}
