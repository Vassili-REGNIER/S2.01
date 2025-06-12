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
import utils.LevelsCreator;

/**
 * Contrôleur de la vue d'accueil de BombermanFX.
 * <p>
 * Gère les interactions avec l'interface utilisateur sur le menu principal :
 * gestion du son, navigation entre menus (principal / multijoueur),
 * lancement du jeu en différents modes, et fermeture de l'application.
 */
public class AccueilController {

    /** Bouton pour activer/désactiver le son. */
    @FXML
    private Button soundToggleButton;

    /** Icône indiquant l'état sonore (activé/désactivé). */
    @FXML
    private ImageView soundIcon;

    /** Conteneur du menu principal. */
    @FXML
    private VBox mainMenu;

    /** Conteneur du menu multijoueur. */
    @FXML
    private VBox multiplayerMenu;

    /** Bouton pour changer le thème (non implémenté ici). */
    @FXML
    private Button themeButton;

    /** Conteneur racine de la scène. */
    @FXML
    private VBox root;

    /** Indique si la musique est actuellement coupée. */
    private boolean isMuted = false;

    /**
     * Inverse l'état sonore : coupe ou active la musique, et change l'icône en conséquence.
     */
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

    /**
     * Affiche le menu multijoueur et masque le menu principal.
     */
    @FXML
    public void onMultiplayerClicked() {
        mainMenu.setVisible(false);
        mainMenu.setManaged(false);
        multiplayerMenu.setVisible(true);
        multiplayerMenu.setManaged(true);
    }

    /**
     * Retourne au menu principal depuis le menu multijoueur.
     */
    @FXML
    public void onBackToMainMenu() {
        multiplayerMenu.setVisible(false);
        multiplayerMenu.setManaged(false);
        mainMenu.setVisible(true);
        mainMenu.setManaged(true);
    }

    /**
     * Ferme l'application et arrête la musique.
     *
     * @param event Événement déclenché par le clic sur le bouton "Quitter".
     */
    @FXML
    private void onQuitterClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        MusicPlayer.stopMusic();
    }

    /**
     * Initialise les éléments de l'interface :
     * - Affiche le menu principal
     * - Lance la musique de fond
     * - Initialise les niveaux.
     */
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
     * Lance une partie solo.
     *
     * @param event Événement déclenché par le clic sur le bouton "Jouer".
     */
    @FXML
    public void lancerSolo(ActionEvent event) {
        LevelsCreator.setCurrentLevel(1);
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();
        lancerJeuMode(event);
    }

    /**
     * Lance une partie multijoueur (1 contre 1).
     *
     * @param event Événement déclenché par le clic sur le bouton correspondant.
     */
    @FXML
    public void lancer1v1(ActionEvent event) {
        LevelsCreator.setCurrentLevel(2);
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        MusicPlayer.stopMusic();
        lancerJeuMode(event);
    }

    /**
     * Lance une partie contre une IA.
     *
     * @param event Événement déclenché par le clic sur le bouton correspondant.
     */
    @FXML
    public void lancer1vMachine(ActionEvent event) {
        LevelsCreator.setCurrentLevel(3);
        Stage stageAccueil = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageAccueil.close();
        stageAccueil.setFullScreen(true);
        MusicPlayer.stopMusic();
        lancerJeuMode(event);
    }

    /**
     * Lance la scène principale du jeu.
     *
     * @param event Événement déclenché par l'utilisateur.
     */
    @FXML
    public void lancerJeuMode(ActionEvent event) {
        try {
            Main jeu = new Main();
            Stage stageJeu = new Stage();
            stageJeu.setFullScreen(true);
            jeu.start(stageJeu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
