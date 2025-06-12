package utils;

import entities.Entity;

/**
 * Classe utilitaire fournissant des méthodes pour calculer les collisions entre entités.
 */
public class CollisionCalculator {

    /**
     * Détermine si deux entités sont en collision.
     *
     * @param a La première entité.
     * @param b La deuxième entité.
     * @return {@code true} si les deux entités se chevauchent, {@code false} sinon.
     *
     * <p>La méthode utilise une détection de collision par boîte englobante (axis-aligned bounding box, AABB).</p>
     */
    public static boolean isColliding(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getSize() &&
                a.getX() + a.getSize() > b.getX() &&
                a.getY() < b.getY() + b.getSize() &&
                a.getY() + a.getSize() > b.getY();
    }
}
