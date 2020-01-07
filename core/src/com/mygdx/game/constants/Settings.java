package com.mygdx.game.constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Settings {

    //APP START settings
    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 768;
    public static final String APP_TITLE = "Test";

    //COMBAT settings
    public static final int BASE_ATTACK_SPEED = 1000;
    public static final int TIME_INCREMENT = BASE_ATTACK_SPEED / 100;
    public static final int DEFAULT_BATTLEFIELD_SIZE = 10;
    public static final int RANGED_ATTACK_RANGE = 3;
    public static final int MAGIC_ATTACK_RANGE = 2;
    public static final int MELEE_ATTACK_RANGE = 1;

    //RPG SETTINGS
    public static final float LEARNING_COEFF = 0.002f;
    public static final int CHANCE_SPECIAL_EVENT = 10;
    public static final int HP_PER_ENDURANCE = 20;
    public static final int MAX_BATTLE_TIME = 1000000;

    //other
    public static final String WORLD_EVENT_PREFIX = "World event: ";

    //debug options
    public static final boolean skipStart = false;
    public static final boolean skipMenu = true;
    public static final boolean debug_startNewGame = true;
    public static final boolean debug_drawUnexplored = true;

}
