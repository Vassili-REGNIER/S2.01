package ui;

import javafx.scene.image.Image;

import java.util.Objects;

public class Assets {

    public static Image player1Up, player1Right, player1Left, player1Down ;
    public static Image player2Up, player2Right, player2Left, player2Down;
    public static Image background;
    public static Image wallSprite;
    public static Image wallBreakable;
    public static Image bombSprite;
    public static Image explosionSprite;
    public static Image bombe1;
    public static Image bombe2;
    public static Image bombe3;
    public static Image bombe4;
    public static Image monsterSprite;


    public static void load() {
        try {
            background = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/background.png")).toExternalForm());
            wallSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/wall2.png")).toExternalForm());
            wallBreakable = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/wall.png")).toExternalForm());
            player1Down = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Down.png")).toExternalForm());
            player1Up = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Up.png")).toExternalForm());
            player1Right = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Right.png")).toExternalForm());
            player1Left = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Left.png")).toExternalForm());

            monsterSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/monster.png")).toExternalForm());

            player2Down = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Down.png")).toExternalForm());
            player2Up = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Up.png")).toExternalForm());
            player2Right = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Right.png")).toExternalForm());
            player2Left = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Left.png")).toExternalForm());


            bombe1 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe1.png")).toExternalForm());
            bombe2 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe2.png")).toExternalForm());
            bombe3 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe3.png")).toExternalForm());
            bombe4 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe4.png")).toExternalForm());

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
            e.printStackTrace();
        }
    }
}