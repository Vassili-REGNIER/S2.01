package utils;

import entities.Entity;
import entities.Player;

import java.util.ArrayList;

/**
 * Représente un niveau du jeu.
 *
 * <p>Un niveau contient :</p>
 * <ul>
 *     <li>Un numéro d'identification {@code level}.</li>
 *     <li>Une liste d'entités statiques (murs, blocs, etc.).</li>
 *     <li>Une liste d'entités dynamiques (bombes, explosions, ennemis, etc.).</li>
 *     <li>Une liste des joueurs présents dans ce niveau.</li>
 * </ul>
 *
 * <p>Cette classe permet aussi de réinitialiser le niveau, en régénérant les entités et en repositionnant les joueurs.</p>
 */
public class Level {
    private int level;
    private ArrayList<Entity> staticEntities; // murs, blocs
    private ArrayList<Entity> dynamicEntities; // bombes, explosions, ennemis, etc.
    private ArrayList<Player> players;

    /**
     * Constructeur principal du niveau.
     *
     * @param level numéro du niveau
     * @param staticEntities liste des entités statiques du niveau
     * @param dynamicEntities liste des entités dynamiques du niveau
     */
    public Level(int level, ArrayList<Entity> staticEntities, ArrayList<Entity> dynamicEntities) {
        this.level = level;
        this.staticEntities = staticEntities;
        this.dynamicEntities = dynamicEntities;
        this.players = new ArrayList<>();
    }

    /**
     * Constructeur pour créer un niveau vide avec un numéro donné.
     *
     * @param level numéro du niveau
     */
    public Level(int level) {
        this.level = level;
        this.staticEntities = new ArrayList<>();
        this.dynamicEntities = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    /**
     * Obtient la liste des joueurs présents dans ce niveau.
     *
     * @return liste des joueurs
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Obtient le nombre de joueurs dans ce niveau.
     *
     * @return nombre de joueurs
     */
    public int getNbJoueur() {
        return players.size();
    }

    /**
     * Ajoute un joueur à ce niveau.
     *
     * @param player joueur à ajouter
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * Définit la liste des joueurs du niveau.
     *
     * @param players nouvelle liste de joueurs
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Obtient le numéro du niveau.
     *
     * @return numéro du niveau
     */
    public int getLevel() {
        return level;
    }

    /**
     * Définit le numéro du niveau.
     *
     * @param level nouveau numéro du niveau
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Obtient la liste des entités statiques du niveau.
     *
     * @return liste des entités statiques
     */
    public ArrayList<Entity> getStaticEntities() {
        return staticEntities;
    }

    /**
     * Définit la liste des entités statiques du niveau.
     *
     * @param staticEntities nouvelle liste des entités statiques
     */
    public void setStaticEntities(ArrayList<Entity> staticEntities) {
        this.staticEntities = staticEntities;
    }

    /**
     * Obtient la liste des entités dynamiques du niveau.
     *
     * @return liste des entités dynamiques
     */
    public ArrayList<Entity> getDynamicEntities() {
        return dynamicEntities;
    }

    /**
     * Définit la liste des entités dynamiques du niveau.
     *
     * @param dynamicEntities nouvelle liste des entités dynamiques
     */
    public void setDynamicEntities(ArrayList<Entity> dynamicEntities) {
        this.dynamicEntities = dynamicEntities;
    }

    /**
     * Réinitialise le niveau.
     * <p>
     * Cette méthode régénère le niveau en recréant aléatoirement les entités
     * statiques et dynamiques via {@link utils.LevelsCreator}, puis repositionne
     * tous les joueurs à leur position initiale.
     * </p>
     */
    public void reset() {
        // Génère un nouveau niveau aléatoire avec le même numéro de niveau
        Level regenerated = LevelsCreator.generateRandomLevel(this.level);

        // Met à jour les entités statiques et dynamiques avec celles du nouveau niveau
        this.staticEntities = regenerated.getStaticEntities();
        this.dynamicEntities = regenerated.getDynamicEntities();

        // Réinitialise les joueurs existants (et les replace à leurs positions initiales)
        for (Player player : this.players) {
            player.reset(); // repositionne et nettoie le joueur
        }

        // Repositionne les joueurs dans la liste (important si leurs références sont externes)
        regenerated.setPlayers(this.players);
    }
}
