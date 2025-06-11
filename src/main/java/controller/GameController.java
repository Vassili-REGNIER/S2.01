package controller;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import ui.Assets;
import ui.Constants;
import ui.Level;
import utils.LevelsCreator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameController extends BorderPane {

    // Permet d'accèder à l'instance de GameController depuis
    // n'importe où dans le projet
    private static GameController instance;

    @FXML
    Label vieJ1Label;
    @FXML
    Label vieJ2Label;
    @FXML
    Label scoreJ1Label;
    @FXML
    Label scoreJ2Label;
    @FXML
    Label timeLabel;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private Player player2;
    private Player player1;

    private Level level;

    private IntegerProperty timeElapsed = new SimpleIntegerProperty();

    private final Set<KeyCode> keysPressed = new HashSet<>();

    private AnimationTimer gameLoop;
    private boolean gameEnded = false;
    private String winnerText = "";

    public static GameController getInstance() {return instance; };
    public Level getLevel() {return level;}

    public GameController() {
        instance = this;
    }

    @FXML
    public void initialize() {

        // 1. Accès au GraphicsContext
        gc = canvas.getGraphicsContext2D();

        // 2. Charger les sprites
        Assets.load();

        level = LevelsCreator.getCurrentLevel();
        // 3. Créer les entités
        if (level.getNbJoueur() == 1) {
            player1 = level.getPlayers().getFirst();

            // Bind le score et les vies avec l'affichage
            vieJ1Label.textProperty().bind(player1.getNbLivesProperty().asString());
            scoreJ1Label.textProperty().bind(player1.getScoreProperty().asString());
        }
        else if (level.getNbJoueur() == 2) {
            player1 = level.getPlayers().get(0);
            player2 = level.getPlayers().get(1);

            // Bind le score et les vies avec l'affichage
            vieJ1Label.textProperty().bind(player1.getNbLivesProperty().asString());
            scoreJ1Label.textProperty().bind(player1.getScoreProperty().asString());

            vieJ2Label.textProperty().bind(player2.getNbLivesProperty().asString());
            scoreJ2Label.textProperty().bind(player2.getScoreProperty().asString());
        }

        //System.out.println("Nb entitées dynamiques" + level.getDynamicEntities().size());
        //System.out.println("Nb entitées statiques" + level.getStaticEntities().size());

        // 4. Gérer les entrées clavier
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                onKeyPressed(event);
            }
        });

        canvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                onKeyReleased(event);
            }
        });


        startTimer();
        // 5. Lancer la boucle de jeu
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                long elapsedNanos = now - lastUpdate;
                long interval = 40_000_000; //40_000_000 = 50 ms = 20 FPS (1000 ms / 50 ms = 20)

                if (elapsedNanos >= interval) {
                    update();
                    render();
                    lastUpdate = now;
                }
            }
        };
        gameLoop.start();

    }

    private void startTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> timeElapsed.set(timeElapsed.get() + 1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        timeLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            int totalSeconds = timeElapsed.get();
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }, timeElapsed));
    }


    private void onKeyPressed(KeyEvent event) {
        keysPressed.add(event.getCode());
        updatePlayerMovement();
        updateBombePlacement();
    }

    private void onKeyReleased(KeyEvent event) {
        keysPressed.remove(event.getCode());
        updatePlayerMovement();
        updateBombePlacement();
    }

    private void updatePlayerMovement() {
        player1.setMovingUp(keysPressed.contains(KeyCode.Z));
        player1.setMovingDown(keysPressed.contains(KeyCode.S));
        player1.setMovingLeft(keysPressed.contains(KeyCode.Q));
        player1.setMovingRight(keysPressed.contains(KeyCode.D));

        if (player2 != null) {
            player2.setMovingUp(keysPressed.contains(KeyCode.UP));
            player2.setMovingDown(keysPressed.contains(KeyCode.DOWN));
            player2.setMovingLeft(keysPressed.contains(KeyCode.LEFT));
            player2.setMovingRight(keysPressed.contains(KeyCode.RIGHT));
        }
    }

    private void updateBombePlacement() {
        if (keysPressed.contains(KeyCode.E)) {
            player1.placeBomb();
        }

        if (player2 != null && keysPressed.contains(KeyCode.NUMPAD0)) {
            player2.placeBomb();
        }
    }

    private void update() {
        player1.update();
        if (player2 != null) {
            player2.update();
        }

        for (Entity entity : new ArrayList<Entity>(level.getDynamicEntities())) {
            entity.update();
        }

        checkWinCondition();
    }

    private void render() {
        // Effacer l’ancienne frame
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Chargement de l'arrière plan
        gc.drawImage(Assets.background, 0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessiner les entités statiques (murs, sol)
        for (Entity entity : level.getStaticEntities()) {
            entity.render(gc);
        }

        // Dessiner les entités dynamiques
        for (Entity entity : level.getDynamicEntities()) {
            entity.render(gc);
        }

        // Dessiner le joueur
        player1.render(gc);
        if (player2 != null) {
            player2.render(gc);
        }

        if (gameEnded) {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.setFont(javafx.scene.text.Font.font(48));
            gc.fillText(winnerText, canvas.getWidth() / 2 - 150, canvas.getHeight() / 2);
        }
    }

    private void checkWinCondition() {
        if (player2 != null) {
            if (player1.getNbLives() <= 0) {
                winnerText = "Joueur 2 a gagné !";
                endGame();
            } else if (player2.getNbLives() <= 0) {
                winnerText = "Joueur 1 a gagné !";
                endGame();
            }
        } else {
            int nbFantomes = 0;
            for (Entity e : level.getDynamicEntities()) {
                if (e instanceof Monster) {
                    nbFantomes++;
                }
            }
            System.out.println(nbFantomes);
            if (player1.getNbLives() <= 0) {
                winnerText = "Game Over";
                endGame();
            } else if (level.getDynamicEntities().isEmpty()) {
                winnerText = "Vous avez gagné !";
                endGame();
            }
        }
    }

    private void endGame() {
        gameEnded = true;
        gameLoop.stop();
    }
}
