package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.entity.being.Being;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BattleContext {

    private long maxLength;
    private String loot;
    private boolean printToConsole = false;
    private boolean printBattleStartInfo = false;
    private long timeElapsed = 0;
    private long lastEffectsEvaluationTime = 0;
    private long skillsIdleTime = 0;
    private List<Being> heroes;
    private List<Being> monsters;
    private List<Being> allBeings;
    private boolean running;
    private Boolean heroesWon;

    public void logMessageSkill(final String s) {
        Gdx.app.log("Combat", getTimeElapsed() + ": [Skill activation] " + s);
    }
}
