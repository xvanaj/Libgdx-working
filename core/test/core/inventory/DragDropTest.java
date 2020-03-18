package core.inventory;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Settings;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.generator.game.GameFactory;

public class DragDropTest extends InputAdapter implements ApplicationListener {

    private Stage stage;

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 768;


        final DragDropTest colorTest = new DragDropTest();
        final LwjglApplication lwjglApplication = new LwjglApplication(colorTest, config);
        Gdx.app = lwjglApplication;
        Gdx.app.setLogLevel(Application.LOG_INFO);


    }

    @Override
    public void create() {
        Utility.assetManager.loadAll();
        Game game = GameFactory.createGame();
        Hero hero = game.getHumanPlayer().getHeroes().get(0);

        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(Settings.debug);
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin/uiskin.json"));
        InventoryWindow inventoryWindow = new InventoryWindow("inventory", Utility.STATUSUI_SKIN, hero);

        stage.addActor(inventoryWindow);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
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
