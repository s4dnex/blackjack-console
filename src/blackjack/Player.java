package blackjack;

import java.util.ArrayList;

public final class Player {

    private static int balance = 1000;
    private static int bet = 0;
    private static ArrayList<Card> firstHand = new ArrayList<Card>();
    private static ArrayList<Card> secondHand = new ArrayList<Card>();
    public static final boolean FIRST_HAND = false;
    public static final boolean SECOND_HAND = true;


    public static void increaseBalance(int value) {
        balance += value;
    }

    public static void decreaseBalance(int value) {
        balance -= value;
    }

    public static int getBalance() {
        return balance;
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

    public static ArrayList<Card> getCards(boolean hand) {
        if (hand == FIRST_HAND) {
            return firstHand;
        }
        else {
            return secondHand;
        }
    }

    public static void hit(Card card, boolean hand) {
        if (hand == FIRST_HAND) {
            firstHand.add(card);
        }
        else {
            secondHand.add(card);
        }
    }

    public static void stay() {

    }

    public static void doubleDown() {

    }

    public static void split() {

    }

    private Player() {

    }
}
