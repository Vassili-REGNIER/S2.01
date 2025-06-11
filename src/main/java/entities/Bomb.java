package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.Constants;


public class Bomb extends Entity {
    private final long startTime;
    private int state = 0; // Étapes : 0 → image1, 1 → image2, 2 → image3, 3 → fin
    private Player sourcePlayer;

    public Bomb(double x, double y, Player sourcePlayer) {
        super(x, y, Constants.TILE_SIZE);
        this.sourcePlayer = sourcePlayer;
        this.x = (int) ((x+10) / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        this.y = (int) ((y+10) / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        this.startTime = System.currentTimeMillis();
        this.currentImage = Assets.bombe1;
    }

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

    private void explode() {
        GameController.getInstance().getLevel().getDynamicEntities().remove(this);
        Explosion explosion = new Explosion(x, y, sourcePlayer);
        GameController.getInstance().getLevel().getDynamicEntities().add(explosion);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, size, size);
    }
}