package com.mygdx.game.world.entity.universe;

import com.mygdx.game.world.enums.spell.SpellTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class SpellResearch {

    private SpellTemplate spellTemplate;
    private Set<String> requiredMaterials;
    private int hoursToResearch, hoursResearched;
    private int difficulty;         //spellcraft needed

}
