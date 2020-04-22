package core.squid;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.asset.Utility;
import core.cartezza.mapgenerator.generator.MapGenerator;
import core.cartezza.mapgenerator.generator.MapInputAdapter;
import core.cartezza.mapgenerator.generator.MapTooltip;
import core.cartezza.mapgenerator.generator.domain.MapType;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidMouse;

import java.awt.*;

public class MapGeneratorTest extends MapInputAdapter implements ApplicationListener {

    public static final int VIEWPORT_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int VIEWPORT_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private SquidInput input;

    private Stage stage;
    private Batch batch;
    private MapTooltip mapTooltip;
    private MapGenerator mapGenerator;

    private int offsetX, offsetY;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("Sampler");
        configuration.setWindowedMode(Toolkit.getDefaultToolkit().getScreenSize().width - 100, Toolkit.getDefaultToolkit().getScreenSize().height - 100);
        configuration.useVsync(true);
        configuration.setIdleFPS(5);

        MapGeneratorTest test = new MapGeneratorTest();
        Gdx.app = new Lwjgl3Application(test, configuration);
        Gdx.app.setLogLevel(Application.LOG_INFO);
        Gdx.input.setInputProcessor(test);
    }

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.zoom = camera.zoom * 1f;
        batch = stage.getBatch();
        /*public static double DeepWater = 0.2;
        public static double MediumWater = 0.3;
        public static double ShallowWater = 0.4;
        public static double CoastalWater = 0.48;
        public static double Sand = 0.5;
        public static double Grass = 0.7;
        public static double Forest = 0.8;
        public static double Rock = 0.9;*/

        int width = 1024;
        int height = 1024;
        mapGenerator = new MapGenerator(width, height);
        mapGenerator.useCartezza = true;
        map = mapGenerator.generate();
        textures.put(MapType.BIOME, map);
        textures.put(MapType.POLITICAL, mapGenerator.getPoliticalMapTexture());
        textures.put(MapType.HEATMAP, mapGenerator.getHeatMapTexture());
        textures.put(MapType.MOISTURE, mapGenerator.getMoistureMapTexture());
        textures.put(MapType.WATER, mapGenerator.getWaterMapTexture());

        //map.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        mapTooltip = new MapTooltip(Utility.STATUSUI_SKIN);

        stage.addActor(mapTooltip);
        input = new SquidInput(new SquidInput.KeyHandler() {
            @Override
            public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
                switch (key) {
                    case '1':
                        map = textures.get(MapType.BIOME);
                        break;
                    case '2':
                        map = textures.get(MapType.MOISTURE);
                        break;
                    case '3':
                        map = textures.get(MapType.HEATMAP);
                        break;
                    case '4':
                        map = textures.get(MapType.POLITICAL);
                        break;
                    case '5':
                        map = textures.get(MapType.WATER);
                        break;
                    case SquidInput.DOWN_ARROW:
                        camera.translate(0, -20);
                        break;
                    case SquidInput.UP_ARROW:
                        camera.translate(0, 20);
                        break;
                    case SquidInput.LEFT_ARROW:
                        camera.translate(-20, 0);
                        break;
                    case SquidInput.RIGHT_ARROW:
                        camera.translate(20, 0);
                        break;
                    case SquidInput.ENTER:
                        mapGenerator.generate();
                        textures.put(MapType.BIOME, map);
                        textures.put(MapType.POLITICAL, mapGenerator.getPoliticalMapTexture());
                        textures.put(MapType.HEATMAP, mapGenerator.getHeatMapTexture());
                        textures.put(MapType.MOISTURE, mapGenerator.getMoistureMapTexture());
                        textures.put(MapType.WATER, mapGenerator.getWaterMapTexture());
                        break;
                    case '=':
                    case '+':
                        if (alt) {
                        } else {
                            camera.zoom = camera.zoom * 1.2f;
                        }
                        break;
                    case '-':
                    case '_':
                        if (alt) {
                        } else {
                            camera.zoom = camera.zoom * 0.8f;
                        }
                        break;
                    case 'P':
                    case 'p':
                    case 'S':
                    case 's':
                        break;
                    case 'Q':
                    case 'q':
                    case SquidInput.ESCAPE: {
                        Gdx.app.exit();
                    }
                }
            }
        }, new SquidMouse(4, 4, width, height, 0, 0, new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (camera != null) {
                    float x = Gdx.input.getDeltaX();
                    float y = Gdx.input.getDeltaY();

                    camera.translate(-x * camera.zoom, y * camera.zoom);

                    if (camera.position.x < -map.getTextureData().getWidth() / 2 || camera.position.x > map.getTextureData().getWidth() * 3 / 2) {
                        camera.position.x = map.getTextureData().getWidth() / 2;
                    }
                    if (camera.position.y < -map.getTextureData().getHeight() / 2 || camera.position.y > map.getTextureData().getHeight() * 3 / 2) {
                        camera.position.y = map.getTextureData().getHeight() / 2;
                    }
                }
                return true;
            }

            @Override
            public boolean scrolled(final int amount) {
                camera.zoom = camera.zoom * 1f + ((float) amount) / 20;
                return super.scrolled(amount);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        /*        if (button == Input.Buttons.RIGHT) {
                    zoomOut(screenX, height - 1 - screenY);
                } else {
                    zoomIn(screenX, height - 1 - screenY);
                }*/
                return true;
            }
        }));
        input.setRepeatGap(Long.MAX_VALUE);

        Gdx.input.setInputProcessor(input);
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
        if (map != null) {
            if (map.getWidth() < camera.viewportWidth) {
                float modifier = camera.viewportWidth / map.getWidth();
                camera.viewportWidth = camera.viewportWidth / modifier;
                camera.viewportHeight = camera.viewportHeight / modifier;
            }
            if (map.getHeight() < camera.viewportHeight) {
                float modifier = camera.viewportHeight / map.getHeight();
                camera.viewportWidth = camera.viewportWidth / modifier;
                camera.viewportHeight = camera.viewportHeight / modifier;
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawTexture();

        //drawTooltip();

        batch.end();
        if (input.hasNext()) {
            input.next();
        }
    }


    private void drawTooltip() {
        mapTooltip.updateDescription(mapGenerator.heightData);
        mapTooltip.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        mapTooltip.toFront();
        mapTooltip.draw(batch, 1f);
    }

    private void drawTexture() {
        int width = map.getTextureData().getWidth();
        int height = map.getTextureData().getHeight();

        batch.draw(map, -width, -height, width, height, width * 3, height * 3);
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

    @Override
    protected void handleInput() {
        for (Integer key : keysHeld.keySet()) {
            if (keysHeld.get(key)) {
                final Integer scrollingSpeed = 10;
                final Float zoomSpeed = 0.05f;
                switch (key) {
                    case Input.Keys.LEFT: {
                        camera.translate(-scrollingSpeed * camera.zoom, 0);
                        //offsetX -= -scrollingSpeed * camera.zoom;
                        break;
                    }
                    case Input.Keys.RIGHT: {
                        camera.translate(+scrollingSpeed * camera.zoom, 0);
                        //offsetX -= -scrollingSpeed * camera.zoom;
                        break;
                    }
                    case Input.Keys.UP: {
                        camera.translate(0, +scrollingSpeed * camera.zoom);
                        //offsetY -= -scrollingSpeed * camera.zoom;
                        break;
                    }
                    case Input.Keys.DOWN: {
                        camera.translate(0, -scrollingSpeed * camera.zoom);
                        //offsetY -= -scrollingSpeed * camera.zoom;
                        break;
                    }
                    case Input.Keys.PLUS: {
                        camera.zoom = camera.zoom - zoomSpeed;
                        if (camera.zoom < MIN_CAMERA_ZOOM) {
                            camera.zoom = MIN_CAMERA_ZOOM;
                        }
                        break;
                    }
                    case Input.Keys.MINUS: {
                        camera.zoom = camera.zoom + zoomSpeed;
                        if (camera.zoom > MAX_CAMERA_ZOOM) {
                            camera.zoom = MAX_CAMERA_ZOOM;
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }
}
