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
    private static Scanner scanner = new Scanner(System .in);

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
        } while (!Settings.setUsername(scanner.nextLine()));
    }

    static void displayMainMenu() {
        do {
            System.out.println(CLEAR_CONSOLE);
            displayLogo();
            System.out.printf("Hello, %s!\n", Settings.getUsername());
            for (int i = 1; i <= MainMenuActions.LENGTH; i++) {
                System.out.printf("%d. %s\n", i, MainMenuActions.valueOf(i).toString());
            }
            displayUserChoice();
        } while (!checkUserChoice(scanner.nextLine(), MainMenuActions.LENGTH));
    }

    static void displayUserChoice() {
        System.out.print("Choose an action by its number: ");
    }

    static boolean checkUserChoice(String userChoice, int maxValue) {
        try {
            int choice = Integer.parseInt(userChoice);
            if (choice >= 1 && choice <= maxValue) {
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