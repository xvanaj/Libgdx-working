package com.mygdx.game.world.builder;

import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.being.params.AttributesGenerationParameters;
import com.mygdx.game.utils.Randomizer;

import java.util.function.BiFunction;

public class AttributesGenerator {

    public void generateRandomPrimaryAttributes(final Being being, AttributesGenerationParameters parameters) {
        int min;
        int max;

        if (parameters.isLevelBasedValue()) {
            min = (int) being.getLevel() - parameters.getLevelBasedValueVariance() / 2;
            max = (int) being.getLevel() + parameters.getLevelBasedValueVariance() / 2;
        } else {
            min = parameters.getMinAttributeValue();
            max = parameters.getMaxAttributeValue();
        }

        if (parameters.isGaussianDistribution()) {
            attributes(this::randomAttributeGaussian, min, max, being);
        } else {
            attributes(this::randomAttributeRange, min, max, being);
        }

        //fix according to class minimum
        if (being instanceof Hero && ((Hero)being).getHeroClass() != null) {
            Hero hero = (Hero)being;
            if (hero.getHeroClass().getMinAgi() > being.getAgility()) {
                being.setAgility(hero.getHeroClass().getMinAgi());
            }
            if (hero.getHeroClass().getMinStr() > being.getStrength()) {
                being.setStrength(hero.getHeroClass().getMinStr());
            }
            if (hero.getHeroClass().getMinEnd() > being.getEndurance()) {
                being.setEndurance(hero.getHeroClass().getMinEnd());
            }
            if (hero.getHeroClass().getMinInt() > being.getIntelligence()) {
                being.setIntelligence(hero.getHeroClass().getMinInt());
            }
        }
    }

    protected int randomAttributeRange(int min, int max) {
        return Randomizer.range(min, max);
    }

    protected int randomAttributeGaussian(int min, int max) {
        int randomAttr = (int) Randomizer.gaussian((min + max) / 2, (max - min) / 3);

        if (randomAttr < min) randomAttr = min;
        if (randomAttr > max) randomAttr = max;

        return randomAttr;
    }


    public void attributes(int strenght, int agility, int endurance, int intelligence, Being being) {
        being.setStrength(strenght);
        being.setAgility(agility);
        being.setEndurance(endurance);
        being.setIntelligence(intelligence);
    }

    protected static void attributes(BiFunction<Integer, Integer, Integer> f, int min, int max, Being being) {
        being.setStrength(f.apply(min, max));
        being.setAgility(f.apply(min, max));
        being.setEndurance(f.apply(min, max));
        being.setIntelligence(f.apply(min, max));
    }
}
