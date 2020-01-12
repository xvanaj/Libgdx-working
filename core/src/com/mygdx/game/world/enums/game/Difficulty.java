package com.mygdx.game.world.enums.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Jakub Vana on 24.8.2018.
 */
@Getter
@AllArgsConstructor
public enum Difficulty {

    FAST    (1, 1, 0, 1000, 1000, 4, 4, 30, 30, 4, 2, 3, 8, 1),
    THREE_WORLDS    (1, 1, 0, 1000, 1000, 4, 4, 30, 30, 4, 2, 3, 8, 3),
    EASY    (1, 1200, 9, 100, 1000, 4, 4, 60, 60, 6, 1, 3, 8, 1),
    MEDIUM  (1, 1000, 9, 100, 100, 5, 14, 20, 20, 10, 1, 3, 10, 2),
    HARD    (1, 800, 9, 100, 10, 6, 20, 200, 200, 20, 3, 10, 12, 1),
    INSANE  (1, 600, 9, 100, 10, 8, 40, 300, 300, 30, 3, 10, 14, 1),
    LIFELONG(1, 1, 0, 100, 0, 10, 40, 50, 50, 50, 3, 10, 20, 1);

    private double incomeBonus;
    private int startingYear;
    private int startingSouls;
    private int startingGems;
    private int startingGold;
    private int startingAIsCount;
    private int maxAIsCount;
    private int worldWidth;
    private int worldHeight;
    private int townsCount;
    private int startingGates;
    private int maxGates;
    private int eldersCount;
    private int startingWorldsCount;

}
