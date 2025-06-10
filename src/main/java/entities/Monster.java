package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.CollisionCalculator;
import ui.Constants;

import java.util.List;
import java.util.Random;

import static ui.CollisionCalculator.isColliding;

public class Monster extends Entity {

    private final double speed = (double) Constants.TILE_SIZE / 10;
    private int currentDeplacementCounter = 0;
    private int currentDeplacementDirection = 0;
    private final double size;
    private final Random random = new Random();

    public Monster(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE * 0.8;
    }

    @Override
    public void update() {

     if (currentDeplacementCounter <= 0) {
        int dir = random.nextInt(4);
        double dx = 0, dy = 0;
        if (dir == 0) {
            dy -= Constants.TILE_SIZE;
            currentDeplacementCounter = 10;
            currentDeplacementDirection = 1; // Haut
        }
        else if (dir == 1) {
            if (isColliding(this, new Player(x, y + Constants.TILE_SIZE))) {
                return;
            }
            dy += Constants.TILE_SIZE;
            currentDeplacementCounter = 10;
            currentDeplacementDirection = 2; // Bas
        }
        else if (dir == 2) {
            if (isColliding(this, new Player(x - Constants.TILE_SIZE, y))) {
                return;
            }
            dx -= Constants.TILE_SIZE;
            currentDeplacementCounter = 10;
            currentDeplacementDirection = 3; // Gauche
        }
        else {
            if (isColliding(this, new Player(x - Constants.TILE_SIZE, y))) {
                return;
            }
            dx += Constants.TILE_SIZE;
            currentDeplacementCounter = 10;
            currentDeplacementDirection = 4; // Bas
        }
        x = x + dx;
        y = y + dy;
        for (Entity entity : GameController.getInstance().getLevel().getStaticEntities()) {
            if (CollisionCalculator.isColliding(this, entity)) {
                currentDeplacementCounter = 0;
            }
        }
        for (Entity entity : GameController.getInstance().getLevel().getDynamicEntities()) {
            if (CollisionCalculator.isColliding(this, entity)) {
                currentDeplacementCounter = 0;
            }
        }

        x = x - dx;
        y = y - dy;


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
}

    @Override
    public void render(GraphicsContext gc) {
        System.out.println("Monstre render");
        currentImage = Assets.monsterSprite;
        gc.drawImage(currentImage, x, y, size, size);
    }
}