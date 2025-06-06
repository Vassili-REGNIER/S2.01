package bombermanfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Game.fxml"));
        // Charge la police depuis le dossier resources
        Font.loadFont(getClass().getResourceAsStream("/fonts/Press_Start_2P/PressStart2P-Regular.ttf"), 10);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Super Bomberman FX");
        primaryStage.show();
        primaryStage.setFullScreen(true); // Plein écran complet (sans barre de titre)
        primaryStage.setFullScreenExitHint("Appuyez sur ÉCHAP pour quitter le plein écran"); // Message personnalisé
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
