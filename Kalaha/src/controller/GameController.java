package controller;

import java.util.ArrayList;
import java.util.Scanner;

import game.BoardState;
import game.Player;
import ai.AI;
import game.Pocket;

public class GameController {

    private int HUMAN_PLAYERS;
    private Player [] PLAYERS;

    /** Log with all states made in the game */
    private ArrayList<BoardState> states = new ArrayList<>();
    private BoardState boardState;

    /** Change who starts the game */
    private int currentPlayer = 0;

    private AI ai;

    /**
     * Constructor to fill out game variables
     * @param humans determine number of human players
     * @param players Contains the list of players initialized by Main.class
     */
    public GameController (int humans, Player [] players) {
        this.HUMAN_PLAYERS = humans;
        this.PLAYERS = players;
    }

    /**
     * Empty constructor for AI
     */
    public GameController () { }

    /** Starts AI and play */
    public void startGame() {
        if (HUMAN_PLAYERS < 2)
            ai = new AI();

        playTurn();
    }


    private void playTurn() {
        /* Saves the current state */
        this.boardState = new BoardState(currentPlayer, PLAYERS);

        if (!boardState.getPlayer(boardState.getCurrentPlayer()).isHuman()) {
            move(boardState, ai.runAIClient(boardState), false);
        } else {
            drawBoard(this.boardState);
            System.out.println("\n" + boardState.getPlayer(boardState.getCurrentPlayer()).getName()
                    + "'s turn. Which pocket would you like to empty?");
            Scanner input = new Scanner(System.in);

            boolean stalling = true;
            int pocketBuffer = input.nextInt();

            while (stalling) {
                if (pocketBuffer < boardState.getPlayer(boardState.getCurrentPlayer()).getPockets().length)
                    if (boardState.getPlayer(boardState.getCurrentPlayer()).getPocket(pocketBuffer).getMarbels() == 0) {
                        System.out.println("Your chosen pocket is empty, choose another one.");
                        playTurn();
                    } else {
                        move(boardState, pocketBuffer, false);
                        stalling = false;
                    }
                else {
                    System.out.println("Couldn't read input. Choose a pocket number");
                    playTurn();
                }
            }
        }

        /* Check if somebody won */
        if (isGameOver(this.boardState))
            calculateFinalScore();
        else
            nextTurn(this.boardState);
    }

    /**
     * Choose a myPocket to empty and play your turn
     * @param myPocket is choosen to be emptied
     * @return value of corresponding move
     */
    public int move(BoardState boardState, int myPocket, boolean aiMove) {
        int marbelsToMove = boardState.getPlayer(boardState.getCurrentPlayer()).getPocket(myPocket).getMarbels();
        boardState.getPlayer(boardState.getCurrentPlayer()).getPocket(myPocket).setMarbels(0);

        int value = 0;

        int opponentPocket = 0;
        int pocketIndexFromMyPocket = 1;
        boolean addToOwnPockets = true;
        Player player = boardState.getPlayer(boardState.getCurrentPlayer());
        Player nextOpponent = boardState.getPlayer(nextPlayer(boardState));

        for (int i = 0; i < marbelsToMove; i++ ) {
            if (addToOwnPockets) {
                if (myPocket + pocketIndexFromMyPocket < player.getPockets().length) {
                    if (i == marbelsToMove - 1) {
                        if (player.getPocket(myPocket + pocketIndexFromMyPocket).getMarbels() == 0) {
                            player.getStore().setStore(
                                      player.getStore().getStore()
                                    + nextOpponent.getPocket(nextOpponent.getPockets().length
                                    - (myPocket + pocketIndexFromMyPocket)).getMarbels() + 1);

                            nextOpponent.getPocket(nextOpponent.getPockets().length - (myPocket + pocketIndexFromMyPocket)).setMarbels(0);
                        }
                    } else
                        player.getPocket(myPocket + pocketIndexFromMyPocket).addMarbels();
                } else if (myPocket + pocketIndexFromMyPocket >= player.getPockets().length) {
                    player.getStore().addStore();
                    addToOwnPockets = false;
                    opponentPocket = 0;

                    if (i == marbelsToMove - 1)
                        if (isGameOver(boardState))
                            calculateFinalScore();
                        else {
                            if (aiMove) {
                                value = aiExtraMove(boardState);
                            } else if (player.isHuman())
                                playTurn();
                            else
                                value = aiExtraMove(boardState);
                        }
                }
            } else {
                if (opponentPocket < nextOpponent.getPockets().length) {
                    nextOpponent.getPocket(opponentPocket).addMarbels();
                    opponentPocket++;
                } else if (opponentPocket >= nextOpponent.getPockets().length) {
                    addToOwnPockets = true;
                    myPocket = 0;
                    pocketIndexFromMyPocket = 0;
                }
            }
            pocketIndexFromMyPocket++;
        }
        return  value + (player.getStore().getStore() - nextOpponent.getStore().getStore());
    }

