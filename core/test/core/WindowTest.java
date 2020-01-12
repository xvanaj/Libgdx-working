package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.gdxutils.GdxTest;
import com.mygdx.game.world.entity.spell.Spell;
import com.mygdx.game.world.entity.spell.SpellFactory;

public class WindowTest extends GdxTest {

    public static void main(String[] args) {
        final WindowTest colorTest = new WindowTest();
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

        Table root = new Table();
        root.setColor(Color.WHITE);

        stage.addActor(root);
        root.setFillParent(true);

        Table column1 = new Table(skin);
        root.setHeight(300);
        column1.setColor(Color.WHITE);
        column1.left();

        for (int i = 0; i < 5; i++) {
            final Spell spell = SpellFactory.generateRandomSpell();
            column1.add(createSpellActor(skin, spell)).row();
        }

        ScrollPane scrollPane=new ScrollPane(column1, skin);
        scrollPane.setForceScroll(false, true);
        //scrollPane.setPosition(50, 50);

        root.add(scrollPane);


    }

    private Table createSpellActor(Skin skin, final Spell spell) {

        Table table = new Table(skin);
        table.defaults().left().grow();

        assetManager.load("core/assets/texture/icon/spell/" + spell.getIconName() + ".png", Texture.class);
        assetManager.finishLoading();
        final Texture texture = assetManager.get("core/assets/texture/icon/spell/" + spell.getIconName() + ".png", Texture.class);

        table.add(spell.getName(), "default", spell.getSpellTemplate().getSpellRarity().getColor());
        table.add("100 gp", "default", Color.BLACK).right().row();

        Table row = new Table(skin);
        row.add(new Image(texture)).left().height(64).width(64);
        final Label label = new Label("description: some long descrrsome long descrrsome long descrrsome long descrrsome long descrr" +
                "long descrrsome long descrrlong descrrsome long descrrlong descrrsome long descrrlong descrrsome long descrrlong descrrsome long descrr" +
                "long descrrsome long descrrlong descrrsome long descrrlong descrrsome long descrrlong descrrsome long descrr", skin);

        label.setWrap(true);
        row.add(label).width(200).top();

        table.add(row).colspan(2);
        return table;
    }


}
