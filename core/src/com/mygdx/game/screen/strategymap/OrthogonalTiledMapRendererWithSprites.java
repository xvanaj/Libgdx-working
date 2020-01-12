package com.mygdx.game.screen.strategymap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Settings;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.entity.universe.world.World;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Iterator;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

@Setter
@Getter
public class OrthogonalTiledMapRendererWithSprites extends OrthogonalTiledMapRenderer {

    public static final Color COLOR = new Color(1f, 1f, 1f, 1f);
    public static final ShapeRenderer shapeRenderer = new ShapeRenderer();
    final World world;
    final int[][] explorationLayer;
    private BitmapFont bitmapFont = new BitmapFont();
    private boolean drawRegions = true;
    private boolean drawNames = true;
    private boolean drawUnexplored = Settings.debug_drawUnexplored;
    private boolean drawAncients = true;
    private boolean drawVegetation = true;
    private Texture highlightTexture;
    private Vector2 selectedPixel;
    final Pixmap objectsPixmap;

    public OrthogonalTiledMapRendererWithSprites(final TiledMap map, final float unitScale, final int[][] explorationLayer, World world) {
        super(map, unitScale);
        batch.enableBlending();
        this.world = world;
        this.explorationLayer = explorationLayer;
        final Iterator<TiledMapTile> projectUtumnoTileset = map.getTileSets().getTileSet("ProjectUtumnoTileset").iterator();

        Utility.assetManager.load("core/assets/maps/tilesets/ProjectUtumnoTileset2.png", Texture.class);
        Utility.assetManager.finishLoading();

        highlightTexture = Utility.assetManager.get("core/assets/maps/tilesets/ProjectUtumnoTileset2.png");

        final Texture texture = projectUtumnoTileset.next().getTextureRegion().getTexture();
        texture.getTextureData().prepare();
        this.objectsPixmap = texture.getTextureData().consumePixmap();
    }

    @Override
    protected void renderMapLayer(final MapLayer layer) {
        super.renderMapLayer(layer);
    }

    @Override
    public void render() {
        beginRender();

        final MapLayers layers = map.getLayers();
        renderMapLayer(layers.get("Main layer"));
        renderMapLayer(layers.get("Trees layer"));
        renderMapLayer(layers.get("Objects layer"));
        renderMapLayer(layers.get("Points layer"));

        highlightSelected();
        highlightObject(layers.get("Objects layer"));
        endRender();
    }

