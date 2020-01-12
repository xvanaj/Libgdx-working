package com.mygdx.game.screen.strategymap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Simulator;
import com.mygdx.game.screen.strategymap.hud.PlayersHud;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.universe.world.World;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MapScreen extends ApplicationAdapter implements InputProcessor, MouseInputListener {

    public static final int MAX_CAMERA_ZOOM = 5;
    public static final float MIN_CAMERA_ZOOM = 0.125f;
    public static final float MOUSE_SCROLL_SPEED = 10f;
    protected OrthogonalTiledMapRendererWithSprites mapRenderer;
    protected OrthographicCamera camera;
    protected TiledMap map;
    protected Simulator parent;
    protected Map<Integer, Boolean> keysHeld = new HashMap();
    protected PlayersHud playersHud;
    protected Window popupMenu;

    public MapScreen(final Simulator parent) {
        this.parent = parent;

    }

    protected void handleInput() {

        for (Integer key : keysHeld.keySet()) {
            if (keysHeld.get(key)) {
                final Integer scrollingSpeed = parent.getPreferences().getScrollingSpeed();
                final Float zoomSpeed = parent.getPreferences().getZoomSpeed();
                switch (key) {
                    case Input.Keys.LEFT: {
                        camera.translate(-scrollingSpeed * camera.zoom, 0);
                        break;
                    }
                    case Input.Keys.RIGHT: {
                        camera.translate(+scrollingSpeed * camera.zoom, 0);
                        break;
                    }
                    case Input.Keys.UP: {
                        camera.translate(0, +scrollingSpeed * camera.zoom);
                        break;
                    }
                    case Input.Keys.DOWN: {
                        camera.translate(0, -scrollingSpeed * camera.zoom);
                        break;
                    }
                    case Input.Keys.PLUS: {
                        camera.zoom = camera.zoom - zoomSpeed;
                        if (camera.zoom < MIN_CAMERA_ZOOM) {
                            camera.zoom = MIN_CAMERA_ZOOM;
                        }
                        mapRenderer.getBitmapFont().getData().setScale(camera.zoom);
                        break;
                    }
                    case Input.Keys.MINUS: {
                        camera.zoom = camera.zoom + zoomSpeed;
                        if (camera.zoom > MAX_CAMERA_ZOOM) {
                            camera.zoom = MAX_CAMERA_ZOOM;
                        }
                        mapRenderer.getBitmapFont().getData().setScale(camera.zoom);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean keyDown(final int keycode) {
        updateKeysHeldMap(keycode, Boolean.TRUE);

        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        updateKeysHeldMap(keycode, Boolean.FALSE);

        if (keycode == Input.Keys.ESCAPE) {
            StrategyMapMenu dialog = new StrategyMapMenu("Setting", parent.getSkin(), parent);
        }

        if (keycode == Input.Keys.NUM_1)
            map.getLayers().get(0).setVisible(!map.getLayers().get(0).isVisible());
        if (keycode == Input.Keys.NUM_2)
            map.getLayers().get(1).setVisible(!map.getLayers().get(1).isVisible());

        if (keycode == Input.Keys.PAGE_DOWN) {
            camera.up.set(0, -1, 0);
            camera.direction.set(0, 0, 1);
            camera.rotate(getCameraCurrentXYAngle(camera) + 90);
        }
        if (keycode == Input.Keys.PAGE_UP) {
            camera.up.set(0, -1, 0);
            camera.direction.set(0, 0, 1);
            camera.rotate(getCameraCurrentXYAngle(camera) - 90);
        }
        if (isPressedWithAlt(keycode, Input.Keys.H)) {
            if (playersHud != null) {
                playersHud.setVisible(!playersHud.getVisible());
            }
        }
        //center of map
        if (isPressedWithAlt(keycode, Input.Keys.C)) {
            centerToMidOfMap();
        }
        //center to player
        if (isPressedWithAlt(keycode, Input.Keys.P)) {
            centerToPlayer();
        }
        if (isPressedWithAlt(keycode, Input.Keys.R)) {
            mapRenderer.setDrawRegions(!mapRenderer.isDrawRegions());
        }
        if (isPressedWithAlt(keycode, Input.Keys.N)) {
            mapRenderer.setDrawNames(!mapRenderer.isDrawNames());
        }
        if (isPressedWithAlt(keycode, Input.Keys.E)) {
            mapRenderer.setDrawUnexplored(!mapRenderer.isDrawUnexplored());
        }
        if (isPressedWithAlt(keycode, Input.Keys.A)) {
            mapRenderer.setDrawAncients(!mapRenderer.isDrawAncients());
        }
        if (isPressedWithAlt(keycode, Input.Keys.V)) {
            mapRenderer.setDrawVegetation(!mapRenderer.isDrawVegetation());
        }

        return false;
    }

    private boolean isPressedWithAlt(final int keycode, final int n) {
        return keycode == n && keysHeld.get(Input.Keys.ALT_LEFT) != null && keysHeld.get(Input.Keys.ALT_LEFT);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        final int height = (int)map.getProperties().get("height") * World.getPixelSize();
        playersHud.updateMapInfo(0f, unproject.x, height - unproject.y + World.getPixelSize());
    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    @Override
    public void mouseDragged(final MouseEvent e) {

    }

    @Override
    public void mouseMoved(final MouseEvent e) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        final int height = (int)map.getProperties().get("height") * World.getPixelSize();
        final int width = (int)map.getProperties().get("width") * World.getPixelSize();

        if (camera != null) {
            Gdx.app.log("Mouse Event", "Click at " + screenX + "," + screenY);
            Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
            Gdx.app.log("Mouse Event", "Projected at " + worldCoordinates.x + "," + worldCoordinates.y);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            final Vector3 unproject = camera.unproject(new Vector3(screenX, screenY, 0));

            playersHud.updateMapInfo(0f, unproject.x, height - unproject.y);
            if (unproject.x >= 0 && unproject.y >= 0 && unproject.x < width && unproject.y < height) {
                mapRenderer.setSelectedPixel(new Vector2(unproject.x, unproject.y));
            } else {
                mapRenderer.setSelectedPixel(null);
            }

            if (popupMenu != null) {
                popupMenu.remove();
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            mapRenderer.setSelectedPixel(null);
            final Vector3 unproject = camera.unproject(new Vector3(screenX, screenY, 0));

            if (unproject.x >= 0 && unproject.y >= 0 && unproject.x < width && unproject.y < height) {
                createPopupMenu(screenX, screenY, unproject);
            }
        }

        return false;
    }

    private void createPopupMenu(final int screenX, final int screenY, final Vector3 unproject) {
        if (popupMenu != null) {
            popupMenu.remove();
        }

        popupMenu = new Window("tile name", parent.getSkin());
        popupMenu.setMovable(false);

        final Label description = new Label("some description", parent.getSkin());
        //description.setFillParent(true);
        popupMenu.add(description).row();
        popupMenu.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
            }
        });
        final TextButton moveButton = new TextButton("Move here", parent.getSkin());
        moveButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                Game.log("MOVE", "dummy move action to " + unproject.x + " x " + unproject.y);
            }
        });

        popupMenu.add(moveButton);
        popupMenu.setX(screenX);
        popupMenu.setY(Gdx.graphics.getHeight() - screenY - popupMenu.getHeight());
        popupMenu.setVisible(true);
        playersHud.getStage().addActor(popupMenu);
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        if (camera != null) {
            float x = Gdx.input.getDeltaX();
            float y = Gdx.input.getDeltaY();

            final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            camera.translate(-x * camera.zoom, y * camera.zoom);

            fixBounds();
        }
        return true;
    }

    public void fixBounds() {
        float scaledViewportWidthHalfExtent = camera.viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = camera.viewportHeight * camera.zoom * 0.5f;
        final Integer xmax = (Integer) map.getProperties().get("width") * World.getPixelSize();
        final Integer ymax = (Integer) map.getProperties().get("height") * World.getPixelSize();

        //skip if too far
        if ((scaledViewportWidthHalfExtent * 2 > xmax) || (scaledViewportHeightHalfExtent * 2 > ymax)) {
            return;
        }

        // Horizontal
        if (camera.position.x < scaledViewportWidthHalfExtent)
            camera.position.x = scaledViewportWidthHalfExtent;
        else if (camera.position.x > xmax - scaledViewportWidthHalfExtent)
            camera.position.x = xmax - scaledViewportWidthHalfExtent;

        // Vertical
        if (camera.position.y < scaledViewportHeightHalfExtent)
            camera.position.y = scaledViewportHeightHalfExtent;
        else if (camera.position.y > ymax - scaledViewportHeightHalfExtent)
            camera.position.y = ymax - scaledViewportHeightHalfExtent;

    }
    
    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        if (camera != null) {
            camera.zoom = camera.zoom + amount/ MOUSE_SCROLL_SPEED;

            if (camera.zoom > MAX_CAMERA_ZOOM) {
                camera.zoom = MAX_CAMERA_ZOOM;
            }
            if (camera.zoom < MIN_CAMERA_ZOOM) {
                camera.zoom = MIN_CAMERA_ZOOM;
            }

            final Integer mapWidth = map.getProperties().get("width", Integer.class) * 32;
            final Integer mapHeight = map.getProperties().get("height", Integer.class) * 32;

        }

        fixBounds();
        return false;
    }


    public float getCameraCurrentXYAngle(OrthographicCamera cam) {
        return (float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees;
    }


    private void updateKeysHeldMap(final int keycode, final Boolean isHeld) {
        if (keycode == Input.Keys.LEFT)
            keysHeld.put(Input.Keys.LEFT, isHeld);
        if (keycode == Input.Keys.RIGHT)
            keysHeld.put(Input.Keys.RIGHT, isHeld);
        if (keycode == Input.Keys.UP)
            keysHeld.put(Input.Keys.UP, isHeld);
        if (keycode == Input.Keys.DOWN)
            keysHeld.put(Input.Keys.DOWN, isHeld);
        if (keycode == Input.Keys.PLUS)
            keysHeld.put(Input.Keys.PLUS, isHeld);
        if (keycode == Input.Keys.MINUS)
            keysHeld.put(Input.Keys.MINUS, isHeld);
        if (keycode == Input.Keys.ALT_LEFT)
            keysHeld.put(Input.Keys.ALT_LEFT, isHeld);
    }

    protected void centerToPlayer() {
        float scaledViewportWidthHalfExtent = camera.viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = camera.viewportHeight * camera.zoom * 0.5f;
        final int height = (int)map.getProperties().get("height") * World.getPixelSize();
        camera.position.x = parent.getGame().getHumanPlayer().getMapPosition().x;
        camera.position.y = parent.getGame().getHumanPlayer().getMapPosition().y;
        fixBounds();
        //camera.lookAt();
    }

    protected void centerToMidOfMap() {
        float scaledViewportWidthHalfExtent = camera.viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = camera.viewportHeight * camera.zoom * 0.5f;
        final int height = (int)map.getProperties().get("height") * World.getPixelSize();
        final int width = (int)map.getProperties().get("width") * World.getPixelSize();
        camera.position.x = width - scaledViewportWidthHalfExtent;
        camera.position.y = height - scaledViewportWidthHalfExtent;
        fixBounds();
        //camera.lookAt();
    }

    public OrthogonalTiledMapRendererWithSprites getMapRenderer() {
        return mapRenderer;
    }
}
