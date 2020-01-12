package com.mygdx.game.world.generator.world;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class VoronoiGenerator {

    static BufferedImage I;
    static int px[], py[], color[];

    private int[][] map;

    public int[][] generateVoronoi(int cells, int width, int height, VoronoiType voronoiType) {
        map = new int[width][height];

        int n = 0;
        Random rand = new Random();
        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        px = new int[cells];
        py = new int[cells];
        color = new int[cells];
        for (int i = 0; i < cells; i++) {
            px[i] = rand.nextInt(width);
            py[i] = rand.nextInt(height);
            color[i] = rand.nextInt(Integer.MAX_VALUE);

        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                n = 0;
                for (byte i = 0; i < cells; i++) {
                    if (distance(px[i], x, py[i], y, voronoiType) < distance(px[n], x, py[n], y, voronoiType)) {
                        n = i;
                    }
                }
                map[x][y] = n;
                I.setRGB(x, y, color[n]);

            }
        }

        createGraphics(voronoiType); //todo delete afterwards

        return map;
    }

    public void createGraphics(VoronoiType voronoiType) {
        try {
            ImageIO.write(I, "png", new File("voronoi " + map + voronoiType + ".png"));
        } catch (IOException e) {

        }
    }

    static double distance(int x1, int x2, int y1, int y2, VoronoiType type) {
        double d = 0;
        switch (type) {
            case EUCLIDIAN:
                d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
                break;
            case MINKOVSKI:
                d = Math.pow(Math.pow(Math.abs(x1 - x2), 1) + Math.pow(Math.abs(y1 - y2), 1), (1 / 1)); // Minkovski
                break;
            case MANHATTAN:
                d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
                break;
        }
        return d;
    }

    public enum VoronoiType {
        EUCLIDIAN,
        MINKOVSKI,
        MANHATTAN,
    }

    public static void main(String[] args) {
        VoronoiGenerator generator = new VoronoiGenerator();
        generator.generateVoronoi(10, 100, 100, VoronoiType.MANHATTAN);
        generator.generateVoronoi(10, 100, 100, VoronoiType.EUCLIDIAN);
        generator.generateVoronoi(10, 100, 100, VoronoiType.MINKOVSKI);
    }
}