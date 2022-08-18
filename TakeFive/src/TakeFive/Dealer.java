package TakeFive;

import java.util.*;

public final class Dealer {

    private final List<Player> players = new ArrayList<>();
    private final Deck deck = Deck.createDeck();
    private final Card[] deckCards = deck.getCards();
    private final List<Card> chosenCards = new ArrayList<>();

    public Dealer() {}

    public void showAllPenaltyPoints() {
        for (Player player : players)
            System.out.println(player.getName() + " has " + player.getPenaltyPoints() + " penalty points");
    }

    public boolean gameHasEnded() {
        for (Player player : players) {
            if (player.getPenaltyPoints() >= Player.MAX_PENALTY_POINTS)
                return true;
        }
        return false;
    }

    public void allCardsToDeck() {
        updatePenaltyPoints();
        removePenaltyCardsFromPlayers();
        removeCardsFromField();
        resetDeck();
    }

    public void startTrick() {
        takeChosenCards();
        sortChosenCards();

        mainTrickLoop();

        chosenCards.clear();
        updatePenaltyPoints();
        Field.showField();
        showAllPenaltyPoints();
    }

    public void prepareField() {
        for (int rowNumber = 1; rowNumber <= 4; rowNumber++) {
            List<Card> row = Field.getRow(rowNumber);
            Card topCard = takeTopCardFromDeck();

            row.add(topCard);
        }
    }

    public void dealCards() {
        for (Player player : players) {
            for (int card = 0; card < Player.MAX_HAND_SIZE; card++) {
                List<Card> hand = player.getHand();
                Card topCard = takeTopCardFromDeck();

                topCard.setOwner(player);
                hand.add(topCard);
            }
        }
    }

    public void addPlayers() {
        List<String> playerNames = getPlayerNames();

        for (String playerName : playerNames) {
            Player player = new Player(playerName);
            players.add(player);
        }
    }

    public void shuffleDeck(int shufflePower) {
        Card[] shuffledCards = deckCards;

        for (int shuffleRound = 0; shuffleRound < shufflePower; shuffleRound++) {
            Card[] splitDeck1 = splitDeck(shuffledCards, 1);
            Card[] splitDeck2 = splitDeck(shuffledCards, 2);

            shuffleSplitDeck(shuffledCards, splitDeck1, splitDeck2);
        }
    }

    private void removePenaltyCardsFromPlayers() {
        for (Player player : players) {
            List<Card> penaltyCards = player.getPenaltyCards();
            for (int index = 0; index < penaltyCards.size(); index++) {
                penaltyCards.remove(penaltyCards.get(index));
            }
        }
    }

    private void removeCardsFromField() {
        for (int rowNumber = 1; rowNumber <= 4; rowNumber++) {
            List<Card> row = Field.getRow(rowNumber);
            for (int index = 0; index < row.size(); index++) {
                row.remove(row.get(index));
            }
        }
    }

    private void resetDeck() {
        for (int index = 0; index < Deck.DECK_SIZE; index++) {
            int cardNumber = index + 1;
            deckCards[index] = new Card(cardNumber);
        }
    }

    private void mainTrickLoop() {
        for (Card chosenCard : chosenCards) {
            Player player = chosenCard.getOwner();
            boolean chosenCardSmallerThanActiveCards = chosenCard.getNumber() < Field.lowestActiveCardNumber();

            if (chosenCardSmallerThanActiveCards) {
                chosenCardSmallerThanActiveCards(player, chosenCard);
            } else {
                chosenCardLargerThanOneActiveCard(player, chosenCard);
            }
        }
    }

    private void chosenCardSmallerThanActiveCards(Player player, Card chosenCard) {
        List<Card> targetRow = player.chooseRow(chosenCard);

        givePenaltyCards(player, targetRow);
        placeChosenCard(chosenCard, targetRow);
    }

    private void chosenCardLargerThanOneActiveCard(Player player, Card chosenCard) {
        List<Card> targetRow = Field.findRowPlacement(chosenCard);
        boolean targetRowFull = targetRow.size() == Field.TAKE_FIVE;

        if (targetRowFull) {
            givePenaltyCards(player, targetRow);
            placeChosenCard(chosenCard, targetRow);
        } else
            placeChosenCard(chosenCard, targetRow);
    }

    private void updatePenaltyPoints() {
        for (Player player : players) {
            int newPenaltyPoints = 0;
            List<Card> penaltyCards = player.getPenaltyCards();

            for (Card penaltyCard : penaltyCards) {
                newPenaltyPoints += penaltyCard.getPenaltyPoints();
            }

            int penaltyPoints = player.getPenaltyPoints();
            int updatedPenaltyPoints = penaltyPoints + newPenaltyPoints;

            player.setPenaltyPoints(updatedPenaltyPoints);
        }
    }

