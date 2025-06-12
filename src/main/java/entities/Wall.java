package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import utils.Assets;
import utils.Constants;

/**
 * Classe représentant un mur dans le jeu.
 * Un mur peut être destructible ou indestructible.
 * Les murs destructibles peuvent être détruits par des explosions.
 */
public class Wall extends Entity {

    /** Indique si le mur est destructible. */
    private boolean breakable;

    /**
     * Retourne si le mur est destructible.
     *
     * @return true si le mur est destructible, false sinon.
     */
    public boolean isBreakable() {
        return breakable;
    }

    /**
     * Construit un mur à la position spécifiée.
     *
     * @param x         Position x du mur.
     * @param y         Position y du mur.
     * @param breakable true si le mur est destructible, false sinon.
     */
    public Wall(double x, double y, boolean breakable) {
        super(x, y, Constants.TILE_SIZE);
        this.breakable = breakable;
        if (breakable) {
            currentImage = Assets.wallBreakable;
        } else {
            currentImage = Assets.wallSprite;
        }
    }

    /**
     * Construit un mur indestructible à la position spécifiée.
     *
     * @param x Position x du mur.
     * @param y Position y du mur.
     */
    public Wall(double x, double y) {
        super(x, y, Constants.TILE_SIZE);
        this.breakable = false;
    }

    /**
     * Détruit le mur (s'il est destructible), le retirant de la liste des entités statiques du niveau.
     */
    public void breakWall() {
        GameController.getInstance().getLevel().getStaticEntities().remove(this);
    }

    /**
     * Met à jour l'état du mur. Les murs sont statiques, cette méthode est donc vide.
     */
    @Override
    public void update() {
        // Les murs sont statiques, aucune mise à jour nécessaire.
    }

    /**
     * Affiche le mur à l'écran.
     *
     * @param gc Contexte graphique JavaFX.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (breakable) {
            currentImage = Assets.wallBreakable;
        } else {
            currentImage = Assets.wallSprite;
        }
        gc.drawImage(currentImage, x, y, size, size);
    }
}
