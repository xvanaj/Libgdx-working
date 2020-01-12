package com.mygdx.game.world.entity.spell;

import com.mygdx.game.world.enums.spell.SpellTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Spell {

    private SpellTemplate spellTemplate;
    private String name;
    private String iconName;

}
