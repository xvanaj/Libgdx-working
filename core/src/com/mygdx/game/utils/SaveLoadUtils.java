package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.world.utils.TiledMapUtils;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.civilization.Civilization;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.entity.universe.NatureWonder;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;

import java.io.*;
import java.util.Date;

public class SaveLoadUtils {

    private static Json json = new Json();

    public static FileHandle saveToFile(final Object object, final String fileName) {
        return saveToFile(object,fileName, false);
    }

    public static FileHandle saveToFile(final Object object, final String fileName, final boolean writeOutput) {
        Date startDate = new Date();

        FileHandle fileHandle = new FileHandle(fileName);
        final String jsonGame = json.prettyPrint(object);

        if (writeOutput) {
            System.out.println(jsonGame);
        }
        fileHandle.writeString(jsonGame, false);

        Gdx.app.log("worldGen", "Saving of game file done in " + (new Date().getTime() - startDate.getTime()) + " millis");

        return fileHandle;
    }


    public static Object loadFromFile(final FileHandle fileHandle, Class clazz) {
        Object object = json.fromJson(clazz, fileHandle);

        final String heroAsJason = json.prettyPrint(object);
        System.out.println(heroAsJason);

        return object;
    }

    public static Game loadGame(final FileHandle fileHandle) {
        final Game object = json.fromJson(Game.class, fileHandle);
        fillTransientValues(object);

        final String universeAsJson = json.prettyPrint(object);

        System.out.println(universeAsJson);

        return object;
    }

    private static void fillTransientValues(final Game game) {
        fillTransientValues(game.getUniverse(), game);
    }

    private static void fillTransientValues(final Universe universe, final Game game) {
        for (World world : universe.getWorlds().values()) {
            fillTransientValues(world, game);
        }

        for (Player player : universe.getPlayers()) {
            fillTransientValues(player, game);
        }
    }

    private static void fillTransientValues(final Player player, final Game game) {
        player.setSelectedWorld((World) game.getGameEntity(player.getSelectedWorldCode()));
        player.setPlayersWorld((World) game.getGameEntity(player.getPlayersWorldCode()));
    }

    private static void fillTransientValues(final World world, final Game game) {
        TiledMapUtils.loadAndInitializeTiledMap(world);
        world.setUniverse((Universe) game.getGameEntity(world.getUniverseCode()));
        world.setUniverseCode(world.getUniverse().getCode());

        for (AncientBeing ancient : world.getAncients()) {
            ancient.setWorld((World) game.getGameEntity(ancient.getWorldCode()));
        }

        for (Civilization civilization : world.getCivilizations()) {
            fillTransientValues(game, civilization);
        }

        for (NatureWonder natureWonder : world.getNatureWonders()) {

        }
    }

    private static void fillTransientValues(final Game game, final Civilization civilization) {
        civilization.setWorld((World) game.getGameEntity(civilization.getWorldCode()));
        civilization.setCapitalTown((Town) game.getGameEntity(civilization.getCapitalTownCode()));
        civilization.getLeader().setTown((Town) game.getGameEntity(civilization.getLeader().getTownCode()));
    }

    public static void writeStringToFile(final String savePath, final String tmxString) {
        PrintWriter writer = null;
        try {
            File file = new File(savePath);
            if (!file.getParentFile().exists()) {
                file = new File(savePath.replace("core/", ""));
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            writer = new PrintWriter(fileWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.print(tmxString);
        writer.close();
    }

}
