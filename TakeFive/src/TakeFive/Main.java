package TakeFive;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Dealer dealer = new Dealer();
        Deck deck = Deck.createDeck();
        dealer.addPlayers();

        while (!dealer.gameHasEnded()) {
            dealer.shuffleDeck(10);
            System.out.println(deck);

            dealer.prepareField();
            dealer.dealCards();
            for (int trickNumber = 0; trickNumber < Player.MAX_HAND_SIZE; trickNumber++) {
                dealer.startTrick();
            }
            dealer.allCardsToDeck();
        }
        System.out.println("Game has ended!");
        dealer.showAllPenaltyPoints();
    }

}
