package bombermanfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Accueil extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Accueil.fxml"));
        // Charge la police depuis le dossier resources
        //Font.loadFont(getClass().getResourceAsStream("/fonts/Pixelify_Sans/static/PixelifySans-Regular.ttf"), 10);

        // Chargez la police AVANT de créer la scène
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/Pixelify_Sans/static/PixelifySans-Regular.ttf"), 16);

        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        stage.setTitle("BombermanFX - Accueil");
        stage.setScene(scene);

        stage.setFullScreen(true); // Plein écran complet (sans barre de titre)
        stage.setFullScreenExitHint("Appuyez sur ÉCHAP pour quitter le plein écran"); // Message personnalisé
        stage.show();

        ImageView logo = (ImageView) scene.lookup("#logoImage");
        if (logo != null) {
            AnimationLogo.appliquerAnimation(logo);
        } else {
            System.out.println("⚠️ logoImage non trouvé dans le FXML !");
        }
       // Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/Pixelify_Sans/static/PixelifySans-Regular.ttf"), 10);
        //System.out.println("✅ Police chargée ? " + (font != null));

    }

    public static void main(String[] args) {
        launch();
    }
}
