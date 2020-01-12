package com.mygdx.game.controller;


import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.town.BuildingType;

public class PropertyController implements IPropertyController {

    @Override
    public boolean buyProperty(Player player, BuildingType buildingType) {
        if (player.getGold() >= buildingType.getCost()) {
            //player.setGold(player.getGold() - buildingType.getCost());
            player.getBuildings().put(buildingType, player.getBuildings().get(buildingType) + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean sellProperty(Player player, BuildingType buildingType) {
        if (player.getBuildings().get(buildingType) > 0) {
            //player.setGold(player.getGold() + buildingType.getCost() / 2);
            player.getBuildings().put(buildingType, player.getBuildings().get(buildingType) - 1);
            return true;
        }
        return false;
    }
}
