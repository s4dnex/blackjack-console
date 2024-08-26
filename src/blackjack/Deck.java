package blackjack;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public final class Deck {
    
    public final static int SIZE = 52;
    private static Random random = new Random();
    private static ArrayList<Card> deck = new ArrayList<Card>();
    private static ArrayList<Card> usedCards = new ArrayList<Card>();

    static {
        for (Suits suit : Suits.values()) {
            for (String cardName : Card.getCardNamesAsSet()) {
                deck.add(new Card(suit, cardName));
            }
        }
    }

    public static void resetDeck() {
        while (usedCards.size() > 0) {
            deck.add(usedCards.removeLast());
        }
    }

    public static void shuffleDeck() {
        for (int i = 0; i < 100; i++) {
            deck.add(random.nextInt(SIZE), deck.remove(random.nextInt(SIZE)));
        }
    }

    public static Card getRandomCard() {
        usedCards.add(deck.remove(random.nextInt(deck.size())));
        return usedCards.getLast();
    }


    private Deck() {
    
    }
}

class Card {
    private Suits suit;
    private String name;
    private int value;


    public static int getCardsTotal(ArrayList<Card> cards) {
        int total = 0;
        int aceCount = 0;

        for (Card card : cards) {
            total += card.value;

            if (card.name.equals("A")) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    @Override
    public String toString() {
        return name + suit;
    }

    public static String getCardsAsString(ArrayList<Card> cards) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(card.toString() + " ");
        }
        return stringBuilder.toString();
    }


    public Card(Suits suit, String name) {
        this.suit = suit;
        this.name = name;
        value = cardValues.get(name);
    }


    public static Set<String> getCardNamesAsSet() {
        return cardValues.keySet();
    }

    private static HashMap<String, Integer> cardValues = new HashMap<String, Integer>();

    static {
        cardValues.put("2", 2);
        cardValues.put("3", 3);
        cardValues.put("4", 4);
        cardValues.put("5", 5);
        cardValues.put("6", 6);
        cardValues.put("7", 7);
        cardValues.put("8", 8);
        cardValues.put("9", 9);
        cardValues.put("10", 10);
        cardValues.put("J", 10);
        cardValues.put("Q", 10);
        cardValues.put("K", 10);
        cardValues.put("A", 11);
    }

}

enum Suits {
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLOVERS("♣"),
    PIKES("♠");

    private String str;

    private Suits(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}