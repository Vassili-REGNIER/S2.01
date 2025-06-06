package bombermanfx;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Classe utilitaire pour gérer la lecture de musique dans l'application.
 */
public class MusicPlayer {

    /** Instance unique du MediaPlayer pour éviter les superpositions de sons. */
    private static MediaPlayer mediaPlayer;

    /**
     * Lance la lecture de la musique spécifiée, en boucle infinie.
     * Si une musique est déjà en cours, elle sera arrêtée et remplacée.
     *
     * @param resourcePath Chemin relatif vers le fichier audio (ex: "/sounds/menu.mp3").
     */
    public static void playMusic(String resourcePath) {
        // Ne relance pas si la même musique est déjà en cours
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }

        // Arrête la musique précédente
        stopMusic();

        URL resource = MusicPlayer.class.getResource(resourcePath);
        if (resource == null) {
            System.out.println("❌ Fichier audio introuvable : " + resourcePath);
            return;
        }

        try {
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lecture en boucle
            mediaPlayer.setVolume(0.5); // Volume moyen (0.0 à 1.0)
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("⚠️ Erreur lors de la lecture audio : " + e.getMessage());
        }
    }

    /**
     * Arrête la lecture de la musique si elle est en cours.
     */
    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}