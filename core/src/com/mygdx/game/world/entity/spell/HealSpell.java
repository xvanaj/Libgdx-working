package com.mygdx.game.world.entity.spell;

import com.mygdx.game.world.enums.spell.SpellTemplate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealSpell extends Spell {

    private int minHeal, maxHeal;

    public HealSpell(final SpellTemplate spellTemplate, final String name, final String iconName) {
        super(spellTemplate, name, iconName);
    }
}
