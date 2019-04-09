package game;

public class Player {

    private String name;
    private boolean human;
    private Store store;
    private Pocket [] pockets;

    public Player(String name, Boolean humanPlayer, Store store, Pocket[] pockets) {
        this.name = name;
        this.human = humanPlayer;
        this.store = store;
        this.pockets = pockets;
    }

    public Player(Player player) {
        this.name = player.name;
        this.human = player.human;
        this.store = new Store(player.store.getStore());
        Pocket [] copyPockets = new Pocket[player.getPockets().length];
        for (int i = 0; i < player.getPockets().length; i++) {
            copyPockets [i] = new Pocket(player.getPocket(i));
        }
        this.pockets = copyPockets;
    }

    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return human;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Pocket[] getPockets() {
        return pockets;
    }

    public Pocket getPocket (int index) {
        return pockets[index];
    }

    public void setPockets(Pocket[] pockets) {
        this.pockets = pockets;
    }
}
