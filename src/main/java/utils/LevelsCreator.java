package utils;

import entities.*;
import ui.CollisionCalculator;
import ui.Constants;
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
        }
    }

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


    public static ArrayList<Entity> basicGrid() {
        ArrayList<Entity> staticEntities = new ArrayList<>();
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
        for (int i = 0; i <= 15; ++i) {
            int yMin = 0;
            int xMin = 0;
            int xMax = WINDOW_HEIGHT - TILE_SIZE;
            int yMax = WINDOW_HEIGHT;
            for (int j = 0; j <= 17; ++j) {
                staticEntities.add(new Wall(xMin, 0, false));
                staticEntities.add(new Wall(xMin, 610 - TILE_SIZE, false));
                staticEntities.add(new Wall(0, yMin, false));
                staticEntities.add(new Wall(705 - TILE_SIZE, yMin, false));
                xMin += TILE_SIZE;
                yMin += TILE_SIZE;
            }
        }
        return staticEntities;
    };

    public static Level generateRandomLevel(int levelNumber) {
        ArrayList<Entity> staticEntities = basicGrid();
        ArrayList<Entity> dynamicEntities = new ArrayList<>();

        Random rand = new Random();
        int triesMax = 100;

        // Placement des murs destructibles
        for (int i = 0; i < 100; i++) {
            int tries = 0;
            boolean placeLibre = false;
            int x = 0, y = 0;

            while (!placeLibre && tries < triesMax) {
                x = rand.nextInt(20) + 2;
                y = rand.nextInt(20) + 2;

                Wall newWall = new Wall(TILE_SIZE * x, TILE_SIZE * y, true);
                placeLibre = true;

                for (Entity e : staticEntities) {
                    if (CollisionCalculator.isColliding(newWall, e)) {
                        placeLibre = false;
                        break;
                    }
                }
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
                staticEntities.add(new Wall(TILE_SIZE * x, TILE_SIZE * y, true));
            } else {
                System.out.println("Impossible de placer un mur sans collision aprÃ¨s " + triesMax + " essais");
            }
        }

        int maxTilesX = 18;
        int maxTilesY = 13;

        for (int i = 0; i < 4 * levelNumber; i++) {
            double posX = Constants.TILE_SIZE * (rand.nextInt(maxTilesX) + 1.1);
            double posY = Constants.TILE_SIZE * (rand.nextInt(maxTilesY) + 1.1);
            dynamicEntities.add(new Monster(posX, posY));
        }

        return new Level(levelNumber, staticEntities, dynamicEntities);
    }
}

