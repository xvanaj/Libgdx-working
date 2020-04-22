package core.squid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.utils.Randomizer;
import core.cartezza.mapgenerator.generator.MapTextureGenerator;
import squidpony.squidgrid.mapping.WorldMapGenerator;
import squidpony.squidmath.FastNoise;

public class SquidMapGeneratorTest extends Game {

    static int size = 200;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setWindowedMode(200, 200);

        SquidMapGeneratorTest test = new SquidMapGeneratorTest();
        Gdx.app = new Lwjgl3Application(test, configuration);

    }

    @Override
    public void create() {
        generate10RandomMaps();
    }


    public static void generate10RandomMaps() {
        WorldMapGenerator.SpaceViewMap worldMapGenerator = new WorldMapGenerator.SpaceViewMap(size, size);

        //generateMap(new WorldMapGenerator.LocalMap(Randomizer.range(0, 10000), size, size));
        //generateMap(new WorldMapGenerator.TilingMap(Randomizer.range(0, 10000), size, size));
        generateMap(new WorldMapGenerator.TilingMap(Randomizer.range(0, 10000), size, size, new FastNoise(30015, 1,2,3), 3));
        //generateMap(new WorldMapGenerator.EllipticalHammerMap(Randomizer.range(0, 10000), size, size));
    }

    private static void generateMap(WorldMapGenerator mapGenerator) {
        for (int i = 0; i < 1; i++) {
            long ttg = System.currentTimeMillis();

            mapGenerator.landModifier = 1.5;
            mapGenerator.generate();
            WorldMapGenerator.SimpleBiomeMapper mapper = new WorldMapGenerator.SimpleBiomeMapper();
            mapper.makeBiomes(mapGenerator);
            String filename = "_mapgenerator/squid/" + mapGenerator.getClass().getSimpleName() + i + ".png";
            MapTextureGenerator.generateBiomeMapTexture(filename , mapper.biomeCodeData);
            System.out.println(filename + " generated in " + (System.currentTimeMillis() - ttg));

            for (int j = 1; j < 10; j++) {
                ttg = System.currentTimeMillis();
                mapGenerator.zoomIn(1, size / 2, size / 2);
                mapper.makeBiomes(mapGenerator);
                filename = "_mapgenerator/squid/" + mapGenerator.getClass().getSimpleName() + "zoomed" + j + ".png";
                MapTextureGenerator.generateBiomeMapTexture(filename , mapper.biomeCodeData);
                System.out.println(filename + " generated in " + (System.currentTimeMillis() - ttg));
            }
        }
    }
}
