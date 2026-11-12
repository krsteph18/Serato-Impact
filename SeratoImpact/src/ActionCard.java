public abstract class ActionCard extends Card {
    private DiceCost cost;
    private String[] group;

    public DiceCost getCost() {
        return cost;
    }

    public String[] getGroup() {
        return group;
    }

    public abstract void play(GameContext context);
}
