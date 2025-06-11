package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public abstract class Entity {
    protected double x, y, size;
    protected Image currentImage;

    public abstract void update();
    public abstract void render(GraphicsContext gc);

    public Entity(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }


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

    // Méthode utilitaire pour tester l'intersection entre deux entités carrées
    private boolean intersects(double x1, double y1, double size1,
                               double x2, double y2, double size2) {
        return x1 < x2 + size2 &&
                x1 + size1 > x2 &&
                y1 < y2 + size2 &&
                y1 + size1 > y2;
    }
}
