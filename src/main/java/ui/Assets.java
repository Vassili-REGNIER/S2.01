package ui;

import javafx.scene.image.Image;

import java.util.Objects;

public class Assets {

    public static Image player1Up, player1Right, player1Left;
    public static Image background;
    public static Image player1Down;
    public static Image wallSprite;
    public static Image wallBreakable;
    public static Image bombSprite;
    public static Image explosionSprite;

    public static void load() {
        try {
            background = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/background.png")).toExternalForm());
            wallSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/wall2.png")).toExternalForm());
            wallBreakable = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/wall.png")).toExternalForm());
            player1Down = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Down.png")).toExternalForm());
            player1Up = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Up.png")).toExternalForm());
            player1Right = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Right.png")).toExternalForm());
            player1Left = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Left.png")).toExternalForm());

            //bombSprite = new Image(Assets.class.getResource("/images/background.png").toExternalForm());
            //explosionSprite = new Image(Assets.class.getResource("/images/background.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
            e.printStackTrace();
        }
    }
}