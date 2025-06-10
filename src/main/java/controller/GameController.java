package controller;

import entities.*;
import javafx.animation.AnimationTimer;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.Assets;
import ui.Constants;
import ui.Level;
import utils.LevelsCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Contrôleur principal du jeu gérant la logique de gameplay,
 * les entrées utilisateur et le rendu graphique.
 */
public class GameController extends BorderPane {

    // Permet d'accèder à l'instance de GameController depuis
    // n'importe où dans le projet
    private static GameController instance;

    @FXML
    private Canvas canvas;
    @FXML
    private VBox pauseMenu;
    private GraphicsContext gc;
    private Player player2;
    private Player player1;

    private Level level;
    private boolean isPaused = false;

    private final Set<KeyCode> keysPressed = new HashSet<>();

    private AnimationTimer gameLoop;

    /**
     * Retourne l'instance singleton du GameController.
     * @return L'instance du GameController
     */
    public static GameController getInstance() {return instance; }

    /**
     * Retourne le niveau actuel du jeu.
     * @return Le niveau en cours
     */
    public Level getLevel() {return level;}

    /**
     * Constructeur du GameController.
     * Initialise l'instance singleton.
     */
    public GameController() {
        instance = this;
    }

    /**
     * Méthode d'initialisation appelée automatiquement par JavaFX
     * après le chargement du FXML. Configure le jeu et démarre la boucle principale.
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
        }
        else if (level.getNbJoueur() == 2) {
            player1 = level.getPlayers().get(0);
            player2 = level.getPlayers().get(1);
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

        // 5. Lancer la boucle de jeu
        startGameLoop();

        // 6. Initialiser le menu de pause (masqué par défaut)
        pauseMenu.setVisible(false);
    }

    /**
     * Démarre la boucle principale du jeu avec un timer d'animation.
     * Configure la fréquence de mise à jour à 20 FPS.
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                long elapsedNanos = now - lastUpdate;
                long interval = 50_000_000; // 50 ms = 20 FPS (1000 ms / 50 ms = 20)

                if (elapsedNanos >= interval) {
                    update();
                    render();
                    lastUpdate = now;
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Action exécutée lors du clic sur le bouton "Restart".
     * Redémarre le niveau actuel en réinitialisant tous les éléments du jeu.
     * @param event L'événement de clic sur le bouton
     */
    @FXML
    private void onRestartClicked(ActionEvent event) {
        try {
            // Masquer le menu de pause
            pauseMenu.setVisible(false);
            isPaused = false;

            // Arrêter la boucle de jeu actuelle
            if (gameLoop != null) {
                gameLoop.stop();
            }

            // Vider les touches pressées
            keysPressed.clear();

            // Recharger le niveau actuel
            level = LevelsCreator.getCurrentLevel();

            // Réinitialiser les joueurs
            if (level.getNbJoueur() == 1) {
                player1 = level.getPlayers().getFirst();
                player2 = null;
            } else if (level.getNbJoueur() == 2) {
                player1 = level.getPlayers().get(0);
                player2 = level.getPlayers().get(1);
            }

            // Redémarrer la boucle de jeu
            startGameLoop();

            // Remettre le focus sur le canvas pour les contrôles clavier
            canvas.requestFocus();

        } catch (Exception e) {
            System.err.println("Erreur lors du redémarrage du niveau : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Action exécutée lors du clic sur le bouton "Quitter".
     * Retourne à la page d'accueil de l'application.
     * @param event L'événement de clic sur le bouton
     */
    @FXML
    private void onQuitterClicked(ActionEvent event) {
        try {
            // Arrêter la boucle de jeu
            if (gameLoop != null) {
                gameLoop.stop();
            }

            // Vider les touches pressées
            keysPressed.clear();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) canvas.getScene().getWindow();

            // Charger la page d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
            Parent root = loader.load();

            // Créer et afficher la nouvelle scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page d'accueil : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture du jeu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère les événements de touche pressée.
     * @param event L'événement clavier
     */
    private void onKeyPressed(KeyEvent event) {
        // Gérer la touche Échap pour afficher/masquer le menu de pause
        if (event.getCode() == KeyCode.ESCAPE) {
            togglePauseMenu();
            return;
        }

        // Si le jeu est en pause, ignorer les autres touches
        if (isPaused) {
            return;
        }

        keysPressed.add(event.getCode());
        updatePlayerMovement();
        updateBombePlacement();
    }

    /**
     * Gère les événements de touche relâchée.
     * @param event L'événement clavier
     */
    private void onKeyReleased(KeyEvent event) {
        // Si le jeu est en pause, ignorer les touches
        if (isPaused) {
            return;
        }

        keysPressed.remove(event.getCode());
        updatePlayerMovement();
        updateBombePlacement();
    }

    /**
     * Met à jour les mouvements des joueurs en fonction des touches pressées.
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
     * Gère le placement des bombes par les joueurs.
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
     * Met à jour la logique du jeu (positions des entités, collisions, etc.).
     * Ne fait rien si le jeu est en pause.
     */
    private void update() {
        // Ne pas mettre à jour si le jeu est en pause
        if (isPaused) {
            return;
        }

        player1.update();
        if (player2 != null) {
            player2.update();
        }

        for (Entity entity : new ArrayList<Entity>(level.getDynamicEntities())) {
            entity.update();
        }
    }

    /**
     * Effectue le rendu graphique de tous les éléments du jeu.
     */
    private void render() {
        // Effacer l'ancienne frame
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
    }

    /**
     * Affiche ou masque le menu de pause et met le jeu en pause/reprend le jeu.
     */
    private void togglePauseMenu() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);

        if (isPaused) {
            // Vider les touches pressées quand on met en pause
            keysPressed.clear();
        } else {
            // Remettre le focus sur le canvas quand on reprend
            canvas.requestFocus();
        }
    }
}