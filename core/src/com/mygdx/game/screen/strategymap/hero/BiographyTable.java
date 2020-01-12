package com.mygdx.game.screen.strategymap.hero;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Simulator;
import com.mygdx.game.utils.TimeUtils;
import com.mygdx.game.world.entity.being.BiographyEvent;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.universe.world.World;

public class BiographyTable extends Table {

    public static final String DELIMITER = ": ";
    private Hero hero;

    public BiographyTable(final Skin skin, final Hero hero, final Simulator parent) {
        super(skin);
        this.hero = hero;
        defaults().left();
        align(Align.left);


        Array array = new Array();
        List list = new List(skin);
        for (BiographyEvent bio : hero.getHistory()) {
            final String time =
                    parent == null
                            ? String.valueOf(bio.getMinute())
                            : TimeUtils.minutesToYearAndDay((World) parent.getGame().getGameEntity(bio.getWorldCode()), bio.getMinute());
            final Label label = new Label(time + DELIMITER + bio.getText(), skin);
            label.setWrap(true);
            array.add(time + DELIMITER + bio.getText());

        }
        list.setItems(array);
        add(list).row();

        pack();
    }

}
