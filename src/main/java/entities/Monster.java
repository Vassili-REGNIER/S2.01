package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import utils.Assets;
import utils.CollisionCalculator;
import utils.Constants;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe représentant un monstre dans le jeu.
 * Les monstres se déplacent aléatoirement sur la carte et infligent des dégâts aux joueurs en cas de collision.
 */
public class Monster extends Entity {

    /** Vitesse de déplacement du monstre en pixels par mise à jour. */
    private final double speed = (double) Constants.TILE_SIZE / 10;

    /** Nombre de mises à jour restantes avant de recalculer une nouvelle direction. */
    private int currentDeplacementCounter = 0;

    /** Direction actuelle du déplacement (1=haut, 2=bas, 3=gauche, 4=droite). */
    private int currentDeplacementDirection = 0;

    /** Générateur de nombres aléatoires pour choisir les directions de déplacement. */
    private final Random random = new Random();

    /**
     * Construit un monstre à la position spécifiée.
     *
     * @param x Position x initiale du monstre.
     * @param y Position y initiale du monstre.
     */
    public Monster(double x, double y) {
        super(x, y, Constants.TILE_SIZE * 0.8);
    }

    /**
     * Met à jour l'état du monstre :
     * <ul>
     *     <li>Le monstre choisit aléatoirement une direction de déplacement si nécessaire.</li>
     *     <li>Le déplacement est bloqué si un obstacle est rencontré.</li>
     *     <li>Si un joueur est en collision avec le monstre, celui-ci perd une vie.</li>
     * </ul>
     */
    @Override
    public void update() {
        if (currentDeplacementCounter <= 0) {
            int direction = random.nextInt(4);
            double dx = 0, dy = 0;
            if (direction == 0) {
                dy -= Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 1; // Haut
            }
            else if (direction == 1) {
                dy += Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 2; // Bas
            }
            else if (direction == 2) {
                dx -= Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 3; // Gauche
            }
            else {
                dx += Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 4; // Droite
            }

            // Collision : ne pas se déplacer
            if (this.collideWhenGoTo(x + dx, y + dy)) {
                currentDeplacementCounter = 0;
            }

        } else {
            currentDeplacementCounter--;
            if (currentDeplacementDirection == 1) {
                y -= speed;
            } else if (currentDeplacementDirection == 2) {
                y += speed;
            } else if (currentDeplacementDirection == 3) {
                x -= speed;
            } else if (currentDeplacementDirection == 4) {
                x += speed;
            }
        }

        // Collision avec les joueurs : leur retire une vie
        ArrayList<Player> players = new ArrayList<>(GameController.getInstance().getLevel().getPlayers());
        for (Player p : players) {
            if (CollisionCalculator.isColliding(p, this)) {
                p.removeLife();
            }
        }
    }

    /**
     * Tue le monstre et le retire de la liste des entités dynamiques du niveau.
     */
    public void kill() {
        GameController.getInstance().getLevel().getDynamicEntities().remove(this);
    }

    /**
     * Affiche le monstre à l'écran.
     *
     * @param gc Contexte graphique.
     */
    @Override
    public void render(GraphicsContext gc) {
        currentImage = Assets.monsterSprite;
        gc.drawImage(currentImage, x, y, size, size);
    }
}
