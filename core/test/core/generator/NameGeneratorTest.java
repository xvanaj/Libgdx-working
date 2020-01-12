package core.generator;

import com.mygdx.game.gdxutils.GameTest;
import com.mygdx.game.name.MaterialNameGenerator;
import com.mygdx.game.name.NameGenerator;
import org.junit.Test;

public class NameGeneratorTest extends GameTest {

    @Test
    public void testGenerateResourceNames() {
        MaterialNameGenerator.generateRandomMaterialNames3("",10).stream().forEach(item -> System.out.println(item));
    }

    @Test
    public void testGenerateWorld() {
        System.out.println(NameGenerator.generateWorldName("anyType"));
    }
}
