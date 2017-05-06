package ru.spbau.mit.core;

import com.googlecode.lanterna.TerminalPosition;
import ru.spbau.mit.core.items.Item;
import ru.spbau.mit.core.mobs.Mob;
import ru.spbau.mit.utils.Direction;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class TurnManager {
    private TurnManager() {}

    private static void handleFight(GameState mGameState) throws IOException {
        Player player = mGameState.getPlayer();
        Mob opponent = mGameState.getAttackingMob().get();

        player.fight(opponent);

        if (player.isDead()) {
            mGameState.setGameOver(true);
        }

        if (opponent.isDead()) {
            opponent.clear();
        }
    }

    public static void handlePlayersTurn(GameState mGameState) throws IOException {
        InputHandler.handleInput().doAction(mGameState);
        Player player = mGameState.getPlayer();

        if (mGameState.isFightSituation()) {
            handleFight(mGameState);
        }

        Item maybeItem = mGameState.getCurrentMap().getItemOnPosition(player.getCurrentPosition());
        if (maybeItem != null) {
            player.pickUp(maybeItem);
            maybeItem.clear();
        }
    }


    // TODO : Don't know where to put this random walk

    private static void moveMobs(GameState gameState) throws IOException {
        Random random = new Random();
        Set<Mob> mobs = gameState.getMobs();
        Map map = gameState.getCurrentMap();

        for (Mob mob : mobs) {
            Direction direction = Direction.values()[random.nextInt(4)];
            TerminalPosition newPosition = mob.maybeMove(direction);

            // TODO : fix bug that Mob can delete an obstacle (check Drawable comparing!!!)
            while (!map.isCellFree(newPosition)) {
                direction = Direction.values()[random.nextInt(4)];
                newPosition = mob.maybeMove(direction);
            }

            map.replace(mob.getCurrentPosition(), newPosition);
            mob.redrawTo(newPosition);
        }
    }

    public static void handleAIsTurn(GameState gameState) throws IOException {
        moveMobs(gameState);
        gameState.setPlayersTurn(true);
    }
}