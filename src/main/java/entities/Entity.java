package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;

/**
 * Classe abstraite représentant une entité du jeu.
 * Une entité a une position, une taille et peut être rendue à l'écran.
 * Les entités peuvent être statiques (murs, sol) ou dynamiques (joueurs, monstres, bombes).
 */
public abstract class Entity {

    /** Position x de l'entité. */
    protected double x;

    /** Position y de l'entité. */
    protected double y;

    /** Taille de l'entité (carrée). */
    protected double size;

    /** Image actuellement affichée pour cette entité. */
    protected Image currentImage;

    /**
     * Met à jour l'état de l'entité. Doit être implémenté par les classes concrètes.
     */
    public abstract void update();

    /**
     * Dessine l'entité à l'écran.
     *
     * @param gc Contexte graphique sur lequel dessiner.
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Constructeur d'une entité.
     *
     * @param x    Position x.
     * @param y    Position y.
     * @param size Taille de l'entité.
     */
    public Entity(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    /**
     * @return Position x de l'entité.
     */
    public double getX() {
        return x;
    }

    /**
     * @return Position y de l'entité.
     */
    public double getY() {
        return y;
    }

    /**
     * @return Taille de l'entité.
     */
    public double getSize() {
        return size;
    }

    /**
     * Détermine si l'entité entrerait en collision si elle se déplaçait à la position donnée.
     *
     * @param x Nouvelle position x.
     * @param y Nouvelle position y.
     * @return true si une collision serait détectée, false sinon.
     */
    public boolean collideWhenGoTo(double x, double y) {
        ArrayList<Entity> staticEntities = GameController.getInstance().getLevel().getStaticEntities();
        ArrayList<Entity> dynamicEntities = GameController.getInstance().getLevel().getDynamicEntities();

        for (Entity other : staticEntities) {
            if (other == this) continue; // Ignore la collision avec soi-même
            if (intersects(x, y, this.size, other.getX(), other.getY(), other.getSize())) {
                return true;
            }
        }

        for (Entity other : dynamicEntities) {
            if (other == this) continue; // Ignore la collision avec soi-même
            if (other instanceof Monster) continue;  // Ignore la collision avec les monstres
            if (intersects(x, y, this.size, other.getX(), other.getY(), other.getSize())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Teste si deux entités carrées s'intersectent.
     *
     * @param x1    Position x de la première entité.
     * @param y1    Position y de la première entité.
     * @param size1 Taille de la première entité.
     * @param x2    Position x de la seconde entité.
     * @param y2    Position y de la seconde entité.
     * @param size2 Taille de la seconde entité.
     * @return true si les deux entités se chevauchent, false sinon.
     */
    private boolean intersects(double x1, double y1, double size1,
                               double x2, double y2, double size2) {
        return x1 < x2 + size2 &&
                x1 + size1 > x2 &&
                y1 < y2 + size2 &&
                y1 + size1 > y2;
    }
}
