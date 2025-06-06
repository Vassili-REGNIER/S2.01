package controller;

import bombermanfx.MusicPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import bombermanfx.Main;

/**
 * Contrôleur pour la scène d'accueil de BombermanFX.
 * Gère les interactions utilisateur sur la page d'accueil.
 */
public class AccueilController {

    @FXML
    private Button soundToggleButton;

    @FXML
    private ImageView soundIcon;

    private boolean isMuted = false;

    /**
     * Méthode appelée lors du clic sur le bouton "Jouer".
     * Ferme la fenêtre d'accueil et lance la scène principale du jeu.
     *
     * @param event L'événement déclenché par le clic.
     */
    public void onJouerClicked(ActionEvent event) {
        // Fermer la fenêtre d'accueil
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();

        // Lancer la scène principale via Main
        try {
            Main jeu = new Main();
            Stage stageJeu = new Stage();
            jeu.start(stageJeu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur le bouton de son.
     * Permet d'activer/désactiver la musique de fond.
     */
    @FXML
    private void onToggleSound() {
        if (isMuted) {
            MusicPlayer.playMusic("/sounds/menu.mp3");
            soundIcon.setImage(new Image(getClass().getResource("/images/home/sound-on.png").toExternalForm()));
        } else {
            MusicPlayer.stopMusic();
            soundIcon.setImage(new Image(getClass().getResource("/images/home/sound-off.png").toExternalForm()));
        }

        isMuted = !isMuted;
    }

    /**
     * Initialise le contrôleur après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        if (soundIcon != null) {
            soundIcon.setImage(new Image(getClass().getResource("/images/home/sound-on.png").toExternalForm()));
        }
    }
}