package core.squid.tilelistener;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import core.cartezza.mapgenerator.generator.domain.Tile;
import squidpony.squidgrid.gui.gdx.SColor;

public class TileTooltip extends Window {

    private Skin _skin;
    private Label _name;
    private Label _description;

    public TileTooltip(final Skin skin) {
        super("", skin);
        this._skin = skin;

        _name = new Label("", skin, "inventory-item-count");
        _description = new Label("", skin, "inventory-item-count");

        this.add(_name).left().row();
        this.add(_description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);
    }

    public void setVisible(Tile tile, boolean visible) {
        super.setVisible(visible);

        if (tile == null) {
            return;
        }
    }

    public void updateDescription(Tile tile) {
        //Gdx.app.log("InventorySlotToolTip", "updateDescription");
        StringBuilder string = new StringBuilder();
        //string.append(item.getItemShortDescription());

        String biome = tile.biomeType != null ? tile.biomeType.name() : tile.heightType.name();
        _name.setText(biome + "[" + tile.x + "," + tile.y + "]" + "[" + (tile.collidable ? "C" : "NC") + "]");
        _name.setColor(SColor.GREEN_YELLOW);

        string.append(System.getProperty("line.separator"));
        string.append(String.format("Height: %s ", tile.heightValue));
        string.append(System.getProperty("line.separator"));
        string.append(String.format("Heat: %s ", tile.heatValue));
        string.append(System.getProperty("line.separator"));
        string.append(String.format("Moisture: %s ", tile.moistureValue));
        string.append(System.getProperty("line.separator"));
        string.append(String.format("Bitmask/biomeBitmask: %s / %s ", tile.bitmask, tile.biomeBitmask));

        _description.setText(string);
        this.pack();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.isVisible();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
