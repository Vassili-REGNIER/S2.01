package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import utils.Assets;
import utils.CollisionCalculator;
import utils.Constants;
import java.util.ArrayList;

/**
 * Classe représentant une explosion déclenchée par une bombe dans le jeu.
 * L'explosion détruit les murs cassables, tue les monstres et inflige des dégâts aux joueurs.
 */
public class Explosion extends Entity {

    /** Temps de début de l'explosion (en millisecondes). */
    private final long startTime;

    /** Joueur à l'origine de l'explosion (utilisé pour attribuer les points). */
    private Player sourcePlayer;

    /**
     * Construit une explosion à la position spécifiée.
     *
     * @param x            Position x de l'explosion (centrée sur la bombe).
     * @param y            Position y de l'explosion (centrée sur la bombe).
     * @param sourcePlayer Joueur qui a posé la bombe.
     */
    public Explosion (double x, double y, Player sourcePlayer) {
        super(x - Constants.TILE_SIZE, y - Constants.TILE_SIZE, 3 * Constants.TILE_SIZE);
        this.startTime = System.currentTimeMillis();
        currentImage = Assets.explosion;
        this.sourcePlayer = sourcePlayer;
    }

    /**
     * Met à jour l'état de l'explosion.
     * Détruit les entités touchées après 500 millisecondes et supprime l'explosion de la liste.
     */
    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if (now - startTime >= 500) {
            this.destroyEntities();
            GameController.getInstance().getLevel().getDynamicEntities().remove(this);
        }
    }

    /**
     * Détruit les entités affectées par l'explosion :
     * <ul>
     *     <li>Les murs cassables sont détruits et le joueur gagne 100 points par mur cassé.</li>
     *     <li>Les monstres sont tués et le joueur gagne 500 points par monstre éliminé.</li>
     *     <li>Les joueurs dans la zone de l'explosion perdent une vie.</li>
     * </ul>
     */
    private void destroyEntities() {
        ArrayList<Entity> staticEntities = new ArrayList<>(GameController.getInstance().getLevel().getStaticEntities());
        ArrayList<Entity> dynamicEntities = new ArrayList<>(GameController.getInstance().getLevel().getDynamicEntities());
        ArrayList<Player> players = new ArrayList<>(GameController.getInstance().getLevel().getPlayers());

        // Création de 5 zones représentant les différentes parties de l'explosion
        Entity topEntity = new Entity(x + Constants.TILE_SIZE * 1.1, y, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };
        Entity centerEntity = new Entity(x + Constants.TILE_SIZE * 1.1, y + Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };
        Entity downEntity = new Entity(x + Constants.TILE_SIZE * 1.1, y + Constants.TILE_SIZE + Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };
        Entity rightEntity = new Entity(x + Constants.TILE_SIZE + Constants.TILE_SIZE * 1.1, y + Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };
        Entity leftEntity = new Entity(x, y + Constants.TILE_SIZE, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };

        // Détruit les murs cassables
        for (Entity entity : staticEntities) {
            if (CollisionCalculator.isColliding(entity, topEntity) ||
                    CollisionCalculator.isColliding(entity, centerEntity) ||
                    CollisionCalculator.isColliding(entity, leftEntity) ||
                    CollisionCalculator.isColliding(entity, rightEntity) ||
                    CollisionCalculator.isColliding(entity, downEntity)) {

                if (entity instanceof Wall w && w.isBreakable()) {
                    w.breakWall();
                    sourcePlayer.setScore(sourcePlayer.getScore() + 100);
                }
            }
        }

        // Tue les monstres
        for (Entity entity : dynamicEntities) {
            if (CollisionCalculator.isColliding(entity, topEntity) ||
                    CollisionCalculator.isColliding(entity, centerEntity) ||
                    CollisionCalculator.isColliding(entity, leftEntity) ||
                    CollisionCalculator.isColliding(entity, rightEntity) ||
                    CollisionCalculator.isColliding(entity, downEntity)) {

                if (entity instanceof Monster m) {
                    m.kill();
                    sourcePlayer.setScore(sourcePlayer.getScore() + 500);
                }
            }
        }

        // Inflige des dégâts aux joueurs
        for (Player player : players) {
            if (CollisionCalculator.isColliding(player, this)) {
                player.removeLife();
            }
        }
    }

    /**
     * Dessine l'explosion à l'écran.
     *
     * @param gc Contexte graphique.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, size, size);
    }
}

