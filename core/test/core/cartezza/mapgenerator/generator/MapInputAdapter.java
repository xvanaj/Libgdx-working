package core.cartezza.mapgenerator.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import core.cartezza.mapgenerator.generator.domain.MapType;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MapInputAdapter extends InputAdapter implements MouseInputListener {

    protected Map<Integer, Boolean> keysHeld = new HashMap();
    protected Map<MapType, Texture> textures = new HashMap<>();
    protected OrthographicCamera camera;
    protected Texture map;

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("sdf");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("");
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        if (camera != null) {
            float x = Gdx.input.getDeltaX();
            float y = Gdx.input.getDeltaY();

            camera.translate(-x * camera.zoom, y * camera.zoom);

            /*if (camera.position.x < -map.getTextureData().getWidth() / 2 || camera.position.x > map.getTextureData().getWidth() * 3 / 2) {
                camera.position.x = map.getTextureData().getWidth() / 2;
            }
            if (camera.position.y < -map.getTextureData().getHeight() / 2 || camera.position.y > map.getTextureData().getHeight() * 3 / 2) {
                camera.position.y = map.getTextureData().getHeight() / 2;
            }*/

            //fixBounds();
        }
        return true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println();
    }


    public static final int MAX_CAMERA_ZOOM = 20;
    public static final float MIN_CAMERA_ZOOM = 0.125f;

    protected void handleInput() {

    }

    protected void updateKeysHeldMap(final int keycode, final Boolean isHeld) {
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

    @Override
    public boolean keyUp(int keycode) {
        updateKeysHeldMap(keycode, Boolean.FALSE);

        if (keycode == Input.Keys.ESCAPE) {
        }

        if (keycode == Input.Keys.NUMPAD_1)

        if (keycode == Input.Keys.NUMPAD_2)

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

        //center of map
        if (isPressedWithAlt(keycode, Input.Keys.C)) {
            centerToMidOfMap();
        }
        //center to player
        if (isPressedWithAlt(keycode, Input.Keys.P)) {
            centerToPlayer();
        }

        return false;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }


    @Override
    public boolean keyDown(final int keycode) {
        updateKeysHeldMap(keycode, Boolean.TRUE);
        handleInput();

        return false;
    }

    public float getCameraCurrentXYAngle(OrthographicCamera cam) {
        return (float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees;
    }

    protected boolean isPressedWithAlt(final int keycode, final int n) {
        return keycode == n && keysHeld.get(Input.Keys.ALT_LEFT) != null && keysHeld.get(Input.Keys.ALT_LEFT);
    }

    public void centerToPlayer() {
        float scaledViewportWidthHalfExtent = camera.viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = camera.viewportHeight * camera.zoom * 0.5f;
/*
        camera.position.x = parent.getGame().getHumanPlayer().getMapPosition().x *//*+ scaledViewportWidthHalfExtent*//*;
        camera.position.y = parent.getGame().getHumanPlayer().getMapPosition().y *//*+ scaledViewportHeightHalfExtent*//*;
        fixBounds();*/
        //camera.lookAt();
    }

    protected void centerToMidOfMap() {

    }
}
