package com.mygdx.game.world.generator.image;


import com.mygdx.game.world.entity.universe.world.World;

import java.awt.*;

public interface IImageCreator {

    void visualizeFromEnum(World world, String filename, boolean save);

    void visualizeTowns(Graphics2D g2d, World world);
}
