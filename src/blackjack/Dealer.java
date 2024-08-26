package blackjack;

import java.util.ArrayList;

public final class Dealer {

    private static ArrayList<Card> hand = new ArrayList<Card>();

    public static ArrayList<Card> getCards() {
        return hand;
    }


    public static void hit(Card card) {
        hand.add(card);
    }

    private Dealer() {
        
    }
}
