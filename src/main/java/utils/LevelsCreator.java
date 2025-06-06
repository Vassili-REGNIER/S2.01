package utils;

import entities.Entity;
import entities.Wall;
import ui.CollisionCalculator;
import ui.Level;

import java.util.ArrayList;
import java.util.Random;

import static ui.Constants.TILE_SIZE;
import static ui.Constants.WINDOW_HEIGHT;

public class LevelsCreator {
    public static Level currentLevel;

    public static ArrayList<Level> levels;

    public static Level getCurrentLevel() {

        System.out.println("getCurrentLevel() : " + currentLevel);
        return currentLevel;
    }

    public static void setCurrentLevel(int level) {
        for (Level l : levels) {
            if (l.getLevel() == level) {
                currentLevel = l;
                System.out.println("setCurrentLevel() : " + currentLevel);
                return;
            }
        currentLevel = levels.getFirst();
        System.out.println("setCurrentLevel() : " + currentLevel);
        }
    }

    public static void initLevels() {
        levels = new ArrayList<>();

        ArrayList<Entity> staticEntitiesL1 = new ArrayList<>();  // murs, blocs
        ArrayList<Entity> dynamicEntitiesL1 = new ArrayList<>(); // bombes, explosions, ennemis, etc.

        staticEntitiesL1.add(new Wall(200, 200));

        Level level1 = new Level(1, staticEntitiesL1, dynamicEntitiesL1);

        ArrayList<Entity> staticEntitiesL2 = new ArrayList<>();  // murs, blocs
        ArrayList<Entity> dynamicEntitiesL2 = new ArrayList<>(); // bombes, explosions, ennemis, etc.

        staticEntitiesL2.add(new Wall(0, 0));
        staticEntitiesL2.add(new Wall(200, 200));

        Level level2 = new Level(2, staticEntitiesL2, dynamicEntitiesL2);



        levels.add(level1);
        levels.add(level2);
        levels.add(getRandomLevel(3));

        currentLevel = level1;

        System.out.println("Levels created");
        System.out.println("Current level: " + currentLevel);
    }


    public static ArrayList<Entity> basicGrid() {
        ArrayList<Entity> staticEntities = new ArrayList<>();
        double width = TILE_SIZE;
        double height = TILE_SIZE;
        for (int i = 0; i <= 4; ++i) {
            int y = (int) height*2;
            for (int j = 0; j <= 5; ++j) {
                int x = (int) width * 2;
                staticEntities.add(new Wall(x, y, false));
                width += TILE_SIZE;
            }
            width = TILE_SIZE;
            height += TILE_SIZE;
        }
        for (int i = 0; i <= 15; ++i) {
            int yMin = 0;
            int xMin = 0;
            int xMax = WINDOW_HEIGHT-TILE_SIZE;
            int yMax = WINDOW_HEIGHT;
            for (int j = 0; j <= 17; ++j) {
                staticEntities.add(new Wall(xMin, 0, false));
                staticEntities.add(new Wall(xMin, 610-TILE_SIZE, false));
                staticEntities.add(new Wall(0, yMin, false));
                staticEntities.add(new Wall(705-TILE_SIZE, yMin, false));
                xMin += TILE_SIZE;
                yMin += TILE_SIZE;
            }
        }
        return staticEntities;
    };

    public static Level getRandomLevel (int levelNumber) {
        ArrayList<Entity> staticEntities = basicGrid();
        ArrayList<Entity> dynamicEntities = new ArrayList<>();

        Random rand = new Random();
        int triesMax = 100; // nombre max d'essais pour trouver une place libre

        for (int i = 0; i < 100; i++) {
            int tries = 0;
            boolean placeLibre = false;
            int x = 0, y = 0;

            while (!placeLibre && tries < triesMax) {
                x = rand.nextInt(20) + 2;
                y = rand.nextInt(20) + 2;

                Wall newWall = new Wall(TILE_SIZE * x, TILE_SIZE * y, true);

                placeLibre = true; // on suppose que la place est libre

                // Vérifie collisions avec staticEntities
                for (Entity e : staticEntities) {
                    if (CollisionCalculator.isColliding(newWall, e)) {
                        placeLibre = false;
                        break;
                    }
                }
                // Vérifie collisions avec dynamicEntities (murs déjà ajoutés)
                if (placeLibre) {
                    for (Entity e : dynamicEntities) {
                        if (CollisionCalculator.isColliding(newWall, e)) {
                            placeLibre = false;
                            break;
                        }
                    }
                }
                tries++;
            }

            if (placeLibre) {
                dynamicEntities.add(new Wall(TILE_SIZE * x, TILE_SIZE * y, true));
            } else {
                System.out.println("Impossible de placer un mur sans collision après " + triesMax + " essais");
            }
        }
        Level level = new Level(levelNumber, staticEntities, dynamicEntities);
        return level;
    }
}

