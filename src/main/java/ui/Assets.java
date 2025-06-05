package ui;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.Objects;

public class Assets {

    public static Image background;
    public static Image playerSprite;
    public static Image wallSprite;
    public static Image bombSprite;
    public static Image explosionSprite;

    public static void load() {
        try {

            background = new Image(Objects.requireNonNull(Assets.class.getResource("/images/background.png")).toExternalForm());
            playerSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/player1.png")).toExternalForm());
            wallSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/wall2.png")).toExternalForm());
            //bombSprite = new Image(Assets.class.getResource("/images/background.png").toExternalForm());
            //explosionSprite = new Image(Assets.class.getResource("/images/background.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
