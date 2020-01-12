package com.mygdx.game.world.builder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.name.NPCNameGenerator;
import com.mygdx.game.world.entity.being.*;
import com.mygdx.game.world.entity.being.hero.CharacterTrait;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.being.hero.LikesDislikes;
import com.mygdx.game.world.entity.being.params.AttributesGenerationParameters;
import com.mygdx.game.world.entity.being.params.HeroCreationParameters;
import com.mygdx.game.world.entity.being.params.StartingGear;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.enums.being.Size;
import com.mygdx.game.world.enums.character.Gender;
import com.mygdx.game.name.NameGenerator;
import com.mygdx.game.name.TownNameGenerator;
import com.mygdx.game.utils.Randomizer;
import com.mygdx.game.world.generator.RaceTypeFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class HeroBuilder {

    public static final String TAG = HeroBuilder.class.getName();
    private Hero hero;

    public HeroBuilder() {
        this.hero = new Hero();
    }

    public HeroBuilder generateBiography(HeroCreationParameters parameters) {
        if (hero.getWorldCode() != null || hero.getCivilization() != null) {
            if (hero.getHistory() == null) {
                hero.setHistory(new ArrayList<>());
            }

            final List<BiographyEvent> events = hero.getHistory();
            events.add(new BiographyEvent(0,
                    hero.getWorldCode() != null
                            ? hero.getWorldCode()
                            : hero.getCivilization().getWorld().getCode(),
                    hero.getTitle() + ", " + hero.getName() + " was born to "
                            + NPCNameGenerator.generateNPCName(hero.getRaceType())
                            + " and " + NPCNameGenerator.generateNPCName(hero.getRaceType()) + " in the "
                            + TownNameGenerator.getTownPrefix(hero.getCivilization().getCapitalTown()) + hero.getCivilization().getCapitalTown().getName()));

            hero.getLikes().stream().forEach(like -> createBiographyEvent(events, like, hero));
            hero.getDislikes().stream().forEach(like -> createNegativeBiographyEvent(events, like, hero));
        }
        return this;
    }

    private boolean createBiographyEvent(final List<BiographyEvent> events, final LikesDislikes like, final Hero hero) {
        final OptionalInt max = events.stream().flatMapToInt(item -> IntStream.of(item.getMinute())).max();
        return events.add(new BiographyEvent(max.getAsInt() + 60 * 24 * 365, hero.getWorldCode(), "Learned to like " + like.getDescription()));
    }

    private boolean createNegativeBiographyEvent(final List<BiographyEvent> events, final LikesDislikes like, final Hero hero) {
        final OptionalInt max = events.stream().flatMapToInt(item -> IntStream.of(item.getMinute())).max();
        return events.add(new BiographyEvent(max.getAsInt() + 60 * 24 * 365, hero.getWorldCode(), "Learned to hate " + like.getDescription()));
    }

    public HeroBuilder generateName(HeroCreationParameters parameters) {
        hero.setName(NameGenerator.generateHeroName(hero.getRaceType(), hero.getGender()));

        return this;
    }


    public HeroBuilder generateRace(HeroCreationParameters parameters) {
        if (parameters.getCivilization() != null) {
            hero.setRaceType(parameters.getCivilization().getRacetype());
        } else {
            hero.setRaceType(RaceTypeFactory.createRandomRaceType(null));
        }

        return this;
    }

    public HeroBuilder generateCharacteristics(HeroCreationParameters parameters) {
        final RaceType raceType = hero.getRaceType();

        if (raceType == null) {
            throw new IllegalArgumentException("Hero must have race type");
        }
        hero.setHairColor(Randomizer.get(raceType.getPossibleHairColors()));
        hero.setHairSize(Randomizer.get(HairSize.values()));
        hero.setHairType(Randomizer.get(HairType.values()));

        hero.setAge(raceType.getStartingAge());
        hero.setBiologicalAge(raceType.getStartingAge());
        hero.setAlignment(raceType.getAlignment());
        hero.setGender(Randomizer.get(Gender.values()));
        hero.setHeroClass(parameters.getHeroClass());
        hero.setCharacterTraits(new HashSet<>());
        for (int i = 0; i < Randomizer.range(5, 10); i++) {
            hero.getCharacterTraits().add(Randomizer.get(CharacterTrait.values()));
        }
        for (int i = 0; i < Randomizer.range(4, 6); i++) {
            hero.getLikes().add(Randomizer.get(LikesDislikes.values()));
        }
        for (int i = 0; i < Randomizer.range(4, 6); i++) {
            final LikesDislikes dislike = Randomizer.get(LikesDislikes.values());
            if (!hero.getLikes().contains(dislike)) {
                hero.getDislikes().add(dislike);
            } else {
                i--;
            }
        }

        final List<Texture> allTextures = Utility.assetManager.getAllPortraits();
        hero.setPortraitName(((FileTextureData) Randomizer.get(allTextures).getTextureData()).getFileHandle().file().getName());

        return this;
    }

    public Hero build() {
        return hero;
    }

    public HeroBuilder generateAttributes(final AttributesGenerationParameters attributesGenerationParameters) {
        new AttributesGenerator().generateRandomPrimaryAttributes(hero, attributesGenerationParameters);

        return this;
    }

    public HeroBuilder generateProficiencies(final HeroCreationParameters parameters) {
        hero.setMeleeProficiency(15);
        hero.setSpellProficiency(10);
        hero.setRangedProficiency(5);

        return this;
    }

    public HeroBuilder generateItems(final HeroCreationParameters parameters) {
        if (parameters.getStartingGear().equals(StartingGear.MINIMAL)) {
            //hero.getEquipment().
        } else if (parameters.getStartingGear().equals(StartingGear.MINIMAL)) {

        }

        return this;
    }

    public HeroBuilder generateDescription(final HeroCreationParameters parameters) {
        hero.setDescription(new ArrayList<>());

        RaceType raceType = hero.getRaceType();
        Size size = raceType.getSize();
        final String heShe = hero.getGender().equals(Gender.FEMALE) ? "she" : "he";
        final String hisHer = hero.getGender().equals(Gender.FEMALE) ? "her" : "his";
        final String has = Randomizer.get("has");

        hero.getDescription().add(heShe + " looks " + getAgeDescription(hero.getAge(), raceType));
        if (hero.getHairType().equals(HairType.NONE)) {

        } else {
            hero.getDescription().add(hisHer + " hair is " + hero.getHairSize() + " " + hero.getHairColor() + " " + hero.getHairType()
                    + " growing from the head " + getHeadModificationDescription(raceType));
        }
        hero.getDescription().add(heShe + " is of a " + size + " size");

        if (hero.getAppearanceFeatures() != null) {
            for (AppearanceFeature feature : hero.getAppearanceFeatures().keySet()) {
                hero.getDescription().add(heShe + " " + has + " " + hero.getAppearanceFeatures().get(feature));
            }
        }

        return this;
    }

    private String getHeadModificationDescription(final RaceType raceType) {
        if (!raceType.getHeadType().equals(ElderBeing.HeadType.NONE) && !raceType.getHeadType().equals(ElderBeing.HeadType.COMMON)) {
            return "with " + raceType.getHeadType().getModificationName();
        }
        return "";
    }

    private String getAgeDescription(final int age, final RaceType raceType) {
        float ageFactor = age / raceType.getMaxAge();
        if (ageFactor < 0.15f) {
            return Randomizer.get("like kiddo", "like whippersnapper", "like a toddler");
        } else if (ageFactor < 0.3f) {
            return Randomizer.get("juvenile", "like youngster");
        } else if (ageFactor < 0.5f) {
            return Randomizer.get("in a best age");
        } else if (ageFactor < 0.85f) {
            return Randomizer.get("weared by tooth of time", "very advanced in years", "like golden-ager", "old-timer");
        } else {
            return Randomizer.get("like death should take him away at any time", "retired person", "retiree");
        }
    }

    public HeroBuilder setCivilization(final HeroCreationParameters parameters) {
        if (parameters.getCivilization() != null) {
            hero.setCivilization(parameters.getCivilization());
            hero.setCivilizationCode(parameters.getCivilization().getCode());
        }

        if (parameters.getWorld() != null && parameters.getWorld().getCode() != null) {
            hero.setWorldCode(parameters.getWorld().getCode());
        } else {
            Gdx.app.log(HeroBuilder.class.getCanonicalName(), "Hero " + hero + " does not have world");
        }

        return this;
    }

    public HeroBuilder setTown(final HeroCreationParameters parameters) {
        if (parameters.getTown() != null) {
            final Town town = parameters.getTown();
            hero.setTownCode(town.getCode());
            hero.setPosition(town.getX(), town.getY());
        } else if (parameters.getCivilization() != null) {
            final Town capitalTown = parameters.getCivilization().getCapitalTown();
            hero.setTownCode(capitalTown.getCode());
            hero.setPosition(capitalTown.getX(), capitalTown.getY());
        } else {
            Gdx.app.log(TAG, "Hero " + hero + " does not have town. Should happen only in tests");
        }

        return this;
    }
}