    private void placeChosenCard(Card chosenCard, List<Card> targetRow) {
        targetRow.add(chosenCard);
        chosenCard.setOwner(Player.NO_OWNER);
    }

    private void givePenaltyCards(Player player, List<Card> targetRow) {
        List<Card> penaltyCards = player.getPenaltyCards();
        penaltyCards.addAll(targetRow);
        for (int index = 0; index < targetRow.size(); index++) {
            targetRow.remove(targetRow.get(index));
        }
    }

    private void sortChosenCards() {
        List<Card> sortedChosenCards = new ArrayList<>();

        while (chosenCards.size() > 1) {
            Card lowestNumberCard = chosenCards.get(0);

            for (int index = 0; index < chosenCards.size() - 1; index++) {
                if (lowestNumberCard.getNumber() > chosenCards.get(index + 1).getNumber())
                    lowestNumberCard = chosenCards.get(index + 1);
            }
            sortedChosenCards.add(lowestNumberCard);
            chosenCards.remove(lowestNumberCard);
        }
        Card lowestNumberCard = chosenCards.get(0);
        sortedChosenCards.add(lowestNumberCard);
        chosenCards.remove(lowestNumberCard);

        chosenCards.addAll(sortedChosenCards);
    }

    private void takeChosenCards() {
        for (Player player : players) {
            Card card = player.chooseCard();
            chosenCards.add(card);
        }
    }

    private List<String> getPlayerNames() {
        int numberOfPlayers = getNumberOfPlayers();

        List<String> playerNames = new ArrayList<>();

        for (int player = 0; player < numberOfPlayers; player++) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter player name:");
            String playerName = scanner.nextLine();
            while (playerName.equals("")) {
                System.out.println("You have not entered anything");
                System.out.println("Again, enter player name:");
                playerName = scanner.nextLine();
            }
            playerNames.add(playerName);
        }
        return playerNames;
    }

    private int getNumberOfPlayers() {

        int numberOfPlayers = 0;

        while (numberOfPlayers == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter number of players:");
            try {
                numberOfPlayers = scanner.nextInt();
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Please enter a NUMBER");
            }
        }


        while (numberOfPlayers < 2 || numberOfPlayers > 10) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Number of players cannot be less than 2 or more than 10");
            System.out.println("Enter number of players:");
            numberOfPlayers = scanner.nextInt();
        }

        return numberOfPlayers;
    }

    private Card takeTopCardFromDeck() {
        int index = Deck.DECK_SIZE - 1;
        while (deckCards[index].getNumber() == 0)
            index--;
        Card topCard = deckCards[index];
        deckCards[index] = Card.EMPTY_CARD;

        return topCard;
    }

    private Card[] splitDeck(Card[] shuffledCards, int splitDeckNumber) {
        if (splitDeckNumber == 1) {
            Card[] splitDeck1 = new Card[Deck.DECK_SIZE / 2];
            System.arraycopy(shuffledCards, 0, splitDeck1, 0, Deck.DECK_SIZE / 2);
            return splitDeck1;
        } else if (splitDeckNumber == 2) {
            Card[] splitDeck2 = new Card[Deck.DECK_SIZE / 2];
            System.arraycopy(shuffledCards, Deck.DECK_SIZE / 2, splitDeck2, 0, Deck.DECK_SIZE / 2);
            return splitDeck2;
        } else
            throw new RuntimeException("Invalid split deck number");
    }

    private void shuffleSplitDeck (Card[] shuffledCards, Card[] splitDeck1, Card[] splitDeck2) {
        Random random = new Random();

        int splitDeck1Counter = 51;
        int splitDeck2Counter = 51;

        for (int index = 0; index < Deck.DECK_SIZE; index++) {
            boolean addFromSplitDeck1 = random.nextBoolean();
            boolean splitDeck1HasCards = splitDeck1Counter >= 0;
            boolean splitDeck2HasCards = splitDeck2Counter >= 0;

            if (addFromSplitDeck1 && splitDeck1HasCards) {
                shuffledCards[index] = splitDeck1[splitDeck1Counter];
                splitDeck1Counter--;
            } else if (splitDeck2HasCards) {
                shuffledCards[index] = splitDeck2[splitDeck2Counter];
                splitDeck2Counter--;
            } else {
                shuffledCards[index] = splitDeck1[splitDeck1Counter];
                splitDeck1Counter--;
            }
        }
    }

}
