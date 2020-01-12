package com.mygdx.game.world.entity.being.skills;

import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.entity.being.effects.StunEffect;
import com.mygdx.game.utils.Randomizer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StunSkill extends Skill {

    private int stunChance;
    private int stunDuration;

    public StunSkill(final int stunChance, final int stunDuration) {
        super("Stun");
        this.stunChance = stunChance;
        this.stunDuration = stunDuration;
    }

    @Override
    public void onSuccesfullAttack(final BattleContext battleContext, final Being originator, final Being target, final int finalDamageDealt) {
        if (Randomizer.range(0,100) < stunChance) {
            //Stun performed
            battleContext.logMessageSkill(originator.getName() + " stunned " + target.getName() + " on " + stunDuration  + " millis");
            target.getEffects().add(new StunEffect(stunDuration));
        }
    }
}
