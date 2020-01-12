package com.mygdx.game.world.generator.world;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by Jakub Vana on 29.8.2018.
 */
public class BasicLayerGenerator implements IWorldGenerator {

    private int OCTAVES;
    private float ROUGHNESS;
    private float SCALE;

    @Override
    public float[][] updateLayer(int width, int height, int octaves, float roughness, float scale) {
        return generateOctavedSimplexNoise(null, width, height, octaves, roughness, scale);
    }


    @Override
    public float[][] updateLayer(float[][] layer, int octaves, float roughness, float scale) {
        return generateOctavedSimplexNoise(layer, layer.length, layer[0].length, octaves, roughness, scale);
    }

    private float[][] generateOctavedSimplexNoise(float[][] layerMask, int width, int height, int octaves, float roughness, float scale) {
        OCTAVES = octaves;
        ROUGHNESS = roughness;
        SCALE = scale;

        float[][] totalNoise = new float[width][height];
        if (layerMask != null) {
            totalNoise = layerMask;
        }

        float layerFrequency = SCALE;
        float layerWeight = 1;
        float weightSum = 0;

        //
/*        for (int i = 5; i < width-5; i++) {
            for (int j = 0; j < 4; j++) {
                totalNoise[i][height/2+j] = 0.4f;
            }
        }*/

        Random r = new Random();

        SimplexNoise.setSeed(r.nextInt(Integer.MAX_VALUE));
        // Summing up all octaves, the whole expression makes up a weighted average
        // computation where the noise with the lowest frequencies have the least effect

        for (int octave = 0; octave < OCTAVES; octave++) {
            // Calculate single layer/octave of simplex noise, then add it to total noise
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    totalNoise[x][y] += SimplexNoise.noise(x * layerFrequency, y * layerFrequency) * layerWeight;
                }
            }

            // Increase variables with each incrementing octave
            layerFrequency *= 2;
            weightSum += layerWeight;
            layerWeight *= ROUGHNESS;

        }

        normalizeArray(totalNoise);
        return totalNoise;
    }

    private void normalizeArray(float[][] map) {
        float min = 0;
        float max = 0;
        DecimalFormat df = new DecimalFormat("#.####");

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                if (map[i][j] < min) {
                    min = map[i][j];
                } else if (map[i][j] > max) {
                    max = map[i][j];
                }
            }
        }

        float divisor = max - min;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                map[i][j] = Float.parseFloat(df.format(( map[i][j] - min ) / divisor));

            }
        }
    }
}
