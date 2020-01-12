package com.mygdx.game.world.entity.universe;

import com.mygdx.game.world.entity.universe.world.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class GatePoint {

    private World world;
    private Point worldPosition;

}
