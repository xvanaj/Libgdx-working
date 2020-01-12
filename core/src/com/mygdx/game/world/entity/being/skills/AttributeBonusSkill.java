package com.mygdx.game.world.entity.being.skills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class AttributeBonusSkill extends Skill {

    private int bonusStr;
    private int bonusInt;
    private int bonusAgi;
    private int bonusEnd;
    private int bonusHPMax;
    private int bonusMPMax;

    public AttributeBonusSkill(final int amountBonus, final BonusType bonusType) {
        super(bonusType.getSkillName());

        switch (bonusType) {
            case AGI: bonusAgi = amountBonus;
                break;
            case END: bonusEnd = amountBonus;
                break;
            case INT: bonusInt = amountBonus;
                break;
            case STR: bonusStr = amountBonus;
                break;
            case HP: bonusHPMax= amountBonus;
                break;
            case MP: bonusMPMax = amountBonus;
                break;
        }

    }

    @Getter
    @AllArgsConstructor
    public enum BonusType {
        AGI("Cats grace"),
        STR("Giants strength"),
        END("Bearskin"),
        INT("Owls wisdom"),
        HP("Intelligence"),
        MP("Toughness"),
        ;

        private String skillName;

    }
}
