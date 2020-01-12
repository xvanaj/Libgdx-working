package com.mygdx.game.world.entity.being.params;

import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.enums.being.BeingType;
import com.mygdx.game.world.generator.being.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeingCreationParameters {

    private int minLevel;
    private int maxLevel;
    private BeingType beingType;
    private RaceType raceType;
    private HeroClass heroClass;
    private boolean hasSpells;

    private MonsterGenerationParameters monsterGenerationParameters;
    private AncientMonsterGenerationParameters ancientMonsterGenerationParameters;
    private ElderBeingCreationParameters elderBeingCreationParameters;

    private CharacteristicsGenerationParams characteristicsGenerationParams;
    private EquipmentGenerationParams equipmentGenerationParams = new EquipmentGenerationParams();
    private AttributesGenerationParameters attributesGenerationParameters = new AttributesGenerationParameters();
    private SpellGenerationParameters spellGenerationParameters;


}
