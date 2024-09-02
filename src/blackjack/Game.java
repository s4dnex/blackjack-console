package blackjack;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;

public class Game {
    static final int WIDTH = 112;
    static final int HEIGHT = 11;
    static final String CLEAR_CONSOLE = "\033[H\033[2J\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
    private static Scanner scanner = new Scanner(System.in);
    private static int userChoice;

    public static void main(String[] args) throws Exception {
        setUsername();
        showMainMenu();
    }

    static void showMainMenu() {
        do {
            do {
                System.out.println(CLEAR_CONSOLE);
                showLogo();
                System.out.println();
                System.out.printf("Hello, %s!\n", Settings.getUserName());
                System.out.println();
                for (int i = 0; i < MainMenuAction.asArrayList().size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, MainMenuAction.asArrayList().get(i).toString());
                }
                showChoiceMenu();
            } while (!checkUserChoice(scanner.nextLine(), MainMenuAction.asArrayList().size()));
    
            switch (MainMenuAction.asArrayList().get(userChoice)) {
                case MainMenuAction.START:
                    startGame();
                    break;
    
                case MainMenuAction.SETTINGS:
                    showSettingsMenu();
                    break;
    
                case MainMenuAction.EXIT:
                    System.out.println(CLEAR_CONSOLE);
                    return;
            }
        } while (true);
    }

    static void showSettingsMenu() {
        do {
            do {
                System.out.println(CLEAR_CONSOLE);
                showLogo();
                for (int i = 0; i < SettingsMenuAction.asArrayList().size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, SettingsMenuAction.asArrayList().get(i).toString());
                }
                showChoiceMenu();
            } while (!checkUserChoice(scanner.nextLine(), SettingsMenuAction.asArrayList().size()));
    
            switch (SettingsMenuAction.asArrayList().get(userChoice)) {
                case SettingsMenuAction.CHANGE_USER_NAME:
                    setUsername();
                    break;
    
                case SettingsMenuAction.RETURN_TO_MAIN_MENU:
                    return;
            }
        } while (true);
    }

    static void startGame() {
        do {
            boolean gameHasEnded = false;
            boolean isFirstTurn = true;

            do {
                System.out.println(CLEAR_CONSOLE);
                Player.checkZeroBalance();
                System.out.printf("Your current balance: %d\n", Player.getBalance());
                System.out.print("Place a bet: ");
            } while (!Player.setBet(scanner.nextLine()));

            Player.reset();
            Dealer.reset();
            Deck.reset();
            Deck.shuffle();
            for (int i = 0; i < 2; i++) {
                Dealer.hit();
                Player.hit(Player.FIRST_HAND);
            }

            if (Card.getCardsTotal(Player.getCards(Player.FIRST_HAND), false) == 21) {
                Player.blackjack();
            }

            PlayerAction.resetActionsList();
            if (Player.getBet() > Player.getBalance()) {
                PlayerAction.removeDoubleDownAction();
            }
            if (!Player.getCards(Player.FIRST_HAND).get(0).equalsByName(Player.getCards(Player.FIRST_HAND).get(1))
                    || Player.getBet() > Player.getBalance()) {
                PlayerAction.removeSplitAction();
            }

            do {
                do {
                    if (Player.isHandSplit()) {
                        PlayerAction.removeDoubleDownAction();
                    }
                    if (!isFirstTurn) {
                        PlayerAction.removeSplitAction();
                        PlayerAction.removeSurrenderAction();
                    }
                    isFirstTurn = false;
                    System.out.println(CLEAR_CONSOLE);
                    System.out.printf("Your current balance: %d\n", Player.getBalance());
                    System.out.printf("Your current bet: %d\n", Player.getBet());
                    System.out.println();
                    System.out.println("== Dealer ==");
                    System.out.printf("%s\n", Card.getCardsAsString(Dealer.getCards(), Player.hasTurn()));
                    System.out.printf("Score: %d\n", Card.getCardsTotal(Dealer.getCards(), Player.hasTurn()));
                    System.out.println();
                    System.out.println("== Player ==");
                    if (Player.isHandSplit() && Player.getCurrentHand() == Player.FIRST_HAND) {
                        System.out.println("-- Current hand --");
                    }
                    System.out.printf("%s\n", Card.getCardsAsString(Player.getCards(Player.FIRST_HAND), false));
                    System.out.printf("Score: %d\n", Card.getCardsTotal(Player.getCards(Player.FIRST_HAND), false));
                    if (Player.isHandSplit()) {
                        System.out.println();
                        if (Player.getCurrentHand() == Player.SECOND_HAND && Player.hasTurn()) {
                            System.out.println("-- Current hand --");
                        }
                        System.out.printf("%s\n", Card.getCardsAsString(Player.getCards(Player.SECOND_HAND), false));
                        System.out.printf("Score: %d\n", Card.getCardsTotal(Player.getCards(Player.SECOND_HAND), false));
                    }

                    System.out.println();
                    if (Player.hasTurn()) {
                        for (int i = 0; i < PlayerAction.asArrayList().size(); i++) {
                            System.out.printf("%d. %s\n", i + 1, PlayerAction.asArrayList().get(i).toString());
                        }
                        showChoiceMenu();
                    } 
                    else {
                        try {
                            Thread.sleep(1000);
                        } 
                        catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        break;
                    }
                } while (!checkUserChoice(scanner.nextLine(), PlayerAction.asArrayList().size()));

                if (Player.hasTurn()) {
                    switch (PlayerAction.asArrayList().get(userChoice)) {
                        case PlayerAction.HIT:
                            Player.hit(Player.getCurrentHand());
                            break;

                        case PlayerAction.STAY:
                            Player.stay(Player.getCurrentHand());
                            break;

                        case PlayerAction.DOUBLE_DOWN:
                            Player.doubleDown();
                            break;

                        case PlayerAction.SPLIT:
                            Player.split();
                            break;

                        case PlayerAction.SURRENDER:
                            do {
                                System.out.println(CLEAR_CONSOLE);
                                System.out.println("Are you sure you want to surrender? You'll lose half of your bet :/");
                                for (int i = 0; i < ConfirmationAction.asArrayList().size(); i++) {
                                    System.out.printf("%d. %s\n", i + 1, ConfirmationAction.asArrayList().get(i).toString());
                                }
                                showChoiceMenu();
                            } while (!checkUserChoice(scanner.nextLine(), ConfirmationAction.asArrayList().size()));
                            
                            switch (ConfirmationAction.asArrayList().get(userChoice)) {
                                case ConfirmationAction.YES:
                                    Player.surrender();
                                    break;

                                case ConfirmationAction.NO:
                                    break;
                            }
                            break;

                        case PlayerAction.EXIT_TO_MAIN_MENU:
                            do {
                                System.out.println(CLEAR_CONSOLE);
                                System.out.println("Are you sure you want to exit? All progress will be lost :(");
                                for (int i = 0; i < ConfirmationAction.asArrayList().size(); i++) {
                                    System.out.printf("%d. %s\n", i + 1, ConfirmationAction.asArrayList().get(i).toString());
                                }
                                showChoiceMenu();
                            } while (!checkUserChoice(scanner.nextLine(), ConfirmationAction.asArrayList().size()));
                            
                            switch (ConfirmationAction.asArrayList().get(userChoice)) {
                                case ConfirmationAction.YES:
                                    return;

                                case ConfirmationAction.NO:
                                    break;
                            }
                            break;
                    }
                } 
                else if (Dealer.hasTurn()) {
                    Dealer.hit();
                } 
                else {
                    System.out.println("== Results ==");

                    if (Player.isHandSplit()) {
                        Player.halveBet();
                    }

                    // First Hand Results
                    showResults(Player.FIRST_HAND);

                    // Second Hand Results
                    if (Player.isHandSplit()) {
                        showResults(Player.SECOND_HAND);
                    }

                    Player.resetBet();

                    System.out.println();
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    
                    gameHasEnded = true;
                }
            } while (!gameHasEnded);

            do {
                System.out.println(CLEAR_CONSOLE);
                Player.checkZeroBalance();
                System.out.printf("Your current balance: %d\n", Player.getBalance());
                System.out.println();
                System.out.println("Do you want to start a new game?");
                for (int i = 0; i < ConfirmationAction.asArrayList().size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, ConfirmationAction.asArrayList().get(i).toString());
                }
                showChoiceMenu();
            } while (!checkUserChoice(scanner.nextLine(), ConfirmationAction.asArrayList().size()));

            switch (ConfirmationAction.asArrayList().get(userChoice)) {
                case ConfirmationAction.YES:
                    break;
            
                case ConfirmationAction.NO:
                    return;
            }
        } while (true);
    }

    static void showResults(boolean hand) {
        if (Player.hasSurrendered()) {
            System.out.println("You've surrendered! You've lost half of your bet :/");
            Player.increaseBalance(Player.getBet() / 2);
        }
        else if (Player.hasBlackjack() && Card.getCardsTotal(Dealer.getCards(), false) != 21) {
            System.out.printf("Blackjack! You've won %d ;)\n", Player.getBet() * 3 / 2);
            Player.increaseBalance(Player.getBet() * 3 / 2);
        }
        else if (Card.getCardsTotal(Player.getCards(hand), false) > 21
                || (Card.getCardsTotal(Dealer.getCards(), false) > Card
                        .getCardsTotal(Player.getCards(hand), false)
                        && Card.getCardsTotal(Dealer.getCards(), false) <= 21)) {
            System.out.printf("Sorry, but you've lost your bet of %d :(\n", Player.getBet());
        } 
        else if (Card.getCardsTotal(Player.getCards(hand), false) == Card
                .getCardsTotal(Dealer.getCards(), false)
                && Card.getCardsTotal(Dealer.getCards(), false) <= 21
                && Card.getCardsTotal(Player.getCards(hand), false) <= 21) {
            System.out.printf("It's a tie! You've saved your bet of %d :)\n", Player.getBet());
            Player.increaseBalance(Player.getBet());
        } 
        else if ((Card.getCardsTotal(Player.getCards(hand), false) > Card
                .getCardsTotal(Dealer.getCards(), false)
                && Card.getCardsTotal(Player.getCards(hand), false) <= 21
                && Card.getCardsTotal(Dealer.getCards(), false) <= 21)
                || ((Card.getCardsTotal(Player.getCards(hand), false)) <= 21
                        & Card.getCardsTotal(Dealer.getCards(), false) > 21)) {
            System.out.printf("Congratulations! You've won %d ;)\n", Player.getBet() * 2);
            Player.increaseBalance(Player.getBet() * 2);
        } 
        else {
            System.out.println("Unexpected behavior! Please, contact the developer.");
        }
    }

    static void showChoiceMenu() {
        System.out.print("\nChoose an action by its number: ");
    }

    static boolean checkUserChoice(String userChoice, int maxValue) {
        try {
            Game.userChoice = Integer.parseInt(userChoice);
            if (Game.userChoice >= 1 && Game.userChoice <= maxValue) {
                Game.userChoice -= 1;
                return true;
            } 
            else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static void setUsername() {
        do {
            System.out.println(CLEAR_CONSOLE);
            showLogo();
            System.out.print("Please, write your username: ");
        } while (!Settings.setUserName(scanner.nextLine()));
    }

    static void showLogo() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(new Font("Georgia", Font.BOLD, 12));
        graphics2D.drawString("B L A C K J A C K", 3, 10);

        for (int y = 0; y < HEIGHT; y++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int x = 0; x < WIDTH; x++) {
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? "■" : " ");
            }

            if (stringBuilder.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(stringBuilder);
        }
    }
}

