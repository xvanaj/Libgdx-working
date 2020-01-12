package com.mygdx.game.world.enums.spell;

import com.mygdx.game.world.entity.universe.IEncyclopediaItem;
import com.mygdx.game.world.entity.spell.SpellEffect;
import com.mygdx.game.world.entity.spell.SpellRange;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static com.mygdx.game.world.entity.spell.SpellEffect.Type.DAMAGE;


@Getter
@AllArgsConstructor
public enum Spell implements IEncyclopediaItem {

    MAGIC_ARROW("Magic arrow", "Deals small amount of damage to one target", new SpellRange(4), 3, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),
    FROSTBITE("Frostbite", "Deals small amount of damage to one target", new SpellRange(4), 10, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),
    FIREBALL("Fire ball", "Deals small amount of damage to one target", new SpellRange(4), 30, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),
    FIREWALL("Fire wall", "Deals small amount of damage to one target", new SpellRange(4, 1, true), 50, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),
    SUFFERING("Magic arrow", "Deals small amount of damage to one target", new SpellRange(4), 70, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),
    HAND_OF_DOOM("Hand of death", "Deals small amount of damage to one target", new SpellRange(4), 90, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),
    IMPLOSION("Hand of death", "Deals small amount of damage to one target", new SpellRange(4), 99, SpellType.BATTLE, Arrays.asList(new SpellEffect(DAMAGE, 4, 10))),

    HEALING_1("Heal 1", "Deals small amount of damage to one target", new SpellRange(4, 0, SpellTarget.ALLY), 3, SpellType.BATTLE, Arrays.asList(new HealEffect(4, 10))),
    HEALING_2("Heal 2", "Deals small amount of damage to one target", new SpellRange(4, 0, SpellTarget.ALLY), 6, SpellType.BATTLE, Arrays.asList(new HealEffect(8, 20))),
    HEALING_3("Heal 3", "Deals small amount of damage to one target", new SpellRange(4, 0, SpellTarget.ALLY), 12, SpellType.BATTLE, Arrays.asList(new HealEffect(16, 40))),
    HEALING_4("Heal 4", "Deals small amount of damage to one target", new SpellRange(4, 0, SpellTarget.ALLY), 24, SpellType.BATTLE, Arrays.asList(new HealEffect(32, 80))),
    HEALING_5("Heal 5", "Deals small amount of damage to one target", new SpellRange(4, 0, SpellTarget.ALLY), 48, SpellType.BATTLE, Arrays.asList(new HealEffect(64, 160))),
    HEALING_MASS("Mass Heal", "Deals small amount of damage to one target", new SpellRange(4, 0, SpellTarget.ALLY), 96, SpellType.BATTLE, Arrays.asList(new HealEffect(64, 160))),;

    private String name;
    private String description;
    private SpellRange range;
    private int requiredLevel;
    private SpellType type;
    private List<SpellEffect> effects;

    @Override
    public String getEncyclopediaDescription() {
        return name;
    }

    @Override
    public String getEncyclopediaIconName() {
        return "ankh";
    }


    public enum SpellType implements IEncyclopediaItem {
        BATTLE,
        TOWN,
        ;

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getEncyclopediaIconName() {
            return "ankh";
        }
    }

    private static class HealEffect extends SpellEffect {

        public HealEffect(Type effectType, int min, int max) {
            super(effectType, min, max);
        }

        public HealEffect(int min, int max) {
            super(Type.HEAL, min, max);
        }
    }
}
