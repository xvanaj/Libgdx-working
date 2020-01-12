package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Simulator;
import com.mygdx.game.asset.Utility;

public class LoadingScreen implements Screen {

    private Simulator parent;
    private Label loadingLabel;
    private Stage stage;
    private ProgressBar progressBar;
    private int currentLoad = 0;
    private float percent = 0;
    private int width,height = 0;

    public LoadingScreen(Simulator parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);

        loadingLabel = new Label("Loading", parent.getSkin());
        progressBar = new ProgressBar(0, 1, 0.2f, false, parent.getSkin());

        stage.addActor(loadingLabel);
        stage.addActor(progressBar);
    }

    @Override
    public void render(float delta) {
        if (Utility.assetManager.update()) { // Load some, will return true if done loading
            currentLoad += 1;
            percent = percent + 1/7;

            switch (currentLoad) {
                case 1:
                    Utility.assetManager.loadFonts();
                    loadingLabel.setText("Loading Fonts");
                    break;
                case 2:
                    Utility.assetManager.loadParticleEffects();
                    loadingLabel.setText("Loading Particle Effects");
                    break;
                case 3:
                    Utility.assetManager.loadSounds();
                    loadingLabel.setText("Loading Sounds");
                    break;
                case 4:
                    Utility.assetManager.loadSkin();
                    loadingLabel.setText("Loading Skins");
                    break;
                case 5:
                    Utility.assetManager.loadImages();
                    loadingLabel.setText("Loading Images");
                    break;
                case 6:
                    Utility.assetManager.loadTextFiles();
                    loadingLabel.setText("Loading text files");
                    break;
                case 7:
                    Utility.assetManager.loadTextures();
                    loadingLabel.setText("Loading textures");
                    break;
                case 8:
                    /*Utility.assetManager.loadMaps();*/
                    loadingLabel.setText("Loading maps");
                    break;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (currentLoad > 8) {
                //Utility.assetManager.getAllTextures();
                currentLoad = 8;
                loadingLabel.setText("All loaded");
                percent = 1;                            // set bar to full
                parent.changeScreen(Simulator.ScreenType.MENU);   //changes screen
            }
        }

        progressBar.setValue(percent);
        loadingLabel.pack();
        loadingLabel.setPosition(width / 2 - loadingLabel.getWidth()/2, height / 2);
        progressBar.setPosition(width / 2 - progressBar.getWidth()/2, height / 2 + progressBar.getHeight());

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        loadingLabel.setPosition(width / 2 - loadingLabel.getGlyphLayout().width/2, height / 2);
        progressBar.setPosition(width / 2 - progressBar.getWidth()/2, height / 2 + progressBar.getHeight());
        this.width = width;
        this.height = height;
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
