package core.cartezza.mapgenerator.generator;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;

public class MapGeneratorTest extends InputAdapter implements ApplicationListener {


    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 700;
        config.height = 768;

        final MapGeneratorTest test = new MapGeneratorTest();
        final LwjglApplication lwjglApplication = new LwjglApplication(test, config);
        Gdx.app = lwjglApplication;
        Gdx.app.setLogLevel(Application.LOG_INFO);

    }

    @Override
    public void create() {
        /*public static double DeepWater = 0.2;
        public static double MediumWater = 0.3;
        public static double ShallowWater = 0.4;
        public static double CoastalWater = 0.48;
        public static double Sand = 0.5;
        public static double Grass = 0.7;
        public static double Forest = 0.8;
        public static double Rock = 0.9;*/

        MapGenerator mapGenerator = new MapGenerator(512,512, 0.2, 0.3,
                0.4,0.48, 0.5, 0.7, 0.8, 0.9);
        Texture generate = mapGenerator.generate();

        /*System.out.println("COASTLINE");
        DungeonUtility.debugPrint(mapGenerator.coastline.toChars());
        System.out.println("POLITICAL");
        DungeonUtility.debugPrint(mapGenerator.politicalMap);
        System.out.println("WATERLAND");
        DungeonUtility.debugPrint(mapGenerator.waterLandMap);
        System.out.println("RIVERS");
        DungeonUtility.debugPrint(mapGenerator.rivers.toChars());
        System.out.println("RIVER BLOCKAGES");
        DungeonUtility.debugPrint(mapGenerator.riverBlockages.toChars());
        System.out.println("TILES");
        for (int i = 0; i < mapGenerator.tiles.length; i++) {
            System.out.println();
            for (int j = 0; j < mapGenerator.tiles[i].length; j++) {
                if (mapGenerator.tiles[i][j].biomeType!= null) {
                    System.out.print(mapGenerator.tiles[i][j].biomeType.name().charAt(0));
                } else {
                    System.out.print(" ");
                }
            }
        }*/
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
