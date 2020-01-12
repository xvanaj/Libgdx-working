package com.mygdx.game.world.enums.spell;

import com.badlogic.gdx.graphics.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpellTemplate {

    HEAL_SELF(SpellRarity.COMMON),
    HEAL_OTHER(SpellRarity.COMMON),
    HEAL_ALL(SpellRarity.RARE),
    RESSURECTION(SpellRarity.VERY_RARE),
    REJUVENATE(SpellRarity.ULTRA_RARE),
    RANGED_DAMAGE(SpellRarity.COMMON),
    RANGED_AOE_DAMAGE(SpellRarity.UNCOMMON),

    CREATE_GATE(SpellRarity.VERY_RARE),
    TOWN_PORTAL(SpellRarity.RARE),
    TELEPORT(SpellRarity.VERY_RARE),
    DIMENSIONAL_TELEPORT(SpellRarity.ULTRA_RARE),
    //fertility - increases pop growth
    //alter material - change one into another

    ;

    private SpellRarity spellRarity;


    @Getter
    @AllArgsConstructor
    public enum SpellRarity {
        COMMON(Color.BLACK),
        UNCOMMON(Color.GRAY),
        RARE(Color.BLUE),
        VERY_RARE(Color.GREEN),
        ULTRA_RARE(Color.PURPLE),
        ;

        private Color color;


    }

}
