package com.mygdx.game.world.generator.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Hashtable;

public class VoronoiMapFilter {

    private Color mixColor;
    private float mixValue;
    private int[] preMultipliedRed;
    private int[] preMultipliedGreen;
    private int[] preMultipliedBlue;

    public void setColoring(Color mixColor, float mixValue) {
        if (mixColor == null) {
            throw new IllegalArgumentException("mixColor cannot be null");
        } else {
            this.mixColor = mixColor;
            if (mixValue < 0.0F) {
                mixValue = 0.0F;
            } else if (mixValue > 1.0F) {
                mixValue = 1.0F;
            }

            this.mixValue = mixValue;
            int mix_r = (int)((float)mixColor.getRed() * mixValue);
            int mix_g = (int)((float)mixColor.getGreen() * mixValue);
            int mix_b = (int)((float)mixColor.getBlue() * mixValue);
            float factor = 1.0F - mixValue;
            this.preMultipliedRed = new int[256];
            this.preMultipliedGreen = new int[256];
            this.preMultipliedBlue = new int[256];

            for(int i = 0; i < 256; ++i) {
                int value = (int)((float)i * factor);
                this.preMultipliedRed[i] = value + mix_r;
                this.preMultipliedGreen[i] = value + mix_g;
                this.preMultipliedBlue[i] = value + mix_b;
            }

        }
    }

    public BufferedImage filter(BufferedImage src, int[][] voronoiMap) {
        ColorModel destCM = src.getColorModel();
        BufferedImage dst = new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), (Hashtable)null);

        copyBufferedImage(src, dst);
        //setColoring();

        return dst;
    }

    private void copyBufferedImage(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
       /* GraphicsUtilities.getPixels(src, 0, 0, width, height, pixels);
        GraphicsUtilities.setPixels(dst, 0, 0, width, height, pixels);*/
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst, int x, int y, int width2, int height2) {

        if (dst == null) {
            ColorModel destCM = src.getColorModel();
            return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), (Hashtable)null);
        }

        copyBufferedImage(src, dst);
        int[] pixels;

        pixels = new int[width2 * height2];
/*        GraphicsUtilities.getPixels(src, x, y, width2, height2, pixels);
        this.mixColor(pixels);
        GraphicsUtilities.setPixels(dst, x, y, width2, height2, pixels);*/
        return dst;
    }


    private void mixColor(int[] pixels) {
        for(int i = 0; i < pixels.length; ++i) {
            int argb = pixels[i];
            pixels[i] = argb & -16777216 | this.preMultipliedRed[argb >> 16 & 255] << 16 | this.preMultipliedGreen[argb >> 8 & 255] << 8 | this.preMultipliedBlue[argb & 255];
        }

    }
}
