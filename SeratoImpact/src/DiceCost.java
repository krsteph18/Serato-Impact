public class DiceCost {
    private int elementalDice;
    private String element;
    private int unalignedDice;

    public DiceCost(int elementalDice, String element, int unalignedDice) {
        this.elementalDice = elementalDice;
        this.element = element;
        this.unalignedDice = unalignedDice;
    }

    public int getElementalDice() {
        return elementalDice;
    }

    public String getElement() {
        return element;
    }

    public int getUnalignedDice() {
        return unalignedDice;
    }

    public void setElementalDice(int elementalDice) {
        this.elementalDice = elementalDice;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setUnalignedDice(int unalignedDice) {
        this.unalignedDice = unalignedDice;
    }
    public boolean canPlay(DicePool dp) {
        return false;
    }
}
