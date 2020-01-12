package com.mygdx.game.screen.universescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Simulator;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.generator.game.GameFactory;

public class CreateUniverseScreen implements Screen {

    private Simulator parent;
    private Stage stage;
    List<World> worldsSelectBox;
    Table imageContainer;
    Image mapImage;


    public CreateUniverseScreen(final Simulator parent) {
        this.parent = parent;

        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        parent.setGame(GameFactory.createGame(Difficulty.MEDIUM));

        FitViewport viewp = new FitViewport(width, height); // change this to your needed viewport
        stage = new Stage(viewp);

        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        Label universeLabel = createLabel(parent.getGame().getUniverse().getCode(), parent.getSkin(), 0, 0);
        universeLabel.setFontScale(2);

        worldsSelectBox = createSelectWordSelectBox(parent);
        stage.addActor(worldsSelectBox);

        TextButton generateGame = createGenerateUniverseButton(parent, worldsSelectBox);
        Table buttonsContainer = new Table();
        buttonsContainer.add(generateGame);
        buttonsContainer.row();
        buttonsContainer.add(worldsSelectBox);
        buttonsContainer.debug();
        TextButton newGame = createChangeScreenButton(parent, "Enter world", Simulator.ScreenType.EDITOR);

        TextButton backButton = createChangeScreenButton(parent, "Back", Simulator.ScreenType.MENU);

        imageContainer = new Table();


        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("core/assets/images/universe.png")),
                3, 3, 3, 3);
        NinePatchDrawable background = new NinePatchDrawable(patch);

        table.debug();
        table.setBackground(background);
        table.defaults().expand();
        table.center();
        table.pad(4);
        table.add(universeLabel).grow().colspan(2).center();
        table.row();
        table.add(buttonsContainer).top();
        table.add(imageContainer).size(360, 360);
        table.row().pad(10, 0, 10, 0);
        table.add(newGame);
        table.add(backButton);

        createMapImage(worldsSelectBox);
    }

    private List<World> createSelectWordSelectBox(final Simulator parent) {
        List<World> worldsSelectBox = new List<World>(parent.getSkin());

        worldsSelectBox.setItems(new Array(parent.getGame().getUniverse().getWorlds().values().toArray()));
        worldsSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createMapImage(worldsSelectBox);
            }
        });
        return worldsSelectBox;
    }

    private void createMapImage(final List<World> worldsSelectBox) {
        if (worldsSelectBox.getSelected().getImageFileName() == null) {
            worldsSelectBox.getSelected().setImageFileName(worldsSelectBox.getSelected().getCode());
        }

        Texture texture = new Texture(Gdx.files.internal("core/assets/maps/" + worldsSelectBox.getSelected().getImageFileName() + ".png"));
        mapImage = new Image(texture);
        mapImage.setScaling(Scaling.fit);
        mapImage.setSize(300, 300);
        imageContainer.clear();
        imageContainer.add(mapImage).fill();
        stage.draw();
    }

    private TextButton createChangeScreenButton(final Simulator parent, final String s, final Simulator.ScreenType newGame2) {
        TextButton newGame = new TextButton(s, parent.getSkin());
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(newGame2);
            }
        });
        return newGame;
    }

    private TextButton createGenerateUniverseButton(final Simulator parent, final List<World> worldsSelectBox) {
        TextButton generateGame = new TextButton("Generate", parent.getSkin());
        generateGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setGame(GameFactory.createGame(Difficulty.MEDIUM));
                worldsSelectBox.setItems(new Array(parent.getGame().getUniverse().getWorlds().values().toArray()));
            }
        });
        return generateGame;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        //worldsSelectBox.getScrollPane().act(delta);
        stage.getViewport().apply();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        mapImage.setFillParent(true);
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
    public void dispose() {
        stage.dispose();
    }

    private Label createLabel(String text, final Skin skin, final int w, final int h) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/Anglodavek-a55E.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;

        Label label = new Label(text, labelStyle);

        return label;
    }
}
