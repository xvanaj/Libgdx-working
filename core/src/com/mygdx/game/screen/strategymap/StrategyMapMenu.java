package com.mygdx.game.screen.strategymap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Simulator;
import com.strongjoshua.console.GUIConsole;

public class StrategyMapMenu extends Dialog {

    private Simulator parent;

    public StrategyMapMenu(final String title, final Skin skin, Simulator parent) {
        this(title, skin);
        this.parent = parent;
    }

    private StrategyMapMenu(final String title, final Skin skin) {
        super(title, skin);

        this.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 100);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        this.addActor(table);

        TextButton mainMenu = new TextButton("Main menu", skin);
        mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.MENU);
            }
        });
        TextButton endButton = new TextButton("End game", skin);
        endButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Simulator.ScreenType.ENDGAME);
            }
        });
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

        table.add(mainMenu).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(endButton).fillX().uniformX();
        table.row();
        table.add(cancelButton).fillX().uniformX();

        this.getContentTable().defaults().pad(10);
        this.getContentTable().add(table);
    }
}
