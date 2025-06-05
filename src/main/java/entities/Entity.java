package entities;

import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
    protected double x, y, size;
    public abstract void update();
    public abstract void render(GraphicsContext gc);

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
