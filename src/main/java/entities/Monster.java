package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.CollisionCalculator;
import ui.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ui.CollisionCalculator.isColliding;

public class Monster extends Entity {

    private final double speed = (double) Constants.TILE_SIZE / 10;
    private int currentDeplacementCounter = 0;
    private int currentDeplacementDirection = 0;
    private final Random random = new Random();

    public Monster(double x, double y) {
        super(x, y, Constants.TILE_SIZE * 0.8);
    }

    @Override
    public void update() {

        if (currentDeplacementCounter <= 0) {
            int direction = random.nextInt(4);
            double dx = 0, dy = 0;
            if (direction == 0) {
                dy -= Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 1; // Haut
            }
            else if (direction == 1) {
                dy += Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 2; // Bas
            }
            else if (direction == 2) {
                dx -= Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 3; // Gauche
            }
            else {
                dx += Constants.TILE_SIZE;
                currentDeplacementCounter = 10;
                currentDeplacementDirection = 4; // Droite
            }

            if (this.collideWhenGoTo(x + dx, y + dy)) {
                currentDeplacementCounter = 0;
            }

        } else {
            currentDeplacementCounter--;
            if (currentDeplacementDirection == 1) {
                y -= speed;
            } else if (currentDeplacementDirection == 2) {
                y += speed;
            } else if (currentDeplacementDirection == 3) {
                x -= speed;
            } else if (currentDeplacementDirection == 4) {
                x += speed;
            }
        }

        ArrayList<Player> players = new ArrayList<>(GameController.getInstance().getLevel().getPlayers());
        for (Player p : players) {
            if (CollisionCalculator.isColliding(p, this)) {
                p.removeLife();
            }
        }
    }

    public void kill() {
        GameController.getInstance().getLevel().getDynamicEntities().remove(this);
    }

    @Override
    public void render(GraphicsContext gc) {
        currentImage = Assets.monsterSprite;
        gc.drawImage(currentImage, x, y, size, size);
    }
}