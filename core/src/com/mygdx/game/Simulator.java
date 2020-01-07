package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.preferences.AppPreferences;
import com.mygdx.game.screen.*;
import com.mygdx.game.screen.editor.EditorScreen;
import com.mygdx.game.screen.universescreen.CreateUniverseScreen;
import com.mygdx.game.screen.strategymap.StrategyMapScreen;

import static com.badlogic.gdx.Application.LOG_DEBUG;

public class Simulator extends Game {

    //screens
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    private EditorScreen editorScreen;
    private CreateUniverseScreen createUniverseScreen;
    private StrategyMapScreen strategyMapScreen;
    private EndScreen endScreen;

    //gamedata
    private com.mygdx.game.world.entity.game.Game gameInstance;

    private Skin skin;
    private AppPreferences preferences;

    public enum ScreenType {
        MENU,
        EDITOR,
        PREFERENCES,
        STRATEGY_MAP,
        CREATE_UNIVERSE,
        ENDGAME,
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(LOG_DEBUG);
        skin = new Skin(Gdx.files.internal("core/assets/skin/shade/skin/uiskin.json"));
        skin = new Skin(Gdx.files.internal("core/assets/skin/uiskin/uiskin.json"));
        //skin = new Skin(Gdx.files.internal("core/assets/skin/pixthulhu/pixthulhu-ui.json"));
        preferences = new AppPreferences();

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (loadingScreen != null) {
            loadingScreen.dispose();
        }
        if (preferencesScreen != null) {
            preferencesScreen.dispose();
        }
        if (editorScreen != null) {
            editorScreen.dispose();
        }
        if (menuScreen != null) {
            menuScreen.dispose();
        }
        if (createUniverseScreen != null) {
            createUniverseScreen.dispose();
        }
        if (strategyMapScreen != null) {
            strategyMapScreen.dispose();
        }
        if (endScreen != null) {
            endScreen.dispose();
        }
    }


    public void changeScreen(ScreenType screen) {
        switch (screen) {
            case MENU:
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case EDITOR:
                if (editorScreen == null) editorScreen = new EditorScreen(this);
                this.setScreen(editorScreen);
                break;
            case PREFERENCES:
                if (preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                this.setScreen(preferencesScreen);
                break;
            case STRATEGY_MAP:
                if (strategyMapScreen != null) {
                    strategyMapScreen.dispose();
                }
                strategyMapScreen = new StrategyMapScreen(this);
                this.setScreen(strategyMapScreen);
                break;
            case CREATE_UNIVERSE:
                if (createUniverseScreen == null) createUniverseScreen = new CreateUniverseScreen(this);
                this.setScreen(createUniverseScreen);
                break;
            case ENDGAME:
                if (endScreen == null) endScreen = new EndScreen(this);
                this.setScreen(endScreen);
                break;
        }
    }

    public AppPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(AppPreferences preferences) {
        this.preferences = preferences;
    }

    public com.mygdx.game.world.entity.game.Game getGameInstance() {
        return gameInstance;
    }

    public void setGameInstance(final com.mygdx.game.world.entity.game.Game gameInstance) {
        this.gameInstance = gameInstance;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(final Skin skin) {
        this.skin = skin;
    }

    public StrategyMapScreen getStrategyMapScreen() {
        return strategyMapScreen;
    }
}
