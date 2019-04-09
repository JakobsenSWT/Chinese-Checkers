package game;

public class Pocket {

    private int marbels;

    public Pocket (int marbels) {
        this.marbels = marbels;
    }

    public Pocket (Pocket pocket) {
        this.marbels = pocket.marbels;
    }

    public int getMarbels() {
        return marbels;
    }

    public void setMarbels(int marbels) {
        this.marbels = marbels;
    }

    public void addMarbels () {
        this.marbels++;
    }
}
