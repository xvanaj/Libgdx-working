package com.mygdx.game.world.entity.being;

import com.mygdx.game.world.enums.being.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElderBeing extends Being {

    private SpeciesType speciesType;
    private SkinColor skinColor;
    private HeadType headType;
    private Size size;

    private String typeName;

    public enum SpeciesType {
        HUMANOID,
        MUTANT,
        DRACONIC,
        DEMON,
        DEVIL,
        UNDEAD,
    }

    public enum SkinColor {
        RED,
        BLACK,
        YELLOW,
        PINK,
        GREEN,
        BROWN,
        BLUE,
        TEAL,
        GOLD,
    }

    @Getter
    @AllArgsConstructor
    public enum HeadType {
        NONE(""),
        COMMON(""),
        TENTACLES("tentacles"),
        ONE_EYE("one-eye"),
        THREE_EYE("three-eye"),
        HORNS("horns"),
        HORN("horn"),

        ;

        private String modificationName;
    }

    @Getter
    @AllArgsConstructor
    public enum BodyMaterial {
        LITHIUM("lithium"),
        MITHRILL("mithril"),
        CRYSTAL("crystal"),
        ETHEREAL("ethereal"),
        ;

        private String nameModification;
    }

    public String toShortString() {
        final StringBuffer sb = new StringBuffer();

        sb.append("[" + getTypeName()+ "]");
        sb.append(super.toShortString());

        return sb.toString();
    }

}
