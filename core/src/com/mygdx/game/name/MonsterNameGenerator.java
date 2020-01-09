package com.mygdx.game.name;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.TextFileContent;

import java.util.HashMap;
import java.util.Map;

public class MonsterNameGenerator {

    public static String generateMonsterName(int level, final GameAssetManager gameAssetManager) {
        final TextFileContent namesFile = gameAssetManager.getTextFileContents("monster");

        Map<TextFileContent.FileKeyType, String> filter = new HashMap<>();
        filter.put(TextFileContent.FileKeyType.MINLEVEL, String.valueOf(level));
        filter.put(TextFileContent.FileKeyType.MAXLEVEL, String.valueOf(level));

        return NameGenerator.generateNameFromFileByFilters(filter, namesFile);
    }

    public static String generateElderName(final String alignment, final GameAssetManager gameAssetManager) {
        final TextFileContent namesFile = gameAssetManager.getTextFileContents("elders");

        Map<TextFileContent.FileKeyType, String> filter = new HashMap<>();
        filter.put(TextFileContent.FileKeyType.ALIGNMENT, alignment);

        return NameGenerator.generateNameFromFileByFilters(filter, namesFile);
    }
}
