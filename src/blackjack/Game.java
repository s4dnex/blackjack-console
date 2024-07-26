package blackjack;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;

public class Game {
    static final int WIDTH = 112;
    static final int HEIGHT = 11;
    static final String CLEAR_CONSOLE = "\033[H\033[2J";
    private static Scanner scanner = new Scanner(System .in);

    public static void main(String[] args) throws Exception {
        do {
            System.out.println(CLEAR_CONSOLE);
            displayLogo();
            System.out.print("Please, write your username: ");
            Settings.setUsername(scanner.nextLine());
        } while (!Settings.setUsername(Settings.getUsername()));

        System.out.println(CLEAR_CONSOLE);
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

    static void displayMainMenu() {
        displayLogo();
        System.out.printf("Hello, %s!", Settings.getUsername());
        
    }
}
