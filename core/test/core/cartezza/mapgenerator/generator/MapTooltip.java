package core.cartezza.mapgenerator.generator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import core.cartezza.mapgenerator.generator.domain.MapData;

public class MapTooltip extends Window {

    private Skin _skin;
    private Label _name;
    private Label _description;

    public MapTooltip(final Skin skin) {
        super("", skin);
        this._skin = skin;

        _name = new Label("", skin, "inventory-item-count");
        _description = new Label("", skin, "inventory-item-count");

        this.add(_name).left().row();
        this.add(_description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(true);
    }

    public void updateDescription(MapData mapData) {
        StringBuilder string = new StringBuilder();
        _name.setText("adadsadads");

        string.append(System.getProperty("line.separator"));
        string.append(String.format("Original Value: %s GP", "adads"));
        string.append(System.getProperty("line.separator"));
        string.append(String.format("Trade Value: %s GP", "dasdaad"));

        _description.setText(string);
        this.pack();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
