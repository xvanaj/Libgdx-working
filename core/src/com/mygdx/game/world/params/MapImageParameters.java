package com.mygdx.game.world.params;

import com.mygdx.game.world.enums.world.MapImageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapImageParameters {

    private MapImageType mapImageType = MapImageType.TILE;
    private String name;
}
