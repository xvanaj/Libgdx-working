package core;

import com.mygdx.game.gdxutils.GameTest;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.exception.WorldCreationException;
import com.mygdx.game.world.generator.game.GameFactory;
import org.junit.Test;

public class HeroPartiesTest extends GameTest {

    @Test
    public void createTestGame() throws WorldCreationException {
        Game game = GameFactory.createGame(Difficulty.MEDIUM);

    }

}
