package com.mygdx.game.world.entity.being.skills;

import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.utils.Randomizer;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CleaveSkill extends Skill {

    private int chance;
    private float percentDamage;

    public CleaveSkill(final int chance, final float percentDamage) {
        super("Cleave");
        this.chance = chance;
        this.percentDamage = percentDamage;
    }

    @Override
    public void onSuccesfullAttack(final BattleContext battleContext, final Being originator, final Being target, int damageDealt) {
        if (Randomizer.range(0,100) < chance) {

            List<Being> nearbyTargets;
            if (battleContext.getHeroes().contains(originator)) {
                nearbyTargets = battleContext.getMonsters();
            } else if (battleContext.getMonsters().contains(originator)) {
                nearbyTargets = battleContext.getHeroes();
            } else {
                throw new UnsupportedOperationException();
            }

            nearbyTargets = nearbyTargets.stream().filter(being -> being.getDistance() == target.getDistance()).collect(Collectors.toList());

            //Cleave damage to others
            damageDealt = (int) (damageDealt * percentDamage);

            final int finalDamageDealt = damageDealt;

            nearbyTargets.stream().forEach(being -> being.setHp(being.getHp() - finalDamageDealt));
            battleContext.logMessageSkill( originator.getName() + " cleave for " + finalDamageDealt + " damage");
        }
    }
}
