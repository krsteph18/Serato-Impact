public abstract class ActionCard extends Card {
    private DiceCost cost;
    private String[] group;


    public abstract void play(GameContext context);
}
