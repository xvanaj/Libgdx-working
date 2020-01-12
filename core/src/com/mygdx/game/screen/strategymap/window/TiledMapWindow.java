package com.mygdx.game.screen.strategymap.window;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.screen.strategymap.OrthogonalTiledMapRendererWithSprites;
import com.mygdx.game.utils.WindowWithTopRightCornerCloseButton;

public class TiledMapWindow extends Window {

    OrthogonalTiledMapRendererWithSprites renderer;
    OrthographicCamera camera;
    Stage stage = new Stage();

    public TiledMapWindow(Skin skin, OrthogonalTiledMapRendererWithSprites renderer) {
        super("", skin);
        this.renderer = renderer;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 100, 100);
        camera.update();
        camera.position.x = 100;
        camera.position.y = 100;

        stage.getViewport().setCamera(camera);
        renderer.setView(camera);
        renderer.render();
        stage.getBatch().setProjectionMatrix(camera.combined);


        setPosition(200,200);
        setSize(200,200);

        setVisible(true);
    }

    public void render(){

        camera.update();
        renderer.setView(camera);
        renderer.render();
    }


}
