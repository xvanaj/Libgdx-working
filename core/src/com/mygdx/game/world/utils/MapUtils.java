package com.mygdx.game.world.utils;


import com.mygdx.game.world.entity.universe.world.World;

import java.awt.*;
import java.util.stream.Collectors;

public class MapUtils {

    public static boolean isTown(World world, Point mapPoint) {
        return !world.getTowns().values().stream()
                .filter(town -> town.getMapPosition().x == mapPoint.x && town.getMapPosition().y == mapPoint.y)
                .collect(Collectors.toList()).isEmpty();
    }
}
