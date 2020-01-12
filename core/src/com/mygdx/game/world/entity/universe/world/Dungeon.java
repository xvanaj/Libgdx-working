package com.mygdx.game.world.entity.universe.world;

import com.mygdx.game.world.entity.game.WorldEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
@Setter
@NoArgsConstructor
public class Dungeon extends WorldEntity {

    public Dungeon(Point mapPosition, String name) {
        setX(mapPosition.x);
        setY(mapPosition.y);
        setName(name);
    }

    @Override
    public String getMapInfo() {
        return null;
    }


    @Getter
    @AllArgsConstructor
    public enum DungeonNames{
        TOMBS_OF_THE_SILVER_SERPENT("Tombs of the Silver Serpent"),
        CHAMBERS_OF_THE_SPIRIT_MONK("Chambers of the Spirit Monk"),
        LABYRINTH_OF_THE_UNKNOWN_PANTHER("Labyrinth of the Unknown Panther"),
        CAVERNS_OF_THE_MAD_MARSH("Caverns of the Mad Marsh"),
        THE_WAILING_HAUNT("The Wailing Haunt"),
        THE_CORAL_DUNGEON("The Coral Dungeon"),
        THE_DANCING_QUARTERS("The Dancing Quarters"),
        THE_TURBULENT_TOMBS("The Turbulent Tombs"),
        THE_SHIMMERING_CRYPT("The Shimmering Crypt"),
        THE_LIFELESS_VAULT("The Lifeless Vault"),
        CATACOMBS_OF_THE_SPIRIT_HUNTER("Catacombs of the Spirit Hunter"),
        VAULT_OF_THE_FORBIDDEN_WIDOW("Vault of the Forbidden Widow"),
        DUNGEON_OF_THE_THUNDER_PHOENIX("Dungeon of the Thunder Phoenix"),
        DELVES_OF_THE_UNKNOWN_CULT("Delves of the Unknown Cult"),
        THE_ABANDONED_CRYPT("The Abandoned Crypt"),
        THE_GLISTENING_CELLS("The Glistening Cells"),
        THE_ENDER_POINT("The Ender Point"),
        THE_ETHEREAL_TOMBS("The Ethereal Tombs"),
        THE_GLISTENING_QUARTERS("The Glistening Quarters"),
        THE_NARROW_TUNNELS("The Narrow Tunnels");

        private String name;
    }

    private static String getDungeonName(){
        return null;
    }

}
