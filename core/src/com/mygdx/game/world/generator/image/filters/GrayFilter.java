package com.mygdx.game.world.generator.image.filters;

import java.awt.image.RGBImageFilter;

class GrayFilter extends RGBImageFilter {
    public GrayFilter() { canFilterIndexColorModel = true; }
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (((rgb & 0xff0000) + 0x1800000)/3) & 0xff0000;
        int g = (((rgb & 0x00ff00) + 0x018000)/3) & 0x00ff00;
        int b = (((rgb & 0x0000ff) + 0x000180)/3) & 0x0000ff;
        return a | r | g | b;
    }
}
