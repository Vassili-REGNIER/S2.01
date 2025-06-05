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
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 16);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        stage.setTitle("BombermanFX - Accueil");
        stage.setScene(scene);
        stage.show();

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
