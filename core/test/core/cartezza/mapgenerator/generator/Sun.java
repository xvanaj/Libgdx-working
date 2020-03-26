package core.cartezza.mapgenerator.generator;

import com.badlogic.gdx.graphics.Color;
import core.map.CelestialBody;

/**
 * Created by zachcarter on 12/10/16.
 */
public class Sun extends CelestialBody {
    int radius;
    private int frequency;


    public Sun(char symbol, Color color, int radius, int frequency, String description) {
        super(symbol, color, description);
        this.radius = radius;
        this.frequency = frequency;
    }

    public Sun(Sun sun) {
        super(sun.symbol, sun.color, sun.description);
        this.radius = sun.radius;
        this.frequency = sun.frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getRadius() {
        return radius;
    }
}
