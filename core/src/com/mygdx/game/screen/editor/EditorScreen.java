package com.mygdx.game.screen.editor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Simulator;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.screen.GuiUtils;
import com.mygdx.game.screen.strategymap.MapScreen;
import com.mygdx.game.screen.strategymap.OrthogonalTiledMapRendererWithSprites;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.world.MaskType;
import com.mygdx.game.world.generator.world.WorldFactory;
import com.mygdx.game.world.params.WorldCreatorInputParameters;

import java.util.Arrays;
import java.util.List;

public class EditorScreen extends MapScreen implements Screen {

    private final Slider edgeFadeSlider;
    private final Slider cellularMaskFillSlider;
    private final Slider cellularMaskSmoothnessSlider;
    private final Slider continentsSlider;
    private Stage stage;
    private World world;
    private Slider civilizationsCountSlider;
    private Slider widthSlider;
    private Slider heightSlider;
    private Slider waterSlider;
    private Slider mountainSlider;
    private Slider hillSlider;
    private Slider lowlandsSlider;
    private Slider octaveSlider;
    private Slider roughnessSlider;
    private Slider scaleSlider;
    private SelectBox<MaskType> maskTypeSelectBox;
    private SelectBox<MaskType> maskTypeTempSelectBox;

    private InputMultiplexer inputMultiplexer;

    Table worldSettingsContainer;
    Table mapContainer;
    Table table;

