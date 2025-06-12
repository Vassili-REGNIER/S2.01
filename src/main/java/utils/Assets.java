package utils;

import javafx.scene.image.Image;
import java.util.Objects;

/**
 * Classe utilitaire chargée de gérer et de stocker toutes les ressources graphiques (images) du jeu.
 *
 * Toutes les ressources doivent être chargées avant utilisation via la méthode {@link #load()}.
 */
public class Assets {

    /** Image de fond du jeu. */
    public static Image background;

    /** Image du mur indestructible. */
    public static Image wallSprite;

    /** Image du mur destructible. */
    public static Image wallBreakable;

    /** Image d'explosion. */
    public static Image explosion;

    /** Images représentant les différentes étapes d'animation d'une bombe. */
    public static Image bombe1, bombe2, bombe3, bombe4;

    /** Image représentant le monstre. */
    public static Image monsterSprite;

    /** Images du joueur 1 en fonction de sa direction ou de son état (mort). */
    public static Image player1Up, player1Right, player1Down, player1Left, player1Dead;

    /** Images du joueur 2 en fonction de sa direction ou de son état (mort). */
    public static Image player2Up, player2Right, player2Down, player2Left, player2Dead;

    /**
     * Charge toutes les ressources graphiques du jeu.
     * Cette méthode doit être appelée une fois au lancement du jeu avant toute tentative d'affichage.
     */
    public static void load() {
        try {
            background = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/background.png")).toExternalForm());
            wallSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/wall2.png")).toExternalForm());
            wallBreakable = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/wall.png")).toExternalForm());

            player1Down = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Down.png")).toExternalForm());
            player1Up = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Up.png")).toExternalForm());
            player1Right = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Right.png")).toExternalForm());
            player1Left = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Left.png")).toExternalForm());
            player1Dead = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player1Dead.png")).toExternalForm());

            player2Down = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Down.png")).toExternalForm());
            player2Up = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Up.png")).toExternalForm());
            player2Right = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Right.png")).toExternalForm());
            player2Left = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Left.png")).toExternalForm());
            player2Dead = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/player2Dead.png")).toExternalForm());

            monsterSprite = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/monster.png")).toExternalForm());

            bombe1 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe1.png")).toExternalForm());
            bombe2 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe2.png")).toExternalForm());
            bombe3 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe3.png")).toExternalForm());
            bombe4 = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/bombe4.png")).toExternalForm());

            explosion = new Image(Objects.requireNonNull(Assets.class.getResource("/images/game/explosion1.png")).toExternalForm());

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
