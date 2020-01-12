package com.mygdx.game.world.entity.being;

import com.mygdx.game.Settings;
import com.mygdx.game.world.entity.being.skills.AttributeBonusSkill;
import com.mygdx.game.world.entity.being.skills.Skill;
import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.entity.item.Equipment;
import com.mygdx.game.world.enums.character.Alignment;
import com.mygdx.game.world.enums.character.AttackType;
import com.mygdx.game.world.enums.character.Race;
import com.mygdx.game.world.enums.item.EquipmentSlot;
import com.mygdx.game.world.enums.item.WeaponType;
import com.mygdx.game.world.enums.spell.Spell;
import com.mygdx.game.world.enums.town.IIconified;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Being extends WorldEntity implements IIconified {

    private String name;
    private Race race;
    private String iconName = "portrait";

    private float level = 1;

    private Equipment equipment = new Equipment();

    //characteristics
    private Alignment alignment = Alignment.NEUTRAL;


    //attributes
    private float strength = 10;
    private float agility = 10;
    private float endurance = 10;
    private float intelligence = 10;

    //skills
    private float meleeProficiency = 1;
    private float rangedProficiency = 1;
    private float spellProficiency = 1;

    private int hp;
    private int mp;
    private int hpMax;
    private int mpMax;

    //calculated attributes
    private int attackSpeed = Settings.BASE_ATTACK_SPEED;
    private int damageMin;
    private int damageMax;

    private int attack;                 //chance to hit
    private int armor;                  //damage reduction in fact
    private int evasion;                //chance to evade
    private int critical;               //chance to critical
    private float criticalMultiplier;     //critical multiplier

    private int power;

    private AttackType attackType = AttackType.MELEE;
    private transient int distance;

    private List<Spell> spells = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();
    private List<Effect> effects = new ArrayList<>();

    //temporary attributes
    private int idleTime;   //in millis
    private int wonBattles;


    public void calculateStats() {
        int bonusStr = race != null ? race.getBonusStr() : 0
                + (equipment.get(EquipmentSlot.Weapon) == null ? 0 : equipment.get(EquipmentSlot.Weapon).getBonusStrength())
                + skills.stream().filter(skill -> skill instanceof AttributeBonusSkill).mapToInt(skill -> ((AttributeBonusSkill) skill).getBonusStr()).sum();
        int bonusAgi =race != null ? race.getBonusAgi() : 0
                + (equipment.get(EquipmentSlot.Legs) == null ? 0 : equipment.get(EquipmentSlot.Legs).getBonusAgility())
                + skills.stream().filter(skill -> skill instanceof AttributeBonusSkill).mapToInt(skill -> ((AttributeBonusSkill) skill).getBonusAgi()).sum();
        int bonusEnd = race != null ? race.getBonusCon() : 0
                + (equipment.get(EquipmentSlot.Torso) == null ? 0 : equipment.get(EquipmentSlot.Torso).getBonusEndurance())
                + skills.stream().filter(skill -> skill instanceof AttributeBonusSkill).mapToInt(skill -> ((AttributeBonusSkill) skill).getBonusEnd()).sum();
        int bonusInt = race != null ? race.getBonusInt() : 0
                + (equipment.get(EquipmentSlot.Head) == null ? 0 : equipment.get(EquipmentSlot.Head).getBonusIntelligence())
                + skills.stream().filter(skill -> skill instanceof AttributeBonusSkill).mapToInt(skill -> ((AttributeBonusSkill) skill).getBonusInt()).sum();
        int bonusHp = race != null ? race.getBonusHPMax() : 0
                + skills.stream().filter(skill -> skill instanceof AttributeBonusSkill).mapToInt(skill -> ((AttributeBonusSkill) skill).getBonusHPMax()).sum();
        int bonusMp = race != null ? race.getBonusMPMax() : 0
                + skills.stream().filter(skill -> skill instanceof AttributeBonusSkill).mapToInt(skill -> ((AttributeBonusSkill) skill).getBonusMPMax()).sum();

        hpMax = (int) (endurance + bonusEnd) * Settings.HP_PER_ENDURANCE + bonusHp;
        mpMax = (int) (intelligence + bonusInt) * Settings.HP_PER_ENDURANCE + bonusMp;

        if (equipment.get(EquipmentSlot.Weapon) == null) {
            attackType = AttackType.MELEE;
        } else {
            attackType = equipment.get(EquipmentSlot.Weapon).getWeaponType().equals(WeaponType.WeaponMelee) ? AttackType.MELEE :
                    equipment.get(EquipmentSlot.Weapon).getWeaponType().equals(WeaponType.WeaponRanged) ? AttackType.RANGED : AttackType.MELEE;
        }

        attackSpeed = (int) (Settings.BASE_ATTACK_SPEED * Math.pow(0.990, agility + bonusAgi));
        damageMin = (int) (strength
                + bonusStr
                + (equipment.get(EquipmentSlot.Weapon) == null ? 0 : equipment.get(EquipmentSlot.Weapon).getDamageMin()));
        damageMax = ((int) strength
                + bonusStr
                + (equipment.get(EquipmentSlot.Weapon) == null ? 0 : equipment.get(EquipmentSlot.Weapon).getDamageMax()));
        critical = normalizeValue((strength + bonusStr)
                + (equipment.get(EquipmentSlot.Waist) == null ? 0 : equipment.get(EquipmentSlot.Waist).getCritical())
                + (equipment.get(EquipmentSlot.LeftFinger) == null ? 0 : equipment.get(EquipmentSlot.LeftFinger).getCritical())
                + (equipment.get(EquipmentSlot.RightFinger) == null ? 0 : equipment.get(EquipmentSlot.RightFinger).getCritical()));
        criticalMultiplier = (float) (1.5 + 0.01 * (strength + bonusStr));
        evasion = normalizeValue((agility + bonusAgi)
                + (equipment.get(EquipmentSlot.Waist) == null ? 0 : equipment.get(EquipmentSlot.Waist).getEvasion())
                + (equipment.get(EquipmentSlot.LeftFinger) == null ? 0 : equipment.get(EquipmentSlot.LeftFinger).getEvasion())
                + (equipment.get(EquipmentSlot.RightFinger) == null ? 0 : equipment.get(EquipmentSlot.RightFinger).getEvasion()));
        armor = (int) endurance + bonusEnd
                + (race != null ? race.getBonusArmor() : 0);


        power = (int) (damageMin + damageMax
                + (endurance + strength + agility + intelligence + armor) * 2 +
                +(evasion + critical) * 4);
        hp = hpMax;
        mp = mpMax;
    }

    private static int normalizeValue(float value) {
        return (int) (((value + 10) / (value + 200))*100);
    }

    public String toShortString() {
        final StringBuffer sb = new StringBuffer();
        if (getHp() <= 0) {
            sb.append("*DEAD* ");
        }
        sb.append(String.format("%-20s",name));
        sb.append("[").append(level).append("]");
        sb.append(", STR=").append(String.format("%.1f", strength));
        sb.append(", AGI=").append(String.format("%.1f", agility));
        sb.append(", END=").append(String.format("%.1f", endurance));
        sb.append(", INT=").append(String.format("%.1f", intelligence));
        sb.append(", Melee=").append(String.format("%.1f", meleeProficiency));
        sb.append(", Ranged=").append(String.format("%.1f", rangedProficiency));
        sb.append(", Spell=").append(String.format("%.1f", spellProficiency));
        sb.append(", hp=").append(hp);
        sb.append("/").append(hpMax);
        sb.append(", mp=").append(mp);
        sb.append("/").append(mpMax);
        sb.append(", attack=").append(attack);
        sb.append("(").append(damageMin).append("-");
        sb.append(damageMax).append(")");
        sb.append(", attSpeed=").append(attackSpeed);
        sb.append(", armor=").append(armor);
        sb.append(", evasion=").append(evasion).append("%");
        sb.append(", critical=").append(critical);
        sb.append("% (x").append(criticalMultiplier).append(")");
        sb.append(", power=").append(power);
        sb.append(", attackType=").append(attackType);
        if (skills != null && !skills.isEmpty()) {
            sb.append(", skills=").append(skills);
        }
        if (spells != null && !spells.isEmpty()) {
            sb.append(", spells=").append(spells);
        }

        return sb.toString();
    }

    public void rejuvenate() {
        setHp(getHpMax());
        setMp(getMpMax());
    }

    public int heal(int hpHealed) {
        if (getHp() <= 0) {
            throw new UnsupportedOperationException();
        }
        
        if (hpHealed > 0) {
            if (getHp() + hpHealed > getHpMax()) {
                hpHealed = getHpMax() - getHp();
                setHp(getHpMax());
            } else {
                setHp(getHp() + hpHealed);
            }
        } else if (hpHealed < 0) {
            setHp(getHp() + hpHealed);
        }

        return hpHealed;
    }

    public int healMana(int mpHealed) {
        if (getMp() <= 0) {
            throw new UnsupportedOperationException();
        }

        if (mpHealed > 0) {
            if (getMp() + mpHealed > getMpMax()) {
                mpHealed = getMpMax() - getMp();
                setMp(getMpMax());
            } else {
                setMp(getMp() + mpHealed);
            }
        } else if (mpHealed < 0) {
            setMp(getMp() + mpHealed);
        }

        return mpHealed;
    }

    public boolean isAlive() {
        return hp > 0;
    }

}
