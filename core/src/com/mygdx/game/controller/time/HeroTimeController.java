package com.mygdx.game.controller.time;

import com.mygdx.game.world.entity.being.hero.CharacterState;
import com.mygdx.game.world.entity.being.hero.CharacterTrait;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.being.hero.HeroAction;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.World;

import java.util.HashSet;
import java.util.Set;

public class HeroTimeController {

    private final Game game;

    public HeroTimeController(final Game game) {
        this.game = game;
    }

    public void performActions(final int minutes, final Hero hero) {
        final Set<CharacterTrait> characterTraits = hero.getCharacterTraits();
        final Set<HeroAction> availableActions = new HashSet<>();

        alterTempValues(minutes, hero);
        checkTempValues(minutes, hero);
    }

    private void checkTempValues(final int minutes, final Hero hero) {
        performAging(minutes, hero);

        checkHitPoints(minutes, hero);
        checkTemperature(minutes, hero);
        checkSanity(minutes, hero);
        checkSleep(minutes, hero);
        checkHunger(minutes, hero);
    }

    private void checkHitPoints(final int minutes, final Hero hero) {
        if (hero.getHp() < 0) {
            hero.setHeroAction(HeroAction.IDLE);
            alterCharacterState(CharacterState.DEAD, minutes, hero);
        } else {
            hero.getCharacterStates().remove(CharacterState.DEAD);
        }
    }

    private void checkTemperature(final int minutes, final Hero hero) {
        final float tileTemp = ((World) game.getEntities().get(hero.getWorldCode())).getTemperatureLayer()[hero.getMapPosition().x][hero.getMapPosition().x];
        if (tileTemp - hero.getRaceType().getHeatResistance() > 50) {
            alterCharacterState(CharacterState.HEATING, minutes, hero);
        } else if (tileTemp - hero.getRaceType().getHeatResistance() > 30) {
            alterCharacterState(CharacterState.WARM, minutes, hero);
        } else if (tileTemp - hero.getRaceType().getColdResistance() < -10) {
            alterCharacterState(CharacterState.CHILLING, minutes, hero);
        } else if (tileTemp - hero.getRaceType().getHeatResistance() < -30) {
            alterCharacterState(CharacterState.FREEZING, minutes, hero);
        }
    }

    private void checkHunger(final int minutes, final Hero hero) {
        if (hero.getHunger() > 80) {
            alterCharacterState(CharacterState.STARVING, minutes, hero);
            hero.setHp(hero.getHp() - 1);
            //game.getConsole().log(hero.getName() + " is starving and loses " + 1 + " hit point"); todo
        } else if (hero.getHunger() > 50) {
            alterCharacterState(CharacterState.HUNGRY, minutes, hero);
        } else {
            hero.getCharacterStates().remove(CharacterState.STARVING);
            hero.getCharacterStates().remove(CharacterState.HUNGRY);
        }
    }

    private void checkSleep(final int minutes, final Hero hero) {
        if (hero.getFatigue() >= 80 + (hero.getCharacterTraits().contains(CharacterTrait.SLEEPLESS) ? 20 : 0)) {
            hero.setHeroAction(HeroAction.SLEEP);
            alterCharacterState(CharacterState.SLEEPS, minutes, hero);
            //game.getConsole().log(hero.getName() + " is tired to death. He immediately fell aslepp");  //todo
        } else if (hero.getFatigue() >= 50) {
            alterCharacterState(CharacterState.TIRED, minutes, hero);
        } else {
            hero.getCharacterStates().remove(CharacterState.SLEEPS);
            hero.getCharacterStates().remove(CharacterState.TIRED);
        }
    }

    private void checkSanity(final int minutes, final Hero hero) {
        final float sanity = hero.getSanity();

        if (sanity < 20) {
            alterCharacterState(CharacterState.BECOMING_MAD, minutes, hero);
        } else if (sanity < 40) {
            alterCharacterState(CharacterState.UNSTABLE_MIND, minutes, hero);
        } else {
            hero.getCharacterStates().remove(CharacterState.BECOMING_MAD);
            hero.getCharacterStates().remove(CharacterState.UNSTABLE_MIND);
        }
    }

    private void performAging(final int minutes, final Hero hero) {
        final float worldGravityInG = ((World) game.getGameEntity(hero.getWorldCode())).getGravityG();
        hero.setAge(hero.getAge() + minutes);
        hero.setBiologicalAge((int) (hero.getBiologicalAge() + Math.abs(worldGravityInG - hero.getRaceType().getIdealGravity())/365/24/60));
    }

    private void alterCharacterState(final CharacterState state, final int minutes, final Hero hero) {
        if (hero.getCharacterStates().get(state) == null) {
            hero.getCharacterStates().put(state, minutes);
        } else {
            hero.getCharacterStates().put(state, hero.getCharacterStates().get(state) + minutes);
        }
    }

    private void alterTempValues(final int minutes, final Hero hero) {
        if (hero.getHeroAction() != HeroAction.SLEEP) {
            hero.setFatigue(hero.getFatigue() + 1/(61-minutes));
            hero.setHunger(hero.getHunger() + 1/(61-minutes));
            hero.setSanity(hero.getSanity() + 0.01f/(61 - minutes));
            hero.setJoy(hero.getJoy() - 0.01f/61-minutes);
            hero.setHeroism(hero.getHeroism() - 0.01f/61-minutes);
        } else {
            if (hero.getFatigue() <= 0) {
                hero.setFatigue(0);
                hero.setHeroAction(HeroAction.IDLE);
            } else {
                hero.setFatigue(hero.getFatigue() - 1/(61-minutes));
            }
        }
    }
}
