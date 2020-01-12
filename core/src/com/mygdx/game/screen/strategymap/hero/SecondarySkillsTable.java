package com.mygdx.game.screen.strategymap.hero;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.world.entity.being.hero.AttributeSecondary;
import com.mygdx.game.world.entity.being.hero.Hero;

public class SecondarySkillsTable extends Table {

    private Hero hero;

    public SecondarySkillsTable(final Skin skin, final Hero hero) {
        super(skin);
        this.hero = hero;
        defaults().left();

        for (AttributeSecondary att : AttributeSecondary.values()) {
            add(new Label(att.name() + " " + hero.getSecondaryAttributes().get(att).getBase(), skin)).row();
        }

        pack();
    }

}
