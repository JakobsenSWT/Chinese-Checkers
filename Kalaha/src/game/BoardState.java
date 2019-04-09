package game;

public class BoardState {

    private int currentPlayer;
    private Player[] players;

    public BoardState(int currentPlayer, Player[] players) {
        this.currentPlayer = currentPlayer;
        this.players = players;
    }

    public BoardState(BoardState state) {
        this.currentPlayer = state.currentPlayer;
        Player [] copyPlayers = new Player[state.players.length];
        for (int i = 0; i < state.players.length; i++) {
            copyPlayers [i] = new Player(state.getPlayer(i));
        }
        this.players = copyPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player[] getPlayers () {
        return players;
    }

    public Player getPlayer(int playerIndex) {
        return players[playerIndex];
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

}
