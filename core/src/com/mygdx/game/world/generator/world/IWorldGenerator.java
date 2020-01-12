package com.mygdx.game.world.generator.world;

/**
 * Created by Jakub Vana on 29.8.2018.
 */
public interface IWorldGenerator {


    float[][] updateLayer(int width, int height, int octaves, float roughness, float scale);

    float[][] updateLayer(float[][] layer, int octaves, float roughness, float scale);
}
