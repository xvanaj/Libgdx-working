package com.mygdx.game.world.enums.being;

import com.mygdx.game.world.enums.town.IIconified;
import com.mygdx.game.world.enums.town.ITreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TalentType implements IIconified, ITreeNode {

    //BARBARIAN
    STUN("Stunning blow", "Gives 10 % chance to stun target for 1 sec", 2000000),
    BARK_SKIN("Bark skin", "Gives 1 DR", 10000),
    IRON_SKIN("Iron skin", "Gives 1 DR", 10000),
    //COMMON
    TOUGHNESS("Toughness", "Adds + 1HP/endurance point", 2000),
    BRIGHT_MIND("Bright mind", "Adds + 1MP/intelligence point", 2000),
    //FIGHTER
    WEAPON_EXPERTISE("Weapon expertise", "Adds + 2 to hit", 100000),
    CLEAVE("Cleave", "Additional attack after killing", 100000),
    WEAPON_MASTERY("Weapon mastery", "Adds + 5 to hit", 1000000),
    DEADLY_BLOW("Deadly blow", "Increased critical multiplier + x0.5", 1000000),
    //WIZARD
    SPELL_EXPERTISE("Spell expertise", "Adds + 2 to spell power", 100000),
    SPELL_MASTERY("Spell mastery", "Adds + 5 to spell power", 1000000),

    ;

    private String name;
    private String description;
    private float cost;
    private Set<ITreeNode> prerequisities;

    static {
        BARK_SKIN.setPrerequisities(TOUGHNESS);
        IRON_SKIN.setPrerequisities(BARK_SKIN);
        WEAPON_MASTERY.setPrerequisities(WEAPON_EXPERTISE);
        CLEAVE.setPrerequisities(WEAPON_EXPERTISE);
        STUN.setPrerequisities(WEAPON_MASTERY);
        DEADLY_BLOW.setPrerequisities(WEAPON_MASTERY);

    }

    TalentType(String name, String description, float cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    @Override
    public String getIconName() {
        return "TT_" + getName();
    }

    @Override
    public Set<ITreeNode> getPrerequisities() {
        return this.prerequisities;
    }

    @Override
    public void setPrerequisities(ITreeNode... prerequisities) {
        this.prerequisities = Stream.of(prerequisities).collect(Collectors.toSet());
    }
}
