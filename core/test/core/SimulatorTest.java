package core;

import com.mygdx.game.gdxutils.GameTest;
import com.mygdx.game.world.entity.being.params.HeroCreationParameters;
import com.mygdx.game.world.builder.PlayerBuilder;
import com.mygdx.game.controller.BattleController;
import com.mygdx.game.controller.BattleContext;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.generator.being.HeroFactory;
import com.mygdx.game.world.utils.PlayerUtils;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class SimulatorTest extends GameTest {

    List<Player> players = new ArrayList<>();
    HashMap<Player, Integer> playerWins = new HashMap<>();

        @Test
    public void test100PlayersFights() {

        for (int i = 0; i < 20; i++) {
            Player player = new PlayerBuilder()
                    .newDummyPlayer()
                    .heroes(HeroFactory.createRandomHeroes(1, new HeroCreationParameters()))
                    .build();

            players.add(player);
            playerWins.put(player, 0);
        }

        int maxLevel = 1;

        for (int k = 0; k < maxLevel; k++) {
            for (int i = 0; i < players.size(); i++) {
                for (int j = i + 1; j < players.size(); j++) {
                    BattleController battleController = new BattleController();

                    final Player player1 = players.get(i);
                    final Player player2 = players.get(j);

                    player1.getHeroes().stream().forEach(hero -> hero.rejuvenate());
                    player2.getHeroes().stream().forEach(hero -> hero.rejuvenate());

                    final BattleContext battleContext = battleController.evaluateBattle(player1.getHeroes(), player2.getHeroes(), null);

                    PlayerUtils.addCombatVictory(battleContext, player1, player2);
                }
            }

            //players.stream().forEach(player -> player.getHeroes().forEach(hero -> ));
        }

        players = players.stream().sorted(Comparator.comparingInt(Player::getArenaWins)).collect(Collectors.toList());

        System.out.println("END");

    }

    @Test
    public void testNaturalSelection() {

        int maxLevel = 5;
        int playersInvolved = (int) Math.pow(2, maxLevel);

        for (int i = 0; i < playersInvolved; i++) {
            Player player = new PlayerBuilder()
                    .newDummyPlayer()
                    .heroes(HeroFactory.createRandomHeroes(4, new HeroCreationParameters()))
                    .build();
            playerWins.put(player, 0);
        }

        BattleController battleController = new BattleController();

        for (int i = 0; i < maxLevel; i++) {
            players = new ArrayList<>();
            final int finalI = i;
            playerWins.keySet().stream().forEach(player -> {
                if (playerWins.get(player) == finalI) players.add(player);
            });

            players.stream().forEach(player -> player.getHeroes().stream().forEach(hero -> hero.rejuvenate()));

            for (int j = 0; j < players.size() - 1; j = j+2) {
                final BattleContext battleContext = battleController.evaluateBattle(players.get(j).getHeroes(), players.get(j + 1).getHeroes());
                PlayerUtils.addCombatVictory(battleContext, players.get(j), players.get(j+1));
            }
            players.stream().forEach(player -> playerWins.put(player, player.getProperties().get(Player.PlayerProperty.ARENA_WINS)));
            //players.stream().forEach(player -> player.getHeroes().stream().forEach(hero -> hero));

            System.out.println("END OF LEVEL");
        }


        System.out.println("END");

    }


}
