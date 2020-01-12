package com.mygdx.game.world.generator.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LayersCreationInputParams {

    private int octaves = 5;
    private float rougness = 0.6f;
    private float scale = 0.03f;

}
