package bombermanfx;

import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Classe utilitaire pour gérer l'animation du logo.
 */
public class AnimationLogo {

    /**
     * Applique une animation de pulsation (agrandissement/rétrécissement) en boucle au logo.
     *
     * @param logo L'ImageView représentant le logo.
     */
    public static void appliquerAnimation(ImageView logo) {
        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), logo);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setCycleCount(ScaleTransition.INDEFINITE);
        scale.setAutoReverse(true);
        scale.play();
    }
}
