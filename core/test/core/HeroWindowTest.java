package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.gdxutils.GdxTest;
import com.mygdx.game.screen.strategymap.hero.HeroWindow;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.generator.being.HeroFactory;

public class HeroWindowTest extends GdxTest {

    public static void main(String[] args) {
        final HeroWindowTest colorTest = new HeroWindowTest();
        colorTest.create();
    }

    @Override
    public void create () {
        super.create();
        Skin skin = new Skin(Gdx.files.internal("core/assets/skin/uiskin/uiskin.json"));
        skin.add("default", new BitmapFont());

        assetManager.load("core/assets/texture/button/BTNearth.png", Texture.class);
        assetManager.finishLoading();
        stage.setDebugAll(true);

        Hero hero = HeroFactory.createRandomHero();
        stage.addActor(new HeroWindow(null, hero, skin));

    }
}
