package com.mygdx.game.world.generator.being;

import com.mygdx.game.world.builder.HeroBuilder;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.being.params.HeroCreationParameters;

import java.util.ArrayList;
import java.util.List;

public class HeroFactory {

    public static List<Hero> createRandomHeroes(int count, HeroCreationParameters params){
        List<Hero> heroes = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            heroes.add(createRandomHero(params));
        }

        return heroes;
    }

    public static Hero createHero(final HeroCreationParameters heroCreationParameters) {
        final Hero hero = HeroFactory.createRandomHero(heroCreationParameters);

        return hero;
    }

    public static Hero createRandomHero(final HeroCreationParameters parameters) {
        final Hero hero = new HeroBuilder()
                .generateRace(parameters)
                .generateCharacteristics(parameters)
                .generateAttributes(parameters.getAttributesGenerationParameters())
                .generateProficiencies(parameters)
                .generateItems(parameters)
                .generateName(parameters)
                .setCivilization(parameters)
                .setTown(parameters)
                .generateBiography(parameters)
                .generateDescription(parameters)
                .build();

        hero.rejuvenate();
        hero.calculateStats();

        return hero;
    }

    public static Hero createRandomHero() {
        return createRandomHero(new HeroCreationParameters());
    }
}
