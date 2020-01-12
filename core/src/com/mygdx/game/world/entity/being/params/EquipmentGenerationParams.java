package com.mygdx.game.world.entity.being.params;

import com.mygdx.game.world.enums.item.ItemRarity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentGenerationParams {

    private boolean hasEquipment = true;

    private ItemRarity maxItemRarity = ItemRarity.COMMON;
    private boolean getItemsLevelFromHeroLevel = true;
    private boolean fillAllSlots = true;

}
