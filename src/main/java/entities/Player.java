package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.Constants;

import static ui.CollisionCalculator.isColliding;

public class Player extends Entity {

    private double speed = (double) Constants.TILE_SIZE / 5;      // vitesse de déplacement en pixels par frame

    private boolean movingUp, movingDown, movingLeft, movingRight;
    private Image currentImage;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE;
        this.currentImage = Assets.player1Down;  // direction initiale (exemple)
    }

    // Méthodes pour gérer les entrées clavier (appelées depuis le contrôleur)
    public void setMovingUp(boolean moving) { movingUp = moving; }
    public void setMovingDown(boolean moving) { movingDown = moving; }
    public void setMovingLeft(boolean moving) { movingLeft = moving; }
    public void setMovingRight(boolean moving) { movingRight = moving; }

    @Override
    public void update() {
        double dx = 0, dy = 0;
        if (movingUp) {
            dy -= speed;
        }
        if (movingDown) {
            dy += speed;
        }
        if (movingLeft) {
            dx -= speed;
        }
        if (movingRight) {
            dx += speed;
        }

        // Déplace le joueur
        x += dx;
        y += dy;

        // vérifier collision avec chaque mur
        for (Entity entity : GameController.getInstance().getStaticEntities()) {
            if (isColliding(this, entity)) {
                // Annule le déplacement si il y a une colision
                x -= dx;
                y -= dy;
                return;
            }
        }
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
         gc.drawImage(currentImage, x, y);
    }

}
