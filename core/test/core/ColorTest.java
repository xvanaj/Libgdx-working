package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.gdxutils.GdxTest;
import gdx.ViewportTest1;

public class ColorTest extends GdxTest {


    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        final ColorTest colorTest = new ColorTest();
        final LwjglApplication lwjglApplication = new LwjglApplication(colorTest, config);
        Gdx.app = lwjglApplication;
    }

    @Override
    public void create () {
        super.create();
        Skin skin = new Skin(Gdx.files.internal("core/assets/skin/uiskin/uiskin.json"));
        skin.add("default", new BitmapFont());

        Table root = new Table();
        stage.addActor(root);
        root.setFillParent(true);

        Table column1 = new Table(skin);
        column1.add("WHITE", "default", Color.WHITE).row();
        column1.add("LIGHT_GRAY", "default", Color.LIGHT_GRAY).row();
        column1.add("GRAY", "default", Color.GRAY).row();
        column1.add("DARK_GRAY", "default", Color.DARK_GRAY).row();

        column1.add("BLUE", "default", Color.BLUE).row();
        column1.add("NAVY", "default", Color.NAVY).row();
        column1.add("ROYAL", "default", Color.ROYAL).row();
        column1.add("SLATE", "default", Color.SLATE).row();
        column1.add("SKY", "default", Color.SKY).row();
        column1.add("CYAN", "default", Color.CYAN).row();
        column1.add("TEAL", "default", Color.TEAL).row();

        Table column2 = new Table(skin);
        column2.add("GREEN", "default", Color.GREEN).row();
        column2.add("CHARTREUSE", "default", Color.CHARTREUSE).row();
        column2.add("LIME", "default", Color.LIME).row();
        column2.add("FOREST", "default", Color.FOREST).row();
        column2.add("OLIVE", "default", Color.OLIVE).row();

        column2.add("YELLOW", "default", Color.YELLOW).row();
        column2.add("GOLD", "default", Color.GOLD).row();
        column2.add("GOLDENROD", "default", Color.GOLDENROD).row();
        column2.add("ORANGE", "default", Color.ORANGE).row();

        column2.add("BROWN", "default", Color.BROWN).row();
        column2.add("TAN", "default", Color.TAN).row();
        column2.add("FIREBRICK", "default", Color.FIREBRICK).row();

        Table column3 = new Table(skin);
        column3.add("RED", "default", Color.RED).row();
        column3.add("SCARLET", "default", Color.SCARLET).row();
        column3.add("CORAL", "default", Color.CORAL).row();
        column3.add("SALMON", "default", Color.SALMON).row();
        column3.add("PINK", "default", Color.PINK).row();
        column3.add("MAGENTA", "default", Color.MAGENTA).row();

        column3.add("PURPLE", "default", Color.PURPLE).row();
        column3.add("VIOLET", "default", Color.VIOLET).row();
        column3.add("MAROON", "default", Color.MAROON).row();

        root.add(column1);
        root.add(column2);
        root.add(column3);
    }


}