package com.mygdx.game.screen.strategymap.hud;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.game.Simulator;
import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.entity.universe.TileProperties;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.name.TownNameGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HudBottom extends Window {

    private Simulator parent;
    private Skin skin;
    private Table table;

    //Scene2D Widgets
    private TextArea tileInfo;

    public HudBottom(final Simulator parent, final Skin skin) {
        super("Tile info", skin);
        this.parent = parent;
        this.skin = skin;
        debugAll();
        //define labels using the String, and a Label style consisting of a font and color
        tileInfo = new TextArea("text", skin);
        StringBuilder build = new StringBuilder();

        tileInfo.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                // Handle a newline properly. If not handled here, the TextField
                // will advance to the next field.
                if (c == '\n') {
                    tileInfo.setMessageText(build.append("\n").toString());
                }
            }
        });

        //define a table used to organize hud's labels
        table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10).grow().expand().fill();
        table.add(tileInfo).left().top().padTop(20);

        //add table to the stage
        setKeepWithinStage(true);
        table.pack();
        addActor(table);
        debugAll();
    }

    public void update(float dt, float x, float y) {
        TileProperties tileProperties = null;
        final World world = parent.getGame().getHumanPlayer().getPlayersWorld();

        int posX = (int) (world.getHeight() - y / 32);

        int posY = (int) ( x / 32);
        final Point mapPosition = new Point(posX, posY);
        try {
            tileProperties = world.getTileProperties(mapPosition);
        } catch (Exception e) {
            return;
        }

        if (tileProperties == null) {
            tileInfo.setText("");
        } else {
            tileInfo.setText("");
            tileInfo.appendText(tileProperties.getTerrainType().name() + "");
            tileInfo.appendText(" [x" + posX + " y " + posY + "]\n");
            tileInfo.appendText("Level: " + tileProperties.getLevel() + "\n");
            tileInfo.appendText("Explored: " + tileProperties.getExplored() + "\n");
            tileInfo.appendText("Hostility: " + tileProperties.getHostility() + "\n");
            tileInfo.appendText("Height: " + String.format("%.1f", tileProperties.getHeight()) + "\n");
            tileInfo.appendText("Temperature: " + String.format("%.1f", tileProperties.getTemperature()) + "\n");
            tileInfo.appendText("Humidity: " + String.format("%.1f", tileProperties.getHumidity()));
            tileInfo.appendText("Resources: " + tileProperties.getResources() + "\n");
            tileInfo.appendText("Notes: ");
            final List<Town> towns = world.getTowns().values().stream()
                    .filter(town -> town.getMapPosition().equals(new Point((int)posX * 32, (int)posY *32)))
                    .collect(Collectors.toList());
            if (towns != null && !towns.isEmpty()) {
                towns.stream().forEach(town -> tileInfo.appendText(", " + getTownText(town)));
            }
            final List<WorldEntity> entities = new ArrayList<>();
            entities.addAll(world.getAncients());
            entities.addAll(world.getGates());
            entities.addAll(world.getDungeons());
            if (entities != null && !entities.isEmpty()) {
                entities.stream().filter(worldEntity -> worldEntity.getMapPosition().equals(mapPosition)).forEach(worldEntity -> tileInfo.appendText(", " + worldEntity.getCode()));
            }
        }
    }

    private String getTownText(final Town town) {
        final String townPrefix = TownNameGenerator.getTownPrefix(town);
        final String townName = town.getName();

        return townPrefix + " of " + townName;
    }

}