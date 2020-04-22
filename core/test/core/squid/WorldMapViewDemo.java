package core.squid;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.asset.Utility;
import core.cartezza.mapgenerator.generator.MapGenerator;
import core.map.common.CommonRNG;
import core.squid.tilelistener.TileTooltip;
import core.squid.tilelistener.TileTooltipListener;
import squidpony.ArrayTools;
import squidpony.FakeLanguageGen;
import squidpony.StringKit;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidgrid.mapping.WorldMapGenerator;
import squidpony.squidmath.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WorldMapViewDemo implements ApplicationListener {

    private boolean drawPolitical = false;
    private MapGenerator cartezza;

    private static SquidColorCenter squidColorCenter = new SquidColorCenter();
    private TileTooltip tileTooltip;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        //configuration.setTitle("Sampler");
        configuration.setWindowedMode(Toolkit.getDefaultToolkit().getScreenSize().width - 100, Toolkit.getDefaultToolkit().getScreenSize().height - 100);
        configuration.useVsync(true);
        configuration.setIdleFPS(5);

        WorldMapViewDemo test = new WorldMapViewDemo();
        Gdx.app = new Lwjgl3Application(test, configuration);
        Gdx.app.setLogLevel(Application.LOG_INFO);
    }

    private static final int width = 512, height = 512;
    public static int PIXEL_SIZE = 16;
    private static int bigWidth = width * PIXEL_SIZE;
    private static int bigHeight = height * PIXEL_SIZE;

    private Stage stage;
    private Window hudTop;

    // Biome map colors
    private static Color Ice = Color.WHITE;
    private static Color DarkIce = squidColorCenter.dimmer(Ice.cpy());

    private static Color Desert = new Color(238 / 255f, 218 / 255f, 130 / 255f, 1);
    private static Color DarkDesert = squidColorCenter.dimmer(Desert.cpy());

    private static Color Savanna = new Color(177 / 255f, 209 / 255f, 110 / 255f, 1);
    private static Color DarkSavanna = squidColorCenter.dimmer(Savanna.cpy());

    private static Color TropicalRainforest = new Color(66 / 255f, 123 / 255f, 25 / 255f, 1);
    private static Color DarkTropicalRainforest = squidColorCenter.dimmer(TropicalRainforest.cpy());

    private static Color Tundra = new Color(96 / 255f, 131 / 255f, 112 / 255f, 1);
    private static Color DarkTundra = squidColorCenter.dimmer(Tundra.cpy());

    private static Color TemperateRainforest = new Color(29 / 255f, 73 / 255f, 40 / 255f, 1);
    private static Color DarkTemperateRainforest = squidColorCenter.dimmer(TemperateRainforest.cpy());

    private static Color Grassland = new Color(164 / 255f, 225 / 255f, 99 / 255f, 1);
    private static Color DarkGrassland = squidColorCenter.dimmer(Grassland.cpy());

    private static Color SeasonalForest = new Color(73 / 255f, 100 / 255f, 35 / 255f, 1);
    private static Color DarkSeasonalForest = squidColorCenter.dimmer(SeasonalForest.cpy());

    private static Color BorealForest = new Color(95 / 255f, 115 / 255f, 62 / 255f, 1);
    private static Color DarkBorealForest = squidColorCenter.dimmer(BorealForest.cpy());

    private static Color Woodland = new Color(139 / 255f, 175 / 255f, 90 / 255f, 1);
    private static Color DarkWoodland = squidColorCenter.dimmer(Woodland.cpy());

    // Heightmap colors
    public static Color DeepColor = new Color(0 / 255f, 27 / 255f, 72 / 255f, 1);
    public static Color MediumColor = new Color(0 / 255f, 69 / 255f, 129 / 255f, 1);
    private static Color ShallowColor = squidColorCenter.light(MediumColor.cpy());
    private static Color CoastalColor = squidColorCenter.light(MediumColor.cpy());
    private static Color FoamColor = new Color(161 / 255f, 252 / 255f, 255 / 255f, 1);

    private Coord position;

    private FilterBatch batch;
    private FilterBatch hudBatch;

    private SquidInput input;
    private Viewport view;
    private StatefulRNG rng;
    private long seed;
    private WorldMapGenerator world;
    private WorldMapView wmv;
    private TextureRegion dot;
    private TextureRegion dot16;

    private ShapeRenderer shapeRenderer;

    private int positionCircles = 4;
    private boolean positionCirclesGrowing = false;

    private boolean spinning = false;

    private long ttg = 0; // time to generate
    private OrthographicCamera camera;
    private Label fpsLabel;

    @Override
    public void create() {
        // in your own code you would probably use your own atlas with a 1x1 white pixel TextureRegion in it
        Skin skin = new Skin(Gdx.files.internal("skin/shade/skin/uiskin.json"));
        TextureAtlas atlas = new TextureAtlas("skin/uiskin/uiskin.atlas");

        // here the 1x1 white pixel image is called "white"
        dot = atlas.findRegion("white");
        dot.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //this is because of performance. River tile is created in advance with correct size
        dot16 = new TextureRegion(dot.getTexture());

        batch = new FilterBatch();
        hudBatch = new FilterBatch();
        shapeRenderer = new ShapeRenderer();


        fpsLabel = new Label("fps:", skin);
        fpsLabel.setColor(Color.BLACK);
        fpsLabel.setScale(2f);
        fpsLabel.setPosition(500, Gdx.graphics.getHeight() - 100);

        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false);
        view = new ScreenViewport(camera);
        stage = new Stage(view, batch);

        seed = new CriticalRNG().nextLong();
        rng = new StatefulRNG(seed);
        //// NOTE: this FastNoise has a different frequency (1f) than the default (1/32f), and that
        //// makes a huge difference on world map quality. It also uses extra octaves.

        /*WorldMapGenerator.DEFAULT_NOISE(FastNoise.SIMPLEX_FRACTAL);
        WorldMapGenerator.DEFAULT_NOISE.setFractalOctaves(4);
        WorldMapGenerator.DEFAULT_NOISE.setFractalLacunarity(2.5f);
        WorldMapGenerator.DEFAULT_NOISE.setFractalGain(0.4f);*/
        //world = new WorldMapGenerator.TilingMap(seed, width, height);

        //world = new WorldMapGenerator.MimicMap(seed, new FastNoise(1337, 1f), 0.7);
        world = new WorldMapGenerator.TilingMap(seed, width, height, new FastNoise(13327, 0.8f), 1);
        world.landModifier = 1;
        world.coolingModifier = 1;
