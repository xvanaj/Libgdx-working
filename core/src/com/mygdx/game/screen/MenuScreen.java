package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Simulator;
import com.mygdx.game.asset.Utility;

public class MenuScreen implements Screen {

    private Simulator parent;
    private Stage stage;

    public MenuScreen(final Simulator parent) {
        this.parent = parent;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        ImageButton continueGame = new ImageButton(parent.getSkin());
        continueGame.add(new Label("Continue game", parent.getSkin()));
        continueGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.STRATEGY_MAP);
            }
        });

        ImageButton newGame = new ImageButton(parent.getSkin());
        newGame.add(new Label("World creator", parent.getSkin()));
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.EDITOR);
            }
        });

        ImageButton editor = new ImageButton(parent.getSkin());
        editor.add(new Label("Universe creator", parent.getSkin()));
        editor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.CREATE_UNIVERSE);
            }
        });

        ImageButton preferences = new ImageButton(parent.getSkin());
        preferences.add(new Label("Preferences", parent.getSkin()));
        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.PREFERENCES);
            }
        });

        ImageButton exit = new ImageButton(parent.getSkin());
        exit.add(new Label("Exit", parent.getSkin()));
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        table.defaults().pad(5);
        table.add(continueGame).fillX().uniformX();
        table.row();
        table.add(newGame).fillX().uniformX();
        table.row();
        table.add(editor).fillX().uniformX();
        table.row();
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        parent.changeScreen(Simulator.ScreenType.STRATEGY_MAP);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        //stage.getBatch().draw(Utility.assetManager.get("core/assets/texture/background.jpg", Texture.class), 0, 0, stage.getWidth(), stage.getHeight());
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
}
