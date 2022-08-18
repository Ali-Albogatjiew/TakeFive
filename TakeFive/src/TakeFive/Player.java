package TakeFive;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Player {

    public static final int MAX_HAND_SIZE = 10;
    public static final int MAX_PENALTY_POINTS = 66;
    public static final Player NO_OWNER = new Player("NO_OWNER");

    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private final List<Card> penaltyCards = new ArrayList<>();
    private int penaltyPoints = 0;


    public Player(String name) {
        this.name = name;
    }


    public List<Card> chooseRow(Card chosenCard) {
        Field.showField();
        System.out.println(name + "'s card: " + chosenCard);
        int rowNumber = checkRowNumberInput();
        return Field.getRow(rowNumber);
    }

    public Card chooseCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(name + "'s turn, press enter to continue");
        scanner.nextLine();

        Field.showField();
        showHand();
        int cardNumber = checkCardNumberInput();

        int chosenCardIndex = hand.indexOf(new Card(cardNumber));
        Card chosenCard = hand.get(chosenCardIndex);
        hand.remove(chosenCardIndex);

        return chosenCard;
    }

    public void showHand() {
        StringBuilder handStringBuilder = new StringBuilder(name + "'s hand: ");

        for (Card card : hand) {
            handStringBuilder.append(card);
        }
        String handString = handStringBuilder.toString();
        System.out.println(handString);
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }


    public List<Card> getPenaltyCards() {
        return penaltyCards;
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public void setPenaltyPoints(int penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }

    private int checkRowNumberInput() {
        int rowNumber = 0;
        rowNumber = checkRowNumberInputMismatch(rowNumber);
        rowNumber = checkRowNumberRange(rowNumber);
        return rowNumber;
    }

    private int checkRowNumberInputMismatch(int rowNumber) {
        while(rowNumber == 0) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(name + ", choose a row to which to prepend your card, enter the row number:");
                rowNumber = scanner.nextInt();
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Please enter a NUMBER");
            }
        }
        return rowNumber;
    }

    private int checkRowNumberRange(int rowNumber) {
        while (rowNumber < 1 || rowNumber > 4) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("There is no row with the entered number, please select a number from 1 to 4");
            System.out.println("Choose a row to which to prepend your card, enter the row number:");
            rowNumber = scanner.nextInt();
        }
        return rowNumber;
    }

    private int checkCardNumberInput() {
        int cardNumber = 0;
        cardNumber = checkCardNumberInputMismatch(cardNumber);
        cardNumber = checkCardNumberHandContains(cardNumber);
        return cardNumber;
    }

    private int checkCardNumberInputMismatch (int cardNumber) {
        while(cardNumber == 0) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose a card from your hand by entering its number:");
                cardNumber = scanner.nextInt();
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Please enter a NUMBER");
            }
        }
        return cardNumber;
    }

    private int checkCardNumberHandContains (int cardNumber) {
        while (!hand.contains(new Card(cardNumber))) {
            Scanner scanner = new Scanner(System.in);
            Field.showField();
            showHand();
            System.out.println("Your hand does not contain a card with the entered number");
            System.out.println("Choose a card from your hand by entering its number:");
            cardNumber = scanner.nextInt();
        }
        return cardNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
