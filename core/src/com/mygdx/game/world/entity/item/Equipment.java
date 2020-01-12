package com.mygdx.game.world.entity.item;

import com.mygdx.game.world.enums.item.EquipmentSlot;
import lombok.Getter;
import lombok.ToString;

@Getter
public class Equipment {

    WornItem[] wornEquipment = new WornItem[EquipmentSlot.values().length];

    public void equip(WornItem wornItem) {
        wornEquipment[wornItem.getSlot().ordinal()] = wornItem;
    }

    public WornItem get(EquipmentSlot equipmentSlot) {
        return wornEquipment[equipmentSlot.ordinal()];
    }

}
