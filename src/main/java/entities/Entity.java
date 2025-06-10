package entities;

import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
    protected double x, y, size;

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
}
