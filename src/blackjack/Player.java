package blackjack;

import java.util.ArrayList;

public final class Player {
    public static final boolean FIRST_HAND = false;
    public static final boolean SECOND_HAND = true;

    private static int balance = 1000;
    private static int bet = 0;
    private static boolean hasTurn = true;
    private static boolean hasBlackjack = false;
    private static boolean hasSurrendered = false;
    private static ArrayList<Card> firstHand = new ArrayList<Card>();
    private static ArrayList<Card> secondHand = new ArrayList<Card>();
    private static boolean isHandSplit = false;
    private static boolean firstHandHasTurn = true;
    private static boolean secondHandHasTurn = false;
    private static boolean currentHand = FIRST_HAND;


    public static void increaseBalance(int value) {
        balance += value;
    }

    public static int getBalance() {
        return balance;
    }

    public static void checkZeroBalance() {
        if (Player.getBalance() == 0) {
            System.out.println("Seems you've lost all your money, therefore your balance has been reset to 1000. Enjoy! :D");
            balance = 1000;
            System.out.println();
        }
    }

    public static void resetBet() {
        bet = 0;
    }

    public static void halveBet() {
        bet /= 2;
    }

    public static boolean setBet(String value) {
        try {
            bet = Integer.parseInt(value);
            if (bet <= 0 || bet > balance) {
                return false;
            }
            else {
                balance -= bet;
                return true;
            }
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getBet() {
        return bet;
    }

    public static boolean hasTurn() {
        return hasTurn;
    }

    public static boolean hasBlackjack() {
        return hasBlackjack;
    }

    public static boolean hasSurrendered() {
        return hasSurrendered;
    }

    public static boolean isHandSplit() {
        return isHandSplit;
    }

    public static boolean getCurrentHand() {
        return currentHand;
    }

    public static ArrayList<Card> getCards(boolean hand) {
        if (hand == FIRST_HAND) {
            return firstHand;
        }
        else {
            return secondHand;
        }
    }

    public static void reset() {
        firstHand.clear();
        secondHand.clear();
        hasBlackjack = false;
        hasSurrendered = false;
        isHandSplit = false;
        firstHandHasTurn = true;
        secondHandHasTurn = false;
        currentHand = FIRST_HAND;
    }

    public static void hit(boolean hand) {
        Card card = Deck.getRandomCard();
        if (hand == FIRST_HAND) {
            firstHand.add(card);
            if (Card.getCardsTotal(firstHand, false) >= 21) {
                firstHandHasTurn = false;
                currentHand = SECOND_HAND;
            }
        }
        else {
            secondHand.add(card);
            if (Card.getCardsTotal(secondHand, false) >= 21) {
                secondHandHasTurn = false;
            }
        }
        hasTurn = firstHandHasTurn || secondHandHasTurn;
    }

    public static void stay(boolean hand) {
        if (hand == FIRST_HAND) {
            firstHandHasTurn = false;
            currentHand = SECOND_HAND;
        }
        else {
            secondHandHasTurn = false;
        }
        hasTurn = firstHandHasTurn || secondHandHasTurn;
    }

    public static void doubleDown() {
        balance -= bet;
        bet *= 2;
        Player.hit(Player.FIRST_HAND);
        hasTurn = false;
    }

    public static void split() {
        isHandSplit = true;
        firstHandHasTurn = true;
        secondHandHasTurn = true;
        secondHand.add(firstHand.remove(1));
        Player.hit(Player.FIRST_HAND);
        Player.hit(Player.SECOND_HAND);
    }

    public static void surrender() {
        hasSurrendered = true;
        hasTurn = false;
    }

    public static void blackjack() {
        hasBlackjack = true;
        hasTurn = false;
    }

    private Player() {

    }
}
