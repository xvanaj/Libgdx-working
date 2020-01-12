package com.mygdx.game.name;

import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.entity.universe.RaceType;

import java.util.HashSet;
import java.util.Set;

public class NPCNameGenerator {

    public static Set<String> generateRaceTypeNames(NameTheme nameTheme, int minFirstNameLength, int maxFirstNameLength, int minSurnameLength, int maxSurnameLength, int count) {
        Set<String> names = new HashSet<>();
        for (int i = 0; i < count; i++) {
            names.add(generateNPCName(nameTheme, minFirstNameLength, maxFirstNameLength, minSurnameLength, maxSurnameLength));
        }
        return names;
    }

    public static String generateNPCName(RaceType raceType) {
        return generateNPCName(raceType.getNameTheme(), raceType.getFirstNameMinLength(), raceType.getFirstNameMaxLength(), raceType.getLastNameMinLength(), raceType.getLastNameMaxLength());
    }

    public static String generateNPCName(NameTheme nameTheme, int minFirstNameLength, int maxFirstNameLength, int minSurnameLength, int maxSurnameLength) {
        String firstName = "";
        String lastName = "";

        int countOfSyllablesInFirstName = Randomizer.range(minFirstNameLength, maxFirstNameLength);

        for (int i = 0; i < countOfSyllablesInFirstName; i++) {
            firstName += Randomizer.get(nameTheme.getFirstNameSyllables());
        }
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        int countOfSyllablesInLastName = Randomizer.range(minSurnameLength, maxSurnameLength);
        for (int i = 0; i < countOfSyllablesInLastName; i++) {
            lastName += Randomizer.get(nameTheme.getLasNameSyllables());
        }

        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

        if (Randomizer.range(1, 100) < 50) {
            lastName += Randomizer.get(nameTheme.getNameSuffixes());
        }

        return firstName + " " + lastName;
    }

    public static void main(String[] args) {
        /*for (NameTheme nameTheme : NameTheme.values()) {
            System.out.println(nameTheme + "<<<<<<<<<<<<<<<<<<<<<<<<<");
            for (int i = 0; i < 10; i++) {
                System.out.println(generateNPCName(nameTheme, 1, 2, 1, 2));
            }
        }*/

        for (NameType nameTheme : NameType.values()) {
            System.out.println(nameTheme + "<<<<<<<<<<<<<<<<<<<<<<<<<");
            for (int i = 0; i < 10; i++) {
                System.out.println(newMethod(nameTheme, null));
            }
            for (SyllableType syllableType : SyllableType.values()) {
                System.out.println(syllableType + "<<<<<<<<<<<<<<<<<<<<<<<<<");
                for (int i = 0; i < 10; i++) {
                    System.out.println(newMethod(nameTheme, syllableType));
                }
            }
        }
    }

    private enum NameType {
        GENERIC
    }

    private enum SyllableType {
        HARSH,
        SMOOTH,
        NEUTRAL,
        VARIOUS,
    }

    public static String newMethod(final NameType nameTheme, final SyllableType syllableType) {
        String name = null;

        switch (nameTheme) {
            case GENERIC:
                int random = Randomizer.generate(1, 20);
                if (between(random, 1, 2)) {
                    name = getRandomNameFromString(NameConstants.oneSyllableNames);
                } else if (between(random, 3, 11)) {
                    name = getRandomNameFromString(NameConstants.twoSyllableNames);
                } else if (between(random, 12, 16)) {
                    name = getRandomNameFromString(NameConstants.threeSyllableNames);
                } else if (between(random, 17, 17)) {
                    name = getRandomNameFromString(NameConstants.multiSyllableNames);
                } else if (between(random, 18, 18)) {
                    name = getRandomNameFromString(NameConstants.oneSyllableNames)
                            + " " + getRandomNameFromString(NameConstants.twoSyllableNames);
                } else if (between(random, 19, 19)) {
                    name = getRandomNameFromString(NameConstants.twoSyllableNames)
                            + " " + getRandomNameFromString(NameConstants.oneSyllableNames);
                } else if (between(random, 20, 20)) {
                    name = getRandomNameFromString(NameConstants.twoSyllableNames);
                }
                break;
        }

        if (syllableType != null) {
            String prefixOrSufix = "";
            switch (syllableType) {
                case HARSH:
                    prefixOrSufix = getRandomNameFromString(NameConstants.harshSyllables);
                    break;
                case SMOOTH:
                    prefixOrSufix = getRandomNameFromString(NameConstants.harshSyllables);
                    break;
                case NEUTRAL:
                    prefixOrSufix = getRandomNameFromString(NameConstants.harshSyllables);
                    break;
                case VARIOUS:
                    prefixOrSufix = getRandomNameFromString(NameConstants.harshSyllables);
                    break;
            }

            String delimiter = Randomizer.generate(0, 5) == 0 ? "-" : " ";
            if (Randomizer.generate(0, 1) == 0) {
                if (Randomizer.generate(0, 1) == 0)
                    name = prefixOrSufix + delimiter + name;
            } else {
                name = name + delimiter + prefixOrSufix;
            }
        }

        return name;
    }

    private static String getRandomNameFromString(final String namesList) {
        return Randomizer.get(namesList
                .replace("  ", " ")
                .replace("\n", "")
                .split(" "));
    }

    private static boolean between(final int random, final int i, final int i1) {
        return random >= i && random <= i1;
    }
}
