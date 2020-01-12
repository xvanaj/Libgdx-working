package com.mygdx.game.world.entity.being;

import com.mygdx.game.controller.BattleContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Effect {

    protected long duration;
    protected boolean immobilized;
    protected boolean cannotCast;
    protected boolean cannotShoot;
    protected boolean stunned;

    public Effect(final long duration) {
        this.duration = duration;
    }

    public void onAttack(BattleContext battleContext, Being originator, Being target) {}
    public void onDefense(BattleContext battleContext, Being originator, Being target) {}
    public void onStartOfTimePeriod(BattleContext battleContext, Being originator) {}

}
