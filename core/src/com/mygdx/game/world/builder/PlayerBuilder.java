package com.mygdx.game.world.builder;

import com.mygdx.game.world.entity.universe.Universe;
import com.mygdx.game.world.entity.universe.world.Town;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.being.hero.Hero;
import com.mygdx.game.world.entity.game.Player;
import com.mygdx.game.world.enums.game.Difficulty;
import com.mygdx.game.world.enums.game.GameState;
import com.mygdx.game.world.enums.player.AIAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerBuilder {

    private static List<String> takenNames = new ArrayList<>();

    private Player player;

    public PlayerBuilder() {
        this.player = new Player();
    }

    public Player build() {
        return player;
    }

    public PlayerBuilder newDummyPlayer() {
        setNameWithIndex();
        player.setGameState(GameState.MAIN_MENU);

        player.setGems(0);
        player.setSouls(0);
        player.setGold(0);

        return this;
    }

    private void setNameWithIndex() {
        String name = Player.PLAYER_CODE;
        int index = 0;

        while (takenNames.contains(name)) {
            int indexLength = index > 0 ? index > 9 ? index > 99 ? index > 999 ? 4 : 3 : 2 : 1 : 0;
            index++;
            name = name.substring(0, name.length() - indexLength) + index;
        }

        takenNames.add(name);
        player.setName(name);
    }

    public PlayerBuilder newHumanPlayer(Difficulty difficulty) {
        setNameWithIndex();
        player.setGameState(GameState.MAIN_MENU);

        player.setGems(difficulty.getStartingGems());
        player.setSouls(difficulty.getStartingSouls());
        player.setGold(difficulty.getStartingGold());

        return this;
    }

    public PlayerBuilder newAIPlayer(Difficulty difficulty) {
        player.setName(PLAYER_NICK.values()[new Random().nextInt(PLAYER_NICK.values().length)].name());
        player.setGameState(GameState.MAIN_MENU);
        player.setAIAction(AIAction.values()[new Random().nextInt(AIAction.values().length)]);
        player.setGems(difficulty.getStartingGems());
        player.setSouls(difficulty.getStartingSouls());
        player.setGold(difficulty.getStartingGold());

        return this;
    }

    public PlayerBuilder addHero(Hero hero) {
        player.getHeroes().add(hero);

        return this;
    }

    public PlayerBuilder gameState(GameState gameState) {
        player.setGameState(gameState);

        return this;
    }

    public PlayerBuilder heroes(List<Hero> heroes) {
        player.setHeroes(heroes);

        return this;
    }

    public PlayerBuilder playersWorld(World world) {
        player.setPlayersWorld(world);
        player.setPlayersWorldCode(world.getCode());
        player.setSelectedWorld(world);
        player.setSelectedWorldCode(world.getCode());

        return this;
    }

    public PlayerBuilder universe(final Universe universe) {
        universe.getPlayers().add(player);

        return this;
    }

    private enum PLAYER_NICK {
        Adam,
        Ondra,
        Pepa,
        Depa,
        Klito,
        Ris,
        Prdik,
        Usak,
        Blink,
        Blinkacek,
        Dortik,
        Smisek,
        Koblizek,
        Mrkvacek,
        Dealer,
        Dino,
        Magic,
        Comet,
        Skin,
        Peanut,
        Honesty,
        Oracle,
        Torch,
        Captain,
        Slayer,
        Bash,
        Cheery,
        Deedee,
        Sheep,
        Loco,
        Angel,
        Tug,
        Dodo,
        Dagger,
        Hawkeye,
        Groovy,
        Scruffy,
        Dynamite,
        Slick,
        Lightning,
        Frosty,
        Fuzzy,
        Bud
    }
}
