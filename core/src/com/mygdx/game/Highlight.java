package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.gdxutils.GdxTest;

public class Highlight extends GdxTest {

    public static void main(String[] args) {
        Highlight highlight = new Highlight();
        highlight.create();
    }

    @Override
    public void create() {
        super.create();

        Pixmap pixmap = new Pixmap(Gdx.files.internal("core/assets/maps/tilesets/ProjectUtumnoTileset.png"));
        Color color = new Color();

        for (int x = 0; x < pixmap.getWidth(); x++)
        {
            for (int y = 0; y < pixmap.getHeight(); y++)
            {
                int val = pixmap.getPixel(x, y);
                Color.rgba8888ToColor(color, val);
                int A = (int) (color.a * 255f);
                int R = (int) (color.r * 255f);
                int G = (int) (color.g * 255f);
                int B = (int) (color.b * 255f);
                pixmap.setColor(0.9f, 0.3f, 0.1f, 1);
                if(A != 0 && R != 0 && G != 0 && B != 0) //The flash shouldn't appear on transparent pixels, same for fully black pixels
                    pixmap.drawPixel(x, y);
            }
        }
        Sprite spriteFlash = new Sprite(new Texture(pixmap));
        PixmapIO.writePNG(new FileHandle("core/assets/maps/tilesets/ProjectUtumnoTileset2.png"), pixmap);
        pixmap.dispose();
    }
}
