package utils;

import entities.*;

import java.util.ArrayList;
import java.util.Random;

import static utils.Constants.TILE_SIZE;

/**
 * Classe utilitaire pour la création et la gestion des niveaux du jeu.
 *
 * <p>Cette classe gère la génération aléatoire des niveaux,
 * leur stockage dans une liste statique, ainsi que la gestion du niveau courant.</p>
 *
 * <p>Elle définit aussi des constantes pour les positions de départ des joueurs.</p>
 */
public class LevelsCreator {
    /**
     * Niveau courant en cours de jeu.
     */
    public static Level currentLevel;

    /**
     * Liste de tous les niveaux disponibles.
     */
    public static ArrayList<Level> levels;

    /**
     * Position X de départ du joueur 1.
     */
    public static final double PLAYER1_X = TILE_SIZE * 1.1;

    /**
     * Position Y de départ du joueur 1.
     */
    public static final double PLAYER1_Y = TILE_SIZE * 1.1;

    /**
     * Position X de départ du joueur 2.
     */
    public static final double PLAYER2_X = TILE_SIZE * 13.1;

    /**
     * Position Y de départ du joueur 2.
     */
    public static final double PLAYER2_Y = TILE_SIZE * 11.1;

    /**
     * Retourne le niveau courant.
     *
     * @return le niveau courant
     */
    public static Level getCurrentLevel() {
        System.out.println("getCurrentLevel() : " + currentLevel);
        return currentLevel;
    }

    /**
     * Définit le niveau courant à partir de son numéro.
     *
     * @param level numéro du niveau à définir comme courant
     */
    public static void setCurrentLevel(int level) {
        for (Level l : levels) {
            if (l.getLevel() == level) {
                currentLevel = l;
                System.out.println("setCurrentLevel() : " + currentLevel);
                return;
            }
        }
    }

    /**
     * Initialise la liste des niveaux avec des niveaux générés aléatoirement.
     * <p>
     * Crée trois niveaux, ajoute les joueurs avec leurs positions de départ,
     * puis définit le premier niveau comme niveau courant.
     * </p>
     */
    public static void initLevels() {
        levels = new ArrayList<>();

        Level level1 = generateRandomLevel(1);
        level1.addPlayer(new Player(Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 1.1, 1));

        Level level2 = generateRandomLevel(2);
        level2.addPlayer(new Player(Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 1.1));
        level2.addPlayer(new Player(Constants.TILE_SIZE * 13.1, Constants.TILE_SIZE * 11.1, 2));

        Level level3 = generateRandomLevel(3);
        level3.addPlayer(new Player(Constants.TILE_SIZE * 1.1, Constants.TILE_SIZE * 1.1));
        level3.addPlayer(new Player(Constants.TILE_SIZE * 13.1, Constants.TILE_SIZE * 11.1, 2));

        levels.add(level1);
        levels.add(level2);
        levels.add(level3);

        currentLevel = level1;

        System.out.println("Levels created");
        System.out.println("Current level: " + currentLevel);
    }

    /**
     * Génère une grille de base pour un niveau, avec les murs autour et une grille de murs centraux.
     *
     * @return liste des entités statiques représentant la grille de base du niveau
     */
    public static ArrayList<Entity> basicGrid() {
        ArrayList<Entity> staticEntities = new ArrayList<>();

        // Génération des murs centraux (grille)
        double width = TILE_SIZE;
        double height = TILE_SIZE;
        for (int i = 0; i <= 4; ++i) {
            int y = (int) height * 2;
            for (int j = 0; j <= 5; ++j) {
                int x = (int) width * 2;
                staticEntities.add(new Wall(x, y, false));
                width += TILE_SIZE;
            }
            width = TILE_SIZE;
            height += TILE_SIZE;
        }

        // Génération des murs aux bords (haut, bas, gauche, droite)
        for (int x = 0; x <= 17; x++) {
            int posX = x * TILE_SIZE;
            staticEntities.add(new Wall(posX, 0, false));                // bord haut
            staticEntities.add(new Wall(posX, 610 - TILE_SIZE, false));  // bord bas
        }

        for (int y = 0; y <= 15; y++) {
            int posY = y * TILE_SIZE;
            staticEntities.add(new Wall(0, posY, false));                // bord gauche
            staticEntities.add(new Wall(705 - TILE_SIZE, posY, false));  // bord droit
        }

        return staticEntities;
    }

