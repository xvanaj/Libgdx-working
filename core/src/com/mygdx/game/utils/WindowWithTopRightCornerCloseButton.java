package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class WindowWithTopRightCornerCloseButton extends Window {

    private static final WindowStyle windowStyle;
    private static final ImageButton.ImageButtonStyle closeButtonStyle;

    static {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("core/assets/windows.pack"));

        windowStyle = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(textureAtlas.findRegion("window-1-background")));
        closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("window-1-close-button"));

    }

    public WindowWithTopRightCornerCloseButton() {
        super("", windowStyle);
        addCloseButton();
        setMovable(true);
        setKeepWithinStage(false);
        setClip(false);
        setTransform(true);
    }

    private void addCloseButton() {
        final Button closeButton = new ImageButton(closeButtonStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
        getTitleTable().add(closeButton).size(38, 38).padRight(10).padTop(0);
    }

}
