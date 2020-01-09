package com.mygdx.game.name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.TextFileContent;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.enums.character.Gender;

import java.io.*;
import java.util.*;

public class NameGenerator {

    public static String getResourceName(final String type) {
        final TextFileContent namesFile = GameAssetManager.getTextFileContentsStatic("resource");

        Map<TextFileContent.FileKeyType, String> filter = new HashMap<>();

        return generateNameFromFileByFilters(filter, namesFile);
    }

    public static String generateHeroName(RaceType race, Gender gender) {
        return NPCNameGenerator.generateNPCName(race.getNameTheme(), race.getFirstNameMinLength(),
                race.getFirstNameMaxLength(), race.getLastNameMinLength(), race.getLastNameMaxLength());
    }

    public static String generateCurrencyName() {
        final TextFileContent namesFile = Utility.assetManager.getTextFileContents("currency");

        return generateNameFromFileByFilters(null, namesFile);
    }

    public static String generateWorldName(final String type) {
        final TextFileContent namesFile = GameAssetManager.getTextFileContentsStatic("world");

        final Map<TextFileContent.FileKeyType, String> fileKeyTypeStringMap = new HashMap<>();
        fileKeyTypeStringMap.put(TextFileContent.FileKeyType.TYPE, type);

        return generateNameFromFileByFilters(fileKeyTypeStringMap, namesFile);
    }


    public static String generateNameFromFileByFilters(Map<TextFileContent.FileKeyType, String> filter, TextFileContent namesFile) {
        return generateNameFromFileByFilters(filter, namesFile, 1).iterator().next();
    }

    public static Set<String> generateNameFromFileByFilters(Map<TextFileContent.FileKeyType, String> filter, TextFileContent namesFile, int count) {
        List<String> possibleNames = new ArrayList<>();

        nextLine:
        for (TextFileContent.TextFileLine line : namesFile.getLines()) {
            if (filter != null && !filter.isEmpty()) {
                for (TextFileContent.FileKeyType keyType : filter.keySet()) {
                    if (line.getValues().get(keyType) == null) {
                        //log?
                    } else {
                        switch (keyType.getFilterType()) {
                            case EQUAL:
                                if (line.getValues().get(keyType).equals(filter.get(keyType).toLowerCase())) {
                                    continue nextLine;
                                }
                                break;
                            case LET:
                                if (!line.getValues().get(keyType).isEmpty() && line.getValues().get(keyType).compareTo(filter.get(keyType).toLowerCase()) > 0) {
                                    continue nextLine;
                                }
                                break;
                            case GET:
                                if (!line.getValues().get(keyType).isEmpty() && line.getValues().get(keyType).compareTo(filter.get(keyType).toLowerCase()) < 0) {
                                    continue nextLine;
                                }
                                break;
                        }
                    }
                }
            }
            possibleNames.add(line.getValues().get(TextFileContent.FileKeyType.NAME));
        }

        Set<String> generatedNames = new HashSet<>();
        int attempts = 0;
        while (generatedNames.size() <= count && attempts < 100) {
            attempts++;
            generatedNames.add(Randomizer.get(possibleNames));
        }
        return generatedNames;
    }

    public static String getRandomNameFromFileByType(final String filename, final String type) {
        List<String> possibleNames = new ArrayList<>();

        FileHandle internal = Gdx.files.classpath("core/assets/names/" + filename + ".txt");
        try {
            InputStream in = new FileInputStream(internal.file());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 1 || parts[1].equals("all") || parts[1].toLowerCase().equals(type.toLowerCase())) {
                    possibleNames.add(parts[0]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return possibleNames.get(new Random().nextInt(possibleNames.size()));
    }
}
