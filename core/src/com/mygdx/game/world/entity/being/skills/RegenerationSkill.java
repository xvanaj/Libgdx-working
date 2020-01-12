package com.mygdx.game.world.entity.being.skills;

import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.being.Being;

public class RegenerationSkill extends Skill {

    private int amountRegenerated;
    private REGEN_TYPE regenType;

    public RegenerationSkill(final int amountRegenerated, final REGEN_TYPE regenType) {
        super("Regeneration " + regenType);
        this.amountRegenerated = amountRegenerated;
        this.regenType = regenType;
    }

    @Override
    public void onStartOfTimePeriod(final BattleContext battleContext, final Being being) {
        switch (regenType) {
            case HP:
                if (being.getHp() <= 0) {
                    throw new UnsupportedOperationException();
                } else if (being.getHp() + amountRegenerated > being.getHpMax()) {
                    battleContext.logMessageSkill(being.getName() + " regenerated " + (being.getHpMax() - being.getHp()) + " hit points");
                    being.setHp(being.getHpMax());

                } else {
                    battleContext.logMessageSkill(being.getName() + " regenerated " + amountRegenerated + " hit points");
                    being.setHp(being.getHp() + amountRegenerated);
                }
            case MP:
                if (being.getMp() <= 0) {
                    throw new UnsupportedOperationException();
                } else if (being.getMp() + amountRegenerated > being.getHpMax()) {
                    battleContext.logMessageSkill(battleContext.getTimeElapsed() + ": " + being.getName() + " regenerated " + (being.getMpMax() - being.getMp()) + " mana points");
                    being.setHp(being.getMpMax());

                } else {
                    battleContext.logMessageSkill(being.getName() + " regenerated " + amountRegenerated + " mana points");
                    being.setHp(being.getMp() + amountRegenerated);
                }
        }

    }

    public enum REGEN_TYPE {
        HP,
        MP
    }
}
