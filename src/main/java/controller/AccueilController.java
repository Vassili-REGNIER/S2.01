package controller;

import bombermanfx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Contrôleur de la scène d'accueil de BombermanFX.
 */
public class AccueilController {

    /**
     * Méthode appelée lors du clic sur le bouton "Jouer".
     * Ferme la fenêtre d'accueil et ouvre la fenêtre principale du jeu.
     *
     * @param event L'événement déclenché par le clic sur le bouton.
     */
    @FXML
    private void onJouerClicked(ActionEvent event) {
        // Fermer la fenêtre d'accueil
        Stage accueilStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        accueilStage.close();

        // Lancer la fenêtre principale du jeu
        try {
            Main jeu = new Main();
            Stage gameStage = new Stage();
            jeu.start(gameStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
