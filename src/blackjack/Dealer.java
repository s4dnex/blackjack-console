package blackjack;

import java.util.ArrayList;

public final class Dealer {

    private static ArrayList<Card> hand = new ArrayList<Card>();

    public static ArrayList<Card> getCards() {
        return hand;
    }

    public static void reset() {
        hand.clear();
    }

    public static void hit() {
        hand.add(Deck.getRandomCard());
    }

    public static boolean hasTurn() {
        if (Player.hasSurrendered() || Player.hasBlackjack()) {
            return false;
        }

        boolean loseToFirstHand = false;
        boolean loseToSecondHand = false;

        if (Card.getCardsTotal(Player.getCards(Player.FIRST_HAND), false) <= 21
        && Card.getCardsTotal(Dealer.getCards(), false) < Card.getCardsTotal(Player.getCards(Player.FIRST_HAND), false)
        && Card.getCardsTotal(Dealer.getCards(), false) < 21) {
            loseToFirstHand = true;
        }

        if (Player.isHandSplit()) {
            if (Card.getCardsTotal(Player.getCards(Player.SECOND_HAND), false) <= 21
                    && Card.getCardsTotal(Dealer.getCards(), false) < Card.getCardsTotal(Player.getCards(Player.SECOND_HAND), false)
                    && Card.getCardsTotal(Dealer.getCards(), false) < 21) {
                loseToSecondHand = true;
            }

            return loseToFirstHand && loseToSecondHand;
        }
        

        return loseToFirstHand;
    }

    private Dealer() {
        
    }
}
