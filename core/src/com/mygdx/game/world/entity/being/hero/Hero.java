package com.mygdx.game.world.entity.being.hero;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.world.entity.being.*;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.enums.character.Gender;
import com.mygdx.game.world.generator.being.HeroClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hero extends Being {

    private String title, tilte2;
    private Map<AttributeSecondary, Attribute> secondaryAttributes;
    {
        secondaryAttributes = new HashMap<>();
        for (AttributeSecondary att : AttributeSecondary.values()) {
            secondaryAttributes.put(att, new Attribute(0, 0));
        }
    }
    private int age, height, weight;
    private float biologicalAge;
    private HairType hairType;
    private HairSize hairSize;
    private Color hairColor;
    private Map<AppearanceFeature, String> appearanceFeatures;
    private Gender gender = Gender.FEMALE;
    private Set<CharacterTrait> characterTraits;
    private Set<LikesDislikes> likes = new HashSet<>();
    private Set<LikesDislikes> dislikes = new HashSet<>();
    private HashMap<CharacterState, Integer> characterStates = new HashMap<>();
    private HeroAction heroAction;
    private float fatigue, hunger = 0;
    private float sanity = 100;
    private float joy, heroism = 50;

    private HeroClass heroClass;
    private String portraitName;
    private RaceType raceType;
    private boolean autopilot = true;
    private List<BiographyEvent> history;
    private List<String> description;

    //placement
    private String townCode, worldCode, civilizationCode;
    private transient Civilization civilization;


}
