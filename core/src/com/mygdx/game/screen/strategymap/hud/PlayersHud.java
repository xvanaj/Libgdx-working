package com.mygdx.game.screen.strategymap.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Simulator;
import com.mygdx.game.screen.strategymap.ConsoleCommandExecutor;
import com.mygdx.game.screen.strategymap.hero.HeroesWindow;
import com.strongjoshua.console.GUIConsole;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class PlayersHud implements Screen, MouseInputListener {

    private Stage stage;
    private Viewport viewport;
    private HeroesWindow hudHeroes;
    private HudTop hudTop;
    private HudBottom hudBottom;
    private HudEast hudEast;

    private GUIConsole console;

    //Scene2D Widgets
    private TextArea tileInfo;

    public PlayersHud(final Simulator parent, Skin skin, final OrthographicCamera hudCamera) {
        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        viewport = new ScreenViewport(hudCamera);
        stage = new Stage(viewport);

        hudHeroes = createHeroesWindow(parent, skin, hudCamera);
        hudTop = createHudTop(parent, skin);
        hudBottom = createHudBottom(parent, skin);
        hudEast = createHudEast(parent, skin);
        console = new GUIConsole(skin);
        console.setSize((int) (Gdx.graphics.getWidth() * 0.7 - 68),(int) (Gdx.graphics.getHeight() * 0.3f));
        console.setPosition(68,0);
        console.setVisible(true);

        console.setCommandExecutor(new ConsoleCommandExecutor(parent.getGame()));
        parent.getGame().setConsole(console);

        stage.addActor(hudHeroes);
        stage.addActor(hudTop);
        stage.addActor(hudBottom);
        stage.addActor(hudEast);
        stage.addActor(console.getWindow());
    }

    private HudEast createHudEast(final Simulator parent, final Skin skin) {
        final HudEast hudEast = new HudEast(parent, skin);

        hudEast.setVisible(true);
        hudEast.pack();

        return hudEast;
    }

    private HudTop createHudTop(final Simulator parent, final Skin skin) {
        final HudTop hudTop = new HudTop(parent, skin);

        hudTop.setVisible(true);
        hudTop.pack();

        return hudTop;
    }

    private HudBottom createHudBottom(final Simulator parent, final Skin skin) {
        final HudBottom hudBottom = new HudBottom(parent, skin);
        hudBottom.setVisible(true);
        hudBottom.setPosition(Gdx.graphics.getWidth() - hudBottom.getWidth(), 0);
        hudBottom.setMovable(false);
        hudBottom.setKeepWithinStage(false);
        hudBottom.pack();

        return hudBottom;
    }

    private HeroesWindow createHeroesWindow(final Simulator parent, final Skin skin, final OrthographicCamera hudCamera) {
        HeroesWindow heroesWindow = new HeroesWindow(parent, skin);
        heroesWindow.setVisible(true);
        heroesWindow.setPosition(0, Gdx.graphics.getHeight() - heroesWindow.getHeight());
        heroesWindow.setKeepWithinStage(false);
        heroesWindow.pack();
        return heroesWindow;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        console.refresh();
        hudHeroes.setPosition(0, 0);
        hudHeroes.setSize(68, h);
        hudBottom.setPosition(w * 0.7f, 0);
        hudBottom.setSize(w * 0.3f, h * 0.3f);
        hudTop.setPosition(68, h - 16);
        hudTop.setSize(w - 68, 16);
        hudEast.setPosition(w - hudEast.getWidth(), hudBottom.getHeight());
        hudEast.setHeight(h - hudTop.getHeight() - hudBottom.getHeight());

        console.setPosition((int) hudHeroes.getWidth(), 0);
        console.setSize(w - (int)hudBottom.getWidth() - (int)hudHeroes.getWidth(), (int) hudBottom.getHeight());

        stage.getViewport().update(w, h, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public Stage getStage() {
        return stage;
    }


    public void updateMapInfo(final float v, final float x, final float y) {
        hudBottom.update(v, x, y);
    }

    public void updateTopHud(final float v) {
        hudTop.update(v);
    }

    public boolean getVisible() {
        return hudTop.isVisible();
    }

    public void setVisible(final boolean visible) {
        if (hudBottom != null) {
            hudBottom.setVisible(visible);
        }
        if (hudTop != null) {
            hudTop.setVisible(visible);
        }
        if (hudEast != null) {
            hudEast.setVisible(visible);
        }
        if (hudHeroes != null) {
            hudHeroes.setVisible(visible);
        }
        if (console != null && console.getWindow() != null) {
            console.getWindow().setVisible(visible);
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        System.out.println("test");
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

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
}