//        world = new WorldMapGenerator.EllipticalMap(seed, width, height, WhirlingNoise.instance, 0.875);
        //world = new WorldMapGenerator.EllipticalHammerMap(seed, width, height, ClassicNoise.instance, 0.75);
//        world = new WorldMapGenerator.MimicMap(seed, new FastNoise(1337, 1f), 0.7);
//        world = new WorldMapGenerator.SpaceViewMap(seed, width, height, ClassicNoise.instance, 0.7);
//        world = new WorldMapGenerator.RotatingSpaceMap(seed, width, height, noise, 0.7);
        //world = new WorldMapGenerator.RoundSideMap(seed, width, height, ClassicNoise.instance, 0.8);
//        world = new WorldMapGenerator.HyperellipticalMap(seed, width, height, noise, 1.2, 0.0625, 2.5);
//        world = new WorldMapGenerator.SphereMap(seed, width, height, new FastNoise(1337, 1f), 0.6);
        //     world = new WorldMapGenerator.LocalMap(seed, width, height,  new FastNoise(1337, 1f), 1.1);
//        world = new WorldMapGenerator.LocalMimicMap(seed, ((WorldMapGenerator.LocalMimicMap) world).earth.not(), new FastNoise(1337, 1f), 0.9);

        wmv = new WorldMapView(world);
    /*    1 1vyrazna 1darkest, 1min vyrazna
                1jinak   1svetlejsi   1jinakVrazna  1jinakVraznasvetlejsi
            1   -predchozi svetlejsi
            2vyraznaJina   stejna    nejvyraznejsi   podobna jako 2*/
       /* wmv.initialize(SColor.CW_FADED_RED, SColor.AURORA_BRICK, SColor.DEEP_SCARLET, SColor.DARK_CORAL,
                SColor.LONG_SPRING, SColor.WATER_PERSIMMON, SColor.AURORA_HOT_SAUCE, SColor.PALE_CARMINE,
                SColor.AURORA_LIGHT_SKIN_3, SColor.AURORA_PINK_SKIN_2,
                SColor.AURORA_PUTTY, SColor.AURORA_PUTTY, SColor.ORANGUTAN, SColor.SILVERED_RED, null);

        wmv.initialize(SColor.AURORA_INFECTION, SColor.MOSS_GREEN, SColor.AURORA_AVOCADO, SColor.AURORA_APPLE_GREEN,
                SColor.AURORA_SEAFOAM, SColor.AURORA_VARISCITE, SColor.TURQUOISE, SColor.AURORA_SHINING_SKY,
                SColor.AURORA_INFECTION, SColor.CHARTREUSE_GREEN,
                SColor.AURORA_CALM_SKY, SColor.AURORA_VAPOR, SColor.AURORA_CALM_SKY, SColor.AURORA_VAPOR, null);

        wmv.initialize(SColor.VARIED_PALETTE[40], SColor.VARIED_PALETTE[41], SColor.VARIED_PALETTE[42], SColor.VARIED_PALETTE[43],
                SColor.VARIED_PALETTE[44], SColor.VARIED_PALETTE[45], SColor.VARIED_PALETTE[46], SColor.VARIED_PALETTE[47],
                SColor.VARIED_PALETTE[39], SColor.VARIED_PALETTE[38],
                SColor.VARIED_PALETTE[48], SColor.VARIED_PALETTE[49], SColor.VARIED_PALETTE[50], SColor.VARIED_PALETTE[51], null);
*/

        input = new SquidInput(new SquidInput.KeyHandler() {
            @Override
            public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
                switch (key) {
                    case SquidInput.DOWN_ARROW:
                        movePlayer(0, -1);
                        break;
                    case SquidInput.UP_ARROW:
                        movePlayer(0, 1);
                        break;
                    case SquidInput.LEFT_ARROW:
                        movePlayer(-1, 0);
                        break;
                    case SquidInput.RIGHT_ARROW:
                        movePlayer(1, 0);
                        break;
                    case SquidInput.ENTER:
                        seed = rng.nextLong();
                        generate(seed);
                        rng.setState(seed);
                        break;
                    case '=':
                    case '+':
                        if (alt) {
                            zoomIn();
                        } else {
                            scrollMap(20);
                        }
                        break;
                    case '-':
                    case '_':
                        if (alt) {
                            zoomOut();
                        } else {
                            scrollMap(-20);
                        }
                        break;
                    case 'C':
                    case 'c':
                        if (alt) {
                            centerToPosition(position);
                        }
                        break;
                    case 'P':
                    case 'p':
                        if (alt) {
                            drawPolitical = !drawPolitical;
                        }
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
            public boolean scrolled(final int amount) {
                scrollMap(amount);
                return super.scrolled(amount);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (camera != null) {
                    float x = Gdx.input.getDeltaX();
                    float y = Gdx.input.getDeltaY();

                    camera.translate(-x * camera.zoom, y * camera.zoom);

                    fixBounds();

                }

                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                /*if (button == Input.Buttons.RIGHT) {
                    final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                    zoomOut((int)unproject.x, (int) unproject.y);
                } else {
                    final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                    zoomIn((int)unproject.x, (int) unproject.y);
                }*/
                return true;
            }
        }));
        input.setRepeatGap(Long.MAX_VALUE);
        Gdx.input.setInputProcessor(new InputMultiplexer(input, stage));
        //generate(seed);

        /*Tile[][] tiles = MapGenerator.loadTiles(world.heightData, world.heatData, world.minHeight, world.maxHeight,
                world.minHeat, world.maxHeat, width, height);

        MapGenerator.refreshTiles(world.heightData, world.minHeight, world.maxHeight,
                world.heatData, world.minHeat, world.maxHeat,
                world.moistureData, world.minWet, world.maxWet,
                tiles, true);
        MapGenerator.generateBiomeMap(width, height, tiles);
        MapTextureGenerator.generateBiomeMapTexture(width, height, tiles, 0, 0, 0, 0);*/
        cartezza = new MapGenerator(width, height);
        cartezza.generateRivers = true;
        cartezza.generate();
        tileTooltip = new TileTooltip(Utility.STATUSUI_SKIN);
        stage.addActor(tileTooltip);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //stage.addActor(cartezza.getTiles()[i][j]);
                cartezza.getTiles()[i][j].addListener(new TileTooltipListener(tileTooltip));
            }
        }

        position = new GreasedRegion(cartezza.waterLandMap, '.').singleRandom(CommonRNG.getRng());
        generateFactionColorMap();
        generatePolitical();

        rng.setState(seed);
    }

    private void movePlayer(final int x, final int y) {
        if (cartezza.waterLandMap[position.x + x][position.y + y] == '.') {
            position = position.translate(x, y);
            centerToPosition(position);
        }
    }

    private void centerToPosition(final Coord position) {
        camera.position.x = position.x * PIXEL_SIZE /*- camera.viewportWidth/2*/;
        camera.position.y = position.y * PIXEL_SIZE /*- camera.viewportHeight/2*/;
    }

    //region zooming
    public void zoomIn() {
        long startTime = System.nanoTime();
//        noiseCalls = 0;
        final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        world.zoomIn(1, (int) unproject.x, (int) unproject.y);

        wmv.generate(world.seedA, world.seedB, world.landModifier, world.coolingModifier);
        wmv.show();
        ttg = System.nanoTime() - startTime >> 20;
    }

    public void zoomIn(int zoomX, int zoomY) {
        long startTime = System.nanoTime();
//        noiseCalls = 0;
        world.zoomIn(1, zoomX, zoomY);

        wmv.generate(world.seedA, world.seedB, world.landModifier, world.coolingModifier);
        wmv.show();
        ttg = System.nanoTime() - startTime >> 20;
    }

    public void zoomOut() {
        long startTime = System.nanoTime();
//        noiseCalls = 0;
        world.zoomOut(1, Gdx.input.getX(), Gdx.input.getY());
        wmv.generate(world.seedA, world.seedB, world.landModifier, world.coolingModifier);
        wmv.show();
        camera.viewportHeight *= 2;
        camera.viewportWidth *= 2;
        ttg = System.nanoTime() - startTime >> 20;
    }

    public void zoomOut(int zoomX, int zoomY) {
        long startTime = System.nanoTime();
//        noiseCalls = 0;
        world.zoomOut(1, zoomX, zoomY);
        wmv.generate(world.seedA, world.seedB, world.landModifier, world.coolingModifier);
        wmv.show();
        ttg = System.nanoTime() - startTime >> 20;
    }
