package ui;

import entities.Entity;

public class CollisionCalculator {
    public static boolean isColliding(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getSize() &&
                a.getX() + a.getSize() > b.getX() &&
                a.getY() < b.getY() + b.getSize() &&
                a.getY() + a.getSize() > b.getY();
    }
}