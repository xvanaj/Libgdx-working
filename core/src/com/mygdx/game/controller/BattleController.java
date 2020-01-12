package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.builder.ContextBuilder;
import com.mygdx.game.Settings;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.enums.spell.Spell;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mygdx.game.Settings.BASE_ATTACK_SPEED;
import static com.mygdx.game.Settings.TIME_INCREMENT;
import static java.lang.Thread.sleep;

public class BattleController {

    public static final String LOGGER_TAG_COMBAT = "Combat";
    public static final int PERCENT_CONSTANT = 100;
    SpellController spellController;

    private Random random = new Random();

    public BattleController() {
    }

    public <T extends Being, R extends Being> BattleContext evaluateBattle(List<T> heroes, List<R> monsters) {
        return evaluateBattle(heroes, monsters, null);
    }

    public <T extends Being, R extends Being> BattleContext evaluateBattle(List<T> heroes, List<R> monsters, BattleContext context) {
        if (context == null) {
            context = new ContextBuilder().createDefaultContext().addHeroes((List<Being>) heroes).addMonsters((List<Being>) monsters).build();
        }
        initBattle(context);

        if (context.isPrintToConsole()) {
            Gdx.app.log("battleController", "Battle begins ");
            Gdx.app.log("battleController", "Side one:");
            heroes.stream().forEach(s -> Gdx.app.log("battleController", s.toShortString()));
            Gdx.app.log("battleController", "Side two:");
            monsters.stream().forEach(a -> Gdx.app.log("battleController", a.toShortString()));
        }
        while (context.isRunning()) {
            try {
                sleep(0);
                context.setTimeElapsed(context.getTimeElapsed() + TIME_INCREMENT);
                performActions(context);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkBattleEnd(context);
        }
        if (context.isPrintToConsole()) {
            Gdx.app.log("battleController", "Battle ends ");
            Gdx.app.log("battleController", "Side one:");
            heroes.stream().forEach(a -> Gdx.app.log("battleController", a.toShortString()));
            Gdx.app.log("battleController", "Side two:");
            monsters.stream().forEach(a -> Gdx.app.log("battleController", a.toShortString()));
        }

        return context;
    }

    private void performActions(BattleContext context) {
        adjustIdleTimes(context);
        adjustEffectDurations(context);

        context.getAllBeings().stream().forEach(being -> {
            if (being.isAlive() &&  being.getIdleTime() >= (being.getAttackSpeed())) {
                performAction(being, context);
                being.setIdleTime(0);
            }
        });

        if (context.getSkillsIdleTime() > BASE_ATTACK_SPEED) {
            context.setSkillsIdleTime(context.getSkillsIdleTime() - BASE_ATTACK_SPEED);
            context.getAllBeings().stream()
                    .filter(being -> being.getHp() > 0)
                    .forEach(being -> Arrays.stream(being.getEquipment().getWornEquipment())
                            .forEach(item -> item.getEffects().stream()
                                    .forEach(effect -> effect.onStartOfTimePeriod(context, being))));
            context.getAllBeings().stream()
                    .filter(being -> being.getHp() > 0)
                    .forEach(being -> being.getSkills().stream()
                            .forEach(skill -> skill.onStartOfTimePeriod(context, being)));
        }
    }

    private void adjustEffectDurations(final BattleContext context) {
        long millisSinceLastEffectEval = context.getTimeElapsed() - context.getLastEffectsEvaluationTime();
        context.setLastEffectsEvaluationTime(context.getTimeElapsed());

        for (Being being : context.getAllBeings()) {
            being.getEffects().stream().forEach(effect -> effect.setDuration(effect.getDuration() - millisSinceLastEffectEval));
            //if duration gets under 0, it is removed
            being.setEffects(being.getEffects().stream().filter(effect -> effect.getDuration() > 0).collect(Collectors.toList()));
        }
    }

    private void performAction(Being attacker, BattleContext context) {
        if (attacker.getEffects().stream().anyMatch(effect -> effect.isStunned())) {
            Gdx.app.log(LOGGER_TAG_COMBAT, attacker.getName() + " is stunned and skips turn");
            return;
        }

        List<Being> allies;
        List<Being> targets;
        if (context.getHeroes().contains(attacker)) {
            targets = context.getMonsters();
            allies = context.getHeroes();
        } else {
            allies = context.getMonsters();
            targets = context.getHeroes();
        }

        switch (attacker.getAttackType()) {
            case MELEE: {
                targets = targets.stream().filter(a -> a.getDistance() == attacker.getDistance() && a.getHp() > 0).collect(Collectors.toList());
                if (!targets.isEmpty()) {
                    performMeleeAttack(attacker, targets, context);
                } else {
                    moveBeingCloserToEnemy(attacker, context);
                }
                break;
            }
            case SPELL: {
                Spell pickedSpell = spellController.chooseSpell(attacker, targets, allies);
                if (pickedSpell == null) {
                    Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " No spell to cast for " + attacker.getName() + ". Skipping");
                } else {
                    spellController.castSpell(attacker, allies, targets, pickedSpell, context);
                }
                break;
            }
            case RANGED: {
                targets = targets.stream().filter(target -> (Math.abs(target.getDistance() - attacker.getDistance()) <= 3) && target.getHp() > 0)
                        .collect(Collectors.toList());
                if (!targets.isEmpty()) {
                    performRangedAttack(attacker, targets, context);
                } else {
                    moveBeingCloserToEnemy(attacker, context);
                }
                break;
            }
        }
    }

