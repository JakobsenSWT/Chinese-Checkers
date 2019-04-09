package ai;

import controller.GameController;
import game.BoardState;

public class AI {

    //TODO Depth search is not fully optimized. Going higher than 7 will result in extremely long wait time.
    private final int DEPTH = 5;

    private GameController game;
    private BoardState state;

    public AI () {
        game = new GameController();
    }

    public int runAIClient (BoardState state) {
        this.state = state;

        int bestMove = 0;
        int value, bestValue = 0;

        /** Runs through all pockets to see which one have the highest outcome */
        for (int pocket = 0; pocket < this.state.getPlayer(this.state.getCurrentPlayer()).getPockets().length; pocket++) {
            if (this.state.getPlayer(this.state.getCurrentPlayer()).getPocket(pocket).getMarbels() != 0) {
                BoardState AIState = new BoardState(state);

                /** Starts a minimax search for each pocket */
                value = minimax(new BoardState(AIState), DEPTH, this.state.getCurrentPlayer());

                /** Sets the best move accordingly */
                if (value >= bestValue)
                    bestMove = pocket;
            }
        }

        return bestMove;
    }

    private int minimax (BoardState state, int depth, int maximizingPlayer) {
        BoardState state1;
        int value = 0, tempValue = 0;
        int minimizingPlayer;

        if (maximizingPlayer == 1)
            minimizingPlayer = 0;
        else
            minimizingPlayer = 1;

        /** If maksimum depth is reach, stop */
        if (depth == 0 /*|| game.isGameOver(state)*/)
            return (state.getPlayer(maximizingPlayer).getStore().getStore() - state.getPlayer(minimizingPlayer).getStore().getStore());

        /** Runs through all possible moves */
        for (int i = 0; i < state.getPlayer(state.getCurrentPlayer()).getPockets().length; i++)
            if (state.getPlayer(state.getCurrentPlayer()).getPocket(i).getMarbels() != 0) {

                /** Sets the same starting table to play from */
                state1 = new BoardState(state);
                state1.setCurrentPlayer(game.nextPlayer(state1));
                value = game.move(state1, i, true);

                /** If AI turn, look for maximum gain */
                if (maximizingPlayer == 1) {
                    maximizingPlayer = 0;
                    tempValue += minimax(new BoardState(state1), depth - 1, maximizingPlayer) ;

                    if (value < tempValue)
                        value = tempValue;

                /** If Player turn, look for minimum gain */
                } else {
                    maximizingPlayer++;
                    tempValue += minimax(new BoardState(state1), depth - 1, maximizingPlayer);

                    if (value > tempValue)
                        value = tempValue;
                }
            }
        return value;
    }
}
