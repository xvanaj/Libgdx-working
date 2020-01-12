package com.mygdx.game.utils;

import com.mygdx.game.world.entity.universe.world.World;

public class TimeUtils {

    public static String minutesToYearAndDay(final World world, final int minute) {
        final int year = minute / 60 / world.getHoursInDay() / world.getDaysInYear();
        final int day = minute % (60 / world.getHoursInDay());

        return "Year " +  year + ", day " + day;
    }
}
