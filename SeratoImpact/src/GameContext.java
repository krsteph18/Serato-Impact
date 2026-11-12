import java.util.*;
public class GameContext {
    private int currentRound = 1;
    private CharacterCard activeCharacter;
    private List<Summon> activeSummons = new ArrayList<>();

    public void addSummon(Summon summon) {
        activeSummons.add(summon);
    }

    public void nextRound() {
        currentRound++;
    }
}
