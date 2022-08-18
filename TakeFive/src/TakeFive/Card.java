package TakeFive;

public class Card {

    public static final Card EMPTY_CARD = new Card(0);

    private final int number;
    private final int penaltyPoints;
    private Player owner = Player.NO_OWNER;


    public Card(int number) {
        this.number = number;

        if (number == 55)
            this.penaltyPoints = 7;
        else if (number % 11 == 0)
            this.penaltyPoints = 5;
        else if (number % 10 == 0)
            this.penaltyPoints = 3;
        else if (number % 5 == 0)
            this.penaltyPoints = 2;
        else
            this.penaltyPoints = 1;
    }

    public int getNumber() {
        return number;
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return " [" + number + "|" + penaltyPoints + "] ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return number == card.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
