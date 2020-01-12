package core;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.gdxutils.GameTest;
import com.mygdx.game.utils.SaveLoadUtils;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.generator.game.GameFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

public class SaveLoadUtilsTest extends GameTest {

    Game game;

    @Before
    public void setUp() throws Exception {
        game = GameFactory.createGame(Difficulty.MEDIUM);
    }

    @Test
    public void testSaveAndLoadHero() {
        final Hero expected = game.getHumanPlayer().getHeroes().get(0);
        FileHandle fileHandle = SaveLoadUtils.saveToFile(expected, "assets/universe/hero.txt");

        final Hero actual = (Hero) SaveLoadUtils.loadFromFile(fileHandle, Hero.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSaveAndLoadTown() {
        final Town expected = game.getUniverse()
                .getWorlds().values().stream()
                .collect(Collectors.toList()).get(0)
                .getTowns().values().stream()
                .collect(Collectors.toList()).get(0);
        FileHandle fileHandle =  SaveLoadUtils.saveToFile(expected, "assets/universe/town.txt");

        final Town actual = (Town) SaveLoadUtils.loadFromFile(fileHandle, Town.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSaveAndLoadWorld() {
        final Object expected = game.getHumanPlayer().getPlayersWorld();
        FileHandle fileHandle =  SaveLoadUtils.saveToFile(expected, "assets/universe/world.txt");

        final Object actual = SaveLoadUtils.loadFromFile(fileHandle, World.class);
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testSaveAndLoadUniverse() {
        final Object expected = game.getUniverse();
        FileHandle fileHandle =  SaveLoadUtils.saveToFile(expected, "assets/universe/universe.txt");

        Universe actual = SaveLoadUtils.loadGame(fileHandle).getUniverse();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSaveAndLoadGame() {
        final Object expected = game;
        FileHandle fileHandle =  SaveLoadUtils.saveToFile(expected, "assets/game/game.txt");

        Game actual = (Game) SaveLoadUtils.loadFromFile(fileHandle, Game.class);

        Assert.assertEquals(expected, actual);
    }
}
