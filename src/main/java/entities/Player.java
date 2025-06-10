package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.CollisionCalculator;
import ui.Constants;

import static ui.CollisionCalculator.isColliding;

public class Player extends Entity {

    private double speed = (double) Constants.TILE_SIZE / 5;      // vitesse de déplacement en pixels par frame
    private int currentDeplacementCounter = 0;
    private int currentDeplacementDirection;
    private boolean movingUp, movingDown, movingLeft, movingRight, isBomb;
    private Image currentImage;
    private final double size;
    private int numberPlayer;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE * 0.8;
        this.currentImage = Assets.player1Down;
        this.numberPlayer = 1;
    }

    public Player(double x, double y, int numberPlayer) {
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE * 0.8;
        this.currentImage = Assets.player1Down;
        this.numberPlayer = numberPlayer;
    }

    // Méthodes pour gérer les entrées clavier (appelées depuis le contrôleur)
    public void setMovingUp(boolean moving) { movingUp = moving; }
    public void setMovingDown(boolean moving) { movingDown = moving; }
    public void setMovingLeft(boolean moving) { movingLeft = moving; }
    public void setMovingRight(boolean moving) { movingRight = moving; }
    public void setBombBool(boolean isBombSet) { isBomb = isBombSet; }

    public double getX() {
        return super.getX();
    }

    public double getY() {
        return super.getY();
    }

    public double getSize() {
        return super.getSize();
    }


    @Override
    public void update() {

        // Si le joueur ne bouge plus permet de lancer un déplacement
        if (currentDeplacementCounter <= 0) {
            double dx = 0, dy = 0;
            if (movingUp) {
                dy -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 1; // Haut
            }
            else if (movingDown) {
                if (isColliding(this, new Player(x, y + Constants.TILE_SIZE))) {
                    return;
                }
                dy += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 2; // Bas
            }
            else if (movingLeft) {
                if (isColliding(this, new Player(x - Constants.TILE_SIZE, y))) {
                    return;
                }
                dx -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 3; // Gauche
            }
            else if (movingRight) {
                if (isColliding(this, new Player(x - Constants.TILE_SIZE, y))) {
                    return;
                }
                dx += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
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

    public void placeBomb() {
        double gap = Constants.TILE_SIZE * 0.1;
        Bomb bomb = new Bomb(x - gap, y - gap);
        GameController.getInstance().getLevel().getDynamicEntities().add(bomb);
        System.out.println("Bombe posée à : x=" + bomb.getX() + ", y=" + bomb.getY());
    }

    @Override
    public void render(GraphicsContext gc) {
         if (movingUp && numberPlayer == 1) {
             currentImage = Assets.player1Up;
         }
         else if (movingRight  && numberPlayer == 1) {
             currentImage = Assets.player1Right;
         }
         else if (movingLeft && numberPlayer == 1) {
             currentImage = Assets.player1Left;
         }
         else if (movingDown && numberPlayer == 1) {
             currentImage = Assets.player1Down;
         }
         else if (movingDown  && numberPlayer == 2) {
             currentImage = Assets.player2Down;
         }
         else if (movingUp  && numberPlayer == 2) {
             currentImage = Assets.player2Up;
         }
         else if (movingLeft  && numberPlayer == 2) {
             currentImage = Assets.player2Left;
         }
         else if (movingRight  && numberPlayer == 2) {
             currentImage = Assets.player2Right;
         }

         gc.drawImage(currentImage, x, y, size, size);

//         Affiche la hitbox
//         double radius = 2;
//         gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
//         gc.fillOval(x - radius + size, y - radius + size, radius * 2, radius * 2);

    }


}