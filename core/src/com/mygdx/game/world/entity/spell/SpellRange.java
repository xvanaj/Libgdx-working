package com.mygdx.game.world.entity.spell;

import com.mygdx.game.world.enums.spell.SpellTarget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpellRange {

    private int range = 1;
    private int aoe = 0;
    private boolean friendlyFire = false;
    private SpellTarget target = SpellTarget.ENEMY;


    public SpellRange(int range) {
        this.range = range;
    }

    public SpellRange(int range, int aoe) {
        this.range = range;
        this.aoe = aoe;
    }

    public SpellRange(int range, int aoe, boolean friendlyFire) {
        this.range = range;
        this.aoe = aoe;
        this.friendlyFire = friendlyFire;
    }

    public SpellRange(int range, int aoe, SpellTarget target) {
        this.range = range;
        this.aoe = aoe;
        this.target = target;
    }

}