    private void performMeleeAttack(Being attacker, List<Being> targets, BattleContext context) {
        Being target = targets.get(random.nextInt(targets.size()));
        double dmgMultiplier = 1;


        if (random.nextInt(PERCENT_CONSTANT) > target.getEvasion()) {
            if (random.nextInt(PERCENT_CONSTANT) < attacker.getCritical()) {
                dmgMultiplier = attacker.getCriticalMultiplier();
            }

            int damageDealt = (int) (new Double((attacker.getDamageMin() +
                    (attacker.getDamageMax() > attacker.getDamageMin()
                            ? random.nextInt(attacker.getDamageMax() - attacker.getDamageMin())
                            : 0))) * dmgMultiplier);

            damageDealt = dealPhysicalDamage(target, dmgMultiplier, damageDealt);

            if (dmgMultiplier == 1) {
                Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + attacker.getName() + " attacked " + target.getName() + " and inflicted " + damageDealt + " damage. HP left = " + target.getHp());
            } else {
                Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + attacker.getName() + " attacked " + target.getName() + " and inflicted critical (x " + new DecimalFormat("#0.00").format(dmgMultiplier) + ") " + damageDealt + " damage. HP left = " + target.getHp());
            }

            final int finalDamageDealt = damageDealt;
            attacker.getSkills().stream().forEach(skill -> skill.onSuccesfullAttack(context, attacker, target, finalDamageDealt));
            attacker.getSkills().stream().forEach(skill -> skill.onBeingDamaged(context, attacker, target, finalDamageDealt));

