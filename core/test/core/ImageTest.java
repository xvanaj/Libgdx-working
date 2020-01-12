package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.gdxutils.GdxTest;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTest extends GdxTest {

    private boolean saved;

    private SpriteBatch spriteBatch;
    private Texture texture;
    private Texture texture2;
    private Texture texture3;
    private Texture texture4;
    private Pixmap pixmap1;
    private Pixmap pixmap2;
    private Pixmap pixmap;

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=32;
        config.height = 32;
        final ImageTest colorTest = new ImageTest();
        final LwjglApplication lwjglApplication = new LwjglApplication(colorTest, config);
        Gdx.app = lwjglApplication;
    }


    @Override
    public void create () {
        super.create();
        Skin skin = new Skin(Gdx.files.internal("core/assets/skin/uiskin/uiskin.json"));
        skin.add("default", new BitmapFont());

        texture = new Texture(Gdx.files.internal( "core/assets/maps/tilesets/tiles/tile054.png"));
        texture2 = new Texture(Gdx.files.internal( "core/assets/maps/tilesets/tiles/tile003.png"));
        texture3 = new Texture(Gdx.files.internal( "core/assets/maps/tilesets/tiles/tile049.png"));
        texture4 = new Texture(Gdx.files.internal( "core/assets/maps/tilesets/tiles/utumno/tile054.png"));

        pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap1 = new Pixmap(Gdx.files.internal( "core/assets/maps/tilesets/tiles/tile000.png"));
        pixmap2 = new Pixmap(Gdx.files.internal( "core/assets/maps/tilesets/tiles/utumno/tile054.png"));
        pixmap.setColor(1, 0, 1 ,0.0f);
        pixmap1.setColor(1, 0, 1 ,0.0f);
        pixmap.drawPixmap(pixmap1, 0, 0);
        pixmap.setColor(1, 0.5f, 0.5f, 0.0f);
        pixmap.drawPixmap(pixmap2, 0, 0);
        spriteBatch = new SpriteBatch();

        final Sprite sprite = new Sprite(texture);
        sprite.setColor(1, 0.5f,1,1 );

        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();


 /*       Table root = new Table();
        stage.addActor(root);
        root.setFillParent(true);*/
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 0.7f);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 32, 32, true);
        //fbo.begin();

        spriteBatch.setColor(1f, 0.5f, 1f, 1f);
        spriteBatch.begin();
        spriteBatch.draw(texture,0,0, 32, 32);
        spriteBatch.setColor(1f, 0f, 0f, 1f);
        spriteBatch.draw(texture4,0,0, 32, 32);
        //spriteBatch.draw(new Texture(pixmap), 0 , 0 );
        spriteBatch.end();

        //fbo.end();
        if (!saved) {
            saveScreenshot();
        } else {
            saved = true;
        }
    }

    public static void saveScreenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        // this loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
        for(int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        PixmapIO.writePNG(Gdx.files.external("libgdx2/screenshot.png"), pixmap);
        pixmap.dispose();
    }
}