    private void highlightSelected() {
        if (selectedPixel != null) {
            ShapeRenderer sr = shapeRenderer;
            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.CORAL);
            int posX = (int) (selectedPixel.x / World.getPixelSize());
            int posY = (int) (selectedPixel.y / World.getPixelSize());
            sr.rect(posX * World.getPixelSize(), posY * World.getPixelSize(), World.getPixelSize(), World.getPixelSize());
            sr.end();
        }
    }

    @Override
    public void renderTileLayer(final TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * unitScale;
        final float layerTileHeight = layer.getTileHeight() * unitScale;

        final float layerOffsetX = layer.getRenderOffsetX() * unitScale;
        // offset in tiled is y down, so we flip it
        final float layerOffsetY = -layer.getRenderOffsetY() * unitScale;

        final int col1 = Math.max(0, (int) ((viewBounds.x - layerOffsetX) / layerTileWidth));
        final int col2 = Math.min(layerWidth,
                (int) ((viewBounds.x + viewBounds.width + layerTileWidth - layerOffsetX) / layerTileWidth));

        final int row1 = Math.max(0, (int) ((viewBounds.y - layerOffsetY) / layerTileHeight));
        final int row2 = Math.min(layerHeight,
                (int) ((viewBounds.y + viewBounds.height + layerTileHeight - layerOffsetY) / layerTileHeight));

        float y = row2 * layerTileHeight + layerOffsetY;
        float xStart = col1 * layerTileWidth + layerOffsetX;
        final float[] vertices = this.vertices;

        for (int row = row2; row >= row1; row--) {
            float x = xStart;
            for (int col = col1; col < col2; col++) {
                final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) {
                    x += layerTileWidth;
                    continue;
                }
                final TiledMapTile tile = cell.getTile();

                if (tile != null && ((drawUnexplored) || (explorationLayer.length > col && explorationLayer[0].length > row && explorationLayer[col][row] == 1))) {
                    final boolean flipX = cell.getFlipHorizontally();
                    final boolean flipY = cell.getFlipVertically();
                    final int rotations = cell.getRotation();

                    TextureRegion region = tile.getTextureRegion();

                    float x1 = x + tile.getOffsetX() * unitScale;
                    float y1 = y + tile.getOffsetY() * unitScale;
                    float x2 = x1 + region.getRegionWidth() * unitScale;
                    float y2 = y1 + region.getRegionHeight() * unitScale;

                    float u1 = region.getU();
                    float v1 = region.getV2();
                    float u2 = region.getU2();
                    float v2 = region.getV();

                    vertices[X1] = x1;
                    vertices[Y1] = y1;
                    vertices[C1] = color;
                    vertices[U1] = u1;
                    vertices[V1] = v1;

                    vertices[X2] = x1;
                    vertices[Y2] = y2;
                    vertices[C2] = color;
                    vertices[U2] = u1;
                    vertices[V2] = v2;

                    vertices[X3] = x2;
                    vertices[Y3] = y2;
                    vertices[C3] = color;
                    vertices[U3] = u2;
                    vertices[V3] = v2;

                    vertices[X4] = x2;
                    vertices[Y4] = y1;
                    vertices[C4] = color;
                    vertices[U4] = u2;
                    vertices[V4] = v1;

                    if (flipX) {
                        float temp = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = temp;
                        temp = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = temp;
                    }
                    if (flipY) {
                        float temp = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = temp;
                        temp = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = temp;
                    }
                    if (rotations != 0) {
                        switch (rotations) {
                            case TiledMapTileLayer.Cell.ROTATE_90: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V2];
                                vertices[V2] = vertices[V3];
                                vertices[V3] = vertices[V4];
                                vertices[V4] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U2];
                                vertices[U2] = vertices[U3];
                                vertices[U3] = vertices[U4];
                                vertices[U4] = tempU;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_180: {
                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U3];
                                vertices[U3] = tempU;
                                tempU = vertices[U2];
                                vertices[U2] = vertices[U4];
                                vertices[U4] = tempU;
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V3];
                                vertices[V3] = tempV;
                                tempV = vertices[V2];
                                vertices[V2] = vertices[V4];
                                vertices[V4] = tempV;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_270: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V4];
                                vertices[V4] = vertices[V3];
                                vertices[V3] = vertices[V2];
                                vertices[V2] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U4];
                                vertices[U4] = vertices[U3];
                                vertices[U3] = vertices[U2];
                                vertices[U2] = tempU;
                                break;
                            }
                        }
                    }
                    if (drawRegions) {
                        final Color regionColor = getRegionColor(x1, y1);
                        final Color originalColor = getBatch().getColor();
                        final float colorFloat = Color.toFloatBits(regionColor.r * originalColor.r, regionColor.g * originalColor.g
                                , regionColor.b * originalColor.b, regionColor.a * layer.getOpacity());
                        vertices[C1] = colorFloat;
                        vertices[C2] = colorFloat;
                        vertices[C3] = colorFloat;
                        vertices[C4] = colorFloat;
                    }

                    batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
                }
                x += layerTileWidth;
            }
            y -= layerTileHeight;
        }
    }

    private Color getRegionColor(final float x1, final float y1) {
        final int regionId = world.getRegionsLayer()[(int) (x1 / World.pixelSize)][(int) (y1 / World.pixelSize)];
        if (regionId <= 0) {
            return COLOR;
        } else {
            return colors[regionId];
        }
    }


    @Override
    public void renderObject(final MapObject object) {
        if (object instanceof TextureMapObject) {
            TextureMapObject textureObj = (TextureMapObject) object;
            if (textureObj.getY() / World.getPixelSize() == -1) {
                System.out.println("");
            }
            if (drawUnexplored || explorationLayer[(int) (textureObj.getX() / World.getPixelSize())][(int) ((textureObj.getY() /*- World.getPixelSize()*/) / World.getPixelSize())] == 1) {
                final TiledMapTileMapObject tileObject = ((TiledMapTileMapObject) object);
                final TiledMapTile tile = tileObject.getTile();

                if (drawNames && tileObject.getName() != null) {
                    bitmapFont.draw(batch, tileObject.getName(), textureObj.getX(), textureObj.getY());
                }

                //if selected
                final TextureRegion textureRegion;
                if (selectedPixel != null && (int) tileObject.getX() / 32 == (int) selectedPixel.x / 32 && (int) tileObject.getY() / 32 == (int) selectedPixel.y / 32) {
                    textureRegion = new TextureRegion(highlightTexture, tileObject.getTextureRegion().getRegionX(), tileObject.getTextureRegion().getRegionY(),
                            tileObject.getTextureRegion().getRegionWidth(), tileObject.getTextureRegion().getRegionHeight());
                } else {
                    textureRegion = textureObj.getTextureRegion();
                }

                batch.draw(textureRegion, textureObj.getX(), textureObj.getY(),
                        textureObj.getOriginX(), textureObj.getOriginY(), textureObj.getTextureRegion().getRegionWidth(), textureObj.getTextureRegion().getRegionHeight(),
                        getUnitScale(), getUnitScale(), textureObj.getRotation());

                //todo deprecated probably
                /*if (selectedPixel != null && (int)tileObject.getX() / 32 == (int)selectedPixel.x / 32
                        && (int)tileObject.getY() / 32 == (int)selectedPixel.y / 32) {
                    highlightObject(tileObject);
                }*/

                if (textureObj.getProperties().containsKey("this")) System.out.println(textureObj.getRotation());
            }
        } else if (object instanceof RectangleMapObject) {
            RectangleMapObject rectObject = (RectangleMapObject) object;
            Rectangle rect = rectObject.getRectangle();
            ShapeRenderer sr = shapeRenderer;
            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.rect(rect.x, rect.y, rect.width, rect.height);
            sr.end();
        } else {
            System.out.println("not drawn object: " + object);
        }
    }

    public void setDrawRegions(final boolean drawRegions) {
        this.drawRegions = drawRegions;
    }

    public boolean isDrawRegions() {
        return drawRegions;
    }

    public boolean isDrawNames() {
        return drawNames;
    }

    public void setDrawNames(final boolean drawNames) {
        this.drawNames = drawNames;
    }


    private Color[] colors = new Color[50];

    {
        colors[0] = new Color(1f, 0.6f, 0.6f, 1f);
        colors[1] = new Color(1f, 0.6f, 0.3f, 1f);
        colors[2] = new Color(0.6f, 0.6f, 1f, 1f);
        colors[3] = new Color(1f, 0.6f, 1f, 1f);
        colors[4] = new Color(0.6f, 0.3f, 1f, 1f);
        colors[5] = new Color(1f, 0.3f, 0.6f, 1f);
        colors[6] = new Color(0.8f, 0.5f, 0.5f, 1f);
        for (int i = 7; i < 50; i++) {
            colors[i] = new Color(0.6f, 0.3f, 1f, 1f);
        }

    }

    public void highlightTile(final Point tilePos) {
        ShapeRenderer sr = shapeRenderer;
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect(tilePos.x, tilePos.y, World.getPixelSize(), World.getPixelSize());
        sr.end();
    }

    public void highlightObject(final MapLayer mapLayer) {
        if (selectedPixel != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                TiledMapTileMapObject tileObject = (TiledMapTileMapObject) mapObject;

                if ((int) tileObject.getX() / 32 == (int) selectedPixel.x / 32
                        && (int) tileObject.getY() / 32 == (int) selectedPixel.y / 32) {

                    //nonworkingHighlight(tileObject);
                    final TextureRegion textureRegion = new TextureRegion(highlightTexture, tileObject.getTextureRegion().getRegionX(), tileObject.getTextureRegion().getRegionY(),
                            tileObject.getTextureRegion().getRegionWidth(), tileObject.getTextureRegion().getRegionHeight());
                    batch.draw(textureRegion, tileObject.getX(), tileObject.getY(),
                            tileObject.getOriginX(), tileObject.getOriginY(), tileObject.getTextureRegion().getRegionWidth(), tileObject.getTextureRegion().getRegionHeight(),
                            getUnitScale(), getUnitScale(), tileObject.getRotation());
                }
            }
        }
    }

    private void nonworkingHighlight(final TiledMapTileMapObject tileObject) {
        final int regionX = tileObject.getTextureRegion().getRegionX();
        final int regionY = tileObject.getTextureRegion().getRegionY();

        ShapeRenderer sr = shapeRenderer;
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GOLD);
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 31; j++) {
                final int pixel = objectsPixmap.getPixel(regionX + i, /*objectsPixmap.getHeight() - World.getPixelSize() -*/ regionY + j);
                final int pixelE = objectsPixmap.getPixel(getYMod() + regionY + j + 1, regionX + i);
                final int pixelW = objectsPixmap.getPixel(getYMod() + regionY + j - 1, regionX + i);
                final int pixelS = objectsPixmap.getPixel(getYMod() + regionY + j, regionX + i - 1);
                final int pixelN = objectsPixmap.getPixel(getYMod() + regionY + j, regionX + i + 1);
                if (pixel == 0 && (pixelE != 0 || pixelS != 0 || pixelW != 0 || pixelN != 0)) {
                    //sr.rect(tileObject.getX() + i, tileObject.getY() + j, 1, 1);
                    //sr.point(tileObject.getX(), tileObject.getY(),  0);
                    sr.circle(tileObject.getX() + i, tileObject.getY() + j, 1);
                }
            }
        }
        sr.end();
    }

    private int getYMod() {
        return 0;
        //return - objectsPixmap.getHeight();
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public void setBitmapFont(final BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
    }
}
