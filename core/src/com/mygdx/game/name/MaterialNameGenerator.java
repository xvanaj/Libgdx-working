package com.mygdx.game.name;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.TextFileContent;
import com.mygdx.game.utils.Randomizer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaterialNameGenerator {

    public static List<String> generateRandomMaterialNames() {
        List<String> possibleNames = new ArrayList<>();

        List<String> availablePrefixes = Arrays.asList("Stron", "Mag", "Kad", "Lit", "Met");
        List<String> availablePremid = Arrays.asList("a", "e", "i", "o", "u");
        List<String> availableMid = Arrays.asList("t", "n", "an", "in", "en");
        List<String> availableSuffixes = Arrays.asList("ium", "ien", "tien", "dum", "tun", "");
        for (int i = 0; i < 10; i++) {
            String s = "" + Randomizer.get(availablePrefixes) + Randomizer.get(availablePremid) + Randomizer.get(availableMid) + Randomizer.get(availableSuffixes);
            possibleNames.add(s);
        }

        return possibleNames;
    }

    public static Set<String> generateRandomMaterialNames3(String namesTheme, int count) {
        final TextFileContent namesFile = GameAssetManager.getTextFileContentsStatic("resource");

        Map<TextFileContent.FileKeyType, String> filter = new HashMap<>();
        /*filter.put(TextFileContent.FileKeyType.TYPE, namesTheme);*/

        return NameGenerator.generateNameFromFileByFilters(filter, namesFile, count);
    }

    public static Set<String> generateRandomMaterialNames2(Set<NameTheme> nameThemes) {
        Set<String> possibleNames = new HashSet<>();

        List<String> availableWordPrefixes = new ArrayList<>();
        List<String> availableRootWords = new ArrayList<>();
        List<String> availablePremid = new ArrayList<>();
        List<String> availableMid = new ArrayList<>();
        List<String> availableSuffixes = new ArrayList<>();
/*
        if (nameThemes.contains(NameTheme.FUTURE)) {
            availableWordPrefixes.addAll(Arrays.asList("Destabilized", "Morphic", "Nonconductive", "Radioactive", "Non-refractive"));
        }*/
        if (nameThemes.contains(NameTheme.FANTASY)) {
            availableWordPrefixes.addAll(Arrays.asList("Crystalized", "Liquid", "Opaque", "Frosty", "Requiem", "Feigned", "Star", "Moon", "Draconian", "Mind"));
        }
        if (nameThemes.contains(NameTheme.DARK)) {
            availableWordPrefixes.addAll(Arrays.asList("Dark", "Deadly", "Decaying", "crepuscular", "twilit","moonless", "starless", "sunless"
                    ,"cloudy", "dull", "dulled", "lackluster","shadowlike", "shadowy", "shady"
                    ,"gray", "leaden", "pale","beclouded", "befogged", "clouded", "foggy", "fuliginous", "misty", "smoggy", "soupy"));
        }
    /*    if (nameThemes.contains(NameTheme.SCIENTIFIC)) {
            availableRootWords = Arrays.asList("Stron", "Mag", "Kad", "Lit", "Met", "Sili", "Fib");
            availablePremid = Arrays.asList("a", "e", "i", "o", "u");
            availableMid = Arrays.asList("t", "n", "an", "in", "en");
            availableSuffixes = Arrays.asList("ium", "ien", "tien", "dum", "tun", "rum", "site");
        } else {
            availableRootWords = Arrays.asList("Stone", "Steel", "Tin", "Gold", "Bark", "Silver", "Copper", "Suede", "Wood", "Darkwood");
        }*/

        for (int i = 0; i < 10; i++) {
            int random = Randomizer.generate(0, 100);

            String s = ((!availableWordPrefixes.isEmpty()) ? Randomizer.get(availableWordPrefixes) + " " : "")
                    + Randomizer.get(availableRootWords)
                    + (availablePremid.isEmpty() ? "" : Randomizer.get(availablePremid))
                    + (availableMid.isEmpty() ? "" : Randomizer.get(availableMid))
                    + (availableSuffixes.isEmpty() ? "" : Randomizer.get(availableSuffixes));
            possibleNames.add(s);
        }

        return possibleNames;
    }

    public static void main(String[] args) {
        generateRandomMaterialNames2(Stream.of(NameTheme.FANTASY).collect(Collectors.toSet())).stream().forEach(item -> System.out.println(item));
    }
}
