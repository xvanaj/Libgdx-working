package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.game.Simulator;

public class EndScreen implements Screen {

    private Simulator parent;

    public EndScreen(Simulator parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
