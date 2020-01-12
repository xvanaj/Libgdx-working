package com.mygdx.game.world.enums.character;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Race {

    DEMON(20, 80, 100, Alignment.EVIL, new HashMap() {{
        put(FeatureType.ATTACK, 10);
    }}),
    DRACONIC(20, 80, 100, Alignment.EVIL, new HashMap() {{
        put(FeatureType.ATTACK, 10);
    }}),
    DWARF(20, 80, 100, Alignment.GOOD, new HashMap() {{
        put(FeatureType.CON, 5);
        put(FeatureType.BUILDER, 20);
    }}),
    ELF(20, 80, 100, Alignment.GOOD, new HashMap<>()),
    GNOME(20, 80, 100, Alignment.GOOD, new HashMap<>()),
    GOBLIN(20, 80, 100, Alignment.EVIL, new HashMap() {{
        put(FeatureType.CON, -5);
        put(FeatureType.POPULATION, 20);
    }}),
    HALFLING(20, 80, 100, Alignment.GOOD, new HashMap<>()),
    HUMAN(20, 80, 100, Alignment.GOOD, new HashMap<>()),
    ORC(20, 80, 100, Alignment.EVIL, new HashMap<>()),
    TROLL(20, 80, 100, Alignment.EVIL, new HashMap<>()),
    VAMPIRE(20, 80, 100, Alignment.EVIL, new HashMap<>()),
    ;

    Race(int minAge, int oldAge, int maxAge, Alignment alignment, Map<FeatureType, Integer> features) {
        this.startingAge = minAge;
        this.oldAge = oldAge;
        this.maxAge = maxAge;
        this.alignment = alignment;
        this.features = features;
        for (FeatureType featureType : features.keySet()) {
            switch (featureType) {
                case STR:
                    bonusStr = features.get(featureType);
                    break;
                case AGI:
                    bonusAgi = features.get(featureType);
                    break;
                case INT:
                    bonusInt = features.get(featureType);
                    break;
                case CON:
                    bonusCon = features.get(featureType);
                    break;
                case ATTACK:
                    break;
                case DEFENSE:
                    break;
                case ARMOR:
                    break;
                case CRITICAL:
                    break;
                case EVASION:
                    break;
                case HPMAX:
                    bonusHPMax = features.get(featureType);
                    break;
                case HPMIN:
                    break;
                case POPULATION:
                    populationGrowthBonus = features.get(featureType);
                    break;
                case BUILDER:
                    buildingSpeedBonus = features.get(featureType);
                    break;
            }
        }
    }

    private Map<FeatureType, Integer> features;

    private int bonusStr;
    private int bonusAgi;
    private int bonusInt;
    private int bonusCon;
    private int bonusAtt;
    private int bonusDefense;
    private int bonusArmor;
    private int bonusHPMax;
    private int bonusMPMax;
    private int bonusEvasion;
    private int bonusCritical;
    
    private int startingAge;
    private int oldAge;
    private int maxAge;
    private Alignment alignment;

    private int populationGrowthBonus;
    private int buildingSpeedBonus;
    private int farmingBonus;

    private enum FeatureType {
        STR,
        AGI,
        INT,
        CON,

        ATTACK,
        DEFENSE,
        ARMOR,
        CRITICAL,
        EVASION,
        HPMAX,
        HPMIN,

        POPULATION,
        BUILDER
    }
}