    /**
     * Génère un niveau aléatoire avec un certain nombre de murs destructibles et de monstres.
     *
     * <p>Le niveau est généré en tentant plusieurs fois (maxAttempts)
     * de placer des murs et monstres aléatoirement sans collision.</p>
     *
     * @param levelNumber numéro du niveau à générer
     * @return un objet Level généré aléatoirement
     * @throws IllegalStateException si la génération échoue après plusieurs tentatives
     */
    public static Level generateRandomLevel(int levelNumber) {
        final int maxAttempts = 5;
        final int triesMax = 100;

        int totalDestructibleWalls = 15;
        int totalMonsters = 4 * levelNumber;
        int totalToPlace = totalDestructibleWalls + totalMonsters;

        int maxTilesX = (Constants.WINDOW_WIDTH - TILE_SIZE * 2) / TILE_SIZE;
        int maxTilesY = (Constants.WINDOW_HEIGHT - TILE_SIZE * 2) / TILE_SIZE;

        int attempt = 0;

        while (attempt < maxAttempts) {
            attempt++;

            ArrayList<Entity> staticEntities = basicGrid();
            ArrayList<Entity> dynamicEntities = new ArrayList<>();

            Random rand = new Random();
            int wallsPlaced = 0;
            int monstersPlaced = 0;
            int tries = 0;

            while ((wallsPlaced < totalDestructibleWalls || monstersPlaced < totalMonsters)
                    && tries < totalToPlace * triesMax) {

                int x = 1 + rand.nextInt(maxTilesX);
                int y = 1 + rand.nextInt(maxTilesY);
                double posX = x * TILE_SIZE;
                double posY = y * TILE_SIZE;

                boolean isOnPlayerStart =
                        (Math.abs(posX - PLAYER1_X) < TILE_SIZE && Math.abs(posY - PLAYER1_Y) < TILE_SIZE) ||
                                (Math.abs(posX - PLAYER2_X) < TILE_SIZE && Math.abs(posY - PLAYER2_Y) < TILE_SIZE);

                if (isOnPlayerStart) {
                    tries++;
                    continue;
                }

                Entity candidate = null;
                if (wallsPlaced < totalDestructibleWalls) {
                    candidate = new Wall(posX, posY, true);
                } else if (monstersPlaced < totalMonsters) {
                    candidate = new Monster(posX, posY);
                }

                boolean placeLibre = true;
                for (Entity e : staticEntities) {
                    if (CollisionCalculator.isColliding(candidate, e)) {
                        placeLibre = false;
                        break;
                    }
                }
                if (placeLibre) {
                    for (Entity e : dynamicEntities) {
                        if (CollisionCalculator.isColliding(candidate, e)) {
                            placeLibre = false;
                            break;
                        }
                    }
                }

                if (placeLibre) {
                    if (candidate instanceof Wall) {
                        staticEntities.add(candidate);
                        wallsPlaced++;
                    } else if (candidate instanceof Monster) {
                        dynamicEntities.add(candidate);
                        monstersPlaced++;
                    }
                }

                tries++;
            }

            if (monstersPlaced == totalMonsters) {
                System.out.println("✅ Niveau " + levelNumber + " généré avec " + monstersPlaced + " monstres.");
                return new Level(levelNumber, staticEntities, dynamicEntities);
            }

            System.out.println("⚠️ Tentative échouée #" + attempt + ", monstres générés : " + monstersPlaced);
        }

        throw new IllegalStateException("Impossible de générer un niveau valide après " + maxAttempts + " tentatives.");
    }
}
