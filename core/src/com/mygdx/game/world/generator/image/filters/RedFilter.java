package com.mygdx.game.world.generator.image.filters;

import java.awt.image.RGBImageFilter;

public class RedFilter extends RGBImageFilter {

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return rgb & 0xffff0000;
    }
}
