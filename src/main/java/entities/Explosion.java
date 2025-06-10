package entities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ui.Assets;
import ui.CollisionCalculator;
import ui.Constants;

import java.util.ArrayList;

public class Explosion extends Entity {


    private final long startTime;

    public Explosion (double x, double y) {
        super(x - Constants.TILE_SIZE, y - Constants.TILE_SIZE, 3 * Constants.TILE_SIZE);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();

        if (now - startTime >= 500) {
            this.destroyEntities();
            GameController.getInstance().getLevel().getDynamicEntities().remove(this);
        }
    }

    private void destroyEntities() {

        Entity topEntity = new Entity(x + Constants.TILE_SIZE * 1.1, y, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };

        Entity centerEntity = new Entity(x + Constants.TILE_SIZE * 1.1, y + Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };

        Entity downEntity = new Entity(x + Constants.TILE_SIZE * 1.1, y + Constants.TILE_SIZE + Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };

        Entity rightEntity = new Entity(x + Constants.TILE_SIZE + Constants.TILE_SIZE * 1.1, y + Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };

        Entity leftEntity = new Entity(x, y + Constants.TILE_SIZE, Constants.TILE_SIZE * 0.8) {
            @Override public void update() {}
            @Override public void render(GraphicsContext gc) {}
        };



        for (Entity entity : new ArrayList<>(GameController.getInstance().getLevel().getStaticEntities())) {
            if (CollisionCalculator.isColliding(entity, topEntity) || CollisionCalculator.isColliding(entity, centerEntity)
                || CollisionCalculator.isColliding(entity, leftEntity) || CollisionCalculator.isColliding(entity, rightEntity)
                || CollisionCalculator.isColliding(entity, downEntity)) {
                if (entity instanceof Wall w) {
                    if (w.isBreakable()) w.breakWall();
                }
            }
        }

        for (Player player : GameController.getInstance().getLevel().getPlayers()) {
            if (CollisionCalculator.isColliding(player, this)) {
                player.removeLife();
                System.out.println("Perds une vie");
            }
        }


    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(Assets.explosion, x, y, size, size);
    }
}
