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
    private boolean movingUp, movingDown, movingLeft, movingRight;
    private Image currentImage;
    private int nbLives = 5;

    public Player(double x, double y) {
        super(x, y, 0.8 * Constants.TILE_SIZE);
        this.currentImage = Assets.player1Down;  // direction initiale (exemple)
    }

    // Méthodes pour gérer les entrées clavier (appelées depuis le contrôleur)
    public void setMovingUp(boolean moving) { movingUp = moving; }
    public void setMovingDown(boolean moving) { movingDown = moving; }
    public void setMovingLeft(boolean moving) { movingLeft = moving; }
    public void setMovingRight(boolean moving) { movingRight = moving; }

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

    public void removeLife() {
        this.nbLives--;
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
         }
         else if (movingRight) {
             currentImage = Assets.player1Right;
         }
         else if (movingLeft) {
             currentImage = Assets.player1Left;
         }
         else if (movingDown) {
             currentImage = Assets.player1Down;
         }
         gc.drawImage(currentImage, x, y, size, size);

//         Affiche la hitbox
//         double radius = 2;
//         gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
//         gc.fillOval(x - radius + size, y - radius + size, radius * 2, radius * 2);

    }


    public int getNbLives() {
        return nbLives;
    }

    public void setNbLives(int nbLives) {
        this.nbLives = nbLives;
    }
}