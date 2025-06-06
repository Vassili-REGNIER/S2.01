package bombermanfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe principale pour le lancement de l'application BombermanFX.
 * Elle initialise l'écran d'accueil, applique les polices personnalisées,
 * déclenche les animations et lance la musique de fond.
 */
public class Accueil extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Charger la police personnalisée
        Font.loadFont(getClass().getResourceAsStream("/fonts/Press_Start_2P/PressStart2P-Regular.ttf"), 16);

        // Charger le FXML de l'accueil
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Accueil.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        stage.setTitle("BombermanFX - Accueil");
        stage.setScene(scene);
        stage.show();

        // Appliquer une animation au logo si présent
        ImageView logo = (ImageView) scene.lookup("#logoImage");
        if (logo != null) {
            AnimationLogo.appliquerAnimation(logo);
        } else {
            System.out.println("⚠️ logoImage non trouvé dans le FXML !");
        }

        // Lancer la musique de fond en boucle
        MusicPlayer.playMusic("/sounds/menu.mp3");

        // Ajouter un bouton pour arrêter la musique
        HBox musicContainer = (HBox) scene.lookup("#musicButtonContainer");
        if (musicContainer != null) {
            ImageView stopIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home/sound-off.png")));
            stopIcon.setFitWidth(32);
            stopIcon.setFitHeight(32);

            Button stopMusicButton = new Button();
            stopMusicButton.setGraphic(stopIcon);
            stopMusicButton.setStyle("-fx-background-color: transparent;");
            stopMusicButton.setOnAction(e -> MusicPlayer.stopMusic());

            musicContainer.getChildren().add(stopMusicButton);
        } else {
            System.out.println("⚠️ musicButtonContainer non trouvé dans le FXML !");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
