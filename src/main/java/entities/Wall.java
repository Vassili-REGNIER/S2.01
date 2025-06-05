package entities;

import javafx.scene.canvas.GraphicsContext;
import ui.Assets;
import ui.Constants;

public class Wall extends Entity {

    public Wall(double x, double y) {
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
        gc.drawImage(Assets.wallSprite, x, y, size, size);
    }
}
