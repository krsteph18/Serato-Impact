public class CharacterCard {
    private int maxHP;
    private int currentHP;
    private int energy;
    private String element;
    private String factions[];

    public CharacterCard(int maxHP, int currentHP, int energy, String element, String[] factions) {
        this.maxHP = maxHP;
        this.currentHP = currentHP;
        this.energy = energy;
        this.element = element;
        this.factions = factions;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getEnergy() {
        return energy;
    }

    public String getElement() {
        return element;
    }

    public String[] getFactions() {
        return factions;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setFactions(String[] factions) {
        this.factions = factions;
    }

    public void takeDmg(int damage) {

    }
    public void useSkill(Skill s) {

    }
    public void heal(int hpGranted) {

    }
    public boolean isDefeated() {
        return false;
    }
}
