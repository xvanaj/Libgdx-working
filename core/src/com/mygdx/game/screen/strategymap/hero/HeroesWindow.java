package com.mygdx.game.screen.strategymap.hero;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Simulator;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.screen.GuiUtils;
import com.mygdx.game.world.entity.being.hero.Hero;

public class HeroesWindow extends Window {

    private Window heroWindow;

    public HeroesWindow(final Simulator parent, Skin skin) {
        super("Heroes", skin);
        align(Align.top);

        for (Hero hero : parent.getGame().getHumanPlayer().getHeroes()) {

            ImageTextButton imageTextButton = GuiUtils.createHeroButton(hero, Utility.assetManager);
            imageTextButton.addListener(new ClickListener(){

                @Override
                public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                    //parent.getStrategyMapScreen().getEntityWindows().put(hero.getCode(), new TiledMapWindow(skin, parent.getStrategyMapScreen().getMapRenderer()));

                    if (heroWindow == null) {
                        heroWindow = new HeroWindow(parent, hero, skin);
                        heroWindow.setPosition(200,200);
                        parent.getStrategyMapScreen().getPlayersHud().getStage().addActor(heroWindow);
                    } else {
                        heroWindow.setVisible(true);
                        //update hero table to current hero
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
            //imageTextButton.getImageCell().size(64,64);
            //imageTextButton.getImage().setAlign(Align.center);
            imageTextButton.getLabelCell().center();

            WidgetGroup widgetGroup = new WidgetGroup();
           // table.add(imageTextButton).left().padTop(1).row();
            widgetGroup.addActor(imageTextButton);

            add(imageTextButton).size(64,64).left().top().row();
        }

        pack();
    }

}