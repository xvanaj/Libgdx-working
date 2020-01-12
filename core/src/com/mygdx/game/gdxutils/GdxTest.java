package com.mygdx.game.gdxutils;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.Utility;

public abstract class GdxTest extends InputAdapter implements ApplicationListener {

    protected GameAssetManager assetManager = Utility.assetManager;
    protected Stage stage;

    public void create () {
        //if (Gdx.app == null) {
            Gdx.app = new LwjglApplication(this, new LwjglApplicationConfiguration());
            Gdx.app.setLogLevel(LwjglApplication.LOG_DEBUG);
            stage = new Stage(new ScreenViewport());
            Gdx.input.setInputProcessor(stage);
            assetManager.loadAll();
        //}
    }

    public void resume () {
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void pause () {
    }

    public void dispose () {
    }

    @Override
    public boolean keyUp(final int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.D) {
            stage.setDebugAll(!stage.isDebugAll());
        }
        return super.keyDown(keycode);
    }
}