package com.mygdx.game.screen.strategymap.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.world.entity.being.hero.Hero;

public class PrimarySkillsTable extends Table {

    private Hero hero;

    Label heroLabel;
    Label raceLabel;
    Label hpLabel, mpLabel, fatigueLabel, sanityLabel;
    Label strLabel, agiLabel, endLabel, intLabel;
    ProgressBar strBar, agiBar, endBar, intBar;
    ProgressBar healthBar, manaBar, fatigueBar, sanityBar;

    public PrimarySkillsTable(final Skin skin, final Hero hero) {
        super(skin);
        this.hero = hero;
        
        heroLabel = createLabel(skin, 100, 20);
        raceLabel = createLabel(skin, 100, 20);
        hpLabel = createLabel(skin, 100, 20);
        mpLabel = createLabel(skin, 100, 20);
        fatigueLabel = createLabel(skin, 100, 20);
        sanityLabel = createLabel(skin, 100, 20);

        strLabel = createLabel(skin, 100, 20);
        agiLabel = createLabel(skin, 100, 20);
        endLabel = createLabel(skin, 100, 20);
        intLabel = createLabel(skin, 100, 20);

        healthBar = createProgressBar(Color.RED, Color.GREEN);
        manaBar = createProgressBar(Color.RED, Color.CYAN);
        fatigueBar = createProgressBar(Color.RED, Color.BROWN);
        sanityBar = createProgressBar(Color.RED, Color.SKY);

        strBar = createProgressBar(Color.BLACK, Color.GOLDENROD);
        agiBar = createProgressBar(Color.BLACK, Color.GOLDENROD);
        endBar = createProgressBar(Color.BLACK, Color.GOLDENROD);
        intBar = createProgressBar(Color.BLACK, Color.GOLDENROD);

        defaults().left();
        add(heroLabel).padTop(5).row();
        add(raceLabel).row();
        add(createLabel("============", skin, 0, 0)).colspan(2).row();
        add(hpLabel).size(60, 20);
        add(healthBar).pad(2, 2, 2, 2).row();
        add(mpLabel).size(60, 20);
        add(manaBar).pad(2, 2, 2, 2).row();
        add(fatigueLabel).size(60, 20);
        add(fatigueBar).pad(2, 2, 2, 2).row();
        add(sanityLabel).size(60, 20);
        add(sanityBar).pad(2, 2, 2, 2).row();
        add(createLabel("============", skin, 0, 0)).colspan(2).row();
        add(strLabel).size(60, 20);
        add(strBar).pad(2, 2, 2, 2).row();
        add(agiLabel).size(60, 20);
        add(agiBar).pad(2, 2, 2, 2).row();
        add(endLabel).size(60, 20);
        add(endBar).pad(2, 2, 2, 2).row();
        add(intLabel).size(60, 20);
        add(intBar).pad(2, 2, 2, 2).row();
        add(createLabel("============", skin, 0, 0)).colspan(2);

        update();
        pack();
    }

    private void update() {
        heroLabel.setText(hero.getName());
        raceLabel.setText(hero.getRaceType().getName());

        hpLabel.setText("HP: ");
        mpLabel.setText("MP: ");
        fatigueLabel.setText("Fatigue: ");
        sanityLabel.setText("Sanity: ");

        strLabel.setText("STR: ");
        agiLabel.setText("AGI: ");
        endLabel.setText("END: ");
        intLabel.setText("INT: ");

        healthBar.setValue(hero.getHp() / hero.getHpMax());
        manaBar.setValue(hero.getMp() / hero.getMpMax());
        fatigueBar.setValue(100 - hero.getFatigue() / 100);
        sanityBar.setValue(hero.getSanity() / 100);

        strBar.setValue(hero.getStrength() / 100);
        agiBar.setValue(hero.getAgility() / 100);
        endBar.setValue(hero.getEndurance() / 100);
        intBar.setValue(hero.getIntelligence() / 100);

    }

    private Label createLabel(String text, final Skin skin, final int w, final int h) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/Anglodavek-a55E.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;

        Label label = new Label(text, skin);

        return label;
    }

    private Label createLabel(final Skin skin, final int i, final int i1) {
        return createLabel("",skin, i, i1);
    }

    private ProgressBar createProgressBar(Color negativeColor, Color positiveColor) {
        Pixmap pixmap = new Pixmap(60, 15, Pixmap.Format.RGBA8888);
        pixmap.setColor(negativeColor);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = drawable;

        pixmap = new Pixmap(0, 15, Pixmap.Format.RGBA8888);
        pixmap.setColor(positiveColor);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        progressBarStyle.knob = drawable;

        pixmap = new Pixmap(60, 15, Pixmap.Format.RGBA8888);
        pixmap.setColor(positiveColor);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        ProgressBar healthBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
        healthBar.setAnimateDuration(0.25f);
        healthBar.setBounds(10, 10, 60, 20);

        progressBarStyle.knobBefore = drawable;
        return healthBar;
    }
}
