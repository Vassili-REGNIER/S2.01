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
    private Player sourcePlayer;

    public Explosion (double x, double y, Player sourcePlayer) {
        super(x - Constants.TILE_SIZE, y - Constants.TILE_SIZE, 3 * Constants.TILE_SIZE);
        this.startTime = System.currentTimeMillis();
        currentImage = Assets.explosion;
        this.sourcePlayer = sourcePlayer;
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

        ArrayList<Entity> staticEntities = new ArrayList<>(GameController.getInstance().getLevel().getStaticEntities());
        ArrayList<Entity> dynamicEntities = new ArrayList<>(GameController.getInstance().getLevel().getDynamicEntities());
        ArrayList<Player> players = new ArrayList<>(GameController.getInstance().getLevel().getPlayers());


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

        for (Entity entity : staticEntities) {
            if (CollisionCalculator.isColliding(entity, topEntity) || CollisionCalculator.isColliding(entity, centerEntity)
                || CollisionCalculator.isColliding(entity, leftEntity) || CollisionCalculator.isColliding(entity, rightEntity)
                || CollisionCalculator.isColliding(entity, downEntity)) {
                if (entity instanceof Wall w) {
                    if (w.isBreakable()) {
                        w.breakWall();
                        sourcePlayer.setScore(sourcePlayer.getScore() + 100);
                    }
                }
            }
        }

        for (Entity entity : dynamicEntities) {
            if (CollisionCalculator.isColliding(entity, topEntity) || CollisionCalculator.isColliding(entity, centerEntity)
                    || CollisionCalculator.isColliding(entity, leftEntity) || CollisionCalculator.isColliding(entity, rightEntity)
                    || CollisionCalculator.isColliding(entity, downEntity)) {
                if (entity instanceof Monster m) {
                    m.kill();
                    sourcePlayer.setScore(sourcePlayer.getScore() + 500);
                }
            }
        }

        for (Player player : players) {
            if (CollisionCalculator.isColliding(player, this)) {
                player.removeLife();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, size, size);
    }
}
