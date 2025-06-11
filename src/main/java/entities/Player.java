package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.CollisionCalculator;
import ui.Constants;

import static ui.CollisionCalculator.isColliding;

public class Player extends Entity {

    private final double initialX;
    private final double initialY;
    private double speed = (double) Constants.TILE_SIZE / 5;  // vitesse de déplacement
    private int currentDeplacementCounter = 0;
    private int currentDeplacementDirection;
    private boolean movingUp, movingDown, movingLeft, movingRight;
    private Image currentImage;
    private final double size;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.initialY = y;
        this.size = Constants.TILE_SIZE * 0.8;
        this.currentImage = Assets.player1Down;  // direction initiale
    }

    // Méthodes pour gérer les entrées clavier
    public void setMovingUp(boolean moving) { movingUp = moving; }
    public void setMovingDown(boolean moving) { movingDown = moving; }
    public void setMovingLeft(boolean moving) { movingLeft = moving; }
    public void setMovingRight(boolean moving) { movingRight = moving; }

    @Override
    public void update() {
        if (currentDeplacementCounter <= 0) {
            double dx = 0, dy = 0;
            if (movingUp) {
                dy -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 1;
            } else if (movingDown) {
                if (isColliding(this, new Player(x, y + Constants.TILE_SIZE))) return;
                dy += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 2;
            } else if (movingLeft) {
                if (isColliding(this, new Player(x - Constants.TILE_SIZE, y))) return;
                dx -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 3;
            } else if (movingRight) {
                if (isColliding(this, new Player(x - Constants.TILE_SIZE, y))) return;
                dx += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 4;
            }

            x += dx;
            y += dy;

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

            x -= dx;
            y -= dy;

        } else {
            currentDeplacementCounter--;
            switch (currentDeplacementDirection) {
                case 1 -> y -= speed;
                case 2 -> y += speed;
                case 3 -> x -= speed;
                case 4 -> x += speed;
            }
        }
    }

    public void placeBomb() {
        double gap = Constants.TILE_SIZE * 0.1;
        Bomb bomb = new Bomb(x - gap, y - gap);
        GameController.getInstance().getLevel().getDynamicEntities().add(bomb);
        System.out.println("Bombe posée à : x=" + bomb.getX() + ", y=" + bomb.getY());
    }

    @Override
    public void render(GraphicsContext gc) {
        if (movingUp) {
            currentImage = Assets.player1Up;
        } else if (movingRight) {
            currentImage = Assets.player1Right;
        } else if (movingLeft) {
            currentImage = Assets.player1Left;
        } else if (movingDown) {
            currentImage = Assets.player1Down;
        }

        gc.drawImage(currentImage, x, y, size, size);

        // Debug : hitbox
        // double radius = 2;
        // gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        // gc.fillOval(x - radius + size, y - radius + size, radius * 2, radius * 2);
    }

    public void reset() {
        this.x = initialX;
        this.y = initialY;
        this.currentDeplacementCounter = 0;
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
        this.currentImage = Assets.player1Down;
    }
}
