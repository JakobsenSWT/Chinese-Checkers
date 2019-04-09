package ai;

public class Move {
    
    private int PocketNumber;
    private int value;
    
    public Move (int pocketNumber, int value) {
        this.PocketNumber = pocketNumber;
        this.value = value;
    }

    public int getPocketNumber() {
        return PocketNumber;
    }

    public void setPocketNumber(int pocketNumber) {
        PocketNumber = pocketNumber;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
