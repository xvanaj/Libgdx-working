package com.mygdx.game.world.builder;

import com.mygdx.game.Settings;
import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.being.Being;

import java.util.List;

public class ContextBuilder {

    private BattleContext context;

    public ContextBuilder() {
        this.context = new BattleContext();
    }

    public BattleContext build(){
        return context;
    }

    public ContextBuilder createDefaultContext() {
        context.setPrintToConsole(true);
        context.setMaxLength(Settings.MAX_BATTLE_TIME);
        context.setPrintBattleStartInfo(true);
        return this;
    }

    public ContextBuilder addHeroes(List<Being> heroes) {
        context.setHeroes(heroes);

        return this;
    }

    public ContextBuilder addMonsters(List<Being> monsters) {
        context.setMonsters(monsters);

        return this;
    }
}
