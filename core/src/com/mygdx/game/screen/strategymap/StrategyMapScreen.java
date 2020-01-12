package com.mygdx.game.screen.strategymap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Simulator;
import com.mygdx.game.screen.strategymap.hud.PlayersHud;
import com.mygdx.game.screen.strategymap.window.TiledMapWindow;
import com.mygdx.game.utils.SaveLoadUtils;
import com.mygdx.game.Settings;
import com.mygdx.game.controller.time.TimeController;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.generator.game.GameFactory;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class StrategyMapScreen extends MapScreen implements Screen {

    private static final String TAG = StrategyMapScreen.class.getSimpleName();
    private final static Color DAWNCOLOR = new Color(0, 0, 1, .2f);
    private final static Color DARKCOLOR = new Color(0, 0, 0, .6f);
    private final static Color LIGHTCOLOR = new Color(1, 1, 1, 0);
    private final static Color DUSKCOLOR = new Color(1, .3f, .67f, .1f);

    private boolean drawMap = true;

    private TimeController timeController;

    private OrthographicCamera hudCamera;
    private InputMultiplexer inputMultiplexer;
    private Map<String, TiledMapWindow> entityWindows = new HashMap<>();

    private float timePassed;

    private Label fpsLabel;
    private float dayNightCycleUpdate;

    SpriteBatch spriteBatch;

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private MapManager mapManager;

    public StrategyMapScreen(Simulator parent) {
        super(parent);


        FileHandle fileHandle =  new FileHandle("core/assets/game/game.txt");
        if (!Settings.debug_startNewGame && fileHandle.exists()) {
            Game actual = SaveLoadUtils.loadGame(fileHandle);
            parent.setGame(actual);
        } else {
            parent.setGame(GameFactory.createGame(Difficulty.LIFELONG));
        }

        this.timeController = new TimeController(parent.getGame());

        mapManager = new MapManager(parent.getGame());
    }

    @Override
    public void show() {
        setupViewport(1024, 768);
        map = mapManager.getCurrentMap();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);

        spriteBatch = new SpriteBatch();

        camera.update();

        mapRenderer = new OrthogonalTiledMapRendererWithSprites(map, MapManager.UNIT_SCALE,
                parent.getGame().getHumanPlayer().getPlayersWorld().getExplorationLayer(),
                parent.getGame().getHumanPlayer().getPlayersWorld());
        Gdx.app.debug(TAG, "UnitScale value is: " + mapRenderer.getUnitScale());

        fpsLabel = new Label("fps:", parent.getSkin());
        fpsLabel.setPosition(100, 150);

        playersHud = new PlayersHud(parent, parent.getSkin(), hudCamera);
        playersHud.getStage().addActor(fpsLabel);

        inputMultiplexer = new InputMultiplexer(playersHud.getStage(), this);

        Gdx.input.setInputProcessor(inputMultiplexer);

        centerToPlayer();

    }

    @Override
    public void render(float delta) {
        fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

        handleInput();

        playersHud.render(delta);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
           /* final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            playersHud.updateMapInfo(0f, unproject.x, unproject.y);*/
        }

        if (parent.getGame().getGameSpeed() > 0) {
            timePassed += delta;
            if (timePassed > 1f / parent.getGame().getGameSpeed()) {
                playersHud.updateTopHud(delta);
                timeController.addMinutes(parent.getGame(), 10);
                timePassed = 0;
            }
        }

        camera.update();
        hudCamera.update();
        mapRenderer.setView(camera);

        if (drawMap) {
            Gdx.gl.glClearColor(0, 0, 0, 0.3f);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            processDayNightCycle(delta);
            mapRenderer.render();
            for (TiledMapWindow i : entityWindows.values()) {
                i.render();
            }
        }

        final Point playerPositionInPixels = parent.getGame().getHumanPlayer().getMapPosition();
        mapRenderer.highlightTile(playerPositionInPixels);

        playersHud.render(delta);
    }

    private void processDayNightCycle(final float delta) {
        dayNightCycleUpdate += delta;

        //if ( dayNightCycleUpdate > 0.5) {
            final World world = parent.getGame().getHumanPlayer().getPlayersWorld();
            final int hourOfDay = world.getHour() % world.getHoursInDay();

            if ((hourOfDay < world.getSunriseHour()) || (hourOfDay > world.getSunsetHour())) {
                final float nightHours = (world.getHoursInDay() - world.getSunsetHour()) + world.getSunsetHour();
                final float nightHour = (hourOfDay > world.getSunsetHour())
                        ? (world.getHoursInDay() - world.getSunsetHour())
                        : (world.getHoursInDay() - world.getSunsetHour()) + hourOfDay;

                float color = 1;
                if (nightHour < nightHours/2) {
                    //before middle of night
                    color = 1f - (0.4f * nightHour / nightHours / 2);
                } else {
                    //after middle of night
                    color = 1f - (0.4f * (nightHour-nightHours/2) / nightHours / 2);
                }

                mapRenderer.getBatch().setColor(color, color, color, 1);
            } else {
                mapRenderer.getBatch().setColor(1f, 1f, 1f, 1);
            }
            dayNightCycleUpdate = 0;
        //}
    }


    @Override
    public void resize(int width, int height) {

        setupViewport(1024, 768);
        camera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
        //playersHud.getStage().getViewport().update(width, height);
        //playersHud.getStage().getViewport().setScreenPosition(200, 0);
        //playersHud.getStage().getViewport().update(width, height);
        playersHud.resize((int) VIEWPORT.physicalWidth, (int) VIEWPORT.physicalHeight);

    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        super.mouseClicked(e);
        final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        playersHud.updateMapInfo(0f, unproject.x, unproject.y);
    }

    @Override
    public void dispose() {
        playersHud.dispose();
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
    }

    protected void setupViewport(int width, int height) {
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;
        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();
        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth /
                VIEWPORT.virtualHeight);
        //update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >=
                VIEWPORT.aspectRatio) {
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight *
                    (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth *
                    (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }
        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" +
                VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" +
                VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")"
        );
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" +
                VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")"
        );
    }

    @Override
    public void hide() {

    }

    public PlayersHud getPlayersHud() {
        return playersHud;
    }

    public boolean isDrawMap() {
        return drawMap;
    }

    public void setDrawMap(final boolean drawMap) {
        this.drawMap = drawMap;
    }

    public Map<String, TiledMapWindow> getEntityWindows() {
        return entityWindows;
    }

    public void setEntityWindows(final Map<String, TiledMapWindow> entityWindows) {
        this.entityWindows = entityWindows;
    }
}
