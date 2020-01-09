package com.mygdx.game.name;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.TextFileContent;
import com.mygdx.game.world.entity.item.WornItem;

import java.util.HashMap;
import java.util.Map;

public class ItemNameGenerator {

    public static String generateItemName(WornItem item) {
        String prefix = "";
        String suffix = "";
        String middle = "";

        switch (item.getSlot()) {
            case Waist:
                middle = "girdle";
                break;
            case Weapon:
                middle = generateWeaponMiddleName(item);
                break;
            case LeftFinger:
                middle = "ring";
                break;
            case RightFinger:
                middle = "ring";
                break;
            case Head:
                middle = "helm";
                break;
            case Legs:
                middle = "leggins";
                break;
            case Torso:
                middle = "armor";
                break;
        }
        return prefix + (prefix.isEmpty() ? middle.substring(0, 1).toUpperCase() + middle.substring(1) : " " + middle)
                + (suffix.isEmpty() ? "" : " " + suffix);
    }

    private static String generateWeaponMiddleName(WornItem item) {
        TextFileContent namesFile = null;

        Map<TextFileContent.FileKeyType, String> filter = new HashMap<>();
        filter.put(TextFileContent.FileKeyType.MINLEVEL, String.valueOf(item.getLevel()));
        filter.put(TextFileContent.FileKeyType.MAXLEVEL, String.valueOf(item.getLevel()));

        switch (item.getWeaponType()) {
            case WeaponMelee:
                namesFile = GameAssetManager.getTextFileContentsStatic("meleeWeaponNames");

                break;
            case WeaponRanged:
                namesFile = GameAssetManager.getTextFileContentsStatic("rangedWeaponNames");

                break;
            case Wand:
                namesFile = GameAssetManager.getTextFileContentsStatic("meleeWeaponNames");

                break;
        }

        return NameGenerator.generateNameFromFileByFilters(filter, namesFile);
    }
}