            attacker.setStrength(attacker.getStrength() + (damageDealt * Settings.LEARNING_COEFF / attacker.getStrength()));
            attacker.setMeleeProficiency(attacker.getMeleeProficiency() + Settings.LEARNING_COEFF);
        } else {
            Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + target.getName() + " evaded " + attacker.getName() + "'s attack");
            attacker.setAgility(attacker.getAgility() + Settings.LEARNING_COEFF);
        }
    }

    private void performRangedAttack(Being attacker, List<Being> targets, BattleContext context) {
        Being target = targets.get(random.nextInt(targets.size()));
        double dmgMultiplier = 1;

        if (random.nextInt(PERCENT_CONSTANT) > target.getEvasion()) {
            if (random.nextInt(PERCENT_CONSTANT) < attacker.getCritical()) {
                dmgMultiplier = attacker.getCriticalMultiplier();
            }

            int damageDealt = (int) (new Double((attacker.getDamageMin() +
                    (attacker.getDamageMax() > attacker.getDamageMin()
                            ? random.nextInt(attacker.getDamageMax() - attacker.getDamageMin())
                            : 0) - target.getArmor())) * dmgMultiplier);
            damageDealt = dealPhysicalDamage(target, dmgMultiplier, damageDealt);

            if (dmgMultiplier == 1) {
                Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + attacker.getName() + " shot " + target.getName() + " and inflicted " + damageDealt + " damage. HP left = " + target.getHp());
            } else {
                Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + attacker.getName() + " shot " + target.getName() + " and inflicted critical (x " + new DecimalFormat("#0.00").format(dmgMultiplier) + ") " + damageDealt + " damage. HP left = " + target.getHp());
            }
            //attacker.setAgility(attacker.getAgility() + Settings.LEARNING_COEFF);
            //attacker.setRangedProficiency(attacker.getRangedProficiency() + Settings.LEARNING_COEFF);
        } else {
            Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + attacker.getName() + " evaded " + target.getName() + "'s ranged attack");
            //target.setAgility(attacker.getAgility() + Settings.LEARNING_COEFF);
        }
    }

    private int dealPhysicalDamage(final Being target, final double dmgMultiplier, int damageDealt) {
        //minimum damage is always at least 1
        if (damageDealt < 1) {
            damageDealt = 1;
        }

        target.setHp(target.getHp() - damageDealt);
        return damageDealt;
    }

    private void moveBeingCloserToEnemy(Being being, BattleContext context) {
        if (being.getEffects().stream().anyMatch(effect -> effect.isImmobilized())) {
            Gdx.app.log(LOGGER_TAG_COMBAT, being.getName() + " is immobilized and cannot move closer to enemy");
            return;
        }

        if (context.getHeroes().contains(being)) {
            being.setDistance(being.getDistance() - 1);
        } else if (context.getMonsters().contains(being)) {
            being.setDistance(being.getDistance() + 1);
        } else {
            throw new UnsupportedOperationException();
        }
        Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " " + being.getName() + " moved closer to battlefield. Distance = " + being.getDistance());
    }

    private void adjustIdleTimes(BattleContext context) {
        context.getAllBeings().stream().forEach(a -> a.setIdleTime(a.getIdleTime() + TIME_INCREMENT));
        context.setSkillsIdleTime(context.getSkillsIdleTime() + TIME_INCREMENT);
    }

    private void initBattle(BattleContext context) {
        context.setAllBeings(Stream.of(context.getHeroes(), context.getMonsters())
                .flatMap(Collection::stream).collect(Collectors.toList()));
        context.getAllBeings().stream().forEach(a -> a.setIdleTime(0));
        context.getHeroes().stream().forEach(a -> a.setDistance(Settings.DEFAULT_BATTLEFIELD_SIZE));
        context.getMonsters().stream().forEach(a -> a.setDistance(0));
        context.setRunning(true);
        context.setTimeElapsed(0);
        context.setHeroesWon(null);
        context.setSkillsIdleTime(0);
    }

    private int getAverageHeroLevel(List<Being> heroes) {
        return (int) (heroes.stream().mapToDouble(Being::getLevel).sum() / heroes.size());
    }

    private void checkBattleEnd(BattleContext context) {
        if (context.getTimeElapsed() > context.getMaxLength()) {
            Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " Battle was too long. Arena closes.");
            afterBattle(context);
        } else if (everyoneIsDead(context.getHeroes())) {
            context.getMonsters().stream().forEach(monster -> monster.setWonBattles(monster.getWonBattles() + 1));
            Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " All heroes are dead");
            context.setHeroesWon(false);
            afterBattle(context);
        } else if (everyoneIsDead(context.getMonsters())) {
            context.getHeroes().stream().forEach(hero -> hero.setWonBattles(hero.getWonBattles() + 1));
            Gdx.app.log(LOGGER_TAG_COMBAT, context.getTimeElapsed() + " All monsters are dead");
            context.setHeroesWon(true);
            afterBattle(context);
        }
    }

    private void afterBattle(final BattleContext context) {
        context.setRunning(false);
    }

    private boolean everyoneIsDead(List<Being> beings) {
        if (beings == null || beings.isEmpty()) return true;
        for (Being being : beings) {
            if (being.getHp() > 0) {
                return false;
            }
        }

        return true;
    }
}
