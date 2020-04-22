package core.squid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import core.cartezza.mapgenerator.generator.MapGenerator;
import core.cartezza.mapgenerator.generator.MapTextureGenerator;
import squidpony.squidmath.FastNoise;
import squidpony.squidmath.Noise;

public class CartezzaRandomMapGeneratorTest extends Game {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setWindowedMode(200,200);

        CartezzaRandomMapGeneratorTest test = new CartezzaRandomMapGeneratorTest();
        Gdx.app = new Lwjgl3Application(test, configuration);

    }

    @Override
    public void create() {
        generate10RandomMaps();
    }

    public static void generate10RandomMaps() {
        int width = 250;
        int height = 250;
        
        MapGenerator generator = new MapGenerator(width, height);
        Noise.Noise4D terrain = new Noise.Ridged4D(FastNoise.instance, 6, 2f);
        Noise.Noise4D terrainRidged = new Noise.Ridged4D(FastNoise.instance, 6, 2f);
        Noise.Noise4D otherRidged = new Noise.Ridged4D(FastNoise.instance, 5, 1.25f);
        Noise.Noise4D heat = new Noise.Ridged4D(FastNoise.instance, 4, 0.05f);
        Noise.Noise4D moisture = new Noise.Ridged4D(FastNoise.instance, 4, 0.05f);

        //protected static final double terrainFreq = 1.5, terrainRidgedFreq = 1.3, heatFreq = 2.8, moistureFreq = 2.9, otherFreq = 4.5;
//        protected static final double terrainFreq = 1.175, terrainRidgedFreq = 1.3, heatFreq = 2.3, moistureFreq = 2.4, otherFreq = 3.5;
        //protected static final double terrainFreq = 0.95, terrainRidgedFreq = 3.1, heatFreq = 2.1, moistureFreq = 2.125, otherFreq = 3.375;
        MapTextureGenerator.pixelSize = 1;
        generator.useCartezza = true;
        generator.generateWithParams(width, height, generator, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        generator.useCartezza = false;
        //generator.generateWithParams(width, height, generator, 8, 1.5f, 10, 1.3f, 6, 2.8f, 6, 2.9f, 8, 4.5f);
        //generator.generateWithParams(width, height, generator, 8, 1.175f, 8, 1.3f, 6, 2.3f, 6, 2.4f, 8, 3.5f);
        generator.generateWithParams(width, height, generator, 8, 0.95f, 8, 3.1f, 6, 2.1f, 6, 2.125f, 8, 3.375f);

        generator.generateWithParams(width, height, generator, 8, 0.4f, 8, 1.2f, 6, 0.4f, 6, 0.4f, 8, 1.2f);
        generator.generateWithParams(width, height, generator, 8, 0.4f, 8, 1.0f, 6, 0.4f, 6, 0.4f, 8, 1.2f);
        /*for (int i = 2; i < 4; i++) {
            for (int j = 4; j < 8; j++) {
                for (int k = 4; k < 5; k++) {
                    int to = 6;
                    float tf = 0.15f * i;
                    int tro = 6;
                    float trf = 0.2f * j;
                    int ho = 4;
                    float hf = 0.05f * k;
                    int mo = 4;
                    float mf = 0.05f * k;
                    int oro = 6;
                    float orf = 1;

                    generator.generateWithParams(width, height, generator, to, tf, tro, trf, ho, hf, mo, mf, oro, orf);
                }
            }
        }*/
    }
}
