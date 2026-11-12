public class DicePool {
    private String map;
    private int dice;

    public DicePool(String map, int dice) {
        this.map = map;
        this.dice = dice;
    }

    public String getMap() {
        return map;
    }

    public int getDice() {
        return dice;
    }

    public boolean spend(DiceCost dc) {
        return false;
    }
}
