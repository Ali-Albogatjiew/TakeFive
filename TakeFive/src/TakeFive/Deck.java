package TakeFive;

public class Deck {

    public static final int DECK_SIZE = 104;

    private final Card[] cards = new Card[DECK_SIZE];

    private static Deck deckInstance;


    private Deck() {
        for (int index = 0; index < DECK_SIZE; index++) {
            int cardNumber = index + 1;
            cards[index] = new Card(cardNumber);
        }
    }

    public static Deck createDeck() {
        if (deckInstance == null)
            deckInstance = new Deck();

        return deckInstance;
    }

    public Card[] getCards() {
        return cards;
    }

    @Override
    public String toString(){
        StringBuilder deckStringBuilder = new StringBuilder("Deck: ");

        for (Card card : cards)
            deckStringBuilder.append(card);

        return deckStringBuilder.toString();
    }
}
