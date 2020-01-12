package com.mygdx.game.world.entity.being.skills;

import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.utils.Randomizer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VampirismSkill extends Skill {

    private int chance;
    private float percentHeal;

    public VampirismSkill(final int chance, final float percentHeal) {
        super("Vampirism");
        this.chance = chance;
        this.percentHeal = percentHeal;
    }

    @Override
    public void onSuccesfullAttack(final BattleContext battleContext, final Being originator, final Being target, int damageDealt) {
        if (Randomizer.range(0,100) < chance) {
            damageDealt = (int) (damageDealt * percentHeal);

            int hpHealed = originator.heal(damageDealt);

            battleContext.logMessageSkill(originator.getName() + " healed for " + hpHealed + " damage with vampirism");
        }
    }
}
