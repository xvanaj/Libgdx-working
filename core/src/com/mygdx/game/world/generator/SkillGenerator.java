package com.mygdx.game.world.generator;

import com.mygdx.game.world.entity.being.skills.*;
import com.mygdx.game.utils.Randomizer;

import java.util.ArrayList;
import java.util.List;

public class SkillGenerator {

    private static List<Skill> skills = new ArrayList<>(); {
        skills.add(new StunSkill(20, 2000));
        skills.add(new StunSkill(15, 3000));
        skills.add(new AttributeBonusSkill(10, AttributeBonusSkill.BonusType.STR));
        skills.add(new AttributeBonusSkill(10, AttributeBonusSkill.BonusType.AGI));
        skills.add(new AttributeBonusSkill(10, AttributeBonusSkill.BonusType.END));
        skills.add(new AttributeBonusSkill(10, AttributeBonusSkill.BonusType.INT));
        skills.add(new CleaveSkill(15, 0.5f));
        skills.add(new CleaveSkill(5, 1));
        skills.add(new VampirismSkill(100, 0.3f));
        skills.add(new VampirismSkill(100, 0.2f));
        skills.add(new RegenerationSkill(1, RegenerationSkill.REGEN_TYPE.HP));
        skills.add(new RegenerationSkill(1, RegenerationSkill.REGEN_TYPE.MP));


    }

    public Skill createRandomSkill() {
        return Randomizer.get(skills);
    }

    public static void main(String[] args) {

    }
}
