package com.paragon.client.managers.social;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfsurge
 */
public class SocialManager {

    public final List<Player> players = new ArrayList<>();

    public SocialManager() {

    }

    /**
     * Checks if a given player name is a friend
     * @param name The name to check
     * @return If the given player is a friend
     */
    public boolean isFriend(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) && player.getRelationship() == Relationship.FRIEND) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a given player name is neutral
     * @param name The name to check
     * @return If the given player is neutral
     */
    public boolean isNeutral(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) && player.getRelationship() == Relationship.NEUTRAL) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a given player name is an enemy
     * @param name The name to check
     * @return If the given player is an enemy
     */
    public boolean isEnemy(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) && player.getRelationship() == Relationship.ENEMY) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a player to our players list
     * @param player The player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

}
