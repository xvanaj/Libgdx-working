package com.mygdx.game.world.entity.spell;

import com.mygdx.game.world.entity.universe.SpellResearch;
import com.mygdx.game.world.enums.spell.SpellTemplate;
import com.mygdx.game.utils.Randomizer;

import java.util.*;

public class SpellResearchFactory {

    public static final int difficultyRange = 15;
    public static final int countOfExtremes = 4;

    public static List<SpellResearch> generateAllSpellResearches(final List<String> materials, final List<String> spellMaterials) {
        if (materials == null || spellMaterials == null || materials.size() < 5 || spellMaterials.size() < 5) {
            throw new IllegalArgumentException("Missing materials lists or not enough available materials in lists");
        }
        final List<SpellResearch> spellResearches = new ArrayList<>();

        for (SpellTemplate spellTemplate : SpellTemplate.values()) {
            final SpellResearch spellResearch = new SpellResearch();
            final SpellTemplate.SpellRarity spellRarity = spellTemplate.getSpellRarity();
            final int rarityOrdinalIncreased = spellRarity.ordinal() + 1;

            spellResearch.setDifficulty(rarityOrdinalIncreased + Randomizer.range(0, difficultyRange * rarityOrdinalIncreased));
            spellResearch.setHoursResearched(0);
            spellResearch.setHoursToResearch((int) (Math.pow(rarityOrdinalIncreased, 3) * Randomizer.range(12, 24)));
            spellResearch.setRequiredMaterials(new HashSet<>());
            spellResearch.setSpellTemplate(spellTemplate);

            for (int i = 0; i < Randomizer.range(0, rarityOrdinalIncreased); i++) {
                spellResearch.getRequiredMaterials().add(Randomizer.get(materials));
            }
            for (int i = 0; i < Randomizer.range(rarityOrdinalIncreased, rarityOrdinalIncreased*2); i++) {
                spellResearch.getRequiredMaterials().add(Randomizer.get(materials));
            }

            for (int i = 0; i < countOfExtremes; i++) {

            }

            spellResearches.add(spellResearch);
        }

        return spellResearches;
    }

    public static void main(String[] args) {
        final List<String> materials = Arrays.asList("M1", "M2", "M3", "M4", "M5", "M6","M7","M8","M9","M10");
        final List<String> spellMaterials = Arrays.asList("MM1", "MM2", "MM3", "MM4", "MM5", "MM6","MM7","MM8","MM9","MM10");

        final List<SpellResearch> spellResearches = SpellResearchFactory.generateAllSpellResearches(materials, spellMaterials);
        spellResearches.stream().sorted(Comparator.comparing(o -> o.getSpellTemplate().getSpellRarity())).forEach(spellResearch -> System.out.println(spellResearch));

    }
}
