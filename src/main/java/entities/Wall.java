package entities;

import controller.GameController;
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
        super(x, y, Constants.TILE_SIZE);
        this.breakable = breakable;
        if (breakable) {
            currentImage = Assets.wallBreakable;
        } else {
            currentImage = Assets.wallSprite;
        }
    }

    public Wall(double x, double y) {
        super(x, y, Constants.TILE_SIZE);
        this.breakable = false;
    }

    public void breakWall() {
        GameController.getInstance().getLevel().getStaticEntities().remove(this);
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