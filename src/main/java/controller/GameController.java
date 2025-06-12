package controller;

import entities.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Assets;
import utils.Level;
import utils.LevelsCreator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Contrôleur principal du jeu BombermanFX. Gère l'initialisation du niveau,
 * la boucle de jeu, le rendu graphique, les entrées clavier, le placement des bombes,
 * la vérification des conditions de victoire, ainsi que les actions utilisateur
 * comme redémarrer une partie ou retourner à l'accueil.
 *
 * Ce contrôleur suit le patron Singleton via l'attribut statique {@code instance}.
 */
public class GameController extends BorderPane {

    /**
     * Instance unique de GameController accessible globalement.
     */
    private static GameController instance;

    /** Label affichant les vies du Joueur 1. */
    @FXML
    Label vieJ1Label;

    /** Label affichant les vies du Joueur 2. */
    @FXML
    Label vieJ2Label;

    /** Label affichant le score du Joueur 1. */
    @FXML
    Label scoreJ1Label;

    /** Label affichant le score du Joueur 2. */
    @FXML
    Label scoreJ2Label;

    /** Label affichant le temps écoulé. */
    @FXML
    Label timeLabel;

    /** Canvas sur lequel est dessiné le jeu. */
    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private Player player1;
    private Player player2;
    private Level level;
    private IntegerProperty timeElapsed = new SimpleIntegerProperty();
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private AnimationTimer gameLoop;
    private boolean gameEnded = false;
    private String winnerText = "";

    /**
     * Retourne l'instance unique du contrôleur.
     *
     * @return L'instance de GameController.
     */
    public static GameController getInstance() { return instance; }

    /**
     * Retourne le niveau courant.
     *
     * @return Le niveau courant.
     */
    public Level getLevel() { return level; }

    /**
     * Constructeur par défaut. Initialise l'instance statique.
     */
    public GameController() {
        instance = this;
    }

    /**
     * Initialise le jeu : charge les ressources graphiques, initialise le niveau,
     * configure les entrées clavier, démarre le timer et la boucle principale.
     */
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
        gameLoop = createGameLoop();
        gameLoop.start();

    }

    /**
     * Crée et configure la boucle principale du jeu.
     *
     * @return L'AnimationTimer représentant la boucle de jeu.
     */
    private AnimationTimer createGameLoop() {
        return new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                long elapsedNanos = now - lastUpdate;
                if (elapsedNanos >= 40_000_000) {
                    update();
                    render();
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Démarre le timer pour afficher le temps écoulé.
     */
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


    /**
     * Méthode appelée lorsqu'une touche est pressée.
     *
     * @param event L'événement clavier.
     */
    private void onKeyPressed(KeyEvent event) {
        keysPressed.add(event.getCode());
        updatePlayerMovement();
        updateBombePlacement();
    }

    /**
     * Méthode appelée lorsqu'une touche est relâchée.
     *
     * @param event L'événement clavier.
     */
    private void onKeyReleased(KeyEvent event) {
        keysPressed.remove(event.getCode());
        updatePlayerMovement();
        updateBombePlacement();
    }

    /**
     * Met à jour l'état des déplacements des joueurs selon les touches pressées.
     */
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

    /**
     * Gère la pose de bombes pour les joueurs.
     */
    private void updateBombePlacement() {
        if (keysPressed.contains(KeyCode.E)) {
            player1.placeBomb();
        }

        if (player2 != null && keysPressed.contains(KeyCode.NUMPAD0)) {
            player2.placeBomb();
        }
    }

    /**
     * Met à jour l'état de toutes les entités du jeu à chaque itération.
     */
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

    /**
     * Dessine l'état courant du jeu sur le canvas.
     */
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

    /**
     * Vérifie si une condition de victoire ou de défaite est remplie.
     */
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

            if (player1.getNbLives() <= 0) {
                winnerText = "Game Over";
                endGame();
            } else if (nbFantomes == 0) {
                winnerText = "Vous avez gagné !";
                endGame();
            }
        }
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Recommencer".
     * Réinitialise l'état du jeu et redémarre la boucle.
     *
     * @param event L'événement déclenché par le clic.
     */
    @FXML
    private void onRestartClicked(ActionEvent event) {
        if (gameLoop != null) gameLoop.stop();

        // Nettoyage
        keysPressed.clear();
        gameEnded = false;
        winnerText = "";


        level.reset();
        player1 = level.getPlayers().get(0);
        player2 = level.getNbJoueur() == 2 ? level.getPlayers().get(1) : null;


        if (player2 != null) {
            vieJ2Label.textProperty().bind(player2.getNbLivesProperty().asString());
            scoreJ2Label.textProperty().bind(player2.getScoreProperty().asString());
        }

        // Réinitialise le temps
        timeElapsed.set(0);

        canvas.requestFocus();
        gameLoop = createGameLoop();
        gameLoop.start();

        System.out.println("restartClicked");
    }


    /**
     * Méthode appelée lors du clic sur le bouton "Retour à l'accueil".
     * Ferme la fenêtre actuelle et recharge l'écran d'accueil.
     *
     * @param event L'événement déclenché par le clic.
     */
    @FXML
    private void onRetourAccueilClicked(ActionEvent event) {
        try {
            Stage gameStage = (Stage) canvas.getScene().getWindow();
            gameStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Accueil.fxml"));
            Parent root = loader.load();

            Stage accueilStage = new Stage();
            accueilStage.setTitle("Super Bomberman FX");
            accueilStage.setScene(new Scene(root));
            accueilStage.setFullScreen(true);
            accueilStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Termine la partie et arrête la boucle de jeu.
     */
    private void endGame() {
        gameEnded = true;
        gameLoop.stop();
    }
}
