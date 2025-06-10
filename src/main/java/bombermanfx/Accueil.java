package bombermanfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**


 Classe principale de l'application BombermanFX qui gère l'écran d'accueil.*/

public class Accueil extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Chargement du fichier FXML d'accueil
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Accueil.fxml"));
        Parent root = fxmlLoader.load();

        // Création de la scène avec les dimensions spécifiées
        scene = new Scene(root, 1200, 800);

        // Ne plus gérer le bouton thème ici, c'est fait dans le controller

        // Configuration de la fenêtre principale
        stage.setTitle("BombermanFX - Accueil");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("Appuyez sur ÉCHAP pour quitter le plein écran");
        stage.show();

        // Recherche et animation du logo d'accueil
        ImageView logo = (ImageView) scene.lookup("#logoImage");
        if (logo != null) {
            AnimationLogo.appliquerAnimation(logo);
        } else {
            System.out.println("⚠️ logoImage non trouvé dans le FXML !");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}