package com.mygdx.game.controller.time;

import com.mygdx.game.controller.EventType;
import com.mygdx.game.world.entity.being.BiographyEvent;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.World;

public class GravityAgingController {

    private final Game game;

    public GravityAgingController(final Game game) {
        this.game = game;
    }

    public void ageHero(final Game game, final Hero hero ) {
        final World world = (World) game.getGameEntity(hero.getWorldCode());
        final float gravityG = world.getGravityG();
        final int daysBetweenEvents = EventType.GRAVITY_EVENT.getMinMinutesToOccurence() +
                EventType.GRAVITY_EVENT.getMaxMinutesToOccurence() / 2 / 60 / 24;
        final float gravityDiff = Math.abs(hero.getRaceType().getIdealGravity() - gravityG);
        final int daysAged = (int) (daysBetweenEvents * gravityDiff - 0.2);
        //todo add because of high ravity, because of low gravity, aged x days, rejuvenated x days
        final String text = String.format("%s aged %s days. World gravity = %s, daysBetweenEvents = %s, gravityDiff = %s",
                hero.getName(), daysAged, gravityG, daysBetweenEvents, gravityDiff);

        hero.getHistory().add(new BiographyEvent(world.getMinute(), world.getCode(), text));
        Game.log("GravityAging", text);
    }
}
