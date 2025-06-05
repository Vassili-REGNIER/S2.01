package controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import bombermanfx.Main;

/**
 * Contrôleur pour la scène d'accueil de BombermanFX.
 * Gère les interactions utilisateur sur la page d'accueil.
 */
public class AccueilController {

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

        // Lancer la scène principale via Main
        try {
            Main jeu = new Main();
            Stage stageJeu = new Stage();
            jeu.start(stageJeu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
