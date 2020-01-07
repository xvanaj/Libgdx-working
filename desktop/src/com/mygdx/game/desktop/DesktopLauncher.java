package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Simulator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1024;
		config.height = 768;
		config.title = "Test";
		//config.samples = 2;

		final LwjglApplication app = new LwjglApplication(new Simulator(), config);
		Gdx.app = app;
		Gdx.app.setLogLevel(LwjglApplication.LOG_DEBUG);

	}
}
