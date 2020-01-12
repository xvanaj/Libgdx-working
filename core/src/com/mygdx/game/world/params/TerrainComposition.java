package com.mygdx.game.world.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Jakub Vana on 8.10.2018.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TerrainComposition {

    private float mountains = 0.2f;
    private float hills = 0.2f;
    private float water = 0.2f;
    private float lowlands = 0.2f;



}
