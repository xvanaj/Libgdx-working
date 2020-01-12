package com.mygdx.game.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.entity.being.hero.Hero;

public class GuiUtils {

    public static ImageButton createImageButton(final String imageUp, final String imageDown) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();

        //example: "core/assets/images/button/button.png"
        final Texture texture = Utility.assetManager.get(imageUp, Texture.class);
        final Texture texture2 = Utility.assetManager.get(imageDown, Texture.class);

        final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(texture));
        final TextureRegionDrawable image2 = new TextureRegionDrawable(new TextureRegion(texture2));

        style.imageUp = image;
        style.imageDown = image2;

        ImageButton button = new ImageButton(style);

        return button;
    }

    public static ImageTextButton createRPGButton(final String text, AssetManager assetManager) {
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();

        final Texture texture = assetManager.get("core/assets/images/button/button.png", Texture.class);
        final Texture texture2 = assetManager.get("core/assets/images/button/button-disabled.png", Texture.class);

        final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(texture));
        final TextureRegionDrawable image2 = new TextureRegionDrawable(new TextureRegion(texture2));

        style.imageUp = image;
        style.imageDown = image2;
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;

        ImageTextButton button = new ImageTextButton(text, style);
        button.getLabelCell().padLeft(-200);
        button.getImageCell().width(200);

        return button;
    }

    public static ImageTextButton createHeroButton(final Hero hero, GameAssetManager assetManager) {
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();

        Texture texture = null;
        try {
            texture = assetManager.get("core/assets/portrait/" + hero.getPortraitName(), Texture.class);
        } catch (GdxRuntimeException g)  {
            System.out.println(hero.getPortraitName());
        }

        final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(texture));

        style.imageUp = image;
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;


        ImageTextButton button = new ImageTextButton(hero.getName(), style);
        button.setBounds(0,0,200, 200);
        button.getLabelCell().padLeft(-200);
        button.getImageCell().width(200);

        return button;
    }
}
