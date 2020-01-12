package com.mygdx.game.world.entity.item;

import com.mygdx.game.world.entity.being.Effect;
import com.mygdx.game.world.enums.item.EquipmentSlot;
import com.mygdx.game.world.enums.item.ItemRarity;
import com.mygdx.game.world.enums.item.WeaponType;
import com.mygdx.game.world.enums.town.IIconified;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WornItem implements IIconified {

    private int level;
    private EquipmentSlot slot;
    private ItemRarity rarity;
    private WeaponType weaponType;
    private float cost;
    private String name;
    private int power;

    private int damageMin;
    private int damageMax;
    private int armor;
    private int bonusAgility;
    private int bonusStrength;
    private int bonusEndurance;
    private int bonusIntelligence;
    private int evasion;
    private int critical;
    private int attackSpeed;
    private Set<Effect> effects = new HashSet<>();

    @Override
    public String getIconName() {
        return "ankh";
    }

}