enum ConfirmationAction {
    YES("Yes"),
    NO("No");

    private final static ArrayList<ConfirmationAction> actions = new ArrayList<ConfirmationAction>(Arrays.asList(ConfirmationAction.values()));
    private String str;

    private ConfirmationAction(String str) {
        this.str = str;
    }

    public static ArrayList<ConfirmationAction> asArrayList() {
        return actions;
    }

    @Override
    public String toString() {
        return str;
    }
}

enum MainMenuAction {
    START("Start"),
    SETTINGS("Settings"),
    EXIT("Exit");

    private final static ArrayList<MainMenuAction> actions = new ArrayList<MainMenuAction>(Arrays.asList(MainMenuAction.values()));
    private String str;

    private MainMenuAction(String str) {
        this.str = str;
    }

    public static ArrayList<MainMenuAction> asArrayList() {
        return actions;
    }

    @Override
    public String toString() {
        return str;
    }
}

enum SettingsMenuAction {
    CHANGE_USER_NAME("Change username"),
    RETURN_TO_MAIN_MENU("Return to Main Menu");

    private final static ArrayList<SettingsMenuAction> actions = new ArrayList<SettingsMenuAction>(Arrays.asList(SettingsMenuAction.values()));
    private String str;

    private SettingsMenuAction(String str) {
        this.str = str;
    }

    public static ArrayList<SettingsMenuAction> asArrayList() {
        return actions;
    }

    @Override
    public String toString() {
        return str;
    }
}

enum PlayerAction {
    HIT("Hit"),
    STAY("Stay"),
    DOUBLE_DOWN("Double Down"),
    SPLIT("Split"),
    SURRENDER("Surrender"),
    EXIT_TO_MAIN_MENU("Exit to Main Menu");

    private static ArrayList<PlayerAction> actions;
    private String str;

    private PlayerAction(String str) {
        this.str = str;
    }

    public static ArrayList<PlayerAction> asArrayList() {
        return actions;
    }

    public static void removeDoubleDownAction() {
        actions.remove(PlayerAction.DOUBLE_DOWN);
    }

    public static void removeSplitAction() {
        actions.remove(PlayerAction.SPLIT);
    }

    public static void removeSurrenderAction() {
        actions.remove(PlayerAction.SURRENDER);
    }

    public static void resetActionsList() {
        actions = new ArrayList<PlayerAction>(Arrays.asList(PlayerAction.values()));
    }

    @Override
    public String toString() {
        return str;
    }
}