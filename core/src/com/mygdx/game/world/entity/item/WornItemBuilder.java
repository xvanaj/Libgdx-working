package com.mygdx.game.world.entity.item;

import com.mygdx.game.name.ItemNameGenerator;
import com.mygdx.game.world.enums.item.EquipmentSlot;
import com.mygdx.game.world.enums.item.ItemRarity;
import com.mygdx.game.world.enums.item.WeaponType;
import com.mygdx.game.name.NameGenerator;
import com.mygdx.game.utils.Randomizer;

import java.util.Random;

public class WornItemBuilder {

    private NameGenerator nameGenerator;

    private WornItem item;

    public WornItemBuilder() {
        item = new WornItem();
    }

    public WornItemBuilder createItem(EquipmentSlot slot, ItemRarity rarity, int levelMin, int levelMax) {
        Random random = new Random();
        int level = levelMin + random.nextInt(levelMax - levelMin);

        return createItem(slot, rarity, level, null);
    }

    public WornItemBuilder createItem(EquipmentSlot slot, ItemRarity rarity, int level) {
        WeaponType type = null;

        if (slot.equals(EquipmentSlot.Weapon)) {
            type = Randomizer.get(WeaponType.values());
        }

        return createItem(slot, rarity, level, type);
    }

    public WornItemBuilder createItem(EquipmentSlot slot, ItemRarity rarity, int level, WeaponType itemType) {
        if (level < rarity.getMinLevel()) {
            System.out.println("Cannot create item of level " + level + " and rarity " + rarity.name());
            return null;
        } else {
            Random random = new Random();

            item.setSlot(slot);
            item.setRarity(rarity);
            item.setLevel(level);

            switch (slot) {
                case Weapon: {
                    item.setWeaponType(itemType);
                    if (itemType == null || itemType.equals(WeaponType.WeaponMelee)) {
                        item.setDamageMin(1 + level / 2 + random.nextInt((int) (level * rarity.getModifier() / 4 + 5)));
                        item.setDamageMax(random.nextInt((int) (level * rarity.getModifier() / 4 + 5)) + item.getDamageMin());
                        item.setBonusStrength(generateAttributeBonus2(rarity, level));
                        item.setWeaponType(WeaponType.WeaponMelee);
                    } else if (itemType.equals(WeaponType.WeaponRanged)) {
                        //todo change damage of ranged hero
                        item.setDamageMin(1 + level / 2 + random.nextInt((int) (level * rarity.getModifier() / 4 + 4)));
                        item.setDamageMax(random.nextInt((int) (level * rarity.getModifier() / 4 + 4)) + item.getDamageMin());
                        item.setBonusStrength(generateAttributeBonus2(rarity, level));
                    } else if (itemType.equals(WeaponType.Wand)) {
                        item.setDamageMin(1 + level / 4 + random.nextInt((int) (level * rarity.getModifier() / 8 + 3)));
                        item.setDamageMax(random.nextInt((int) (level * rarity.getModifier() / 8 + 3)) + item.getDamageMin());

                        item.setBonusIntelligence(generateAttributeBonus2(rarity, level));
                    }
                    break;
                }
                case Torso: {
                    item.setArmor(level / 2 + random.nextInt((int) (level * rarity.getModifier() / 4 + 1)));
                    item.setBonusEndurance(generateAttributeBonus2(rarity, level));

                    break;
                }
                case Waist: {
                    item.setEvasion(generateSpecialAttributeBonus(rarity, level));
                    item.setCritical(generateSpecialAttributeBonus(rarity, level));
                    break;
                }
                case LeftFinger:
                case RightFinger: {
                    item.setCritical(generateSpecialAttributeBonus(rarity, level));
                    item.setDamageMin(level / 4 + random.nextInt((int) (level * rarity.getModifier() / 8 + 1)));
                    item.setDamageMax(random.nextInt((int) (level * rarity.getModifier() / 8 + 1)) + item.getDamageMin());
                    break;
                }
                case Head: {
                    item.setBonusIntelligence(generateAttributeBonus2(rarity, level));
                    item.setEvasion(generateSpecialAttributeBonus(rarity, level));

                    break;
                }
                case Legs: {
                    item.setBonusAgility(generateAttributeBonus2(rarity, level));
                    item.setAttackSpeed(generateAttributeBonus2(rarity, level));

                    break;
                }
            }

            setPowerAndCost();
        }
        item.setName(ItemNameGenerator.generateItemName(item));

        return this;
    }

    public WornItemBuilder createWeapon(WeaponType itemType, int level) {
        createWeapon(itemType, ItemRarity.COMMON, level);

        return this;
    }

    public WornItemBuilder createWeapon(WeaponType weaponType, ItemRarity rarity, int level) {
        if (level < rarity.getMinLevel()) {
            System.out.println("Cannot create item of level " + level + " and rarity " + rarity.name());
            return null;
        } else {
            Random random = new Random();

            item.setLevel(level);
            item.setRarity(rarity);
            item.setWeaponType(weaponType);

            switch (item.getSlot()) {
                case Weapon: {
                    //todo determine if ranged or melee
                    item.setDamageMin(level / 2 + random.nextInt((int) (level * rarity.getModifier() / 4 + 1)));
                    item.setDamageMax(random.nextInt((int) (level * rarity.getModifier() / 4 + 1)) + item.getDamageMin());
                    item.setBonusStrength(generateAttributeBonus2(rarity, level));

                    break;
                }
            }

            setPowerAndCost();

        }
        return this;
    }

    private void setPowerAndCost() {
        item.setPower(item.getDamageMin() + item.getDamageMax()
                + (item.getBonusEndurance() + item.getBonusStrength() + item.getBonusAgility() + item.getBonusIntelligence()) * 2 +
                +(item.getEvasion() + item.getCritical() + item.getAttackSpeed()) * 4);

        item.setCost((float) Math.pow(1.05d, item.getPower()
                + Math.pow(item.getPower() + 1, 2.5)
                * item.getRarity().getModifier()));
    }

    private int generateAttributeBonus(ItemRarity rarity, int level) {
        Random random = new Random();

        return (int) ((random.nextInt(level / 4 + 1) + level / 4) * rarity.getModifier());
    }

    private int generateAttributeBonus2(ItemRarity rarity, int level) {
        Random random = new Random();
        float gaussian = (float) random.nextGaussian(); // -1 to 1 ?

        return (int) (Math.abs((gaussian * level / 10 * 0.7 + level / 10)) * rarity.getModifier());
    }

    private int generateSpecialAttributeBonus(ItemRarity rarity, int level) {
        Random random = new Random();

        return (int) ((random.nextInt(level / 8 + 1) + level / 8) * rarity.getModifier());
    }

    private int generateSpecialAttributeBonus2(ItemRarity rarity, int level) {
        Random random = new Random();

        return (int) ((Math.abs(random.nextGaussian()) * level / 8 * 0.7 + level / 8) * rarity.getModifier());
    }

    public WornItem build() {
        return item;
    }

}
