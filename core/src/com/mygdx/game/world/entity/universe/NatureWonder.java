package com.mygdx.game.world.entity.universe;

import com.mygdx.game.world.entity.game.WorldEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class NatureWonder extends WorldEntity {

    @AllArgsConstructor
    @Getter
    public enum HighPlaceName {

        Snezka("Snezka");

        private String name;
    }

    @AllArgsConstructor
    @Getter
    public enum LowPlaceName {

        Snezka("Propadliste");

        private String name;
    }

}
