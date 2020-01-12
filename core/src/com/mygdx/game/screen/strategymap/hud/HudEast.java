package com.mygdx.game.screen.strategymap.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Simulator;
import com.mygdx.game.screen.GuiUtils;
import com.mygdx.game.utils.SaveLoadUtils;
import com.mygdx.game.utils.WindowWithTopRightCornerCloseButton;
import com.mygdx.game.world.entity.universe.world.World;

public class HudEast extends Table {

    private Simulator parent;
    private Skin skin;
    private WindowWithTopRightCornerCloseButton worldWindow;

    public HudEast(final Simulator parent, final Skin skin) {
        this.parent = parent;
        this.skin = skin;

        this.setDebug(true);
        this.defaults().size(64,64).top();

        final ImageButton worldButton = GuiUtils.createImageButton("core/assets/texture/button/BTNSphere_Of_Gods.png",
                "core/assets/texture/button/DISBTNSphere_Of_Gods.png");

        //worldButton.addListener(new TextTooltip("World info", skin));
        worldButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                if (worldWindow == null) {
                    createWorldWindow(parent, skin);
                } else {
                    worldWindow.setVisible(true);
                }
                worldWindow.setPosition((Gdx.graphics.getWidth() - worldWindow.getWidth()) / 2, (Gdx.graphics.getHeight() - worldWindow.getHeight()) / 2);
            }
        });
        this.add(worldButton).row();
        final ImageButton b = GuiUtils.createImageButton("core/assets/texture/button/BTNBog_Monster.png",
                "core/assets/texture/button/DISBTNBog_Monster.png");
        this.add(b).row();

        final ImageButton ancients = GuiUtils.createImageButton("core/assets/texture/button/BTNdaemon.png",
                "core/assets/texture/button/DISBTNdaemon.png");
        this.add(ancients).row();
        final ImageButton heroesButton = GuiUtils.createImageButton("core/assets/texture/button/BTNXena_by67chrome.png",
                "core/assets/texture/button/DISBTNXena_by67chrome.png");

        this.add(heroesButton).row();
        this.add(new TextButton("E", skin)).row();
        this.add(new TextButton("F", skin)).row();

        final ImageButton menuButton = GuiUtils.createImageButton("core/assets/texture/button/menu/BTNSpellBook.png",
                "core/assets/texture/button/menu/DISBTNSpellBook.png");
        menuButton.addListener(new ClickListener() {
            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                SaveLoadUtils.saveToFile(parent.getGame(), "core/assets/game/game.txt");
                parent.changeScreen(Simulator.ScreenType.MENU);
            }
        });
        this.add(menuButton).row();

    }

    private void createWorldWindow(final Simulator parent, final Skin skin) {
        worldWindow = new WindowWithTopRightCornerCloseButton();
        final Table table = new Table();
        table.setFillParent(true);
        table.defaults().left();

        final World world = parent.getGame().getHumanPlayer().getSelectedWorld();
        table.add(new Label(world.getName(), skin));
        table.add(new Label(" [Age: " + (world.getHour() %24 % 365) + "]" , skin));
        table.add(new Label("Width: " + world.getWidth() * 10 + " km", skin)).row();
        table.add(new Label("Height: " + world.getHeight() * 10 + " km", skin)).row();
        table.add(new Label("Highest temp: " + world.getMaxTemperature(), skin)).row();
        table.add(new Label("Lowest temp: " + world.getMinTemperature(), skin)).row();
        table.add(new Label("Avg temp: " + world.getAvgTemperature(), skin)).row();
        table.add(new Label("Highest mountain: " + world.getMaxHeight(), skin)).row();
        table.add(new Label("Lowest point: " + world.getMinHeight(), skin)).row();
        table.add(new Label("Average height: " + world.getAvgHeight(), skin)).row();
        table.add(new Label("Gravity: " + world.getGravityG(), skin)).row();

        worldWindow.addActor(table);
        table.pack();
        worldWindow.pack();
        parent.getStrategyMapScreen().getPlayersHud().getStage().addActor(worldWindow);
    }
}
