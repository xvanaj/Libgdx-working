package com.mygdx.game.controller;

import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.town.BuildingType;

public interface IPropertyController {

    boolean buyProperty(Player player, BuildingType buildingType);

    boolean sellProperty(Player player, BuildingType buildingType);
}
