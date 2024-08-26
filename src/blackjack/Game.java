package blackjack;

import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;
import java.util.HashMap;

public class Game {
    static final int WIDTH = 112;
    static final int HEIGHT = 11;
    static final String CLEAR_CONSOLE = "\033[H\033[2J";
    private static Scanner scanner = new Scanner(System.in);
    private static int userChoice;

    public static void main(String[] args) throws Exception {
        setUsername();
        displayMainMenu();
    }

    static void displayLogo() {
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

    static void setUsername() {
        do {
            System.out.println(CLEAR_CONSOLE);
            displayLogo();
            System.out.print("Please, write your username: ");
        } while (!Settings.setUserName(scanner.nextLine()));
    }

    static void displayMainMenu() {
        do {
            System.out.println(CLEAR_CONSOLE);
            displayLogo();
            System.out.printf("Hello, %s!\n", Settings.getUserName());
            for (int i = 1; i <= MainMenuActions.LENGTH; i++) {
                System.out.printf("%d. %s\n", i, MainMenuActions.valueOf(i).toString());
            }
            displayUserChoice();
        } while (!checkUserChoice(scanner.nextLine(), MainMenuActions.LENGTH));

        switch (MainMenuActions.valueOf(userChoice)) {
            case MainMenuActions.START:
                startGame();
                break;
            
            case MainMenuActions.SETTINGS:
                displaySettingsMenu();
                break;
            
            case MainMenuActions.EXIT:
                System.out.println(CLEAR_CONSOLE);
                break;
                
            default:
                break;
        }
    }

    static void displaySettingsMenu() {
        do {
            System.out.println(CLEAR_CONSOLE);
            displayLogo();
            for (int i = 1; i <= SettingsMenuActions.LENGTH; i++) {
                System.out.printf("%d. %s\n", i, SettingsMenuActions.valueOf(i).toString());
            }
            displayUserChoice();
        } while (!checkUserChoice(scanner.nextLine(), SettingsMenuActions.LENGTH));

        switch (SettingsMenuActions.valueOf(userChoice)) {
            case SettingsMenuActions.CHANGE_USER_NAME:
                setUsername();
                displaySettingsMenu();
                break;
            
                case SettingsMenuActions.RETURN_TO_MAIN_MENU:
                displayMainMenu();
                break;
            
            default:
                break;
            
        }
    }

    static void startGame() {
        do {
            System.out.println(CLEAR_CONSOLE);
            System.out.printf("Your current balance: %d\n", Player.getBalance());
            System.out.print("Place a bet: ");
        } while (!Player.setBet(scanner.nextLine()));

        Deck.resetDeck();
        Deck.shuffleDeck();
        for (int i = 0; i < 2; i++) {
            Dealer.hit(Deck.getRandomCard());
            Player.hit(Deck.getRandomCard(), Player.FIRST_HAND);
        }

        do {
            System.out.println(CLEAR_CONSOLE);
            System.out.printf("Your current balance: %d\n", Player.getBalance());
            System.out.printf("Your current bet: %d\n", Player.getBet());
            System.out.println();
            System.out.println("== Dealer ==");
            System.out.printf("%s\n", Card.getCardsAsString(Dealer.getCards()));
            System.out.printf("Score: %d\n", Card.getCardsTotal(Dealer.getCards()));
            System.out.println();
            System.out.println("== Player ==");
            System.out.printf("%s\n", Card.getCardsAsString(Player.getCards(Player.FIRST_HAND)));
            System.out.printf("Score: %d\n", Card.getCardsTotal(Player.getCards(Player.FIRST_HAND)));
            if (!Player.getCards(Player.SECOND_HAND).isEmpty()) {
                System.out.printf("%s\n", Card.getCardsAsString(Player.getCards(Player.SECOND_HAND)));
                System.out.printf("Score: %d\n", Card.getCardsTotal(Player.getCards(Player.SECOND_HAND)));
            }
        } while (!checkUserChoice(scanner.nextLine(), PlayerActions.LENGTH));
    }

    static void displayUserChoice() {
        System.out.print("Choose an action by its number: ");
    }

    static boolean checkUserChoice(String userChoice, int maxValue) {
        try {
            Game.userChoice = Integer.parseInt(userChoice);
            if (Game.userChoice >= 1 && Game.userChoice <= maxValue) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}

enum MainMenuActions {
    START(1, "Start"),
    SETTINGS(2, "Settings"),
    EXIT(3, "Exit");

    public final static int LENGTH = MainMenuActions.values().length;
    private int index;
    private String str;

    private MainMenuActions(int index, String str) {
        this.str = str;
        this.index = index;
    }

    public int toInt() {
        return index;
    }

    @Override
    public String toString() {
        return str;
    }

    private static HashMap<Integer, MainMenuActions> actionsMap = new HashMap<Integer, MainMenuActions>();

    static {
        for (MainMenuActions action : MainMenuActions.values()) {
            actionsMap.put(action.index, action);
        }
    }

    public static MainMenuActions valueOf(int index) {
        return actionsMap.get(index);
    }
}

enum SettingsMenuActions {
    CHANGE_USER_NAME(1, "Change username"),
    RETURN_TO_MAIN_MENU(2, "Return to Main Menu");

    public final static int LENGTH = SettingsMenuActions.values().length;
    private int index;
    private String str;

    private SettingsMenuActions(int index, String str) {
        this.str = str;
        this.index = index;
    }

    public int toInt() {
        return index;
    }

    @Override
    public String toString() {
        return str;
    }

    private static HashMap<Integer, SettingsMenuActions> actionsMap = new HashMap<Integer, SettingsMenuActions>();

    static {
        for (SettingsMenuActions action : SettingsMenuActions.values()) {
            actionsMap.put(action.index, action);
        }
    }

    public static SettingsMenuActions valueOf(int index) {
        return actionsMap.get(index);
    }
}

enum PlayerActions {
    HIT(1, "Hit"),
    STAY(2, "Stay"),
    DOUBLE_DOWN(3, "Double Down"),
    SPLIT(4, "Split"),
    SURRENDER(5, "Surrender");

    public final static int LENGTH = PlayerActions.values().length;
    private int index;
    private String str;

    private PlayerActions(int index, String str) {
        this.str = str;
        this.index = index;
    }

    public int toInt() {
        return index;
    }

    @Override
    public String toString() {
        return str;
    }

    private static HashMap<Integer, PlayerActions> actionsMap = new HashMap<Integer, PlayerActions>();

    static {
        for (PlayerActions action : PlayerActions.values()) {
            actionsMap.put(action.index, action);
        }
    }

    public static PlayerActions valueOf(int index) {
        return actionsMap.get(index);
    }
}