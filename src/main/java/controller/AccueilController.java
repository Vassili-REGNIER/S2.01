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
    private boolean isDarkTheme = false;

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
    public void on1v1Clicked() {
        System.out.println("Mode multijoueur 1v1 sélectionné");
    }

    @FXML
    public void onVsAIInMultiplayerClicked() {
        System.out.println("Mode multijoueur contre IA sélectionné");
    }

    @FXML
    private void onQuitterClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        MusicPlayer.stopMusic();
    }

    @FXML
    private void switchTheme() {
        isDarkTheme = !isDarkTheme;

        Scene scene = root.getScene();
        if (scene == null) {
            System.err.println("Scene non encore initialisée");
            return;
        }

        scene.getStylesheets().clear();

        if (isDarkTheme) {
            scene.getStylesheets().add(getClass().getResource("/style/style1.css").toExternalForm());
            themeButton.setText("Mode Clair");
        } else {
            scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
            themeButton.setText("Mode Sombre");
        }
    }

    @FXML
    public void initialize() {
        if (soundIcon != null) {
            soundIcon.setImage(new Image(getClass().getResource("/images/home/sound-on.png").toExternalForm()));
        }
        MusicPlayer.playMusic("/sounds/menu.mp3");

        mainMenu.setVisible(true);
        mainMenu.setManaged(true);
        multiplayerMenu.setVisible(false);
        multiplayerMenu.setManaged(false);

        // Ajout d'un listener sur la scène pour s'assurer qu'elle est prête avant d'ajouter les styles
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().clear();
                newScene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
            }
        });

        if (root.getScene() != null) {
            root.getScene().getStylesheets().clear();
            root.getScene().getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        }

    }
}
