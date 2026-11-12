public abstract class SupportCard extends ActionCard {
    private String supportType;
    private int usages;

    public SupportCard(DiceCost cost, String[] group, String supportType, int usages) {
        super(cost, group);
        this.supportType = supportType;
        this.usages = usages;
    }

    @Override
    public void play(GameContext context) {

    }
}
