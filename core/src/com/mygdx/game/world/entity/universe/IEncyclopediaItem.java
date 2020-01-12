package com.mygdx.game.world.entity.universe;

public interface IEncyclopediaItem {

    String getName();
    default String getEncyclopediaDescription() {
        return getName();
    };

    String getEncyclopediaIconName();
}
