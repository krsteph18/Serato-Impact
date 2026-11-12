public abstract class EventCard extends ActionCard {
    private String eventType;

    public EventCard(DiceCost cost, String[] group, String eventType) {
        super(cost, group);
        this.eventType = eventType;
    }
    @Override
    public void play(GameContext context) {

    }
}
