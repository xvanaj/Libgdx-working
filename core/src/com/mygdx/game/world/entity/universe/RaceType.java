package com.mygdx.game.world.entity.universe;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.name.NameTheme;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.enums.being.Size;
import com.mygdx.game.world.enums.character.Alignment;
import com.mygdx.game.world.enums.world.TerrainType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class RaceType {

    /*private int bonusStr;
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

    private int buildingSpeedBonus;
    private int farmingBonus;
*/
    private String name;
    private int startingAge;
    private int oldAge;
    private int maxAge;
    private Alignment alignment;

    private ReligionType religionType;
    private Set<TerrainType> favouriteTerrains;
    private Set<Color> possibleHairColors;
    private List<RaceTraits> traits;
    private NameTheme nameTheme;
    private int firstNameMinLength, firstNameMaxLength, lastNameMinLength, lastNameMaxLength, suffixChance;
    private Set<String> sampleNames;
    //private preferred spell type

    //appearance
    private ElderBeing.SkinColor skinColor;
    private ElderBeing.HeadType headType;
    private ElderBeing.SpeciesType speciesType;
    private Size size;

    private int coldResistance;
    private int heatResistance;
    private float idealGravity;
    private int fertilityRate;  //average years for new child

}
