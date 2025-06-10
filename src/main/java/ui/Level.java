package ui;

import entities.Entity;
import entities.Player;

import java.util.ArrayList;

public class Level {
    private int level;
    private ArrayList<Entity> staticEntities; // murs, blocs
    private ArrayList<Entity> dynamicEntities; // bombes, explosions, ennemis, etc.
    private ArrayList<Player> players;

    public Level(int level, ArrayList<Entity> staticEntities, ArrayList<Entity> dynamicEntities) {
        this.level = level;
        this.staticEntities = staticEntities;
        this.dynamicEntities = dynamicEntities;
        this.players = new ArrayList<>();
    }

    public Level(int level) {
        this.level = level;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNbJoueur() {
        return players.size();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Entity> getStaticEntities() {
        return staticEntities;
    }

    public void setStaticEntities(ArrayList<Entity> staticEntities) {
        this.staticEntities = staticEntities;
    }

    public ArrayList<Entity> getDynamicEntities() {
        return dynamicEntities;
    }

    public void setDynamicEntities(ArrayList<Entity> dynamicEntities) {
        this.dynamicEntities = dynamicEntities;
    }
}
