package com.mygdx.game.world.generator.world;

import com.mygdx.game.utils.Randomizer;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class MaskGenerator {
    
    public static void createEdgeMask(float[][] map, float edgeFade) {

        float percentToGradient = edgeFade;
        // this is the percentage of the entire width/height from edge to fade in

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                if (i < map.length / 2) { // left side of map
                    float percentIn = (float) i / map.length; // calculate how far in the map we are
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

                if (i > map.length / 2) { // right side of map
                    float percentIn = 1 - (float) i / map.length;
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

                if (j < map[0].length / 2) { // top side of map
                    float percentIn = (float) j / map[0].length;
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

                if (j > map[0].length / 2) { // top side of map
                    float percentIn = 1 - (float) j / map[0].length;
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }
            }
        }
    }

    public static void createCellularAutomataMask(float[][] heightmap, int fillPercent, int smoothFactor) {
        WaterGenerator waterGenerator = new WaterGenerator(heightmap.length, heightmap[0].length, true, "seed here", fillPercent, smoothFactor);
        waterGenerator.generateMap();
        boolean[][] water = waterGenerator.getMap();

        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[0].length; j++) {
                if (water[i][j]) {
                    heightmap[i][j] = 0;
                }
            }
        }
    }

    public static void createRadialMask(float[][] map) {
        int centerX = map.length / 2;
        int centerY = map[0].length / 2;

        float furthestDistance = (float) Math.sqrt((centerX * centerX) + (centerY * centerY));

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                //Simple squaring, you can use whatever math libraries are available to you to make this more readable
                //The cool thing about squaring is that it will always give you a positive distance! (-10 * -10 = 100)
                float distanceX = (centerX - i) * (centerX - i);
                float distanceY = (centerY - j) * (centerY - j);

                float distanceToCenter = (float) Math.sqrt(distanceX + distanceY);

                //Make sure this value ends up as a float and not an integer
                //If you're not outputting this to an image, get the correct 1.0 white on the furthest edges by dividing by half the map size, in this case 64. You will get higher than 1.0 values, so clamp them!
                distanceToCenter = distanceToCenter / furthestDistance;

                map[i][j] = map[i][j] * (1 - distanceToCenter/2);
            }
        }
    }

    public static float[][] createArtMask(float[][] layer, MapArtType mapArtType, int width, int height) {
        switch (mapArtType) {
            case CIRCLE:
                return drawCircle(layer, "a", new ArtMaskSettings(new Font("TimesRoman", Font.PLAIN, 10), width, height));
            case ASTERISK:
                return drawString(layer, "*", "a", new ArtMaskSettings(new Font("TimesRoman", Font.PLAIN, (int) (width * 0.99)),width, height));
        }
        return null;
    }

    public static float[][] createArtMask(MapArtType mapArtType, int width, int height) {
        switch (mapArtType) {
            case CIRCLE:
                return drawCircle(null, "a", new ArtMaskSettings(new Font("TimesRoman", Font.PLAIN, 10), width, height));
            case ASTERISK:
                return drawString(null, "*", "a", new ArtMaskSettings(new Font("TimesRoman", Font.PLAIN, (int) (width * 0.99)),width, height));
            case SHEET:
                return drawString(null, "Sheet", "a", new ArtMaskSettings(new Font("TimesRoman", Font.PLAIN, (int) (width * 0.99)),width, height));
        }
        return null;
    }

    private static float[][] drawString(final float[][] layer, String text, String artChar, ArtMaskSettings settings) {
        BufferedImage image = getImageIntegerMode(settings.width, settings.height);

        Graphics2D graphics2D = getGraphics2D(image.getGraphics(), settings);
        graphics2D.drawString(text, 1, ((int) (settings.height * 0.99)));

        return graphicsToMap(layer, artChar, settings, image);
    }


    private static float[][] drawCircle(final float[][] layer, String artChar, ArtMaskSettings settings) {
        Ellipse2D.Double circle = new Ellipse2D.Double (10,10,settings.getWidth() - 20,settings.getHeight() - 20);

        BufferedImage image = getImageIntegerMode(settings.width, settings.height);
        Graphics2D g2 = getGraphics2D(image.getGraphics(), settings);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(6.0f));
        g2.draw(circle);

        return graphicsToMap(layer, artChar, settings, image);
    }

    private static float[][] graphicsToMap(float[][] layer, final String artChar, final ArtMaskSettings settings, final BufferedImage image) {
        if (layer == null) {
            layer = new float[settings.getWidth()][settings.getHeight()];
        }

        for (int y = 0; y < settings.height; y++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int x = 0; x < settings.width; x++) {
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : artChar);
                if (image.getRGB(x, y) != -16777216) {
                    layer[x][y] = 0.2f;
                }
            }

            if (stringBuilder.toString()
                    .trim()
                    .isEmpty()) {
                continue;
            }

            //System.out.println(String.valueOf(y) + stringBuilder);
        }

        return layer;
    }

    private static BufferedImage getImageIntegerMode(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private static Graphics2D getGraphics2D(Graphics graphics, ArtMaskSettings settings) {
        graphics.setFont(settings.font);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        return graphics2D;
    }

    public static void createTempMask(final float[][] layer, float min, float max, float disparity) {
        final int width = layer.length;
        final int height = layer[0].length;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                layer[i][j] = (float) (((max-min)*j/height + min) + Randomizer.generate(-disparity/2,disparity/2));
            }
        }
    }

    public static void createTempMask2Poles(final float[][] layer, float min, float max, float disparity) {
        final int width = layer.length;
        final int height = layer[0].length;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height/2; j++) {
                layer[j][i] = (float) (((max-min)*(float)j/(float)height*2 + min) + Randomizer.generate(-disparity/2,disparity/2));
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = height/2; j < height; j++) {
                layer[j][i] = (float) (((max-min)*(2-(float)j*2/(float)height) + min) + Randomizer.generate(-disparity/2,disparity/2));
            }
        }
    }

    @Getter
    private static class ArtMaskSettings {
        public Font font;
        public int width;
        public int height;

        public ArtMaskSettings(Font font, int width, int height) {
            this.font = font;
            this.width = width;
            this.height = height;
        }
    }

    public enum MapArtType {
        CIRCLE,
        ASTERISK,
        SHEET;
    }

    public static void main(String[] args) {
        createArtMask(MaskGenerator.MapArtType.ASTERISK, 100,100);
    }
}
