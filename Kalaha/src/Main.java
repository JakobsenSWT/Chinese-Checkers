import controller.GameController;
import game.Player;
import game.Pocket;
import game.Store;

import java.awt.*;

public class Main {

    /** game settings */
    static final int HUMAN_PLAYERS = 0;
    static final int POCKETS_FOR_EACH_PLAYER = 6;
    static final int MARBELS_IN_EACH_POCKET = 4;
    final static String [] PLAYER_NAMES = {"Player 1", "Player 2"};
    static Player[] PLAYERS;

    public static void main(String[] args) {
        EventQueue.invokeLater(Game::new);
    }

    public static class Game {
        GameController gameController;

        Game () {
            PLAYERS = new Player[2];
            initializeBoard();

            gameController = new GameController(HUMAN_PLAYERS, PLAYERS);
            gameController.startGame();
        }

        void initializeBoard() {
            for (int i = 0; i < 2; i++) {
                Pocket[] pockets = new Pocket[POCKETS_FOR_EACH_PLAYER];
                for (int pocket = 0; pocket < POCKETS_FOR_EACH_PLAYER; pocket++)
                    pockets[pocket] = new Pocket(MARBELS_IN_EACH_POCKET);
                if (HUMAN_PLAYERS > i)
                    PLAYERS [i] = new Player(PLAYER_NAMES[i], true, new Store(0), pockets);
                else
                    PLAYERS [i] = new Player(PLAYER_NAMES[i], false, new Store(0), pockets);
            }
        }
    }
}
