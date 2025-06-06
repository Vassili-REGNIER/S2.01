package controller;

import bombermanfx.MusicPlayer;
import bombermanfx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Contrôleur pour la scène d'accueil de BombermanFX.
 * Gère les interactions utilisateur sur la page d'accueil, y compris
 * la gestion du son, la navigation entre menus, et le lancement du jeu.
 */
public class AccueilController {

    @FXML
    private Button soundToggleButton;

    @FXML
    private ImageView soundIcon;

    @FXML
    private VBox mainMenu;

    @FXML
    private VBox multiplayerMenu;

    private boolean isMuted = false;

    /**
     * Méthode appelée lors du clic sur le bouton "Jouer".
     * Ferme la fenêtre d'accueil et lance la scène principale du jeu.
     *
     * @param event L'événement déclenché par le clic.
     */
    @FXML
    public void onJouerClicked(ActionEvent event) {
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();

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
     * Méthode appelée lorsque l'utilisateur clique sur "Multiplayer".
     * Affiche le sous-menu multijoueur et masque le menu principal.
     */
    @FXML
    public void onMultiplayerClicked() {
        mainMenu.setVisible(false);
        mainMenu.setManaged(false);
        multiplayerMenu.setVisible(true);
        multiplayerMenu.setManaged(true);
    }

    /**
     * Méthode appelée lorsqu'on clique sur "Retour" dans le sous-menu.
     * Revient au menu principal depuis le menu multijoueur.
     */
    @FXML
    public void onBackToMainMenu() {
        multiplayerMenu.setVisible(false);
        multiplayerMenu.setManaged(false);
        mainMenu.setVisible(true);
        mainMenu.setManaged(true);
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton "1v1".
     * À implémenter : démarre une partie multijoueur en local.
     */
    @FXML
    public void on1v1Clicked() {
        System.out.println("Mode multijoueur 1v1 sélectionné");
        // TODO: Logique du mode 1v1
    }

    /**
     * Méthode appelée lorsqu'on clique sur "Contre IA" dans le menu multijoueur.
     * À implémenter : démarre une partie contre l'ordinateur.
     */
    @FXML
    public void onVsAIInMultiplayerClicked() {
        System.out.println("Mode multijoueur contre IA sélectionné");
        // TODO: Logique du mode contre IA
    }

    /**
     * Permet de quitter l'application via un bouton dans le menu principal.
     *
     * @param event L'événement de clic sur le bouton Quitter.
     */
    @FXML
    private void onQuitterClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        MusicPlayer.stopMusic();
    }

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Initialise l'icône sonore et démarre la musique de fond.
     */
    @FXML
    public void initialize() {
        if (soundIcon != null) {
            soundIcon.setImage(new Image(getClass().getResource("/images/home/sound-on.png").toExternalForm()));
        }
        MusicPlayer.playMusic("/sounds/menu.mp3");

        // Afficher uniquement le menu principal au départ
        if (mainMenu != null && multiplayerMenu != null) {
            mainMenu.setVisible(true);
            mainMenu.setManaged(true);
            multiplayerMenu.setVisible(false);
            multiplayerMenu.setManaged(false);
        }
    }
}
