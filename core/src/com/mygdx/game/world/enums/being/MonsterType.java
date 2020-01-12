package com.mygdx.game.world.enums.being;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MonsterType {
    AWAKENED_SHRUB("Awakened Shrub", 1, 3),
    BADGER("Badger", 1, 3),
    GIANT_BAT("Giant bat", 1, 3),
    GIANT_CRAB("Giant crab", 1, 3),
    CRANIUM_RAT("Cranium Rat", 1, 3),
    CRAWLING_CLAW("Crawling Claw", 1, 3),
    GIANT_FIRE_BEETLE("Giant Fire Beetle", 1, 3),
    GIANT_FLY("Giant Fly", 1, 3),
    HOMUNCULUS("Homunculus", 1, 3),
    HYENA("Hyena", 1, 3),
    JACKAL("Jackal", 1, 3),
    LEMURE("Lemure", 1, 3),
    LIZARD("Lizard", 1, 3),
    MYCONID_SPROUT("Myconid Sprout", 1, 3),
    GIANT_RAT("Giant rat", 1, 3),
    SCORPION("Scorpion", 1, 3),
    SHRIEKER("Shrieker", 1, 3),
    GIANT_SPIDER("Giant spider", 1, 3),
    TRESSYM("Tressym", 1, 5),
    HOBGOBLIN("Hobgoblin", 5, 10),
    ORC("Orc", 10, 20),
    VAMPIRE("Vampire", 20, 30),
    LICH("Lich", 30, 40),
    DEMILICH("Demilich", 40, 50),
    RED_DRAGON("Red dragon", 50, 60),
    TARRASQUE("Tarrasque", 60, 70),
    BLACK_DRAGON("Black dragon", 70, 80),
    DEMIGOD("Demigod", 80, 90),
    GOD("Godlike being", 90, 100),
    HUMAN("Human", 1, 100);

    private String name;
    private int levelMin;
    private int levelMax;
}