    //ToDo
    /*
        KNOWN BUG: Stackoverflow:
        Occurrences: When AI.Depth is an even number
     */
    /**
     * Helper class for AI
     * @param state is the board after the last move
     * @return
     */
    private int aiExtraMove(BoardState state) {
        BoardState state1;
        int value = 0, tempValue = 0, bestValue = 0;

        for (int i = 0; i < state.getPlayer(state.getCurrentPlayer()).getPockets().length; i++) {
            if (state.getPlayer(state.getCurrentPlayer()).getPocket(i).getMarbels() != 0) {
                state1 = new BoardState(state);
                tempValue = move(state1, i, true);
            }

            if (value < tempValue)
                bestValue = tempValue;
        }
        return bestValue;
    }

    /** Give the turn to the next player */
    private void nextTurn(BoardState state) {
        currentPlayer = nextPlayer(state);
        this.boardState.setCurrentPlayer(currentPlayer);
        playTurn();
    }

    /** Determine who is the next player */
    public int nextPlayer(BoardState state) {
        int temp = state.getCurrentPlayer();
        if (temp == 1)
            return 0;
        else
            temp++;
        return temp;
    }

    /**
     * Draw up the board in text format
     * Can be replaced with any form of UI
     */
    public void drawBoard(BoardState state) {
        System.out.print("------------------------------------------------------------\n");
        System.out.print("|\t");
        for (int i = state.getPlayer(nextPlayer(state)).getPockets().length-1; i >= 0; i--) {
            System.out.print(" \t " + i + " \t ");
        }
        System.out.print("\t\t| \t Socket numbers" +
                "\n|\t");
        for (int i = state.getPlayer(nextPlayer(state)).getPockets().length-1; i >= 0; i--) {
            if (state.getPlayer(nextPlayer(state)).getPocket(i).getMarbels() > 9)
                System.out.print(" \t " + state.getPlayer(nextPlayer(state)).getPocket(i).getMarbels() + " ");
            else
                System.out.print(" \t " + state.getPlayer(nextPlayer(state)).getPocket(i).getMarbels() + " \t ");
        }
        System.out.print("\t\t| \t Marbels" +
                "\n|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|" +
                "\n|\t" + state.getPlayer(nextPlayer(state)).getStore().getStore() + " \t "
                + state.getPlayer(nextPlayer(state)).getName() + "'s store \t\t|\t\t"
                + "My store \t  "
                + state.getPlayer(state.getCurrentPlayer()).getStore().getStore() + "\t\t|" +
                "\n|\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|" +
                "\n|\t" );
        for (int i = 0; i < state.getPlayer(state.getCurrentPlayer()).getPockets().length; i++) {
            if (state.getPlayer(state.getCurrentPlayer()).getPocket(i).getMarbels() > 9)
                System.out.print(" \t " + state.getPlayer(state.getCurrentPlayer()).getPocket(i).getMarbels() + " ");
            else
                System.out.print(" \t " + state.getPlayer(state.getCurrentPlayer()).getPocket(i).getMarbels() + " \t ");
        }
        System.out.print("\t\t| \t My Marbels " +
                "\n|\t");
        for (int i = 0; i < state.getPlayer(state.getCurrentPlayer()).getPockets().length; i++) {
            System.out.print(" \t " + i + " \t ");
        }
        System.out.print("\t\t| \t My Socket numbers" +
                "\n");
        System.out.print("------------------------------------------------------------\n");
    }

    /**
     * Looks through the desired state space to check for a winner
     * @param state space
     * @return true if there is a winner, else false
     */
    public boolean isGameOver(BoardState state) {
        for (Player player : state.getPlayers()) {
            int count = 0;
            for (Pocket pocket : player.getPockets()) {
                if (pocket.getMarbels() == 0)
                    count++;
            }
            if (count >= player.getPockets().length)
                return true;
        }
        return false;
    }

    private void calculateFinalScore() {
        for (int i = 0; i < 2; i++)
            for (Pocket pocket : boardState.getPlayer(i).getPockets())
                boardState.getPlayer(i).getStore()
                        .setStore(boardState.getPlayer(i).getStore().getStore() + pocket.getMarbels());

        if(this.boardState.getPlayer(0).getStore().getStore() >
                this.boardState.getPlayer(1).getStore().getStore())
            System.out.println("Player 1 wins! Score: " + boardState.getPlayer(0).getStore().getStore()
                    + " against " + boardState.getPlayer(1).getStore().getStore());
        else
            System.out.println("Player 2 wins! Score: " + boardState.getPlayer(1).getStore().getStore()
                    + " against " + boardState.getPlayer(0).getStore().getStore());
        System.exit(0);
    }
}