package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Simulator;
import com.mygdx.game.constants.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Settings.APP_WIDTH;
		config.height = Settings.APP_HEIGHT;
		config.title = Settings.APP_TITLE;
		//config.samples = 2;

		final LwjglApplication app = new LwjglApplication(new Simulator(), config);
		Gdx.app = app;
		Gdx.app.setLogLevel(LwjglApplication.LOG_DEBUG);

	}
}
