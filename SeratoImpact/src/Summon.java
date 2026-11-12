public class Summon {
    private String summonId;
    private String name;
    private int usages;

    public void trigger(GameContext context) {

    }

    public void decreaseUsage() {
        this.usages -= 1;
        this.isExpired();
    }

    public boolean isExpired() {
        if (this.usages == 0) {
            return true;
        }
        return false;
    }
}