    public EditorScreen(final Simulator parent) {
        super(parent);

        stage = new Stage(new ScreenViewport());

        // Create a table that fills the screen. Everything else will go inside this table.
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label worldGeneratorLabel = new Label("World generator", parent.getSkin());
        worldGeneratorLabel.setFontScale(1.5f);

        //VisUI.load(parent.getSkin());

        mapContainer = new Table();

        ImageTextButton back = GuiUtils.createRPGButton("Back", Utility.assetManager);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.MENU);
            }
        });

        TextButton generateWorldButton = createGenerateWorldButton(parent);
        TextButton generateTandomWorldButton = createGenerateRandomWorldButton(parent);

        HorizontalGroup presetsGroup = new HorizontalGroup();
        presetsGroup.addActor(new Label("Presets: ", parent.getSkin()));
        TextButton smallButton = new TextButton("SMALL", parent.getSkin());
        smallButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                setStartingValues(50, 50, 50, 50, 50, 50, 5, 0.3f, 0.03f, 40, 3, 0.3f);
            }
        });
        TextButton medButton = new TextButton("MEDIUM", parent.getSkin());
        medButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                setStartingValues(100, 100, 50, 50, 50, 50, 5, 0.3f, 0.03f, 40, 3, 0.3f);
            }
        });
        TextButton bigButton = new TextButton("BIG", parent.getSkin());
        bigButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                setStartingValues(150, 150, 50, 50, 50, 50, 5, 0.3f, 0.03f, 40, 3, 0.3f);
            }
        });
        presetsGroup.addActor(smallButton);
        presetsGroup.addActor(medButton);
        presetsGroup.addActor(bigButton);
        widthSlider = createSlider(50, 1000, 1);
        widthSlider.setName(String.valueOf(widthSlider.getValue()));
        HorizontalGroup widthGroup = createSliderGroup(widthSlider, "Width: ");
        heightSlider = createSlider(50, 1000, 1);
        HorizontalGroup heightSliderGroup = createSliderGroup(heightSlider, "Height: ");
        continentsSlider = createSlider(1, 5, 1);
        HorizontalGroup continentsSliderGroup = createSliderGroup(continentsSlider, "Continents: ");
        waterSlider = createSlider(10, 100, 1);
        HorizontalGroup waterSliderGroup = createSliderGroup(waterSlider, "Water %: ");
        hillSlider = createSlider(10, 100, 1);
        HorizontalGroup hillSliderGroup = createSliderGroup(hillSlider, "Hills %: ");
        mountainSlider = createSlider(10, 100, 1);
        HorizontalGroup mountainSliderGroup = createSliderGroup(mountainSlider, "Mountains %: ");
        lowlandsSlider = createSlider(10, 100, 1);
        HorizontalGroup lowlandsSliderGroup = createSliderGroup(lowlandsSlider, "Lowlands %: ");
        octaveSlider = createSlider(3, 10, 1);
        HorizontalGroup octaveSliderGroup = createSliderGroup(octaveSlider, "Octaves: ");
        roughnessSlider = createSlider(0.1f, 1f, 0.1f);
        HorizontalGroup roughnessSliderGroup = createSliderGroup(roughnessSlider, "Rougness: ");
        scaleSlider = createSlider(0.01f, 0.1f, 0.01f);
        HorizontalGroup scaleSliderGroup = createSliderGroup(scaleSlider, "Scale: ");
        civilizationsCountSlider = createSlider(1f, 10f, 1f);
        HorizontalGroup civilizationsCountSliderGroup = createSliderGroup(civilizationsCountSlider, "Civs count: ");
        maskTypeSelectBox = new SelectBox<>(parent.getSkin());
        maskTypeSelectBox.setItems(MaskType.values());
        maskTypeSelectBox.setSelected(MaskType.CELLULAR);
        maskTypeTempSelectBox = new SelectBox<MaskType>(parent.getSkin());
        maskTypeTempSelectBox.setItems(MaskType.values());
        maskTypeTempSelectBox.setSelected(MaskType.REAL_TEMPERATURE_ONE_POLE);
        edgeFadeSlider = createSlider(0.05f, 0.6f, 0.05f);
        HorizontalGroup edgeFadeSliderGroup = createSliderGroup(edgeFadeSlider, "Edge fade: ");
        cellularMaskFillSlider = createSlider(25, 60, 1);
        HorizontalGroup cellularMaskFillSliderGroup = createSliderGroup(cellularMaskFillSlider, "Cellular mask fill: ");
        cellularMaskSmoothnessSlider = createSlider(1, 10, 2);
        HorizontalGroup cellularMaskSmoothnessSliderGroup = createSliderGroup(cellularMaskSmoothnessSlider, "Cellular mask smoothness: ");


        TextButton worldButton = new TextButton("World", parent.getSkin());
        worldButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                if (world != null) {
                    mapContainer.clear();
                    createMapImage();
                }
            }
        });
        TextButton raceButton = new TextButton("Races", parent.getSkin());
        raceButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                camera = null;
                mapRenderer = null;
                if (world != null) {
                    createRaceContainer(world);
                }
            }
        });
        Table buttonsContainer = new Table();
        buttonsContainer.add(worldButton);
        buttonsContainer.add(raceButton).grow();

        setStartingValues(30, 30, 50, 50, 50, 50, 5, 0.3f, 0.03f, 40, 3, 0.3f);

        worldSettingsContainer = new Table();
        worldSettingsContainer.defaults().expand();
        worldSettingsContainer.left();
        worldSettingsContainer.add(generateWorldButton).row();
        worldSettingsContainer.add(generateTandomWorldButton).row();
        worldSettingsContainer.add(presetsGroup).row();
        worldSettingsContainer.add(widthGroup).row();
        worldSettingsContainer.add(heightSliderGroup).row();
        worldSettingsContainer.add(continentsSliderGroup).row();
        worldSettingsContainer.add(waterSliderGroup).row();
        worldSettingsContainer.add(hillSliderGroup).row();
        worldSettingsContainer.add(mountainSliderGroup).row();
        worldSettingsContainer.add(lowlandsSliderGroup).row();
        worldSettingsContainer.add(octaveSliderGroup).row();
        worldSettingsContainer.add(roughnessSliderGroup).row();
        worldSettingsContainer.add(scaleSliderGroup).row();
        worldSettingsContainer.add(civilizationsCountSliderGroup).row();
        worldSettingsContainer.add(maskTypeSelectBox).row();
        worldSettingsContainer.add(maskTypeTempSelectBox).row();
        worldSettingsContainer.add(edgeFadeSliderGroup).row();
        worldSettingsContainer.add(cellularMaskFillSliderGroup).row();
        worldSettingsContainer.add(cellularMaskSmoothnessSliderGroup);
        mapContainer.setFillParent(true);

        table.defaults().pad(4).center();
        table.add(worldGeneratorLabel).colspan(2).center();
        table.row();
        table.add(worldSettingsContainer).width(300).left().growY().fillY();
        table.add(mapContainer).fill().grow();
        table.row().pad(10, 0, 10, 0);
        table.add(buttonsContainer);
        table.add(back).right();

        stage.setDebugAll(true);
        inputMultiplexer = new InputMultiplexer();
    }

    private Table createRaceContainer(final World world) {
        final List<RaceType> races = world.getRaces();
        RaceType race = races.get(0);
        final Table table = mapContainer;
        table.clear();
        table.setFillParent(true);
        table.pad(3);
        table.defaults().grow().expand().left();
        table.add(new Label("Races", parent.getSkin())).top().center().row();
        table.add(new Label(race.getName(), parent.getSkin())).row();
        table.add(new Label(race.getAlignment().name(), parent.getSkin())).row();
        final TextButton previous = new TextButton("<", parent.getSkin());
        table.add(previous).bottom().left();
        final TextButton next = new TextButton(">", parent.getSkin());
        next.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                final int i = races.indexOf(race);
                updateRace(races.get(i < 0 ? races.size() - 1 : i));
            }
        });
        table.add(next).right();
        table.pack();
        return table;
    }

    private void updateRace(final RaceType raceType) {

    }

    private void setStartingValues(final int w, final int h, final int water, final int lowlands, final int i,
                                   final int mountains, final int octaves, final float roughness, final float scale, final float cellularMaskFill, final float cellularMaskSmoothness, final float edgeFade) {
        widthSlider.setValue(w);
        heightSlider.setValue(h);
        waterSlider.setValue(water);
        lowlandsSlider.setValue(lowlands);
        hillSlider.setValue(i);
        mountainSlider.setValue(mountains);
        octaveSlider.setValue(octaves);
        roughnessSlider.setValue(roughness);
        scaleSlider.setValue(scale);
        edgeFadeSlider.setValue(edgeFade);
        cellularMaskFillSlider.setValue(cellularMaskFill);
        cellularMaskSmoothnessSlider.setValue(cellularMaskSmoothness);

    }

    private HorizontalGroup createSliderGroup(Widget widget, final String name) {
        HorizontalGroup group = new HorizontalGroup();
        final Label label = new Label(name, parent.getSkin());

        group.addActor(label);
        group.addActor(widget);

        return group;
    }

    private Slider createSlider(float min, float max, float stepSize) {
        return new Slider(min, max, stepSize, false, parent.getSkin());
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (mapRenderer != null) {
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
    }

    private TextButton createGenerateRandomWorldButton(final Simulator parent) {
        TextButton generateGame = new TextButton("Generate random", parent.getSkin());
        generateGame.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                world = WorldFactory.createRandomWorld();
                createMapImage();
            }
        });
        return generateGame;
    }

    private TextButton createGenerateWorldButton(final Simulator parent) {
        TextButton generateGame = new TextButton("Generate", parent.getSkin());
        generateGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WorldCreatorInputParameters parameters = new WorldCreatorInputParameters();
                final com.mygdx.game.world.entity.game.Game game = new Game();
                game.setName("testGame");
                game.setCode("game1");
                parameters.setGame(game);
                final Universe universe = new Universe();
                game.setUniverse(universe);
                parameters.setWidth((int) widthSlider.getValue());
                parameters.setHeight((int) heightSlider.getValue());
                parameters.setContinentsCount((int) continentsSlider.getValue());
                parameters.setCivilizationsCount((int) civilizationsCountSlider.getValue());
                parameters.getTerrainComposition().setWater(waterSlider.getValue());
                parameters.getTerrainComposition().setHills(hillSlider.getValue());
                parameters.getTerrainComposition().setMountains(mountainSlider.getValue());
                parameters.getTerrainComposition().setLowlands(lowlandsSlider.getValue());
                parameters.getLayersCreationInputParams().setOctaves((int) octaveSlider.getValue());
                parameters.getLayersCreationInputParams().setRougness(roughnessSlider.getValue());
                parameters.getLayersCreationInputParams().setScale(scaleSlider.getValue());
                parameters.getMaskCreatorInputParameters().setMaskType(maskTypeSelectBox.getSelected());
                parameters.getMaskCreatorInputParameters().setTemperatureMaskType(maskTypeTempSelectBox.getSelected());
                parameters.getMaskCreatorInputParameters().setEdgeFade(edgeFadeSlider.getValue());
                parameters.getMaskCreatorInputParameters().setFillPercentCellularMask((int) cellularMaskFillSlider.getValue());
                parameters.getMaskCreatorInputParameters().setSmoothFactorCellularMask((int) cellularMaskSmoothnessSlider.getValue());
                parameters.getMapImageParameters().setMapImageType(null);

                world = WorldFactory.createWorld(parameters);
                map = world.getTiledMap();
                createMapImage();
            }
        });
        return generateGame;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.draw();
        if (mapRenderer != null) {
            createMapImage();
        }
    }

    private void createMapImage() {
        final float mapMaxWidth = table.getCell(mapContainer).getActorWidth();
        final float mapMaxHeight = table.getCell(mapContainer).getActorHeight();
        int mapWidth = world.getWidth() * World.getPixelSize();
        int mapHeight = world.getHeight() * World.getPixelSize();
        float scaleH = mapMaxHeight / mapHeight;
        float scaleW = mapMaxWidth / mapWidth;

        mapRenderer = new OrthogonalTiledMapRendererWithSprites(world.getTiledMap(), Math.min(scaleH, scaleW), getExploredLayer(), world);
        camera = new OrthographicCamera(mapContainer.getWidth(), mapContainer.getHeight());
        camera.setToOrtho(false);
        camera.translate(-mapContainer.getX(), -mapContainer.getY());
        camera.update();
        mapRenderer.setView(camera);
    }

    private int[][] getExploredLayer() {
        int[][] explorationLayer = new int[world.getWidth()][world.getHeight()];

        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                explorationLayer[i][j] = 1;
            }
        }

        return explorationLayer;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return false;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
