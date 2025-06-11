package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import ui.Assets;
import ui.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player extends Entity {

    private double speed = (double) Constants.TILE_SIZE / 5;      // vitesse de déplacement en pixels par frame
    private int currentDeplacementCounter = 0;
    private int currentDeplacementDirection;
    private boolean movingUp, movingDown, movingLeft, movingRight;
    private SimpleIntegerProperty nbLives, score;
    private int numberPlayer;
    private boolean canPlaceBomb = true, canLoseLife = true;

    public Player(double x, double y) {
        super(x, y, 0.8 * Constants.TILE_SIZE);
        this.numberPlayer = 1;
        nbLives = new SimpleIntegerProperty(5);
        score = new SimpleIntegerProperty(0);
    }

    public Player(double x, double y, int numberPlayer) {
        super(x, y, 0.8 * Constants.TILE_SIZE);
        this.numberPlayer = numberPlayer;
        nbLives = new SimpleIntegerProperty(5);
        score = new SimpleIntegerProperty(0);
    }

    // Méthodes pour gérer les entrées clavier (appelées depuis le contrôleur)
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
                currentDeplacementDirection = 1; // Haut
            }
            else if (movingDown) {
                dy += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 2; // Bas
            }
            else if (movingLeft) {
                dx -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 3; // Gauche
            }
            else if (movingRight) {
                dx += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 4; // Droite
            } else {
                return;
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
    }

    public void placeBomb() {
        if (canPlaceBomb) {
            // Pose la bombe
            double gap = Constants.TILE_SIZE * 0.1;
            Bomb bomb = new Bomb(x - gap, y - gap, this);
            GameController.getInstance().getLevel().getDynamicEntities().add(bomb);

            // Active le cooldown
            canPlaceBomb = false;
            Timeline cooldown = new Timeline(new KeyFrame(Duration.seconds(2), e -> canPlaceBomb = true));
            cooldown.setCycleCount(1);
            cooldown.play();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Images de base si pas de déplacement
        if (numberPlayer == 1) {
            if (currentDeplacementDirection == 1) {
                currentImage = Assets.player1Up;
            } else if (currentDeplacementDirection == 4) {
                currentImage = Assets.player1Right;
            } else if (currentDeplacementDirection == 3) {
                currentImage = Assets.player1Left;
            } else {
                currentImage = Assets.player1Down;
            }
        } else {
            if (currentDeplacementDirection == 1) {
                currentImage = Assets.player1Up;
            } else if (currentDeplacementDirection == 4) {
                currentImage = Assets.player1Right;
            } else if (currentDeplacementDirection == 3) {
                currentImage = Assets.player1Left;
            } else {
                currentImage = Assets.player1Down;
            }
        }
        gc.drawImage(currentImage, x, y, size, size);
    }

    public int getScore() {return score.getValue();}
    public void setScore(int score) {this.score.setValue(score);}
    public IntegerProperty getScoreProperty() {return score;}
    public int getNbLives() {return nbLives.getValue();}
    public void setNbLives(int nbLives) {this.nbLives.setValue(nbLives);}
    public IntegerProperty getNbLivesProperty() {return nbLives;}
    public void removeLife() {
        if (canLoseLife && nbLives.getValue() > 0) {
            // Enleve la vie
            nbLives.setValue(nbLives.getValue() - 1);

            // Active le cooldown
            canLoseLife = false;
            Timeline cooldown = new Timeline(new KeyFrame(Duration.seconds(2), e -> canLoseLife = true));
            cooldown.setCycleCount(1);
            cooldown.play();
        }
    }
}