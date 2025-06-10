package controller;

import bombermanfx.MusicPlayer;
import bombermanfx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.Level;
import utils.LevelsCreator;

import java.net.URL;

public class AccueilController {

    @FXML
    private Button soundToggleButton;

    @FXML
    private ImageView soundIcon;

    @FXML
    private VBox mainMenu;

    @FXML
    private VBox multiplayerMenu;

    @FXML
    private Button themeButton;

    @FXML
    private VBox root;

    private boolean isMuted = false;

    @FXML
    private void onToggleSound() {
        if (isMuted) {
            MusicPlayer.playMusic("/sounds/menu.mp3");
            Image soundOnImage = new Image(
                    getClass().getResource("/images/home/sound-on.png").toExternalForm(),
                    40, 40, true, true
            );
            soundIcon.setImage(soundOnImage);
        } else {
            MusicPlayer.stopMusic();
            Image soundOffImage = new Image(
                    getClass().getResource("/images/home/sound-off.png").toExternalForm(),
                    40, 40, true, true
            );
            soundIcon.setImage(soundOffImage);
        }
        isMuted = !isMuted;
    }

    @FXML
    public void onMultiplayerClicked() {
        mainMenu.setVisible(false);
        mainMenu.setManaged(false);
        multiplayerMenu.setVisible(true);
        multiplayerMenu.setManaged(true);
    }

    @FXML
    public void onBackToMainMenu() {
        multiplayerMenu.setVisible(false);
        multiplayerMenu.setManaged(false);
        mainMenu.setVisible(true);
        mainMenu.setManaged(true);
    }

    @FXML
    private void onQuitterClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        MusicPlayer.stopMusic();
    }

    @FXML
    public void initialize() {
        if (soundIcon != null) {
            soundIcon.setImage(new Image(getClass().getResource("/images/home/sound-on.png").toExternalForm()));
        }
        MusicPlayer.playMusic("/sounds/menu.mp3");
        soundIcon.getStyleClass().add("sound-icon");
        mainMenu.setVisible(true);
        mainMenu.setManaged(true);
        multiplayerMenu.setVisible(false);
        multiplayerMenu.setManaged(false);
        LevelsCreator.initLevels();
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Jouer".
     * Ferme la fenêtre d'accueil et lance la scène principale du jeu.
     *
     * @param event L'événement déclenché par le clic.
     */
    @FXML
    public void lancerSolo(ActionEvent event) {
        LevelsCreator.setCurrentLevel(1);

        // Ferme la fenêtre
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();

        lancerJeuMode(event);
    }

    @FXML
    public void lancer1v1(ActionEvent event) {
        LevelsCreator.setCurrentLevel(2);

        // Ferme la fenêtre
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();

        lancerJeuMode(event);
    }

    @FXML
    public void lancer1vMachine(ActionEvent event) {
        LevelsCreator.setCurrentLevel(3);

        // Ferme la fenêtre
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();

        lancerJeuMode(event);
    }

    @FXML
    public void lancerJeuMode(ActionEvent event) {
        try {
            Main jeu = new Main();
            Stage stageJeu = new Stage();
            jeu.start(stageJeu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}