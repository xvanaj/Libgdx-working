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

        int countOfSyllablesInFirstName = Randomizer.range(minFirstNameLength,maxFirstNameLength);

        for (int i = 0; i < countOfSyllablesInFirstName; i++) {
            firstName += Randomizer.get(nameTheme.getFirstNameSyllables());
        }
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
        int countOfSyllablesInLastName = Randomizer.range(minSurnameLength,maxSurnameLength);
        for (int i = 0; i < countOfSyllablesInLastName; i++) {
            lastName += Randomizer.get(nameTheme.getLasNameSyllables());
        }

        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);

        if (Randomizer.range(1,100) < 50) {
            lastName += Randomizer.get(nameTheme.getNameSuffixes());
        }

        return firstName + " " + lastName;
    }

    public static void main(String[] args) {
        for (NameTheme nameTheme : NameTheme.values()) {
            System.out.println(nameTheme + "<<<<<<<<<<<<<<<<<<<<<<<<<");
            for (int i = 0; i < 10; i++) {
                System.out.println(generateNPCName(nameTheme, 1, 2, 1, 2));
            }
        }
    }

}
