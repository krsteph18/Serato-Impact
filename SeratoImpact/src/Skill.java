public class Skill {
    private String skillID;
    private String name;
    private DiceCost cost;
    private int energyCost;

    public Skill(String skillID, String name, DiceCost cost, int energyCost) {
        this.skillID = skillID;
        this.name = name;
        this.cost = cost;
        this.energyCost = energyCost;
    }

    public String getSkillID() {
        return skillID;
    }

    public String getName() {
        return name;
    }

    public DiceCost getCost() {
        return cost;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public void setSkillID(String skillID) {
        this.skillID = skillID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(DiceCost cost) {
        this.cost = cost;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }
    public void activate(GameContext gg) {

    }
}
