package com.mygdx.game.world.generator.image.filters;

import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.RGBImageFilter;

public class ThresholdImageFilter extends RGBImageFilter {

    int min_red, min_blue, min_green;
    boolean canFilterIndexColorModel;

    public ThresholdImageFilter(int min_red, int min_blue, int
            min_green)
    {
        this.min_red = min_red;
        this.min_blue = min_blue;
        this.min_green = min_green;
        canFilterIndexColorModel = true;
    }

    public int filterRGB( int x, int y, int rgb )
    {
        DirectColorModel cm = (DirectColorModel) ColorModel.getRGBdefault();

        int alpha = cm.getAlpha(rgb);
        int red   = cm.getRed(rgb);
        int green = cm.getGreen(rgb);
        int blue  = cm.getBlue(rgb);

        red = (red >= this.min_red)? red : 0;
        green = (green >= this.min_green)? green : 0;
        blue = (blue >= this.min_blue)? blue : 0;

        alpha = alpha << 24;
        red     = red   << 16;
        green = green << 8;

        return alpha | red | green | blue;
    }
}
