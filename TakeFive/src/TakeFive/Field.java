package TakeFive;

import java.util.ArrayList;
import java.util.List;

public class Field {

    public static final int TAKE_FIVE = 5;

    private static final List<Card> row1 = new ArrayList<>();
    private static final List<Card> row2 = new ArrayList<>();
    private static final List<Card> row3 = new ArrayList<>();
    private static final List<Card> row4 = new ArrayList<>();
    private static final List<Card> dummyRow = new ArrayList<>();

    public static List<Card> getRow(int rowNumber) {
        return switch (rowNumber) {
            case 1 -> row1;
            case 2 -> row2;
            case 3 -> row3;
            case 4 -> row4;
            default -> throw new RuntimeException("Invalid row number");
        };
    }

    public static List<Card> findRowPlacement(Card chosenCard) {
        List<Card> targetRow = new ArrayList<>();
        int smallestDistance = Deck.DECK_SIZE;

        for (int rowNumber = 1; rowNumber <= 4; rowNumber++) {
            List<Card> row = getRow(rowNumber);
            Card lastCard = row.get(row.size() - 1);

            int cardsDistance = chosenCard.getNumber() - lastCard.getNumber();

            if (smallestDistance > cardsDistance && cardsDistance > 0) {
                smallestDistance = cardsDistance;
                targetRow = row;
            }
        }
        return targetRow;
    }

    public static int lowestActiveCardNumber() {
        Card lowestCard = row1.get(row1.size() - 1);
        Card competitorCard;

        for (int nextRowNumber = 2; nextRowNumber <= 4; nextRowNumber++) {
            List<Card> nextRow = getRow(nextRowNumber);

            competitorCard = nextRow.get(nextRow.size() -1);

            if (lowestCard.getNumber() > competitorCard.getNumber()) {
                lowestCard = competitorCard;
            }
        }
        return lowestCard.getNumber();
    }

    public static void showField() {
        for (int rowNumber = 1; rowNumber <= 4; rowNumber++) {
            StringBuilder rowStringBuilder = new StringBuilder("Row ");
            rowStringBuilder.append(rowNumber).append(": ");

            List<Card> row = getRow(rowNumber);
            for (Card card : row)
                rowStringBuilder.append(card);
            String rowString = rowStringBuilder.toString();
            System.out.println(rowString);
        }
    }
}
