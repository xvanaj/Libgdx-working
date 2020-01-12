package com.mygdx.game.screen.strategymap.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.Simulator;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.universe.world.World;

public class HudTop extends Table {

    private Simulator parent;
    private Skin skin;

    //game speed
    Label gameSpeedLabel;
    Button slowSpeedButton;
    Button mediumSpeedButton;
    Button fastSpeedButton;
    Button extraFastSpeedButton;
    Button speedX100Button;
    Button pauseButton;
    private HorizontalGroup worldInfoWidgetGroup, gameSpeedWidgetGroup;

    //Scene2D Widgets
    private Label worldLabel, dateLabel;

    public HudTop(final Simulator parent, final Skin skin) {
        this.parent = parent;
        this.skin = skin;
        final Game game = parent.getGame();
        final Player player = game.getHumanPlayer();

        //define labels using the String, and a Label style consisting of a font and color
        worldInfoWidgetGroup = createWorldInfoGroup();
        gameSpeedWidgetGroup = createSpeedButtonsGroup(parent);

        defaults().expand();
        worldLabel.setX(100);


        /*widgetGroup2 = new WidgetGroup();
        widgetGroup2.addActor(gameSpeedLabel);
        //gameSpeedLabel.setPosition(0, 0);
        widgetGroup2.addActor(pauseButton);
        //pauseButton.setPosition(gameSpeedLabel.getWidth(), 0);
        widgetGroup2.addActor(slowSpeedButton);
        //slowSpeedButton.setPosition(gameSpeedLabel.getWidth() + pauseButton.getWidth(), 0);
        widgetGroup2.addActor(mediumSpeedButton);
        //mediumSpeedButton.setPosition(gameSpeedLabel.getWidth() + pauseButton.getWidth() + slowSpeedButton.getWidth(), 0);
        widgetGroup2.addActor(fastSpeedButton);
        //fastSpeedButton.setPosition(gameSpeedLabel.getWidth() + pauseButton.getWidth()  + slowSpeedButton.getWidth() + mediumSpeedButton.getWidth(), 0);
        widgetGroup2.addActor(extraFastSpeedButton);
        //extraFastSpeedButton.setPosition(gameSpeedLabel.getWidth() + pauseButton.getWidth()  + slowSpeedButton.getWidth() + mediumSpeedButton.getWidth() + fastSpeedButton.getWidth(), 0);
        widgetGroup2.addActor(speedX100Button);
        widgetGroup2.pack();*/


        pad(5, 5, 5, 5);
        //add table to the stage

        add(worldInfoWidgetGroup).left();
        add(gameSpeedWidgetGroup).right();
    }

    private HorizontalGroup createWorldInfoGroup() {
        worldInfoWidgetGroup = new HorizontalGroup();

        worldLabel = new Label("world", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        dateLabel = new Label(String.format("%03d", 0), new Label.LabelStyle(new BitmapFont(), Color.GREEN));

        worldInfoWidgetGroup.addActor(worldLabel);
        worldInfoWidgetGroup.addActor(dateLabel);

        return worldInfoWidgetGroup;
    }

    private HorizontalGroup createSpeedButtonsGroup(final Simulator parent) {
        HorizontalGroup widgetGroup = new HorizontalGroup();

        gameSpeedLabel = new Label("speed:", skin);

        slowSpeedButton = new TextButton(">", skin);
        slowSpeedButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                parent.getGame().setGameSpeed(1);
                parent.getGame().setFastMode(false);
                parent.getStrategyMapScreen().setDrawMap(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        mediumSpeedButton = new TextButton(">>", skin);
        mediumSpeedButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                parent.getGame().setGameSpeed(2);
                parent.getGame().setFastMode(false);
                parent.getStrategyMapScreen().setDrawMap(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        fastSpeedButton = new TextButton(">>>", skin);
        fastSpeedButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                parent.getGame().setGameSpeed(3);
                parent.getGame().setFastMode(false);
                parent.getStrategyMapScreen().setDrawMap(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        extraFastSpeedButton = new TextButton("X10", skin);
        extraFastSpeedButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                parent.getGame().setGameSpeed(10);
                parent.getGame().setFastMode(false);
                parent.getStrategyMapScreen().setDrawMap(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        speedX100Button = new TextButton("X100", skin);
        speedX100Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                parent.getGame().setGameSpeed(100);
                parent.getGame().setFastMode(true);
                parent.getStrategyMapScreen().setDrawMap(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        pauseButton = new TextButton("||", skin);
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                parent.getGame().setGameSpeed(0);
                parent.getGame().setFastMode(false);
                parent.getStrategyMapScreen().setDrawMap(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        widgetGroup.addActor(gameSpeedLabel);
        widgetGroup.addActor(slowSpeedButton);
        widgetGroup.addActor(mediumSpeedButton);
        widgetGroup.addActor(fastSpeedButton);
        widgetGroup.addActor(extraFastSpeedButton);
        widgetGroup.addActor(speedX100Button);
        widgetGroup.addActor(pauseButton);

        return widgetGroup;
    }

    public void update(float dt) {
        final World world = parent.getGame().getHumanPlayer().getSelectedWorld();
        worldLabel.setText(parent.getGame().getUniverse().getCode() + ", " + world.getName() + " ");
        final String nightDay = ((world.getHour() % world.getHoursInDay() < world.getSunriseHour()) || (world.getHour() % world.getHoursInDay() > world.getSunsetHour())) ? "Night" : "Day";

        dateLabel.setText("Year " + world.getYear()
                + " Day " + world.getDay() + " "
                + " Hour " + String.format("%02d", world.getHour()) + ":" + String.format("%02d", world.getMinute() % 60)
                + " " + nightDay);

        gameSpeedLabel.setText("x " + parent.getGame().getGameSpeed());
    }

}