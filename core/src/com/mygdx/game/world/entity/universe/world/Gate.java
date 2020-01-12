package com.mygdx.game.world.entity.universe.world;

import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.utils.Randomizer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gate extends WorldEntity {

    public Gate() {
        setName(Randomizer.get(GateNames.values()).getName());
    }

    @Getter
    @AllArgsConstructor
    public enum GateNames {
        GATE_OF_THE_SILVER_SERPENT("Gate of the Silver Serpent"),
        CHAMBERS_OF_THE_SPIRIT_MONK("Gate of the Spirit Monk"),
        LABYRINTH_OF_THE_UNKNOWN_PANTHER("Portal of the Unknown Panther"),
        CAVERNS_OF_THE_MAD_MARSH("Portal of the Mad Marsh"),
        THE_WAILING_HAUNT("The Wailing Gate"),
        THE_CORAL_DUNGEON("The Coral Entrance"),
        THE_DANCING_QUARTERS("The Dancing Portal"),
        THE_TURBULENT_TOMBS("The Turbulent Portal"),
        THE_SHIMMERING_CRYPT("The Shimmering Gateway"),
        THE_LIFELESS_VAULT("The Lifeless Gate"),
        CATACOMBS_OF_THE_SPIRIT_HUNTER("Entrance of the Spirit Hunter"),
        VAULT_OF_THE_FORBIDDEN_WIDOW("Entrance of the Forbidden Widow"),
        DUNGEON_OF_THE_THUNDER_PHOENIX("Gateway of the Thunder Phoenix"),
        DELVES_OF_THE_UNKNOWN_CULT("Delves of the Unknown Cult"),
        THE_ABANDONED_CRYPT("The Abandoned Gateway"),
        THE_GLISTENING_CELLS("The Glistening Cells"),
        THE_ENDER_POINT("The Ender Point"),
        THE_ETHEREAL_TOMBS("The Ethereal Portal"),
        THE_GLISTENING_QUARTERS("The Glistening Portal"),
        THE_NARROW_TUNNELS("The Narrow Gate");

        private String name;
    }

}
