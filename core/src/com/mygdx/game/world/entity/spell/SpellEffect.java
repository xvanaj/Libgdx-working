package com.mygdx.game.world.entity.spell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jakub Vana on 14.9.2018.
 */
@Getter
@Setter
@AllArgsConstructor
public class SpellEffect {

    private Type effectType;
    private int min;
    private int max;

    public enum Type {
        DAMAGE,
        HEAL,
        RESSURECT,
        BUFF,
    }
}
