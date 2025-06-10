package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.Constants;

public class Wall extends Entity {

    private Image currentImage;
    boolean breakable;

    public boolean isBreakable() {
        return breakable;
    }

    public Wall(double x, double y, boolean breakable) {
        this.breakable = breakable;
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE;
        if (breakable) {
            currentImage = Assets.wallBreakable;
        } else {
            currentImage = Assets.wallSprite;
        }
    }

    public Wall(double x, double y) {
        this.breakable = false;
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE;
    }

    @Override
    public void update() {
        // Le mur ne fait rien
    }

    @Override
    public void render(GraphicsContext gc) {

        if (breakable) {
            currentImage = Assets.wallBreakable;
        }

        else {
            currentImage = Assets.wallSprite;
        }

        gc.drawImage(currentImage, x, y, size, size);
    }
}