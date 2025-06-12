package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import utils.Assets;
import utils.Constants;

/**
 * Représente une bombe posée par un joueur dans le jeu.
 * La bombe passe par plusieurs états visuels avant d'exploser et de générer une {@link Explosion}.
 */
public class Bomb extends Entity {

    /** Temps en millisecondes où la bombe a été posée. */
    private final long startTime;

    /** État courant de l'animation de la bombe. 0 → image1, 1 → image2, 2 → image3, 3 → image4 (avant explosion). */
    private int state = 0;

    /** Joueur qui a posé cette bombe. */
    private final Player sourcePlayer;

    /**
     * Constructeur d'une bombe.
     *
     * @param x            Position x de la bombe.
     * @param y            Position y de la bombe.
     * @param sourcePlayer Joueur ayant posé cette bombe.
     */
    public Bomb(double x, double y, Player sourcePlayer) {
        super(x, y, Constants.TILE_SIZE);
        this.sourcePlayer = sourcePlayer;
        this.x = (int) ((x + 10) / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        this.y = (int) ((y + 10) / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        this.startTime = System.currentTimeMillis();
        this.currentImage = Assets.bombe1;
    }

    /**
     * Met à jour l'état de la bombe. Change d'apparence toutes les 500 ms.
     * À la fin de l'animation (après 2000 ms), déclenche l'explosion.
     */
    @Override
    public void update() {
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        if (state == 0 && elapsed >= 500) {
            currentImage = Assets.bombe2;
            state = 1;
        } else if (state == 1 && elapsed >= 1000) {
            currentImage = Assets.bombe3;
            state = 2;
        } else if (state == 2 && elapsed >= 1500) {
            currentImage = Assets.bombe4;
            state = 3;
        } else if (state == 3 && elapsed >= 2000) {
            state = 4;
            this.explode();
        }
    }

    /**
     * Déclenche l'explosion de la bombe.
     * Retire la bombe de la liste des entités dynamiques et ajoute une {@link Explosion} à la même position.
     */
    private void explode() {
        GameController.getInstance().getLevel().getDynamicEntities().remove(this);
        Explosion explosion = new Explosion(x, y, sourcePlayer);
        GameController.getInstance().getLevel().getDynamicEntities().add(explosion);
    }

    /**
     * Dessine l'image actuelle de la bombe sur le {@link GraphicsContext} fourni.
     *
     * @param gc Contexte graphique utilisé pour dessiner.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, size, size);
    }
}
