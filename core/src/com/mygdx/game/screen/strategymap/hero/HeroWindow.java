package com.mygdx.game.screen.strategymap.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Simulator;
import com.mygdx.game.world.entity.being.hero.Hero;

public class HeroWindow extends Window {

    private final Table primaryTable;
    private Table secondaryTable, biographyTable;
    private Hero hero;
    private TextButton primarySkillsButton, secondarySkillsButton, biographyButton;

    public HeroWindow(final Simulator parent, final Hero hero, final Skin skin) {
        super("Hero", skin);
        this.hero = hero;
        setMovable(true);
        //addCloseButton();
        debugAll();
        pad(2,2,2,2);

        primaryTable = new PrimarySkillsTable(skin, hero);;
        secondaryTable = new SecondarySkillsTable(skin, hero);
        biographyTable = new BiographyTable(skin, hero, parent);

        biographyButton = new TextButton("History", skin);
        biographyButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                updateUI(biographyTable);
            }
        });

        primarySkillsButton = new TextButton("Primary skills", skin);
        primarySkillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                updateUI(primaryTable);
            }
        });
        secondarySkillsButton = new TextButton("Sec skills", skin);
        secondarySkillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                updateUI(secondaryTable);
            }
        });

        updateUI(primaryTable);
    }

    private void updateUI(final Table table) {
        clear();
        add(primarySkillsButton);
        add(secondarySkillsButton);
        add(biographyButton).row();
        add(table).colspan(3).expand().grow().fill();
        table.pack();
        pack();
    }

    private float getMaxTableWidth() {
        return Math.max(Math.max(primaryTable.getWidth(), secondaryTable.getWidth()), biographyTable.getWidth());
    }

    private float getMaxTableHeight() {
        return Math.max(Math.max(primaryTable.getHeight(), secondaryTable.getHeight()), biographyTable.getHeight());
    }

    private static final ImageButton.ImageButtonStyle closeButtonStyle;

    static {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("core/assets/windows.pack"));

        closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("window-1-close-button"));

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
