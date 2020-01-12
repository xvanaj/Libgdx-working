package com.mygdx.game.world.enums.item;

import com.mygdx.game.world.enums.town.IIconified;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jakub Vana on 2.9.2018.
 */
@Getter
@AllArgsConstructor
public enum EquipmentSlot implements IIconified {

    Weapon("Weapon"),
    Head("Headgear"),
    Torso("Armor"),
    Legs("Leggins"),
    //Arms("Gloves"),
    LeftFinger("Ring"),
    RightFinger("Ring"),
    Waist("Amulet"),
    //Back("Cloak");
    ;

    private String name;

    @Override
    public String getIconName() {
        return "ankh";
    }
}