//endregion

    public void generate(final long seed) {
        long startTime = System.nanoTime();
        System.out.println("Seed used: 0x" + StringKit.hex(seed) + "L");
        //// parameters to generate() are seedA, seedB, landModifier, coolingModifier.
        //// seeds can be anything (if both 0, they'll be changed so seedA is 1, otherwise used as-is).
        //// higher landModifier means more land, lower means more water; the middle is 1.0.
        //// higher coolingModifier means hotter average temperature, lower means colder; the middle is 1.0.
        //// coolingModifier defaults to being higher than 1.0 on average here so polar ice caps are smaller.
        wmv.generate((int) (seed & 0xFFFFFFFFL), (int) (seed >>> 32),
                0.9 + NumberTools.formCurvedDouble((seed ^ 0x123456789ABCDL) * 0x12345689ABL) * 0.3,
                DiverRNG.determineDouble(seed * 0x12345L + 0x54321L) * 0.55 + 0.9);

        wmv.show();
        ttg = System.nanoTime() - startTime >> 20;
    }

    public void rotate() {
        long startTime = System.nanoTime();
        world.setCenterLongitude((startTime & 0xFFFFFFFFFFFFL) * 0x1.0p-32);
        wmv.generate(world.seedA, world.seedB, world.landModifier, world.coolingModifier);
        wmv.show();
        ttg = System.nanoTime() - startTime >> 20;
    }


    private float timing;

    private void putPositionCircle() {
        //float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
        float delta = Gdx.graphics.getDeltaTime() / 1000;
        timing += delta;

        if (timing > 0.25) {
            timing = 0;
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            shapeRenderer.circle(position.x * PIXEL_SIZE, position.y * PIXEL_SIZE, positionCircles * 20);
            if (positionCirclesGrowing) {
                positionCircles++;
            } else {
                positionCircles--;
            }
            if (positionCircles > 5 || positionCircles < 0) {
                positionCirclesGrowing = !positionCirclesGrowing;
            }
            shapeRenderer.end();
        }
    }

    @Override
    public void render() {
        // standard clear the background routine for libGDX
        Gdx.gl.glClearColor(SColor.DB_INK.r, SColor.DB_INK.g, SColor.DB_INK.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // need to display the map every frame, since we clear the screen to avoid artifacts.
        putMap();
        Gdx.graphics.setTitle("Took " + ttg + " ms to generate" + ". FPS: " + Gdx.graphics.getFramesPerSecond());

        // if we are waiting for the player's input and get input, process it.
        if (input.hasNext()) {
            input.next();
        }
    }

    public void putMap() {
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.setColor(Color.WHITE);
        float c;
        int x = (int) ((camera.position.x - camera.viewportWidth * camera.zoom / 2) / 16);
        int y = (int) ((camera.position.y - camera.viewportHeight * camera.zoom / 2) / 16);
        int x1 = (int) (x + (camera.viewportWidth * camera.zoom / 16)) + 5;
        int y1 = (int) (y + (camera.viewportHeight * camera.zoom / 16)) + 5;
        stage.clear();
        for (int i = x; i < x1; i++) {
            for (int j = y; j < y1; j++) {
                if (i >= 0 && j >= 0 && i < width && j < height) {
                    stage.addActor(cartezza.getTiles()[i][j]);
                }
            }
        }
        stage.addActor(tileTooltip);
        if (PIXEL_SIZE == 16) {

            stage.draw();
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

            batch.begin();
            //tileTooltip.draw(batch, 0.8f);

            /*String[] biomeNameTable = wmv.biomeMapper.getBiomeNameTable();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    String s;
                    //squid mapping
                    //s = biomeNameTable[wmv.getBiomeMapper().getBiomeCode(x, y)];

                    //cartezza mapping
                    Tile tile = cartezza.getTiles()[x][y];
                    BiomeType biomeType = tile.biomeType;
                    s = biomeType != null ? biomeType.name() : "Ocean";
                    batch.draw(Utility.BASIC_TILESET_ATLAS.findRegion(s), x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                    //draw rivers
                    if (Arrays.asList(HeightType.River).contains(tile.heightType)) {
                        batch.draw(Utility.BASIC_TILESET_ATLAS.findRegion("River"), x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                    }

                    //draw political
                    if (drawPolitical) {
                        char symbol = cartezza.politicalMap[x][y];
                        if (factionColorMap.get(symbol) != null) {
                            batch.setColor(factionColorMap.get(symbol).r, factionColorMap.get(symbol).g, factionColorMap.get(symbol).b, 0.65f);
                            batch.draw(Utility.BASIC_TILESET_ATLAS.findRegion("White"), x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                            batch.setColor(Color.WHITE);
                        }
                    }
                }
            }*/

            //cities
            for (Coord coord : cities.keySet()) {
                batch.draw(Utility.BASIC_TILESET_ATLAS.findRegion("Town"), coord.x * PIXEL_SIZE, coord.y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                Utility.STATUSUI_SKIN.getFont("smooth-font").draw(batch, cities.get(coord), coord.x * PIXEL_SIZE, coord.y * PIXEL_SIZE);
            }

            //draw player
            batch.draw(Utility.BASIC_TILESET_ATLAS.findRegion("Player"), position.x * PIXEL_SIZE, position.y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);

            //coords grid
            for (int w = 0; w < width; w++) {
                Utility.STATUSUI_SKIN.getFont("smooth-font").draw(batch, String.valueOf(w), w * PIXEL_SIZE, 0);
            }
            for (int h = 0; h < height; h++) {
                Utility.STATUSUI_SKIN.getFont("smooth-font").draw(batch, String.valueOf(h), 0, h * PIXEL_SIZE);
            }
            batch.end();
        } else {
            /*//slow way should be replaced with texture
            float[][] cm = wmv.getColorMap();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    c = cm[x][y];
                    if (c != WorldMapView.emptyColor) {
                        batch.setColor(c);

                        //change color according to political map
                        if (drawPolitical) {
                            char symbol = politicalMap[x][y];
                            if (factionColorMap.get(symbol) != null) {
                                batch.setColor(Color.rgba8888(new Color(SColor.floatToInt(c)).lerp(factionColorMap.get(symbol), 0.85f)));
                            }
                        }

                        batch.draw(dot, x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                    }
                }
            }*/
            batch.begin();
            batch.draw(cartezza.getBiomeMapTexture(), 0, 0, width, height, 0, 0, width, height, false, true);
            batch.end();
        }

       /* putPositionCircle();
        batch.end();
*/
        hudBatch.begin();
        fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
        fpsLabel.draw(hudBatch, 0.8f);
        hudBatch.end();
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

    //region camera stuff
    @Override
    public void resize(int width, int height) {
        view.update(width, height, true);
        // we also set the bounds of that drawn area here for each viewport.
        view.setScreenBounds(0, 0, width, height - 20);

        if (world != null) {

            float modifier = 1;
            if (bigWidth < camera.viewportWidth) {
                modifier = camera.viewportWidth / bigWidth;
            }
            if (bigHeight < camera.viewportHeight) {
                modifier = (Math.min(camera.viewportHeight / bigHeight, modifier));
            }
            if (modifier != 1) {
                camera.viewportWidth = camera.viewportWidth / modifier;
                camera.viewportHeight = camera.viewportHeight / modifier;
            }
        }

        fixBounds();

    }

    public void fixBounds() {
        float scaledViewportWidthHalfExtent = camera.viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = camera.viewportHeight * camera.zoom * 0.5f;
        final Integer xmax = bigWidth;
        final Integer ymax = bigHeight;

        //skip if too far
        if ((scaledViewportWidthHalfExtent * 2 > xmax) || (scaledViewportHeightHalfExtent * 2 > ymax)) {
            return;
        }

        // Horizontal
        if (camera.position.x < scaledViewportWidthHalfExtent)
            camera.position.x = scaledViewportWidthHalfExtent;
        else if (camera.position.x > xmax - scaledViewportWidthHalfExtent)
            camera.position.x = xmax - scaledViewportWidthHalfExtent;

        // Vertical
        if (camera.position.y < scaledViewportHeightHalfExtent)
            camera.position.y = scaledViewportHeightHalfExtent;
        else if (camera.position.y > ymax - scaledViewportHeightHalfExtent)
            camera.position.y = ymax - scaledViewportHeightHalfExtent;

    }

    private void scrollMap(final int amount) {
        if (camera.viewportWidth * camera.zoom * (1 - 0.05f * amount) < bigWidth) {
            camera.zoom = camera.zoom * (1 - 0.05f * amount);
        } else {
            camera.zoom = bigWidth / camera.viewportWidth;
        }

        //switching from one pixel map to tiled map
        float zoomThreshold = width / 512; //when we scroll 4x on 1024 map then view will be switched

        if (camera.zoom < 1f / 4 && PIXEL_SIZE == 1) {
            PIXEL_SIZE = 16;
            bigWidth = width * PIXEL_SIZE;
            bigHeight = height * PIXEL_SIZE;
            camera.zoom = 4;
            camera.position.x *= 16;
            camera.position.y *= 16;
        } else if (camera.zoom > 4 && PIXEL_SIZE == 16) {
            PIXEL_SIZE = 1;
            bigWidth = width * PIXEL_SIZE;
            bigHeight = height * PIXEL_SIZE;
            camera.position.x /= 16;
            camera.position.y /= 16;
            camera.zoom = 1f / 4;
        }

        fixBounds();
    }
    //endregion

    public Texture createMapTexture() {
        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        float[][] cm = wmv.getColorMap();
        float c;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                c = cm[x][y];
                if (c != WorldMapView.emptyColor) {
                    pixmap.setColor(SColor.floatToInt(c));
                    pixmap.drawPixel(x, y);
                }
            }
        }

        final Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        PixmapIO.writePNG(new FileHandle("mapTexture.png"), pixmap);
        return texture;
    }

    //region Politics
    public OrderedMap<Coord, String> cities = new OrderedMap<>();
    private char[][] politicalMap;
    private OrderedMap<Character, FakeLanguageGen> atlas = new OrderedMap<>();
    private Map<Character, SColor> factionColorMap;

    private void generateFactionColorMap() {
        //generates 64 real world like languages
        atlas.clear();
        for (int i = 0; i < 64; i++) {
            atlas.put(ArrayTools.letterAt(i),
                    rng.getRandomElement(FakeLanguageGen.registered)
                            .mix(rng.getRandomElement(FakeLanguageGen.registered), rng.nextFloat()).removeAccents());
        }

        //generate faction color map
        factionColorMap = new HashMap<>();
        SColor[] factionColors = new SColor[]{
                SColor.RED
                , SColor.BLUE
                , SColor.YELLOW
                , SColor.GREEN
                , SColor.PURPLE
                , SColor.BLACK
                , SColor.CYAN
                , SColor.MAGENTA
                , SColor.PINK
                , SColor.PINK
                , SColor.GRAY
                , SColor.LIME
                , SColor.BROWN
                , SColor.WHITE
                , SColor.MAROON
                , SColor.PEACH
                , SColor.BEIGE
                , SColor.GOLD
                , SColor.PLATINUM
        };
        OrderedSet<SColor> colors = new OrderedSet<>(factionColors);
        colors.shuffle(CommonRNG.getRng());
        for (int i = 0; i < atlas.keySet().size(); i++) {
            char symbol = atlas.keyAt(i);
            if (Character.isLetter(symbol)) {
                factionColorMap.put(symbol, colors.getAt(i));
            }
        }
    }

    private void generatePolitical() {
        //generate SQUID political map
        /*PoliticalMapper politicalMapper = new PoliticalMapper();
        FantasyPoliticalMapper fantasyPoliticalMapper = new FantasyPoliticalMapper();
        politicalMapper.generate(world, 6, 0.2);
        politicalMap = fantasyPoliticalMapper.generate(seed, world, wmv.getBiomeMapper(), 6);*/
        //DungeonUtility.debugPrint(politicalMapper.politicalMap);

        //generate cities

        Coord[] points = new GreasedRegion(cartezza.waterLandMap, '.')
                .copy() // don't want to edit the actual land map
                .removeEdges() // don't want cities on the edge of the map
                .separatedRegionZCurve(0.1, 500) // get 500 points in a regularly-tiling but unpredictable, sparse pattern
                .randomPortion(rng, 112); // randomly select less than 1/4 of those points, breaking the pattern
        for (int i = 0; i < points.length; i++) {
            char p = cartezza.politicalMap[points[i].x][points[i].y];
            if (p == '~' || p == '%')
                continue;
            FakeLanguageGen lang = atlas.get(p);
            if (lang != null) {
                cities.put(points[i], lang.word(rng, false).toUpperCase());
            }
        }
    }
    //endregion

}
