package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.Assets;
import ui.Constants;
import utils.Direction;

import static ui.CollisionCalculator.isColliding;

public class Player extends Entity {

    private double speed = (double) Constants.TILE_SIZE / 5;      // vitesse de déplacement en pixels par frame
    //private Direction direction;     // direction du joueur (pas obligatoire au départ)

    private boolean movingUp, movingDown, movingLeft, movingRight;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = Constants.TILE_SIZE;
        //this.direction = Direction.DOWN;  // direction initiale (exemple)
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
            //direction = Direction.UP;
        }
        if (movingDown) {
            dy += speed;
            //direction = Direction.DOWN;
        }
        if (movingLeft) {
            dx -= speed;
            //direction = Direction.LEFT;
        }
        if (movingRight) {
            dx += speed;
            //direction = Direction.RIGHT;
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
        gc.drawImage(Assets.playerSprite, x, y, size, size);
    }
}
