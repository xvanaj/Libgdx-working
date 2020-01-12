package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Settings;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.entity.spell.SpellEffect;
import com.mygdx.game.world.enums.spell.Spell;
import com.mygdx.game.world.enums.spell.SpellTarget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class SpellController {

    public void castSpell(Being attacker, List<Being> allies, List<Being> enemies, Spell spell, BattleContext context) {
        List<Being> targets = new ArrayList<>();
        enemies.stream().filter(enemy -> enemy.getHp() > 0).collect(Collectors.toList());
        allies.stream().filter(ally -> ally.getHp() > 0).collect(Collectors.toList());


        if (spell.getRange().getTarget().equals(SpellTarget.ENEMY)) {
            if (spell.getRange().getAoe() == 0) {
                enemies.sort(Comparator.comparing(Being::getHp));
                targets.add(enemies.get(0));
            } else {
                targets = enemies.stream()
                        .filter(enemy -> Math.abs(enemy.getDistance() - attacker.getDistance()) - spell.getRange().getAoe() <= -1)
                        .collect(Collectors.toList());
            }

        } else if (spell.getRange().getTarget().equals(SpellTarget.ALLY)) {
            if (spell.getRange().getAoe() == 0) {
                allies.sort(Comparator.comparing(Being::getPower, Comparator.reverseOrder()));
                targets.add(allies.get(0));
            } else {
                targets = allies.stream()
                        .filter(enemy -> Math.abs(enemy.getDistance() - attacker.getDistance()) - spell.getRange().getAoe() <= -1)
                        .collect(Collectors.toList());
            }
        }

        evaluateSpell(attacker, targets, spell, context);
    }

    private void evaluateSpell(Being attacker, List<Being> targets, Spell spell, BattleContext context) {
        Gdx.app.log("combat", attacker.getName() + " casts " + spell.getName() + " on " + targets.size() + " target(s)");
        attacker.setIntelligence(attacker.getIntelligence() + Settings.LEARNING_COEFF);
        attacker.setSpellProficiency(attacker.getSpellProficiency() + Settings.LEARNING_COEFF);

        for (SpellEffect effect : spell.getEffects()) {
            //do spell damage
            if (effect.getEffectType().equals(SpellEffect.Type.DAMAGE)) {
                int damageDealt = new Random().nextInt(effect.getMax() - effect.getMin() + 1) + effect.getMin();
                targets.stream().forEach(target -> {
                    target.setHp(target.getHp() - damageDealt);
                    Gdx.app.log("combat",  attacker.getName() + " inflicted " + damageDealt + " damage to " + target.getName() + ". HP left = " + target.getHp());
                });
            }
            //do spell healing
            if (effect.getEffectType().equals(SpellEffect.Type.HEAL)) {
                int damageDealt = new Random().nextInt(effect.getMax() - effect.getMin() + 1) + effect.getMin();
                targets.stream().forEach(target -> {
                    target.setHp(target.getHp() - damageDealt);
                    Gdx.app.log("combat", context.getTimeElapsed() + attacker.getName() + " inflicted " + damageDealt + " damage to " + target.getName() + ". HP left = " + target.getHp());
                });
            }
            //do spell buffs

            //do special IEffects - resurrect, etc
            if (effect.getEffectType().equals(SpellEffect.Type.RESSURECT)) {
                targets.stream().forEach(target -> target.setHp(new Random().nextInt(effect.getMax() - effect.getMin() + 1) + effect.getMin()));
            }
        }
    }

    public Spell chooseSpell(Being attacker, List<Being> targets, List<Being> allies) {
        List<Spell> usableSpells = attacker.getSpells().stream()
                .filter(spell -> spell.getRequiredLevel() <= attacker.getSpellProficiency())
                .filter(spell -> spell.getType().equals(Spell.SpellType.BATTLE))
                .filter(spell -> (spell.getRange().getTarget().equals(SpellTarget.ENEMY)
                        && targets.stream().anyMatch(target -> Math.abs(target.getDistance() - attacker.getDistance()) <= spell.getRange().getRange())) ||
                        (spell.getRange().getTarget().equals(SpellTarget.ALLY)
                                && allies.stream().anyMatch(ally -> Math.abs(ally.getDistance() - attacker.getDistance()) <= spell.getRange().getRange())))
                .collect(Collectors.toList());

        usableSpells.sort(Comparator.comparing(Spell::getRequiredLevel, Comparator.reverseOrder()));
        return usableSpells.isEmpty() ? null : usableSpells.get(0);
    }
}
