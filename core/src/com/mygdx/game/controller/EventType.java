package com.mygdx.game.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    CITY_GROWTH(Constants.HOUR * 22, Constants.HOUR * 26),
    CIV_RANDOM_EVENT(Constants.HOUR * 22 * 1, Constants.HOUR * 26 * 1),
    CIV_BORDER_EXPANSION(Constants.HOUR * 22 * 1, Constants.HOUR * 26 * 1),
    CIV_RELATIONS_UPDATE(Constants.HOUR * 22 * 1, Constants.HOUR * 26 * 1),

    GRAVITY_EVENT(Constants.DAY * 28, Constants.DAY * 32),
    WORLD_EVENT(Constants.HOUR * 22 * 90, Constants.HOUR * 26 * 90),
    ELDER_EVENT(Constants.HOUR * 22 * 30, Constants.HOUR * 26 * 30),
    UNIVERSE_EVENT(Constants.YEAR - 5 * Constants.DAY, Constants.YEAR + 5 * Constants.DAY),
    ANCIENTS_MOVE_EVENT(Constants.HOUR * 2, Constants.HOUR * 6),
    ;

    private int minMinutesToOccurence, maxMinutesToOccurence;

    private static class Constants {
        public static final int HOUR = 60;
        public static final int DAY = 60 * 24;

        public static final int YEAR = 60 * 24 * 365;

    }
}
