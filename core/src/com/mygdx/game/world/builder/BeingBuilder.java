package com.mygdx.game.world.builder;


import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.name.MonsterNameGenerator;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.entity.being.params.AttributesGenerationParameters;
import com.mygdx.game.world.entity.being.params.BeingCreationParameters;
import com.mygdx.game.world.entity.being.params.EquipmentGenerationParams;
import com.mygdx.game.world.entity.item.Equipment;
import com.mygdx.game.world.entity.item.WornItem;
import com.mygdx.game.world.entity.item.WornItemBuilder;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.enums.being.BeingType;
import com.mygdx.game.world.enums.being.MonsterType;
import com.mygdx.game.world.enums.character.AttackType;
import com.mygdx.game.world.enums.character.Race;
import com.mygdx.game.world.enums.item.EquipmentSlot;
import com.mygdx.game.world.enums.item.ItemRarity;
import com.mygdx.game.world.enums.item.WeaponType;
import com.mygdx.game.world.enums.spell.Spell;
import com.mygdx.game.world.generator.SkillGenerator;
import com.mygdx.game.world.generator.being.HeroClass;
import com.mygdx.game.utils.Randomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BeingBuilder<T extends BeingBuilder<T>> {

    protected Being being;

    public BeingBuilder() {
        this.being = new Being();
    }

    public T createBeingOfLevel(int level) {
        return createBeingOfLevel(level, AttackType.values()[new Random().nextInt(AttackType.values().length)]);
    }

    public <R extends BeingCreationParameters> T createBeing(R parameters) {
        if (parameters.getBeingType().equals(BeingType.MONSTER)) {
            generateMonster(parameters);
        }

        being.calculateStats();

        return (T) this;
    }

    protected void generateMonster(BeingCreationParameters parameters) {

        this.generateLevel(parameters)
                .generateMonsterType(parameters)
                .generateAttributes(parameters.getAttributesGenerationParameters())
                .generateName(parameters)
                .generateItems(parameters)
                .generateSpells(parameters);

    }


    protected T generateMonsterType(BeingCreationParameters parameters) {

        return (T) this;
    }

    public T generateLevel(BeingCreationParameters parameters) {
        int min = parameters.getMinLevel();
        int max = parameters.getMaxLevel();

        being.setLevel(Randomizer.range(min, max));
        return (T) this;
    }

    protected T generateSpells(BeingCreationParameters parameters) {
        return (T) this;
    }

    protected T generateItems(BeingCreationParameters beingParams) {
        final EquipmentGenerationParams params = beingParams.getEquipmentGenerationParams();
        if (params != null && params.isHasEquipment()) {
            if (params.isFillAllSlots()) {
                Arrays.stream(EquipmentSlot.values()).forEach(slot -> {
                    final WornItem item = new WornItemBuilder().createItem(slot, ItemRarity.COMMON, (int) being.getLevel(), getWeaponTypeForHeroType(beingParams.getHeroClass())).build();
                    being.getEquipment().equip(item);
                });

            } else {
                final WornItem weapon = new WornItemBuilder().createItem(EquipmentSlot.Weapon, ItemRarity.COMMON, (int) being.getLevel()).build();
                final WornItem armor = new WornItemBuilder().createItem(EquipmentSlot.Weapon, ItemRarity.COMMON, (int) being.getLevel()).build();
                being.getEquipment().equip(weapon);
                being.getEquipment().equip(armor);
            }
        }

        return (T) this;
    }

    private WeaponType getWeaponTypeForHeroType(HeroClass heroClass) {
        if (heroClass == null) {
            heroClass = Randomizer.get(HeroClass.values());
        }

        switch (heroClass) {
            case Mage:
                return WeaponType.Wand;
            case Warrior:
                return WeaponType.WeaponMelee;
            case Archer:
                return WeaponType.WeaponRanged;
        }

        throw new UnsupportedOperationException("Unsupported weapon type");
    }

    protected T generateName(BeingCreationParameters parameters) {
        return (T) this;
    }

    protected T generateAttributes(AttributesGenerationParameters parameters) {
        new AttributesGenerator().generateRandomPrimaryAttributes(being, parameters);

        return (T) this;
    }

    protected T generateRace(final RaceType raceType) {
        being.setRace(Race.values()[new Random().nextInt(Race.values().length)]);

        return (T) this;
    }


    public T createBeingOfLevel(int level, AttackType attackType) {
        being.setLevel(level);
        being.setIconName("someIconName");

        generateAttributes(new AttributesGenerationParameters());

        this.equipBeingWithItemsOfLevel(level);
        if (attackType == AttackType.RANGED) {
            this.equipBeingWithWeaponOfLevel(WeaponType.WeaponRanged, level);
            being.setRangedProficiency(level);
        } else if (attackType == AttackType.SPELL) {
            this.equipBeingWithWeaponOfLevel(WeaponType.Wand, level);
            being.setSpellProficiency(level);
            addSpellsUpToLevel(level, 50);
        } else {
            being.setMeleeProficiency(level);
        }
        being.calculateStats();
        being.setHp(being.getHpMax());
        being.setMp(being.getMpMax());

        return (T) this;
    }

    public T createMonsterOfLevel(int level, GameAssetManager gameAssetManager) {

        final List<MonsterType> availableMonsterTypes = Arrays.stream(MonsterType.values())
                .filter(type -> type.getLevelMin() <= level && type.getLevelMax() >= level)
                .collect(Collectors.toList());
        final MonsterType chosenMonsterType = availableMonsterTypes.get(new Random().nextInt(availableMonsterTypes.size()));

        being = createBeingOfLevel(level).build(gameAssetManager);
        being.setName(chosenMonsterType.getName());

        return (T) this;
    }

    protected void addSpellsUpToLevel(int level, int percentOfAllSpells) {
        if (being.getSpells() == null) {
            being.setSpells(new ArrayList<>());
        }
        Random random = new Random();
        Arrays.stream(Spell.values()).forEach(spell -> {
            if (spell.getRequiredLevel() <= level && random.nextInt(100) < percentOfAllSpells) {
                being.getSpells().add(spell);
            }
        });
    }

    public T equipBeingWithItemsOfLevel(int level) {
        if (being.getEquipment() == null) {
            being.setEquipment(new Equipment());
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            being.getEquipment().equip(new WornItemBuilder().createItem(slot, ItemRarity.COMMON, level).build());
        }

        return (T) this;
    }

    public T equipBeingWithWeaponOfLevel(WeaponType type, int level) {
        if (being.getEquipment() == null) {
            being.setEquipment(new Equipment());
        }
        being.getEquipment().equip(new WornItemBuilder().createItem(EquipmentSlot.Weapon, ItemRarity.COMMON, level, type).build());

        return (T) this;
    }

    public Being build(GameAssetManager gameAssetManager) {
        being.calculateStats();
        if (being.getName() == null) {  //todo delete
            being.setName(MonsterNameGenerator.generateMonsterName((int) being.getLevel(), gameAssetManager));
        }

        being.setHp(being.getHpMax());
        being.setMp(being.getMpMax());
        return being;
    }

    public T name(String name) {
        being.setName(name);

        return (T) this;
    }

    protected T generateSkills(final BeingCreationParameters parameters) {
        being.getSkills().add(new SkillGenerator().createRandomSkill());

        return (T) this;
    }
}
    
