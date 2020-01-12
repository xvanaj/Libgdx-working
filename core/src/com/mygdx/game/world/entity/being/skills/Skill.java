package com.mygdx.game.world.entity.being.skills;

import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.being.Being;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Skill {

    private String name;
    private String description;
    private int cost;
    private SkillType skillType = SkillType.PASSIVE;

    public Skill(final String name) {
        this.name = name;
    }

    public void onSuccesfullAttack(BattleContext battleContext, Being attacker, Being target, final int finalDamageDealt) {}
    public void onBeingDamaged(BattleContext battleContext, Being attacker, Being target, int damage) {}
    public void onStartOfTimePeriod(BattleContext battleContext, Being being) {}

    public enum SkillType {
        PASSIVE,
    }

    @Override
    public String toString() {
        return name;
    }
}

