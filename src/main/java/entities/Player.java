package entities;

import controller.GameController;
import javafx.scene.canvas.GraphicsContext;
import utils.Assets;
import utils.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Classe représentant un joueur dans le jeu.
 * Un joueur peut se déplacer, poser des bombes, perdre des vies, accumuler des points, et réinitialiser sa position.
 */
public class Player extends Entity {

    /** Vitesse de déplacement du joueur (en pixels par frame). */
    private double speed = (double) Constants.TILE_SIZE / 5;

    /** Nombre de frames restantes avant de recalculer un déplacement. */
    private int currentDeplacementCounter = 0;

    /** Direction actuelle du déplacement (1=haut, 2=bas, 3=gauche, 4=droite). */
    private int currentDeplacementDirection;

    /** Indicateurs de mouvement en fonction des touches pressées. */
    private boolean movingUp, movingDown, movingLeft, movingRight;

    /** Nombre de vies du joueur. */
    private SimpleIntegerProperty nbLives;

    /** Score du joueur. */
    private SimpleIntegerProperty score;

    /** Numéro du joueur (1 ou 2). */
    private int numberPlayer;

    /** Indique si le joueur peut poser une bombe (cooldown). */
    private boolean canPlaceBomb = true;

    /** Indique si le joueur peut perdre une vie (invincibilité temporaire après dégât). */
    private boolean canLoseLife = true;

    /** Position initiale du joueur. */
    private final double initialX;
    private final double initialY;

    /**
     * Construit un joueur à la position spécifiée.
     *
     * @param x Position x initiale.
     * @param y Position y initiale.
     */
    public Player(double x, double y) {
        super(x, y, 0.8 * Constants.TILE_SIZE);
        this.numberPlayer = 1;
        this.initialX = x;
        this.initialY = y;
        nbLives = new SimpleIntegerProperty(5);
        score = new SimpleIntegerProperty(0);
    }

    /**
     * Construit un joueur à la position spécifiée, en précisant le numéro du joueur.
     *
     * @param x            Position x initiale.
     * @param y            Position y initiale.
     * @param numberPlayer Numéro du joueur (1 ou 2).
     */
    public Player(double x, double y, int numberPlayer) {
        super(x, y, 0.8 * Constants.TILE_SIZE);
        this.initialX = x;
        this.initialY = y;
        this.numberPlayer = numberPlayer;
        nbLives = new SimpleIntegerProperty(5);
        score = new SimpleIntegerProperty(0);
    }

    /**
     * Définit si le joueur est en train d'essayer de monter.
     *
     * @param moving Vrai si le joueur se déplace vers le haut.
     */
    public void setMovingUp(boolean moving) { movingUp = moving; }

    /**
     * Définit si le joueur est en train d'essayer de descendre.
     *
     * @param moving Vrai si le joueur se déplace vers le bas.
     */
    public void setMovingDown(boolean moving) { movingDown = moving; }

    /**
     * Définit si le joueur est en train d'essayer d'aller à gauche.
     *
     * @param moving Vrai si le joueur se déplace vers la gauche.
     */
    public void setMovingLeft(boolean moving) { movingLeft = moving; }

    /**
     * Définit si le joueur est en train d'essayer d'aller à droite.
     *
     * @param moving Vrai si le joueur se déplace vers la droite.
     */
    public void setMovingRight(boolean moving) { movingRight = moving; }

    /**
     * Met à jour la position du joueur en fonction des entrées clavier et gère les collisions.
     */
    @Override
    public void update() {
        if (currentDeplacementCounter <= 0) {
            double dx = 0, dy = 0;
            if (movingUp) {
                dy -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 1;
            } else if (movingDown) {
                dy += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 2;
            } else if (movingLeft) {
                dx -= Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 3;
            } else if (movingRight) {
                dx += Constants.TILE_SIZE;
                currentDeplacementCounter = 5;
                currentDeplacementDirection = 4;
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

    /**
     * Permet au joueur de poser une bombe si le cooldown le permet.
     * Une fois posée, un cooldown de 2 secondes empêche la pose d'une autre bombe immédiatement.
     */
    public void placeBomb() {
        if (canPlaceBomb) {
            double gap = Constants.TILE_SIZE * 0.1;
            Bomb bomb = new Bomb(x - gap, y - gap, this);
            GameController.getInstance().getLevel().getDynamicEntities().add(bomb);

            canPlaceBomb = false;
            Timeline cooldown = new Timeline(new KeyFrame(Duration.seconds(2), e -> canPlaceBomb = true));
            cooldown.setCycleCount(1);
            cooldown.play();
        }
    }

    /**
     * Affiche le joueur à l'écran, avec l'image appropriée selon sa direction ou son état.
     *
     * @param gc Contexte graphique.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (numberPlayer == 1) {
            if (!canLoseLife) currentImage = Assets.player1Dead;
            else if (currentDeplacementDirection == 1) currentImage = Assets.player1Up;
            else if (currentDeplacementDirection == 4) currentImage = Assets.player1Right;
            else if (currentDeplacementDirection == 3) currentImage = Assets.player1Left;
            else currentImage = Assets.player1Down;

        } else {
            if (!canLoseLife) currentImage = Assets.player2Dead;
            else if (currentDeplacementDirection == 1) currentImage = Assets.player2Up;
            else if (currentDeplacementDirection == 4) currentImage = Assets.player2Right;
            else if (currentDeplacementDirection == 3) currentImage = Assets.player2Left;
            else currentImage = Assets.player2Down;
        }
        gc.drawImage(currentImage, x, y, size, size);
    }

    /** @return Score actuel du joueur. */
    public int getScore() { return score.getValue(); }

    /**
     * Définit le score du joueur.
     * @param score Nouveau score.
     */
    public void setScore(int score) { this.score.setValue(score); }

    /**
     * @return Propriété JavaFX associée au score du joueur.
     */
    public IntegerProperty getScoreProperty() { return score; }

    /** @return Nombre de vies restantes du joueur. */
    public int getNbLives() { return nbLives.getValue(); }

    /**
     * Définit le nombre de vies du joueur.
     * @param nbLives Nouveau nombre de vies.
     */
    public void setNbLives(int nbLives) { this.nbLives.setValue(nbLives); }

    /**
     * @return Propriété JavaFX associée au nombre de vies du joueur.
     */
    public IntegerProperty getNbLivesProperty() { return nbLives; }

    /**
     * Retire une vie au joueur, puis active un délai d'invincibilité temporaire.
     */
    public void removeLife() {
        if (canLoseLife && nbLives.getValue() > 0) {
            nbLives.setValue(nbLives.getValue() - 1);
            canLoseLife = false;
            Timeline cooldown = new Timeline(new KeyFrame(Duration.seconds(2), e -> canLoseLife = true));
            cooldown.setCycleCount(1);
            cooldown.play();
        }
    }

    /**
     * Réinitialise le joueur à sa position initiale et restaure ses valeurs par défaut.
     */
    public void reset() {
        this.x = initialX;
        this.y = initialY;
        this.currentDeplacementCounter = 0;
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
        this.currentImage = Assets.player1Down;
        this.nbLives.setValue(5);
        this.score.setValue(0);
    }
}
