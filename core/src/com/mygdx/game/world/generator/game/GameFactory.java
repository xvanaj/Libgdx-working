package com.mygdx.game.world.generator.game;

import com.mygdx.game.world.utils.TiledMapUtils;
import com.mygdx.game.utils.SaveLoadUtils;
import com.mygdx.game.world.params.PresetWorldCreationParameters;
import com.mygdx.game.world.params.WorldCreatorParametersFactory;
import com.mygdx.game.world.entity.EntityType;
import com.mygdx.game.world.entity.being.params.HeroCreationParameters;
import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.game.Game;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.utils.CodeGenerator;
import com.mygdx.game.world.generator.world.WorldFactory;
import com.mygdx.game.world.params.WorldCreatorInputParameters;
import com.mygdx.game.world.utils.EntryType;
import com.mygdx.game.world.utils.LogEntry;
import com.mygdx.game.utils.Randomizer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.util.stream.Collectors;

public class GameFactory {

    public static Game createGame(Difficulty difficulty) {
        final Game game = new Game();
        final Universe universe = new Universe();
        universe.setCode(CodeGenerator.getNextSequenceNumber(game, EntityType.UNIVERSE));

        game.setName("testGame");
        game.setCode("game1");
        game.setUniverse(universe);

        //generate Magical item researches
        //generate Spell researches
        //SpellResearchFactory.generateAllSpellResearches(null, null);

        //Generate worlds
        for (int i = 0; i < 1; i++) {
            WorldCreatorInputParameters parameters = WorldCreatorParametersFactory.createWorldCreationParams(PresetWorldCreationParameters.SMALL);

            parameters.setGame(game);
            final World world = WorldFactory.createWorld(parameters);

            game.getUniverse().getWorlds().put(world.getCode(), world);
        }

        setHumanPlayer(game);

        CodeGenerator.generateEntityCodes(game);

        //LogEntry entry = createFirstLogEntry(game);
        //game.getGameLog().get(1).add(entry);

        TiledMapUtils.loadAndInitializeTiledMap(game.getHumanPlayer().getPlayersWorld());
        SaveLoadUtils.saveToFile(game, "core/assets/game/game.txt");

        return game;
    }

    private static void setHumanPlayer(final Game game) {
        Player firstPlayer = game.getUniverse().getPlayers().stream().findFirst().get();
        final Point tilePosition = firstPlayer.getTilePosition();
        final int[][] explorationLayer = firstPlayer.getPlayersWorld().getExplorationLayer();

        //update exploration layer
        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                final int newX = tilePosition.x + i;
                final int newY = explorationLayer[0].length - tilePosition.y + j;
                if (newX >= 0 && newY >= 0
                        && newX < explorationLayer.length && newY < explorationLayer[0].length) {
                    explorationLayer[newX][newY] = 1;
                }
            }
        }
        game.setHumanPlayer(firstPlayer);
    }

    public static HeroCreationParameters createHeroCreationParams(final Player player) {
        HeroCreationParameters heroCreationParameters = new HeroCreationParameters();

        heroCreationParameters.setWorld(player.getPlayersWorld());
        heroCreationParameters.setFirstBorn(true);
        heroCreationParameters.setCivilization(Randomizer.get(player.getPlayersWorld().getCivilizations()));

        return heroCreationParameters;
    }

    private static LogEntry createFirstLogEntry(Game game) {
        StringBuffer sb = new StringBuffer();
        ElderBeing evilElder = Randomizer.get(game.getUniverse().getElders().stream()
                //.filter(elder -> elder.getAlignment().equals(Alignment.EVIL) || elder.getAlignment().equals(Alignment.CHAOTIC))
                .collect(Collectors.toList()));
        String evilElderName = evilElder.getName();
        Player humanPlayer = game.getHumanPlayer();
        World playersWorld = humanPlayer.getPlayersWorld();
        Hero firstHero = humanPlayer.getHeroes().get(0);

        sb.append("Entering universe of " + game.getName() + "\n");
        sb.append("It was once created by powerful entities, known as elders\n");
        sb.append("In the eons of time, " + Randomizer.get(BorednessType.values()).getText() + ", some of them turned to destruction of everything living.\n");

        sb.append("One such being, " + evilElderName + ", " + evilElder.getTypeName() + " is on the mission to turn every last of known worlds or even the whole universe into void\n");
        sb.append("You, another of the elders, swore oath to protect life at all cost\n");
        sb.append("The everlasting battle with the " + evilElderName + " led you to his next target. World of " + playersWorld.getName());
        sb.append("This " + playersWorld.getWorldType() + " world is primarily made of |crystals| mushrooms|metal|dirt|  \n");
        sb.append("It is lowly populated. There are many races living in peace - |orcs|gnomes|humans|elves|\n");
        sb.append("and also a few monsters and beings of ancient times which came here from other worlds to rule its lands or just to find hideout from elders.\n");
        sb.append("These include ");
        playersWorld.getAncients().stream().forEach(ancientBeing -> sb.append(ancientBeing.getAncientMonsterType().getName() + ", "));
        sb.append("\n");
        sb.append("The majority of " + playersWorld.getName() + " population is situated in towns ");
        playersWorld.getTowns().values().stream().forEach(town -> sb.append(town.getName() + ", "));
        sb.append("\n\n");
        sb.append(playersWorld.getDay() / playersWorld.getDaysInYear() + playersWorld.getName() + "s years. " + evilElderName + " started invasion\n");
        sb.append("In the first phase, " + evilElderName + " sent commanders of his twisted legions to the world and ordered to build gates there\n");
        sb.append("Meanwhile, he is gathering army and power to invade world through the gates.\n");
        sb.append("As eternal being, it might take Khronos years or even centuries to gather army big enough to success as time means nothing to him. " +
                "But you know that it is only a matter of time and that you should start organising defense asap\n");
        sb.append("As you no longer possess physical form, you have to choose one of inhabitants to fulfill your goals. \nYou chose following being among people as your avatar\n");
        sb.append(firstHero.getName() + ", " + firstHero.getAge() + " years old " + firstHero.getGender() + " " + firstHero.getRace() + ", cunny and muscular,  \n");
        sb.append("You should hurry and prepare town defenses before its too late.\n");

        return new LogEntry(EntryType.PLAYER, (long) playersWorld.getHour(), sb.toString());
    }

    /**
     * :)
     */
    @AllArgsConstructor
    @Getter
    private enum BorednessType {
        ETERNAL_LIFE("bored by eternal life"),
        DEPRESSED("depressed by endless life"),
        HATE("filled with hate"),
        USELESSNESS("disappointed by uselessness of younger races"),
        FRUSTRATION("frustrated by endless life"),
        EXISTENCE("disappointed by actions of younger races"),
        ;

        private String text;

    }
}
