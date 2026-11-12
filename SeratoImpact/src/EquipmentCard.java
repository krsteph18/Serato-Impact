public abstract class EquipmentCard extends ActionCard {
    private String equipmentType;

    public EquipmentCard(DiceCost cost, String[] group, String equipmentType) {
        super(cost, group);
        this.equipmentType = equipmentType;
    }

    @Override
    public void play(GameContext context) {

    }

    public class Weapon extends EquipmentCard {
        public Weapon(DiceCost cost, String[] group, String equipmentType) {
            super(cost, group, equipmentType);
        }
    }

    public class Artifact extends EquipmentCard {
        public Artifact(DiceCost cost, String[] group, String equipmentType) {
            super(cost, group, equipmentType);
        }
    }

    public class Talent extends EquipmentCard {
        public Talent(DiceCost cost, String[] group, String equipmentType) {
            super(cost, group, equipmentType);
        }
    }

    public class Technique extends EquipmentCard {
        public Technique(DiceCost cost, String[] group, String equipmentType) {
            super(cost, group, equipmentType);
        }
    }
}
